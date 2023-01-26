/** 
 **************************************************************************************
 * @file Zenkaku.java
 * @brief バリデーション項目として[全角文字]の条件を定義したアノテーションインターフェースを
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
import javax.validation.constraints.Pattern;








/** 
 **************************************************************************************
 * @brief バリデーション項目として[全角文字]の条件を定義したアノテーションインターフェース
 * 
 * @details 
 * - このアノテーションは、既存のバリデーション用のアノテーションを組み合わせて独自に作成した
 * 物である。
 * - このアノテーションでは、正規表現によるバリデーションを使用して[全角文字]のみ受け付ける
 * バリデーションを実装している。
 * 
 * @note このアノテーションは[フィールド変数]に対してのみのアノテーションとなる。
 **************************************************************************************
 */ 
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Pattern(regexp="^[^ -~｡-ﾟ]*$")
public @interface Zenkaku{

  String message() default "missing zenkaku";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    Zenkaku[] values();
  }
}