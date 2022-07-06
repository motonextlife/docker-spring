package com.springproject.dockerspring.form.OriginalAnno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;





/***********************************************************/
/*   [DateCorrCheckValid]
/*   アノテーション「DateCorrCheck」に指定された処理内容として
/*   機能する。
/***********************************************************/

public class DateCorrCheckValid implements ConstraintValidator<DateCorrCheck, Object>{

  private String[] start;
  private String[] end;
  private String message;

  @Override
  public void initialize(DateCorrCheck annotation){
    this.start = annotation.start();
    this.end = annotation.end();
    this.message = annotation.message();
  }
  

/*********************************************************************/
/*   [isValid]
/*   「開始日」と「終了日」の塊として配列で渡された日付を判定。
/*   関連は、同じインデックス同士で設定されている。
/*   指定された「開始日」以前に、指定された「終了日」が設定されていれば
/*   論理的に辻褄が合わないので「条件不一致」とする。
/*********************************************************************/

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    BeanWrapper bean = new BeanWrapperImpl(value);

    List<Boolean> checklist = new ArrayList<>();
    int checklen = 0;

    if(this.start.length == this.end.length){
      checklen = this.start.length;
    }else{
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("アノテーションの設定に不備があります。")
             .addPropertyNode(this.end[0]).addConstraintViolation();
      return false;
    }

    /*********************************************************************/
    /*   「開始日」が設定されていないにも関わらず、「終了日」が設定
    /*   されているときも辻褄が合わないので、「条件不一致」とする。
    /*   ※「開始日」のみの指定で、「終了日」がまだない分には問題ない。
    /*********************************************************************/
    for(int i = 0; i < checklen; i++){
      if(bean.getPropertyValue(this.end[i]) == null){
        checklist.add(true);
        continue;
      }

      if(bean.getPropertyValue(this.start[i]) == null){
        checklist.add(false);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(this.end[i]).addConstraintViolation();
        continue;
      }

      Date startdate = (Date)bean.getPropertyValue(this.start[i]);
      Date enddate = (Date)bean.getPropertyValue(this.end[i]);
      if(enddate.after(startdate) || enddate.equals(startdate)){
        checklist.add(true);
      }else{
        checklist.add(false);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(this.end[i]).addConstraintViolation();
      }
    }

    return checklist.stream().allMatch(s -> s == true);
  }
}
