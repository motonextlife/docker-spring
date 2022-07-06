package com.springproject.dockerspring.form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;




/***********************************************************/
/*   [DateCorrCheck]
/*   指定された日付同士の論理チェックを行う。
/*   辻褄の合わない関連の日付を防ぐ。
/***********************************************************/

/***********************************************************/
/*   付与対象はクラス全体とする。
/***********************************************************/

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