/** 
 **************************************************************************************
 * @file System_User_Form_Output_ValidatorImpl.java
 * @brief 主に[システムユーザ管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを
 * 格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Form.FormInterface.System_User_Form_Output_Validator;








/** 
 **************************************************************************************
 * @brief 主に[システムユーザ管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
 * 
 * @details 
 * - 主に、データ出力時に誤って不正なデータを出力してしまわないようにする目的で用いる。
 * - 使い方としては、このクラスと対になる通常用エンティティが存在するので、そのエンティティに検査
 * 対象データを格納した状態の物で渡す事で検査を実施する。
 * - 出力時に関しては、入力時に用いる手法でのバリデーションを用いることができない為、[Validator]
 * インターフェースを用いた手法で行う。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see System_User
 * @see System_User_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
public class System_User_Form_Output_ValidatorImpl implements System_User_Form_Output_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return System_User.class.isAssignableFrom(clazz);
  }








  /** 
   **************************************************************
   * @brief 実際のバリデーション処理を行う。各項目の検査処理は
   * 非公開メソッドに切り分けてあるので、そちらを参照。
   * 
   * @note 途中でバリデーションエラーが発生した場合は、判定用の
   * 真偽値が「False」になり、後続の処理は実行されずスルーするように
   * なっている。
   *************************************************************
   */
  @Override
  public void validate(Object target, Errors errors) {
    System_User entity = (System_User)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validSerialNum(judge, entity);}
    if(judge){validMemberId(judge, entity);}
    if(judge){validUsername(judge, entity);}
    if(judge){validPassword(judge, entity);}
    if(judge){validAuthId(judge, entity);}
    if(judge){validFailCount(judge, entity);}
    if(judge){validLocking(judge, entity);}

    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "System_User_Form_Output_Validator"));
    }
  }








  /** @name バリデーション用の非公開メソッド */
  /** @{ */

  /** 
   ***************************************************************
   * @brief シリアルナンバー
   * 
   * @details
   * -# 値が空でない事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validSerialNum(Boolean judge, System_User entity){
    Integer num = entity.getSerial_num();
    String serial_num = num != null ? String.valueOf(num) : null;

    if(serial_num == null 
        || !serial_num.matches(Regex_Enum.DIGIT.getRegex())
        || serial_num.length() > 9){

      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 団員番号
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validMemberId(Boolean judge, System_User entity){
    String member_id = entity.getMember_id();

    if(member_id == null || !member_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || member_id.length() > 30){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief ユーザー名
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字列が[半角英数字]のみで構成されている事。
   * -# 文字数が[8～20文字]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validUsername(Boolean judge, System_User entity){
    String username = entity.getUsername();

    if(username == null || !username.matches("^[0-9a-zA-Z]+$") || username.length() < 8 || username.length() > 20){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief パスワード
   * 
   * @details
   * -# 出力対象外の為、そもそも存在してはならない。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validPassword(Boolean judge, System_User entity){
    String password = entity.getPassword();

    if(judge && password != null){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 権限番号
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validAuthId(Boolean judge, System_User entity){
    String auth_id = entity.getAuth_id();

    if(auth_id == null || !auth_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || auth_id.length() > 30){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief ログイン失敗回数
   * 
   * @details
   * -# 出力対象外の為、そもそも存在してはならない。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validFailCount(Boolean judge, System_User entity){
    Integer fail_count = entity.getFail_count();

    if(judge && fail_count != null){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief ロック有無
   * 
   * @details
   * -# 値が空でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validLocking(Boolean judge, System_User entity){
    Boolean locking = entity.getLocking();

    if(judge && locking == null){
      judge = false;
    }
  }

  /** @} */
}
