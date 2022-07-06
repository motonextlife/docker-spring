package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Musical_Score_Enum]
/*   主に「Musical_Score」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Musical_Score_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Score_Id("楽譜番号", "score_id"),
  Buy_Date("購入日", "buy_date"),
  Song_Title("曲名", "song_title"),
  Composer("作曲者", "composer"),
  Arranger("編曲者", "arranger"),
  Publisher("出版社", "publisher"),
  Strage_Loc("保管場所", "strage_loc"),
  Disp_Date("廃棄日", "disp_date"),
  Other_Comment("その他コメント", "other_comment");

  private final String text;
  private final String key;

  private Musical_Score_Enum(String text, String key){
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
