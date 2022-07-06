package com.springproject.dockerspring.form.NormalForm;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.form.OriginalAnno.DateCorrCheck;

import lombok.Data;





/***********************************************************/
/*   [ComHistGetForm]
/*   「各履歴の期間と番号を定めた取得」に関するフォーム入力値の
/*    入力値のチェックを行う。
/***********************************************************/

@Data
@DateCorrCheck(start={"start_datetime"}, end={"end_datetime"})
public class ComHistGetForm implements Serializable{


  /****************************************************/
  /* {全ジャンルの番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String id;


  /**************************************************/
  /* {開始日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
  private Date start_datetime;


  /**************************************************/
  /* {終了日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* また、開始日以前の日付に設定されてはならない。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
  private Date end_datetime;


  /******************************************************/
  /* {並び順指定}
  /* 未入力は許さない。
  /* 「true」「false」等の真偽値と判定される値のみ受付可能。
  /* 「true」で「昇順」指定となる。
  /******************************************************/
  @NotNull
  private Boolean order;
}