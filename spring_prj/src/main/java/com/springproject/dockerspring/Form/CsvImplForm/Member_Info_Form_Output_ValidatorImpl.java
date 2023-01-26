/** 
 **************************************************************************************
 * @file Member_Info_Form_Output_ValidatorImpl.java
 * @brief 主に[団員管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラスを格納
 * したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CsvImplForm;

import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Form.FormInterface.Member_Info_Form_Output_Validator;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[団員管理]の出力時バリデーションにおいて、実際の検査処理を実行するクラス
 * 
 * @details 
 * - 主に、データ出力時に誤って不正なデータを出力してしまわないようにする目的で用いる。
 * - 使い方としては、このクラスと対になる通常用エンティティが存在するので、そのエンティティに検査
 * 対象データを格納した状態の物で渡す事で検査を実施する。
 * - 出力時に関しては、入力時に用いる手法でのバリデーションを用いることができない為、[Validator]
 * インターフェースを用いた手法で行う。
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - バイナリデータのMIMEタイプを判別するライブラリに関しては、[Apache Tika]を用いる。
 * これによって、バイナリデータのタイプを拡張子だけで判断せず、バイト配列から厳密にMIMEタイプを
 * 判別し、不正なバイナリデータがシステム内に入ることを防ぐ。
 * - メールアドレスの文字列の判定に関しては、ライブラリ[apache commons EmailValidator]を
 * 用いる。通常の正規表現で判定を行おうとすると、処理が非常に長くなってしまうためである。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Member_Info
 * @see Member_Info_Form_Output_Validator
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_Form_Output_ValidatorImpl implements Member_Info_Form_Output_Validator {

  private final Environment_Config config;
  private final EmailValidator email_valid = EmailValidator.getInstance();






  //! Validator使用時に必ず実装するメソッドだが、使用しないので説明を割愛。
  @Override
  public boolean supports(Class<?> clazz) {
    return Member_Info.class.isAssignableFrom(clazz);
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
    Member_Info entity = (Member_Info)target;
    BindingResult error = (BindingResult)errors;
    Boolean judge = true;

    if(judge){validSerialNum(judge, entity);}
    if(judge){validMemberId(judge, entity);}
    if(judge){validName(judge, entity);}
    if(judge){validNamePronu(judge, entity);}
    if(judge){validSex(judge, entity);}

    Boolean flag_1 = true;
    Date birthday = judge ? validBirthday(judge, entity, flag_1) : null;

    if(judge){validFacePhoto(judge, entity);}
    
    Boolean flag_2 = true;
    Date join_date = judge ? validJoinDate(judge, entity, flag_2) : null;

    Date ret_date = entity.getRet_date();
    Boolean flag_3 = ret_date != null;

    if(judge){validEmail_1(judge, entity);}
    if(judge){validEmail_2(judge, entity);}
    if(judge){validTel_1(judge, entity);}
    if(judge){validTel_2(judge, entity);}
    if(judge){validAddrPostcode(judge, entity);}
    if(judge){validAddr(judge, entity);}
    if(judge){validPosition(judge, entity);}

    Date position_arri_date = entity.getPosition_arri_date();
    Boolean flag_4 = position_arri_date != null;

    if(judge){validJob(judge, entity);}
    if(judge){validAssignDept(judge, entity);}

    Date assign_date = entity.getAssign_date();
    Boolean flag_5 = assign_date != null;

    if(judge){validInstCharge(judge, entity);}
    if(judge){validOtherComment(judge, entity);}
    if(judge){dateLogicCheck_BirthAndJoin(judge, flag_1, flag_2, birthday, join_date);}
    if(judge){dateLogicCheck_JoinAndRet(judge, flag_2, flag_3, join_date, ret_date);}
    if(judge){dateLogicCheck_JoinAndPosition(judge, flag_2, flag_4, join_date, position_arri_date);}
    if(judge){dateLogicCheck_JoinAndAssign(judge, flag_2, flag_5, join_date, assign_date);}


    if(!judge){
      error.addError(new FieldError(error.getObjectName(), "error", "Member_Info_Form_Output_ValidatorImpl"));
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
  private void validSerialNum(Boolean judge, Member_Info entity){
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
  private void validMemberId(Boolean judge, Member_Info entity){
    String member_id = entity.getMember_id();

    if(member_id == null || !member_id.matches(Regex_Enum.ALNUM_HYPHEN_NOTNULL.getRegex()) || member_id.length() > 30){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 名前
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
  private void validName(Boolean judge, Member_Info entity){
    String name = entity.getName();

    if(name == null || !name.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || name.length() > 50){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief ふりがな
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 文字数が[100文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validNamePronu(Boolean judge, Member_Info entity){
    String name_prone = entity.getName_pronu();

    if(name_prone == null || !name_prone.matches(Regex_Enum.ZENKAKU_ONLY_NOTNULL.getRegex()) || name_prone.length() > 100){
      judge = false;
    }
  }






  /** 
   ***************************************************************
   * @brief 性別
   * 
   * @details
   * -# 値が空文字や空白でない事。
   * -# 指定文字列のみで構成されている事。
   * (male, female)
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validSex(Boolean judge, Member_Info entity){
    String sex = entity.getSex();

    if(sex == null || !sex.matches("male|female")){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 誕生日
   * 
   * @details
   * -# 値が空でない事。
   * 
   * @return 抽出した誕生日データ
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private Date validBirthday(Boolean judge, Member_Info entity, Boolean flag_1){
    Date birthday = entity.getBirthday();

    if(judge && birthday == null){
      judge = false;
      flag_1 = false;
    }

    return birthday;
  }








  /** 
   ***************************************************************
   * @brief 顔写真
   * 
   * @details
   * -# ファイル名が、要件を満たすこと。（厳密な条件はアノテーション
   * ファイルを参照の事）
   * -# 画像データが、環境変数で指定した基準値以下のデータ量かつ、[png]
   * 形式のMIMEタイプである事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validFacePhoto(Boolean judge, Member_Info entity){
    byte[] face_photo = entity.getFace_photo();
    Tika tika = new Tika();

    String mime = tika.detect(face_photo);
    if(!mime.equals(Datatype_Enum.PHOTO.getMimetype()) || face_photo.length > config.getPhoto_limit()){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 入団日
   * 
   * @details
   * -# 値が空でない事。
   * 
   * @return 抽出した入団日データ
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private Date validJoinDate(Boolean judge, Member_Info entity, Boolean flag_2){
    Date join_date = entity.getJoin_date();
    
    if(judge && join_date == null){
      judge = false;
      flag_2 = false;
    }

    return join_date;
  }








  /** 
   ***************************************************************
   * @brief メールアドレス1
   * 
   * @details
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   * -# 文字数が[100文字以内]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validEmail_1(Boolean judge, Member_Info entity){
    String email_1 = entity.getEmail_1();
    email_1 = email_1 == null ? "" : email_1;

    if(!this.email_valid.isValid(email_1) || !email_1.matches(Regex_Enum.EMAIL_SUPPORT.getRegex()) || email_1.length() > 100){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief メールアドレス2
   * 
   * @details
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   * -# 文字数が[100文字以内]であること。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validEmail_2(Boolean judge, Member_Info entity){
    String email_2 = entity.getEmail_2();
    email_2 = email_2 == null ? "" : email_2;

    if(!this.email_valid.isValid(email_2) || !email_2.matches(Regex_Enum.EMAIL_SUPPORT.getRegex()) || email_2.length() > 100){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 電話番号1
   * 
   * @details
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validTel_1(Boolean judge, Member_Info entity){
    String tel_1 = entity.getTel_1();
    tel_1 = tel_1 == null ? "" : tel_1;

    if(!tel_1.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || tel_1.length() > 20){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 電話番号2
   * 
   * @details
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validTel_2(Boolean judge, Member_Info entity){
    String tel_2 = entity.getTel_2();
    tel_2 = tel_2 == null ? "" : tel_2;

    if(!tel_2.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || tel_2.length() > 20){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 現住所郵便番号
   * 
   * @details
   * -# 文字数が[15文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validAddrPostcode(Boolean judge, Member_Info entity){
    String postcode = entity.getAddr_postcode();
    postcode = postcode == null ? "" : postcode;

    if(!postcode.matches(Regex_Enum.DIGIT_HYPHEN.getRegex()) || postcode.length() > 15){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 現住所
   * 
   * @details
   * -# 文字数が[200文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validAddr(Boolean judge, Member_Info entity){
    String addr = entity.getAddr();
    addr = addr == null ? "" : addr;

    if(!addr.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || addr.length() > 200){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 役職名
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validPosition(Boolean judge, Member_Info entity){
    String position = entity.getPosition();
    position = position == null ? "" : position;

    if(!position.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || position.length() > 50){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 職種名
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validJob(Boolean judge, Member_Info entity){
    String job = entity.getJob();
    job = job == null ? "" : job;

    if(!job.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || job.length() > 50){
      judge = false;
    }
  }






  /** 
   ***************************************************************
   * @brief 配属部署
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validAssignDept(Boolean judge, Member_Info entity){
    String assign_dept = entity.getAssign_dept();
    assign_dept = assign_dept == null ? "" : assign_dept;

    if(!assign_dept.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || assign_dept.length() > 50){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 担当楽器
   * 
   * @details
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] entity 検査対象のデータを格納したエンティティ
   ***************************************************************
   */
  private void validInstCharge(Boolean judge, Member_Info entity){
    String inst_charge = entity.getInst_charge();
    inst_charge = inst_charge == null ? "" : inst_charge;

    if(!inst_charge.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || inst_charge.length() > 50){
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
  private void validOtherComment(Boolean judge, Member_Info entity){
    String other_comment = entity.getOther_comment();
    other_comment = other_comment == null ? "" : other_comment;

    if(!other_comment.matches(Regex_Enum.ZENKAKU_ONLY_NULLOK.getRegex()) || other_comment.length() > 400){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 「誕生日」と「入団日」の論理チェック
   * 
   * @details
   * -# 「誕生日」が「入団日」以前に設定されている事を判別。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] flag_1 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] birthday 検査対象誕生日
   * @param[in] join_date 検査対象入団日
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_BirthAndJoin(Boolean judge, Boolean flag_1, Boolean flag_2, Date birthday, Date join_date){
    if(flag_1 && flag_2 && birthday != null && birthday.after(join_date)){
      judge = false;
    }
  }








  /** 
   ***************************************************************
   * @brief 「入団日」と「退団日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「退団日」以前に設定されている事を判別。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_3 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] ret_date 検査対象退団日
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndRet(Boolean judge, Boolean flag_2, Boolean flag_3, Date join_date, Date ret_date){
    if(flag_2 && flag_3 && join_date != null && join_date.after(ret_date)){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 「入団日」と「現役職着任日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「現役職着任日」以前に設定されている事を判別。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_4 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] position_arri_date 検査対象現役職着任日
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndPosition(Boolean judge, Boolean flag_2, Boolean flag_4, Date join_date, Date position_arri_date){
    if(flag_2 && flag_4 && join_date != null && join_date.after(position_arri_date)){
      judge = false;
    }
  }







  /** 
   ***************************************************************
   * @brief 「入団日」と「配属日」の論理チェック
   * 
   * @details
   * -# 「入団日」が「配属日」以前に設定されている事を判別。
   *
   * @param[out] judge 現在の判定状況を保持する真偽値
   * @param[in] flag_2 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] flag_5 日付論理チェックの際に使用する真偽値フラグ
   * @param[in] join_date 検査対象入団日
   * @param[in] assign_date 検査対象配属日
   * 
   * @note 日付論理チェックの際の真偽値フラグとは、対象の日付が有効、
   * つまりNullやバリデーションエラーの状態ではなくて検査が可能だ
   * 状態であることを示すためにある。なので、検査対象の日付が全て
   * 有効状態になっていないと検査ができない。
   ***************************************************************
   */
  private void dateLogicCheck_JoinAndAssign(Boolean judge, Boolean flag_2, Boolean flag_5, Date join_date, Date assign_date){
    if(flag_2 && flag_5 && join_date != null && join_date.after(assign_date)){
      judge = false;
    }
  }

  //* @} */
}
