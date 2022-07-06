package com.springproject.dockerspring.form.NormalForm;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;




/***********************************************************/
/*   [ComSearchForm]
/*   「一括情報取得時の検索ワード」に関するフォーム入力値の
/*    入力値のチェックを行う。
/***********************************************************/

@Data
public class ComSearchForm implements Serializable{


  /**************************************************/
  /* {検索ワード}
  /* 文字数は「90文字以内」まで受付可能。
  /* XSSの危険性がある以下の文字は受付できない。
  /**************************************************/
  @Pattern(regexp="^[^<>;:,\"\'`]*$")
  @Length(min=0, max=90)
  private String word;


  /**************************************************/
  /* {検索ジャンル}
  /* 文字数は「90文字以内」まで受付可能。
  /* XSSの危険性がある以下の文字は受付できない。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^<>;:,\"\'`]*$")
  @Length(min=0, max=90)
  private String subject;


  /******************************************************/
  /* {並び順指定}
  /* 未入力は許さない。
  /* 「true」「false」等の真偽値と判定される値のみ受付可能。
  /* 「true」で「昇順」指定となる。
  /******************************************************/
  @NotNull
  private Boolean order;
}