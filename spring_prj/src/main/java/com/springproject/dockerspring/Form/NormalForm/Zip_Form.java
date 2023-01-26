/** 
 **************************************************************************************
 * @file Zip_Form.java
 * @brief 共通の圧縮ファイル入力時のフォームバリデーションを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;
import com.springproject.dockerspring.Form.OriginalAnno.FileNameCheck;

import lombok.Data;








/** 
 **************************************************************************************
 * @brief 共通の圧縮ファイル入力時のフォームバリデーションを行うクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、入力した圧縮ファイルに関連付ける管理番号と、ファイル本体
 * である。
 * - 厳密なバリデーションの順番や内容に関しては、各フィールド変数の説明を参照。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see StrLength_30
 * @see AlnumHyphen
 * @see FileNameCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Zip_Form{


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
   * @var zip_file
   * 
   * @brief ZIPファイル
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# ファイル名が、要件を満たすこと。（厳密な条件はアノテーション
   * ファイルを参照の事）
   * 
   * @see FileNameCheck
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @FileNameCheck(groups = Order_2.class)
  private MultipartFile zip_file;
}