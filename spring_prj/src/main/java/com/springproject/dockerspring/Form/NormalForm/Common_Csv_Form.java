/** 
 **************************************************************************************
 * @file Common_Csv_Form.java
 * @brief 主に[共通のCSVファイル入力]の入力時バリデーションで用いるフォームを実装したクラスを
 * 格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.OriginalAnno.FileNameCheck;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[共通のCSVファイル入力]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、共通のCSVファイル入力に必要なデータ全般である。
 * 詳しい説明は、各変数の説明に記載する。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see FileNameCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Common_Csv_Form{


  /** 
   **************************************************************
   * @var csv_file
   * 
   * @brief CSVファイル
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
  private MultipartFile csv_file;
}