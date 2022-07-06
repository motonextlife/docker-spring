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

import com.springproject.dockerspring.component.CsvProcess.Musical_Score_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Musical_Score_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.form.CsvImplForm.MusicalScoreForm;
import com.springproject.dockerspring.repository.HistoryRepo.MusicScoreHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.MusicScoreRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.ScorePdfService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [ScoreServiceImpl]
/*   「楽譜管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class ScoreServiceImpl extends GetLoginUser implements ScoreService{

  @Autowired
  private MusicScoreRepo musicscore_repo;

  @Autowired
  private MusicScoreHistRepo musicscorehist_repo;

  @Autowired
  private Musical_Score_Pdf musicscore_pdf;

  @Autowired
  private Musical_Score_Csv musicscore_csv;

  @Autowired
  private MusicalScoreForm musicscore_form;

  @Autowired
  private ScorePdfService pdfserv;






  /*************************************************************/
  /*   [selectData]
  /*   楽譜番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Musical_Score> selectData(String score_id) {
    return musicscore_repo.findByScore_Id(score_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと楽譜番号を用いて、
  /*   楽譜番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String score_id) {

    if(musicscore_repo.existsById(serial_num) && existsData(score_id)){

      List<Musical_Score> list = (ArrayList<Musical_Score>)musicscore_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の楽譜番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getScore_id())
                   .noneMatch(s -> s.equals(score_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [ScoreServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の楽譜番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String score_id) {
    return musicscore_repo.findByScore_Id(score_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された楽譜情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Musical_Score changeData(Musical_Score entity) throws DbActionException {

    try{
      entity = musicscore_repo.save(entity);

      //履歴情報を必ず残す。
      Musical_Score_History scorehist = new Musical_Score_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      musicscorehist_repo.save(scorehist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ScoreServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した楽譜情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Musical_Score> selectAllData(String word, String subject, Boolean order) {
    return musicscore_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   楽譜番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Musical_Score_History> selectHist(String score_id, Date start_datetime, Date end_datetime) {
    return musicscorehist_repo.findAllByDateBetween(score_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Musical_Score_History scorehist = musicscorehist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の楽譜番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(scorehist.getScore_id())){
      scorehist.setSerial_num(null);

      /*************************************************************/
      /*   該当楽譜番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当楽譜番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(scorehist.getSerial_num(), scorehist.getScore_id())){
      Musical_Score latest = musicscore_repo.findByScore_Id(scorehist.getScore_id()).get();
      scorehist.setSerial_num(latest.getSerial_num());
    }

    try{
      Musical_Score score = new Musical_Score(scorehist);
      score = musicscore_repo.save(score);

      scorehist = new Musical_Score_History(score, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      musicscorehist_repo.save(scorehist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ScoreServiceImpl:rollbackData]");
    }
  }





  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Musical_Score entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Musical_Score_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Musical_Score> datalist) {

    List<Musical_Score> list = (ArrayList<Musical_Score>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Musical_Score_History> datalist) {

    List<Musical_Score_History> list = (ArrayList<Musical_Score_History>)datalist;
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
    Optional<Musical_Score> entity = musicscore_repo.findById(serial_num);

    if(entity.isPresent()){
      return musicscore_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = musicscore_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = musicscore_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Musical_Score> entities = musicscore_csv.csvToEntitys(maplist);

      try{
        entities = musicscore_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Musical_Score> scorelist = (ArrayList<Musical_Score>)entities;
        List<Musical_Score_History> hist_list = scorelist.stream()
                                                         .parallel()
                                                         .map(s -> new Musical_Score_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                         .collect(Collectors.toList());
        
        musicscorehist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [ScoreServiceImpl:inputCsv]");
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
  public String outputCsv(Iterable<Musical_Score> datalist) throws IOException {
    return musicscore_csv.outputCsv(datalist);
  }






  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return musicscore_csv.outputTemplate();
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Musical_Score> opt = musicscore_repo.findById(serial_num);

    if(opt.isPresent()){
      Musical_Score entity = opt.get();

      try{
        pdfserv.deleteAllData(entity.getScore_id());

        musicscore_repo.delete(entity);

        //履歴情報を必ず残す。
        Musical_Score_History hist = new Musical_Score_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        musicscorehist_repo.save(hist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [ScoreServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}