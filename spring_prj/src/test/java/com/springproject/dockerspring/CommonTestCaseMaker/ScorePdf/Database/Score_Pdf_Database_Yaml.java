/** 
 **************************************************************************************
 * @file Score_Pdf_Database_Yaml.java
 * @brief 主に[楽譜データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Database;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[score-pdf-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Score_Pdf_Database_Yaml {

  //! 楽譜番号
  private Map<String, String> score_id;

  //! 楽譜データ名
  private Map<String, String> score_name;

  //! 楽譜データハッシュ値
  private Map<String, String> pdf_hash;

  //! 楽譜データファイル名
  private Map<String, String> pdf_data;
}