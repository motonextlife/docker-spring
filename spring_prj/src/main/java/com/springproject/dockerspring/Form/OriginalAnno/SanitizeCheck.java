/** 
 **************************************************************************************
 * @file SanitizeCheck.java
 * @brief バリデーション項目として[入力文字列の危険性確認]の条件を定義したアノテーション
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

import com.springproject.dockerspring.Form.Validmethod.SanitizeCheckValid;








/** 
 **************************************************************************************
 * @brief バリデーション項目として[入力文字列の危険性確認]の条件を定義したアノテーション
 * インターフェース
 * 
 * @details 
 * - このアノテーションは、判定用のメソッドを独自に実装し、そのメソッドによって判定を行う独自
 * アノテーションである。
 * - 厳密な実装内容は、バリデーションメソッドを参照の事。
 * 
 * @note このアノテーションは[フィールド変数]に対してのみのアノテーションとなる。
 * 
 * @see SanitizeCheckValid
 **************************************************************************************
 */ 
@Constraint(validatedBy = {SanitizeCheckValid.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SanitizeCheck{

  String message() default "include harmful characters";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    SanitizeCheck[] values();
  }
}