package com.springproject.dockerspring.entity.HistoryEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.Equip_Inspect_Enum;
import com.springproject.dockerspring.commonenum.History_Common;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Equip_Inspect_History]
/*   テーブル「Equip_Inspect_History」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Equip_Inspect_History")
public class Equip_Inspect_History implements Serializable, EntitySetUp{


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
  private String inspect_id;
  private String faci_id;
  private String inspect_name;
  private Date inspect_date;
  private String inspsheet_id;
  private String insp_contents_1;
  private String insp_rank_1;
  private String insp_contents_2;
  private String insp_rank_2;
  private String insp_contents_3;
  private String insp_rank_3;
  private String insp_contents_4;
  private String insp_rank_4;
  private String insp_contents_5;
  private String insp_rank_5;
  private String insp_contents_6;
  private String insp_rank_6;
  private String insp_contents_7;
  private String insp_rank_7;
  private String insp_contents_8;
  private String insp_rank_8;
  private String insp_contents_9;
  private String insp_rank_9;
  private String insp_contents_10;
  private String insp_rank_10;






  /******************************************************************************************/
  /*   [コンストラクタ]
  /*   データが格納済みの対応する通常エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Equip_Inspect_History(Equip_Inspect info, String kind, String username){

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
      throw new NullPointerException("Error location [Equip_Inspect_History:constructor]");
    }

    this.operation_user = username;
    this.serial_num = info.getSerial_num();
    this.inspect_id = info.getInspect_id();
    this.faci_id = info.getFaci_id();
    this.inspect_name = info.getInspect_name();
    this.inspect_date = info.getInspect_date();
    this.inspsheet_id = info.getInspsheet_id();
    this.insp_contents_1 = info.getInsp_contents_1();
    this.insp_rank_1 = info.getInsp_rank_1();
    this.insp_contents_2 = info.getInsp_contents_2();
    this.insp_rank_2 = info.getInsp_rank_2();
    this.insp_contents_3 = info.getInsp_contents_3();
    this.insp_rank_3 = info.getInsp_rank_3();
    this.insp_contents_4 = info.getInsp_contents_4();
    this.insp_rank_4 = info.getInsp_rank_4();
    this.insp_contents_5 = info.getInsp_contents_5();
    this.insp_rank_5 = info.getInsp_rank_5();
    this.insp_contents_6 = info.getInsp_contents_6();
    this.insp_rank_6 = info.getInsp_rank_6();
    this.insp_contents_7 = info.getInsp_contents_7();
    this.insp_rank_7 = info.getInsp_rank_7();
    this.insp_contents_8 = info.getInsp_contents_8();
    this.insp_rank_8 = info.getInsp_rank_8();
    this.insp_contents_9 = info.getInsp_contents_9();
    this.insp_rank_9 = info.getInsp_rank_9();
    this.insp_contents_10 = info.getInsp_contents_10();
    this.insp_rank_10 = info.getInsp_rank_10();

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

      if(this.inspect_id == null || this.inspect_id.isEmpty() || this.inspect_id.isBlank()){
        this.inspect_id = null;
      }

      if(this.faci_id == null || this.faci_id.isEmpty() || this.faci_id.isBlank()){
        this.faci_id = null;
      }

      if(this.inspect_name == null || this.inspect_name.isEmpty() || this.inspect_name.isBlank()){
        this.inspect_name = null;
      }

      if(this.inspsheet_id == null || this.inspsheet_id.isEmpty() || this.inspsheet_id.isBlank()){
        this.inspsheet_id = null;
      }

      if(this.insp_contents_1 == null || this.insp_contents_1.isEmpty() || this.insp_contents_1.isBlank()){
        this.insp_contents_1 = null;
      }

      if(this.insp_rank_1 == null || this.insp_rank_1.isEmpty() || this.insp_rank_1.isBlank()){
        this.insp_rank_1 = null;
      }

      if(this.insp_contents_2 == null || this.insp_contents_2.isEmpty() || this.insp_contents_2.isBlank()){
        this.insp_contents_2 = null;
      }

      if(this.insp_rank_2 == null || this.insp_rank_2.isEmpty() || this.insp_rank_2.isBlank()){
        this.insp_rank_2 = null;
      }

      if(this.insp_contents_3 == null || this.insp_contents_3.isEmpty() || this.insp_contents_3.isBlank()){
        this.insp_contents_3 = null;
      }

      if(this.insp_rank_3 == null || this.insp_rank_3.isEmpty() || this.insp_rank_3.isBlank()){
        this.insp_rank_3 = null;
      }

      if(this.insp_contents_4 == null || this.insp_contents_4.isEmpty() || this.insp_contents_4.isBlank()){
        this.insp_contents_4 = null;
      }

      if(this.insp_rank_4 == null || this.insp_rank_4.isEmpty() || this.insp_rank_4.isBlank()){
        this.insp_rank_4 = null;
      }

      if(this.insp_contents_5 == null || this.insp_contents_5.isEmpty() || this.insp_contents_5.isBlank()){
        this.insp_contents_5 = null;
      }

      if(this.insp_rank_5 == null || this.insp_rank_5.isEmpty() || this.insp_rank_5.isBlank()){
        this.insp_rank_5 = null;
      }

      if(this.insp_contents_6 == null || this.insp_contents_6.isEmpty() || this.insp_contents_6.isBlank()){
        this.insp_contents_6 = null;
      }

      if(this.insp_rank_6 == null || this.insp_rank_6.isEmpty() || this.insp_rank_6.isBlank()){
        this.insp_rank_6 = null;
      }

      if(this.insp_contents_7 == null || this.insp_contents_7.isEmpty() || this.insp_contents_7.isBlank()){
        this.insp_contents_7 = null;
      }

      if(this.insp_rank_7 == null || this.insp_rank_7.isEmpty() || this.insp_rank_7.isBlank()){
        this.insp_rank_7 = null;
      }

      if(this.insp_contents_8 == null || this.insp_contents_8.isEmpty() || this.insp_contents_8.isBlank()){
        this.insp_contents_8 = null;
      }

      if(this.insp_rank_8 == null || this.insp_rank_8.isEmpty() || this.insp_rank_8.isBlank()){
        this.insp_rank_8 = null;
      }

      if(this.insp_contents_9 == null || this.insp_contents_9.isEmpty() || this.insp_contents_9.isBlank()){
        this.insp_contents_9 = null;
      }

      if(this.insp_rank_9 == null || this.insp_rank_9.isEmpty() || this.insp_rank_9.isBlank()){
        this.insp_rank_9 = null;
      }

      if(this.insp_contents_10 == null || this.insp_contents_10.isEmpty() || this.insp_contents_10.isBlank()){
        this.insp_contents_10 = null;
      }

      if(this.insp_rank_10 == null || this.insp_rank_10.isEmpty() || this.insp_rank_10.isBlank()){
        this.insp_rank_10 = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_History:stringSetNull]");
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
              
      map.put(Equip_Inspect_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Equip_Inspect_Enum.Inspect_Id.getKey(), 
              this.inspect_id == null ? "" : this.inspect_id);
              
      map.put(Equip_Inspect_Enum.Faci_Id.getKey(), 
              this.faci_id == null ? "" : this.faci_id);
              
      map.put(Equip_Inspect_Enum.Inspect_Name.getKey(), 
              this.inspect_name == null ? "" : this.inspect_name);
              
      map.put(Equip_Inspect_Enum.Inspect_Date.getKey(), 
              this.inspect_date == null ? "" : date.format(this.inspect_date));
              
      map.put(Equip_Inspect_Enum.Inspsheet_Id.getKey(), 
              this.inspsheet_id == null ? "" : this.inspsheet_id);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_1.getKey(), 
              this.insp_contents_1 == null ? "" : this.insp_contents_1);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_1.getKey(), 
              this.insp_rank_1 == null ? "" : this.insp_rank_1);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_2.getKey(), 
              this.insp_contents_2 == null ? "" : this.insp_contents_2);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_2.getKey(), 
              this.insp_rank_2 == null ? "" : this.insp_rank_2);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_3.getKey(), 
              this.insp_contents_3 == null ? "" : this.insp_contents_3);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_3.getKey(), 
              this.insp_rank_3 == null ? "" : this.insp_rank_3);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_4.getKey(), 
              this.insp_contents_4 == null ? "" : this.insp_contents_4);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_4.getKey(), 
              this.insp_rank_4 == null ? "" : this.insp_rank_4);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_5.getKey(), 
              this.insp_contents_5 == null ? "" : this.insp_contents_5);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_5.getKey(), 
              this.insp_rank_5 == null ? "" : this.insp_rank_5);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_6.getKey(), 
              this.insp_contents_6 == null ? "" : this.insp_contents_6);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_6.getKey(), 
              this.insp_rank_6 == null ? "" : this.insp_rank_6);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_7.getKey(), 
              this.insp_contents_7 == null ? "" : this.insp_contents_7);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_7.getKey(), 
              this.insp_rank_7 == null ? "" : this.insp_rank_7);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_8.getKey(), 
              this.insp_contents_8 == null ? "" : this.insp_contents_8);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_8.getKey(), 
              this.insp_rank_8 == null ? "" : this.insp_rank_8);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_9.getKey(), 
              this.insp_contents_9 == null ? "" : this.insp_contents_9);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_9.getKey(), 
              this.insp_rank_9 == null ? "" : this.insp_rank_9);
              
      map.put(Equip_Inspect_Enum.Insp_Contents_10.getKey(), 
              this.insp_contents_10 == null ? "" : this.insp_contents_10);
              
      map.put(Equip_Inspect_Enum.Insp_Rank_10.getKey(), 
              this.insp_rank_10 == null ? "" : this.insp_rank_10);
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_History:makeMap]");
    }

    return map;
  }
}