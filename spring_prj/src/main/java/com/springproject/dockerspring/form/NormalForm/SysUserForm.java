package com.springproject.dockerspring.form.NormalForm;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.springproject.dockerspring.form.OriginalAnno.ForeignKey;

import lombok.Data;





/********************************************************/
/*   [SysUserForm]
/*   「システム利用者」に関するフォーム入力値の
/*    入力値のチェックを行う。
/********************************************************/

@Data
public class SysUserForm implements Serializable{


  /************************************************************/
  /* {団員番号}
  /* 未入力は許されない。
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Member_Info」内に存在する団員番号のみ設定可能。
  /************************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @ForeignKey(table="Member_Info")
  private String member_id;


  /**************************************************/
  /* {ユーザー名}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「8～20文字」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[0-9a-zA-Z]*$")
  @Length(min=8, max=20)
  private String username;


  /**************************************************/
  /* {パスワード}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「8～20文字」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[0-9a-zA-Z]*$")
  @Length(min=8, max=20)
  private String password;


  /************************************************************/
  /* {付与権限番号}
  /* 未入力は許されない。
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Usage_Authority」内に存在する権限番号のみ設定可能。
  /************************************************************/
  @NotBlank
  @Length(min=0, max=90)
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @ForeignKey(table="Usage_Authority")
  private String auth_id;
}