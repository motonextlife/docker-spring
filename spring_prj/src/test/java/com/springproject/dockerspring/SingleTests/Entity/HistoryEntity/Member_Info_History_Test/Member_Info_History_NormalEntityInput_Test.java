/** 
 **************************************************************************************
 * @file Member_Info_History_NormalEntityInput_Test.java
 * 
 * @brief  [団員情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Member_Info_History_Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_NormalEntityInput_Test;

import lombok.RequiredArgsConstructor;







/** 
 **************************************************************************************
 * @brief  [団員情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラス
 * 
 * 
 * @details 
 * - 検査対象のクラス名は[Member_Info_History]である。
 * - テスト内容としては、入力が終わったデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Member_Info_History
 * @see Entity_NormalEntityInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_History_NormalEntityInput_Test implements Entity_NormalEntityInput_Test{

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

    Member_Info mock_entity = mock(Member_Info.class);
    when(mock_entity.getSerial_num()).thenReturn(null);
    when(mock_entity.getMember_id()).thenReturn(str);
    when(mock_entity.getName()).thenReturn(str);
    when(mock_entity.getName_pronu()).thenReturn(str);
    when(mock_entity.getSex()).thenReturn(str);
    when(mock_entity.getEmail_1()).thenReturn(str);
    when(mock_entity.getEmail_2()).thenReturn(str);
    when(mock_entity.getTel_1()).thenReturn(str);
    when(mock_entity.getTel_2()).thenReturn(str);
    when(mock_entity.getAddr_postcode()).thenReturn(str);
    when(mock_entity.getAddr()).thenReturn(str);
    when(mock_entity.getPosition()).thenReturn(str);
    when(mock_entity.getJob()).thenReturn(str);
    when(mock_entity.getAssign_dept()).thenReturn(str);
    when(mock_entity.getInst_charge()).thenReturn(str);
    when(mock_entity.getOther_comment()).thenReturn(str);

    assertAllNull(new Member_Info_History(mock_entity, null, str, null));
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
  private void assertAllNull(Member_Info_History entity){
    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isNull();
    this.softly.assertThat(entity.getOperation_user()).isNull();
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
   * @throw IOException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した通常エンティティデータの同一性確認() throws ParseException, IOException{

    Map<Member_Info_TestKeys, String> normalcase = testcase.getNormalData();
    Map<History_TestKeys, String> historycase = testcase.getHistoryNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    Date change_datetime = datetime.parse(historycase.get(History_TestKeys.Change_Datetime));
    String change_kinds = historycase.get(History_TestKeys.Change_Kinds);
    String operation_user = historycase.get(History_TestKeys.Operation_User);
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
    
    Member_Info mockentity = mock(Member_Info.class);
    when(mockentity.getSerial_num()).thenReturn(serial_num);
    when(mockentity.getMember_id()).thenReturn(member_id);
    when(mockentity.getName()).thenReturn(name);
    when(mockentity.getName_pronu()).thenReturn(name_pronu);
    when(mockentity.getSex()).thenReturn(sex);
    when(mockentity.getBirthday()).thenReturn(birthday);
    when(mockentity.getFace_photo()).thenReturn(face_photo);
    when(mockentity.getJoin_date()).thenReturn(join_date);
    when(mockentity.getRet_date()).thenReturn(ret_date);
    when(mockentity.getEmail_1()).thenReturn(email);
    when(mockentity.getEmail_2()).thenReturn(email);
    when(mockentity.getTel_1()).thenReturn(tel);
    when(mockentity.getTel_2()).thenReturn(tel);
    when(mockentity.getAddr_postcode()).thenReturn(addr_postcode);
    when(mockentity.getAddr()).thenReturn(addr);
    when(mockentity.getPosition()).thenReturn(position);
    when(mockentity.getPosition_arri_date()).thenReturn(position_arri_date);
    when(mockentity.getJob()).thenReturn(job);
    when(mockentity.getAssign_dept()).thenReturn(assign_dept);
    when(mockentity.getAssign_date()).thenReturn(assign_date);
    when(mockentity.getInst_charge()).thenReturn(inst_charge);
    when(mockentity.getOther_comment()).thenReturn(other_comment);

    Member_Info_History entity = new Member_Info_History(mockentity, History_Kind_Enum.INSERT, operation_user, change_datetime);

    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isEqualTo(change_datetime);
    this.softly.assertThat(entity.getChange_kinds()).isEqualTo(change_kinds);
    this.softly.assertThat(entity.getOperation_user()).isEqualTo(operation_user);
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

    Member_Info mock_entity = mock(Member_Info.class);
    Member_Info_History entity;

    History_Kind_Enum[] hist_enum = {History_Kind_Enum.INSERT, 
                                     History_Kind_Enum.UPDATE, 
                                     History_Kind_Enum.DELETE, 
                                     History_Kind_Enum.ROLLBACK};

    String[] hist_str = {"insert", "update", "delete", "rollback"};
    int size = hist_enum.length;

    for(int i = 0; i < size; i++){
      entity = new Member_Info_History(mock_entity, hist_enum[i], null, null);
      this.softly.assertThat(entity.getChange_kinds()).isEqualTo(hist_str[i]);
    }

    this.softly.assertAll();
  }
}
