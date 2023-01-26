/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.System_User_Test
 * 
 * @brief 通常用エンティティのうち、[システムユーザー管理]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、システムユーザー管理のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.System_User_Test;





/** 
 **************************************************************************************
 * @file System_User_FormInput_Test.java
 * 
 * @brief [システムユーザー管理]において、テスト対象エンティティに対応するフォームクラスを
 * 入力した際のテストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
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

import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_FormInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Form.NormalForm.System_User_Form;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [システムユーザー管理]において、テスト対象エンティティに対応するフォームクラスを
 * 入力した際のテストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[System_User]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see System_User
 * @see Entity_FormInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = System_User_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class System_User_FormInput_Test implements Entity_FormInput_Test{

  private final System_User_TestCase_Make testcase;
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

    System_User_Form mock_form = mock(System_User_Form.class);
    when(mock_form.getSerial_num()).thenReturn(null);
    when(mock_form.getMember_id()).thenReturn(str);
    when(mock_form.getUsername()).thenReturn(str);
    when(mock_form.getPassword()).thenReturn(str);
    when(mock_form.getAuth_id()).thenReturn(str);

    assertAllNull(new System_User(mock_form));
  }








  /** 
   ****************************************************************************************
   * @brief 初期化処理が終わったエンティティからデータを取り出し、全てがNullになっていることを確認。
   * 
   * @param[in] entity 初期化が終わっている検査対象のエンティティ
   * 
   * @details
   * - アサーションの際は、全てこのメソッドで行う。
   * - なお、ログイン失敗回数とロック有無は、最初にエンティティに入れた際は初期状態なので、
   * 「失敗回数0回」かつ「ロック無し」となる。
   ****************************************************************************************
   */ 
  private void assertAllNull(System_User entity){
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getMember_id()).isNull();
    this.softly.assertThat(entity.getUsername()).isNull();
    this.softly.assertThat(entity.getPassword()).isNull();
    this.softly.assertThat(entity.getAuth_id()).isNull();
    this.softly.assertThat(entity.getFail_count()).isEqualTo(0);
    this.softly.assertThat(entity.getLocking()).isEqualTo(false);
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

    Map<System_User_TestKeys, String> normalcase = testcase.getNormalData();

    Integer serial_num = Integer.parseInt(normalcase.get(System_User_TestKeys.Serial_Num));
    String member_id = normalcase.get(System_User_TestKeys.Member_Id);
    String username = normalcase.get(System_User_TestKeys.Username);
    String password = normalcase.get(System_User_TestKeys.Password);
    String auth_id = normalcase.get(System_User_TestKeys.Auth_Id);
    
    System_User_Form mock_form = mock(System_User_Form.class);
    when(mock_form.getSerial_num()).thenReturn(serial_num);
    when(mock_form.getMember_id()).thenReturn(member_id);
    when(mock_form.getUsername()).thenReturn(username);
    when(mock_form.getPassword()).thenReturn(password);
    when(mock_form.getAuth_id()).thenReturn(auth_id);
    
    System_User entity = new System_User(mock_form);
    
    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getMember_id()).isEqualTo(member_id);
    this.softly.assertThat(entity.getUsername()).isEqualTo(username);
    this.softly.assertThat(entity.getPassword()).isEqualTo(password);
    this.softly.assertThat(entity.getAuth_id()).isEqualTo(auth_id);
    this.softly.assertThat(entity.getFail_count()).isEqualTo(0);
    this.softly.assertThat(entity.getLocking()).isEqualTo(false);
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
