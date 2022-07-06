package com.springproject.dockerspring.form;


/********************************************************/
/*   [RegexEnum]
/*   CSVデータバリデーションの際に用いる正規表現の
/*   列挙型である。
/********************************************************/
public enum RegexEnum {
  ALNUM_NOTNULL("^[a-zA-Z0-9]+$"),
  ALNUM_NULLOK("^[a-zA-Z0-9]*$"),
  ZENKAKU_ONLY_NOTNULL("^[^ -~｡-ﾟ]+$"),
  ZENKAKU_ONLY_NULLOK("^[^ -~｡-ﾟ]*$"),
  DATE_NO_HYPHEN_NOTNULL("^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$"),
  DATE_NO_HYPHEN_NULLOK("^([0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01]))*$"),
  E_MAILL("^([a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\\.)+[a-zA-Z]{2,})*$"),
  DIGIT("^[0-9]*$");

  private final String regex;

  private RegexEnum(String regex){
    this.regex = regex;
  }

  public String getRegex() {
    return this.regex;
  }
}
