/** 
 **************************************************************************************
 * @file Blob_Data_Form_Child.java
 * @brief 共通の入力時のバイナリデータのフォームバリデーションを行う際に、入れ子となる
 * バイナリデータの個別の情報を格納するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.BlobImplForm;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;
import com.springproject.dockerspring.Form.OriginalAnno.FileNameCheck;

import lombok.Data;








/** 
 **************************************************************************************
 * @brief 共通の入力時のバイナリデータのフォームバリデーションを行う際に、入れ子となる
 * バイナリデータの個別の情報を格納するクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、個別のバイナリデータに関連付けられているシリアルナンバー
 * と、バイナリデータ本体。
 * - このクラスは、[Blob_Data_Form]で入れ子として用いる。
 * - 厳密なバリデーションの順番や内容に関しては、各フィールド変数の説明を参照。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see SerialNumCheck
 * @see FileNameCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Blob_Data_Form_Child{


  /** 
   **************************************************************
   * @var serial_num
   * 
   * @brief 個別のデータに関連づいているシリアルナンバー
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   * 
   * @see SerialNumCheck
   *************************************************************
   */
  @SerialNumCheck(groups = Order_2.class)
  private Integer serial_num;


  /** 
   **************************************************************
   * @var blob_data
   * 
   * @brief 個別のバイナリデータ
   * 
   * @details　以下の順番通りにバリデーションを実施
   * -# 値が空でない事。
   * -# ファイル名が、要件を満たすこと。（厳密な条件はアノテーション
   * ファイルを参照の事）
   * 
   * @see FileNameCheck
   *************************************************************
   */
  @NotNull(groups = Order_2.class)
  @FileNameCheck(groups = Order_3.class)
  private MultipartFile blob_data;
}