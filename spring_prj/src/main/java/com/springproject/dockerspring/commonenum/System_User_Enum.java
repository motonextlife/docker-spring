package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [System_User_Enum]
/*   主に「System_User」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum System_User_Enum {
  Member_Id("団員番号", "member_id"),
  Username("ユーザネーム", "username"),
  Password("パスワード", "password"),
  Auth_Id("権限番号", "auth_id"),
  Fail_Count("ログイン失敗回数", "fail_count"),
  Locking("ロック有無", "locking");

  private final String text;
  private final String key;

  private System_User_Enum(String text, String key){
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
