package com.springproject.dockerspring.service.ServiceImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.springproject.dockerspring.component.CsvProcess.Facility_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Facility_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.entity.NormalEntity.Facility;
import com.springproject.dockerspring.form.CsvImplForm.FacilityForm;
import com.springproject.dockerspring.repository.HistoryRepo.FaciHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.EquipInspRepo;
import com.springproject.dockerspring.repository.NormalRepo.FaciRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.FacilityService;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [FacilityServiceImpl]
/*   「設備管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class FacilityServiceImpl extends GetLoginUser implements FacilityService{

  @Autowired
  private FaciRepo faci_repo;

  @Autowired
  private FaciHistRepo facihist_repo;

  @Autowired
  private Facility_Pdf faci_pdf;
  
  @Autowired
  private Facility_Csv faci_csv;

  @Autowired
  private FacilityForm faci_form;

  @Autowired
  private MemberInfoService faciserv;

  @Autowired
  private EquipInspRepo equipinsp_repo;





  /*************************************************************/
  /*   [selectData]
  /*   設備番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Facility> selectData(String faci_id) {
    return faci_repo.findByFaci_Id(faci_id);
  }





  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと団員番号を用いて、
  /*   団員番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String faci_id) {

    if(faci_repo.existsById(serial_num) && existsData(faci_id)){

      List<Facility> list = (ArrayList<Facility>)faci_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の設備番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getFaci_id())
                   .noneMatch(s -> s.equals(faci_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [FacilityServiceImpl:duplicateData]");
      }
    }

    return false;
  }





  /*************************************************************/
  /*   [existsData]
  /*   該当の設備番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String faci_id) {
    return faci_repo.findByFaci_Id(faci_id).isPresent();
  }





  /*************************************************************/
  /*   [changeData]
  /*   渡された設備情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Facility changeData(Facility entity) throws DbActionException {

    try{
      entity = faci_repo.save(entity);
    
      //履歴情報を必ず残す。
      Facility_History facihist = new Facility_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      facihist_repo.save(facihist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [FacilityServiceImpl:changeData]");
    }
  }





  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した設備情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Facility> selectAllData(String word, String subject, Boolean order) {
    return faci_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   団員番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Facility_History> selectHist(String faci_id, Date start_datetime, Date end_datetime) {
    return facihist_repo.findAllByDateBetween(faci_id, start_datetime, end_datetime);
  }





  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Facility_History facihist = facihist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の設備番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(facihist.getFaci_id())){
      facihist.setSerial_num(null);

      /*************************************************************/
      /*   該当設備番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当設備番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(facihist.getSerial_num(), facihist.getFaci_id())){
      Facility latest = faci_repo.findByFaci_Id(facihist.getFaci_id()).get();
      facihist.setSerial_num(latest.getSerial_num());
    }

    //参照整合性チェック。
    foreignId(facihist);

    try{
      Facility faci = new Facility(facihist);
      faci = faci_repo.save(faci);

      facihist = new Facility_History(faci, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      facihist_repo.save(facihist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [FacilityServiceImpl:rollbackData]");
    }
  }





  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Facility entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Facility_History entity) {
    return entity.makeMap();
  }





  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Facility> datalist) {

    List<Facility> list = (ArrayList<Facility>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Facility_History> datalist) {

    List<Facility_History> list = (ArrayList<Facility_History>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseStringH(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }





  /*************************************************************/
  /*   [outputPdf]
  /*   指定されたシリアルナンバーのデータをPDFシートとして出力する。
  /*************************************************************/
  @Override
  public String outputPdf(Integer serial_num) throws IOException {
    Optional<Facility> entity = faci_repo.findById(serial_num);

    if(entity.isPresent()){
      return faci_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = faci_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = faci_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Facility> entities = faci_csv.csvToEntitys(maplist);

      try{
        entities = faci_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Facility> facilist = (ArrayList<Facility>)entities;
        List<Facility_History> hist_list = facilist.stream()
                                                   .parallel()
                                                   .map(s -> new Facility_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                   .collect(Collectors.toList());
        
        facihist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [FacilityServiceImpl:inputCsv]");
      }

    }else{
      return errorlist;
    }
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   渡された設備情報リスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Facility> datalist) throws IOException {
    return faci_csv.outputCsv(datalist);
  }





  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return faci_csv.outputTemplate();
  }





  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  @Override
  public Boolean foreignId(Facility_History entity) {

    //対象の団員がすでに存在しなければ、「Null」とする。
    if(!faciserv.existsData(entity.getChief_admin())){
      entity.setChief_admin(null);
    }

    if(!faciserv.existsData(entity.getResp_person())){
      entity.setResp_person(null);
    }

    return true;
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Facility> opt = faci_repo.findById(serial_num);

    if(opt.isPresent()){
      Facility entity = opt.get();

      Integer equipinspcount = equipinsp_repo.existRemainFaci_id(entity.getFaci_id());

      //他のテーブルから削除対象設備番号が参照されていれば削除できない。
      if(equipinspcount != 0){
        return false;
      }

      try{
        faci_repo.delete(entity);

        //履歴情報を必ず残す。
        Facility_History hist = new Facility_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        facihist_repo.save(hist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [FacilityServiceImpl:deleteData]");
      }
      
      return true;

    }else{
      return false;
    }
  }
}