package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [DateFormat_Enum]
/*   主に日付変換の機能全般で共通に用いる列挙型である。
/********************************************************************/
public enum DateFormat_Enum {
  DATE("yyyy-MM-dd"),
  DATE_NO_HYPHEN("yyyyMMdd"),
  DATETIME("yyyy-MM-dd hh:mm:ss");

  private final String str;

  private DateFormat_Enum(String str){
    this.str = str;
  }

  public String getStr() {
    return this.str;
  }
}
