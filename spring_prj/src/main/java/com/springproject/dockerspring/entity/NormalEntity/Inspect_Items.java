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
import com.springproject.dockerspring.commonenum.Inspect_Items_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Inspect_Items_History;
import com.springproject.dockerspring.form.CsvImplForm.InspectItemsForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Inspect_Items]
/*   テーブル「Inspect_Items」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Inspect_Items")
public class Inspect_Items implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
  private Integer serial_num;
  
  private String inspsheet_id;
  private String inspsheet_name;
  private Date inspsheet_date;
  private String insp_item_contents_1;
  private String insp_item_kinds_1;
  private String insp_item_unit_1;
  private String insp_item_contents_2;
  private String insp_item_kinds_2;
  private String insp_item_unit_2;
  private String insp_item_contents_3;
  private String insp_item_kinds_3;
  private String insp_item_unit_3;
  private String insp_item_contents_4;
  private String insp_item_kinds_4;
  private String insp_item_unit_4;
  private String insp_item_contents_5;
  private String insp_item_kinds_5;
  private String insp_item_unit_5;
  private String insp_item_contents_6;
  private String insp_item_kinds_6;
  private String insp_item_unit_6;
  private String insp_item_contents_7;
  private String insp_item_kinds_7;
  private String insp_item_unit_7;
  private String insp_item_contents_8;
  private String insp_item_kinds_8;
  private String insp_item_unit_8;
  private String insp_item_contents_9;
  private String insp_item_kinds_9;
  private String insp_item_unit_9;
  private String insp_item_contents_10;
  private String insp_item_kinds_10;
  private String insp_item_unit_10;






  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Inspect_Items(InspectItemsForm form){

    this.serial_num = form.getSerial_num();
    this.inspsheet_id = form.getInspsheet_id();
    this.inspsheet_name = form.getInspsheet_name();
    this.inspsheet_date = form.getInspsheet_date();
    this.insp_item_contents_1 = form.getInsp_item_contents_1();
    this.insp_item_kinds_1 = form.getInsp_item_kinds_1();
    this.insp_item_unit_1 = form.getInsp_item_unit_1();
    this.insp_item_contents_2 = form.getInsp_item_contents_2();
    this.insp_item_kinds_2 = form.getInsp_item_kinds_2();
    this.insp_item_unit_2 = form.getInsp_item_unit_2();
    this.insp_item_contents_3 = form.getInsp_item_contents_3();
    this.insp_item_kinds_3 = form.getInsp_item_kinds_3();
    this.insp_item_unit_3 = form.getInsp_item_unit_3();
    this.insp_item_contents_4 = form.getInsp_item_contents_4();
    this.insp_item_kinds_4 = form.getInsp_item_kinds_4();
    this.insp_item_unit_4 = form.getInsp_item_unit_4();
    this.insp_item_contents_5 = form.getInsp_item_contents_5();
    this.insp_item_kinds_5 = form.getInsp_item_kinds_5();
    this.insp_item_unit_5 = form.getInsp_item_unit_5();
    this.insp_item_contents_6 = form.getInsp_item_contents_6();
    this.insp_item_kinds_6 = form.getInsp_item_kinds_6();
    this.insp_item_unit_6 = form.getInsp_item_unit_6();
    this.insp_item_contents_7 = form.getInsp_item_contents_7();
    this.insp_item_kinds_7 = form.getInsp_item_kinds_7();
    this.insp_item_unit_7 = form.getInsp_item_unit_7();
    this.insp_item_contents_8 = form.getInsp_item_contents_8();
    this.insp_item_kinds_8 = form.getInsp_item_kinds_8();
    this.insp_item_unit_8 = form.getInsp_item_unit_8();
    this.insp_item_contents_9 = form.getInsp_item_contents_9();
    this.insp_item_kinds_9 = form.getInsp_item_kinds_9();
    this.insp_item_unit_9 = form.getInsp_item_unit_9();
    this.insp_item_contents_10 = form.getInsp_item_contents_10();
    this.insp_item_kinds_10 = form.getInsp_item_kinds_10();
    this.insp_item_unit_10 = form.getInsp_item_unit_10();

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Inspect_Items(Inspect_Items_History hist){

    this.serial_num = hist.getSerial_num();
    this.inspsheet_id = hist.getInspsheet_id();
    this.inspsheet_name = hist.getInspsheet_name();
    this.inspsheet_date = hist.getInspsheet_date();
    this.insp_item_contents_1 = hist.getInsp_item_contents_1();
    this.insp_item_kinds_1 = hist.getInsp_item_kinds_1();
    this.insp_item_unit_1 = hist.getInsp_item_unit_1();
    this.insp_item_contents_2 = hist.getInsp_item_contents_2();
    this.insp_item_kinds_2 = hist.getInsp_item_kinds_2();
    this.insp_item_unit_2 = hist.getInsp_item_unit_2();
    this.insp_item_contents_3 = hist.getInsp_item_contents_3();
    this.insp_item_kinds_3 = hist.getInsp_item_kinds_3();
    this.insp_item_unit_3 = hist.getInsp_item_unit_3();
    this.insp_item_contents_4 = hist.getInsp_item_contents_4();
    this.insp_item_kinds_4 = hist.getInsp_item_kinds_4();
    this.insp_item_unit_4 = hist.getInsp_item_unit_4();
    this.insp_item_contents_5 = hist.getInsp_item_contents_5();
    this.insp_item_kinds_5 = hist.getInsp_item_kinds_5();
    this.insp_item_unit_5 = hist.getInsp_item_unit_5();
    this.insp_item_contents_6 = hist.getInsp_item_contents_6();
    this.insp_item_kinds_6 = hist.getInsp_item_kinds_6();
    this.insp_item_unit_6 = hist.getInsp_item_unit_6();
    this.insp_item_contents_7 = hist.getInsp_item_contents_7();
    this.insp_item_kinds_7 = hist.getInsp_item_kinds_7();
    this.insp_item_unit_7 = hist.getInsp_item_unit_7();
    this.insp_item_contents_8 = hist.getInsp_item_contents_8();
    this.insp_item_kinds_8 = hist.getInsp_item_kinds_8();
    this.insp_item_unit_8 = hist.getInsp_item_unit_8();
    this.insp_item_contents_9 = hist.getInsp_item_contents_9();
    this.insp_item_kinds_9 = hist.getInsp_item_kinds_9();
    this.insp_item_unit_9 = hist.getInsp_item_unit_9();
    this.insp_item_contents_10 = hist.getInsp_item_contents_10();
    this.insp_item_kinds_10 = hist.getInsp_item_kinds_10();
    this.insp_item_unit_10 = hist.getInsp_item_unit_10();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Inspect_Items(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
      this.serial_num = null;
      this.inspsheet_id = map.get(Inspect_Items_Enum.Inspsheet_Id.getKey());
      this.inspsheet_name = map.get(Inspect_Items_Enum.Inspsheet_Name.getKey());

      String tmp = map.get(Inspect_Items_Enum.Inspsheet_Date.getKey());
      this.inspsheet_date = tmp == null ? null : date.parse(tmp);
      
      this.insp_item_contents_1 = map.get(Inspect_Items_Enum.Insp_Item_Contents_1.getKey());
      this.insp_item_kinds_1 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_1.getKey());
      this.insp_item_unit_1 = map.get(Inspect_Items_Enum.Insp_Item_Unit_1.getKey());
      this.insp_item_contents_2 = map.get(Inspect_Items_Enum.Insp_Item_Contents_2.getKey());
      this.insp_item_kinds_2 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_2.getKey());
      this.insp_item_unit_2 = map.get(Inspect_Items_Enum.Insp_Item_Unit_2.getKey());
      this.insp_item_contents_3 = map.get(Inspect_Items_Enum.Insp_Item_Contents_3.getKey());
      this.insp_item_kinds_3 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_3.getKey());
      this.insp_item_unit_3 = map.get(Inspect_Items_Enum.Insp_Item_Unit_3.getKey());
      this.insp_item_contents_4 = map.get(Inspect_Items_Enum.Insp_Item_Contents_4.getKey());
      this.insp_item_kinds_4 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_4.getKey());
      this.insp_item_unit_4 = map.get(Inspect_Items_Enum.Insp_Item_Unit_4.getKey());
      this.insp_item_contents_5 = map.get(Inspect_Items_Enum.Insp_Item_Contents_5.getKey());
      this.insp_item_kinds_5 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_5.getKey());
      this.insp_item_unit_5 = map.get(Inspect_Items_Enum.Insp_Item_Unit_5.getKey());
      this.insp_item_contents_6 = map.get(Inspect_Items_Enum.Insp_Item_Contents_6.getKey());
      this.insp_item_kinds_6 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_6.getKey());
      this.insp_item_unit_6 = map.get(Inspect_Items_Enum.Insp_Item_Unit_6.getKey());
      this.insp_item_contents_7 = map.get(Inspect_Items_Enum.Insp_Item_Contents_7.getKey());
      this.insp_item_kinds_7 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_7.getKey());
      this.insp_item_unit_7 = map.get(Inspect_Items_Enum.Insp_Item_Unit_7.getKey());
      this.insp_item_contents_8 = map.get(Inspect_Items_Enum.Insp_Item_Contents_8.getKey());
      this.insp_item_kinds_8 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_8.getKey());
      this.insp_item_unit_8 = map.get(Inspect_Items_Enum.Insp_Item_Unit_8.getKey());
      this.insp_item_contents_9 = map.get(Inspect_Items_Enum.Insp_Item_Contents_9.getKey());
      this.insp_item_kinds_9 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_9.getKey());
      this.insp_item_unit_9 = map.get(Inspect_Items_Enum.Insp_Item_Unit_9.getKey());
      this.insp_item_contents_10 = map.get(Inspect_Items_Enum.Insp_Item_Contents_10.getKey());
      this.insp_item_kinds_10 = map.get(Inspect_Items_Enum.Insp_Item_Kinds_10.getKey());
      this.insp_item_unit_10 = map.get(Inspect_Items_Enum.Insp_Item_Unit_10.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Inspect_Items:constructor3]", e.getErrorOffset());
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
      if(this.inspsheet_id == null || this.inspsheet_id.isEmpty() || this.inspsheet_id.isBlank()){
        this.inspsheet_id = null;
      }

      if(this.inspsheet_name == null || this.inspsheet_name.isEmpty() || this.inspsheet_name.isBlank()){
        this.inspsheet_name = null;
      }

      if(this.insp_item_contents_1 == null || this.insp_item_contents_1.isEmpty() || this.insp_item_contents_1.isBlank()){
        this.insp_item_contents_1 = null;
      }

      if(this.insp_item_kinds_1 == null || this.insp_item_kinds_1.isEmpty() || this.insp_item_kinds_1.isBlank()){
        this.insp_item_kinds_1 = null;
      }

      if(this.insp_item_unit_1 == null || this.insp_item_unit_1.isEmpty() || this.insp_item_unit_1.isBlank()){
        this.insp_item_unit_1 = null;
      }

      if(this.insp_item_contents_2 == null || this.insp_item_contents_2.isEmpty() || this.insp_item_contents_2.isBlank()){
        this.insp_item_contents_2 = null;
      }

      if(this.insp_item_kinds_2 == null || this.insp_item_kinds_2.isEmpty() || this.insp_item_kinds_2.isBlank()){
        this.insp_item_kinds_2 = null;
      }

      if(this.insp_item_unit_2 == null || this.insp_item_unit_2.isEmpty() || this.insp_item_unit_2.isBlank()){
        this.insp_item_unit_2 = null;
      }

      if(this.insp_item_contents_3 == null || this.insp_item_contents_3.isEmpty() || this.insp_item_contents_3.isBlank()){
        this.insp_item_contents_3 = null;
      }

      if(this.insp_item_kinds_3 == null || this.insp_item_kinds_3.isEmpty() || this.insp_item_kinds_3.isBlank()){
        this.insp_item_kinds_3 = null;
      }

      if(this.insp_item_unit_3 == null || this.insp_item_unit_3.isEmpty() || this.insp_item_unit_3.isBlank()){
        this.insp_item_unit_3 = null;
      }

      if(this.insp_item_contents_4 == null || this.insp_item_contents_4.isEmpty() || this.insp_item_contents_4.isBlank()){
        this.insp_item_contents_4 = null;
      }

      if(this.insp_item_kinds_4 == null || this.insp_item_kinds_4.isEmpty() || this.insp_item_kinds_4.isBlank()){
        this.insp_item_kinds_4 = null;
      }

      if(this.insp_item_unit_4 == null || this.insp_item_unit_4.isEmpty() || this.insp_item_unit_4.isBlank()){
        this.insp_item_unit_4 = null;
      }

      if(this.insp_item_contents_5 == null || this.insp_item_contents_5.isEmpty() || this.insp_item_contents_5.isBlank()){
        this.insp_item_contents_5 = null;
      }

      if(this.insp_item_kinds_5 == null || this.insp_item_kinds_5.isEmpty() || this.insp_item_kinds_5.isBlank()){
        this.insp_item_kinds_5 = null;
      }

      if(this.insp_item_unit_5 == null || this.insp_item_unit_5.isEmpty() || this.insp_item_unit_5.isBlank()){
        this.insp_item_unit_5 = null;
      }

      if(this.insp_item_contents_6 == null || this.insp_item_contents_6.isEmpty() || this.insp_item_contents_6.isBlank()){
        this.insp_item_contents_6 = null;
      }

      if(this.insp_item_kinds_6 == null || this.insp_item_kinds_6.isEmpty() || this.insp_item_kinds_6.isBlank()){
        this.insp_item_kinds_6 = null;
      }

      if(this.insp_item_unit_6 == null || this.insp_item_unit_6.isEmpty() || this.insp_item_unit_6.isBlank()){
        this.insp_item_unit_6 = null;
      }

      if(this.insp_item_contents_7 == null || this.insp_item_contents_7.isEmpty() || this.insp_item_contents_7.isBlank()){
        this.insp_item_contents_7 = null;
      }

      if(this.insp_item_kinds_7 == null || this.insp_item_kinds_7.isEmpty() || this.insp_item_kinds_7.isBlank()){
        this.insp_item_kinds_7 = null;
      }

      if(this.insp_item_unit_7 == null || this.insp_item_unit_7.isEmpty() || this.insp_item_unit_7.isBlank()){
        this.insp_item_unit_7 = null;
      }

      if(this.insp_item_contents_8 == null || this.insp_item_contents_8.isEmpty() || this.insp_item_contents_8.isBlank()){
        this.insp_item_contents_8 = null;
      }

      if(this.insp_item_kinds_8 == null || this.insp_item_kinds_8.isEmpty() || this.insp_item_kinds_8.isBlank()){
        this.insp_item_kinds_8 = null;
      }

      if(this.insp_item_unit_8 == null || this.insp_item_unit_8.isEmpty() || this.insp_item_unit_8.isBlank()){
        this.insp_item_unit_8 = null;
      }

      if(this.insp_item_contents_9 == null || this.insp_item_contents_9.isEmpty() || this.insp_item_contents_9.isBlank()){
        this.insp_item_contents_9 = null;
      }

      if(this.insp_item_kinds_9 == null || this.insp_item_kinds_9.isEmpty() || this.insp_item_kinds_9.isBlank()){
        this.insp_item_kinds_9 = null;
      }

      if(this.insp_item_unit_9 == null || this.insp_item_unit_9.isEmpty() || this.insp_item_unit_9.isBlank()){
        this.insp_item_unit_9 = null;
      }

      if(this.insp_item_contents_10 == null || this.insp_item_contents_10.isEmpty() || this.insp_item_contents_10.isBlank()){
        this.insp_item_contents_10 = null;
      }

      if(this.insp_item_kinds_10 == null || this.insp_item_kinds_10.isEmpty() || this.insp_item_kinds_10.isBlank()){
        this.insp_item_kinds_10 = null;
      }

      if(this.insp_item_unit_10 == null || this.insp_item_unit_10.isEmpty() || this.insp_item_unit_10.isBlank()){
        this.insp_item_unit_10 = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Inspect_Items:stringSetNull]");
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
      map.put(Inspect_Items_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Inspect_Items_Enum.Inspsheet_Id.getKey(), 
              this.inspsheet_id == null ? "" : this.inspsheet_id);
              
      map.put(Inspect_Items_Enum.Inspsheet_Name.getKey(), 
              this.inspsheet_name == null ? "" : this.inspsheet_name);
              
      map.put(Inspect_Items_Enum.Inspsheet_Date.getKey(), 
              this.inspsheet_date == null ? "" : date.format(this.inspsheet_date));
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_1.getKey(), 
              this.insp_item_contents_1 == null ? "" : this.insp_item_contents_1);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_1.getKey(), 
              this.insp_item_kinds_1 == null ? "" : this.insp_item_kinds_1);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_1.getKey(), 
              this.insp_item_unit_1 == null ? "" : this.insp_item_unit_1);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_2.getKey(), 
              this.insp_item_contents_2 == null ? "" : this.insp_item_contents_2);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_2.getKey(), 
              this.insp_item_kinds_2 == null ? "" : this.insp_item_kinds_2);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_2.getKey(), 
              this.insp_item_unit_2 == null ? "" : this.insp_item_unit_2);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_3.getKey(), 
              this.insp_item_contents_3 == null ? "" : this.insp_item_contents_3);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_3.getKey(), 
              this.insp_item_kinds_3 == null ? "" : this.insp_item_kinds_3);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_3.getKey(), 
              this.insp_item_unit_3 == null ? "" : this.insp_item_unit_3);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_4.getKey(), 
              this.insp_item_contents_4 == null ? "" : this.insp_item_contents_4);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_4.getKey(), 
              this.insp_item_kinds_4 == null ? "" : this.insp_item_kinds_4);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_4.getKey(), 
              this.insp_item_unit_4 == null ? "" : this.insp_item_unit_4);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_5.getKey(), 
              this.insp_item_contents_5 == null ? "" : this.insp_item_contents_5);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_5.getKey(), 
              this.insp_item_kinds_5 == null ? "" : this.insp_item_kinds_5);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_5.getKey(), 
              this.insp_item_unit_5 == null ? "" : this.insp_item_unit_5);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_6.getKey(), 
              this.insp_item_contents_6 == null ? "" : this.insp_item_contents_6);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_6.getKey(), 
              this.insp_item_kinds_6 == null ? "" : this.insp_item_kinds_6);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_6.getKey(), 
              this.insp_item_unit_6 == null ? "" : this.insp_item_unit_6);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_7.getKey(), 
              this.insp_item_contents_7 == null ? "" : this.insp_item_contents_7);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_7.getKey(), 
              this.insp_item_kinds_7 == null ? "" : this.insp_item_kinds_7);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_7.getKey(), 
              this.insp_item_unit_7 == null ? "" : this.insp_item_unit_7);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_8.getKey(), 
              this.insp_item_contents_8 == null ? "" : this.insp_item_contents_8);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_8.getKey(), 
              this.insp_item_kinds_8 == null ? "" : this.insp_item_kinds_8);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_8.getKey(), 
              this.insp_item_unit_8 == null ? "" : this.insp_item_unit_8);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_9.getKey(), 
              this.insp_item_contents_9 == null ? "" : this.insp_item_contents_9);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_9.getKey(), 
              this.insp_item_kinds_9 == null ? "" : this.insp_item_kinds_9);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_9.getKey(), 
              this.insp_item_unit_9 == null ? "" : this.insp_item_unit_9);
              
      map.put(Inspect_Items_Enum.Insp_Item_Contents_10.getKey(), 
              this.insp_item_contents_10 == null ? "" : this.insp_item_contents_10);
              
      map.put(Inspect_Items_Enum.Insp_Item_Kinds_10.getKey(), 
              this.insp_item_kinds_10 == null ? "" : this.insp_item_kinds_10);
              
      map.put(Inspect_Items_Enum.Insp_Item_Unit_10.getKey(), 
              this.insp_item_unit_10 == null ? "" : this.insp_item_unit_10);
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Inspect_Items:makeMap]");
    }

    return map;
  }
}