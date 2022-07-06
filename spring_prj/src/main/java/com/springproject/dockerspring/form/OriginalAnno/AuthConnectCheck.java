package com.springproject.dockerspring.form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;




/***********************************************************/
/*   [AuthConnectCheck]
/*   クラス内の権限同士の論理チェックを行う。
/*   権限同士の辻褄が合わなければ、「条件不一致」となる。
/***********************************************************/

/***********************************************************/
/*   付与対象はクラス全体とする。
/***********************************************************/

@Constraint(validatedBy = {AuthConnectCheckValid.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthConnectCheck{

  String message() default "role inconsistent";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    AuthConnectCheck[] values();
  }
}