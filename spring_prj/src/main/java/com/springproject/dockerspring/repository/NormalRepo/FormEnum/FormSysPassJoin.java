package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormSysPassJoin]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormSysPassJoin {
  All,
  Member_Id,
  Name,
  Username,
  Auth_Id,
  Locking;
}