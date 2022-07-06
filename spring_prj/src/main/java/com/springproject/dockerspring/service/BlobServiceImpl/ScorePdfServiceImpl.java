package com.springproject.dockerspring.service.BlobServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.ZipProcess.Score_Pdf_Zip;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.form.BlobImplForm.ScorePdfForm;
import com.springproject.dockerspring.repository.HistoryRepo.ScorePdfHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.ScorePdfRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.ScorePdfService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;
import com.springproject.dockerspring.service.ServiceInterface.ScoreService;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [ScorePdfServiceImpl]
/*   楽譜デー楽譜」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class ScorePdfServiceImpl extends GetLoginUser implements ScorePdfService{

  @Autowired
  private ScorePdfRepo scorepdf_repo;

  @Autowired
  private ScorePdfHistRepo scorepdfhist_repo;

  @Autowired
  private Score_Pdf_Zip scorepdf_zip;

  @Autowired
  private ScorePdfForm scorepdf_form;

  @Autowired
  private ScoreService scoreserv;


  //データ数許容値
  private final int MAX_COUNT = 20;





  
  /*************************************************************/
  /*   [selectData]
  /*   楽譜番号を元に、該当するデータを取得する。
  /*************************************************************/
  @Override
  public Iterable<Score_Pdf> selectData(String score_id) {
    return scorepdf_repo.findByScore_id(score_id);
  }







  /*************************************************************/
  /*   [existsData]
  /*   該当の楽譜番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String score_id) {
    List<Score_Pdf> list = (ArrayList<Score_Pdf>)scorepdf_repo.findByScore_id(score_id);
    return !list.isEmpty();
  }





  /*************************************************************/
  /*   [countData]
  /*   該当の楽譜番号のデータの個数を数え、カウント数を返す。
  /*************************************************************/
  @Override
  public Integer countData(String score_id) {
    List<Score_Pdf> list = (ArrayList<Score_Pdf>)scorepdf_repo.findByScore_id(score_id);
    return list.size();
  }






  /*************************************************************/
  /*   [checkMaxLimitData]
  /*   該当の楽譜番号のデータの個数を数え、規定数未満に
  /*   収まっているか確認する。
  /*************************************************************/
  @Override
  public Boolean checkMaxLimitData(String score_id, int inputcount) {
    return MAX_COUNT >= countData(score_id) + inputcount ? true : false;
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Iterable<Score_Pdf> changeData(Iterable<Score_Pdf> entities) throws DbActionException {
    
    try{
      entities = scorepdf_repo.saveAll(entities);
    
      //履歴情報を必ず残す。
      List<Score_Pdf> pdfhist = (ArrayList<Score_Pdf>)entities;
      List<Score_Pdf_History> hist_list = pdfhist.stream()
                                                 .parallel()
                                                 .map(s -> new Score_Pdf_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                 .collect(Collectors.toList());

      scorepdfhist_repo.saveAll(hist_list); 

      return entities;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ScorePdfServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectHist]
  /*   楽譜番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Score_Pdf_History> selectHist(String score_id, Date start_datetime, Date end_datetime) {
    return scorepdfhist_repo.findAllByDateBetween(score_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [selectHistBlobData]
  /*   指定された履歴番号のバイナリデータを取得する。
  /*************************************************************/
  @Override
  public Optional<Score_Pdf_History> selectHistBlobData(Integer history_id) {
    return scorepdfhist_repo.findById(history_id);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) 
      throws DbActionException, ForeignMissException, DataFormatException, OverCountException {

    Score_Pdf_History pdfhist = scorepdfhist_repo.findById(history_id).get();

    /************************************************************************/
    /*   以下のケースでは、シリアルナンバーを「Null」として新規追加扱いとする。
    /*   ・本データに指定履歴の楽譜番号が存在しない場合。
    /*   ・本データに指定履歴のシリアルナンバーが存在しない場合。
    /*   ・本データが履歴のシリアルナンバーと楽譜番号のペアと一致しない場合。
    /************************************************************************/
    if(!existsPair(pdfhist.getSerial_num(), pdfhist.getScore_id())){
      pdfhist.setSerial_num(null);
    }

    //参照整合性&データ数チェック。
    foreignAndMaxcount(pdfhist);

    try{
      Score_Pdf pdf = new Score_Pdf(pdfhist);
      pdf = scorepdf_repo.save(pdf);

      pdfhist = new Score_Pdf_History(pdf, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      scorepdfhist_repo.save(pdfhist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ScorePdfServiceImpl:rollbackData(DbActionException)]");
    }catch(DataFormatException e) {
      throw new DataFormatException("Error location [ScorePdfServiceImpl:rollbackData(DataFormatException)]");
    }
  }





  /*************************************************************/
  /*   [existsPair]
  /*   引数で渡されたシリアルナンバーと音源番号を用いて、
  /*   シリアルナンバーと音源番号のペアが存在するか確認。
  /*************************************************************/
  @Override
  public Boolean existsPair(Integer serial_num, String score_id) {

    if(scorepdf_repo.existsById(serial_num) && existsData(score_id)){

      List<Score_Pdf> list = (ArrayList<Score_Pdf>)scorepdf_repo.findByScore_id(score_id);

      try{
        return list.stream()
                   .parallel()
                   .anyMatch(s -> s.getSerial_num() == serial_num && s.getScore_id().equals(score_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [ScorePdfServiceImpl:existsPair]");
      }
    }

    return false;
  }







  /*************************************************************/
  /*   [foreignAndMaxcount]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*   またロールバック時に、ファイルの最大数を超えないかも
  /*   チェックする。
  /*************************************************************/
  @Override
  public void foreignAndMaxcount(Score_Pdf_History entity) throws ForeignMissException, OverCountException {

    if(!scoreserv.existsData(entity.getScore_id())){
      throw new ForeignMissException();
    }

    //シリアルナンバーが「Null」の時は新規追加扱いとなるので、データ数チェックが必要である。
    if(entity.getSerial_num() == null || !checkMaxLimitData(entity.getScore_id(), 1)){
      throw new OverCountException();
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Score_Pdf entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Score_Pdf_History entity) throws DataFormatException {
    return entity.makeMap();
  }





  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Score_Pdf> datalist) {

    List<Score_Pdf> list = (ArrayList<Score_Pdf>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Score_Pdf_History> datalist) throws DataFormatException {

    List<Score_Pdf_History> list = (ArrayList<Score_Pdf_History>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    try{
      list.stream()
          .parallel()
          .map(s -> {
            try {
              return parseStringH(s);
            } catch (DataFormatException e) {
              throw new RuntimeException();
            }
          })
          .forEachOrdered(s -> result.add(s));
    }catch(RuntimeException e){
      throw new DataFormatException("Error location [ScorePdfServiceImpl:mapPackingH]");
    }

    return result;
  }






  
  /************************************************************/
  /*   [inputZip]
  /*   指定された楽譜番号の為に入力されたデータの
  /*   ZIPファイルを解凍し、データベースに取り込む。
  /*************************************************************/
  @Override
  public void inputZip(String score_id, MultipartFile file) 
      throws ZipException, IOException, InputFileDifferException, InterruptedException, ExecutionException, DbActionException, OverCountException {

    Map<String, byte[]> map = scorepdf_zip.extractZip(file);

    //現在のファイル数と、入力ファイル数の合計が、規定値を上回るのであれば、処理を中止。
    if(!checkMaxLimitData(score_id, map.size())){
      throw new OverCountException();
    }

    //取り込んだデータは、必ずバリデーションチェックを行う。
    Boolean nonerror = scorepdf_form.blobChecker(map);

    if(nonerror){
      Iterable<Score_Pdf> entities = scorepdf_zip.zipToEntitys(map, score_id);

      try{
        entities = scorepdf_repo.saveAll(entities);

        //履歴情報を必ず残す。
        List<Score_Pdf> pdflist = (ArrayList<Score_Pdf>)entities;
        List<Score_Pdf_History> hist_list = pdflist.stream()
                                                   .parallel()
                                                   .map(s -> new Score_Pdf_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                   .collect(Collectors.toList());
        
        scorepdfhist_repo.saveAll(hist_list);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [ScorePdfServiceImpl:inputZip]");
      }

    }else{
      throw new InputFileDifferException();
    }
  }






  /*************************************************************/
  /*   [outputZip]
  /*   渡されたエンティティリスト内のデータをZIPファイルに
  /*   詰めて、出力する。
  /*************************************************************/
  @Override
  public String outputZip(Iterable<Score_Pdf> datalist) 
      throws ZipException, IOException, InterruptedException {
    return scorepdf_zip.outputZip(datalist);
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public void deleteData(Integer serial_num) throws DbActionException {
    Optional<Score_Pdf> opt = scorepdf_repo.findById(serial_num);

    if(opt.isPresent()){
      Score_Pdf entity = opt.get();

      try{
        scorepdf_repo.delete(entity);

        //履歴情報を必ず残す。
        Score_Pdf_History pdfhist = new Score_Pdf_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        scorepdfhist_repo.save(pdfhist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [ScorePdfServiceImpl:deleteData]");
      }
    }
  }






  /*************************************************************/
  /*   [deleteAllData]
  /*   指定の楽譜番号のデータをすべて削除する。
  /*************************************************************/
  @Override
  public void deleteAllData(String sound_id) throws DbActionException {
    List<Score_Pdf> list = (ArrayList<Score_Pdf>)selectData(sound_id);
    
    if(list.size() != 0){
      try{
        scorepdf_repo.deleteAll(list);

        //削除履歴を必ず残す。
        List<Score_Pdf_History> hist_list = list.stream()
                                                .parallel()
                                                .map(s -> new Score_Pdf_History(s, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]))
                                                .collect(Collectors.toList());

        scorepdfhist_repo.saveAll(hist_list);           
      
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [ScorePdfServiceImpl:deleteAllData]");
      }   
    }
  }






  /*************************************************************/
  /*   [selectData]
  /*   フォームクラス内のバイナリデータを取得し、それぞれの
  /*   エンティティを作成する。
  /*************************************************************/
  @Override
  public List<Score_Pdf> blobPacking(ScorePdfForm form) throws IOException {
    List<Score_Pdf> list = new ArrayList<>();

    try{
      list.add(new Score_Pdf(form, 1));

      if(form.getPdf_data_2() != null){
        list.add(new Score_Pdf(form, 2));
      }

      if(form.getPdf_data_3() != null){
        list.add(new Score_Pdf(form, 3));
      }

      if(form.getPdf_data_4() != null){
        list.add(new Score_Pdf(form, 4));
      }

      if(form.getPdf_data_5() != null){
        list.add(new Score_Pdf(form, 5));
      }
    }catch(IOException e){
      throw new IOException("Error location [ScorePdfServiceImpl:blobPacking]");
    }

    return list;
  }
}