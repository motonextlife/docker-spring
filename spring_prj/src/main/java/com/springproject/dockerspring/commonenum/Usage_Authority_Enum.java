package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [Usage_Authority_Enum]
/*   主に「Usage_Authority」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Usage_Authority_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Auth_Id("権限番号", "auth_id"),
  Auth_Name("権限名", "auth_name"),
  Admin("管理者権限", "admin"),

  Memb_Info_Brows("団員参照", "m_i_brows"),
  Rec_Eval_Brows("採用考課参照", "r_e_brows"),
  Facility_Brows("設備参照", "f_brows"),
  Equip_Insp_Brows("点検参照", "e_i_brows"),
  Musical_Score_Brows("楽譜参照", "m_s_brows"),
  Sound_Source_Brows("音源参照", "s_s_brows"),

  Memb_Info_Change("団員変更", "m_i_change"),
  Rec_Eval_Change("採用考課変更", "r_e_change"),
  Facility_Change("設備変更", "f_change"),
  Equip_Insp_Change("点検変更", "e_i_change"),
  Musical_Score_Change("楽譜変更", "m_s_change"),
  Sound_Source_Change("音源変更", "s_s_change"),

  Memb_Info_Delete("団員削除", "m_i_delete"),
  Rec_Eval_Delete("採用考課削除", "r_e_delete"),
  Facility_Delete("設備削除", "f_delete"),
  Equip_Insp_Delete("点検削除", "e_i_delete"),
  Musical_Score_Delete("楽譜削除", "m_s_delete"),
  Sound_Source_Delete("音源削除", "s_s_delete"),

  Memb_Info_Hist_Brows("団員履歴参照", "m_i_hist_brows"),
  Rec_Eval_Hist_Brows("採用考課履歴参照", "r_e_hist_brows"),
  Facility_Hist_Brows("設備履歴参照", "f_hist_brows"),
  Equip_Insp_Hist_Brows("点検履歴参照", "e_i_hist_brows"),
  Musical_Score_Hist_Brows("楽譜履歴参照", "m_s_hist_brows"),
  Sound_Source_Hist_Brows("音源履歴参照", "s_s_hist_brows"),

  Memb_Info_Hist_Rollback("団員履歴ロールバック", "m_i_hist_rollback"),
  Rec_Eval_Hist_Rollback("採用考課履歴ロールバック", "r_e_hist_rollback"),
  Facility_Hist_Rollback("設備履歴ロールバック", "f_hist_rollback"),
  Equip_Insp_Hist_Rollback("点検履歴ロールバック", "e_i_hist_rollback"),
  Musical_Score_Hist_Rollback("楽譜履歴ロールバック", "m_s_hist_rollback"),
  Sound_Source_Hist_Rollback("音源履歴ロールバック", "s_s_hist_rollback");

  private final String text;
  private final String key;

  private Usage_Authority_Enum(String text, String key){
    this.text = text;
    this.key = key;
  }

  public String getText() {
    return this.text;
  }

  public String getKey() {
    return this.key;
  }
}
