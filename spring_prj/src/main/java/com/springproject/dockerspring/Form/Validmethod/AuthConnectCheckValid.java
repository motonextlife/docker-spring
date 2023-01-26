/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.Validmethod
 * 
 * @brief 完全独自実装したフォームクラス用のアノテーションに紐付いている処理用クラスを格納した
 * パッケージ。
 * 
 * @details
 * - このパッケージでは、独自実装のアノテーションを付加した項目に対して処理を行うメソッドを
 * 実装したクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.Validmethod;





/** 
 **************************************************************************************
 * @file AuthConnectCheckValid.java
 * @brief アノテーション[AuthConnectCheck]に対応する判定メソッドを実装したクラスを格納する
 * ファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Usage_Authority_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Authority_Kinds_Enum;
import com.springproject.dockerspring.Form.OriginalAnno.AuthConnectCheck;








/** 
 **************************************************************************************
 * @brief アノテーション[AuthConnectCheck]に対応する判定メソッドを実装したクラス
 * 
 * @details 
 * - このアノテーションの目的としては、入力された権限情報の辻褄を確認する為に用いる。
 * - 権限に管理者権限が含まれていた場合、他の権限は一切付与されていてはいけないという制約がある。
 * その制約を見たいしているかどうかを、各権限同士の関連を判定して判断する。
 * - 管理者権限が付与されているにも関わらず、どれか一つの他の権限が少しでも付与されていれば、判定は
 * エラーとなりバリデーションは不合格になる。
 * - ただし、管理者権限が付与されていなければ、権限同士の関連性は判定する必要がそもそもないため、
 * 無条件でバリデーションは合格となる。
 * 
 * @see AuthConnectCheck
 **************************************************************************************
 */ 
public class AuthConnectCheckValid implements ConstraintValidator<AuthConnectCheck, Object>{

  private String message;



  //! アノテーションに付与された属性値の取得
  @Override
  public void initialize(AuthConnectCheck annotation){
    this.message = annotation.message();
  }



  





  /** 
   *************************************************************************************
   * @brief 実際のバリデーション処理を行う。
   * 
   * @details
   * - このバリデーションはNullを許容する（Nullチェックは他のアノテーションに任せている）ため、
   * 対象のプロパティが存在しなければ、無条件でバリデーションを通過する。
   * - 管理者権限以外の権限で権限がない状態の値は「none」であるため、管理者権限が付与されている
   * 場合は、他の権限が「none」であることを確認する。
   * - レスポンスを早くするため、一個でも不合格になる物を見つけたら、処理を中断し判定を不合格とする。
   *************************************************************************************
   */
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    if(value == null){
      return true;
    }
    
    BeanWrapper bean = new BeanWrapperImpl(value);
    Object admin = bean.getPropertyValue(Usage_Authority_Enum.Admin.getEnName());
    if(admin == null){
      return true;
    }

    if((Boolean)admin){

      String[] auth_list = {
        Usage_Authority_Enum.Member_Info.getEnName(),
        Usage_Authority_Enum.Facility.getEnName(),
        Usage_Authority_Enum.Musical_Score.getEnName(),
        Usage_Authority_Enum.Sound_Source.getEnName()
      };
      
      for(String auth: auth_list){
        String judge = (String)bean.getPropertyValue(auth);

        if(judge != null && !judge.equals(Authority_Kinds_Enum.NONE.getKinds())){
          context.disableDefaultConstraintViolation();
          context.buildConstraintViolationWithTemplate(message)
                 .addPropertyNode(Usage_Authority_Enum.Admin.getEnName())
                 .addConstraintViolation();
          return false;
        }
      }
    }

    return true;
  }
}
