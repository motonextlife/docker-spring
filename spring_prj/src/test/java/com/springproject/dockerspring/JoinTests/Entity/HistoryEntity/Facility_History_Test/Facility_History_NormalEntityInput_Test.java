/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests
 * 
 * @brief テストを行うクラスのうち、[結合テスト]を格納したパッケージ
 * 
 * @details
 * - このパッケージは、テスト対象クラスが他のパッケージのクラスに依存する場合に、依存するクラスを
 * 用いて行う結合テストを行う機能を格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity
 * 
 * @brief 結合テストのうち、[データ格納エンティティ]に関するテストを格納したパッケージ
 * 
 * @details
 * - このパッケージは、エンティティ間でのデータの移し替えなどの、フォームクラスや履歴用エンティティ
 * 等の依存するクラスを用いて行った結合テストを格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.HistoryEntity
 * 
 * @brief 結合テストのデータ格納エンティティテストのうち、[履歴用エンティティ]に関するテストを
 * 格納したパッケージ
 * 
 * @details
 * - このパッケージは、履歴用エンティティにおいて、対となる通常データ用エンティティからの
 * データの移し替えの機能をテストするクラスを格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.HistoryEntity.Facility_History_Test
 * 
 * @brief 履歴用エンティティの結合テストのうち、[設備情報変更履歴]に関するテストを格納したパッケージ
 * 
 * @details
 * - このパッケージは、設備情報変更履歴で用いるエンティティの結合テストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.HistoryEntity.Facility_History_Test;





/** 
 **************************************************************************************
 * @file Facility_History_NormalEntityInput_Test.java
 * 
 * @brief [設備情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。また、現在位置のパッケージの上の階層の
 * パッケージの説明の記載も行う。
 **************************************************************************************
 */ 
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make.Facility_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_NormalEntityInput_Test;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;










/** 
 **************************************************************************************
 * @brief [設備情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラス
 * 
 * 
 * @details 
 * - 検査対象のクラス名は[Facility_History]である。
 * - テスト内容としては、入力が終わったデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Facility_History
 * @see Entity_NormalEntityInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Facility_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_History_NormalEntityInput_Test implements Entity_NormalEntityInput_Test{

  private final Facility_TestCase_Make testcase;
  private final SoftAssertions softly = new SoftAssertions();







  /** 
   ****************************************************************************************
   * @brief エンティティ内にNull初期化対象のデータだけを入れたモックエンティティを生成する。
   * このモックを検査対象エンティティに渡すことでテストが可能になる。そしてそのままテストに入る。
   * 
   * @param[in] str 格納する初期化対象文字
   * 
   * @details
   * - 格納するデータは引数で渡し、そのデータでエンティティ内を埋めつくす。
   * - ただし初期化対象は文字列型のデータのみなので、文字列型以外は何もせず、そのままNullの状態に
   * しておく。ただし、シリアルナンバー等の数値型は初期値がNullにならないようなので、あえて手動で
   * Nullに初期化しておく。
   ****************************************************************************************
   */ 
  private void setInitTargetEntity(String str){

    Facility depend_entity = new Facility();
    depend_entity.setSerial_num(null);
    depend_entity.setFaci_id(str);
    depend_entity.setFaci_name(str);
    depend_entity.setProducer(str);
    depend_entity.setStorage_loc(str);
    depend_entity.setOther_comment(str);
    
    assertAllNull(new Facility_History(depend_entity, null, str, null));
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
   * @brief 入力した通常エンティティからデータを取り出して履歴用エンティティに格納したデータが、
   * あらかじめ用意しておいたテストケースと同一であることを確認する。
   * 
   * @par 大まかな処理の流れ
   * - テストケースから、比較対象かつ格納するデータを取得する。
   * - 投入するための通常エンティティのモックを作成し、その中にテストケースを格納する。
   * - 検査対象エンティティのインスタンスを作成し、作成したモックを投入する。
   * - 検査対象エンティティからデータを取り出し、想定されたテストケースの値と同一か確認する。
   * 
   * @note 履歴として追加したデータは必ず新規追加扱いとなる。そのため、履歴番号は常に「Null」になって
   * いなければならない。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した通常エンティティデータの同一性確認() throws ParseException{
    
    Map<Facility_TestKeys, String> normalcase = testcase.getNormalData();
    Map<History_TestKeys, String> historycase = testcase.getHistoryNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    Date change_datetime = datetime.parse(historycase.get(History_TestKeys.Change_Datetime));
    String change_kinds = historycase.get(History_TestKeys.Change_Kinds);
    String operation_user = historycase.get(History_TestKeys.Operation_User);
    Integer serial_num = Integer.parseInt(normalcase.get(Facility_TestKeys.Serial_Num));
    String faci_id = normalcase.get(Facility_TestKeys.Faci_Id);
    String faci_name = normalcase.get(Facility_TestKeys.Faci_Name);
    Date buy_date = date.parse(normalcase.get(Facility_TestKeys.Buy_Date));
    String producer = normalcase.get(Facility_TestKeys.Producer);
    String storage_loc = normalcase.get(Facility_TestKeys.Storage_Loc);
    Date disp_date = date.parse(normalcase.get(Facility_TestKeys.Disp_Date));
    String other_comment = normalcase.get(Facility_TestKeys.Other_Comment);
    
    Facility depend_entity = new Facility();
    depend_entity.setSerial_num(serial_num);
    depend_entity.setFaci_id(faci_id);
    depend_entity.setFaci_name(faci_name);
    depend_entity.setBuy_date(buy_date);
    depend_entity.setProducer(producer);
    depend_entity.setStorage_loc(storage_loc);
    depend_entity.setDisp_date(disp_date);
    depend_entity.setOther_comment(other_comment);

    Facility_History entity = new Facility_History(depend_entity, 
                                                   History_Kind_Enum.INSERT, 
                                                   operation_user, 
                                                   change_datetime);

    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isEqualTo(change_datetime);
    this.softly.assertThat(entity.getChange_kinds()).isEqualTo(change_kinds);
    this.softly.assertThat(entity.getOperation_user()).isEqualTo(operation_user);
    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getFaci_id()).isEqualTo(faci_id);
    this.softly.assertThat(entity.getFaci_name()).isEqualTo(faci_name);
    this.softly.assertThat(entity.getBuy_date()).isEqualTo(buy_date);
    this.softly.assertThat(entity.getProducer()).isEqualTo(producer);
    this.softly.assertThat(entity.getStorage_loc()).isEqualTo(storage_loc);
    this.softly.assertThat(entity.getDisp_date()).isEqualTo(disp_date);
    this.softly.assertThat(entity.getOther_comment()).isEqualTo(other_comment);
    this.softly.assertAll();
  }









  /** 
   ****************************************************************************************
   * @brief 入力したデータに初期化対象文字が含まれていた場合、入力後に取り出した該当データが
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
  public void 通常エンティティの初期化対象文字のNull初期化確認(String init_str){
    setInitTargetEntity(init_str);
    this.softly.assertAll();
  }








  /** 
   ****************************************************************************************
   * @brief 入力したデータに[Null]が含まれていた場合でも、処理中に[NullPointerException]が発生して
   * 処理が止まらないことを確認する。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 通常エンティティのNull入力時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInitTargetEntity(null));
  }








  /** 
   ****************************************************************************************
   * @brief 入力の際に履歴種別を判定する列挙型を同時に保存するが、保存後のデータに列挙型に対応する
   * 文字列が間違いなく入っていることを確認する。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 登録履歴種別の同一性確認(){

    Facility depend_entity = new Facility();
    Facility_History entity;

    History_Kind_Enum[] hist_enum = {History_Kind_Enum.INSERT, 
                                     History_Kind_Enum.UPDATE, 
                                     History_Kind_Enum.DELETE, 
                                     History_Kind_Enum.ROLLBACK};

    String[] hist_str = {"insert", "update", "delete", "rollback"};
    int size = hist_enum.length;

    for(int i = 0; i < size; i++){
      entity = new Facility_History(depend_entity, hist_enum[i], null, null);
      this.softly.assertThat(entity.getChange_kinds()).isEqualTo(hist_str[i]);
    }

    this.softly.assertAll();
  }
}
