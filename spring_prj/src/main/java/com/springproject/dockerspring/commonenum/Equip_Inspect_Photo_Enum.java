package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [Equip_Inspect_Photo_Enum]
/*   主に「Equip_Inspect_Photo」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Equip_Inspect_Photo_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Inspect_Id("点検番号", "inspect_id"),
  Photo_Name("写真名", "photo_name"),
  Photo_Data("写真データ", "photo_data");

  private final String text;
  private final String key;

  private Equip_Inspect_Photo_Enum(String text, String key){
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
