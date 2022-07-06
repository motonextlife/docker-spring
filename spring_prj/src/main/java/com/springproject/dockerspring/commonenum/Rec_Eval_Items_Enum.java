package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Rec_Eval_Items_Enum]
/*   主に「Rec_Eval_Items」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Rec_Eval_Items_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Evalsheet_Id("採用考課シート番号", "evalsheet_id"),
  Evalsheet_Name("採用考課シート名", "evalsheet_name"),
  Evalsheet_Date("採用考課シート作成日", "evalsheet_date"),
  Evalsheet_Kinds("採用考課シート種別", "evalsheet_kinds"),
  Eval_Item_Contents_1("評価項目内容１", "eval_item_contents_1"),
  Eval_Item_Kinds_1("評価方法１", "eval_item_kinds_1"),
  Eval_Item_Contents_2("評価項目内容２", "eval_item_contents_2"),
  Eval_Item_Kinds_2("評価方法２", "eval_item_kinds_2"),
  Eval_Item_Contents_3("評価項目内容３", "eval_item_contents_3"),
  Eval_Item_Kinds_3("評価方法３", "eval_item_kinds_3"),
  Eval_Item_Contents_4("評価項目内容４", "eval_item_contents_4"),
  Eval_Item_Kinds_4("評価方法４", "eval_item_kinds_4"),
  Eval_Item_Contents_5("評価項目内容５", "eval_item_contents_5"),
  Eval_Item_Kinds_5("評価方法５", "eval_item_kinds_5"),
  Eval_Item_Contents_6("評価項目内容６", "eval_item_contents_6"),
  Eval_Item_Kinds_6("評価方法６", "eval_item_kinds_6"),
  Eval_Item_Contents_7("評価項目内容７", "eval_item_contents_7"),
  Eval_Item_Kinds_7("評価方法７", "eval_item_kinds_7"),
  Eval_Item_Contents_8("評価項目内容８", "eval_item_contents_8"),
  Eval_Item_Kinds_8("評価方法８", "eval_item_kinds_8"),
  Eval_Item_Contents_9("評価項目内容９", "eval_item_contents_9"),
  Eval_Item_Kinds_9("評価方法９", "eval_item_kinds_9"),
  Eval_Item_Contents_10("評価項目内容１０", "eval_item_contents_10"),
  Eval_Item_Kinds_10("評価方法１０", "eval_item_kinds_10");

  private final String text;
  private final String key;

  private Rec_Eval_Items_Enum(String text, String key){
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