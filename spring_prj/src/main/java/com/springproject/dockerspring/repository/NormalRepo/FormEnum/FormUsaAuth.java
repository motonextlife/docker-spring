package com.springproject.dockerspring.repository.NormalRepo.FormEnum;


/***************************************************************************/
/*   [FormUsaAuth]
/*   フォームから来た検索ジャンルを判別するための列挙型を格納するクラスである。
/*   主にリポジトリの検索メソッド内で使用する。
/***************************************************************************/
public enum FormUsaAuth {
  All,
  Admin,
  Memb_Info_Brows,
  Rec_Eval_Brows,
  Facility_Brows,
  Equip_Insp_Brows,
  Musical_Score_Brows,
  Sound_Source_Brows,
  Memb_Info_Change,
  Rec_Eval_Change,
  Facility_Change,
  Equip_Insp_Change,
  Musical_Score_Change,
  Sound_Source_Change,
  Memb_Info_Delete,
  Rec_Eval_Delete,
  Facility_Delete,
  Equip_Insp_Delete,
  Musical_Score_Delete,
  Sound_Source_Delete,
  Memb_Info_Hist_Brows,
  Rec_Eval_Hist_Brows,
  Facility_Hist_Brows,
  Equip_Insp_Hist_Brows,
  Musical_Score_Hist_Brows,
  Sound_Source_Hist_Brows,
  Memb_Info_Hist_Rollback,
  Rec_Eval_Hist_Rollback,
  Facility_Hist_Rollback,
  Equip_Insp_Hist_Rollback,
  Musical_Score_Hist_Rollback,
  Sound_Source_Hist_Rollback;
}
