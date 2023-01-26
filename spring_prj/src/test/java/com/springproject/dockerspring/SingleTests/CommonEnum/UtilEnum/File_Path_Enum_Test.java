/** 
 **************************************************************************************
 * @file File_Path_Enum_Test.java
 * 
 * @brief 主に[ローカルファイル作成時のパス文字]機能において使用する列挙型をテストするクラスを
 * 格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.UtilEnum;

import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 主に[ローカルファイル作成時のパス文字]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[File_Path_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see File_Path_Enum
 **************************************************************************************
 */ 
public class File_Path_Enum_Test {

  private final String[] path_order = {
    "src/main/resources/TempFileBuffer",
    "AudioData",
    "FacilityPhoto",
    "ScorePdf",
    "rw-r--r--"
  };

  private final SoftAssertions softly = new SoftAssertions();




  /** 
   ****************************************************************************************
   * @brief 各列挙型項目の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  public void 各列挙型項目の一致テスト(){
    File_Path_Enum[] enumlist = File_Path_Enum.values();
    int size = this.path_order.length;

    for(int i = 0; i < size; i++){
      this.softly.assertThat(enumlist[i].getPath())
                 .isEqualTo(this.path_order[i]);
    }
        
    this.softly.assertAll();
  }
}