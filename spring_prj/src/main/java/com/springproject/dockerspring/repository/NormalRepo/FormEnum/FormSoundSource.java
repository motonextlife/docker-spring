package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormSoundSource]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormSoundSource {
  All,
  Sound_Id,
  Upload_Date,
  Song_Title,
  Composer,
  Performer,
  Publisher,
  Comment;
}