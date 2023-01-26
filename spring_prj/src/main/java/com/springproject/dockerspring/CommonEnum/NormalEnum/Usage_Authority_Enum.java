/** 
 **************************************************************************************
 * @file Usage_Authority_Enum.java
 * @brief 主に[権限情報]機能において使用する列挙型を格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonEnum.NormalEnum;




/** 
 **************************************************************************************
 * @brief 主に[権限情報]機能において使用する列挙型である。
 * @details 
 * - 列挙型に定義する内容としては、「日本語でのデータ項目名」「英語でのデータ項目名」を定義する。
 * - 日本語項目名はCSVファイルの出力の際のヘッダー名に使用する。
 * - 英語項目名に関してはマップリストのキー名の定義に用いる。
 **************************************************************************************
 */ 
public enum Usage_Authority_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Auth_Id("権限番号", "auth_id"),
  Auth_Name("権限名", "auth_name"),
  Admin("管理者権限", "admin"),
  Member_Info("団員管理権限", "member_info"),
  Facility("設備管理権限", "facility"),
  Musical_Score("楽譜管理権限", "musical_score"),
  Sound_Source("音源管理権限", "sound_source");


  //! 日本語項目名の格納変数
  private final String ja_name;

  //! 英語項目名の格納変数
  private final String en_name;


  /** 
   **********************************************************
   * @brief 列挙型の定義値をセットする。
   * @param[in] ja_name 項目の日本語名をセットする。
   * @param[in] en_name 項目の英語名をセットする。
   **********************************************************
   */ 
  private Usage_Authority_Enum(String ja_name, String en_name){
    this.ja_name = ja_name;
    this.en_name = en_name;
  }
 

  /** 
   **********************************************************
   * @brief 各列挙型項目の日本語項目名を取得する。
   **********************************************************
   */ 
  public String getJaName() { 
    return this.ja_name;
  }


  /** 
   **********************************************************
   * @brief 各列挙型項目の英語項目名を取得する。
   **********************************************************
   */ 
  public String getEnName() {
    return this.en_name;
  }
}
