/** 
 **************************************************************************************
 * @file DateCorrCheckValid.java
 * @brief アノテーション[DateCorrCheck]に対応する判定メソッドを実装したクラスを格納する
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.Validmethod;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.springproject.dockerspring.Form.OriginalAnno.DateCorrCheck;








/** 
 **************************************************************************************
 * @brief アノテーション[DateCorrCheck]に対応する判定メソッドを実装したクラス
 * 
 * @details 
 * - このアノテーションの目的としては、入力された二つの日付の関連が論理的におかしくないかの判定を
 * 行う。
 * - 例えば、誕生日と入団日を比べた場合、普通に考えて誕生日以前に入団日が来ることはありえないので、
 * 比較対象の日付よりも以前の日付にされていることで、ありえないような設定になることを防ぐ。
 * 
 * @see DateCorrCheck
 **************************************************************************************
 */ 
public class DateCorrCheckValid implements ConstraintValidator<DateCorrCheck, Object>{

  private String[] start;
  private String[] end;
  private String message;


  
  //! アノテーションに付与された属性値の取得
  @Override
  public void initialize(DateCorrCheck annotation){
    this.start = annotation.start();
    this.end = annotation.end();
    this.message = annotation.message();
  }
  







  /** 
   *************************************************************************************
   * @brief 実際のバリデーション処理を行う。
   * 
   * @details
   * - このバリデーションはNullを許容する（Nullチェックは他のアノテーションに任せている）ため、
   * 対象のプロパティが存在しなければ、無条件でバリデーションを通過する。
   * - 指定された「開始日」以前に、指定された「終了日」が設定されていれば論理的に辻褄が合わないので
   * 「条件不一致」とする。
   * - なお、「開始日」のみの指定で、「終了日」がまだない分には問題ない。
   * - レスポンスを早くするため、一個でも不合格になる物を見つけたら、処理を中断し判定を不合格とする。
   *************************************************************************************
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    if(value == null){
      return true;
    }
    
    BeanWrapper bean = new BeanWrapperImpl(value);
    int checklen = 0;

    if(this.start.length == this.end.length){
      checklen = this.start.length;
    }else{
      throw new IllegalArgumentException("Error location [DateCorrCheckValid:isValid]");
    }


    for(int i = 0; i < checklen; i++){

      if(bean.getPropertyValue(this.end[i]) == null){
        continue;
      }

      if(bean.getPropertyValue(this.start[i]) == null){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addPropertyNode(this.start[i])
               .addConstraintViolation();
        return false;
      }

      Date start_date = (Date)bean.getPropertyValue(this.start[i]);
      Date end_date = (Date)bean.getPropertyValue(this.end[i]);

      if(end_date != null && end_date.before(start_date)){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addPropertyNode(this.end[i])
               .addConstraintViolation();
        return false;
      }
    }

    return true;
  }
}
