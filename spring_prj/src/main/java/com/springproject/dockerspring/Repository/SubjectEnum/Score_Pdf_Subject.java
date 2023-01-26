/** 
 **************************************************************************************
 * @file Score_Pdf_Subject.java
 * 
 * @brief フォームから来た検索ジャンルを判別するための列挙型を格納するファイルである。
 * 主に[楽譜データ管理]機能のリポジトリで使用する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.SubjectEnum;




/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]機能のリポジトリで、フォームから来た検索ジャンルを判別するための
 * 列挙型である。
 * 
 * @details 列挙型に定義する内容としては、「検索ジャンル名」を定義する。
 **************************************************************************************
 */ 
public enum Score_Pdf_Subject {
  All,
  ScoreName;
}