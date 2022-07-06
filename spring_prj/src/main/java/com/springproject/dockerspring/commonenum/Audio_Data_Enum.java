package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Audio_Data_Enum]
/*   主に「Audio_Data」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Audio_Data_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Sound_Id("音源番号", "sound_id"),
  Sound_Name("音源名", "sound_name"),
  Audio_Data("音源データ", "audio_data");

  private final String text;
  private final String key;

  private Audio_Data_Enum(String text, String key){
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
