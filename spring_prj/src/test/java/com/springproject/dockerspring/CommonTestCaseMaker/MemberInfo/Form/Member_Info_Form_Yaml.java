/** 
 **************************************************************************************
 * @file Member_Info_Form_Yaml.java
 * @brief 主に[団員管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Form;

import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、フォームバリデーションテストの際に用いる
 * テストケースをマッピングするクラス。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[member-info-form.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * - YMLファイルの中には、様々なデータ型のデータが存在するが、読取後に対応する型に変換するため、
 * 一度文字列型でマッピングするものとする。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Member_Info_Form_Yaml {

  //! シリアルナンバー
  private Map<String, String> serial_num;

  //! 団員番号
  private Map<String, String> member_id;

  //! 名前
  private Map<String, String> name;

  //! ふりがな
  private Map<String, String> name_pronu;

  //! 性別
  private Map<String, String> sex;

  //! 誕生日
  private Map<String, String> birthday;

  //! 顔写真
  private Map<String, String> face_photo;

  //! 入団日
  private Map<String, String> join_date;

  //! 退団日
  private Map<String, String> ret_date;

  //! メールアドレス
  private Map<String, String> email;

  //! 電話番号
  private Map<String, String> tel;

  //! 現住所郵便番号
  private Map<String, String> addr_postcode;

  //! 現住所
  private Map<String, String> addr;

  //! 役職名
  private Map<String, String> position;

  //! 現役職着任日
  private Map<String, String> position_arri_date;

  //! 職種名
  private Map<String, String> job;

  //! 配属部署
  private Map<String, String> assign_dept;

  //! 配属日
  private Map<String, String> assign_date;

  //! 担当楽器
  private Map<String, String> inst_charge;

  //! その他コメント
  private Map<String, String> other_comment;
}