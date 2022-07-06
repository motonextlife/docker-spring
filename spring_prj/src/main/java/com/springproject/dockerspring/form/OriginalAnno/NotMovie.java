package com.springproject.dockerspring.form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;




/***********************************************************/
/*   [NotMovie]
/*   指定されたファイルが「動画」の場合は、「条件不一致」とする。
/***********************************************************/

/***********************************************************/
/*   付与対象はフィールド変数のみとする。
/***********************************************************/

@Constraint(validatedBy = {NotMovieValid.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotMovie{

  String message() default "not movie";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    NotMovie[] values();
  }
}