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

import com.springproject.dockerspring.component.CsvProcess.Inspect_Items_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Inspect_Items_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Inspect_Items_History;
import com.springproject.dockerspring.entity.NormalEntity.Inspect_Items;
import com.springproject.dockerspring.form.CsvImplForm.InspectItemsForm;
import com.springproject.dockerspring.repository.HistoryRepo.InspItemsHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.EquipInspRepo;
import com.springproject.dockerspring.repository.NormalRepo.InspItemsRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [InspectItemsServiceImpl]
/*   「点検シート管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class InspectItemsServiceImpl extends GetLoginUser implements InspectItemsService{

  @Autowired
  private InspItemsRepo inspitems_repo;

  @Autowired
  private InspItemsHistRepo inspitemshist_repo;

  @Autowired
  private Inspect_Items_Pdf inspitems_pdf;

  @Autowired
  private Inspect_Items_Csv inspitems_csv;

  @Autowired
  private InspectItemsForm inspitems_form;

  @Autowired
  private EquipInspRepo equipinsp_repo;






  /*************************************************************/
  /*   [selectData]
  /*   点検シート番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Inspect_Items> selectData(String inspsheet_id) {
    return inspitems_repo.findByInspsheet_id(inspsheet_id);
  }





  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと点検シート番号を用いて、
  /*   点検シート番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String inspsheet_id) {

    if(inspitems_repo.existsById(serial_num) && existsData(inspsheet_id)){

      List<Inspect_Items> list = (ArrayList<Inspect_Items>)inspitems_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の点検シート番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getInspsheet_id())
                   .noneMatch(s -> s.equals(inspsheet_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [InspectItemsServiceImpl:duplicateData]");
      }
    }

    return false;
  }





  /*************************************************************/
  /*   [existsData]
  /*   該当の点検シート番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String inspsheet_id) {
    return inspitems_repo.findByInspsheet_id(inspsheet_id).isPresent();
  }





  /*************************************************************/
  /*   [changeData]
  /*   渡された点検シート情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Inspect_Items changeData(Inspect_Items entity) throws DbActionException {

    try{
      entity = inspitems_repo.save(entity);

      //履歴情報を必ず残す。
      Inspect_Items_History inspitemshist = new Inspect_Items_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      inspitemshist_repo.save(inspitemshist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [InspectItemsServiceImpl:changeData]");
    }
  }





  /*************************************************************/
  /*   [selectAllData(1)]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した点検シート情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Inspect_Items> selectAllData(String word, String subject, Boolean order) {
    return inspitems_repo.findAllJudge(word, subject, order);
  }






  /*************************************************************/
  /*   [selectHist]
  /*   点検シート番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Inspect_Items_History> selectHist(String inspsheet_id, Date start_datetime, Date end_datetime) {
    return inspitemshist_repo.findAllByDateBetween(inspsheet_id, start_datetime, end_datetime);
  }





  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Inspect_Items_History inspectitemshist = inspitemshist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の点検シート番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(inspectitemshist.getInspsheet_id())){
      inspectitemshist.setSerial_num(null);

      /*************************************************************/
      /*   該当点検シート番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当点検シート番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(inspectitemshist.getSerial_num(), inspectitemshist.getInspsheet_id())){
      Inspect_Items latest = inspitems_repo.findByInspsheet_id(inspectitemshist.getInspsheet_id()).get();
      inspectitemshist.setSerial_num(latest.getSerial_num());
    }

    try{
      Inspect_Items inspectitems = new Inspect_Items(inspectitemshist);
      inspectitems = inspitems_repo.save(inspectitems);

      inspectitemshist = new Inspect_Items_History(inspectitems, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      inspitemshist_repo.save(inspectitemshist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [InspectItemsServiceImpl:rollbackData]");
    }
  }





  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Inspect_Items entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Inspect_Items_History entity) {
    return entity.makeMap();
  }





  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Inspect_Items> datalist) {

    List<Inspect_Items> list = (ArrayList<Inspect_Items>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Inspect_Items_History> datalist) {

    List<Inspect_Items_History> list = (ArrayList<Inspect_Items_History>)datalist;
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
    Optional<Inspect_Items> entity = inspitems_repo.findById(serial_num);

    if(entity.isPresent()){
      return inspitems_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = inspitems_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = inspitems_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Inspect_Items> entities = inspitems_csv.csvToEntitys(maplist);

      try{
        entities = inspitems_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Inspect_Items> insplist = (ArrayList<Inspect_Items>)entities;
        List<Inspect_Items_History> hist_list = insplist.stream()
                                                        .parallel()
                                                        .map(s -> new Inspect_Items_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                        .collect(Collectors.toList());
        
        inspitemshist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [InspectItemsServiceImpl:inputCsv]");
      }

    }else{
      return errorlist;
    }
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   渡された団員情報リスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Inspect_Items> datalist) throws IOException {
    return inspitems_csv.outputCsv(datalist);
  }





  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return inspitems_csv.outputTemplate();
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Inspect_Items> opt = inspitems_repo.findById(serial_num);

    if(opt.isPresent()){
      Inspect_Items entity = opt.get();

      Integer equipinspcount = equipinsp_repo.existRemainInspsheet_id(entity.getInspsheet_id());

      //他のテーブルから削除対象設備番号が参照されていれば削除できない。
      if(equipinspcount != 0){
        return false;
      }

      try{
        //履歴情報を必ず残す。
        Inspect_Items_History hist = new Inspect_Items_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        inspitemshist_repo.save(hist);

        inspitems_repo.delete(entity);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [InspectItemsServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}