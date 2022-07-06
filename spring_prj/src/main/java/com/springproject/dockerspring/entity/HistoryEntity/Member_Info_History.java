package com.springproject.dockerspring.entity.HistoryEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.History_Common;
import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.NormalEntity.Member_Info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





/**************************************************************/
/*   [Member_Info_History]
/*   テーブル「Member_Info_History」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Member_Info_History")
public class Member_Info_History implements Serializable, EntitySetUp{


  /*****************************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /* 
  /* なお、履歴エンティティだけにある以下の3個の意味を
  /* 記述しておく。
  /*****************************************************/

  @Id
  private Integer history_id;  //履歴番号（履歴情報を一意に判別）

  private Date change_datetime;  //履歴記録日時
  private String change_kinds;  //履歴種別（「更新」「削除」「復元」がある）
  private String operation_user;  //操作したユーザー名

  private Integer serial_num;
  private String member_id;
  private String name;
  private String name_pronu;
  private String sex;
  private Date birthday;
  private byte[] face_photo;
  private Date join_date;
  private Date ret_date;
  private String email_1;
  private String email_2;
  private String tel_1;
  private String tel_2;
  private String addr_postcode;
  private String addr;
  private String position;
  private Date position_arri_date;
  private String job;
  private String assign_dept;
  private Date assign_date;
  private String inst_charge;
  private String other_comment;





  

  /******************************************************************************************/
  /*   [コンストラクタ]
  /*   データが格納済みの対応する通常エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Member_Info_History(Member_Info info, String kind, String username){

    this.history_id = null;
    this.change_datetime = new Date();

    try{
      if(kind.equals(HistoryKindEnum.UPDATE.getKind())
         || kind.equals(HistoryKindEnum.DELETE.getKind())
         || kind.equals(HistoryKindEnum.ROLLBACK.getKind())){
        this.change_kinds = kind;
      }else{
        throw new IllegalArgumentException();
      }
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Audio_Data_History:constructor(IllegalArgumentException)]");
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info_History:constructor]");
    }
    
    this.operation_user = username;
    this.serial_num = info.getSerial_num();
    this.member_id = info.getMember_id();
    this.name = info.getName();
    this.name_pronu = info.getName_pronu();
    this.sex = info.getSex();
    this.birthday = info.getBirthday();
    this.face_photo = info.getFace_photo();
    this.join_date = info.getJoin_date();
    this.ret_date = info.getRet_date();
    this.email_1 = info.getEmail_1();
    this.email_2 = info.getEmail_2();
    this.tel_1 = info.getTel_1();
    this.tel_2 = info.getTel_2();
    this.addr_postcode = info.getAddr_postcode();
    this.addr = info.getAddr();
    this.position = info.getPosition();
    this.position_arri_date = info.getPosition_arri_date();
    this.job = info.getJob();
    this.assign_dept = info.getAssign_dept();
    this.assign_date = info.getAssign_date();
    this.inst_charge = info.getInst_charge();
    this.other_comment = info.getOther_comment();

    stringSetNull();
  }






  /***************************************************************/
  /*   [stringSetNull]
  /*   呼び出された時点で、変数内に格納されている「文字列型」の
  /*   データが「空文字又は空白のみ」の場合、「Null」に置き換える。
  /*   これは、空白や空文字がデータベース内に入り込み、参照性制約違反
  /*   などの不具合が発生しないよう、確実に空データに「Null」を
  /*   入れるための処置である。
  /***************************************************************/

  public void stringSetNull(){

    try{
      if(this.operation_user == null || this.operation_user.isEmpty() || this.operation_user.isBlank()){
        this.operation_user = null;
      }

      if(this.member_id == null || this.member_id.isEmpty() || this.member_id.isBlank()){
        this.member_id = null;
      }

      if(this.name == null || this.name.isEmpty() || this.name.isBlank()){
        this.name = null;
      }

      if(this.name_pronu == null || this.name_pronu.isEmpty() || this.name_pronu.isBlank()){
        this.name_pronu = null;
      }

      if(this.sex == null || this.sex.isEmpty() || this.sex.isBlank()){
        this.sex = null;
      }

      if(this.email_1 == null || this.email_1.isEmpty() || this.email_1.isBlank()){
        this.email_1 = null;
      }

      if(this.email_2 == null || this.email_2.isEmpty() || this.email_2.isBlank()){
        this.email_2 = null;
      }

      if(this.tel_1 == null || this.tel_1.isEmpty() || this.tel_1.isBlank()){
        this.tel_1 = null;
      }

      if(this.tel_2 == null || this.tel_2.isEmpty() || this.tel_2.isBlank()){
        this.tel_2 = null;
      }

      if(this.addr_postcode == null || this.addr_postcode.isEmpty() || this.addr_postcode.isBlank()){
        this.addr_postcode = null;
      }

      if(this.addr == null || this.addr.isEmpty() || this.addr.isBlank()){
        this.addr = null;
      }

      if(this.position == null || this.position.isEmpty() || this.position.isBlank()){
        this.position = null;
      }

      if(this.job == null || this.job.isEmpty() || this.job.isBlank()){
        this.job = null;
      }

      if(this.assign_dept == null || this.assign_dept.isEmpty() || this.assign_dept.isBlank()){
        this.assign_dept = null;
      }

      if(this.inst_charge == null || this.inst_charge.isEmpty() || this.inst_charge.isBlank()){
        this.inst_charge = null;
      }

      if(this.other_comment == null || this.other_comment.isEmpty() || this.other_comment.isBlank()){
        this.other_comment = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info_History:stringSetNull]");
    }
  }







  /***************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/

  @Override
  public Map<String, String> makeMap() {
    Map<String, String> map = new HashMap<>();
    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE.getStr());
    SimpleDateFormat datetime = new SimpleDateFormat(DateFormat_Enum.DATETIME.getStr());

    stringSetNull();

    try{
      map.put(History_Common.History_Id.getKey(),
              this.history_id == null ? "" : String.valueOf(this.history_id));
              
      map.put(History_Common.Change_Datetime.getKey(),
              this.change_datetime == null ? "" : datetime.format(this.change_datetime));
              
      map.put(History_Common.Change_Kinds.getKey(),
              this.change_kinds == null ? "" : this.change_kinds);

      map.put(History_Common.Operation_User.getKey(),
              this.operation_user == null ? "" : this.operation_user);
              
      map.put(Member_Info_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Member_Info_Enum.Member_Id.getKey(), 
              this.member_id == null ? "" : this.member_id);
              
      map.put(Member_Info_Enum.Name.getKey(), 
              this.name == null ? "" : this.name);
              
      map.put(Member_Info_Enum.Name_Pronu.getKey(), 
              this.name_pronu == null ? "" : this.name_pronu);
              
      map.put(Member_Info_Enum.Sex.getKey(), 
              this.sex == null ? "" : this.sex);
              
      map.put(Member_Info_Enum.Birthday.getKey(), 
              this.birthday == null ? "" : date.format(this.birthday));
              
      map.put(Member_Info_Enum.Face_Photo.getKey(), 
              this.face_photo == null ? "" : base64encode(this.face_photo, DatatypeEnum.PHOTO));
              
      map.put(Member_Info_Enum.Join_Date.getKey(), 
              this.join_date == null ? "" : date.format(this.join_date));
              
      map.put(Member_Info_Enum.Ret_Date.getKey(), 
              this.ret_date == null ? "" : date.format(this.ret_date));
              
      map.put(Member_Info_Enum.Email_1.getKey(), 
              this.email_1 == null ? "" : this.email_1);
              
      map.put(Member_Info_Enum.Email_2.getKey(), 
              this.email_2 == null ? "" : this.email_2);
              
      map.put(Member_Info_Enum.Tel_1.getKey(), 
              this.tel_1 == null ? "" : this.tel_1);
              
      map.put(Member_Info_Enum.Tel_2.getKey(), 
              this.tel_2 == null ? "" : this.tel_2);
              
      map.put(Member_Info_Enum.Addr_Postcode.getKey(), 
              this.addr_postcode == null ? "" : this.addr_postcode);
              
      map.put(Member_Info_Enum.Addr.getKey(), 
              this.addr == null ? "" : this.addr);
              
      map.put(Member_Info_Enum.Position.getKey(), 
              this.position == null ? "" : this.position);
              
      map.put(Member_Info_Enum.Position_Arri_Date.getKey(), 
              this.position_arri_date == null ? "" : date.format(this.position_arri_date));
              
      map.put(Member_Info_Enum.Job.getKey(), 
              this.job == null ? "" : this.job);
              
      map.put(Member_Info_Enum.Assign_Dept.getKey(), 
              this.assign_dept == null ? "" : this.assign_dept);
              
      map.put(Member_Info_Enum.Assign_Date.getKey(), 
              this.assign_date == null ? "" : date.format(this.assign_date));
              
      map.put(Member_Info_Enum.Inst_Charge.getKey(), 
              this.inst_charge == null ? "" : this.inst_charge);
              
      map.put(Member_Info_Enum.Other_Comment.getKey(), 
              this.other_comment == null ? "" : this.other_comment);
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info_History:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Member_Info_History:makeMap(IllegalStateException)]");
    }

    return map;
  }
}