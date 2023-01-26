/** 
 **************************************************************************************
 * @file Sound_Source_Form_Output_ValidatorImpl.java
 * @brief 主に[音源管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを格納
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
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.Form.FormInterface.Sound_Source_Form_Output_Validator;








/** 
 **************************************************************************************
 * @brief 主に[音源管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
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
 * @see Sound_Source
 * @see Sound_Source_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
public class Sound_Source_Form_Output_ValidatorImpl implements Sound_Source_Form_Output_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Sound_Source.class.isAssignableFrom(clazz);
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
    Sound_Source entity = (Sound_Source)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;


    if(judge){validSerialNum(judge, entity);}
    if(judge){validSoundId(judge, entity);}
    if(judge){validUploadDate(judge, entity);}
    if(judge){validSongTitle(judge, entity);}
    if(judge){validComposer(judge, entity);}
    if(judge){validPerformer(judge, entity);}
    if(judge){validPublisher(judge, entity);}
    if(judge){validOtherComment(judge, entity);}


    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Sound_Source_Form_Output_Validator"));
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
  private void validSerialNum(Boolean judge, Sound_Source entity){
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
   * @brief 音源番号
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
  private void validSoundId(Boolean judge, Sound_Source entity){
    String sound_id = entity.getSound_id();

    if(sound_id == null || !sound_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || sound_id.length() > 30){
      judge = false;
    }
  }






  /** 
   ***************************************************************
   * @brief 登録日
   * 
   * @details
   * -# 値が空でない事。
   *
   * @return 抽出した購入日データ
   * 
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validUploadDate(Boolean judge, Sound_Source entity){
    Date upload_date = entity.getUpload_date();

    if(judge && upload_date == null){
      judge = false;
    }
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
  private void validSongTitle(Boolean judge, Sound_Source entity){
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
  private void validComposer(Boolean judge, Sound_Source entity){
    String composer = entity.getComposer();

    if(composer == null || !composer.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || composer.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 演奏者
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
  private void validPerformer(Boolean judge, Sound_Source entity){
    String performer = entity.getPerformer();

    if(performer == null || !performer.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || performer.length() > 50){
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
  private void validPublisher(Boolean judge, Sound_Source entity){
    String publisher = entity.getPublisher();
    publisher = publisher == null ? "" : publisher;

    if(!publisher.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || publisher.length() > 50){
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
  private void validOtherComment(Boolean judge, Sound_Source entity){
    String other_comment = entity.getOther_comment();
    other_comment = other_comment == null ? "" : other_comment;

    if(!other_comment.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || other_comment.length() > 400){
      judge = false;
    }
  }

  /** @} */
}
