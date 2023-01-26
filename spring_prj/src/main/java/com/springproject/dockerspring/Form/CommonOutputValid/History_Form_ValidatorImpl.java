/** 
 **************************************************************************************
 * @file History_Form_ValidatorImpl.java
 * @brief 共通の履歴付随情報の出力時のバリデーションにおいて、バリデーションの処理を実行する
 * クラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CommonOutputValid;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Form.FormInterface.History_Form_Validator;








/** 
 **************************************************************************************
 * @brief 共通の履歴付随情報の出力時のバリデーションにおいて、バリデーションの処理を実行する
 * クラス
 * 
 * @details 
 * - 主に、データ出力時に誤って不正なデータを出力してしまわないようにする目的で用いる。
 * - 使い方としては、このクラスと対になるフォームクラスが存在するので、そのフォームクラスに検査
 * 対象データを格納した状態の物で渡す事で検査を実施する。
 * - 出力時に関しては、入力時に用いる手法でのバリデーションを用いることができない為、[Validator]
 * インターフェースを用いた手法で行う。
 * - 通常は履歴付随情報のほかに、履歴本体のデータが存在するが、ここでは共通部分となる履歴付随情報
 * のみが対象となる。その他の履歴本体の情報のバリデーションは各機能の出力用バリデーションに処理を
 * 委託するものとする。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see History_Form
 * @see History_Form_Validator
 **************************************************************************************
 */
@Component
public class History_Form_ValidatorImpl implements History_Form_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return History_Form.class.isAssignableFrom(clazz);
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
    History_Form form = (History_Form)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validHistoryId(judge, form);}
    if(judge){validChangeDatetime(judge, form);}
    if(judge){validChangeKinds(judge, form);}
    if(judge){validOperationUser(judge, form);}

    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "History_Form_ValidatorImpl"));
    }
  }









  /** @name バリデーション用の非公開メソッド */
  /** @{ */

  /** 
   ***************************************************************
   * @brief 履歴番号
   * 
   * @details
   * -# 値が空でない事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validHistoryId(Boolean judge, History_Form form){
    Integer num = form.getHistory_id();
    String history_id = num != null ? String.valueOf(num) : null;

    if(history_id == null 
        || !history_id.matches(Regex_Enum.DIGIT.getRegex())
        || history_id.length() > 9){

      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 履歴記録日時
   * 
   * @details
   * -# 値が空でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validChangeDatetime(Boolean judge, History_Form form){
    Date change_datetime = form.getChange_datetime();

    if(change_datetime == null){
      judge = false;
    }
  }






  /** 
   ***************************************************************
   * @brief 履歴種別
   * 
   * @details
   * -# 値が空でない事。
   * -# 所定の文字列のみ受け付ける。
   * (insert, update, delete, rollback)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validChangeKinds(Boolean judge, History_Form form){
    String change_kinds = form.getChange_kinds();

    if(change_kinds == null || change_kinds.isEmpty() || change_kinds.isBlank() 
        || !change_kinds.matches("insert|update|delete|rollback")){

      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 操作ユーザー名
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字列が[半角英数字]のみで構成されている事。
   * -# 文字数が[8~20文字以内]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] form 検査対象のデータを格納したフォーム
   ***************************************************************
   */
  private void validOperationUser(Boolean judge, History_Form form){
    String operation_user = form.getOperation_user();

    if(operation_user == null || operation_user.isEmpty() || operation_user.isBlank() 
        || !operation_user.matches("^[0-9a-zA-Z]*$") 
        || operation_user.length() < 8 || operation_user.length() > 20){

      judge = false;
    }
  }

  /** @} */
}
