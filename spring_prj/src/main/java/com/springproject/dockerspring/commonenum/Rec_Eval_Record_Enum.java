package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [Rec_Eval_Record_Enum]
/*   主に「Rec_Eval_Record」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Rec_Eval_Record_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Eval_Id("採用考課番号", "eval_id"),
  Data_Kinds("データ種別", "data_kinds"),
  Record_Name("データ名", "record_name"),
  Record_Data("保存データ", "record_data");

  private final String text;
  private final String key;

  private Rec_Eval_Record_Enum(String text, String key){
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
