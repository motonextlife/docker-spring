/** 
 **************************************************************************************
 * @file Member_Info_Enum_Test.java
 * 
 * @brief 主に[団員情報管理]機能において使用する列挙型をテストするクラスを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.CommonEnum.NormalEnum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.springproject.dockerspring.SingleTests.CommonEnum.Normal_Enum_Test;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief 主に[団員情報管理]機能において使用する列挙型をテストするクラス。
 * 
 * @details 
 * - 検査対象の列挙型名は[Member_Info_Enum]である。
 * - テスト内容としては、主に想定された値が列挙型に格納されていることを確認する。
 * 
 * @par 大まかなテストの手順
 * - 列挙型に格納されている値の比較用として、想定される値を想定される順番通りに格納した配列を用意。
 * - 比較用配列と検査対象列挙型から順番に値を取り出していき、値が完全一致することを確認。
 * 
 * @see Member_Info_Enum
 **************************************************************************************
 */ 
public class Member_Info_Enum_Test implements Normal_Enum_Test{

  private final String[] ja_name_order = {
    "シリアルナンバー",
    "団員番号",
    "名前",
    "ふりがな",
    "性別",
    "誕生日",
    "顔写真",
    "入団日",
    "退団日",
    "メールアドレス１",
    "メールアドレス２",
    "電話番号１",
    "電話番号２",
    "現住所郵便番号",
    "現住所",
    "役職名",
    "現役職着任日",
    "職種名",
    "配属部署",
    "配属日",
    "担当楽器",
    "その他コメント"
  };

  private final String[] en_name_keyorder = {
    "serial_num",
    "member_id",
    "name",
    "name_pronu",
    "sex",
    "birthday",
    "face_photo",
    "join_date",
    "ret_date",
    "email_1",
    "email_2",
    "tel_1",
    "tel_2",
    "addr_postcode",
    "addr",
    "position",
    "position_arri_date",
    "job",
    "assign_dept",
    "assign_date",
    "inst_charge",
    "other_comment"
  };

  private static Member_Info_Enum[] enumlist;

  private final SoftAssertions softly = new SoftAssertions();







  /** 
   ****************************************************************************************
   * @brief テストの前準備として、検査対象列挙型から値を配列化したものを取得し、静的フィールドに保存。
   ****************************************************************************************
   */ 
  @BeforeAll
  public static void setUp(){
    Member_Info_Enum_Test.enumlist = Member_Info_Enum.values();
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
      this.softly.assertThat(Member_Info_Enum_Test.enumlist[i].getJaName())
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
      this.softly.assertThat(Member_Info_Enum_Test.enumlist[i].getEnName())
                 .isEqualTo(this.en_name_keyorder[i]);
    }
        
    this.softly.assertAll();
  }
}