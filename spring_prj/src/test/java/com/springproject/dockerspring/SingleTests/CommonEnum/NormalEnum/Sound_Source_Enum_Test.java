/** 
 **************************************************************************************
 * @file Sound_Source_Enum_Test.java
 * 
 * @brief 主に[音源情報管理]機能において使用する列挙型をテストするクラスを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.SingleTests.CommonEnum.Normal_Enum_Test;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Sound_Source_Enum;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 主に[音源情報管理]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Sound_Source_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Sound_Source_Enum
 **************************************************************************************
 */ 
public class Sound_Source_Enum_Test implements Normal_Enum_Test{

  private final String[] ja_name_order = {
    "シリアルナンバー",
    "音源番号",
    "登録日",
    "曲名",
    "作曲者",
    "演奏者",
    "出版社",
    "その他コメント"
  };

  private final String[] en_name_keyorder = {
    "serial_num",
    "sound_id",
    "upload_date",
    "song_title",
    "composer",
    "performer",
    "publisher",
    "other_comment"
  };

  private static Sound_Source_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();







  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    Sound_Source_Enum_Test.enumlist = Sound_Source_Enum.values();
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
      this.softly.assertThat(Sound_Source_Enum_Test.enumlist[i].getJaName())
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
      this.softly.assertThat(Sound_Source_Enum_Test.enumlist[i].getEnName())
                 .isEqualTo(this.en_name_keyorder[i]);
    }
        
    this.softly.assertAll();
  }
}