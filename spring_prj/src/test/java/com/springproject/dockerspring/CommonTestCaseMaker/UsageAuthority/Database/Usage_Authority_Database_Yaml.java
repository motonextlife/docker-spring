/** 
 **************************************************************************************
 * @file Usage_Authority_Database_Yaml.java
 * @brief 主に[権限管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Database;

import java.util.Map;

import lombok.Data;





/** 
 **************************************************************************************
 * @brief 主に[権限管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[usage-authority-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Usage_Authority_Database_Yaml {

  //! 権限番号
  private Map<String, String> auth_id;

  //! 権限名
  private Map<String, String> auth_name;

  //! 管理者権限
  private Map<String, Boolean> admin;

  //! 団員管理権限
  private Map<String, String> member_info;

  //! 設備管理権限
  private Map<String, String> facility;

  //! 楽譜管理権限
  private Map<String, String> musical_score;

  //! 音源管理権限
  private Map<String, String> sound_source;
}