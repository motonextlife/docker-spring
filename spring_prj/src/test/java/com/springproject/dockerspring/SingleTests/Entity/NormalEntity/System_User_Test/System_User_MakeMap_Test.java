/** 
 **************************************************************************************
 * @file System_User_MakeMap_Test.java
 * 
 * @brief [システムユーザー管理]において、エンティティ内部のデータを画面出力用に文字列に変換して
 * 出力するメソッドをテストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.System_User_Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_MakeMap_Test;

import lombok.RequiredArgsConstructor;







/** 
 **************************************************************************************
 * @brief [システムユーザー管理]において、エンティティ内部のデータを画面出力用に文字列に変換して
 * 出力するメソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[System_User]で、検査対象メソッドは[makeMap]である。
 * - テスト内容としては、出力されたデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see System_User
 * @see Entity_MakeMap_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = System_User_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class System_User_MakeMap_Test implements Entity_MakeMap_Test{

  private final System_User_TestCase_Make testcase;
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
   * - ロック有無に関しては、初期化対象だった場合アカウントを自動ロックするようになっているため
   * 初期化後は「True」となる。
   ****************************************************************************************
   */ 
  private Map<String, String> setInitCompareMap(){
    Map<String, String> init_map = new HashMap<>();

    init_map.put("serial_num", "");
    init_map.put("member_id", "");
    init_map.put("username", "");
    init_map.put("auth_id", "");
    init_map.put("locking", "true");

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

    System_User entity = new System_User();
    entity.setMember_id(str);
    entity.setUsername(str);
    entity.setPassword(str);
    entity.setAuth_id(str);

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
   * - なお、ロックの有無に関しては、「True」と「False」の両方で検証する。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した文字列マップリストデータの同一性確認() throws ParseException{

    Map<System_User_TestKeys, String> normal_case = testcase.getNormalData();

    String serial_num = normal_case.get(System_User_TestKeys.Serial_Num);
    String member_id = normal_case.get(System_User_TestKeys.Member_Id);
    String username = normal_case.get(System_User_TestKeys.Username);
    String password = normal_case.get(System_User_TestKeys.Password);
    String auth_id = normal_case.get(System_User_TestKeys.Auth_Id);
    String locking = normal_case.get(System_User_TestKeys.Locking);

    System_User entity = new System_User();
    entity.setSerial_num(Integer.parseInt(serial_num));
    entity.setMember_id(member_id);
    entity.setUsername(username);
    entity.setPassword(password);
    entity.setAuth_id(auth_id);
    entity.setLocking(Boolean.parseBoolean(locking));

    Map<String, String> compare_map = new HashMap<>();
    compare_map.put("serial_num", serial_num);
    compare_map.put("member_id", member_id);
    compare_map.put("username", username);
    compare_map.put("auth_id", auth_id);
    compare_map.put("locking", locking);

    this.softly.assertThat(compare_map).isEqualTo(entity.makeMap());

    entity.setLocking(Boolean.parseBoolean("true"));
    compare_map.put("locking", "true");

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
