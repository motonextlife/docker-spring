/** 
 **************************************************************************************
 * @file DateCorrCheck.java
 * @brief バリデーション項目として[日付の論理チェック]の条件を定義したアノテーション
 * インターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.springproject.dockerspring.Form.Validmethod.DateCorrCheckValid;








/** 
 **************************************************************************************
 * @brief バリデーション項目として[日付の論理チェック]の条件を定義したアノテーション
 * インターフェース
 * 
 * @details 
 * - このアノテーションは、判定用のメソッドを独自に実装し、そのメソッドによって判定を行う独自
 * アノテーションである。
 * - 厳密な実装内容は、バリデーションメソッドを参照の事。
 * 
 * @note このアノテーションは[クラス]に対してのみのアノテーションとなる。
 * 
 * @see DateCorrCheckValid
 **************************************************************************************
 */ 
@Constraint(validatedBy = {DateCorrCheckValid.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateCorrCheck{

  String message() default "date inconsistent";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};
  String[] start();
  String[] end();

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    DateCorrCheck[] values();
  }
}