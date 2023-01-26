/** 
 **************************************************************************************
 * @file Del_Indivi_Hist_Form.java
 * @brief 主に[共通の履歴削除]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.NormalForm;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.OriginalAnno.DateCorrCheck;

import lombok.Data;









/** 
 **************************************************************************************
 * @brief 主に[共通の履歴削除]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、共通の履歴削除に必要なデータ全般である。詳しい説明は、
 * 各変数の説明に記載する。
 * 
 * @par 使用アノテーション
 * - @Data
 * - @DateCorrCheck(start={"start_datetime"}, end={"end_datetime"}, groups = Order_2.class)
 * 
 * @see DateCorrCheck
 * @see Order
 **************************************************************************************
 */ 
@Data
@DateCorrCheck(start={"start_datetime"}, end={"end_datetime"}, groups = Order_2.class)
public class Del_Indivi_Hist_Form{


  /** 
   **************************************************************
   * @var table_name
   * 
   * @brief 削除対象の機能名
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字列が指定の内容と同一であること。
   * (All, Audio_Data, Facility_Photo, Facility, Member_Info, 
   * Musical_Score, Score_Pdf, Sound_Source)
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @Pattern(regexp="All|" 
                + "Audio_Data|" 
                + "Facility_Photo|" 
                + "Facility|" 
                + "Member_Info|" 
                + "Musical_Score|" 
                + "Score_Pdf|" 
                + "Sound_Source",
           groups = Order_2.class)
  private String table_name;


  /** 
   **************************************************************
   * @var start_datetime
   * 
   * @brief 削除開始日時
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
   * @brief 削除終了日時
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
}