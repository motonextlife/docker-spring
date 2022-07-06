package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Sound_Source_Enum]
/*   主に「Sound_Source」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Sound_Source_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Sound_Id("音源番号", "sound_id"),
  Upload_Date("登録日", "upload_date"),
  Song_Title("曲名", "song_title"),
  Composer("作曲者", "composer"),
  Performer("演奏者", "performer"),
  Publisher("出版社", "publisher"),
  Other_Comment("その他コメント", "other_comment");

  private final String text;
  private final String key;

  private Sound_Source_Enum(String text, String key){
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
