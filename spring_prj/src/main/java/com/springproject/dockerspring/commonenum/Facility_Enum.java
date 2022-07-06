package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Facility_Enum]
/*   主に「Facility」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Facility_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Faci_Id("設備番号", "faci_id"),
  Faci_Name("設備名", "faci_name"),
  Chief_Admin("管理責任者団員番号", "chief_admin"),
  Resp_Person("管理担当者団員番号", "resp_person"),
  Buy_Date("購入日", "buy_date"),
  Storage_Loc("保管場所", "storage_loc"),
  Buy_Price("購入金額", "buy_price"),
  Disp_Date("廃棄日", "disp_date"),
  Other_Comment("その他コメント", "other_comment");

  private final String text;
  private final String key;

  private Facility_Enum(String text, String key){
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
