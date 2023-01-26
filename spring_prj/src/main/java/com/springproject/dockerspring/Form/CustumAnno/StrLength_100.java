/** 
 **************************************************************************************
 * @file StrLength_100.java
 * @brief バリデーション項目として[文字列100文字以内]の条件を定義したアノテーションインターフェースを
 * 格納したファイル。
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

import org.hibernate.validator.constraints.Length;









/** 
 **************************************************************************************
 * @brief バリデーション項目として[文字列100文字以内]の条件を定義したアノテーションインターフェース
 * 
 * @details 
 * - このアノテーションは、既存のバリデーション用のアノテーションを組み合わせて独自に作成した
 * 物である。
 * - このアノテーションでは、[文字列100文字以内]のみ受け付けるバリデーションを実装している。
 * 
 * @note このアノテーションは[フィールド変数]に対してのみのアノテーションとなる。
 **************************************************************************************
 */ 
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Length(min=0, max=100)
public @interface StrLength_100{

  String message() default "missing StrLength_100";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    StrLength_100[] values();
  }
}