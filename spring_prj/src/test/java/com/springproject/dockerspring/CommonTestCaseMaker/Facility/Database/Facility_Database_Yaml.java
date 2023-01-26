/** 
 **************************************************************************************
 * @file Facility_Database_Yaml.java
 * @brief 主に[設備管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility.Database;

import java.util.Date;
import java.util.Map;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[facility-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Facility_Database_Yaml{

  //! 設備番号
  private Map<String, String> faci_id;

  //! 設備名
  private Map<String, String> faci_name;

  //! 購入日
  private Map<String, Date> buy_date;

  //! 製作者
  private Map<String, String> producer;

  //! 保管場所
  private Map<String, String> storage_loc;

  //! 廃棄日
  private Map<String, Date> disp_date;

  //! その他コメント
  private Map<String, String> other_comment;
}