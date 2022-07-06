package com.springproject.dockerspring.form.OriginalAnno;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;





/***************************************************************/
/*   [AuthConnectCheckValid]
/*   アノテーション「AuthConnectCheck」に指定された処理内容として
/*   機能する。
/***************************************************************/

public class AuthConnectCheckValid implements ConstraintValidator<AuthConnectCheck, Object>{

  private String message;

  /***********************************************************/
  /*   配列内の対応付けとして、
  /*   [i][0]を参照権限、
  /*   [i][1]を対応する変更権限、
  /*   [i][2]を対応する削除権限、
  /*   [i][3]を対応する履歴参照権限、
  /*   [i][4]を対応する履歴ロールバック権限とする。
  /***********************************************************/
  private String[][] auth = {
    {
      Usage_Authority_Enum.Memb_Info_Brows.getKey(), 
      Usage_Authority_Enum.Memb_Info_Change.getKey(), 
      Usage_Authority_Enum.Memb_Info_Delete.getKey(), 
      Usage_Authority_Enum.Memb_Info_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Memb_Info_Hist_Rollback.getKey()},
    {
      Usage_Authority_Enum.Rec_Eval_Brows.getKey(), 
      Usage_Authority_Enum.Rec_Eval_Change.getKey(), 
      Usage_Authority_Enum.Rec_Eval_Delete.getKey(), 
      Usage_Authority_Enum.Rec_Eval_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Rec_Eval_Hist_Rollback.getKey()},
    {
      Usage_Authority_Enum.Facility_Brows.getKey(), 
      Usage_Authority_Enum.Facility_Change.getKey(), 
      Usage_Authority_Enum.Facility_Delete.getKey(), 
      Usage_Authority_Enum.Facility_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Facility_Hist_Rollback.getKey()},
    {
      Usage_Authority_Enum.Equip_Insp_Brows.getKey(), 
      Usage_Authority_Enum.Equip_Insp_Change.getKey(), 
      Usage_Authority_Enum.Equip_Insp_Delete.getKey(), 
      Usage_Authority_Enum.Equip_Insp_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Equip_Insp_Hist_Rollback.getKey()},
    {
      Usage_Authority_Enum.Musical_Score_Brows.getKey(), 
      Usage_Authority_Enum.Musical_Score_Change.getKey(), 
      Usage_Authority_Enum.Musical_Score_Delete.getKey(), 
      Usage_Authority_Enum.Musical_Score_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Musical_Score_Hist_Rollback.getKey()},
    {
      Usage_Authority_Enum.Sound_Source_Brows.getKey(), 
      Usage_Authority_Enum.Sound_Source_Change.getKey(), 
      Usage_Authority_Enum.Sound_Source_Delete.getKey(), 
      Usage_Authority_Enum.Sound_Source_Hist_Brows.getKey(), 
      Usage_Authority_Enum.Sound_Source_Hist_Rollback.getKey()
    }
  };


  @Override
  public void initialize(AuthConnectCheck annotation){
    this.message = annotation.message();
  }


/****************************************************************/
/*  [isValid]
/*  権限同士の不整合チェックを行う。
/****************************************************************/
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    BeanWrapper bean = new BeanWrapperImpl(value);


    //見にくいが、並列処理とする。
    List<Boolean> errorcheck = Arrays.stream(this.auth).parallel().map(i -> {

      Boolean check = false;
      Boolean brows = (Boolean)bean.getPropertyValue(i[0]);
      Boolean change = (Boolean)bean.getPropertyValue(i[1]);
      Boolean delete = (Boolean)bean.getPropertyValue(i[2]);
      Boolean histbrows = (Boolean)bean.getPropertyValue(i[3]);
      Boolean rollback = (Boolean)bean.getPropertyValue(i[4]);

      /***************************************************************/
      /*   履歴ロールバック権限が付与されている場合は、「参照権限」
      /*   「変更権限」「履歴参照権限」が必要である。
      /***************************************************************/
      if(rollback){
        if(!brows || !change || !histbrows){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(message)
                 .addPropertyNode(i[4])
                 .addConstraintViolation();
          check = true;
        }
      }

      /***************************************************************/
      /*   履歴参照権限が付与されている場合は、「参照権限」
      /*   「変更権限」が必要である。
      /***************************************************************/
      if(histbrows){
        if(!brows || !change){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(message)
                 .addPropertyNode(i[3])
                 .addConstraintViolation();
          check = true;
        }
      }

      /***************************************************************/
      /*   削除権限が付与されている場合は、「参照権限」
      /*   「変更権限」が必要である。
      /***************************************************************/
      if(delete){
        if(!brows || !change){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(message)
                 .addPropertyNode(i[2])
                 .addConstraintViolation();
          check = true;
        }
      }

      /***************************************************************/
      /*   変更権限が付与されている場合は、「参照権限」が必要である。
      /***************************************************************/
      if(change){
        if(!brows){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(message)
                 .addPropertyNode(i[2])
                 .addConstraintViolation();
          check = true;
        }
      }

      return check;

    }).collect(Collectors.toList());


    //ここまでの検査でエラーが一つでもあれば、次の処理には進まない。
    if(errorcheck.contains(true)){
      return false;
    }


    //管理者権限との競合チェック。
    if((Boolean)bean.getPropertyValue(Usage_Authority_Enum.Admin.getKey())){

      /*************************************************************************/
      /*   全ての機能の参照権限を確認し、一つでも付与されていれば
      /*   エラーとする。参照権限が無ければ、他の権限が付与されていない事になる。
      /*************************************************************************/
      Boolean admincheck = false;
      for(String[] brows: auth){
        if((Boolean)bean.getPropertyValue(brows[0])){
          admincheck = true;
          break;
        }
      }

      if(admincheck){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addPropertyNode(Usage_Authority_Enum.Admin.getKey())
               .addConstraintViolation();
        return false;
      }
    }

    return true;
  }
}
