/** 
 **************************************************************************************
 * @file Zip_Output_Form_Child.java
 * @brief 共通の圧縮ファイル出力時のフォームバリデーションを行う際に、入れ子となる
 * バイナリデータの個別の情報を格納するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotNull;

import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 共通の圧縮ファイル出力時のフォームバリデーションを行う際に、入れ子となる
 * 指定のシリアルナンバーの情報を格納するクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、個別のデータに関連付けられているシリアルナンバー。
 * - このクラスは、[Zip_Output_Form]で入れ子として用いる。
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
public class Zip_Output_Form_Child{


  /** 
   **************************************************************
   * @var serial_num
   * 
   * @brief 個別のデータに関連づいているシリアルナンバー
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 値が空でない事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   * 
   * @see SerialNumCheck
   *************************************************************
   */
  @NotNull(groups = Order_2.class)
  @SerialNumCheck(groups = Order_3.class)
  private Integer serial_num;
}