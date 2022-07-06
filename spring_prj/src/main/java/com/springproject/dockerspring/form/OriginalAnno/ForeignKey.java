package com.springproject.dockerspring.form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;




/***********************************************************/
/*   [ForeignKey]
/*   「該当番号の指定テーブル内存在確認」に関する
/*    フォーム入力値の入力値のチェックを行う。（参照性制約）
/***********************************************************/

/***********************************************************/
/*   付与対象はフィールド変数のみとする。
/***********************************************************/

@Constraint(validatedBy = {ForeignKeyValid.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForeignKey{

  String message() default "no reference";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};
  String table();

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    ForeignKey[] values();
  }
}