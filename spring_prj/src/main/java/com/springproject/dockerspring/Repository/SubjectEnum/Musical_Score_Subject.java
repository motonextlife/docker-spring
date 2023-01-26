/** 
 **************************************************************************************
 * @file Musical_Score_Subject.java
 * 
 * @brief フォームから来た検索ジャンルを判別するための列挙型を格納するファイルである。
 * 主に[楽譜管理]機能のリポジトリで使用する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.SubjectEnum;



/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能のリポジトリで、フォームから来た検索ジャンルを判別するための
 * 列挙型である。
 * 
 * @details 列挙型に定義する内容としては、「検索ジャンル名」を定義する。
 **************************************************************************************
 */ 
public enum Musical_Score_Subject {
  All,
  ScoreId,
  BuyDate,
  SongTitle,
  Composer,
  Arranger,
  Publisher,
  StorageLoc,
  DispDate,
  Comment;
}