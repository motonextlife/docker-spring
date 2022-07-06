package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [Score_Pdf_Enum]
/*   主に「Score_Pdf」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Score_Pdf_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Score_Id("楽譜番号", "score_id"),
  Score_Name("楽譜名", "score_name"),
  Pdf_Data("ＰＤＦデータ", "pdf_data");

  private final String text;
  private final String key;

  private Score_Pdf_Enum(String text, String key){
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
