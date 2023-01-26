/** 
 **************************************************************************************
 * @file Musical_Score_Form_Yaml.java
 * @brief 主に[楽譜管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[musical-score-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Musical_Score_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 楽譜番号
  private Map<String, String> score_id;

  //! 購入日
  private Map<String, String> buy_date;

  //! 曲名
  private Map<String, String> song_title;

  //! 作曲者
  private Map<String, String> composer;

  //! 編曲者
  private Map<String, String> arranger;

  //! 出版社
  private Map<String, String> publisher;

  //! 保管場所
  private Map<String, String> storage_loc;

  //! 廃棄日
  private Map<String, String> disp_date;

  //! その他コメント
  private Map<String, String> other_comment;
}