/** 
 **************************************************************************************
 * @file Score_Pdf_RepoForm_Yaml.java
 * @brief 主に[楽譜データ情報]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.RepoForm;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[score-pdf-repoform.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Score_Pdf_RepoForm_Yaml {

  //! 検索種別
  private Map<String, String> subject;

  //! 楽譜番号
  private Map<String, String[]> score_id;

  //! 楽譜データ名
  private Map<String, String[]> score_name;
}