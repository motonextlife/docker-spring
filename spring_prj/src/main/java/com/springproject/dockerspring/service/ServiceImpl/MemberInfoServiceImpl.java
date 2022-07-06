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

import com.springproject.dockerspring.component.CsvProcess.Member_Info_Csv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.component.PdfProcess.execution.Member_Info_Pdf;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.form.CsvImplForm.MemberInfoForm;
import com.springproject.dockerspring.repository.HistoryRepo.MembInfoHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.FaciRepo;
import com.springproject.dockerspring.repository.NormalRepo.MembInfoRepo;
import com.springproject.dockerspring.repository.NormalRepo.RecEvalRepo;
import com.springproject.dockerspring.repository.NormalRepo.SysUserRepo;
import com.springproject.dockerspring.security.GetLoginUser;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;




/**************************************************************/
/*   [MemberInfoServiceImpl]
/*   「団員管理」に関する処理を行う。
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberInfoServiceImpl extends GetLoginUser implements MemberInfoService{
  
  @Autowired
  private MembInfoRepo membinfo_repo;
  
  @Autowired
  private MembInfoHistRepo membinfohist_repo;

  @Autowired
  private Member_Info_Pdf membinfo_pdf;

  @Autowired
  private Member_Info_Csv membinfo_csv;

  @Autowired
  private MemberInfoForm membinfo_form;

  @Autowired
  private RecEvalRepo recevalrepo;

  @Autowired
  private FaciRepo facirepo;

  @Autowired
  private SysUserRepo sysuserrepo;



  /*************************************************************/
  /*   [selectData]
  /*   団員番号を元に、個別の情報を取得する。
  /*************************************************************/
  @Override
  public Optional<Member_Info> selectData(String member_id) {
    return membinfo_repo.findByMember_id(member_id);
  }






  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと団員番号を用いて、
  /*   団員番号に重複がないか確認する。
  /*************************************************************/
  @Override
  public Boolean duplicateData(Integer serial_num, String member_id) {

    if(membinfo_repo.existsById(serial_num) && existsData(member_id)){

      List<Member_Info> list = (ArrayList<Member_Info>)membinfo_repo.findAll();

      try{
        //対象は、該当シリアルナンバー「以外」の団員番号とする。
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getMember_id())
                   .noneMatch(s -> s.equals(member_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [MemberInfoServiceImpl:duplicateData]");
      }
    }

    return false;
  }






  /*************************************************************/
  /*   [existsData]
  /*   該当の団員番号が存在するか確認する。
  /*************************************************************/
  @Override
  public Boolean existsData(String member_id) {
    return membinfo_repo.findByMember_id(member_id).isPresent();
  }






  /*************************************************************/
  /*   [changeData]
  /*   渡された団員情報をデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  @Override
  public Member_Info changeData(Member_Info entity) throws DbActionException {

    try{
      entity = membinfo_repo.save(entity);
    
      //履歴情報を必ず残す。
      Member_Info_History membinfohist = new Member_Info_History(entity, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]);
      membinfohist_repo.save(membinfohist);

      return entity;

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [MemberInfoServiceImpl:changeData]");
    }
  }






  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した団員情報をデータベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Member_Info> selectAllData(String word, String subject, Boolean order) {
    return membinfo_repo.findAllJudge(word, subject, order);
  }







  /*************************************************************/
  /*   [selectHist]
  /*   団員番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  @Override
  public Iterable<Member_Info_History> selectHist(String member_id, Date start_datetime, Date end_datetime) {
    return membinfohist_repo.findAllByDateBetween(member_id, start_datetime, end_datetime);
  }






  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  @Override
  public void rollbackData(Integer history_id) throws DbActionException, ForeignMissException {

    Member_Info_History membinfohist = membinfohist_repo.findById(history_id).get();

    /*************************************************************/
    /*   本データに指定履歴の団員番号が存在しない場合は、
    /*   シリアルナンバーを「Null」として新規追加扱いとする。
    /*************************************************************/
    if(!existsData(membinfohist.getMember_id())){
      membinfohist.setSerial_num(null);

      /*************************************************************/
      /*   該当団員番号は存在するが、該当履歴内のシリアルナンバーが
      /*   存在しない場合は、該当団員番号情報の現状の最新シリアル
      /*   ナンバーを取得し、その最新シリアルナンバーの情報に
      /*   ロールバックをする。
      /*   また、本データが、履歴のシリアルナンバーと点検シート番号の
      /*   ペアと一致していなくても、同じ処理になる。
      /*************************************************************/
    }else if(!duplicateData(membinfohist.getSerial_num(), membinfohist.getMember_id())){
      Member_Info latest = membinfo_repo.findByMember_id(membinfohist.getMember_id()).get();
      membinfohist.setSerial_num(latest.getSerial_num());
    }

    try{
      Member_Info membinfo = new Member_Info(membinfohist);
      membinfo = membinfo_repo.save(membinfo);

      membinfohist = new Member_Info_History(membinfo, HistoryKindEnum.ROLLBACK.getKind(), getLoginUser()[0]);
      membinfohist_repo.save(membinfohist);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [MemberInfoServiceImpl:rollbackData]");
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  @Override
  public Map<String, String> parseString(Member_Info entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringH(Member_Info_History entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPacking(Iterable<Member_Info> datalist){

    List<Member_Info> list = (ArrayList<Member_Info>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseString(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingH(Iterable<Member_Info_History> datalist){

    List<Member_Info_History> list = (ArrayList<Member_Info_History>)datalist;
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
    Optional<Member_Info> entity = membinfo_repo.findById(serial_num);

    if(entity.isPresent()){
      return membinfo_pdf.makePdf(entity.get());
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

    List<Map<String, String>> maplist = membinfo_csv.extractCsv(file);

    //取り込んだデータは、必ずバリデーションチェックを行う。
    List<List<Integer>> errorlist = membinfo_form.csvChecker(maplist);  

    if(errorlist.size() == 0){
      Iterable<Member_Info> entities = membinfo_csv.csvToEntitys(maplist);

      try{
        entities = membinfo_repo.saveAll(entities);
      
        //履歴情報を必ず残す。
        List<Member_Info> memblist = (ArrayList<Member_Info>)entities;
        List<Member_Info_History> hist_list = memblist.stream()
                                                      .parallel()
                                                      .map(s -> new Member_Info_History(s, HistoryKindEnum.UPDATE.getKind(), getLoginUser()[0]))
                                                      .collect(Collectors.toList());
        
        membinfohist_repo.saveAll(hist_list);
  
        return null;

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [MemberInfoServiceImpl:inputCsv]");
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
  public String outputCsv(Iterable<Member_Info> datalist) throws IOException {
    return membinfo_csv.outputCsv(datalist);
  }




  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  @Override
  public String outputCsvTemplate() throws IOException {
    return membinfo_csv.outputTemplate();
  }





  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  @Override
  public Boolean deleteData(Integer serial_num) throws DbActionException {
    Optional<Member_Info> opt = membinfo_repo.findById(serial_num);

    if(opt.isPresent()){
      Member_Info entity = opt.get();

      Integer recevalcount = recevalrepo.existRemainMember_id(entity.getMember_id());
      Integer facicount = facirepo.existRemainMember_id(entity.getMember_id());
      Integer syscount = sysuserrepo.existRemainMember_id(entity.getMember_id());

      //他のテーブルから削除対象の団員番号が参照されていれば削除できない。
      if(recevalcount != 0 || facicount != 0 || syscount != 0){
        return false;
      }

      try{
        //履歴情報を必ず残す。
        Member_Info_History hist = new Member_Info_History(entity, HistoryKindEnum.DELETE.getKind(), getLoginUser()[0]);
        membinfohist_repo.save(hist);

        membinfo_repo.delete(entity);

      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [MemberInfoServiceImpl:deleteData]");
      }

      return true;

    }else{
      return false;
    }
  }
}