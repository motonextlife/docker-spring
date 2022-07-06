package com.springproject.dockerspring.entity.NormalEntity;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.form.CsvImplForm.MemberInfoForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Member_Info]
/*   テーブル「Member_Info」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Member_Info")
public class Member_Info implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
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







  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/
  
  public Member_Info(MemberInfoForm form) throws IOException{

    this.serial_num = form.getSerial_num();
    this.member_id = form.getMember_id();
    this.name = form.getName();
    this.name_pronu = form.getName_pronu();
    this.sex = form.getSex();
    this.birthday = form.getBirthday();

    try{
      /********************************************************************/
      /*   データのコンテンツタイプがPNGか判別。
      /********************************************************************/
      if(!form.getFace_photo().getContentType().equals(DatatypeEnum.PHOTO.getType())){
        throw new IllegalStateException();
      }

      this.face_photo = form.getFace_photo().getBytes();

    }catch(IOException e){
      throw new IOException("Error location [Member_Info:constructor]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Member_Info:constructor(IllegalStateException)]");
    }
    
    this.join_date = form.getJoin_date();
    this.ret_date = form.getRet_date();
    this.email_1 = form.getEmail_1();
    this.email_2 = form.getEmail_2();
    this.tel_1 = form.getTel_1();
    this.tel_2 = form.getTel_2();
    this.addr_postcode = form.getAddr_postcode();
    this.addr = form.getAddr();
    this.position = form.getPosition();
    this.position_arri_date = form.getPosition_arri_date();
    this.job = form.getJob();
    this.assign_dept = form.getAssign_dept();
    this.assign_date = form.getAssign_date();
    this.inst_charge = form.getInst_charge();
    this.other_comment = form.getOther_comment();

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Member_Info(Member_Info_History hist){

    this.serial_num = hist.getSerial_num();
    this.member_id = hist.getMember_id();
    this.name = hist.getName();
    this.name_pronu = hist.getName_pronu();
    this.sex = hist.getSex();
    this.birthday = hist.getBirthday();
    this.face_photo = hist.getFace_photo();
    this.join_date = hist.getJoin_date();
    this.ret_date = hist.getRet_date();
    this.email_1 = hist.getEmail_1();
    this.email_2 = hist.getEmail_2();
    this.tel_1 = hist.getTel_1();
    this.tel_2 = hist.getTel_2();
    this.addr_postcode = hist.getAddr_postcode();
    this.addr = hist.getAddr();
    this.position = hist.getPosition();
    this.position_arri_date = hist.getPosition_arri_date();
    this.job = hist.getJob();
    this.assign_dept = hist.getAssign_dept();
    this.assign_date = hist.getAssign_date();
    this.inst_charge = hist.getInst_charge();
    this.other_comment = hist.getOther_comment();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Member_Info(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
      this.serial_num = null;
      this.member_id = map.get(Member_Info_Enum.Member_Id.getKey());
      this.name = map.get(Member_Info_Enum.Name.getKey());
      this.name_pronu = map.get(Member_Info_Enum.Name_Pronu.getKey());
      this.sex = map.get(Member_Info_Enum.Sex.getKey());

      String tmp = map.get(Member_Info_Enum.Birthday.getKey());
      this.birthday = tmp == null ? null : date.parse(tmp);

      this.face_photo = null;

      tmp = map.get(Member_Info_Enum.Join_Date.getKey());
      this.join_date = tmp == null ? null : date.parse(tmp);

      tmp = map.get(Member_Info_Enum.Ret_Date.getKey());
      this.ret_date = tmp == null ? null : date.parse(tmp);

      this.email_1 = map.get(Member_Info_Enum.Email_1.getKey());
      this.email_2 = map.get(Member_Info_Enum.Email_2.getKey());
      this.tel_1 = map.get(Member_Info_Enum.Tel_1.getKey());
      this.tel_2 = map.get(Member_Info_Enum.Tel_2.getKey());
      this.addr_postcode = map.get(Member_Info_Enum.Addr_Postcode.getKey());
      this.addr = map.get(Member_Info_Enum.Addr.getKey());
      this.position = map.get(Member_Info_Enum.Position.getKey());

      tmp = map.get(Member_Info_Enum.Position_Arri_Date.getKey());
      this.position_arri_date = tmp == null ? null : date.parse(tmp);

      this.job = map.get(Member_Info_Enum.Job.getKey());
      this.assign_dept = map.get(Member_Info_Enum.Assign_Dept.getKey());

      tmp = map.get(Member_Info_Enum.Assign_Date.getKey());
      this.assign_date = tmp == null ? null : date.parse(tmp);
      
      this.inst_charge = map.get(Member_Info_Enum.Inst_Charge.getKey());
      this.other_comment = map.get(Member_Info_Enum.Other_Comment.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Member_Info:constructor3]", e.getErrorOffset());
    }

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
      throw new NullPointerException("Error location [Member_Info:stringSetNull]");
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

    stringSetNull();

    try{
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
      throw new NullPointerException("Error location [Member_Info:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Member_Info:makeMap(IllegalStateException)]");
    }

    return map;
  }
}