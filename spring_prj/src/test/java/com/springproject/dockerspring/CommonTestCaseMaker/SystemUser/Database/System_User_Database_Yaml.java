/** 
 **************************************************************************************
 * @file System_User_Database_Yaml.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Database;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[system-user-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class System_User_Database_Yaml {

  //! 団員番号
  private Map<String, String> member_id;

  //! ユーザー名
  private Map<String, String> username;

  //! パスワード
  private Map<String, String> password;

  //! 権限番号
  private Map<String, String> auth_id;

  //! ログイン失敗回数
  private Map<String, Integer> fail_count;

  //! ロック有無
  private Map<String, Boolean> locking;

  //! テスト時認証情報
  private Map<String, String[]> certification;
}