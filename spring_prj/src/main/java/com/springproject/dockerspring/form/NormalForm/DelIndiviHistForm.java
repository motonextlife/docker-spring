package com.springproject.dockerspring.form.NormalForm;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.form.OriginalAnno.DateCorrCheck;

import lombok.Data;




/***********************************************************/
/*   [DelIndiviHistForm]
/*   「指定した履歴テーブルの期間を定めた削除」
/*    に関するフォーム入力値の入力値のチェックを行う。
/***********************************************************/

@Data
@DateCorrCheck(start={"start_datetime"}, end={"end_datetime"})
public class DelIndiviHistForm implements Serializable{


  /****************************************************/
  /* {対象テーブルの指定}
  /* 空白や空文字は許さず「以下の指定文字」のみ受け付ける。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="All| " 
                + "Audio_Data| " 
                + "Equip_Inspect_Photo| " 
                + "Equip_Inspect| " 
                + "Facility| " 
                + "Inspect_Items| " 
                + "Member_Info| " 
                + "Musical_Score| " 
                + "Rec_Eval_Items| " 
                + "Rec_Eval_Record| " 
                + "Rec_Eval| " 
                + "Score_Pdf| " 
                + "Sound_Source")
  private String table_name;


  /**************************************************/
  /* {開始日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date start_datetime;


  /**************************************************/
  /* {終了日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* また、開始日以前の日付に設定されてはならない。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date end_datetime;
}