package com.springproject.dockerspring.entity.NormalEntity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.Facility_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.form.CsvImplForm.FacilityForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Facility]
/*   テーブル「Facility」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Facility")
public class Facility implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
  private Integer serial_num;

  private String faci_id;
  private String faci_name;
  private String chief_admin;
  private String resp_person;
  private Date buy_date;
  private String storage_loc;
  private Integer buy_price;
  private Date disp_date;
  private String other_comment;






  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Facility(FacilityForm form){

    this.serial_num = form.getSerial_num();
    this.faci_id = form.getFaci_id();
    this.faci_name = form.getFaci_name();
    this.chief_admin = form.getChief_admin();
    this.resp_person = form.getResp_person();
    this.buy_date = form.getBuy_date();
    this.storage_loc = form.getStorage_loc();
    this.buy_price = form.getBuy_price();
    this.disp_date = form.getDisp_date();
    this.other_comment = form.getOther_comment();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Facility(Facility_History hist){

    this.serial_num = hist.getSerial_num();
    this.faci_id = hist.getFaci_id();
    this.faci_name = hist.getFaci_name();
    this.chief_admin = hist.getChief_admin();
    this.resp_person = hist.getResp_person();
    this.buy_date = hist.getBuy_date();
    this.storage_loc = hist.getStorage_loc();
    this.buy_price = hist.getBuy_price();
    this.disp_date = hist.getDisp_date();
    this.other_comment = hist.getOther_comment();

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Facility(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
      this.serial_num = null;
      this.faci_id = map.get(Facility_Enum.Faci_Id.getKey());
      this.faci_name = map.get(Facility_Enum.Faci_Name.getKey());
      this.chief_admin = map.get(Facility_Enum.Chief_Admin.getKey());
      this.resp_person = map.get(Facility_Enum.Resp_Person.getKey());

      String tmp = map.get(Facility_Enum.Buy_Date.getKey());
      this.buy_date = tmp == null ? null : date.parse(tmp);

      this.storage_loc = map.get(Facility_Enum.Storage_Loc.getKey());

      tmp = map.get(Facility_Enum.Buy_Price.getKey());
      this.buy_price = tmp == null ? null : Integer.parseInt(tmp);

      tmp = map.get(Facility_Enum.Disp_Date.getKey());
      this.disp_date = tmp == null ? null : date.parse(tmp);

      this.other_comment = map.get(Facility_Enum.Other_Comment.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Facility:constructor3]", e.getErrorOffset());
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
      if(this.faci_id == null || this.faci_id.isEmpty() || this.faci_id.isBlank()){
        this.faci_id = null;
      }

      if(this.faci_name == null || this.faci_name.isEmpty() || this.faci_name.isBlank()){
        this.faci_name = null;
      }

      if(this.chief_admin == null || this.chief_admin.isEmpty() || this.chief_admin.isBlank()){
        this.chief_admin = null;
      }

      if(this.resp_person == null || this.resp_person.isEmpty() || this.resp_person.isBlank()){
        this.resp_person = null;
      }

      if(this.storage_loc == null || this.storage_loc.isEmpty() || this.storage_loc.isBlank()){
        this.storage_loc = null;
      }

      if(this.other_comment == null || this.other_comment.isEmpty() || this.other_comment.isBlank()){
        this.other_comment = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Facility:stringSetNull]");
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
      map.put(Facility_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Facility_Enum.Faci_Id.getKey(), 
              this.faci_id == null ? "" : this.faci_id);
              
      map.put(Facility_Enum.Faci_Name.getKey(), 
              this.faci_name == null ? "" : this.faci_name);
              
      map.put(Facility_Enum.Chief_Admin.getKey(), 
              this.chief_admin == null ? "" : this.chief_admin);
              
      map.put(Facility_Enum.Resp_Person.getKey(), 
              this.resp_person == null ? "" : this.resp_person);
              
      map.put(Facility_Enum.Buy_Date.getKey(), 
              this.buy_date == null ? "" : date.format(this.buy_date));
              
      map.put(Facility_Enum.Storage_Loc.getKey(), 
              this.storage_loc == null ? "" : this.storage_loc);
              
      map.put(Facility_Enum.Buy_Price.getKey(), 
              this.buy_price == null ? "" : String.valueOf(this.buy_price));
              
      map.put(Facility_Enum.Disp_Date.getKey(), 
              this.disp_date == null ? "" : date.format(this.disp_date));
              
      map.put(Facility_Enum.Other_Comment.getKey(), 
              this.other_comment == null ? "" : this.other_comment);
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Facility:makeMap]");
    }

    return map;
  }
}