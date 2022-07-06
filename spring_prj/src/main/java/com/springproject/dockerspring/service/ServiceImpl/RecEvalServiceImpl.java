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

import com.springproject.dockerspring.component.CsvProcess.Rec_Eval_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Rec_Eval_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval;
import com.springproject.dockerspring.form.CsvImplForm.RecEvalForm;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.RecEvalRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.ListOrderEnum;
import com.springproject.dockerspring.service.BlobServiceInterface.RecEvalRecordService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalItemsService;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [RecEvalServiceImpl]
/*   「採用考課管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecEvalServiceImpl extends GetLoginUser implements RecEvalService{

  @Autowired
  private RecEvalRepo receval_repo;
  
  @Autowired
  private RecEvalHistRepo recevalhist_repo;

  @Autowired
  private Rec_Eval_Pdf receval_pdf;

  @Autowired
  private Rec_Eval_Csv receval_csv;

  @Autowired
  private RecEvalForm receval_form;
  
  @Autowired
	private MemberInfoService membinfoserv;
  
  @Autowired
  private RecEvalItemsService recevalitemsserv;

  @Autowired
  private RecEvalRecordService recordserv;






  /*************************************************************/
  /*   [selectList]
  /*   入力された団員番号に対応する情報をリストで取得する。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval> selectList(String member_id, ListOrderEnum order) {
    List<Rec_Eval> list = (List<Rec_Eval>)receval_repo.findList(member_id);
    
    switch(order){
      case UPLOAD_DATE_ASC:
        Collections.sort(list, (a, b) -> a.getEval_date().toString().compareTo(b.getEval_date().toString()));
        break;
      case UPLOAD_DATE_DESC:
        Collections.sort(list, (a, b) -> b.getEval_date().toString().compareTo(a.getEval_date().toString()));
        break;
      case ID_ASC:
        Collections.sort(list, (a, b) -> a.getEval_id().compareTo(b.getEval_id().toString()));
        break;
      case ID_DESC:
        Collections.sort(list, (a, b) -> b.getEval_id().compareTo(a.getEval_id().toString()));
        break;
      case SHEET_ASC:
        Collections.sort(list, (a, b) -> a.getEvalsheet_id().compareTo(b.getEvalsheet_id().toString()));
        break;
      case SHEET_DESC:
        Collections.sort(list, (a, b) -> b.getEvalsheet_id().compareTo(a.getEvalsheet_id().toString()));
        break;
    }

    return list;
  }





  /*************************************************************/
  /*   [selectData]
  /*   採用考課番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Rec_Eval> selectData(String eval_id) {
    return receval_repo.findByEval_id(eval_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと採用考課番号を用いて、
  /*   採用考課番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String eval_id) {

    if(receval_repo.existsById(serial_num) && existsData(eval_id)){

      List<Rec_Eval> list = (ArrayList<Rec_Eval>)receval_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の採用考課番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getEval_id())
                   .noneMatch(s -> s.equals(eval_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [RecEvalServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の採用考課番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String eval_id) {
    return receval_repo.findByEval_id(eval_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された採用考課情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Rec_Eval changeData(Rec_Eval entity) throws DbActionException {

    try{
      entity = receval_repo.save(entity);

      //履歴情報を必ず残す。
      Rec_Eval_History recevalhist = new Rec_Eval_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      recevalhist_repo.save(recevalhist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した採用考課情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval> selectAllData(String word, String subject, Boolean order) {
    return receval_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   採用考課番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_History> selectHist(String eval_id, Date start_datetime, Date end_datetime) {
    return recevalhist_repo.findAllByDateBetween(eval_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Rec_Eval_History recevalhist = recevalhist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の採用考課番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(recevalhist.getEval_id())){
      recevalhist.setSerial_num(null);

      /*************************************************************/
      /*   該当採用考課番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当採用考課番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(recevalhist.getSerial_num(), recevalhist.getEval_id())){
      Rec_Eval latest = receval_repo.findByEval_id(recevalhist.getEval_id()).get();
      recevalhist.setSerial_num(latest.getSerial_num());
    }

    //参照整合性チェック。
    foreignId(recevalhist);

    try{
      Rec_Eval receval = new Rec_Eval(recevalhist);
      receval = receval_repo.save(receval);

      recevalhist = new Rec_Eval_History(receval, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      recevalhist_repo.save(recevalhist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalServiceImpl:rollbackData]");
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Rec_Eval entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Rec_Eval_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Rec_Eval> datalist) {

    List<Rec_Eval> list = (ArrayList<Rec_Eval>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Rec_Eval_History> datalist) {

    List<Rec_Eval_History> list = (ArrayList<Rec_Eval_History>)datalist;
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
    Optional<Rec_Eval> entity = receval_repo.findById(serial_num);

    if(entity.isPresent()){
      return receval_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = receval_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = receval_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Rec_Eval> entities = receval_csv.csvToEntitys(maplist);

      try{
        entities = receval_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Rec_Eval> receval = (ArrayList<Rec_Eval>)entities;
        List<Rec_Eval_History> hist_list = receval.stream()
                                                  .parallel()
                                                  .map(s -> new Rec_Eval_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                  .collect(Collectors.toList());
        
        recevalhist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalServiceImpl:inputCsv]");
      }

    }else{
      return errorlist;
    }
  }






  /*************************************************************/
  /*   [outputCsv]
  /*   渡された採用考課情報リスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Rec_Eval> datalist) throws IOException {
    return receval_csv.outputCsv(datalist);
  }






  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return receval_csv.outputTemplate();
  }






  /*************************************************************/
  /*   [foreignId]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*************************************************************/
  @Override
  public void foreignId(Rec_Eval_History entity) throws ForeignMissException {

    if(!membinfoserv.existsData(entity.getMember_id()) || !recevalitemsserv.existsData(entity.getEvalsheet_id())){
      throw new ForeignMissException();
    }
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Rec_Eval> opt = receval_repo.findById(serial_num);

    if(opt.isPresent()){
      Rec_Eval entity = opt.get();

      try{
        recordserv.deleteAllData(entity.getEval_id());

        receval_repo.delete(entity);

        //履歴情報を必ず残す。
        Rec_Eval_History hist = new Rec_Eval_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        recevalhist_repo.save(hist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}