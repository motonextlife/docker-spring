package com.springproject.dockerspring.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;
import com.springproject.dockerspring.entity.NormalEntity.SysPass_Join;
import com.springproject.dockerspring.entity.NormalEntity.System_User;
import com.springproject.dockerspring.entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.repository.HistoryRepo.AudioDataHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.EquipInspHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.EquipInspPhotoHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.FaciHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.InspItemsHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.MembInfoHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.MusicScoreHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalItemsHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.RecEvalRecordHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.ScorePdfHistRepo;
import com.springproject.dockerspring.repository.HistoryRepo.SoundSourceHistRepo;
import com.springproject.dockerspring.repository.NormalRepo.SysPassJoinRepo;
import com.springproject.dockerspring.repository.NormalRepo.SysUserRepo;
import com.springproject.dockerspring.repository.NormalRepo.UsaAuthRepo;
import com.springproject.dockerspring.security.RoleEnum;
import com.springproject.dockerspring.service.OriginalException.DbActionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




/**************************************************************/
/*   [AdminServiceImpl]
/*   ????????????????????????????????????????????????
/**************************************************************/
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService{

  @Autowired
  private AudioDataHistRepo audiohist_repo;

  @Autowired
  private EquipInspHistRepo equiphist_repo;

  @Autowired
  private EquipInspPhotoHistRepo equipphoto_repo;

  @Autowired
  private FaciHistRepo faci_repo;

  @Autowired
  private InspItemsHistRepo inspitem_repo;

  @Autowired
  private MembInfoHistRepo membinfo_repo;

  @Autowired
  private MusicScoreHistRepo score_repo;

  @Autowired
  private RecEvalHistRepo receval_repo;

  @Autowired
  private RecEvalItemsHistRepo evalitem_repo;

  @Autowired
  private RecEvalRecordHistRepo evalrecord_repo;

  @Autowired
  private ScorePdfHistRepo pdf_repo;

  @Autowired
  private SoundSourceHistRepo sound_repo;

  @Autowired
  private SysPassJoinRepo syspass_repo;

  @Autowired
  private SysUserRepo sysuser_repo;

  @Autowired
  private UsaAuthRepo usaauth_repo;






  /*******************************************/
  /*   [deleteHistory(1)]
  /*   ????????????????????????????????????????????????
  /*   ????????????????????????????????????
  /*******************************************/
  @Override
  public Boolean deleteHistory(String table_name, Date start_datetime, Date end_datetime) {

    /**********************************************************************/
    /* ????????????TableNameEnum???????????????????????????????????????????????????????????????
    /* ???????????????????????????????????????
    /* ?????????????????????????????????????????????????????????????????????????????????
    /**********************************************************************/
    Optional<TableNameEnum> table = Arrays.stream(TableNameEnum.values())
                                          .parallel()
                                          .filter(s -> s.name().equals(table_name))
                                          .findFirst();

    if(table.isEmpty()){
      return false;
    }

    /**********************************************************************/
    /* ?????????????????????????????????????????????????????????????????????????????????????????????
    /**********************************************************************/
    switch(table.get()){
      case Audio_Data:
        audiohist_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Equip_Inspect_Photo:
        equiphist_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Equip_Inspect:
        equipphoto_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Facility:
        faci_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Inspect_Items:
        inspitem_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Member_Info:
        membinfo_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Musical_Score:
        score_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Rec_Eval_Items:
        evalitem_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Rec_Eval_Record:
        evalrecord_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Rec_Eval:
        receval_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Score_Pdf:
        pdf_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      case Sound_Source:
        sound_repo.deleteByDateBetween(start_datetime, end_datetime);
        break;
      default:
        return false;
    }

    return true;
  }







  /*******************************************/
  /*   [deleteHistory(2)]
  /*   ??????????????????????????????????????????????????????
  /*   ??????????????????
  /*******************************************/
  @Override
  public void deleteHistory(Date start_datetime, Date end_datetime) {

    audiohist_repo.deleteByDateBetween(start_datetime, end_datetime);
    equiphist_repo.deleteByDateBetween(start_datetime, end_datetime);
    faci_repo.deleteByDateBetween(start_datetime, end_datetime);
    inspitem_repo.deleteByDateBetween(start_datetime, end_datetime);
    membinfo_repo.deleteByDateBetween(start_datetime, end_datetime);
    score_repo.deleteByDateBetween(start_datetime, end_datetime);
    evalitem_repo.deleteByDateBetween(start_datetime, end_datetime);
    evalrecord_repo.deleteByDateBetween(start_datetime, end_datetime);
    receval_repo.deleteByDateBetween(start_datetime, end_datetime);
    pdf_repo.deleteByDateBetween(start_datetime, end_datetime);
    sound_repo.deleteByDateBetween(start_datetime, end_datetime);
  }







  /*******************************************/
  /*   [sysUserList]
  /*   ????????????????????????????????????????????????????????????
  /*   ???????????????????????????????????????????????????
  /*******************************************/
  @Override
  public Iterable<SysPass_Join> sysUserList(String word, String subject, Boolean order) {
    return syspass_repo.findAllJudge(word, subject, order);
  }






  /*******************************************/
  /*   [checkSysUser]
  /*   ??????????????????????????????????????????????????????
  /*   ??????????????????????????????????????????
  /*******************************************/
  @Override
  public Optional<SysPass_Join> checkSysUser(String member_id) {
    return syspass_repo.findByMember_id(member_id);
  }





  /*******************************************/
  /*   [existsSysUser]
  /*   ????????????????????????????????????????????????
  /*   ???????????????
  /*******************************************/
  @Override
  public Boolean existsSysUser(String member_id) {
    return syspass_repo.findByMember_id(member_id).isPresent();
  }





  /*******************************************/
  /*   [changeSysUser]
  /*   ??????????????????????????????????????????????????????????????????
  /*   ???????????????????????????????????????????????????
  /*******************************************/
  @Override
  public System_User changeSysUser(System_User entity, Boolean new_data) throws DbActionException {

    try{
      //????????????????????????????????????????????????????????????????????????????????????
      if(new_data){
        sysuser_repo.makeSlot(entity.getMember_id());
      }

      return sysuser_repo.save(entity);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [AdminServiceImpl:changeSysUser]");
    }
  }





  /*******************************************/
  /*   [deleteSysUser]
  /*   ????????????????????????????????????????????????
  /*******************************************/
  @Override
  public void deleteSysUser(String member_id) throws DbActionException {
    Optional<System_User> opt = sysuser_repo.findById(member_id);
    
    if(opt.isPresent()){
      System_User entity = opt.get();

      try{
        sysuser_repo.delete(entity);
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [AdminServiceImpl:deleteSysUser]");
      }
    }
  }






  /*******************************************/
  /*   [authList]
  /*   ????????????????????????????????????????????????????????????
  /*   ??????????????????????????????????????????
  /*******************************************/
  @Override
  public Iterable<Usage_Authority> authList(String word, String subject, Boolean order) {
    return usaauth_repo.findAllJudge(word, subject, order);
  }







  /*******************************************/
  /*   [checkAuth]
  /*   ?????????????????????????????????????????????
  /*   ???????????????????????????
  /*******************************************/
  @Override
  public Optional<Usage_Authority> checkAuth(String auth_id) {
    return usaauth_repo.findByAuth_id(auth_id);
  }





  /*******************************************/
  /*   [existsAuth]
  /*   ??????????????????????????????????????????????????????
  /*******************************************/
  @Override
  public Boolean existsAuth(String auth_id) {
    return usaauth_repo.findByAuth_id(auth_id).isPresent();
  }





  /*************************************************************/
  /*   [duplicateAuth]
  /*   ???????????????????????????????????????????????????????????????????????????
  /*   ????????????????????????????????????????????????
  /*************************************************************/
  @Override
  public Boolean duplicateAuth(Integer serial_num, String auth_id) {

    if(usaauth_repo.existsById(serial_num) && existsAuth(auth_id)){

      List<Usage_Authority> list = (ArrayList<Usage_Authority>)usaauth_repo.findAll();
      
      try{
        //?????????????????????????????????????????????????????????????????????????????????
        return list.stream()
                   .parallel()
                   .filter(s -> s.getSerial_num() != serial_num)
                   .map(s -> s.getAuth_id())
                   .noneMatch(s -> s.equals(auth_id));

      }catch(NullPointerException e){
        throw new NullPointerException("Error location [AdminServiceImpl:duplicateAuth]");
      }
    }

    return false;
  }





  /*************************************************/
  /*   [changeAuth]
  /*   ?????????????????????????????????????????????????????????????????????
  /*   ???????????????????????????????????????????????????
  /*************************************************/
  @Override
  public Usage_Authority changeAuth(Usage_Authority entity) throws DbActionException {

    try{
      return usaauth_repo.save(entity);

    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [AdminServiceImpl:changeAuth]");
    }
  }





  /*******************************************/
  /*   [deleteAuth]
  /*   ???????????????????????????????????????
  /*******************************************/
  @Override
  public Boolean deleteAuth(Integer serial_num) throws DbActionException {
    Optional<Usage_Authority> opt = usaauth_repo.findById(serial_num);

    if(opt.isPresent()){
      Usage_Authority entity = opt.get();

      Integer sysusercount = sysuser_repo.existRemainAuth_id(entity.getAuth_id());

      //????????????????????????????????????????????????????????????????????????????????????????????????
      if(sysusercount != 0){
        return false;
      }

      try{
        usaauth_repo.delete(entity);
      }catch(DbActionExecutionException e){
        throw new DbActionException("Error location [AdminServiceImpl:deleteAuth]");
      }

      return true;

    }else{
      return false;
    }
  }





  /*******************************************/
  /*   [judgeUserAuth]
  /*   ????????????????????????????????????????????????????????????
  /*   ?????????????????????????????????????????????????????????
  /*   ???????????????
  /*******************************************/
  @Override
  public Boolean judgeUserAuth(Usage_Authority_Enum authname) {
    
    //?????????????????????????????????????????????????????????????????????
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getPrincipal().getClass().getSimpleName();
    Optional<String> role = auth.getAuthorities().stream().map(s -> s.getAuthority()).findFirst();

    if(role.isEmpty()){
      return false;
    }

    if(role.get().equals(RoleEnum.ROLE_CREATOR.name())){

      return true;  //???????????????????????????????????????????????????

    }else if(role.get().equals(RoleEnum.ROLE_ADMIN_AND_USER.name())){

      Optional<System_User> sysuser = sysuser_repo.sysSecurity(username);

      if(sysuser.isEmpty()){
        return false;
      }

      Optional<Usage_Authority> usage = usaauth_repo.findByAuth_id(sysuser.get().getAuth_id());

      if(usage.isEmpty()){
        return false;
      }

      Boolean admin = usage.get().getAuthority(Usage_Authority_Enum.Admin);
      Boolean normal = usage.get().getAuthority(authname);

      return admin || normal;  //?????????????????????????????????????????????????????????????????????????????????????????????

    }else{
      return false;
    }
  }






  /*************************************************************/
  /*   [parseString]
  /*   ????????????????????????????????????????????????????????????????????????
  /*   ??????????????????????????????????????????
  /*************************************************************/
  @Override
  public Map<String, String> parseStringS(SysPass_Join entity) {
    return entity.makeMap();
  }


  @Override
  public Map<String, String> parseStringA(Usage_Authority entity) {
    return entity.makeMap();
  }






  /*************************************************************/
  /*   [mapPacking]
  /*   ??????????????????????????????????????????????????????????????????????????????
  /*   ???????????????????????????
  /*************************************************************/
  @Override
  public List<Map<String, String>> mapPackingS(Iterable<SysPass_Join> datalist) {

    List<SysPass_Join> list = (ArrayList<SysPass_Join>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseStringS(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }


  @Override
  public List<Map<String, String>> mapPackingA(Iterable<Usage_Authority> datalist) {

    List<Usage_Authority> list = (ArrayList<Usage_Authority>)datalist;
    List<Map<String, String>> result = new ArrayList<>();

    list.stream()
        .parallel()
        .map(s -> parseStringA(s))
        .forEachOrdered(s -> result.add(s));

    return result;
  }
}