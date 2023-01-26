/** 
 **************************************************************************************
 * @file SerialNumCheck.java
 * @brief バリデーション項目として[シリアルナンバーや履歴番号]の条件を定義したアノテーション
 * インターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CustumAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;








/** 
 **************************************************************************************
 * @brief バリデーション項目として[シリアルナンバーや履歴番号]の条件を定義したアノテーション
 * インターフェース
 * 
 * @details 
 * - このアノテーションは、既存のバリデーション用のアノテーションを組み合わせて独自に作成した
 * 物である。
 * - このアノテーションでは、[9桁以内かつ0を含めた正の数の数値の文字列]のみ受け付ける
 * バリデーションを実装している。
 * 
 * @note このアノテーションは[フィールド変数]に対してのみのアノテーションとなる。
 **************************************************************************************
 */ 
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Digits(integer=9, fraction=0)
@PositiveOrZero
public @interface SerialNumCheck{

  String message() default "missing serialnum";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    SerialNumCheck[] values();
  }
}