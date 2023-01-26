/** 
 **************************************************************************************
 * @file Rollback_Form.java
 * @brief 共通のロールバック時のフォームバリデーションを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotNull;

import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 共通のロールバック時のフォームバリデーションを行うクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、ロールバック対象となる履歴情報の履歴番号である。
 * - 厳密なバリデーションの順番や内容に関しては、各フィールド変数の説明を参照。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see SerialNumCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Rollback_Form{


  /** 
   **************************************************************
   * @var history_id
   * 
   * @brief ロールバック対象データの履歴番号
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 値が空で無い事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @SerialNumCheck(groups = Order_2.class)
  private Integer history_id;
}