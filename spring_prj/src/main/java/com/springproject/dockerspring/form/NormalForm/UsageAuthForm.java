package com.springproject.dockerspring.form.NormalForm;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.springproject.dockerspring.form.OriginalAnno.AuthConnectCheck;

import lombok.Data;





/********************************************************/
/*   [UsageAuthForm]
/*   「利用権限」に関するフォーム入力値の
/*    入力値のチェックを行う。
/********************************************************/

@Data
@AuthConnectCheck
public class UsageAuthForm implements Serializable{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /****************************************************/
  /* {権限番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String auth_id;


  /**************************************************/
  /* {権限名}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String auth_name;


  /*******************************************************************************/
  /* {各権限付与}
  /* 「true」「false」等の真偽値と判定される値のみ受付可能。
  /* 「true」で「許可」指定となる。
  /* 
  /* なお、権限の付与には以下の条件がある。
  /* ・管理者権限がある場合、「他の権限は一切ない」事。
  /* ・通常データの変更権限がある場合、「必ず対応する参照権限がある」事。
  /* ・通常データの削除権限がある場合、「必ず対応する変更権限と参照権限がある」事。
  /* ・履歴データのロールバック権限がある場合、「必ず対応する履歴参照権限がある」事。
  /*******************************************************************************/
  private Boolean admin;
  
  private Boolean m_i_brows;
  private Boolean r_e_brows;
  private Boolean f_brows;
  private Boolean e_i_brows;
  private Boolean m_s_brows;
  private Boolean s_s_brows;

  private Boolean m_i_change;
  private Boolean r_e_change;
  private Boolean f_change;
  private Boolean e_i_change;
  private Boolean m_s_change;
  private Boolean s_s_change;

  private Boolean m_i_delete;
  private Boolean r_e_delete;
  private Boolean f_delete;
  private Boolean e_i_delete;
  private Boolean m_s_delete;
  private Boolean s_s_delete;

  private Boolean m_i_hist_brows;
  private Boolean r_e_hist_brows;
  private Boolean f_hist_brows;
  private Boolean e_i_hist_brows;
  private Boolean m_s_hist_brows;
  private Boolean s_s_hist_brows;

  private Boolean m_i_hist_rollback;
  private Boolean r_e_hist_rollback;
  private Boolean f_hist_rollback;
  private Boolean e_i_hist_rollback;
  private Boolean m_s_hist_rollback;
  private Boolean s_s_hist_rollback;
}