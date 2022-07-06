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

import com.springproject.dockerspring.component.CsvProcess.Rec_Eval_Items_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Rec_Eval_Items_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Items_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Items;
import com.springproject.dockerspring.form.CsvImplForm.RecEvalItemsForm;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalItemsHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.RecEvalItemsRepo;
import com.springproject.dockerspring.repository.NormalRepo.RecEvalRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalItemsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [RecEvalItemsServiceImpl]
/*   「採用考課シート管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecEvalItemsServiceImpl extends GetLoginUser implements RecEvalItemsService{

  @Autowired
  private RecEvalItemsRepo recevalitems_repo;

  @Autowired
  private RecEvalItemsHistRepo recevalitemshist_repo;
  
  @Autowired
  private Rec_Eval_Items_Pdf recevalitems_pdf;

  @Autowired
  private Rec_Eval_Items_Csv recevalitems_csv;

  @Autowired
  private RecEvalItemsForm recevalitems_form;

  @Autowired
  private RecEvalRepo receval_repo;






  /*************************************************************/
  /*   [selectData]
  /*   採用考課シート番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Rec_Eval_Items> selectData(String evalsheet_id) {
    return recevalitems_repo.findByEvalsheet_id(evalsheet_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと採用考課シート番号を用いて、
  /*   採用考課シート番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String evalsheet_id) {

    if(recevalitems_repo.existsById(serial_num) && existsData(evalsheet_id)){

      List<Rec_Eval_Items> list = (ArrayList<Rec_Eval_Items>)recevalitems_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の団員番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getEvalsheet_id())
                   .noneMatch(s -> s.equals(evalsheet_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [RecEvalItemsServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の採用考課シート番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String evalsheet_id) {
    return recevalitems_repo.findByEvalsheet_id(evalsheet_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された採用考課シート情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Rec_Eval_Items changeData(Rec_Eval_Items entity) throws DbActionException {

    try{
      entity = recevalitems_repo.save(entity);

      //履歴情報を必ず残す。
      Rec_Eval_Items_History recevalitemshist = new Rec_Eval_Items_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      recevalitemshist_repo.save(recevalitemshist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalItemsServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した採用考課シート情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Items> selectAllData(String word, String subject, Boolean order) {
    return recevalitems_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   採用考課シート番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Items_History> selectHist(String evalsheet_id, Date start_datetime, Date end_datetime) {
    return recevalitemshist_repo.findAllByDateBetween(evalsheet_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Rec_Eval_Items_History recevalitemshist = recevalitemshist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の採用考課シート番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(recevalitemshist.getEvalsheet_id())){
      recevalitemshist.setSerial_num(null);

      /*************************************************************/
      /*   該当採用考課シート番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当採用考課シート番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(recevalitemshist.getSerial_num(), recevalitemshist.getEvalsheet_id())){
      Rec_Eval_Items latest = recevalitems_repo.findByEvalsheet_id(recevalitemshist.getEvalsheet_id()).get();
      recevalitemshist.setSerial_num(latest.getSerial_num());
    }

    try{
      Rec_Eval_Items recevalitems = new Rec_Eval_Items(recevalitemshist);
      recevalitems = recevalitems_repo.save(recevalitems);
    
      recevalitemshist = new Rec_Eval_Items_History(recevalitems, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);    
      recevalitemshist_repo.save(recevalitemshist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalItemsServiceImpl:rollbackData]");
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Rec_Eval_Items entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Rec_Eval_Items_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Rec_Eval_Items> datalist) {

    List<Rec_Eval_Items> list = (ArrayList<Rec_Eval_Items>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Rec_Eval_Items_History> datalist) {

    List<Rec_Eval_Items_History> list = (ArrayList<Rec_Eval_Items_History>)datalist;
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
    Optional<Rec_Eval_Items> entity = recevalitems_repo.findById(serial_num);

    if(entity.isPresent()){
      return recevalitems_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = recevalitems_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = recevalitems_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Rec_Eval_Items> entities = recevalitems_csv.csvToEntitys(maplist);

      try{
        entities = recevalitems_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Rec_Eval_Items> recitem = (ArrayList<Rec_Eval_Items>)entities;
        List<Rec_Eval_Items_History> hist_list = recitem.stream()
                                                        .parallel()
                                                        .map(s -> new Rec_Eval_Items_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                        .collect(Collectors.toList());
        
        recevalitemshist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalItemsServiceImpl:inputCsv]");
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
  public String outputCsv(Iterable<Rec_Eval_Items> datalist) throws IOException {
    return recevalitems_csv.outputCsv(datalist);
  }






  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return recevalitems_csv.outputTemplate();
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Rec_Eval_Items> opt = recevalitems_repo.findById(serial_num);

    if(opt.isPresent()){
      Rec_Eval_Items entity = opt.get();

      Integer recevalcount = receval_repo.existRemainEvalsheet_id(entity.getEvalsheet_id());

      //他のテーブルから削除対象設備番号が参照されていれば削除できない。
      if(recevalcount != 0){
        return false;
      }

      try{
        recevalitems_repo.delete(entity);

        //履歴情報を必ず残す。
        Rec_Eval_Items_History hist = new Rec_Eval_Items_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        recevalitemshist_repo.save(hist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalItemsServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}