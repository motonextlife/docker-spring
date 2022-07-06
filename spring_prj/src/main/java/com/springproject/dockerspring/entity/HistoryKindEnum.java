package com.springproject.dockerspring.entity;


/**************************************************************/
/*   [HistoryKindEnum]
/*   履歴保存時の種別を登録する際に使用する列挙型である。
/**************************************************************/
public enum HistoryKindEnum{
  UPDATE("update"),
  DELETE("delete"),
  ROLLBACK("rollback");

  private final String kind;

  private HistoryKindEnum(String kind){
    this.kind = kind;
  }

  public String getKind() {
    return this.kind;
  }
}