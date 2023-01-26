/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Usage_Authority_Test
 * 
 * @brief 通常用エンティティの結合テストのうち、[権限情報]に関するテストを格納したパッケージ
 * 
 * @details
 * - このパッケージは、権限情報で用いるエンティティの結合テストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Usage_Authority_Test;





/** 
 **************************************************************************************
 * @file Usage_Authority_FormInput_Test.java
 * 
 * @brief [権限管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラスを格納するファイルである。
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

import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make.Usage_Authority_TestKeys;
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_FormInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Form.NormalForm.Usage_Authority_Form;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [権限管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Usage_Authority]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Usage_Authority
 * @see Entity_FormInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Usage_Authority_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Usage_Authority_FormInput_Test implements Entity_FormInput_Test{

  private final Usage_Authority_TestCase_Make testcase;
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

    Usage_Authority_Form depend_form = new Usage_Authority_Form();
    depend_form.setSerial_num(null);
    depend_form.setAuth_id(str);
    depend_form.setAuth_name(str);
		depend_form.setAdmin(null);
    depend_form.setMember_info(str);
    depend_form.setFacility(str);
    depend_form.setMusical_score(str);
    depend_form.setSound_source(str);

    assertAllNull(new Usage_Authority(depend_form));
  }








  /** 
   ****************************************************************************************
   * @brief 初期化処理が終わったエンティティからデータを取り出し、全てがNullになっていることを確認。
   * 
   * @param[in] entity 初期化が終わっている検査対象のエンティティ
   * 
   * @details
   * - アサーションの際は、全てこのメソッドで行う。
   * - なお、権限情報は、エンティティに追加した最初の段階では「全く権限がない」状態となっている。
   ****************************************************************************************
   */ 
  private void assertAllNull(Usage_Authority entity){
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getAuth_id()).isNull();
    this.softly.assertThat(entity.getAuth_name()).isNull();
		this.softly.assertThat(entity.getAdmin()).isEqualTo(false);
    this.softly.assertThat(entity.getMember_info()).isEqualTo("none");
    this.softly.assertThat(entity.getFacility()).isEqualTo("none");
    this.softly.assertThat(entity.getMusical_score()).isEqualTo("none");
    this.softly.assertThat(entity.getSound_source()).isEqualTo("none");
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

    Map<Usage_Authority_TestKeys, String> normalcase = testcase.getNormalData();
    
    Integer serial_num = Integer.parseInt(normalcase.get(Usage_Authority_TestKeys.Serial_Num));
    String auth_id = normalcase.get(Usage_Authority_TestKeys.Auth_Id);
    String auth_name = normalcase.get(Usage_Authority_TestKeys.Auth_Name);
    Boolean admin = Boolean.parseBoolean(normalcase.get(Usage_Authority_TestKeys.Admin));
    String member_info = normalcase.get(Usage_Authority_TestKeys.Member_Info);
    String facility = normalcase.get(Usage_Authority_TestKeys.Facility);
    String musical_score = normalcase.get(Usage_Authority_TestKeys.Musical_Score);
    String sound_source = normalcase.get(Usage_Authority_TestKeys.Sound_Source);
    
    Usage_Authority_Form depend_form = new Usage_Authority_Form();
    depend_form.setSerial_num(serial_num);
    depend_form.setAuth_id(auth_id);
    depend_form.setAuth_name(auth_name);
		depend_form.setAdmin(admin);
    depend_form.setMember_info(member_info);
    depend_form.setFacility(facility);
    depend_form.setMusical_score(musical_score);
    depend_form.setSound_source(sound_source);

    Usage_Authority entity = new Usage_Authority(depend_form);

    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getAuth_id()).isEqualTo(auth_id);
    this.softly.assertThat(entity.getAuth_name()).isEqualTo(auth_name);
		this.softly.assertThat(entity.getAdmin()).isEqualTo(admin);
    this.softly.assertThat(entity.getMember_info()).isEqualTo(member_info);
    this.softly.assertThat(entity.getFacility()).isEqualTo(facility);
    this.softly.assertThat(entity.getMusical_score()).isEqualTo(musical_score);
    this.softly.assertThat(entity.getSound_source()).isEqualTo(sound_source);
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
