/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.CustumAnno
 * 
 * @brief バリデーション用のフォームクラスで使用するアノテーションのうち、既存のアノテーションを
 * 用いて作ったカスタムアノテーションを格納する。
 * 
 * @details
 * - このパッケージでは、既存のバリデーション用アノテーション(javaxやspringframework)を組み
 * 合わせて要件に沿うように作成したアノテーションを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CustumAnno;






/** 
 **************************************************************************************
 * @file AlnumHyphen.java
 * @brief バリデーション項目として[半角英数字]の条件を定義したアノテーションインターフェースを
 * 格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
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
 * @brief バリデーション項目として[半角英数字]の条件を定義したアノテーションインターフェース
 * 
 * @details 
 * - このアノテーションは、既存のバリデーション用のアノテーションを組み合わせて独自に作成した
 * 物である。
 * - このアノテーションでは、正規表現によるバリデーションを使用して[半角英数字]のみ受け付ける
 * バリデーションを実装している。
 * 
 * @note このアノテーションは[フィールド変数]に対してのみのアノテーションとなる。
 **************************************************************************************
 */ 
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Pattern(regexp="^[a-zA-Z0-9-]*$")
public @interface AlnumHyphen{

  String message() default "missing alnum and hyphen";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    AlnumHyphen[] values();
  }
}