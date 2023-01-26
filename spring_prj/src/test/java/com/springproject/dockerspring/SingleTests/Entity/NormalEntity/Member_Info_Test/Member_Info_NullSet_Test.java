/** 
 **************************************************************************************
 * @file Member_Info_NullSet_Test.java
 * 
 * @brief [団員管理]において、エンティティ内部に格納されているデータをNull初期化するメソッドを
 * テストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Member_Info_Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_NullSet_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.SoftAssertions;









/** 
 **************************************************************************************
 * @brief [団員管理]において、エンティティ内部に格納されているデータをNull初期化するメソッドを
 * テストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info]で、検査対象メソッドは[stringSetNull]である。
 * - テスト内容としては、不正なデータの初期化テストである。
 * 
 * @see Member_Info
 * @see Entity_NullSet_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
public class Member_Info_NullSet_Test implements Entity_NullSet_Test{

  private final SoftAssertions softly = new SoftAssertions();






  
  /** 
   ****************************************************************************************
   * @brief エンティティ内にNull初期化対象のデータだけを入れた検査対象エンティティを生成する。
   * 生成後は、そのままテストを行う。
   * 
   * @param[in] str 格納する初期化対象文字
   * 
   * @details
   * - 格納するデータは引数で渡し、そのデータでエンティティ内を埋めつくす。
   * - ただし初期化対象は文字列型のデータのみなので、シリアルナンバー等の文字列型以外は何もせず、
   * そのままNullの状態にしておく。
   ****************************************************************************************
   */ 
  private void setInitTargetEntity(String str){

    Member_Info entity = new Member_Info();
    entity.setMember_id(str);
    entity.setName(str);
    entity.setName_pronu(str);
    entity.setSex(str);
    entity.setEmail_1(str);
    entity.setEmail_2(str);
    entity.setTel_1(str);
    entity.setTel_2(str);
    entity.setAddr_postcode(str);
    entity.setAddr(str);
    entity.setPosition(str);
    entity.setJob(str);
    entity.setAssign_dept(str);
    entity.setInst_charge(str);
    entity.setOther_comment(str);

    entity.stringSetNull();
    assertAllNull(entity);
  }







  /** 
   ****************************************************************************************
   * @brief 初期化処理が終わったエンティティからデータを取り出し、全てがNullになっていることを確認。
   * 
   * @param[in] entity 初期化が終わっている検査対象のエンティティ
   * 
   * @details
   * - アサーションの際は、全てこのメソッドで行う。
   ****************************************************************************************
   */ 
  private void assertAllNull(Member_Info entity){
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getMember_id()).isNull();
    this.softly.assertThat(entity.getName()).isNull();
    this.softly.assertThat(entity.getName_pronu()).isNull();
    this.softly.assertThat(entity.getSex()).isNull();
    this.softly.assertThat(entity.getBirthday()).isNull();
    this.softly.assertThat(entity.getFace_photo()).isNull();
    this.softly.assertThat(entity.getJoin_date()).isNull();
    this.softly.assertThat(entity.getRet_date()).isNull();
    this.softly.assertThat(entity.getEmail_1()).isNull();
    this.softly.assertThat(entity.getEmail_2()).isNull();
    this.softly.assertThat(entity.getTel_1()).isNull();
    this.softly.assertThat(entity.getTel_2()).isNull();
    this.softly.assertThat(entity.getAddr_postcode()).isNull();
    this.softly.assertThat(entity.getAddr()).isNull();
    this.softly.assertThat(entity.getPosition()).isNull();
    this.softly.assertThat(entity.getPosition_arri_date()).isNull();
    this.softly.assertThat(entity.getJob()).isNull();
    this.softly.assertThat(entity.getAssign_dept()).isNull();
    this.softly.assertThat(entity.getAssign_date()).isNull();
    this.softly.assertThat(entity.getInst_charge()).isNull();
    this.softly.assertThat(entity.getOther_comment()).isNull();
  }








  /** 
   ****************************************************************************************
   * @brief エンティティ内部のデータに初期化対象文字が含まれていた場合、入力後に取り出した該当データが
   * Nullに初期化されていることを確認する。
   * 
   * @details 初期化対象文字は以下の物である。
   * - 空文字("")
   * - 半角空白(" ")
   * - 全角空白("　")
   * - タブ空白("\t")
   * - 改行空白("\n")
   ****************************************************************************************
   */ 
  @ParameterizedTest
  @ValueSource(strings = {"", " ", "　", "\t", "\n"})
  @Override
  public void 初期化対象文字のNull初期化確認(String init_str){
    setInitTargetEntity(init_str);
    this.softly.assertAll();
  }





  /** 
   ****************************************************************************************
   * @brief エンティティ内部のデータに[Null]が含まれていた場合でも、処理中に[NullPointerException]が
   * 発生して処理が止まらないことを確認する。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void Null存在時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInitTargetEntity(null));
  }
}
