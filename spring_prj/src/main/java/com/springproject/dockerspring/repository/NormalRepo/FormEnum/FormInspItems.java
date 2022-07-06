package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormInspItems]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormInspItems {
  All,
  Inspsheet_Id,
  Inspsheet_Name,
  Inspsheet_Date,
  Inspsheet_Contents,
  Inspsheet_Itemkinds,
  Inspsheet_Itemunit;
}