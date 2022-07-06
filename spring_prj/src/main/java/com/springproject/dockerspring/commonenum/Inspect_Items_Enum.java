package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Inspect_Items_Enum]
/*   主に「Inspect_Items」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Inspect_Items_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Inspsheet_Id("点検シート番号", "inspsheet_id"),
  Inspsheet_Name("点検シート名", "inspsheet_name"),
  Inspsheet_Date("点検シート作成日", "inspsheet_date"),
  Insp_Item_Contents_1("点検項目内容１", "insp_item_contents_1"),
  Insp_Item_Kinds_1("点検種別１", "insp_item_kinds_1"),
  Insp_Item_Unit_1("点検単位１", "insp_item_unit_1"),
  Insp_Item_Contents_2("点検項目内容２", "insp_item_contents_2"),
  Insp_Item_Kinds_2("点検種別２", "insp_item_kinds_2"),
  Insp_Item_Unit_2("点検単位２", "insp_item_unit_2"),
  Insp_Item_Contents_3("点検項目内容３", "insp_item_contents_3"),
  Insp_Item_Kinds_3("点検種別３", "insp_item_kinds_3"),
  Insp_Item_Unit_3("点検単位３", "insp_item_unit_3"),
  Insp_Item_Contents_4("点検項目内容４", "insp_item_contents_4"),
  Insp_Item_Kinds_4("点検種別４", "insp_item_kinds_4"),
  Insp_Item_Unit_4("点検単位４", "insp_item_unit_4"),
  Insp_Item_Contents_5("点検項目内容５", "insp_item_contents_5"),
  Insp_Item_Kinds_5("点検種別５", "insp_item_kinds_5"),
  Insp_Item_Unit_5("点検単位５", "insp_item_unit_5"),
  Insp_Item_Contents_6("点検項目内容６", "insp_item_contents_6"),
  Insp_Item_Kinds_6("点検種別６", "insp_item_kinds_6"),
  Insp_Item_Unit_6("点検単位６", "insp_item_unit_6"),
  Insp_Item_Contents_7("点検項目内容７", "insp_item_contents_7"),
  Insp_Item_Kinds_7("点検種別７", "insp_item_kinds_7"),
  Insp_Item_Unit_7("点検単位７", "insp_item_unit_7"),
  Insp_Item_Contents_8("点検項目内容８", "insp_item_contents_8"),
  Insp_Item_Kinds_8("点検種別８", "insp_item_kinds_8"),
  Insp_Item_Unit_8("点検単位８", "insp_item_unit_8"),
  Insp_Item_Contents_9("点検項目内容９", "insp_item_contents_9"),
  Insp_Item_Kinds_9("点検種別９", "insp_item_kinds_9"),
  Insp_Item_Unit_9("点検単位９", "insp_item_unit_9"),
  Insp_Item_Contents_10("点検項目内容１０", "insp_item_contents_10"),
  Insp_Item_Kinds_10("点検種別１０", "insp_item_kinds_10"),
  Insp_Item_Unit_10("点検単位１０", "insp_item_unit_10");

  private final String text;
  private final String key;

  private Inspect_Items_Enum(String text, String key){
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
