/** 
 **************************************************************************************
 * @file Member_Info_Form.java
 * @brief 主に[団員管理]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CsvImplForm;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.DigitHyphen;
import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_100;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_20;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_400;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_50;
import com.springproject.dockerspring.Form.CustumAnno.Zenkaku;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;
import com.springproject.dockerspring.Form.OriginalAnno.DateCorrCheck;
import com.springproject.dockerspring.Form.OriginalAnno.FileNameCheck;

import lombok.Data;










/** 
 **************************************************************************************
 * @brief 主に[団員管理]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、団員管理に必要なデータ全般である。詳しい説明は、各変数の
 * 説明に記載する。
 * - なお、このフォームクラスは出力時バリデーションに使いまわせない為、入力時のみの使用になる。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @DateCorrCheck(start={"join_date", "join_date", "join_date", "birthday"}, 
 * end={"ret_date", "position_arri_date", "assign_date", "join_date"},
 * groups = Order_3.class)
 * 
 * @see DateCorrCheck
 * @see SerialNumCheck
 * @see StrLength_30
 * @see AlnumHyphen
 * @see StrLength_50
 * @see StrLength_100
 * @see Zenkaku
 * @see FileNameCheck
 * @see StrLength_20
 * @see DigitHyphen
 * @see StrLength_400
 * @see Order
 **************************************************************************************
 */ 
@Data
@DateCorrCheck(start={"join_date", "join_date", "join_date", "birthday"}, 
               end={"ret_date", "position_arri_date", "assign_date", "join_date"},
               groups = Order_3.class)
public class Member_Info_Form{


  /** 
   **************************************************************
   * @var serial_num
   * 
   * @brief シリアルナンバー
   * 
   * @details 記述してある順番に検査を実施
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   * 
   * @see SerialNumCheck
   *************************************************************
   */
  @SerialNumCheck(groups = Order_1.class)
  private Integer serial_num;


  /** 
   **************************************************************
   * @var member_id
   * 
   * @brief 団員番号
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   * 
   * @see StrLength_30
   * @see AlnumHyphen
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_30(groups = Order_2.class)
  @AlnumHyphen(groups = Order_3.class)
  private String member_id;


  /** 
   **************************************************************
   * @var name
   * 
   * @brief 名前
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_50(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String name;


  /** 
   **************************************************************
   * @var name_pronu
   * 
   * @brief ふりがな
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[100文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_100
   * @see Zenkaku
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_100(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String name_pronu;


  /** 
   **************************************************************
   * @var sex
   * 
   * @brief 性別
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 指定文字列のみで構成されている事。
   * (male, female)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="male|female", groups = Order_2.class)
  private String sex;


  /** 
   **************************************************************
   * @var birthday
   * 
   * @brief 誕生日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date birthday;


  /** 
   **************************************************************
   * @var face_photo
   * 
   * @brief 顔写真
   * 
   * @details 記述してある順番に検査を実施
   * -# ファイル名が、要件を満たすこと。（厳密な条件はアノテーション
   * ファイルを参照の事）
   * 
   * @see FileNameCheck
   *************************************************************
   */
  @FileNameCheck(groups = Order_1.class)
  private MultipartFile face_photo;


  /** 
   **************************************************************
   * @var photo_change_flag
   * 
   * @brief 顔写真変更可否フラグ
   * - 値が空でない事。
   * 
   * @details この値はバリデーション対象の物ではなく、この値を指定する
   * 事で、顔写真を新規のデータで変更するか指定する。「False」であれば
   * 顔写真データが無くても、Nullにデータを上書きしない。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  private Boolean photo_change_flag;


  /** 
   **************************************************************
   * @var join_date
   * 
   * @brief 入団日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   * -# 「誕生日」以前の日付でないこと。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date join_date;


  /** 
   **************************************************************
   * @var ret_date
   * 
   * @brief 退団日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   * -# 「入団日」以前の日付でないこと。
   *************************************************************
   */
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date ret_date;


  /** 
   **************************************************************
   * @var email_1
   * 
   * @brief メールアドレス1
   * 
   * @details 記述してある順番に検査を実施
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   *************************************************************
   */
  @Email(groups = Order_1.class)
  @Pattern(regexp = "^[a-zA-Z0-9_+\\.@-]*$", groups = Order_2.class)
  private String email_1;


  /** 
   **************************************************************
   * @var email_2
   * 
   * @brief メールアドレス2
   * 
   * @details 記述してある順番に検査を実施
   * -# [Eメール形式]として成立する文字列であること。
   * -# [半角英数字＆メールアドレスで使用する記号]のみで構成されている事。
   *************************************************************
   */
  @Email(groups = Order_1.class)
  @Pattern(regexp = "^[a-zA-Z0-9_+\\.@-]*$", groups = Order_2.class)
  private String email_2;


  /** 
   **************************************************************
   * @var tel_1
   * 
   * @brief 電話番号1
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   * 
   * @see StrLength_20
   * @see DigitHyphen
   *************************************************************
   */
  @StrLength_20(groups = Order_1.class)
  @DigitHyphen(groups = Order_2.class)
  private String tel_1;


  /** 
   **************************************************************
   * @var tel_2
   * 
   * @brief 電話番号2
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[20文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   * 
   * @see StrLength_20
   * @see DigitHyphen
   *************************************************************
   */
  @StrLength_20(groups = Order_1.class)
  @DigitHyphen(groups = Order_2.class)
  private String tel_2;


  /** 
   **************************************************************
   * @var addr_postcode
   * 
   * @brief 現住所郵便番号
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[15文字以内]であること。
   * -# [半角英数字＆ハイフン]のみで構成されている事。
   * 
   * @see DigitHyphen
   *************************************************************
   */
  @Length(min=0, max=15, groups = Order_1.class)
  @DigitHyphen(groups = Order_2.class)
  private String addr_postcode;


  /** 
   **************************************************************
   * @var addr
   * 
   * @brief 現住所
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[200文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see Zenkaku
   *************************************************************
   */
  @Length(min=0, max=200, groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String addr;


  /** 
   **************************************************************
   * @var position
   * 
   * @brief 役職名
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String position;


  /** 
   **************************************************************
   * @var position_arri_date
   * 
   * @brief 現役職着任日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   * -# 「入団日」以前の日付でないこと。
   *************************************************************
   */
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date position_arri_date;


  /** 
   **************************************************************
   * @var job
   * 
   * @brief 職種名
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String job;


  /** 
   **************************************************************
   * @var assign_dept
   * 
   * @brief 配属部署
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String assign_dept;


  /** 
   **************************************************************
   * @var assign_date
   * 
   * @brief 配属日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   * -# 「入団日」以前の日付でないこと。
   *************************************************************
   */
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date assign_date;


  /** 
   **************************************************************
   * @var inst_charge
   * 
   * @brief 担当楽器
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String inst_charge;


  /** 
   **************************************************************
   * @var other_comment
   * 
   * @brief その他コメント
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[400文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_400
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_400(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String other_comment;
}