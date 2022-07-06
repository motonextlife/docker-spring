package com.springproject.dockerspring.form.OriginalAnno;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.service.ServiceInterface.EquipInspectService;
import com.springproject.dockerspring.service.ServiceInterface.FacilityService;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalItemsService;
import com.springproject.dockerspring.service.ServiceInterface.RecEvalService;
import com.springproject.dockerspring.service.ServiceInterface.ScoreService;
import com.springproject.dockerspring.service.ServiceInterface.SoundService;






/***********************************************************/
/*   [ForeignKeyValid]
/*   アノテーション「ForeignKey」に指定された処理内容として
/*   機能する。
/***********************************************************/

public class ForeignKeyValid implements ConstraintValidator<ForeignKey, Object>{

  @Autowired
  MemberInfoService membinfoserv;

  @Autowired
  FacilityService faciserv;
  
  @Autowired
  RecEvalItemsService recevalitemsserv;

  @Autowired
  RecEvalService recevalserv;

  @Autowired
  InspectItemsService inspectitemsserv;

  @Autowired
  EquipInspectService equipinspserv;

  @Autowired
  ScoreService scoreserv;

  @Autowired
  SoundService soundserv;

  // @Autowired
  // AdminService adminserv;  //サービスクラスに変える

  private String table;
  private String message;

  @Override
  public void initialize(ForeignKey annotation){
    this.table = annotation.table();
    this.message = annotation.message();
  }


/****************************************************************/
/*   [isValid]
/*   指定されたテーブル名にしたがって検索テーブルを選び、
/*   同時に指定された番号で検索を行う。
/*   番号が存在すれば「条件合致」とする。
/*   なお、番号が指定されていない場合は、無条件で「条件合致」となる。
/****************************************************************/

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    BeanWrapper bean = new BeanWrapperImpl(value);
    String id = (String)bean.getWrappedInstance();

    if(id.isEmpty() || id.isBlank()){
      return true;
    }

    Boolean judge = false;

    switch(this.table){
      case "Member_Info":
        judge = membinfoserv.existsData(id);
        break;
      case "Rec_Eval_Items":
        judge = recevalitemsserv.existsData(id);
        break;
      case "Rec_Eval":
        judge = recevalserv.existsData(id);
        break;
      case "Facility":
        judge = faciserv.existsData(id);
        break;
      case "Inspect_Items":
        judge = inspectitemsserv.existsData(id);
        break;
      case "Equip_Inspect":
        judge = equipinspserv.existsData(id);
        break;
      case "Musical_Score":
        judge = scoreserv.existsData(id);
        break;
      case "Sound_Source":
        judge = soundserv.existsData(id);
        break;
      // case "Usage_Authority":
      //   judge = adminserv.existsAuth(id);
      //   break;
      default:
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("アノテーションの設定に不備があります。").addConstraintViolation();
        return false;
    }

    if(judge){
      return true;
    }else{
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
      return false;
    }
  }
}
