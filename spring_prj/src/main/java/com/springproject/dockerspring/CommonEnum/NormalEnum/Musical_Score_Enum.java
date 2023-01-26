/** 
 **************************************************************************************
 * @file Musical_Score_Enum.java
 * @brief 主に[楽譜情報管理]機能において使用する列挙型を格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonEnum.NormalEnum;




/** 
 **************************************************************************************
 * @brief 主に[楽譜情報管理]機能において使用する列挙型である。
 * @details 
 * - 列挙型に定義する内容としては、「日本語でのデータ項目名」「英語でのデータ項目名」を定義する。
 * - 日本語項目名はCSVファイルの出力の際のヘッダー名に使用する。
 * - 英語項目名に関してはマップリストのキー名の定義に用いる。
 **************************************************************************************
 */ 
public enum Musical_Score_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Score_Id("楽譜番号", "score_id"),
  Buy_Date("購入日", "buy_date"),
  Song_Title("曲名", "song_title"), 
  Composer("作曲者", "composer"),
  Arranger("編曲者", "arranger"),
  Publisher("出版社", "publisher"),
  Storage_Loc("保管場所", "storage_loc"),
  Disp_Date("廃棄日", "disp_date"),
  Other_Comment("その他コメント", "other_comment");


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
  private Musical_Score_Enum(String ja_name, String en_name){
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
