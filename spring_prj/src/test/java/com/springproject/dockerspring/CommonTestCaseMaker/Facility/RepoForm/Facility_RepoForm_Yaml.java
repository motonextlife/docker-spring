/** 
 **************************************************************************************
 * @file Facility_RepoForm_Yaml.java
 * @brief 主に[設備管理]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility.RepoForm;

import java.util.Map;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[facility-repoform.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Facility_RepoForm_Yaml {

  //! 検索種別
  private Map<String, String> subject;

  //! 全検索時
  private Map<String, Integer> all;

  //! 設備番号
  private Map<String, String[]> faci_id;

  //! 設備名
  private Map<String, String[]> faci_name;

  //! 購入日
  private Map<String, String[]> buy_date;

  //! 製作者
  private Map<String, String[]> producer;

  //! 保管場所
  private Map<String, String[]> storage_loc;

  //! 廃棄日
  private Map<String, String[]> disp_date;

  //! その他コメント
  private Map<String, String[]> other_comment;
}