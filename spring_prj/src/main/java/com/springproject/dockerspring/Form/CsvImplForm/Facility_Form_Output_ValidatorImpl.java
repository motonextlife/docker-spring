/** 
 **************************************************************************************
 * @file Facility_Form_Output_ValidatorImpl.java
 * @brief 主に[設備管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを格納
 * したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CsvImplForm;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Form.FormInterface.Facility_Form_Output_Validator;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
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
 * @see Facility
 * @see Facility_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
public class Facility_Form_Output_ValidatorImpl implements Facility_Form_Output_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Facility.class.isAssignableFrom(clazz);
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
    Facility entity = (Facility)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validSerialNum(judge, entity);}
    if(judge){validFaciId(judge, entity);}
    if(judge){validFaciName(judge, entity);}
    
    Boolean flag_1 = true;
    Date buy_date = judge ? validBuyDate(judge, entity, flag_1) : null;

    if(judge){validProducer(judge, entity);}
    if(judge){validStorageLoc(judge, entity);}

    Date disp_date = entity.getDisp_date();
    Boolean flag_2 = disp_date != null;
    
    if(judge){validOtherComment(judge, entity);}
    if(judge){dateLogicCheck(judge, flag_1, flag_2, buy_date, disp_date);}


    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Facility_Form_Outpu_tValidatorImpl"));
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
  private void validSerialNum(Boolean judge, Facility entity){
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
   * @brief 設備番号
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
  private void validFaciId(Boolean judge, Facility entity){
    String faci_id = entity.getFaci_id();

    if(faci_id == null || !faci_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || faci_id.length() > 30){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 設備名
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validFaciName(Boolean judge, Facility entity){
    String faci_name = entity.getFaci_name();

    if(faci_name == null || !faci_name.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || faci_name.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 購入日
   * 
   * @details
   * -# 値が空でない事。
   * 
   * @return 抽出した購入日データ
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   * @param[in] flag_1 日付論理チェックの際に使用する真偽値フラグ
   ***************************************************************
   */
  private Date validBuyDate(Boolean judge, Facility entity, Boolean flag_1){
    Date buy_date = entity.getBuy_date();
    
    if(judge && buy_date == null){
      judge = false;
      flag_1 = false;
    }

    return buy_date;
  }








  /** 
   ***************************************************************
   * @brief 製作者
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validProducer(Boolean judge, Facility entity){
    String producer = entity.getProducer();

    if(producer == null || !producer.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || producer.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 保管場所
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validStorageLoc(Boolean judge, Facility entity){
    String storage_loc = entity.getStorage_loc();
    storage_loc = storage_loc == null ? "" : storage_loc;

    if(!storage_loc.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || storage_loc.length() > 50){
      judge = false;
    }
  }






  /** 
   ***************************************************************
   * @brief その他コメント
   * 
   * @details
   * -# 文字数が[400文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validOtherComment(Boolean judge, Facility entity){
    String other_comment = entity.getOther_comment();
    other_comment = other_comment == null ? "" : other_comment;

    if(!other_comment.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || other_comment.length() > 400){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 「購入日」と「廃棄日」の論理チェック
   * 
   * @details
   * -# 「購入日」が「廃棄日」以前に設定されている事を判別。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] flag_1 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] buy_date 検査対象購入日
   * @param[in] disp_date 検査対象廃棄日
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck(Boolean judge, Boolean flag_1, Boolean flag_2, Date buy_date, Date disp_date){
    if(flag_1 && flag_2 && buy_date != null && buy_date.after(disp_date)){
      judge = false;
    }
  }

  /** @} */
}
