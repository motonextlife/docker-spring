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
import com.springproject.dockerspring.commonenum.Rec_Eval_Items_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Items_History;
import com.springproject.dockerspring.form.CsvImplForm.RecEvalItemsForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





/**************************************************************/
/*   [Rec_Eval_Items]
/*   テーブル「Rec_Eval_Items」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Rec_Eval_Items")
public class Rec_Eval_Items implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
  private Integer serial_num;

  private String evalsheet_id;
  private String evalsheet_name;
  private Date evalsheet_date;
  private String evalsheet_kinds;
  private String eval_item_contents_1;
  private String eval_item_kinds_1;
  private String eval_item_contents_2;
  private String eval_item_kinds_2;
  private String eval_item_contents_3;
  private String eval_item_kinds_3;
  private String eval_item_contents_4;
  private String eval_item_kinds_4;
  private String eval_item_contents_5;
  private String eval_item_kinds_5;
  private String eval_item_contents_6;
  private String eval_item_kinds_6;
  private String eval_item_contents_7;
  private String eval_item_kinds_7;
  private String eval_item_contents_8;
  private String eval_item_kinds_8;
  private String eval_item_contents_9;
  private String eval_item_kinds_9;
  private String eval_item_contents_10;
  private String eval_item_kinds_10;






  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Rec_Eval_Items(RecEvalItemsForm form){

    this.serial_num = form.getSerial_num();
    this.evalsheet_id = form.getEvalsheet_id();
    this.evalsheet_name = form.getEvalsheet_name();
    this.evalsheet_date = form.getEvalsheet_date();
    this.evalsheet_kinds = form.getEvalsheet_kinds();
    this.eval_item_contents_1 = form.getEval_item_contents_1();
    this.eval_item_kinds_1 = form.getEval_item_kinds_1();
    this.eval_item_contents_2 = form.getEval_item_contents_2();
    this.eval_item_kinds_2 = form.getEval_item_kinds_2();
    this.eval_item_contents_3 = form.getEval_item_contents_3();
    this.eval_item_kinds_3 = form.getEval_item_kinds_3();
    this.eval_item_contents_4 = form.getEval_item_contents_4();
    this.eval_item_kinds_4 = form.getEval_item_kinds_4();
    this.eval_item_contents_5 = form.getEval_item_contents_5();
    this.eval_item_kinds_5 = form.getEval_item_kinds_5();
    this.eval_item_contents_6 = form.getEval_item_contents_6();
    this.eval_item_kinds_6 = form.getEval_item_kinds_6();
    this.eval_item_contents_7 = form.getEval_item_contents_7();
    this.eval_item_kinds_7 = form.getEval_item_kinds_7();
    this.eval_item_contents_8 = form.getEval_item_contents_8();
    this.eval_item_kinds_8 = form.getEval_item_kinds_8();
    this.eval_item_contents_9 = form.getEval_item_contents_9();
    this.eval_item_kinds_9 = form.getEval_item_kinds_9();
    this.eval_item_contents_10 = form.getEval_item_contents_10();
    this.eval_item_kinds_10 = form.getEval_item_kinds_10();

    stringSetNull();
  }








  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Rec_Eval_Items(Rec_Eval_Items_History hist){

    this.serial_num = hist.getSerial_num();
    this.evalsheet_id = hist.getEvalsheet_id();
    this.evalsheet_name = hist.getEvalsheet_name();
    this.evalsheet_date = hist.getEvalsheet_date();
    this.evalsheet_kinds = hist.getEvalsheet_kinds();
    this.eval_item_contents_1 = hist.getEval_item_contents_1();
    this.eval_item_kinds_1 = hist.getEval_item_kinds_1();
    this.eval_item_contents_2 = hist.getEval_item_contents_2();
    this.eval_item_kinds_2 = hist.getEval_item_kinds_2();
    this.eval_item_contents_3 = hist.getEval_item_contents_3();
    this.eval_item_kinds_3 = hist.getEval_item_kinds_3();
    this.eval_item_contents_4 = hist.getEval_item_contents_4();
    this.eval_item_kinds_4 = hist.getEval_item_kinds_4();
    this.eval_item_contents_5 = hist.getEval_item_contents_5();
    this.eval_item_kinds_5 = hist.getEval_item_kinds_5();
    this.eval_item_contents_6 = hist.getEval_item_contents_6();
    this.eval_item_kinds_6 = hist.getEval_item_kinds_6();
    this.eval_item_contents_7 = hist.getEval_item_contents_7();
    this.eval_item_kinds_7 = hist.getEval_item_kinds_7();
    this.eval_item_contents_8 = hist.getEval_item_contents_8();
    this.eval_item_kinds_8 = hist.getEval_item_kinds_8();
    this.eval_item_contents_9 = hist.getEval_item_contents_9();
    this.eval_item_kinds_9 = hist.getEval_item_kinds_9();
    this.eval_item_contents_10 = hist.getEval_item_contents_10();
    this.eval_item_kinds_10 = hist.getEval_item_kinds_10();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Rec_Eval_Items(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
        this.serial_num = null;
        this.evalsheet_id = map.get(Rec_Eval_Items_Enum.Evalsheet_Id.getKey());
        this.evalsheet_name = map.get(Rec_Eval_Items_Enum.Evalsheet_Name.getKey());

        String tmp = map.get(Rec_Eval_Items_Enum.Evalsheet_Date.getKey());
        this.evalsheet_date = tmp == null ? null : date.parse(tmp);

        this.evalsheet_kinds = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey());
        this.eval_item_contents_1 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_1.getKey());
        this.eval_item_kinds_1 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_2.getKey());
        this.eval_item_contents_2 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_2.getKey());
        this.eval_item_kinds_2 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_3.getKey());
        this.eval_item_contents_3 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_3.getKey());
        this.eval_item_kinds_3 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_4.getKey());
        this.eval_item_contents_4 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_4.getKey());
        this.eval_item_kinds_4 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_5.getKey());
        this.eval_item_contents_5 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_5.getKey());
        this.eval_item_kinds_5 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_6.getKey());
        this.eval_item_contents_6 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_6.getKey());
        this.eval_item_kinds_6 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_7.getKey());
        this.eval_item_contents_7 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_7.getKey());
        this.eval_item_kinds_7 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_8.getKey());
        this.eval_item_contents_8 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_8.getKey());
        this.eval_item_kinds_8 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_9.getKey());
        this.eval_item_contents_9 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_9.getKey());
        this.eval_item_kinds_9 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_10.getKey());
        this.eval_item_contents_10 = map.get(Rec_Eval_Items_Enum.Eval_Item_Contents_10.getKey());
        this.eval_item_kinds_10 = map.get(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Rec_Eval_Items:constructor3]", e.getErrorOffset());
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
      if(this.evalsheet_id == null || this.evalsheet_id.isEmpty() || this.evalsheet_id.isBlank()){
        this.evalsheet_id = null;
      }

      if(this.evalsheet_name == null || this.evalsheet_name.isEmpty() || this.evalsheet_name.isBlank()){
        this.evalsheet_name = null;
      }

      if(this.eval_item_contents_1 == null || this.eval_item_contents_1.isEmpty() || this.eval_item_contents_1.isBlank()){
        this.eval_item_contents_1 = null;
      }

      if(this.eval_item_contents_2 == null || this.eval_item_contents_2.isEmpty() || this.eval_item_contents_2.isBlank()){
        this.eval_item_contents_2 = null;
      }

      if(this.eval_item_contents_3 == null || this.eval_item_contents_3.isEmpty() || this.eval_item_contents_3.isBlank()){
        this.eval_item_contents_3 = null;
      }

      if(this.eval_item_contents_4 == null || this.eval_item_contents_4.isEmpty() || this.eval_item_contents_4.isBlank()){
        this.eval_item_contents_4 = null;
      }

      if(this.eval_item_contents_5 == null || this.eval_item_contents_5.isEmpty() || this.eval_item_contents_5.isBlank()){
        this.eval_item_contents_5 = null;
      }

      if(this.eval_item_contents_6 == null || this.eval_item_contents_6.isEmpty() || this.eval_item_contents_6.isBlank()){
        this.eval_item_contents_6 = null;
      }

      if(this.eval_item_contents_7 == null || this.eval_item_contents_7.isEmpty() || this.eval_item_contents_7.isBlank()){
        this.eval_item_contents_7 = null;
      }

      if(this.eval_item_contents_8 == null || this.eval_item_contents_8.isEmpty() || this.eval_item_contents_8.isBlank()){
        this.eval_item_contents_8 = null;
      }

      if(this.eval_item_contents_9 == null || this.eval_item_contents_9.isEmpty() || this.eval_item_contents_9.isBlank()){
        this.eval_item_contents_9 = null;
      }

      if(this.eval_item_contents_10 == null || this.eval_item_contents_10.isEmpty() || this.eval_item_contents_10.isBlank()){
        this.eval_item_contents_10 = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Items:stringSetNull]");
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
      map.put(Rec_Eval_Items_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Rec_Eval_Items_Enum.Evalsheet_Id.getKey(), 
              this.evalsheet_id == null ? "" : this.evalsheet_id);
              
      map.put(Rec_Eval_Items_Enum.Evalsheet_Name.getKey(), 
              this.evalsheet_name == null ? "" : this.evalsheet_name);
              
      map.put(Rec_Eval_Items_Enum.Evalsheet_Date.getKey(), 
              this.evalsheet_date == null ? "" : date.format(this.evalsheet_date));
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey(), 
              this.evalsheet_kinds == null ? "" : this.evalsheet_kinds);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_1.getKey(), 
              this.eval_item_contents_1 == null ? "" : this.eval_item_contents_1);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_2.getKey(), 
              this.eval_item_kinds_1 == null ? "" : this.eval_item_kinds_1);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_2.getKey(), 
              this.eval_item_contents_2 == null ? "" : this.eval_item_contents_2);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_3.getKey(), 
              this.eval_item_kinds_2 == null ? "" : this.eval_item_kinds_2);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_3.getKey(), 
              this.eval_item_contents_3 == null ? "" : this.eval_item_contents_3);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_4.getKey(), 
              this.eval_item_kinds_3 == null ? "" : this.eval_item_kinds_3);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_4.getKey(), 
              this.eval_item_contents_4 == null ? "" : this.eval_item_contents_4);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_5.getKey(), 
              this.eval_item_kinds_4 == null ? "" : this.eval_item_kinds_4);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_5.getKey(), 
              this.eval_item_contents_5 == null ? "" : this.eval_item_contents_5);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_6.getKey(), 
              this.eval_item_kinds_5 == null ? "" : this.eval_item_kinds_5);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_6.getKey(), 
              this.eval_item_contents_6 == null ? "" : this.eval_item_contents_6);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_7.getKey(), 
              this.eval_item_kinds_6 == null ? "" : this.eval_item_kinds_6);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_7.getKey(), 
              this.eval_item_contents_7 == null ? "" : this.eval_item_contents_7);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_8.getKey(), 
              this.eval_item_kinds_7 == null ? "" : this.eval_item_kinds_7);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_8.getKey(), 
              this.eval_item_contents_8 == null ? "" : this.eval_item_contents_8);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_9.getKey(), 
              this.eval_item_kinds_8 == null ? "" : this.eval_item_kinds_8);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_9.getKey(), 
              this.eval_item_contents_9 == null ? "" : this.eval_item_contents_9);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_10.getKey(), 
              this.eval_item_kinds_9 == null ? "" : this.eval_item_kinds_9);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Contents_10.getKey(), 
              this.eval_item_contents_10 == null ? "" : this.eval_item_contents_10);
              
      map.put(Rec_Eval_Items_Enum.Eval_Item_Kinds_1.getKey(), 
              this.eval_item_kinds_10 == null ? "" : this.eval_item_kinds_10);
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Items:makeMap]");
    }

    return map;
  }
}