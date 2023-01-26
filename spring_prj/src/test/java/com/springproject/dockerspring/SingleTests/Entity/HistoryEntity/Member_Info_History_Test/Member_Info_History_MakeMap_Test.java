/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Member_Info_History_Test
 * 
 * @brief 履歴用エンティティのうち、[団員情報変更履歴]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、団員情報変更履歴のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Member_Info_History_Test;





/** 
 **************************************************************************************
 * @file Member_Info_History_MakeMap_Test.java
 * 
 * @brief  [団員情報変更履歴]において、エンティティ内部のデータを画面出力用に文字列に変換して
 * 出力するメソッドをテストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_MakeMap_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;







/** 
 **************************************************************************************
 * @brief  [団員情報変更履歴]において、エンティティ内部のデータを画面出力用に文字列に変換して
 * 出力するメソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info_History]で、検査対象メソッドは[makeMap]である。
 * - テスト内容としては、出力されたデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Member_Info_History
 * @see Entity_MakeMap_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_History_MakeMap_Test implements Entity_MakeMap_Test{

  private final Member_Info_TestCase_Make testcase;
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
   ****************************************************************************************
   */ 
  private Map<String, String> setInitCompareMap(){
    Map<String, String> init_map = new HashMap<>();

    init_map.put("history_id", "");
    init_map.put("change_datetime", "");
    init_map.put("change_kinds", "");
    init_map.put("operation_user", "");
    init_map.put("serial_num", "");
    init_map.put("member_id", "");
    init_map.put("name", "");
    init_map.put("name_pronu", "");
    init_map.put("sex", "");
    init_map.put("birthday", "");
    init_map.put("face_photo", "");
    init_map.put("join_date", "");
    init_map.put("ret_date", "");
    init_map.put("email_1", "");
    init_map.put("email_2", "");
    init_map.put("tel_1", "");
    init_map.put("tel_2", "");
    init_map.put("addr_postcode", "");
    init_map.put("addr", "");
    init_map.put("position", "");
    init_map.put("position_arri_date", "");
    init_map.put("job", "");
    init_map.put("assign_dept", "");
    init_map.put("assign_date", "");
    init_map.put("inst_charge", "");
    init_map.put("other_comment", "");

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

    Member_Info_History entity = new Member_Info_History();
    entity.setChange_kinds(str);
    entity.setOperation_user(str);
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
   * 
   * @throw ParseException
   * @throw IOException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した文字列マップリストデータの同一性確認() throws ParseException, IOException{

    Map<Member_Info_TestKeys, String> normal_case = testcase.getNormalData();
    Map<History_TestKeys, String> history_case = testcase.getHistoryNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String history_id = history_case.get(History_TestKeys.History_Id);
    String change_datetime = history_case.get(History_TestKeys.Change_Datetime);
    String change_kinds = history_case.get(History_TestKeys.Change_Kinds);
    String operation_user = history_case.get(History_TestKeys.Operation_User);
    String serial_num = normal_case.get(Member_Info_TestKeys.Serial_Num);
    String member_id = normal_case.get(Member_Info_TestKeys.Member_Id);
    String name = normal_case.get(Member_Info_TestKeys.Name);
    String name_pronu = normal_case.get(Member_Info_TestKeys.Name_Pronu);
    String sex = normal_case.get(Member_Info_TestKeys.Sex);
    String birthday = normal_case.get(Member_Info_TestKeys.Birthday);

    String filename = normal_case.get(Member_Info_TestKeys.Face_Photo);
    byte[] face_photo = new byte[0];
    try(InputStream in = new ClassPathResource("TestCaseFile/MemberInfo/BlobTestCaseData/form/" + filename).getInputStream();){
      face_photo = IOUtils.toByteArray(in);
    }

    String join_date = normal_case.get(Member_Info_TestKeys.Join_Date);
    String ret_date = normal_case.get(Member_Info_TestKeys.Ret_Date);
    String email = normal_case.get(Member_Info_TestKeys.Email);
    String tel = normal_case.get(Member_Info_TestKeys.Tel);
    String addr_postcode = normal_case.get(Member_Info_TestKeys.Addr_Postcode);
    String addr = normal_case.get(Member_Info_TestKeys.Addr);
    String position = normal_case.get(Member_Info_TestKeys.Position);
    String position_arri_date = normal_case.get(Member_Info_TestKeys.Position_Arri_Date);
    String job = normal_case.get(Member_Info_TestKeys.Job);
    String assign_dept = normal_case.get(Member_Info_TestKeys.Assign_Dept);
    String assign_date = normal_case.get(Member_Info_TestKeys.Assign_Date);
    String inst_charge = normal_case.get(Member_Info_TestKeys.Inst_Charge);
    String other_comment = normal_case.get(Member_Info_TestKeys.Other_Comment);

    Member_Info_History entity = new Member_Info_History();
    entity.setHistory_id(Integer.parseInt(history_id));
    entity.setChange_datetime(datetime.parse(change_datetime));
    entity.setChange_kinds(change_kinds);
    entity.setOperation_user(operation_user);
    entity.setSerial_num(Integer.parseInt(serial_num));
    entity.setMember_id(member_id);
    entity.setName(name);
    entity.setName_pronu(name_pronu);
    entity.setSex(sex);
    entity.setBirthday(date.parse(birthday));
    entity.setFace_photo(face_photo);
    entity.setJoin_date(date.parse(join_date));
    entity.setRet_date(date.parse(ret_date));
    entity.setEmail_1(email);
    entity.setEmail_2(email);
    entity.setTel_1(tel);
    entity.setTel_2(tel);
    entity.setAddr_postcode(addr_postcode);
    entity.setAddr(addr);
    entity.setPosition(position);
    entity.setPosition_arri_date(date.parse(position_arri_date));
    entity.setJob(job);
    entity.setAssign_dept(assign_dept);
    entity.setAssign_date(date.parse(assign_date));
    entity.setInst_charge(inst_charge);
    entity.setOther_comment(other_comment);

    Map<String, String> compare_map = new HashMap<>();
    String base64_str = Base64.getEncoder().encodeToString(face_photo);
    base64_str =  "data:image/png;base64," + base64_str;
    
    compare_map.put("history_id", history_id);
    compare_map.put("change_datetime", change_datetime);
    compare_map.put("change_kinds", change_kinds);
    compare_map.put("operation_user", operation_user);
    compare_map.put("serial_num", serial_num);
    compare_map.put("member_id", member_id);
    compare_map.put("name", name);
    compare_map.put("name_pronu", name_pronu);
    compare_map.put("sex", sex);
    compare_map.put("birthday", birthday);
    compare_map.put("face_photo", base64_str);
    compare_map.put("join_date", join_date);
    compare_map.put("ret_date", ret_date);
    compare_map.put("email_1", email);
    compare_map.put("email_2", email);
    compare_map.put("tel_1", tel);
    compare_map.put("tel_2", tel);
    compare_map.put("addr_postcode", addr_postcode);
    compare_map.put("addr", addr);
    compare_map.put("position", position);
    compare_map.put("position_arri_date", position_arri_date);
    compare_map.put("job", job);
    compare_map.put("assign_dept", assign_dept);
    compare_map.put("assign_date", assign_date);
    compare_map.put("inst_charge", inst_charge);
    compare_map.put("other_comment", other_comment);

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
