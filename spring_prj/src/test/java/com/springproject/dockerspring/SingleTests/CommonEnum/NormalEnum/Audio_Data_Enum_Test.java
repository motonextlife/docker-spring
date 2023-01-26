/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum
 * 
 * @brief 列挙型の単体テストのうち、[標準のデータ取り扱いの列挙型]に関するテストを格納する。
 * 
 * @details
 * - このパッケージは、各機能に付随するカラム名や、マップキー名などの文字列の統一の為に定義した
 * 列挙型のテストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum;





/** 
 **************************************************************************************
 * @file Audio_Data_Enum_Test.java
 * 
 * @brief 主に[音源データ管理]機能において使用する列挙型をテストするクラスを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.SingleTests.CommonEnum.Normal_Enum_Test;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Audio_Data_Enum;

import org.assertj.core.api.SoftAssertions;









/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Audio_Data_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Audio_Data_Enum
 **************************************************************************************
 */ 
public class Audio_Data_Enum_Test implements Normal_Enum_Test{

  private final String[] ja_name_order = {
    "シリアルナンバー",
    "音源番号",
    "音源名",
    "音源ハッシュ値"
  };

  private final String[] en_name_keyorder = {
    "serial_num",
    "sound_id",
    "sound_name",
    "audio_hash"
  };

  private static Audio_Data_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();






  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    Audio_Data_Enum_Test.enumlist = Audio_Data_Enum.values();
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
      this.softly.assertThat(Audio_Data_Enum_Test.enumlist[i].getJaName())
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
      this.softly.assertThat(Audio_Data_Enum_Test.enumlist[i].getEnName())
                 .isEqualTo(this.en_name_keyorder[i]);
    }
    
    this.softly.assertAll();
  }
}