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

import com.springproject.dockerspring.component.CsvProcess.Sound_Source_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Sound_Source_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.form.CsvImplForm.SoundSourceForm;
import com.springproject.dockerspring.repository.HistoryRepo.SoundSourceHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.SoundSourceRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.BlobServiceInterface.AudioDataService;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.SoundService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;





/**************************************************************/
/*   [SoundServiceImpl]
/*   「音源管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class SoundServiceImpl extends GetLoginUser implements SoundService{

  @Autowired
  private SoundSourceRepo soundsource_repo;

  @Autowired
  private SoundSourceHistRepo soundsourcehist_repo;

  @Autowired
  private Sound_Source_Pdf soundsource_pdf;

  @Autowired
  private Sound_Source_Csv soundsource_csv;

  @Autowired
  private SoundSourceForm soundsource_form;

  @Autowired
  private AudioDataService audioserv;




  /*************************************************************/
  /*   [selectData]
  /*   音源番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Sound_Source> selectData(String sound_id) {
    return soundsource_repo.findBySound_Id(sound_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと音源番号を用いて、
  /*   音源番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String sound_id) {

    if(soundsource_repo.existsById(serial_num) && existsData(sound_id)){

      List<Sound_Source> list = (ArrayList<Sound_Source>)soundsource_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の音源番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getSound_id())
                   .noneMatch(s -> s.equals(sound_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [SoundServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の音源番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String sound_id) {
    return soundsource_repo.findBySound_Id(sound_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された音源情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Sound_Source changeData(Sound_Source entity) throws DbActionException {

    try{
      entity = soundsource_repo.save(entity);

      //履歴情報を必ず残す。
      Sound_Source_History soundhist = new Sound_Source_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      soundsourcehist_repo.save(soundhist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [SoundServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData(1)]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した音源情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Sound_Source> selectAllData(String word, String subject, Boolean order) {
    return soundsource_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   音源番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Sound_Source_History> selectHist(String sound_id, Date start_datetime, Date end_datetime) {
    return soundsourcehist_repo.findAllByDateBetween(sound_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Sound_Source_History soundhist = soundsourcehist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の音源番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(soundhist.getSound_id())){
      soundhist.setSerial_num(null);

      /*************************************************************/
      /*   該当音源番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当音源番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(soundhist.getSerial_num(), soundhist.getSound_id())){
      Sound_Source latest = soundsource_repo.findBySound_Id(soundhist.getSound_id()).get();
      soundhist.setSerial_num(latest.getSerial_num());
    }

    try{
      Sound_Source sound = new Sound_Source(soundhist);
      sound = soundsource_repo.save(sound);

      soundhist = new Sound_Source_History(sound, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      soundsourcehist_repo.save(soundhist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [SoundServiceImpl:rollbackData]");
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Sound_Source entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Sound_Source_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Sound_Source> datalist) {

    List<Sound_Source> list = (ArrayList<Sound_Source>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Sound_Source_History> datalist) {

    List<Sound_Source_History> list = (ArrayList<Sound_Source_History>)datalist;
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
    Optional<Sound_Source> entity = soundsource_repo.findById(serial_num);

    if(entity.isPresent()){
      return soundsource_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = soundsource_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = soundsource_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Sound_Source> entities = soundsource_csv.csvToEntitys(maplist);

      try{
        entities = soundsource_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Sound_Source> soundlist = (ArrayList<Sound_Source>)entities;
        List<Sound_Source_History> hist_list = soundlist.stream()
                                                        .parallel()
                                                        .map(s -> new Sound_Source_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                        .collect(Collectors.toList());
        
        soundsourcehist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [SoundServiceImpl:inputCsv]");
      }

    }else{
      return errorlist;
    }
  }






  /*************************************************************/
  /*   [outputCsv]
  /*   渡された音源情報リスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Sound_Source> datalist) throws IOException {
    return soundsource_csv.outputCsv(datalist);
  }






  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return soundsource_csv.outputTemplate();
  }






  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Sound_Source> opt = soundsource_repo.findById(serial_num);

    if(opt.isPresent()){
      Sound_Source entity = opt.get();

      try{
        audioserv.deleteAllData(entity.getSound_id());

        soundsource_repo.delete(entity);

        //履歴情報を必ず残す。
        Sound_Source_History hist = new Sound_Source_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        soundsourcehist_repo.save(hist);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [SoundServiceImpl:deleteData]");
      }
      
      return true;

    }else{
      return false;
    }
  }
}