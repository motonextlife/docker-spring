/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Member_Info_Test
 * 
 * @brief 通常用エンティティのうち、[団員管理]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、団員管理のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Member_Info_Test;





/** 
 **************************************************************************************
 * @file Member_Info_CsvMapInput_Test.java
 * 
 * @brief [団員管理]において、エンティティへのCSV入力テストをテストするクラスを格納する
 * ファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_CsvMapInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;





/** 
 **************************************************************************************
 * @brief [団員管理]において、エンティティへのCSV入力テストをテストするクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Member_Info
 * @see Entity_CsvMapInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_CsvMapInput_Test implements Entity_CsvMapInput_Test{

  private final Member_Info_TestCase_Make testcase;
  private final SoftAssertions softly = new SoftAssertions();



  




  /** 
   ****************************************************************************************
   * @brief エンティティ内に格納するCSVファイルから抽出したと想定されるマップリストを作成する。
   * 作成するマップリストは、Null初期化の対象となるデータだけを格納する。そのままテストに移行する。
   * 
   * @param[in] str 格納する初期化対象文字
   * 
   * @details
   * - このマップリストを対象のエンティティに渡すことで、正常に初期化処理が行われていることを確認する。
   * - ただし初期化対象は文字列型のデータのみなので、シリアルナンバー等の文字列型以外は何もせず、
   * そのままNullの状態にしておく。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  private void setInputCsvMap(String str) throws ParseException{
    Map<String, String> csv_map = new HashMap<>();

    csv_map.put("serial_num", str);
    csv_map.put("member_id", str);
    csv_map.put("name", str);
    csv_map.put("name_pronu", str);
    csv_map.put("sex", str);
    csv_map.put("email_1", str);
    csv_map.put("email_2", str);
    csv_map.put("tel_1", str);
    csv_map.put("tel_2", str);
    csv_map.put("addr_postcode", str);
    csv_map.put("addr", str);
    csv_map.put("position", str);
    csv_map.put("job", str);
    csv_map.put("assign_dept", str);
    csv_map.put("inst_charge", str);
    csv_map.put("other_comment", str);

    assertAllNull(new Member_Info(csv_map));
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
   * @brief 入力したCSVから抽出されているマップリストのデータが、あらかじめ用意しておいたテストケース
   * と同一であることを確認する。
   * 
   * @par 大まかな処理の流れ
   * - テストケースから、比較対象かつ格納するデータを取得する。
   * - 検査対象エンティティに投入するマップリストを、テストケースから作成する。
   * - 作成したマップリストを、検査対象エンティティに格納する。
   * - 検査対象エンティティからデータを取り出し、テストケースと値が同一か確認する。
   * 
   * @note 
   * - 日付データの扱いに関しては、通常では日付形式文字列は[yyyy-MM-dd]の形式だが、CSVからの
   * 抽出データは[yyyyMMdd]となっているため、その仕様に従い日付データを変換する。
   * - そのため、テストケースに含まれている日付文字列はハイフン形式の為、ハイフンを取り除いたうえで
   * 使用する事。
   * - また、CSVからのデータは全て新規追加扱いとなるため、何があってもシリアルナンバーは「Null」と
   * なる。
   * - 性別に関しては漢字形式で来るので変換してテストする。また、画像データは存在しないので「Null」と
   * なる。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力したCSVマップデータの同一性確認() throws ParseException{

    Map<Member_Info_TestKeys, String> normalcase = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

    String serial_num = normalcase.get(Member_Info_TestKeys.Serial_Num);
    String member_id = normalcase.get(Member_Info_TestKeys.Member_Id);
    String name = normalcase.get(Member_Info_TestKeys.Name);
    String name_pronu = normalcase.get(Member_Info_TestKeys.Name_Pronu);
    String sex = normalcase.get(Member_Info_TestKeys.Sex);
    String birthday = normalcase.get(Member_Info_TestKeys.Birthday).replace("-", "");
    String join_date = normalcase.get(Member_Info_TestKeys.Join_Date).replace("-", "");
    String ret_date = normalcase.get(Member_Info_TestKeys.Ret_Date).replace("-", "");
    String email = normalcase.get(Member_Info_TestKeys.Email);
    String tel = normalcase.get(Member_Info_TestKeys.Tel);
    String addr_postcode = normalcase.get(Member_Info_TestKeys.Addr_Postcode);
    String addr = normalcase.get(Member_Info_TestKeys.Addr);
    String position = normalcase.get(Member_Info_TestKeys.Position);
    String position_arri_date = normalcase.get(Member_Info_TestKeys.Position_Arri_Date).replace("-", "");
    String job = normalcase.get(Member_Info_TestKeys.Job);
    String assign_dept = normalcase.get(Member_Info_TestKeys.Assign_Dept);
    String assign_date = normalcase.get(Member_Info_TestKeys.Assign_Date).replace("-", "");
    String inst_charge = normalcase.get(Member_Info_TestKeys.Inst_Charge);
    String other_comment = normalcase.get(Member_Info_TestKeys.Other_Comment);

    Map<String, String> csv_map = new HashMap<>();
    csv_map.put("serial_num", serial_num);
    csv_map.put("member_id", member_id);
    csv_map.put("name", name);
    csv_map.put("name_pronu", name_pronu);

    if(sex.equals("male")){
      csv_map.put("sex", "男");
    }else if(sex.equals("female")){
      csv_map.put("sex", "女");
    }

    csv_map.put("birthday", birthday);
    csv_map.put("face_photo", "blankdata");
    csv_map.put("join_date", join_date);
    csv_map.put("ret_date", ret_date);
    csv_map.put("email_1", email);
    csv_map.put("email_2", email);
    csv_map.put("tel_1", tel);
    csv_map.put("tel_2", tel);
    csv_map.put("addr_postcode", addr_postcode);
    csv_map.put("addr", addr);
    csv_map.put("position", position);
    csv_map.put("position_arri_date", position_arri_date);
    csv_map.put("job", job);
    csv_map.put("assign_dept", assign_dept);
    csv_map.put("assign_date", assign_date);
    csv_map.put("inst_charge", inst_charge);
    csv_map.put("other_comment", other_comment);

    Member_Info entity = new Member_Info(csv_map);

    this.softly.assertThat(entity.getSerial_num()).isNull(); 
    this.softly.assertThat(entity.getMember_id()).isEqualTo(member_id);
    this.softly.assertThat(entity.getName()).isEqualTo(name);
    this.softly.assertThat(entity.getName_pronu()).isEqualTo(name_pronu);
    this.softly.assertThat(entity.getSex()).isEqualTo(sex);
    this.softly.assertThat(entity.getBirthday()).isEqualTo(date.parse(birthday));
    this.softly.assertThat(entity.getFace_photo()).isNull();
    this.softly.assertThat(entity.getJoin_date()).isEqualTo(date.parse(join_date));
    this.softly.assertThat(entity.getRet_date()).isEqualTo(date.parse(ret_date));
    this.softly.assertThat(entity.getEmail_1()).isEqualTo(email);
    this.softly.assertThat(entity.getEmail_2()).isEqualTo(email);
    this.softly.assertThat(entity.getTel_1()).isEqualTo(tel);
    this.softly.assertThat(entity.getTel_2()).isEqualTo(tel);
    this.softly.assertThat(entity.getAddr_postcode()).isEqualTo(addr_postcode);
    this.softly.assertThat(entity.getAddr()).isEqualTo(addr);
    this.softly.assertThat(entity.getPosition()).isEqualTo(position);
    this.softly.assertThat(entity.getPosition_arri_date()).isEqualTo(date.parse(position_arri_date));
    this.softly.assertThat(entity.getJob()).isEqualTo(job);
    this.softly.assertThat(entity.getAssign_dept()).isEqualTo(assign_dept);
    this.softly.assertThat(entity.getAssign_date()).isEqualTo(date.parse(assign_date));
    this.softly.assertThat(entity.getInst_charge()).isEqualTo(inst_charge);
    this.softly.assertThat(entity.getOther_comment()).isEqualTo(other_comment);
    this.softly.assertAll();
  }








  /** 
   ****************************************************************************************
   * @brief 出力しようとしたデータに初期化対象文字が含まれていた場合、入力後に取り出した該当データ
   * がNullに初期化されていることを確認する。
   * 
   * @details 初期化対象文字は以下の物である。
   * - 空文字("")
   * - 半角空白(" ")
   * - 全角空白("　")
   * - タブ空白("\t")
   * - 改行空白("\n")
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @ParameterizedTest
  @ValueSource(strings = {"", " ", "　", "\t", "\n"})
  @Override
  public void CSVの初期化対象文字のNull初期化確認(String init_str) throws ParseException{
    setInputCsvMap(init_str);
    this.softly.assertAll();
  }








  /** 
   ****************************************************************************************
   * @brief 入力したデータに[Null]が含まれていた場合でも、処理中に[NullPointerException]が発生して
   * 処理が止まらないことを確認する。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void CSVのNull入力時のNullPointerException未発生確認(){
    assertDoesNotThrow(() -> setInputCsvMap(null));
  }






  /** 
   ****************************************************************************************
   * @brief 入力したデータに[変換ができない形式の日付文字列]が含まれていた場合、処理中に
   * [ParseException]が発生して処理が止まることを確認する。
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @ParameterizedTest
  @ValueSource(strings = {"birthday", "join_date", "ret_date", "position_arri_date", "assign_date"})
  @Override
  public void CSVの日付文字列不正時のParseException発生確認(String key_name){

    Map<String, String> csv_map = new HashMap<>();

    csv_map.put(key_name, "1996/02/19");
    this.softly.assertThatThrownBy(() -> new Member_Info(csv_map)).isInstanceOf(ParseException.class);

    this.softly.assertAll();
  }
}
