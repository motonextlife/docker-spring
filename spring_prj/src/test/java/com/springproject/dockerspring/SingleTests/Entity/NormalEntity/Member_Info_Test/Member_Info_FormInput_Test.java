/** 
 **************************************************************************************
 * @file Member_Info_FormInput_Test.java
 * 
 * @brief [団員管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Member_Info_Test;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_FormInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Form.CsvImplForm.Member_Info_Form;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 * 
 * 
 **************************************************************************************
 * @brief [団員管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Member_Info
 * @see Entity_FormInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_FormInput_Test implements Entity_FormInput_Test{

  private final Member_Info_TestCase_Make testcase;
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
   * 
   * @throw IOException
   ****************************************************************************************
   */ 
  private void setInitTargetForm(String str) throws IOException{

    Member_Info_Form mock_form = mock(Member_Info_Form.class);
    when(mock_form.getSerial_num()).thenReturn(null);
    when(mock_form.getMember_id()).thenReturn(str);
    when(mock_form.getName()).thenReturn(str);
    when(mock_form.getName_pronu()).thenReturn(str);
    when(mock_form.getSex()).thenReturn(str);
    when(mock_form.getEmail_1()).thenReturn(str);
    when(mock_form.getEmail_2()).thenReturn(str);
    when(mock_form.getTel_1()).thenReturn(str);
    when(mock_form.getTel_2()).thenReturn(str);
    when(mock_form.getAddr_postcode()).thenReturn(str);
    when(mock_form.getAddr()).thenReturn(str);
    when(mock_form.getPosition()).thenReturn(str);
    when(mock_form.getJob()).thenReturn(str);
    when(mock_form.getAssign_dept()).thenReturn(str);
    when(mock_form.getInst_charge()).thenReturn(str);
    when(mock_form.getOther_comment()).thenReturn(str);

    assertAllNull(new Member_Info(mock_form));
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
   * @throw IOException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力したフォームクラスデータの同一性確認() throws ParseException, IOException{

    Map<Member_Info_TestKeys, String> normalcase = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    Integer serial_num = Integer.parseInt(normalcase.get(Member_Info_TestKeys.Serial_Num));
    String member_id = normalcase.get(Member_Info_TestKeys.Member_Id);
    String name = normalcase.get(Member_Info_TestKeys.Name);
    String name_pronu = normalcase.get(Member_Info_TestKeys.Name_Pronu);
    String sex = normalcase.get(Member_Info_TestKeys.Sex);
    Date birthday = date.parse(normalcase.get(Member_Info_TestKeys.Birthday));
    
    String filename = normalcase.get(Member_Info_TestKeys.Face_Photo);
    byte[] face_photo = new byte[0];
    try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/form/" + filename).getInputStream();){
      face_photo = IOUtils.toByteArray(in);
    }
    
    Date join_date = date.parse(normalcase.get(Member_Info_TestKeys.Join_Date));
    Date ret_date = date.parse(normalcase.get(Member_Info_TestKeys.Ret_Date));
    String email = normalcase.get(Member_Info_TestKeys.Email);
    String tel = normalcase.get(Member_Info_TestKeys.Tel);
    String addr_postcode = normalcase.get(Member_Info_TestKeys.Addr_Postcode);
    String addr = normalcase.get(Member_Info_TestKeys.Addr);
    String position = normalcase.get(Member_Info_TestKeys.Position);
    Date position_arri_date = date.parse(normalcase.get(Member_Info_TestKeys.Position_Arri_Date));
    String job = normalcase.get(Member_Info_TestKeys.Job);
    String assign_dept = normalcase.get(Member_Info_TestKeys.Assign_Dept);
    Date assign_date = date.parse(normalcase.get(Member_Info_TestKeys.Assign_Date));
    String inst_charge = normalcase.get(Member_Info_TestKeys.Inst_Charge);
    String other_comment = normalcase.get(Member_Info_TestKeys.Other_Comment);
    
    MockMultipartFile multipart = new MockMultipartFile("normal.png", "normal.png", "image/png", face_photo);
    
    Member_Info_Form mock_form = mock(Member_Info_Form.class);
    when(mock_form.getSerial_num()).thenReturn(serial_num);
    when(mock_form.getMember_id()).thenReturn(member_id);
    when(mock_form.getName()).thenReturn(name);
    when(mock_form.getName_pronu()).thenReturn(name_pronu);
    when(mock_form.getSex()).thenReturn(sex);
    when(mock_form.getBirthday()).thenReturn(birthday);
    when(mock_form.getFace_photo()).thenReturn(multipart);
    when(mock_form.getJoin_date()).thenReturn(join_date);
    when(mock_form.getRet_date()).thenReturn(ret_date);
    when(mock_form.getEmail_1()).thenReturn(email);
    when(mock_form.getEmail_2()).thenReturn(email);
    when(mock_form.getTel_1()).thenReturn(tel);
    when(mock_form.getTel_2()).thenReturn(tel);
    when(mock_form.getAddr_postcode()).thenReturn(addr_postcode);
    when(mock_form.getAddr()).thenReturn(addr);
    when(mock_form.getPosition()).thenReturn(position);
    when(mock_form.getPosition_arri_date()).thenReturn(position_arri_date);
    when(mock_form.getJob()).thenReturn(job);
    when(mock_form.getAssign_dept()).thenReturn(assign_dept);
    when(mock_form.getAssign_date()).thenReturn(assign_date);
    when(mock_form.getInst_charge()).thenReturn(inst_charge);
    when(mock_form.getOther_comment()).thenReturn(other_comment);

    Member_Info entity = new Member_Info(mock_form);

    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getMember_id()).isEqualTo(member_id);
    this.softly.assertThat(entity.getName()).isEqualTo(name);
    this.softly.assertThat(entity.getName_pronu()).isEqualTo(name_pronu);
    this.softly.assertThat(entity.getSex()).isEqualTo(sex);
    this.softly.assertThat(entity.getBirthday()).isEqualTo(birthday);
    this.softly.assertThat(entity.getFace_photo()).isEqualTo(face_photo);
    this.softly.assertThat(entity.getJoin_date()).isEqualTo(join_date);
    this.softly.assertThat(entity.getRet_date()).isEqualTo(ret_date);
    this.softly.assertThat(entity.getEmail_1()).isEqualTo(email);
    this.softly.assertThat(entity.getEmail_2()).isEqualTo(email);
    this.softly.assertThat(entity.getTel_1()).isEqualTo(tel);
    this.softly.assertThat(entity.getTel_2()).isEqualTo(tel);
    this.softly.assertThat(entity.getAddr_postcode()).isEqualTo(addr_postcode);
    this.softly.assertThat(entity.getAddr()).isEqualTo(addr);
    this.softly.assertThat(entity.getPosition()).isEqualTo(position);
    this.softly.assertThat(entity.getPosition_arri_date()).isEqualTo(position_arri_date);
    this.softly.assertThat(entity.getJob()).isEqualTo(job);
    this.softly.assertThat(entity.getAssign_dept()).isEqualTo(assign_dept);
    this.softly.assertThat(entity.getAssign_date()).isEqualTo(assign_date);
    this.softly.assertThat(entity.getInst_charge()).isEqualTo(inst_charge);
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
   * 
   * @throw IOException
   ****************************************************************************************
   */ 
  @ParameterizedTest
  @ValueSource(strings = {"", " ", "　", "\t", "\n"})
  @Override
  public void フォームクラスの初期化対象文字のNull初期化確認(String init_str) throws IOException{
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
