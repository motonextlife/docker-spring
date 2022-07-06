package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormRecEvalItems]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormRecEvalItems {
  All,
  Evalsheet_Id,
  Evalsheet_Name,
  Evalsheet_Date,
  Evalsheet_Kinds,
  Evalsheet_Contents,
  Evalsheet_Itemkinds;
}