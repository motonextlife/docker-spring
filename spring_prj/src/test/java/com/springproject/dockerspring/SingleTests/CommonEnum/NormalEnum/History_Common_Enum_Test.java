/** 
 **************************************************************************************
 * @file History_Common_Enum_Test.java
 * 
 * @brief 主に[全機能共通の履歴情報管理]機能において使用する列挙型をテストするクラスを格納した
 * ファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.SingleTests.CommonEnum.Normal_Enum_Test;
import com.springproject.dockerspring.CommonEnum.NormalEnum.History_Common_Enum;

import org.assertj.core.api.SoftAssertions;









/** 
 **************************************************************************************
 * @brief 主に[全機能共通の履歴情報管理]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[History_Common_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see History_Common_Enum
 **************************************************************************************
 */ 
public class History_Common_Enum_Test implements Normal_Enum_Test{

  private final String[] ja_name_order = {
    "履歴番号",
    "履歴記録日時",
    "履歴種別",
    "操作ユーザー名"
  };

  private final String[] en_name_keyorder = {
    "history_id",
    "change_datetime",
    "change_kinds",
    "operation_user"
  };

  private static History_Common_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();








  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    History_Common_Enum_Test.enumlist = History_Common_Enum.values();
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
      this.softly.assertThat(History_Common_Enum_Test.enumlist[i].getJaName())
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
      this.softly.assertThat(History_Common_Enum_Test.enumlist[i].getEnName())
                 .isEqualTo(this.en_name_keyorder[i]);
    }
        
    this.softly.assertAll();
  }
}