/** 
 **************************************************************************************
 * @file Musical_Score_Database_Yaml.java
 * @brief 主に[楽譜管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Database;

import java.util.Date;
import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[musical-score-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Musical_Score_Database_Yaml {

  //! 楽譜番号
  private Map<String, String> score_id;

  //! 購入日
  private Map<String, Date> buy_date;

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
  private Map<String, Date> disp_date;

  //! その他コメント
  private Map<String, String> other_comment;
}