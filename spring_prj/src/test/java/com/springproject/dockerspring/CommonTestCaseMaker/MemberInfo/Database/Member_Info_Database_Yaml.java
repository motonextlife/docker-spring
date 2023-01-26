/** 
 **************************************************************************************
 * @file Member_Info_Database_Yaml.java
 * @brief 主に[団員管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Database;

import java.util.Date;
import java.util.Map;

import lombok.Data;






/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、データベースが関連したテストの際に用いる、
 * データベースセットアップ用のデータのYMLファイルをマッピングするエンティティを格納したファイル。
 * 
 * @details 
 * - このエンティティが対応するYMLファイルの名称は[musical-score-database.yml]である。
 * - 定義しているフィールド変数に関しては、対応するYMLファイルのタグ名にリンクした名称である。
 * 
 * @par 使用アノテーション
 * - @Data
 **************************************************************************************
 */ 
@Data
public class Member_Info_Database_Yaml {

  //! 団員番号
  private Map<String, String> member_id;

  //! 名前
  private Map<String, String> name;

  //! ふりがな
  private Map<String, String> name_pronu;

  //! 性別
  private Map<String, String> sex;

  //! 誕生日
  private Map<String, Date> birthday;

  //! 顔写真ファイル
  private Map<String, String> face_photo;

  //! 入団日
  private Map<String, Date> join_date;

  //! 退団日
  private Map<String, Date> ret_date;

  //! メースアドレス１
  private Map<String, String> email_1;

  //! メールアドレス２
  private Map<String, String> email_2;

  //! 電話番号１
  private Map<String, String> tel_1;

  //! 電話番号２
  private Map<String, String> tel_2;

  //! 現住所郵便番号
  private Map<String, String> addr_postcode;

  //! 現住所
  private Map<String, String> addr;

  //! 役職名
  private Map<String, String> position;

  //! 現役職着任日
  private Map<String, Date> position_arri_date;

  //! 職種名
  private Map<String, String> job;

  //! 配属部署
  private Map<String, String> assign_dept;

  //! 配属日
  private Map<String, Date> assign_date;

  //! 担当楽器
  private Map<String, String> inst_charge;

  //! その他コメント
  private Map<String, String> other_comment;
}