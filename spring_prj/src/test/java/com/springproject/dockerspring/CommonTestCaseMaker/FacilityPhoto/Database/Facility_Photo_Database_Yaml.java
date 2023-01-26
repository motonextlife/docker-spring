/** 
 **************************************************************************************
 * @file Facility_Photo_Database_Yaml.java
 * @brief 主に[設備写真データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Database;

import java.util.Map;

import lombok.Data;



/** 
 **************************************************************************************
 * @brief 主に[設備写真データ情報]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[facility-photo-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Facility_Photo_Database_Yaml {

  //! 設備番号
  private Map<String, String> faci_id;

  //! 設備写真データ名
  private Map<String, String> photo_name;

  //! 設備写真データハッシュ値
  private Map<String, String> photo_hash;

  //! 設備写真データファイル名
  private Map<String, String> photo_data;
}