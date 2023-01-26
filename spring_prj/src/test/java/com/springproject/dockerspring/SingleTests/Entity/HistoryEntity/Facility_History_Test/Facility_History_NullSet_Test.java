/** 
 **************************************************************************************
 * @file Facility_History_NullSet_Test.java
 * 
 * @brief [設備情報変更履歴]において、エンティティ内部に格納されているデータをNull初期化する
 * メソッドをテストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Facility_History_Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_NullSet_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.SoftAssertions;








/** 
 **************************************************************************************
 * @brief [設備情報変更履歴]において、エンティティ内部に格納されているデータをNull初期化する
 * メソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Facility_History]で、検査対象メソッドは[stringSetNull]である。
 * - テスト内容としては、不正なデータの初期化テストである。
 * 
 * @see Facility_History
 * @see Entity_NullSet_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
public class Facility_History_NullSet_Test implements Entity_NullSet_Test{

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

    Facility_History entity = new Facility_History();
    entity.setChange_kinds(str);
    entity.setOperation_user(str);
    entity.setFaci_id(str);
    entity.setFaci_name(str);
    entity.setProducer(str);
    entity.setStorage_loc(str);
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
  private void assertAllNull(Facility_History entity){
    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isNull();
    this.softly.assertThat(entity.getChange_kinds()).isNull();
    this.softly.assertThat(entity.getOperation_user()).isNull();
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getFaci_id()).isNull();
    this.softly.assertThat(entity.getFaci_name()).isNull();
    this.softly.assertThat(entity.getBuy_date()).isNull();
    this.softly.assertThat(entity.getProducer()).isNull();
    this.softly.assertThat(entity.getStorage_loc()).isNull();
    this.softly.assertThat(entity.getDisp_date()).isNull();
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
