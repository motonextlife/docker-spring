package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormMusicScore]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormMusicScore {
  All,
  Score_Id,
  Buy_Date,
  Song_Title,
  Composer,
  Arranger,
  Publisher,
  Strage_Loc,
  Disp_Date,
  Comment;
}