/** 
 **************************************************************************************
 * @file Member_Info_HistInput_Test.java
 * 
 * @brief [団員管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した際の
 * テストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Member_Info_Test;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_HistInput_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [団員管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Member_Info
 * @see Entity_HistInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_HistInput_Test implements Entity_HistInput_Test{

  private final Member_Info_TestCase_Make testcase;
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

    Member_Info_History depend_hist = new Member_Info_History();
    depend_hist.setSerial_num(null);
    depend_hist.setMember_id(str);
    depend_hist.setName(str);
    depend_hist.setName_pronu(str);
    depend_hist.setSex(str);
    depend_hist.setEmail_1(str);
    depend_hist.setEmail_2(str);
    depend_hist.setTel_1(str);
    depend_hist.setTel_2(str);
    depend_hist.setAddr_postcode(str);
    depend_hist.setAddr(str);
    depend_hist.setPosition(str);
    depend_hist.setJob(str);
    depend_hist.setAssign_dept(str);
    depend_hist.setInst_charge(str);
    depend_hist.setOther_comment(str);

    assertAllNull(new Member_Info(depend_hist));
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
   * @brief 入力した履歴用エンティティからデータを取り出して通常エンティティに格納したデータが、
   * あらかじめ用意しておいたテストケースと同一であることを確認する。
   * 
   * @par 大まかな処理の流れ
   * - テストケースから、比較対象かつ格納するデータを取得する。
   * - 投入するための履歴用エンティティのモックを作成し、その中にテストケースを格納する。
   * - 検査対象エンティティのインスタンスを作成し、作成したモックを投入する。
   * - 検査対象エンティティからデータを取り出し、想定されたテストケースの値と同一か確認する。
   * 
   * @throw ParseException
   * @throw IOException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した履歴用エンティティデータの同一性確認() throws ParseException, IOException{

    Map<Member_Info_TestKeys, String> normal_case = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    Integer serial_num = Integer.parseInt(normal_case.get(Member_Info_TestKeys.Serial_Num));
    String member_id = normal_case.get(Member_Info_TestKeys.Member_Id);
    String name = normal_case.get(Member_Info_TestKeys.Name);
    String name_pronu = normal_case.get(Member_Info_TestKeys.Name_Pronu);
    String sex = normal_case.get(Member_Info_TestKeys.Sex);
    Date birthday = date.parse(normal_case.get(Member_Info_TestKeys.Birthday));
    
    String filename = normal_case.get(Member_Info_TestKeys.Face_Photo);
    byte[] face_photo = new byte[0];
    try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/form/" + filename).getInputStream();){
      face_photo = IOUtils.toByteArray(in);
    }
    
    Date join_date = date.parse(normal_case.get(Member_Info_TestKeys.Join_Date));
    Date ret_date = date.parse(normal_case.get(Member_Info_TestKeys.Ret_Date));
    String email = normal_case.get(Member_Info_TestKeys.Email);
    String tel = normal_case.get(Member_Info_TestKeys.Tel);
    String addr_postcode = normal_case.get(Member_Info_TestKeys.Addr_Postcode);
    String addr = normal_case.get(Member_Info_TestKeys.Addr);
    String position = normal_case.get(Member_Info_TestKeys.Position);
    Date position_arri_date = date.parse(normal_case.get(Member_Info_TestKeys.Position_Arri_Date));
    String job = normal_case.get(Member_Info_TestKeys.Job);
    String assign_dept = normal_case.get(Member_Info_TestKeys.Assign_Dept);
    Date assign_date = date.parse(normal_case.get(Member_Info_TestKeys.Assign_Date));
    String inst_charge = normal_case.get(Member_Info_TestKeys.Inst_Charge);
    String other_comment = normal_case.get(Member_Info_TestKeys.Other_Comment);
    
    Member_Info_History depend_hist = new Member_Info_History();
    depend_hist.setSerial_num(serial_num);
    depend_hist.setMember_id(member_id);
    depend_hist.setName(name);
    depend_hist.setName_pronu(name_pronu);
    depend_hist.setSex(sex);
    depend_hist.setBirthday(birthday);
    depend_hist.setFace_photo(face_photo);
    depend_hist.setJoin_date(join_date);
    depend_hist.setRet_date(ret_date);
    depend_hist.setEmail_1(email);
    depend_hist.setEmail_2(email);
    depend_hist.setTel_1(tel);
    depend_hist.setTel_2(tel);
    depend_hist.setAddr_postcode(addr_postcode);
    depend_hist.setAddr(addr);
    depend_hist.setPosition(position);
    depend_hist.setPosition_arri_date(position_arri_date);
    depend_hist.setJob(job);
    depend_hist.setAssign_dept(assign_dept);
    depend_hist.setAssign_date(assign_date);
    depend_hist.setInst_charge(inst_charge);
    depend_hist.setOther_comment(other_comment);

    Member_Info entity = new Member_Info(depend_hist);

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
   * @brief が含まれていた場合、入力後に取り出した該当データがNullに
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
  public void 履歴用エンティティの初期化対象文字のNull初期化確認(String init_str){
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
  public void 履歴用エンティティのNull入力時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInitTargetEntity(null));
  }
}
