/** 
 **************************************************************************************
 * @file Musical_Score_Form_Output_ValidatorImpl.java
 * @brief 主に[楽譜管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを格納
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
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Form.FormInterface.Musical_Score_Form_Output_Validator;








/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
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
 * @see Musical_Score
 * @see Musical_Score_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
public class Musical_Score_Form_Output_ValidatorImpl implements Musical_Score_Form_Output_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Musical_Score.class.isAssignableFrom(clazz);
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
    Musical_Score entity = (Musical_Score)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validSerialNum(judge, entity);}
    if(judge){validScoreId(judge, entity);}

    Boolean flag_1 = true;
    Date buy_date = judge ? validBuyDate(judge, entity, flag_1) : null;

    if(judge){validSongTitle(judge, entity);}
    if(judge){validComposer(judge, entity);}
    if(judge){validArranger(judge, entity);}
    if(judge){validPublisher(judge, entity);}
    if(judge){validStorageLoc(judge, entity);}

    Date disp_date = entity.getDisp_date();
    Boolean flag_2 = disp_date != null;

    if(judge){validOtherComment(judge, entity);}
    if(judge){dateLogicCheck(judge, flag_1, flag_2, buy_date, disp_date);}


    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Musical_Score_Form_Output_Validator"));
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
  private void validSerialNum(Boolean judge, Musical_Score entity){
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
   * @brief 楽譜番号
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
  private void validScoreId(Boolean judge, Musical_Score entity){
    String score_id = entity.getScore_id();

    if(score_id == null || !score_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || score_id.length() > 30){
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
  private Date validBuyDate(Boolean judge, Musical_Score entity, Boolean flag_1){
    Date buy_date = entity.getBuy_date();

    if(judge && buy_date == null){
      judge = false;
      flag_1 = false;
    }
    
    return buy_date;
  }







  /** 
   ***************************************************************
   * @brief 曲名
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
  private void validSongTitle(Boolean judge, Musical_Score entity){
    String song_title = entity.getSong_title();

    if(song_title == null || !song_title.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || song_title.length() > 50){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 作曲者
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
  private void validComposer(Boolean judge, Musical_Score entity){
    String composer = entity.getComposer();

    if(composer == null || !composer.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || composer.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 編曲者
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validArranger(Boolean judge, Musical_Score entity){
    String arranger = entity.getArranger();

    if(arranger == null || !arranger.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || arranger.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 出版社
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validPublisher(Boolean judge, Musical_Score entity){
    String publisher = entity.getPublisher();
    publisher = publisher == null ? "" : publisher;

    if(!publisher.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || publisher.length() > 50){
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
  private void validStorageLoc(Boolean judge, Musical_Score entity){
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
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validOtherComment(Boolean judge, Musical_Score entity){
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
