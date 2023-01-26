/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.NormalEntity
 * 
 * @brief 結合テストのデータ格納エンティティテストのうち、[通常用エンティティ]に関するテストを
 * 格納したパッケージ
 * 
 * @details
 * - このパッケージは、通常用エンティティにおいて、対となる履歴データ用エンティティやフォームクラス
 * からのデータの移し替えの機能をテストするクラスを格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Facility_Test
 * 
 * @brief 通常用エンティティの結合テストのうち、[設備情報]に関するテストを格納したパッケージ
 * 
 * @details
 * - このパッケージは、設備情報で用いるエンティティの結合テストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Facility_Test;





/** 
 **************************************************************************************
 * @file Facility_FormInput_Test.java
 * 
 * @brief [設備管理]において、テスト対象エンティティに対応するフォームクラスを入力した
 * 際のテストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
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
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_FormInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Form.CsvImplForm.Facility_Form;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [設備管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Facility]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Facility
 * @see Entity_FormInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Facility_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_FormInput_Test implements Entity_FormInput_Test{

  private final Facility_TestCase_Make testcase;
  private final SoftAssertions softly = new SoftAssertions();









  /** 
   ****************************************************************************************
   * @brief フォームクラス内にNull初期化対象のデータだけを入れたモックフォームを生成する。
   * このモックを検査対象エンティティに渡すことでテストが可能になる。そしてそのままテストに入る。
   * 
   * @param[in] str 格納する初期化対象文字
   * 
   * @details
   * - 格納するデータは引数で渡し、そのデータでフォームクラス内を埋めつくす。
   * - ただし初期化対象は文字列型のデータのみなので、文字列型以外は何もせず、そのままNullの状態に
   * しておく。ただし、シリアルナンバー等の数値型は初期値がNullにならないようなので、あえて手動で
   * Nullに初期化しておく。
   ****************************************************************************************
   */ 
  private void setInitTargetForm(String str){

    Facility_Form depend_form = new Facility_Form();
    depend_form.setSerial_num(null);
    depend_form.setFaci_id(str);
    depend_form.setFaci_name(str);
    depend_form.setProducer(str);
    depend_form.setStorage_loc(str);
    depend_form.setOther_comment(str);

    assertAllNull(new Facility(depend_form));
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
  private void assertAllNull(Facility entity){
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
   * @brief 入力したフォームクラスからデータを取り出してエンティティに格納したデータが、あらかじめ
   * 用意しておいたテストケースと同一であることを確認する。
   * 
   * @par 大まかな処理の流れ
   * - テストケースから、比較対象かつ格納するデータを取得する。
   * - 投入するためのフォームクラスのモックを作成し、その中にテストケースを格納する。
   * - 検査対象エンティティのインスタンスを作成し、作成したモックを投入する。
   * - 検査対象エンティティからデータを取り出し、想定されたテストケースの値と同一か確認する。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力したフォームクラスデータの同一性確認() throws ParseException{

    Map<Facility_TestKeys, String> normal_case = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    Integer serial_num = Integer.parseInt(normal_case.get(Facility_TestKeys.Serial_Num));
    String faci_id = normal_case.get(Facility_TestKeys.Faci_Id);
    String faci_name = normal_case.get(Facility_TestKeys.Faci_Name);
    Date buy_date = date.parse(normal_case.get(Facility_TestKeys.Buy_Date));
    String producer = normal_case.get(Facility_TestKeys.Producer);
    String storage_loc = normal_case.get(Facility_TestKeys.Storage_Loc);
    Date disp_date = date.parse(normal_case.get(Facility_TestKeys.Disp_Date));
    String other_comment = normal_case.get(Facility_TestKeys.Other_Comment);
    
    Facility_Form depend_form = new Facility_Form();
    depend_form.setSerial_num(serial_num);
    depend_form.setFaci_id(faci_id);
    depend_form.setFaci_name(faci_name);
    depend_form.setBuy_date(buy_date);
    depend_form.setProducer(producer);
    depend_form.setStorage_loc(storage_loc);
    depend_form.setDisp_date(disp_date);
    depend_form.setOther_comment(other_comment);

    Facility entity = new Facility(depend_form);

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
  public void フォームクラスの初期化対象文字のNull初期化確認(String init_str){
    setInitTargetForm(init_str);
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
  public void フォームクラスのNull入力時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInitTargetForm(null));
  }
}
