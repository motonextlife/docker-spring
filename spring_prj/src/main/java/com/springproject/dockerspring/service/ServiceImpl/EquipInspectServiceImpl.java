package com.springproject.dockerspring.service.ServiceImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.springproject.dockerspring.component.CsvProcess.Equip_Inspect_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Equip_Inspect_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_History;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;
import com.springproject.dockerspring.form.CsvImplForm.EquipInspectForm;
import com.springproject.dockerspring.repository.HistoryRepo.EquipInspHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.EquipInspRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.ListOrderEnum;
import com.springproject.dockerspring.service.BlobServiceInterface.EquipInspPhotoService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.EquipInspectService;
import com.springproject.dockerspring.service.ServiceInterface.FacilityService;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [EquipInspectServiceImpl]
/*   「点検管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class EquipInspectServiceImpl extends GetLoginUser implements EquipInspectService{

  @Autowired
  private EquipInspRepo equipinsp_repo;
  
  @Autowired
  private EquipInspHistRepo equipinsphist_repo;

  @Autowired
  private Equip_Inspect_Pdf equipinsp_pdf;

  @Autowired
  private Equip_Inspect_Csv equipinsp_csv;

  @Autowired
  private EquipInspectForm equipinsp_form;
  
  @Autowired
	private FacilityService faciserv;
  
  @Autowired
  private InspectItemsService inspectitemsserv;

  @Autowired
  private EquipInspPhotoService photoserv;






  /*************************************************************/
  /*   [selectList]
  /*   入力された設備番号に対応する情報をリストで取得する。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect> selectList(String faci_id, ListOrderEnum order) {

    List<Equip_Inspect> list = (List<Equip_Inspect>)equipinsp_repo.findList(faci_id);
    
    switch(order){
      case UPLOAD_DATE_ASC:
        Collections.sort(list, (a, b) -> a.getInspect_date().toString().compareTo(b.getInspect_date().toString()));
        break;
      case UPLOAD_DATE_DESC:
        Collections.sort(list, (a, b) -> b.getInspect_date().toString().compareTo(a.getInspect_date().toString()));
        break;
      case ID_ASC:
        Collections.sort(list, (a, b) -> a.getInspect_id().compareTo(b.getInspect_id().toString()));
        break;
      case ID_DESC:
        Collections.sort(list, (a, b) -> b.getInspect_id().compareTo(a.getInspect_id().toString()));
        break;
      case SHEET_ASC:
        Collections.sort(list, (a, b) -> a.getInspsheet_id().compareTo(b.getInspsheet_id().toString()));
        break;
      case SHEET_DESC:
        Collections.sort(list, (a, b) -> b.getInspsheet_id().compareTo(a.getInspsheet_id().toString()));
        break;
    }

    return list;
  }





  /*************************************************************/
  /*   [selectData]
  /*   点検番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Equip_Inspect> selectData(String inspect_id) {
    return equipinsp_repo.findByInspect_id(inspect_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと点検番号を用いて、
  /*   点検番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String inspect_id) {

    if(equipinsp_repo.existsById(serial_num) && existsData(inspect_id)){

      List<Equip_Inspect> list = (ArrayList<Equip_Inspect>)equipinsp_repo.findAll();
      
      try{
        //対象は、該当シリアルナンバー「以外」の点検番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getInspect_id())
                   .noneMatch(s -> s.equals(inspect_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [EquipInspectServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の点検番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String inspect_id) {
    return equipinsp_repo.findByInspect_id(inspect_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された点検情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Equip_Inspect changeData(Equip_Inspect entity) throws DbActionException {

    try{
      entity = equipinsp_repo.save(entity);

      //履歴情報を必ず残す。
      Equip_Inspect_History equipinsphist = new Equip_Inspect_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      equipinsphist_repo.save(equipinsphist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [EquipInspectServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した点検情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect> selectAllData(String word, String subject, Boolean order) {
    return equipinsp_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   点検番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect_History> selectHist(String inspect_id, Date start_datetime, Date end_datetime) {
    return equipinsphist_repo.findAllByDateBetween(inspect_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Equip_Inspect_History equipinsphist = equipinsphist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の点検番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(equipinsphist.getInspect_id())){
      equipinsphist.setSerial_num(null);

      /*************************************************************/
      /*   該当点検番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当点検番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(equipinsphist.getSerial_num(), equipinsphist.getInspect_id())){
      Equip_Inspect latest = equipinsp_repo.findByInspect_id(equipinsphist.getInspect_id()).get();
      equipinsphist.setSerial_num(latest.getSerial_num());
    }

    //参照整合性チェック。
    foreignId(equipinsphist);

    try{
      Equip_Inspect equipinsp = new Equip_Inspect(equipinsphist);
      equipinsp = equipinsp_repo.save(equipinsp);

      equipinsphist = new Equip_Inspect_History(equipinsp, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      equipinsphist_repo.save(equipinsphist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [EquipInspectServiceImpl:rollbackData]");
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Equip_Inspect entity) {
    return entity.makeMap();
  }

  @Override
  public Map<String, String> parseStringH(Equip_Inspect_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Equip_Inspect> datalist) {

    List<Equip_Inspect> list = (ArrayList<Equip_Inspect>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Equip_Inspect_History> datalist) {

    List<Equip_Inspect_History> list = (ArrayList<Equip_Inspect_History>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseStringH(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }






  /*************************************************************/
  /*   [outputPdf]
  /*   指定された シリアルナンバーのデータをPDFシートとして出力する。
  /*************************************************************/
  @Override
  public String outputPdf(Integer serial_num) throws IOException {
    Optional<Equip_Inspect> entity = equipinsp_repo.findById(serial_num);

    if(entity.isPresent()){
      return equipinsp_pdf.makePdf(entity.get());
    }else{
      return null;
    }
  }






  /*************************************************************/
  /*   [inputCsv]
  /*   CSVシートに書かれたデータを取り込み、データベースに登録する。
  /*************************************************************/
  @Override
  public List<List<Integer>> inputCsv(MultipartFile file) 
      throws IOException, InputFileDifferException, ExecutionException, InterruptedException, ParseException, DbActionException {

    List<Map<String, String>> maplist = equipinsp_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = equipinsp_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Equip_Inspect> entities = equipinsp_csv.csvToEntitys(maplist);

      try{
        entities = equipinsp_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Equip_Inspect> equiplist = (ArrayList<Equip_Inspect>)entities;
        List<Equip_Inspect_History> hist_list = equiplist.stream()
                                                         .parallel()
                                                         .map(s -> new Equip_Inspect_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                         .collect(Collectors.toList());
        
        equipinsphist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [EquipInspectServiceImpl:inputCsv]");
      }

    }else{
      return errorlist;
    }
  }






  /*************************************************************/
  /*   [outputCsv]
  /*   渡された点検情報リスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Equip_Inspect> datalist) throws IOException {
    return equipinsp_csv.outputCsv(datalist);
  }






  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return equipinsp_csv.outputTemplate();
  }





  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  @Override
  public void foreignId(Equip_Inspect_History entity) throws ForeignMissException {

    if(!faciserv.existsData(entity.getFaci_id()) || !inspectitemsserv.existsData(entity.getInspsheet_id())){
      throw new ForeignMissException();
    }
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Equip_Inspect> opt = equipinsp_repo.findById(serial_num);

    if(opt.isPresent()){
      Equip_Inspect entity = opt.get();

      try{
        photoserv.deleteAllData(entity.getInspect_id());

        equipinsp_repo.delete(entity);

        //履歴情報を必ず残す。
        Equip_Inspect_History hist = new Equip_Inspect_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        equipinsphist_repo.save(hist);
        
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [EquipInspectServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}