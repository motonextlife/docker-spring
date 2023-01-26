/** 
 **************************************************************************************
 * @file Sound_Source_Form.java
 * @brief 主に[音源管理]の入力時バリデーションで用いるフォームを実装したクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.CsvImplForm;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.Form.CustumAnno.AlnumHyphen;
import com.springproject.dockerspring.Form.CustumAnno.SerialNumCheck;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_30;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_400;
import com.springproject.dockerspring.Form.CustumAnno.StrLength_50;
import com.springproject.dockerspring.Form.CustumAnno.Zenkaku;
import com.springproject.dockerspring.Form.Order.Order_1;
import com.springproject.dockerspring.Form.Order.Order_2;
import com.springproject.dockerspring.Form.Order.Order_3;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[音源管理]の入力時バリデーションで用いるフォームを実装したクラス
 * 
 * @details 
 * - フォーム入力時に受け取る値としては、音源管理に必要なデータ全般である。詳しい説明は、各変数の
 * 説明に記載する。
 * - なお、このフォームクラスは出力時バリデーションに使いまわせない為、入力時のみの使用になる。
 * 
 * @par 使用アノテーション
 * - @Data
 * 
 * @see SerialNumCheck
 * @see StrLength_30
 * @see AlnumHyphen
 * @see StrLength_50
 * @see Zenkaku
 * @see StrLength_400
 * @see Order
 **************************************************************************************
 */ 
@Data
public class Sound_Source_Form{


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
   * @var sound_id
   * 
   * @brief 音源番号
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
  private String sound_id;


  /** 
   **************************************************************
   * @var upload_date
   * 
   * @brief 登録日
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空でない事。
   * -# 値を[yyyy-MM-dd]のフォーマットに変換できること。
   *************************************************************
   */
  @NotNull(groups = Order_1.class)
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date upload_date;


  /** 
   **************************************************************
   * @var song_title
   * 
   * @brief 曲名
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_50(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String song_title;


  /** 
   **************************************************************
   * @var composer
   * 
   * @brief 作曲者
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_50(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String composer;


  /** 
   **************************************************************
   * @var performer
   * 
   * @brief 演奏者
   * 
   * @details 記述してある順番に検査を実施
   * -# 値が空文字や空白でない事。
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @NotBlank(groups = Order_1.class)
  @StrLength_50(groups = Order_2.class)
  @Zenkaku(groups = Order_3.class)
  private String performer;


  /** 
   **************************************************************
   * @var publisher
   * 
   * @brief 出版社
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[50文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_50
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_50(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String publisher;


  /** 
   **************************************************************
   * @var other_comment
   * 
   * @brief その他コメント
   * 
   * @details 記述してある順番に検査を実施
   * -# 文字数が[400文字以内]であること。
   * -# 文字列が[全角文字]のみで構成されている事。
   * 
   * @see StrLength_400
   * @see Zenkaku
   *************************************************************
   */
  @StrLength_400(groups = Order_1.class)
  @Zenkaku(groups = Order_2.class)
  private String other_comment;
}