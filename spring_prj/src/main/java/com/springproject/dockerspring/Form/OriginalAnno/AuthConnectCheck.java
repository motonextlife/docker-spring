/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.OriginalAnno
 * 
 * @brief バリデーションのフォームクラスで使用するアノテーションのうち、完全に独自実装した
 * アノテーションを格納するパッケージ。
 * 
 * @details
 * - このパッケージでは、独自作成したアノテーションのみ格納する。これらのアノテーションに紐つけ
 * られた処理メソッドは、別のパッケージに格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.OriginalAnno;






/** 
 **************************************************************************************
 * @file AuthConnectCheck.java
 * @brief バリデーション項目として[権限情報の整合性確認]の条件を定義したアノテーション
 * インターフェースを格納したファイル。
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

import com.springproject.dockerspring.Form.Validmethod.AuthConnectCheckValid;







/** 
 **************************************************************************************
 * @brief バリデーション項目として[権限情報の整合性確認]の条件を定義したアノテーション
 * インターフェース
 * 
 * @details 
 * - このアノテーションは、判定用のメソッドを独自に実装し、そのメソッドによって判定を行う独自
 * アノテーションである。
 * - 厳密な実装内容は、バリデーションメソッドを参照の事。
 * 
 * @note このアノテーションは[クラス]に対してのみのアノテーションとなる。
 * 
 * @see AuthConnectCheckValid
 **************************************************************************************
 */ 
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