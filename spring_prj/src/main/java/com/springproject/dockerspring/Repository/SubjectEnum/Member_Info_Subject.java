/** 
 **************************************************************************************
 * @file Member_Info_Subject.java
 * 
 * @brief フォームから来た検索ジャンルを判別するための列挙型を格納するファイルである。
 * 主に[団員管理]機能のリポジトリで使用する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.SubjectEnum;



/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のリポジトリで、フォームから来た検索ジャンルを判別するための
 * 列挙型である。
 * 
 * @details 列挙型に定義する内容としては、「検索ジャンル名」を定義する。
 **************************************************************************************
 */ 
public enum Member_Info_Subject {
  All,
  Memberid,
  Name,
  NamePronu,
  Sex,
  Birthday,
  JoinDate,
  RetDate,
  Email,
  Tel,
  Postcode,
  Addr,
  Position,
  ArriDate,
  Job,
  AssignDept,
  AssignDate,
  InstCharge,
  Comment;
}