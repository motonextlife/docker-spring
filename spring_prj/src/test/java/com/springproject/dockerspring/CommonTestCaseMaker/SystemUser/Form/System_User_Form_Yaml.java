/** 
 **************************************************************************************
 * @file System_User_Form_Yaml.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[system-user-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class System_User_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 団員番号
  private Map<String, String> member_id;

  //! ユーザー名
  private Map<String, String> username;

  //! パスワード
  private Map<String, String> password;

  //! 権限番号
  private Map<String, String> auth_id;
}