package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormFaci]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormFaci {
  All,
  Faci_Id,
  Faci_Name,
  Chief_Admin,
  Resp_Person,
  Buy_Date,
  Storage_Loc,
  Buy_Price,
  Disp_Date,
  Comment;
}