/** 
 **************************************************************************************
 * @file Datatype_Enum.java
 * @brief 主に[バイナリデータのデータ型定義]機能において使用する列挙型を格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonEnum.UtilEnum;





/** 
 **************************************************************************************
 * @brief 主に[バイナリデータのデータ型定義]機能において使用する列挙型である。
 * @details 
 * - 列挙型に定義する内容としては、「データのMIMEタイプ」「拡張子」を定義する。
 * - MIMEタイプに関しては、バイナリデータのバリデーションの際にデータ型を判別する際に用いる。
 * - 拡張子に関しては前項のバリデーションに加え、ファイルサーバーにデータ保存する際に用いる。
 **************************************************************************************
 */ 
public enum Datatype_Enum{
  PHOTO("image/png", "png"),
  AUDIO("audio/vnd.wave", "wav"),
  PDF("application/pdf", "pdf"),
  CSV("text/plain", "csv"),
  ZIP("application/zip", "zip"),
  GZIP("application/gzip", "gz");

  
  //! MIMEタイプ文字列の格納変数
  private final String mimetype;

  //! 拡張子文字列の格納変数
  private final String extension;


  /** 
   **********************************************************
   * @brief 列挙型の定義値をセットする。
   * @param[in] mimetype MIMEタイプ文字列をセットする。
   * @param[in] extension ファイルの拡張子名をセットする。
   **********************************************************
   */ 
  private Datatype_Enum(String mimetype, String extension){
    this.mimetype = mimetype;
    this.extension = extension;
  }


  /** 
   **********************************************************
   * @brief 各列挙型のMIMEタイプ文字列を取得する。
   **********************************************************
   */ 
  public String getMimetype() {
    return this.mimetype;
  }


  /** 
   **********************************************************
   * @brief 各列挙型の拡張子を取得する。
   **********************************************************
   */ 
  public String getExtension() {
    return this.extension;
  }
}