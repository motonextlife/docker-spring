/** 
 **************************************************************************************
 * @file System_User_RepoForm_Yaml.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、リポジトリでの検索メソッド時に使用する
 * テストケースのマッピングを行うクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[system-user-repoform.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class System_User_RepoForm_Yaml {

  //! 検索種別
  private Map<String, String> subject;

  //! 全検索時
  private Map<String, Integer> all;

  //! 団員番号存在確認
  private Map<String, String[]> exist_remain_member;

  //! 権限番号存在確認
  private Map<String, String[]> exist_remain_auth;

  //! 団員番号
  private Map<String, String[]> member_id;

  //! ユーザー名
  private Map<String, String[]> username;

  //! 権限番号
  private Map<String, String[]> auth_id;

  //! ロック有無
  private Map<String, String[]> locking;
}