/** 
 **************************************************************************************
 * @file Usage_Authority_Enum_Test.java
 * 
 * @brief 主に[権限情報]機能において使用する列挙型をテストするクラスを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.SingleTests.CommonEnum.Normal_Enum_Test;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Usage_Authority_Enum;

import org.assertj.core.api.SoftAssertions;






/** 
 **************************************************************************************
 * @brief 主に[権限情報]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Usage_Authority_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Usage_Authority_Enum
 **************************************************************************************
 */ 
public class Usage_Authority_Enum_Test implements Normal_Enum_Test{

  private String[] ja_name_order = {
    "シリアルナンバー",
    "権限番号",
    "権限名",
    "管理者権限",
    "団員管理権限",
    "設備管理権限",
    "楽譜管理権限",
    "音源管理権限"
  };

  private String[] en_name_keyorder = {
    "serial_num",
    "auth_id",
    "auth_name",
    "admin",
    "member_info",
    "facility",
    "musical_score",
    "sound_source"
  };

  private static Usage_Authority_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();





  

  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    Usage_Authority_Enum_Test.enumlist = Usage_Authority_Enum.values();
  }





  
  /** 
   ****************************************************************************************
   * @brief 各列挙型項目の日本語項目名の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 各列挙型項目の日本語項目名の一致テスト(){
    int size = this.ja_name_order.length;

    for(int i = 0; i < size; i++){
      this.softly.assertThat(Usage_Authority_Enum_Test.enumlist[i].getJaName())
                 .isEqualTo(this.ja_name_order[i]);
    }
        
    this.softly.assertAll();
  }




  

  /** 
   ****************************************************************************************
   * @brief 各列挙型項目の英語項目名の一致テストを行う。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 各列挙型項目の英語項目名の一致テスト(){
    int size = this.en_name_keyorder.length;
    
    for(int i = 0; i < size; i++){
      this.softly.assertThat(Usage_Authority_Enum_Test.enumlist[i].getEnName())
                 .isEqualTo(this.en_name_keyorder[i]);
    }
        
    this.softly.assertAll();
  }
}