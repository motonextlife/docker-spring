/** 
 **************************************************************************************
 * @file System_User_Form.java
 * @brief 主に[システムユーザー管理]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;

import lombok.Data;








/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、システムユーザー管理に必要なデータ全般である。詳しい説明は、
 * 各変数の説明に記載する。
 * - なお、このフォームクラスは出力時バリデーションに使いまわせない為、入力時のみの使用になる。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see SerialNumCheck
 * @see StrLength_30
 * @see AlnumHyphen
 * @see Order
 **************************************************************************************
 */ 
@Data
public class System_User_Form{


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
   * @var username
   * 
   * @brief ユーザー名
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字列が[半角英数字]のみで構成されている事。
   * -# 文字数が[8～20文字]であること。
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="^[0-9a-zA-Z]*$", groups = Order_2.class)
  @Length(min=8, max=20, groups = Order_3.class)
  private String username;


  /** 
   **************************************************************
   * @var password
   * 
   * @brief パスワード
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字列が[半角英数字]のみで構成されている事。
   * -# 文字数が[8～20文字]であること。
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="^[0-9a-zA-Z]*$", groups = Order_2.class)
  @Length(min=8, max=20, groups = Order_3.class)
  private String password;


  /** 
   **************************************************************
   * @var auth_id
   * 
   * @brief 権限番号
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
  private String auth_id;
}