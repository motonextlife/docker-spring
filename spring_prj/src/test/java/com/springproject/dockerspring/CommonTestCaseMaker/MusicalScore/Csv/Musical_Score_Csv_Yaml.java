/** 
 **************************************************************************************
 * @file Musical_Score_Csv_Yaml.java
 * @brief 主に[楽譜管理]機能のテストにおいて、CSVファイルの入出力に関係するテストケースの
 * マッピングを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Csv;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能のテストにおいて、CSVファイルの入出力に関係するテストケースの
 * マッピングを行うクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[musical-score-csv.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Musical_Score_Csv_Yaml {

  //! 入力CSVファイル
  private Map<String, String> csv_file;

  //! 出力CSVファイルの比較用ファイル
  private Map<String, String> output_csv;

  //! バリデーションエラー時の想定される発生個所
  private LinkedHashMap<Integer, List<Integer>> ng_valid_error_pos;
}