/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.NormalForm
 * 
 * @brief バリデーション機能のうち、全機能共通で使用する機能を格納したパッケージ。
 * 
 * @details
 * - このパッケージには、情報検索時やロールバック時など全機能共通で用いるフォークムクラスを格納
 * している。また、システムアカウントや権限関連の情報をバリデーションする機能を含んでいる。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;






/** 
 **************************************************************************************
 * @file Com_Hist_Get_Form.java
 * @brief 主に[共通の履歴情報取得]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_50;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;
import com.springproject.dockerspring.Form.OriginalAnno.DateCorrCheck;
import com.springproject.dockerspring.Form.OriginalAnno.SanitizeCheck;

import lombok.Data;









/** 
 **************************************************************************************
 * @brief 主に[共通の履歴情報取得]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、共通の履歴情報取得に必要なデータ全般である。詳しい説明は、
 * 各変数の説明に記載する。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @DateCorrCheck(start={"start_datetime"}, end={"end_datetime"}, groups = Order_2.class)
 * 
 * @see DateCorrCheck
 * @see StrLength_50
 * @see SanitizeCheck
 * @see StrLength_30
 * @see SerialNumCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
@DateCorrCheck(start={"start_datetime"}, end={"end_datetime"}, groups = Order_2.class)
public class Com_Hist_Get_Form{


  /** 
   **************************************************************
   * @var main_word
   * 
   * @brief 検索ワード
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[30文字以内]であること。
   * -# 文字列中にセキュリティ上危険性のある文字列が含まれていない事。
   * 
   * @see StrLength_50
   * @see SanitizeCheck
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @SanitizeCheck(groups = Order_2.class)
  private String main_word;


  /** 
   **************************************************************
   * @var start_datetime
   * 
   * @brief 検索開始日時
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 値を[yyyy-MM-dd'T'HH:mm]のフォーマットに変換できること。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
  private Date start_datetime;


  /** 
   **************************************************************
   * @var end_datetime
   * 
   * @brief 検索終了日時
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 値を[yyyy-MM-dd'T'HH:mm]のフォーマットに変換できること。
   * -# 開始日以前に指定されていない事。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
  private Date end_datetime;


  /** 
   **************************************************************
   * @var subject
   * 
   * @brief 検索ジャンル
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[30文字以内]であること。
   * -# 文字列が[半角英字]のみで構成されている事。
   * 
   * @see StrLength_30
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_30(groups = Order_2.class)
  @Pattern(regexp="^[a-zA-Z]*$", groups = Order_3.class)
  private String subject;


  /** 
   **************************************************************
   * @var order
   * 
   * @brief 並び順指定
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * 
   * @note 「True」で昇順となる。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  private Boolean order;


  /** 
   **************************************************************
   * @var page_num
   * 
   * @brief ページ番号指定
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 符号なしで[9桁]までの数値であり、浮動小数点でない事。
   * 
   * @see SerialNumCheck
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @SerialNumCheck(groups = Order_2.class)
  private Integer page_num;
}