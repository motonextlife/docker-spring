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
import com.springproject.dockerspring.component.ZipProcess.Equip_Inspect_Photo_Zip;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_Photo_History;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect_Photo;
import com.springproject.dockerspring.form.BlobImplForm.EquipInspPhotoForm;
import com.springproject.dockerspring.repository.HistoryRepo.EquipInspPhotoHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.EquipInspPhotoRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.EquipInspPhotoService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;
import com.springproject.dockerspring.service.ServiceInterface.EquipInspectService;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [EquipInspPhotoServiceImpl]
/*   「点検データ点検」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class EquipInspPhotoServiceImpl extends GetLoginUser implements EquipInspPhotoService{

  @Autowired
  private EquipInspPhotoRepo equipphoto_repo;

  @Autowired
  private EquipInspPhotoHistRepo equipphotohist_repo;

  @Autowired
  private Equip_Inspect_Photo_Zip equipphoto_zip;

  @Autowired
  private EquipInspPhotoForm equipphoto_form;

  @Autowired
  private EquipInspectService equipinspserv;


  //データ数許容値
  private final int MAX_COUNT = 50;





  
  /*************************************************************/
  /*   [selectData]
  /*   点検番号を元に、該当するデータを取得する。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect_Photo> selectData(String inspect_id) {
    return equipphoto_repo.findByInspect_id(inspect_id);
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の点検番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String inspect_id) {
    List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)equipphoto_repo.findByInspect_id(inspect_id);
    return !list.isEmpty();
  }





  /*************************************************************/
  /*   [countData]
  /*   該当の点検番号のデータの個数を数え、カウント数を返す。
  /*************************************************************/
  @Override
  public Integer countData(String inspect_id) {
    List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)equipphoto_repo.findByInspect_id(inspect_id);
    return list.size();
  }






  /*************************************************************/
  /*   [checkMaxLimitData]
  /*   該当の点検番号のデータの個数を数え、規定数未満に
  /*   収まっているか確認する。
  /*************************************************************/
  @Override
  public Boolean checkMaxLimitData(String inspect_id, int inputcount) {
    return MAX_COUNT >= countData(inspect_id) + inputcount ? true : false;
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect_Photo> changeData(Iterable<Equip_Inspect_Photo> entities) throws DbActionException {
    
    try{
      entities = equipphoto_repo.saveAll(entities);
    
      //履歴情報を必ず残す。
      List<Equip_Inspect_Photo> equipphotohist = (ArrayList<Equip_Inspect_Photo>)entities;
      List<Equip_Inspect_Photo_History> hist_list = equipphotohist.stream()
                                                                  .parallel()
                                                                  .map(s -> new Equip_Inspect_Photo_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                                  .collect(Collectors.toList());

      equipphotohist_repo.saveAll(hist_list); 

      return entities;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [EquipInspPhotoServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectHist]
  /*   点検番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect_Photo_History> selectHist(String inspect_id, Date start_datetime, Date end_datetime) {
    return equipphotohist_repo.findAllByDateBetween(inspect_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [selectHistBlobData]
  /*   指定された履歴番号のバイナリデータを取得する。
  /*************************************************************/
  @Override
  public Optional<Equip_Inspect_Photo_History> selectHistBlobData(Integer history_id) {
    return equipphotohist_repo.findById(history_id);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) 
      throws DbActionException, ForeignMissException, DataFormatException, OverCountException {

    Equip_Inspect_Photo_History equipphotohist = equipphotohist_repo.findById(history_id).get();

    /************************************************************************/
    /*   以下のケースでは、シリアルナンバーを「Null」として新規追加扱いとする。
    /*   ・本データに指定履歴の点検番号が存在しない場合。
    /*   ・本データに指定履歴のシリアルナンバーが存在しない場合。
    /*   ・本データが履歴のシリアルナンバーと点検番号のペアと一致しない場合。
    /************************************************************************/
    if(!existsPair(equipphotohist.getSerial_num(), equipphotohist.getInspect_id())){
      equipphotohist.setSerial_num(null);
    }

    //参照整合性&データ数チェック。
    foreignAndMaxcount(equipphotohist);

    try{
      Equip_Inspect_Photo equipphoto = new Equip_Inspect_Photo(equipphotohist);
      equipphoto = equipphoto_repo.save(equipphoto);

      equipphotohist = new Equip_Inspect_Photo_History(equipphoto, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      equipphotohist_repo.save(equipphotohist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [EquipInspPhotoServiceImpl:rollbackData(DbActionException)]");
    }catch(DataFormatException e) {
      throw new DataFormatException("Error location [EquipInspPhotoServiceImpl:rollbackData(DataFormatException)]");
    }
  }






  /*************************************************************/
  /*   [existsPair]
  /*   引数で渡されたシリアルナンバーと点検番号を用いて、
  /*   シリアルナンバーと点検番号のペアが存在するか確認。
  /*************************************************************/
  @Override
  public Boolean existsPair(Integer serial_num, String inspect_id) {

    if(equipphoto_repo.existsById(serial_num) && existsData(inspect_id)){

      List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)equipphoto_repo.findByInspect_id(inspect_id);

      try{
        return list.stream()
                   .parallel()
                   .anyMatch(s -> s.getSerial_num() == serial_num && s.getInspect_id().equals(inspect_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [EquipInspPhotoServiceImpl:existsPair]");
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
  public void foreignAndMaxcount(Equip_Inspect_Photo_History entity) throws ForeignMissException, OverCountException {

    if(!equipinspserv.existsData(entity.getInspect_id())){
      throw new ForeignMissException();
    }

    //シリアルナンバーが「Null」の時は新規追加扱いとなるので、データ数チェックが必要である。
    if(entity.getSerial_num() == null || !checkMaxLimitData(entity.getInspect_id(), 1)){
      throw new OverCountException();
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Equip_Inspect_Photo entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Equip_Inspect_Photo_History entity) throws DataFormatException {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Equip_Inspect_Photo> datalist) {

    List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Equip_Inspect_Photo_History> datalist) throws DataFormatException {

    List<Equip_Inspect_Photo_History> list = (ArrayList<Equip_Inspect_Photo_History>)datalist;
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
      throw new DataFormatException("Error location [EquipInspPhotoServiceImpl:mapPackingH]");
    }

    return result;
  }






  
  /*************************************************************/
  /*   [inputZip]
  /*   指定された点検番号の為に入力されたデータの
  /*   ZIPファイルを解凍し、データベースに取り込む。
  /*************************************************************/
  @Override
  public void inputZip(String inspect_id, MultipartFile file)
      throws ZipException, IOException, InputFileDifferException, InterruptedException, ExecutionException, DbActionException, OverCountException {

    Map<String, byte[]> map = equipphoto_zip.extractZip(file);

    //現在のファイル数と、入力ファイル数の合計が、規定値を上回るのであれば、処理を中止。
    if(!checkMaxLimitData(inspect_id, map.size())){
      throw new OverCountException();
    }

    //取り込んだデータは、必ずバリデーションチェックを行う。
    Boolean nonerror = equipphoto_form.blobChecker(map);

    if(nonerror){
      Iterable<Equip_Inspect_Photo> entities = equipphoto_zip.zipToEntitys(map, inspect_id);

      try{
        entities = equipphoto_repo.saveAll(entities);

        //履歴情報を必ず残す。
        List<Equip_Inspect_Photo> equipphotolist = (ArrayList<Equip_Inspect_Photo>)entities;
        List<Equip_Inspect_Photo_History> hist_list = equipphotolist.stream()
                                                      .parallel()
                                                      .map(s -> new Equip_Inspect_Photo_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                      .collect(Collectors.toList());
        
        equipphotohist_repo.saveAll(hist_list);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [EquipInspPhotoServiceImpl:inputZip]");
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
  public String outputZip(Iterable<Equip_Inspect_Photo> datalist) 
      throws ZipException, IOException, InterruptedException {
    return equipphoto_zip.outputZip(datalist);
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public void deleteData(Integer serial_num) throws DbActionException {
    Optional<Equip_Inspect_Photo> opt = equipphoto_repo.findById(serial_num);

    if(opt.isPresent()){
      Equip_Inspect_Photo entity = opt.get();

      try{
        equipphoto_repo.delete(entity);

        //履歴情報を必ず残す。
        Equip_Inspect_Photo_History photohist = new Equip_Inspect_Photo_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        equipphotohist_repo.save(photohist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [EquipInspPhotoServiceImpl:deleteData]");
      }
    }
  }





  /*************************************************************/
  /*   [deleteAllData]
  /*   指定の点検番号のデータをすべて削除する。
  /*************************************************************/
  @Override
  public void deleteAllData(String inspect_id) throws DbActionException {
    List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)selectData(inspect_id);
    
    if(list.size() != 0){
      try{
        equipphoto_repo.deleteAll(list);

        //削除履歴を必ず残す。
        List<Equip_Inspect_Photo_History> hist_list = list.stream()
                                                          .parallel()
                                                          .map(s -> new Equip_Inspect_Photo_History(s, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]))
                                                          .collect(Collectors.toList());

        equipphotohist_repo.saveAll(hist_list);        
        
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [EquipInspPhotoServiceImpl:deleteAllData]");
      }   
    }
  }





  /*************************************************************/
  /*   [selectData]
  /*   フォームクラス内のバイナリデータを取得し、それぞれの
  /*   エンティティを作成する。
  /*************************************************************/
  @Override
  public List<Equip_Inspect_Photo> blobPacking(EquipInspPhotoForm form) throws IOException {
    List<Equip_Inspect_Photo> list = new ArrayList<>();

    try{
      list.add(new Equip_Inspect_Photo(form, 1));

      if(form.getPhoto_data_2() != null){
        list.add(new Equip_Inspect_Photo(form, 2));
      }

      if(form.getPhoto_data_3() != null){
        list.add(new Equip_Inspect_Photo(form, 3));
      }

      if(form.getPhoto_data_4() != null){
        list.add(new Equip_Inspect_Photo(form, 4));
      }

      if(form.getPhoto_data_5() != null){
        list.add(new Equip_Inspect_Photo(form, 5));
      }

      if(form.getPhoto_data_6() != null){
        list.add(new Equip_Inspect_Photo(form, 6));
      }

      if(form.getPhoto_data_7() != null){
        list.add(new Equip_Inspect_Photo(form, 7));
      }

      if(form.getPhoto_data_8() != null){
        list.add(new Equip_Inspect_Photo(form, 8));
      }

      if(form.getPhoto_data_9() != null){
        list.add(new Equip_Inspect_Photo(form, 9));
      }

      if(form.getPhoto_data_10() != null){
        list.add(new Equip_Inspect_Photo(form, 10));
      }
    }catch(IOException e){
      throw new IOException("Error location [EquipInspPhotoServiceImpl:blobPacking]");
    }

    return list;
  }
}