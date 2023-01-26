/** 
 **************************************************************************************
 * @file Usage_Authority_RepoForm_Yaml.java
 * @brief 主に[権限番号]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.RepoForm;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[権限番号]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[usage-authority-repoform.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Usage_Authority_RepoForm_Yaml {

  //! 検索種別
  private Map<String, String> subject;

  //! 全検索時
  private Map<String, Integer> all;

  //! 権限番号
  private Map<String, String[]> auth_id;

  //! 権限名
  private Map<String, String[]> auth_name;

  //! 管理者権限
  private Map<String, String[]> admin;

  //! 団員管理権限
  private Map<String, String[]> member_info;

  //! 設備管理権限
  private Map<String, String[]> facility;

  //! 楽譜管理権限
  private Map<String, String[]> musical_score;

  //! 音源管理権限
  private Map<String, String[]> sound_source;
}