/** 
 **************************************************************************************
 * @file Usage_Authority_Form_Output_ValidatorImpl.java
 * @brief 主に[権限管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを
 * 格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Authority_Kinds_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Form.FormInterface.Usage_Authority_Form_Output_Validator;









/** 
 **************************************************************************************
 * @brief 主に[権限管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
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
 * @see Usage_Authority
 * @see Usage_Authority_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
public class Usage_Authority_Form_Output_ValidatorImpl implements Usage_Authority_Form_Output_Validator {


  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Usage_Authority.class.isAssignableFrom(clazz);
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
    Usage_Authority entity = (Usage_Authority)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;


    if(judge){validSerialNum(judge, entity);}
    if(judge){validAuthId(judge, entity);}
    if(judge){validAuthName(judge, entity);}
    Boolean admin = judge ? validAdmin(judge, entity) : null;
    String member_info = judge ? validMemberInfo(judge, entity) : null;
    String facility = judge ? validFacility(judge, entity) : null;
    String musical_score = judge ? validMusicalScore(judge, entity) : null;
    String sound_source = judge ? validSoundSource(judge, entity) : null;
    if(judge){checkIntegrity(judge, admin, member_info, facility, musical_score, sound_source);}


    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Usage_Authority_Form_Output_Validator"));
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
  private void validSerialNum(Boolean judge, Usage_Authority entity){
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
  private void validAuthId(Boolean judge, Usage_Authority entity){
    String auth_id = entity.getAuth_id();

    if(auth_id == null || !auth_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || auth_id.length() > 30){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 権限名
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字列が[全角文字]のみで構成されている事。
   * -# 文字数が[50文字以内]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validAuthName(Boolean judge, Usage_Authority entity){
    String auth_name = entity.getAuth_name();

    if(auth_name == null || !auth_name.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || auth_name.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 権限名
   * 
   * @details
   * -# 値が空でない事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private Boolean validAdmin(Boolean judge, Usage_Authority entity){
    Boolean admin = entity.getAdmin();

    if(judge && admin == null){
      judge = false;
    }

    return admin;
  }







  /** 
   ***************************************************************
   * @brief 団員管理権限
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private String validMemberInfo(Boolean judge, Usage_Authority entity){
    String member_info = entity.getMember_info();

    if(member_info == null || !member_info.matches("none|brows|change|delete|hist|rollback")){
      judge = false;
    }

    return member_info;
  }






  /** 
   ***************************************************************
   * @brief 設備管理権限
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private String validFacility(Boolean judge, Usage_Authority entity){
    String facility = entity.getFacility();

    if(facility == null || !facility.matches("none|brows|change|delete|hist|rollback")){
      judge = false;
    }

    return facility;
  }







  /** 
   ***************************************************************
   * @brief 楽譜管理権限
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private String validMusicalScore(Boolean judge, Usage_Authority entity){
    String musical_score = entity.getMusical_score();

    if(musical_score == null || !musical_score.matches("none|brows|change|delete|hist|rollback")){
      judge = false;
    }

    return musical_score;
  }






  /** 
   ***************************************************************
   * @brief 音源管理権限
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private String validSoundSource(Boolean judge, Usage_Authority entity){
    String sound_source = entity.getSound_source();

    if(sound_source == null || !sound_source.matches("none|brows|change|delete|hist|rollback")){
      judge = false;
    }

    return sound_source;
  }







  /** 
   ***************************************************************
   * @brief 管理者権限との不整合チェック
   * 
   * @details
   * -# 全ての機能の権限を確認し、一つでも「none」以外が付与されていれば
   * エラーとする。
   * -# なお管理者権限が付与されていなければ、バリデーションの必要はない。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[out] admin 管理者権限
   * @param[out] member_info 団員管理権限
   * @param[out] facility 設備管理権限
   * @param[out] musical_score 楽譜管理権限
   * @param[out] sound_source 音源管理権限
   ***************************************************************
   */
  private void checkIntegrity(Boolean judge, 
                              Boolean admin, 
                              String member_info, 
                              String facility, 
                              String musical_score, 
                              String sound_source){

    if(admin != null && admin){
      judge = judge && member_info != null && member_info.equals(Authority_Kinds_Enum.NONE.getKinds());
      judge = judge && facility != null && facility.equals(Authority_Kinds_Enum.NONE.getKinds());
      judge = judge && musical_score != null && musical_score.equals(Authority_Kinds_Enum.NONE.getKinds());
      judge = judge && sound_source != null && sound_source.equals(Authority_Kinds_Enum.NONE.getKinds());
    }
  }

  /** @} */
}
