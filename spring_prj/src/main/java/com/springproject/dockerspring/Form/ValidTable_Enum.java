/** 
 **************************************************************************************
 * @file ValidTable_Enum.java
 * @brief 主に[バリデーション時に索引対象になるテーブルの指定]機能において使用する列挙型を
 * 格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form;







/** 
 **************************************************************************************
 * @brief 主に[バリデーション時に索引対象になるテーブルの指定]機能において使用する列挙型
 * 
 * @details 
 * - 主にデータベースからのデータを元にバリデーションを行うフォームについて、データを取り出す
 * データベース上のテーブルを指定するためにある。
 **************************************************************************************
 */ 
public enum ValidTable_Enum {
  Audio_Data_History,
  Facility_History,
  Facility_Photo_History,
  Member_Info_History,
  Musical_Score_History,
  Score_Pdf_History,
  Sound_Source_History,
  Audio_Data,
  Facility_Photo,
  Facility,
  Member_Info,
  Musical_Score,
  Score_Pdf,
  Sound_Source,
  System_User,
  Usage_Authority;
}