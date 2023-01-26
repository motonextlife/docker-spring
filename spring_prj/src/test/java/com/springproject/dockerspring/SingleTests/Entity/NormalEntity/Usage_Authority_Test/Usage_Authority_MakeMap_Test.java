/** 
 **************************************************************************************
 * @file Usage_Authority_MakeMap_Test.java
 * 
 * @brief [権限管理]において、エンティティ内部のデータを画面出力用に文字列に変換して出力する
 * メソッドをテストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Usage_Authority_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make.Usage_Authority_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_MakeMap_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.HashMap;
import java.util.Map;







/** 
 **************************************************************************************
 * @brief [権限管理]において、エンティティ内部のデータを画面出力用に文字列に変換して出力する
 * メソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Usage_Authority]で、検査対象メソッドは[makeMap]である。
 * - テスト内容としては、出力されたデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Usage_Authority
 * @see Entity_MakeMap_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Usage_Authority_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Usage_Authority_MakeMap_Test implements Entity_MakeMap_Test{

  private final Usage_Authority_TestCase_Make testcase;
  private final SoftAssertions softly = new SoftAssertions();



  






  /** 
   ****************************************************************************************
   * @brief エンティティ内にNull初期化の対象となるデータだけが存在し、それらが出力されたデータを
   * 比較するための、マップリストを作成する。
   * 
   * @return 生成した比較用のマップリスト
   * 
   * @details
   * - エンティティ内の全データが初期化対象だった場合は、出力されるデータは全て空文字となる。
   * そのためこのメソッドで作成する比較用マップリストは全ての項目に空文字が入る。
   * - 権限情報が初期化対象だった場合、「全ての権限無し」の状態になる。
   ****************************************************************************************
   */ 
  private Map<String, String> setInitCompareMap(){
    Map<String, String> init_map = new HashMap<>();

    init_map.put("serial_num", "");
    init_map.put("auth_id", "");
    init_map.put("auth_name", "");
    init_map.put("admin", "false");
    init_map.put("member_info", "none");
    init_map.put("facility", "none");
    init_map.put("musical_score", "none");
    init_map.put("sound_source", "none");

    return init_map;
  }








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

    Usage_Authority entity = new Usage_Authority();
    entity.setAuth_id(str);
    entity.setAuth_name(str);
    entity.setMember_info(str);
    entity.setFacility(str);
    entity.setMusical_score(str);
    entity.setSound_source(str);

    this.softly.assertThat(setInitCompareMap())
               .isEqualTo(entity.makeMap());
  }





  /** 
   ****************************************************************************************
   * @brief あらかじめ対象エンティティにデータを格納しておき、検査対象メソッドを動かしてデータを取得
   * した際に、あらかじめ用意しておいたテストケースと同一であることを確認する。
   * 
   * @par 大まかな処理の流れ
   * - テストケースから、比較対象かつ格納するデータを取得する。
   * - 検査対象エンティティのインスタンスを作成し、テストケースのデータを格納する。
   * - 比較するためのマップリストを、テストケースを使って作成する。
   * - 検査対象メソッドを実行し出力されたマップリストを取得。
   * - 出力されたマップリストと、比較用マップリストを比較し、完全一致していれば合格とする。
   * - なお、管理者権限に関しては、「True」と「False」の両方で検証する。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した文字列マップリストデータの同一性確認(){

    Map<Usage_Authority_TestKeys, String> normal_case = testcase.getNormalData();

    String serial_num = normal_case.get(Usage_Authority_TestKeys.Serial_Num);
    String auth_id = normal_case.get(Usage_Authority_TestKeys.Auth_Id);
    String auth_name = normal_case.get(Usage_Authority_TestKeys.Auth_Name);
    String admin = normal_case.get(Usage_Authority_TestKeys.Admin);
    String member_info = normal_case.get(Usage_Authority_TestKeys.Member_Info);
    String facility = normal_case.get(Usage_Authority_TestKeys.Facility);
    String musical_score = normal_case.get(Usage_Authority_TestKeys.Musical_Score);
    String sound_source = normal_case.get(Usage_Authority_TestKeys.Sound_Source);

    Usage_Authority entity = new Usage_Authority();
    entity.setSerial_num(Integer.parseInt(serial_num));
    entity.setAuth_id(auth_id);
    entity.setAuth_name(auth_name);
		entity.setAdmin(Boolean.parseBoolean(admin));
    entity.setMember_info(member_info);
    entity.setFacility(facility);
    entity.setMusical_score(musical_score);
    entity.setSound_source(sound_source);

    Map<String, String> compare_map = new HashMap<>();
    compare_map.put("serial_num", serial_num);
    compare_map.put("auth_id", auth_id);
    compare_map.put("auth_name", auth_name);
    compare_map.put("admin", admin);
    compare_map.put("member_info", member_info);
    compare_map.put("facility", facility);
    compare_map.put("musical_score", musical_score);
    compare_map.put("sound_source", sound_source);

    this.softly.assertThat(compare_map).isEqualTo(entity.makeMap());

    entity.setAdmin(Boolean.parseBoolean("false"));
    compare_map.put("admin", "false");

    this.softly.assertThat(compare_map).isEqualTo(entity.makeMap());
    this.softly.assertAll();
  }









  /** 
   ****************************************************************************************
   * @brief 出力しようとしたデータに初期化対象文字が含まれていた場合、出力後の該当データが空文字に
   * 初期化されていることを確認する。
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
  public void 文字列マップリストの初期化対象文字の空文字初期化確認(String init_str){
    setInitTargetEntity(init_str);
    this.softly.assertAll();
  }







  /** 
   ****************************************************************************************
   * @brief 出力しようとしたデータに[Null]が含まれていた場合でも、処理中に[NullPointerException]が
   * 発生して処理が止まらないことを確認する。
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 文字列マップリストの空文字入力時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInitTargetEntity(null));
  }
}
