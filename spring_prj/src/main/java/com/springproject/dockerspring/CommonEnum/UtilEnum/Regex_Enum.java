/** 
 **************************************************************************************
 * @file Regex_Enum.java
 * @brief 主に[正規表現文字列]機能において使用する列挙型を格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonEnum.UtilEnum;




/** 
 **************************************************************************************
 * @brief 主に[正規表現文字列]機能において使用する列挙型である。
 * @details 
 * - 列挙型に定義する内容としては、「正規表現文字列」を定義する。
 * - バリデーションの際に保存文字列を検査する際に用いる。
 * @note
 * - 項目[EMAIL_SUPPORT]の使用方法としては、Eメール文字列の検査は既存ライブラリを用いるため、
 * そのライブラリの機能では補えない部分を補助する目的で追加する。
 **************************************************************************************
 */ 
public enum Regex_Enum {
  ALNUM_HYPHEN_NOTNULL("^[a-zA-Z0-9-]+$"),
  ALNUM_HYPHEN_NULLOK("^[a-zA-Z0-9-]*$"),
  ZENKAKU_ONLY_NOTNULL("^[^ -~｡-ﾟ\t\n]+$"),
  ZENKAKU_ONLY_NULLOK("^[^ -~｡-ﾟ\t\n]*$"),
  DATE_NO_HYPHEN_NOTNULL("^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$"),
  DATE_NO_HYPHEN_NULLOK("^([0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01]))*$"),
  EMAIL_SUPPORT("^[a-zA-Z0-9_+\\.@-]*$"),
  DIGIT_HYPHEN("^[0-9-]*$"),
  DIGIT("^[0-9]*$");


  //! 正規表現文字列の格納変数。
  private final String regex; 


  /** 
   **********************************************************
   * @brief 列挙型の定義値をセットする。
   * @param[in] regex 正規表現文字列をセットする。
   **********************************************************
   */ 
  private Regex_Enum(String regex){
    this.regex = regex;
  }


  /** 
   **********************************************************
   * @brief 各列挙型の定義値を取得する。
   **********************************************************
   */ 
  public String getRegex() {
    return this.regex;
  }
}
