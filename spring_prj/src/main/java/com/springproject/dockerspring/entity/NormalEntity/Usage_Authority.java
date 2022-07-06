package com.springproject.dockerspring.entity.NormalEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.Usage_Authority_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.form.NormalForm.UsageAuthForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Usage_Authority]
/*   テーブル「Usage_Authority」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Usage_Authority")
public class Usage_Authority implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /* 権限の詳しい適用範囲に関しては、各コントローラーの
  /* 設計書を参照。
  /***********************************************/

  @Id
  private Integer serial_num;

  private String auth_id;
  private String auth_name;
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




  private final String TRUE_STR = "true";
  private final String FALSE_STR = "false";
  

  /*************************************************************************************/
  /*   [コンストラクタ]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Usage_Authority(UsageAuthForm form){

    this.serial_num = form.getSerial_num();
    this.auth_id = form.getAuth_id();
    this.auth_name = form.getAuth_name();
		this.admin = form.getAdmin();

    this.m_i_brows = form.getM_i_brows();
    this.r_e_brows = form.getR_e_brows();
    this.f_brows = form.getF_brows();
    this.e_i_brows = form.getE_i_brows();
    this.m_s_brows = form.getM_s_brows();
    this.s_s_brows = form.getS_s_brows();
  
    this.m_i_change = form.getM_i_change();
    this.r_e_change = form.getR_e_change();
    this.f_change = form.getF_change();
    this.e_i_change = form.getE_i_change();
    this.m_s_change = form.getM_s_change();
    this.s_s_change = form.getS_s_change();
  
    this.m_i_delete = form.getM_i_delete();
    this.r_e_delete = form.getR_e_delete();
    this.f_delete = form.getF_delete();
    this.e_i_delete = form.getE_i_delete();
    this.m_s_delete = form.getM_s_delete();
    this.s_s_delete = form.getS_s_delete();
  
    this.m_i_hist_brows = form.getM_i_hist_brows();
    this.r_e_hist_brows = form.getR_e_hist_brows();
    this.f_hist_brows = form.getF_hist_brows();
    this.e_i_hist_brows = form.getE_i_hist_brows();
    this.m_s_hist_brows = form.getM_s_hist_brows();
    this.s_s_hist_brows = form.getS_s_hist_brows();
  
    this.m_i_hist_rollback = form.getM_i_hist_rollback();
    this.r_e_hist_rollback = form.getR_e_hist_rollback();
    this.f_hist_rollback = form.getF_hist_rollback();
    this.e_i_hist_rollback = form.getE_i_hist_rollback();
    this.m_s_hist_rollback = form.getM_s_hist_rollback();
    this.s_s_hist_rollback = form.getS_s_hist_rollback();
    
    stringSetNull();
  }






  /***************************************************************/
  /*   [stringSetNull]
  /*   呼び出された時点で、変数内に格納されている「文字列型」の
  /*   データが「空文字又は空白のみ」の場合、「Null」に置き換える。
  /*   これは、空白や空文字がデータベース内に入り込み、参照性制約違反
  /*   などの不具合が発生しないよう、確実に空データに「Null」を
  /*   入れるための処置である。
  /*   なお、このエンティティの権限情報に関してのみ、「Null」で
  /*   あればデフォルト値として「false」を入れる。
  /***************************************************************/

  public void stringSetNull(){
    
    try{
      if(this.auth_id == null || this.auth_id.isEmpty() || this.auth_id.isBlank()){
        this.auth_id = null;
      }

      if(this.auth_name == null || this.auth_name.isEmpty() || this.auth_name.isBlank()){
        this.auth_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Usage_Authority:stringSetNull]");
    }

    this.admin = this.admin == null ? false : this.admin;

    this.m_i_brows = this.m_i_brows == null ? false : this.m_i_brows;
    this.r_e_brows = this.r_e_brows == null ? false : this.r_e_brows;
    this.f_brows = this.f_brows == null ? false : this.f_brows;
    this.e_i_brows = this.e_i_brows == null ? false : this.e_i_brows;
    this.m_s_brows = this.m_s_brows == null ? false : this.m_s_brows;
    this.s_s_brows = this.s_s_brows == null ? false : this.s_s_brows;
  
    this.m_i_change = this.m_i_change == null ? false : this.m_i_change;
    this.r_e_change = this.r_e_change == null ? false : this.r_e_change;
    this.f_change = this.f_change == null ? false : this.f_change;
    this.e_i_change = this.e_i_change == null ? false : this.e_i_change;
    this.m_s_change = this.m_s_change == null ? false : this.m_s_change;
    this.s_s_change = this.s_s_change == null ? false : this.s_s_change;

		this.m_i_delete = this.m_i_delete == null ? false : this.m_i_delete;
    this.r_e_delete = this.r_e_delete == null ? false : this.r_e_delete;
    this.f_delete = this.f_delete == null ? false : this.f_delete;
    this.e_i_delete = this.e_i_delete == null ? false : this.e_i_delete;
    this.m_s_delete = this.m_s_delete == null ? false : this.m_s_delete;
    this.s_s_delete = this.s_s_delete == null ? false : this.s_s_delete;

		this.m_i_hist_brows = this.m_i_hist_brows == null ? false : this.m_i_hist_brows;
    this.r_e_hist_brows = this.r_e_hist_brows == null ? false : this.r_e_hist_brows;
    this.f_hist_brows = this.f_hist_brows == null ? false : this.f_hist_brows;
    this.e_i_hist_brows = this.e_i_hist_brows == null ? false : this.e_i_hist_brows;
    this.m_s_hist_brows = this.m_s_hist_brows == null ? false : this.m_s_hist_brows;
    this.s_s_hist_brows = this.s_s_hist_brows == null ? false : this.s_s_hist_brows;

		this.m_i_hist_rollback = this.m_i_hist_rollback == null ? false : this.m_i_hist_rollback;
    this.r_e_hist_rollback = this.r_e_hist_rollback == null ? false : this.r_e_hist_rollback;
    this.f_hist_rollback = this.f_hist_rollback == null ? false : this.f_hist_rollback;
    this.e_i_hist_rollback = this.e_i_hist_rollback == null ? false : this.e_i_hist_rollback;
    this.m_s_hist_rollback = this.m_s_hist_rollback == null ? false : this.m_s_hist_rollback;
    this.s_s_hist_rollback = this.s_s_hist_rollback == null ? false : this.s_s_hist_rollback;
  }







  /***************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/

  @Override
  public Map<String, String> makeMap() {
    Map<String, String> map = new HashMap<>();

    stringSetNull();

    try{
			map.put(Usage_Authority_Enum.Serial_Num.getKey(), 
							this.serial_num == null ? "" : String.valueOf(this.serial_num));
      
			map.put(Usage_Authority_Enum.Auth_Id.getKey(), 
							this.auth_id == null ? "" : this.auth_id);
      
			map.put(Usage_Authority_Enum.Auth_Name.getKey(), 
							this.auth_name == null ? "" : this.auth_name);
      
			map.put(Usage_Authority_Enum.Admin.getKey(), 
							this.admin ? TRUE_STR : FALSE_STR); 
			
			map.put(Usage_Authority_Enum.Memb_Info_Brows.getKey(), 
							this.m_i_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Rec_Eval_Brows.getKey(), 
							this.r_e_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Facility_Brows.getKey(), 
							this.f_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Equip_Insp_Brows.getKey(), 
							this.e_i_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Musical_Score_Brows.getKey(), 
							this.m_s_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Sound_Source_Brows.getKey(), 
							this.s_s_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Memb_Info_Change.getKey(), 
							this.m_i_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Rec_Eval_Change.getKey(), 
							this.r_e_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Facility_Change.getKey(), 
							this.f_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Equip_Insp_Change.getKey(), 
							this.e_i_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Musical_Score_Change.getKey(), 
							this.m_s_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Sound_Source_Change.getKey(), 
							this.s_s_change ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Memb_Info_Delete.getKey(), 
							this.m_i_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Rec_Eval_Delete.getKey(), 
							this.r_e_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Facility_Delete.getKey(), 
							this.f_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Equip_Insp_Delete.getKey(), 
							this.e_i_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Musical_Score_Delete.getKey(), 
							this.m_s_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Sound_Source_Delete.getKey(), 
							this.s_s_delete ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Memb_Info_Hist_Brows.getKey(), 
							this.m_i_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Rec_Eval_Hist_Brows.getKey(), 
							this.r_e_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Facility_Hist_Brows.getKey(), 
							this.f_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Equip_Insp_Hist_Brows.getKey(), 
							this.e_i_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Musical_Score_Hist_Brows.getKey(), 
							this.m_s_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Sound_Source_Hist_Brows.getKey(), 
							this.s_s_hist_brows ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Memb_Info_Hist_Rollback.getKey(), 
							this.m_i_hist_rollback ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Rec_Eval_Hist_Rollback.getKey(), 
							this.r_e_hist_rollback ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Facility_Hist_Rollback.getKey(), 
							this.f_hist_rollback ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Equip_Insp_Hist_Rollback.getKey(), 
							this.e_i_hist_rollback ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Musical_Score_Hist_Rollback.getKey(), 
							this.m_s_hist_rollback ? TRUE_STR : FALSE_STR);
			
			map.put(Usage_Authority_Enum.Sound_Source_Hist_Rollback.getKey(), 
							this.s_s_hist_rollback ? TRUE_STR : FALSE_STR);

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Usage_Authority:makeMap]");
    }

    return map;
  }





  /***************************************************************/
  /*   [getAuthority]
  /*   指定された列挙型の権限の、エンティティ内で定義されている
  /*   真偽値を返却する。
  /*   主に、各機能の権限判別の際に使用。
  /***************************************************************/
  public Boolean getAuthority(Usage_Authority_Enum auth){

    switch(auth){
      case Admin:
        return this.admin;
      case Memb_Info_Brows:
        return this.m_i_brows;
      case Rec_Eval_Brows:
        return this.r_e_brows;
      case Facility_Brows:
        return this.f_brows;
      case Equip_Insp_Brows:
        return this.e_i_brows;
      case Musical_Score_Brows:
        return this.m_s_brows;
      case Sound_Source_Brows:
        return this.s_s_brows;
      case Memb_Info_Change:
        return this.m_i_change;
      case Rec_Eval_Change:
        return this.r_e_change;
      case Facility_Change:
        return this.f_change;
      case Equip_Insp_Change:
        return this.e_i_change;
      case Musical_Score_Change:
        return this.m_s_change;
      case Sound_Source_Change:
        return this.s_s_change;
      case Memb_Info_Delete:
        return this.m_i_delete;
      case Rec_Eval_Delete:
        return this.r_e_delete;
      case Facility_Delete:
        return this.f_delete;
      case Equip_Insp_Delete:
        return this.e_i_delete;
      case Musical_Score_Delete:
        return this.m_s_delete;
      case Sound_Source_Delete:
        return this.s_s_delete;
      case Memb_Info_Hist_Brows:
        return this.m_i_hist_brows;
      case Rec_Eval_Hist_Brows:
        return this.r_e_hist_brows;
      case Facility_Hist_Brows:
        return this.f_hist_brows;
      case Equip_Insp_Hist_Brows:
        return this.e_i_hist_brows;
      case Musical_Score_Hist_Brows:
        return this.m_s_hist_brows;
      case Sound_Source_Hist_Brows:
        return this.s_s_hist_brows;
      case Memb_Info_Hist_Rollback:
        return this.m_i_hist_rollback;
      case Rec_Eval_Hist_Rollback:
        return this.r_e_hist_rollback;
      case Facility_Hist_Rollback:
        return this.f_hist_rollback;
      case Equip_Insp_Hist_Rollback:
        return this.e_i_hist_rollback;
      case Musical_Score_Hist_Rollback:
        return this.m_s_hist_rollback;
      case Sound_Source_Hist_Rollback:
        return this.s_s_hist_rollback;
      default:
        return false;
    }
  }
}