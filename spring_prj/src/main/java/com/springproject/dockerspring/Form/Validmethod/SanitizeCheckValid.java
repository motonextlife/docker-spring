/** 
 **************************************************************************************
 * @file SanitizeCheckValid.java
 * @brief アノテーション[SanitizeCheck]に対応する判定メソッドを実装したクラスを格納する
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.Validmethod;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.relational.core.dialect.Escaper;
import org.springframework.web.util.HtmlUtils;

import com.springproject.dockerspring.Form.OriginalAnno.SanitizeCheck;








/** 
 **************************************************************************************
 * @brief アノテーション[SanitizeCheck]に対応する判定メソッドを実装したクラス
 * 
 * @details 
 * - このアノテーションの目的としては、入力された文字列が正当性を確保していることを確認する。
 * - チェック項目としては、文字列内にXSSやディレクトリトラバーサルの危険性のある文字列がないか等
 * を判別する。
 * 
 * @see SanitizeCheck
 **************************************************************************************
 */ 
public class SanitizeCheckValid implements ConstraintValidator<SanitizeCheck, Object>{

  private String message;



  //! アノテーションに付与された属性値の取得
  @Override
  public void initialize(SanitizeCheck annotation){
    this.message = annotation.message();
  }








  

  /** 
   *************************************************************************************
   * @brief 実際のバリデーション処理を行う。
   * 
   * @details
   * - レスポンスを早くするため、一個でも不合格になる物を見つけたら、後続の処理をスルーする。
   * そしてそのまま不合格とする。
   * 
   * @par 判定の項目
   * -# HTMLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# SQLエスケープし、エスケープ後の文字列を比較。変化があれば不合格。
   * -# ヌル文字が含まれていないか確認する。
   * -# ファイルパス構成文字を調べ、含まれていたら不合格。
   *************************************************************************************
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    if(value == null){
      return true;
    }
    
    BeanWrapper bean = new BeanWrapperImpl(value);
    String target_str = (String)bean.getWrappedInstance();
    Boolean judge = true; 

    if(target_str == null || target_str.isEmpty() || target_str.isBlank()){
      return true;
    }


    judge = judge && target_str.equals(HtmlUtils.htmlEscape(target_str, "UTF-8"));
    judge = judge && target_str.equals(Escaper.of('\\').escape(target_str));
    judge = judge && !target_str.contains("\0");
    judge = judge && !(target_str.contains("/") || target_str.contains(".") || 
                       target_str.contains("\\")|| target_str.contains("~"));


    if(judge){ 
      return true;
    }else{
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message)
             .addConstraintViolation();
      return false;
    }
  }
}
