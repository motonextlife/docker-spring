/** 
 **************************************************************************************
 * @file Datatype_Enum_Test.java
 * 
 * @brief 主に[バイナリデータのデータ型定義]機能において使用する列挙型をテストするクラスを
 * 格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.UtilEnum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 主に[バイナリデータのデータ型定義]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Datatype_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Datatype_Enum
 **************************************************************************************
 */ 
public class Datatype_Enum_Test {

  private String[] mimetype_order = {
    "image/png",
    "audio/vnd.wave",
    "application/pdf",
    "text/plain",
    "application/zip",
    "application/gzip"
  };

  private String[] extension_order = {
    "png",
    "wav",
    "pdf",
    "csv",
    "zip",
    "gz"
  };


  private static Datatype_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();




  


  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    Datatype_Enum_Test.enumlist = Datatype_Enum.values();
  }







  /** 
   ****************************************************************************************
   * @brief 各列挙型項目のコンテンツタイプ文字列の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  public void 各列挙型項目のコンテンツタイプ文字列の一致テスト(){
    int size = this.mimetype_order.length;

    for(int i = 0; i < size; i++){
      this.softly.assertThat(Datatype_Enum_Test.enumlist[i].getMimetype())
                 .isEqualTo(this.mimetype_order[i]);
    }
        
    this.softly.assertAll();
  }







  /** 
   ****************************************************************************************
   * @brief 各列挙型項目のファイル拡張子文字列の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  public void 各列挙型項目のファイル拡張子文字列の一致テスト(){
    int size = this.extension_order.length;

    for(int i = 0; i < size; i++){
      this.softly.assertThat(Datatype_Enum_Test.enumlist[i].getExtension())
                 .isEqualTo(this.extension_order[i]);
    }
        
    this.softly.assertAll();
  }
}