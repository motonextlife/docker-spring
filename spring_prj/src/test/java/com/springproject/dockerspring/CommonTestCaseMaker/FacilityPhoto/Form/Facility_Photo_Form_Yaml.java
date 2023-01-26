/** 
 **************************************************************************************
 * @file Facility_Photo_Form_Yaml.java
 * @brief 主に[設備写真データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[設備写真データ情報]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[facility-photo-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Facility_Photo_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 設備番号
  private Map<String, String> faci_id;

  //! 設備写真データ名
  private Map<String, String> photo_name;

  //! 設備写真データファイル
  private Map<String, String> photo_data;
}