package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormEquipInsp]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormEquipInsp {
  All,
  Inspect_Id,
  Faci_Id,
  Inspect_Name,
  Inspect_Date,
  Inspsheet_Id,
  Inspect_Contents,
  Inspect_Rank;
}