/** 
 **************************************************************************************
 * @file Blob_Data_Form.java
 * @brief 共通の入力時のバイナリデータのフォームバリデーションを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.BlobImplForm;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;

import lombok.Data;








/** 
 **************************************************************************************
 * @brief 共通の入力時のバイナリデータのフォームバリデーションを行うクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、入力したバイナリデータに関連付ける管理番号と、バイナリ
 * データのリストである。
 * - 厳密なバリデーションの順番や内容に関しては、各フィールド変数の説明を参照。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see Blob_Data_Form_Child
 * @see StrLength_30
 * @see AlnumHyphen
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Blob_Data_Form{


  /** 
   **************************************************************
   * @var id
   * 
   * @brief データに関連づける管理番号
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 値が空白や空文字ではないこと。
   * -# 文字数は[30文字以内]に収まっている事。
   * -# 文字列が[半角英数字＆ハイフン]のみで構成されている事。
   * 
   * @see StrLength_30
   * @see AlnumHyphen
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_30(groups = Order_2.class)
  @AlnumHyphen(groups = Order_3.class)
  private String id;


  /** 
   **************************************************************
   * @var data_list
   * 
   * @brief 入力するバイナリデータのリスト。専用のエンティティに
   * 格納した状態で渡す。
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 値が空で無い事。
   * 
   * @see Blob_Data_Form_Child
   *************************************************************
   */
  @Valid
  @NotNull(groups = Order_1.class)
  private List<Blob_Data_Form_Child> data_list;
}