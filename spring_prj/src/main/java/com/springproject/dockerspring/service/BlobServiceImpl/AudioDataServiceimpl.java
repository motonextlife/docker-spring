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
import com.springproject.dockerspring.component.ZipProcess.Audio_Data_Zip;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.form.BlobImplForm.AudioDataForm;
import com.springproject.dockerspring.repository.HistoryRepo.AudioDataHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.AudioDataRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.AudioDataService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;
import com.springproject.dockerspring.service.ServiceInterface.SoundService;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [AudioDataServiceimpl]
/*   「音源データ音源」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class AudioDataServiceimpl extends GetLoginUser implements AudioDataService{

  @Autowired
  private AudioDataRepo audio_repo;

  @Autowired
  private AudioDataHistRepo audiohist_repo;

  @Autowired
  private Audio_Data_Zip audio_zip;

  @Autowired
  private AudioDataForm audio_form;

  @Autowired
  private SoundService soundserv;


  //データ数許容値
  private final int MAX_COUNT = 10;





  
  /*************************************************************/
  /*   [selectData]
  /*   音源番号を元に、該当するデータを取得する。
  /*************************************************************/
  @Override
  public Iterable<Audio_Data> selectData(String sound_id) {
    return audio_repo.findBySound_id(sound_id);
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の音源番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String sound_id) {
    List<Audio_Data> list = (ArrayList<Audio_Data>)audio_repo.findBySound_id(sound_id);
    return !list.isEmpty();
  }





  /*************************************************************/
  /*   [countData]
  /*   該当の音源番号のデータの個数を数え、カウント数を返す。
  /*************************************************************/
  @Override
  public Integer countData(String sound_id) {
    List<Audio_Data> list = (ArrayList<Audio_Data>)audio_repo.findBySound_id(sound_id);
    return list.size();
  }






  /*************************************************************/
  /*   [checkMaxLimitData]
  /*   該当の音源番号のデータの個数を数え、規定数未満に
  /*   収まっているか確認する。
  /*************************************************************/
  @Override
  public Boolean checkMaxLimitData(String sound_id, int inputcount) {
    return MAX_COUNT >= countData(sound_id) + inputcount ? true : false;
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Iterable<Audio_Data> changeData(Iterable<Audio_Data> entities) throws DbActionException {
    
    try{
      entities = audio_repo.saveAll(entities);
    
      //履歴情報を必ず残す。
      List<Audio_Data> audiolist = (ArrayList<Audio_Data>)entities;
      List<Audio_Data_History> hist_list = audiolist.stream()
                                                    .parallel()
                                                    .map(s -> new Audio_Data_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                    .collect(Collectors.toList());

      audiohist_repo.saveAll(hist_list); 

      return entities;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [AudioDataServiceimpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectHist]
  /*   音源番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Audio_Data_History> selectHist(String sound_id, Date start_datetime, Date end_datetime) {
    return audiohist_repo.findAllByDateBetween(sound_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [selectHistBlobData]
  /*   指定された履歴番号のバイナリデータを取得する。
  /*************************************************************/
  @Override
  public Optional<Audio_Data_History> selectHistBlobData(Integer history_id) {
    return audiohist_repo.findById(history_id);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) 
      throws DbActionException, ForeignMissException, DataFormatException, OverCountException {

    Audio_Data_History audiohist = audiohist_repo.findById(history_id).get();

    /************************************************************************/
    /*   以下のケースでは、シリアルナンバーを「Null」として新規追加扱いとする。
    /*   ・本データに指定履歴の音源番号が存在しない場合。
    /*   ・本データに指定履歴のシリアルナンバーが存在しない場合。
    /*   ・本データが履歴のシリアルナンバーと音源番号のペアと一致しない場合。
    /************************************************************************/
    if(!existsPair(audiohist.getSerial_num(), audiohist.getSound_id())){
      audiohist.setSerial_num(null);
    }

    //参照整合性&データ数チェック。
    foreignAndMaxcount(audiohist);

    try{
      Audio_Data audio = new Audio_Data(audiohist);
      audio = audio_repo.save(audio);

      audiohist = new Audio_Data_History(audio, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      audiohist_repo.save(audiohist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [AudioDataServiceimpl:rollbackData(DbActionException)]");
    }catch(DataFormatException e) {
      throw new DataFormatException("Error location [AudioDataServiceimpl:rollbackData(DataFormatException)]");
    }
  }





  /*************************************************************/
  /*   [existsPair]
  /*   引数で渡されたシリアルナンバーと音源番号を用いて、
  /*   シリアルナンバーと音源番号のペアが存在するか確認。
  /*************************************************************/
  @Override
  public Boolean existsPair(Integer serial_num, String sound_id) {

    if(audio_repo.existsById(serial_num) && existsData(sound_id)){

      List<Audio_Data> list = (ArrayList<Audio_Data>)audio_repo.findBySound_id(sound_id);

      try{
        return list.stream()
                   .parallel()
                   .anyMatch(s -> s.getSerial_num() == serial_num && s.getSound_id().equals(sound_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [AudioDataServiceimpl:existsPair]");
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
  public void foreignAndMaxcount(Audio_Data_History entity) throws ForeignMissException, OverCountException {

    if(!soundserv.existsData(entity.getSound_id())){
      throw new ForeignMissException();
    }

    //シリアルナンバーが「Null」の時は新規追加扱いとなるので、データ数チェックが必要である。
    if(entity.getSerial_num() == null || !checkMaxLimitData(entity.getSound_id(), 1)){
      throw new OverCountException();
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Audio_Data entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Audio_Data_History entity) throws DataFormatException {
    return entity.makeMap();
  }





  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Audio_Data> datalist) {

    List<Audio_Data> list = (ArrayList<Audio_Data>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Audio_Data_History> datalist) throws DataFormatException {

    List<Audio_Data_History> list = (ArrayList<Audio_Data_History>)datalist;
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
      throw new DataFormatException("Error location [AudioDataServiceimpl:mapPackingH]");
    }

    return result;
  }






  
  /*************************************************************/
  /*   [inputZip]
  /*   指定された音源番号の為に入力されたデータの
  /*   ZIPファイルを解凍し、データベースに取り込む。
  /*************************************************************/
  @Override
  public void inputZip(String sound_id, MultipartFile file) 
      throws ZipException, IOException, InputFileDifferException, InterruptedException, ExecutionException, DbActionException, OverCountException {

    Map<String, byte[]> map = audio_zip.extractZip(file);

    //現在のファイル数と、入力ファイル数の合計が、規定値を上回るのであれば、処理を中止。
    if(!checkMaxLimitData(sound_id, map.size())){
      throw new OverCountException();
    }

    //取り込んだデータは、必ずバリデーションチェックを行う。
    Boolean nonerror = audio_form.blobChecker(map);

    if(nonerror){
      Iterable<Audio_Data> entities = audio_zip.zipToEntitys(map, sound_id);

      try{
        entities = audio_repo.saveAll(entities);

        //履歴情報を必ず残す。
        List<Audio_Data> audiolist = (ArrayList<Audio_Data>)entities;
        List<Audio_Data_History> hist_list = audiolist.stream()
                                                      .parallel()
                                                      .map(s -> new Audio_Data_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                      .collect(Collectors.toList());
        
        audiohist_repo.saveAll(hist_list);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [AudioDataServiceimpl:inputZip]");
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
  public String outputZip(Iterable<Audio_Data> datalist) 
      throws ZipException, IOException, InterruptedException {
    return audio_zip.outputZip(datalist);
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public void deleteData(Integer serial_num) throws DbActionException {
    Optional<Audio_Data> opt = audio_repo.findById(serial_num);

    if(opt.isPresent()){
      Audio_Data entity = opt.get();

      try{
        audio_repo.delete(entity);

        //履歴情報を必ず残す。
        Audio_Data_History audiohist = new Audio_Data_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        audiohist_repo.save(audiohist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [AudioDataServiceimpl:deleteData]");
      }
    }
  }





  /*************************************************************/
  /*   [deleteAllData]
  /*   指定の音源番号のデータをすべて削除する。
  /*************************************************************/
  @Override
  public void deleteAllData(String sound_id) throws DbActionException {
    List<Audio_Data> list = (ArrayList<Audio_Data>)selectData(sound_id);
    
    if(list.size() != 0){
      try{
        audio_repo.deleteAll(list);

        //削除履歴を必ず残す。
        List<Audio_Data_History> hist_list = list.stream()
                                                 .parallel()
                                                 .map(s -> new Audio_Data_History(s, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]))
                                                 .collect(Collectors.toList());

        audiohist_repo.saveAll(hist_list);      

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [AudioDataServiceimpl:deleteAllData]");
      }                
    }
  }





  /*************************************************************/
  /*   [selectData]
  /*   フォームクラス内のバイナリデータを取得し、それぞれの
  /*   エンティティを作成する。
  /*************************************************************/
  @Override
  public List<Audio_Data> blobPacking(AudioDataForm form) throws IOException {
    List<Audio_Data> list = new ArrayList<>();

    try{
      list.add(new Audio_Data(form, 1));

      if(form.getAudio_data_2() != null){
        list.add(new Audio_Data(form, 2));
      }

      if(form.getAudio_data_3() != null){
        list.add(new Audio_Data(form, 3));
      }

      if(form.getAudio_data_4() != null){
        list.add(new Audio_Data(form, 4));
      }

      if(form.getAudio_data_5() != null){
        list.add(new Audio_Data(form, 5));
      }
    }catch(IOException e){
      throw new IOException("Error location [AudioDataServiceimpl:blobPacking]");
    }

    return list;
  }
}