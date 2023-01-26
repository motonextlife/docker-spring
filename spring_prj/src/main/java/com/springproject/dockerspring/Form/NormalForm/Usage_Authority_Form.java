/** 
 **************************************************************************************
 * @file Usage_Authority_Form.java
 * @brief 主に[権限管理]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_50;
import com.springproject.dockerspring.Form.CustumAnno.Zenkaku;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;
import com.springproject.dockerspring.Form.OriginalAnno.AuthConnectCheck;

import lombok.Data;








/** 
 **************************************************************************************
 * @brief 主に[権限管理]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、権限管理に必要なデータ全般である。詳しい説明は、
 * 各変数の説明に記載する。
 * - なお、このフォームクラスは出力時バリデーションに使いまわせない為、入力時のみの使用になる。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @AuthConnectCheck
 * 
 * @see AuthConnectCheck
 * @see SerialNumCheck
 * @see StrLength_30
 * @see AlnumHyphen
 * @see StrLength_50
 * @see Zenkaku
 * @see Order
 **************************************************************************************
 */ 
@Data
@AuthConnectCheck(groups = Order_3.class)
public class Usage_Authority_Form{


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


  /** 
   **************************************************************
   * @var auth_name
   * 
   * @brief 権限名
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字列が[全角文字]のみで構成されている事。
   * -# 文字数が[50文字以内]であること。
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_50(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String auth_name;


  /** 
   **************************************************************
   * @var admin
   * 
   * @brief 管理者権限
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# この権限が付与されているときは、他の権限が一切付与されていない事。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  private Boolean admin;
  

  /** 
   **************************************************************
   * @var member_info
   * 
   * @brief 団員管理権限
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="none|brows|change|delete|hist|rollback", 
           groups = Order_2.class)
  private String member_info;


  /** 
   **************************************************************
   * @var facility
   * 
   * @brief 設備管理権限
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="none|brows|change|delete|hist|rollback", 
           groups = Order_2.class)
  private String facility;


  /** 
   **************************************************************
   * @var musical_score
   * 
   * @brief 楽譜管理権限
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="none|brows|change|delete|hist|rollback", 
           groups = Order_2.class)
  private String musical_score;


  /** 
   **************************************************************
   * @var sound_source
   * 
   * @brief 音源管理権限
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 指定の文字列であること。
   * (none, brows, change, delete, hist, rollback)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="none|brows|change|delete|hist|rollback", 
           groups = Order_2.class)
  private String sound_source;
}