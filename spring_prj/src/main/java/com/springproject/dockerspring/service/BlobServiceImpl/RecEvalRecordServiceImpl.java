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
import com.springproject.dockerspring.component.ZipProcess.Rec_Eval_Record_Zip;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Record_History;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Record;
import com.springproject.dockerspring.form.BlobImplForm.RecEvalRecordForm;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalRecordHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.RecEvalRecordRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.RecEvalRecordService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalService;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [RecEvalRecordServiceImpl]
/*   「採用考課時記録データ」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecEvalRecordServiceImpl extends GetLoginUser implements RecEvalRecordService{

  @Autowired
  private RecEvalRecordRepo recrecord_repo;

  @Autowired
  private RecEvalRecordHistRepo recrecordhist_repo;

  @Autowired
  private Rec_Eval_Record_Zip recrecord_zip;

  @Autowired
  private RecEvalRecordForm recrecord_form;

  @Autowired
  private RecEvalService recevalserv;


  //データ数許容値
  private final int MAX_COUNT = 10;





  
  /*************************************************************/
  /*   [selectData]
  /*   採用考課番号を元に、該当するデータを取得する。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Record> selectData(String eval_id) {
    return recrecord_repo.findByEval_id(eval_id);
  }







  /*************************************************************/
  /*   [existsData]
  /*   該当の採用考課番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String eval_id) {
    List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)recrecord_repo.findByEval_id(eval_id);
    return !list.isEmpty();
  }





  /*************************************************************/
  /*   [countData]
  /*   該当の採用考課番号のデータの個数を数え、カウント数を返す。
  /*************************************************************/
  @Override
  public Integer countData(String eval_id) {
    List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)recrecord_repo.findByEval_id(eval_id);
    return list.size();
  }






  /*************************************************************/
  /*   [checkMaxLimitData]
  /*   該当の採用考課番号のデータの個数を数え、規定数未満に
  /*   収まっているか確認する。
  /*************************************************************/
  @Override
  public Boolean checkMaxLimitData(String eval_id, int inputcount) {
    return MAX_COUNT >= countData(eval_id) + inputcount ? true : false;
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Record> changeData(Iterable<Rec_Eval_Record> entities) throws DbActionException {
    
    try{
      entities = recrecord_repo.saveAll(entities);
    
      //履歴情報を必ず残す。
      List<Rec_Eval_Record> recordhist = (ArrayList<Rec_Eval_Record>)entities;
      List<Rec_Eval_Record_History> hist_list = recordhist.stream()
                                                          .parallel()
                                                          .map(s -> new Rec_Eval_Record_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                          .collect(Collectors.toList());

      recrecordhist_repo.saveAll(hist_list); 

      return entities;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalRecordServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectHist]
  /*   採用考課番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Record_History> selectHist(String eval_id, Date start_datetime, Date end_datetime) {
    return recrecordhist_repo.findAllByDateBetween(eval_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [selectHistBlobData]
  /*   指定された履歴番号のバイナリデータを取得する。
  /*************************************************************/
  @Override
  public Optional<Rec_Eval_Record_History> selectHistBlobData(Integer history_id) {
    return recrecordhist_repo.findById(history_id);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) 
      throws DbActionException, ForeignMissException, DataFormatException, OverCountException {

    Rec_Eval_Record_History recordhist = recrecordhist_repo.findById(history_id).get();

    /************************************************************************/
    /*   以下のケースでは、シリアルナンバーを「Null」として新規追加扱いとする。
    /*   ・本データに指定履歴の採用考課番号が存在しない場合。
    /*   ・本データに指定履歴のシリアルナンバーが存在しない場合。
    /*   ・本データが履歴のシリアルナンバーと採用考課番号のペアと一致しない場合。
    /************************************************************************/
    if(!existsPair(recordhist.getSerial_num(), recordhist.getEval_id())){
      recordhist.setSerial_num(null);
    }

    //参照整合性&データ数チェック。
    foreignAndMaxcount(recordhist);

    try{
      Rec_Eval_Record record = new Rec_Eval_Record(recordhist);
      record = recrecord_repo.save(record);

      recordhist = new Rec_Eval_Record_History(record, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      recrecordhist_repo.save(recordhist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [RecEvalRecordServiceImpl:rollbackData(DbActionException)]");
    }catch(DataFormatException e) {
      throw new DataFormatException("Error location [RecEvalRecordServiceImpl:rollbackData(DataFormatException)]");
    }
  }





  /*************************************************************/
  /*   [existsPair]
  /*   引数で渡されたシリアルナンバーと採用考課番号を用いて、
  /*   シリアルナンバーと採用考課番号のペアが存在するか確認。
  /*************************************************************/
  @Override
  public Boolean existsPair(Integer serial_num, String eval_id) {

    if(recrecord_repo.existsById(serial_num) && existsData(eval_id)){

      List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)recrecord_repo.findByEval_id(eval_id);

      try{
        return list.stream()
                   .parallel()
                   .anyMatch(s -> s.getSerial_num() == serial_num && s.getEval_id().equals(eval_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [RecEvalRecordServiceImpl:existsPair]");
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
  public void foreignAndMaxcount(Rec_Eval_Record_History entity) throws ForeignMissException, OverCountException {

    if(!recevalserv.existsData(entity.getEval_id())){
      throw new ForeignMissException();
    }

    //シリアルナンバーが「Null」の時は新規追加扱いとなるので、データ数チェックが必要である。
    if(entity.getSerial_num() == null || !checkMaxLimitData(entity.getEval_id(), 1)){
      throw new OverCountException();
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Rec_Eval_Record entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Rec_Eval_Record_History entity) throws DataFormatException {
    return entity.makeMap();
  }





  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Rec_Eval_Record> datalist) {

    List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Rec_Eval_Record_History> datalist) throws DataFormatException {

    List<Rec_Eval_Record_History> list = (ArrayList<Rec_Eval_Record_History>)datalist;
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
      throw new DataFormatException("Error location [RecEvalRecordServiceImpl:mapPackingH]");
    }

    return result;
  }






  
  /*************************************************************/
  /*   [inputZip]
  /*   指定された採用考課番号の為に入力されたデータの
  /*   ZIPファイルを解凍し、データベースに取り込む。
  /*************************************************************/
  @Override
  public void inputZip(String record_id, MultipartFile file) 
      throws ZipException, IOException, InputFileDifferException, InterruptedException, ExecutionException, DbActionException, OverCountException {

    Map<String, byte[]> map = recrecord_zip.extractZip(file);

    //現在のファイル数と、入力ファイル数の合計が、規定値を上回るのであれば、処理を中止。
    if(!checkMaxLimitData(record_id, map.size())){
      throw new OverCountException();
    }

    //取り込んだデータは、必ずバリデーションチェックを行う。
    Boolean nonerror = recrecord_form.blobChecker(map);

    if(nonerror){
      Iterable<Rec_Eval_Record> entities = recrecord_zip.zipToEntitys(map, record_id);

      try{
        entities = recrecord_repo.saveAll(entities);

        //履歴情報を必ず残す。
        List<Rec_Eval_Record> recordlist = (ArrayList<Rec_Eval_Record>)entities;
        List<Rec_Eval_Record_History> hist_list = recordlist.stream()
                                                            .parallel()
                                                            .map(s -> new Rec_Eval_Record_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                            .collect(Collectors.toList());
        
        recrecordhist_repo.saveAll(hist_list);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalRecordServiceImpl:inputZip]");
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
  public String outputZip(Iterable<Rec_Eval_Record> datalist) 
      throws ZipException, IOException, InterruptedException {
    return recrecord_zip.outputZip(datalist);
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public void deleteData(Integer serial_num) throws DbActionException {
    Optional<Rec_Eval_Record> opt = recrecord_repo.findById(serial_num);

    if(opt.isPresent()){
      Rec_Eval_Record entity = opt.get();

      try{
        recrecord_repo.delete(entity);

        //履歴情報を必ず残す。
        Rec_Eval_Record_History recordhist = new Rec_Eval_Record_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        recrecordhist_repo.save(recordhist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalRecordServiceImpl:deleteData]");
      }
    }
  }





  /*************************************************************/
  /*   [deleteAllData]
  /*   指定の採用考課番号のデータをすべて削除する。
  /*************************************************************/
  @Override
  public void deleteAllData(String record_id) throws DbActionException {
    List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)selectData(record_id);
    
    if(list.size() != 0){
      try{
        recrecord_repo.deleteAll(list);

        //削除履歴を必ず残す。
        List<Rec_Eval_Record_History> hist_list = list.stream()
                                                      .parallel()
                                                      .map(s -> new Rec_Eval_Record_History(s, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]))
                                                      .collect(Collectors.toList());

        recrecordhist_repo.saveAll(hist_list);            
        
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [RecEvalRecordServiceImpl:deleteAllData]");
      }   
    }
  }






  /*************************************************************/
  /*   [selectData]
  /*   フォームクラス内のバイナリデータを取得し、それぞれの
  /*   エンティティを作成する。
  /*************************************************************/
  @Override
  public List<Rec_Eval_Record> blobPacking(RecEvalRecordForm form) throws IOException {
    List<Rec_Eval_Record> list = new ArrayList<>();

    try{
      list.add(new Rec_Eval_Record(form, 1));

      if(form.getRecord_data_2() != null){
        list.add(new Rec_Eval_Record(form, 2));
      }

      if(form.getRecord_data_3() != null){
        list.add(new Rec_Eval_Record(form, 3));
      }

      if(form.getRecord_data_4() != null){
        list.add(new Rec_Eval_Record(form, 4));
      }

      if(form.getRecord_data_5() != null){
        list.add(new Rec_Eval_Record(form, 5));
      }
    }catch(IOException e){
      throw new IOException("Error location [RecEvalRecordServiceImpl:blobPacking]");
    }

    return list;
  }
}