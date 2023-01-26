/** 
 **************************************************************************************
 * @file Facility_Form_Yaml.java
 * @brief 主に[設備管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility.Form;

import java.util.Map;

import lombok.Data;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[facility-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Facility_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 設備番号
  private Map<String, String> faci_id;

  //! 設備名
  private Map<String, String> faci_name;

  //! 購入日
  private Map<String, String> buy_date;

  //! 製作者
  private Map<String, String> producer;

  //! 保管場所
  private Map<String, String> storage_loc;

  //! 廃棄日
  private Map<String, String> disp_date;

  //! その他コメント
  private Map<String, String> other_comment;
}