/** 
 **************************************************************************************
 * @file Regex_Enum_Test.java
 * 
 * @brief 主に[正規表現文字列]機能において使用する列挙型をテストするクラスを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.UtilEnum;

import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Regex_Enum;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 主に[正規表現文字列]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Regex_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Regex_Enum
 **************************************************************************************
 */ 
public class Regex_Enum_Test {

  private String[] regex_order = {
    "^[a-zA-Z0-9-]+$",
    "^[a-zA-Z0-9-]*$",
    "^[^ -~｡-ﾟ\t\n]+$",
    "^[^ -~｡-ﾟ\t\n]*$",
    "^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$",
    "^([0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01]))*$",
    "^[a-zA-Z0-9_+\\.@-]*$",
    "^[0-9-]*$",
    "^[0-9]*$"
  };

  private final SoftAssertions softly = new SoftAssertions();



  



  /** 
   ****************************************************************************************
   * @brief 各列挙型項目の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  public void 各列挙型項目の一致テスト(){
    Regex_Enum[] enumlist = Regex_Enum.values();
    int size = this.regex_order.length;

    for(int i = 0; i < size; i++){
      this.softly.assertThat(enumlist[i].getRegex())
                 .isEqualTo(this.regex_order[i]);
    }
        
    this.softly.assertAll();
  }
}