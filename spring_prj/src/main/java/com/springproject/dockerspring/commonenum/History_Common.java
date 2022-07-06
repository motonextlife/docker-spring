package com.springproject.dockerspring.commonenum;


/********************************************************************/
/*   [History_Common]
/*   主に履歴関連の機能全般で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum History_Common {
  History_Id("履歴番号", "history_id"),
  Change_Datetime("履歴記録日時", "change_datetime"),
  Change_Kinds("履歴種別", "change_kinds"),
  Operation_User("操作ユーザー名", "operation_user");

  private final String text;
  private final String key;

  private History_Common(String text, String key){
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
