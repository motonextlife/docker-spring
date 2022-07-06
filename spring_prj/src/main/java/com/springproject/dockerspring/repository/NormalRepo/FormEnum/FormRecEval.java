package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormRecEval]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormRecEval {
  All,
  Eval_Id,
  Member_Id,
  Eval_Name,
  Eval_Date,
  Evalsheet_Id,
  Eval_Contents,
  Eval_Rank;
}