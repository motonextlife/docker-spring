package com.springproject.dockerspring.form.OriginalAnno;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;




/***********************************************************/
/*   [NotAudio]
/*   指定されたファイルが「音声」の場合は、「条件不一致」とする。
/***********************************************************/

/***********************************************************/
/*   付与対象はフィールド変数のみとする。
/***********************************************************/

@Constraint(validatedBy = {NotAudioValid.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotAudio{

  String message() default "not pdf";
  Class<? extends Payload>[] payload() default {};
  Class<?>[] groups() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    NotAudio[] values();
  }
}