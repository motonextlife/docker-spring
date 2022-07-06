package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Equip_Inspect_Enum]
/*   主に「Equip_Inspect」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Equip_Inspect_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Inspect_Id("点検番号", "inspect_id"),
  Faci_Id("設備番号", "faci_id"),
  Inspect_Name("点検名", "inspect_name"),
  Inspect_Date("点検日", "inspect_date"),
  Inspsheet_Id("点検シート番号", "inspsheet_id"),
  Insp_Contents_1("点検内容１", "insp_contents_1"),
  Insp_Rank_1("点検結果１", "insp_rank_1"),
  Insp_Contents_2("点検内容２", "insp_contents_2"),
  Insp_Rank_2("点検結果２", "insp_rank_2"),
  Insp_Contents_3("点検内容３", "insp_contents_3"),
  Insp_Rank_3("点検結果３", "insp_rank_3"),
  Insp_Contents_4("点検内容４", "insp_contents_4"),
  Insp_Rank_4("点検結果４", "insp_rank_4"),
  Insp_Contents_5("点検内容５", "insp_contents_5"),
  Insp_Rank_5("点検結果５", "insp_rank_5"),
  Insp_Contents_6("点検内容６", "insp_contents_6"),
  Insp_Rank_6("点検結果６", "insp_rank_6"),
  Insp_Contents_7("点検内容７", "insp_contents_7"),
  Insp_Rank_7("点検結果７", "insp_rank_7"),
  Insp_Contents_8("点検内容８", "insp_contents_8"),
  Insp_Rank_8("点検結果８", "insp_rank_8"),
  Insp_Contents_9("点検内容９", "insp_contents_9"),
  Insp_Rank_9("点検結果９", "insp_rank_9"),
  Insp_Contents_10("点検内容１０", "insp_contents_10"),
  Insp_Rank_10("点検結果１０", "insp_rank_10");

  private final String text;
  private final String key;

  private Equip_Inspect_Enum(String text, String key){
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
