/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Facility_Photo_History_Test
 * 
 * @brief 履歴用エンティティのうち、[設備写真データ情報変更履歴]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、設備写真データ情報変更履歴のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Facility_Photo_History_Test;





/** 
 **************************************************************************************
 * @file Facility_Photo_History_MakeMap_Test.java
 * 
 * @brief  [設備写真データ変更履歴]において、エンティティ内部のデータを画面出力用に文字列に
 * 変換して出力するメソッドをテストするクラスを格納するファイルである。
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

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make.Facility_Photo_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_MakeMap_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_Photo_History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;







/** 
 **************************************************************************************
 * @brief  [設備写真データ変更履歴]において、エンティティ内部のデータを画面出力用に文字列に
 * 変換して出力するメソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Facility_Photo_History]で、検査対象メソッドは[makeMap]である。
 * - テスト内容としては、出力されたデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Facility_Photo_History
 * @see Entity_MakeMap_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Facility_Photo_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_Photo_History_MakeMap_Test implements Entity_MakeMap_Test{

  private final Facility_Photo_TestCase_Make testcase;
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
   * - データのハッシュ値に関しては出力対象外の為除外する。
   ****************************************************************************************
   */ 
  private Map<String, String> setInitCompareMap(){
    Map<String, String> init_map = new HashMap<>();

    init_map.put("history_id", "");
    init_map.put("change_datetime", "");
    init_map.put("change_kinds", "");
    init_map.put("operation_user", "");
    init_map.put("serial_num", "");
    init_map.put("faci_id", "");
    init_map.put("photo_name", "");

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
   * - データのハッシュ値に関しては出力対象外の為除外する。
   ****************************************************************************************
   */ 
  private void setInitTargetEntity(String str){

    Facility_Photo_History entity = new Facility_Photo_History();
    entity.setChange_kinds(str);
    entity.setOperation_user(str);
    entity.setFaci_id(str);
    entity.setPhoto_name(str);

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
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した文字列マップリストデータの同一性確認() throws ParseException{

    Map<Facility_Photo_TestKeys, String> normal_case = testcase.getNormalData();
    Map<History_TestKeys, String> history_case = testcase.getHistoryNormalData();
    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String history_id = history_case.get(History_TestKeys.History_Id);
    String change_datetime = history_case.get(History_TestKeys.Change_Datetime);
    String change_kinds = history_case.get(History_TestKeys.Change_Kinds);
    String operation_user = history_case.get(History_TestKeys.Operation_User);
    String serial_num = normal_case.get(Facility_Photo_TestKeys.Serial_Num);
    String faci_id = normal_case.get(Facility_Photo_TestKeys.Faci_Id);
    String photo_name = normal_case.get(Facility_Photo_TestKeys.Photo_Name);

    Facility_Photo_History entity = new Facility_Photo_History();
    entity.setHistory_id(Integer.parseInt(history_id));
    entity.setChange_datetime(datetime.parse(change_datetime));
    entity.setChange_kinds(change_kinds);
    entity.setOperation_user(operation_user);
    entity.setSerial_num(Integer.parseInt(serial_num));
    entity.setFaci_id(faci_id);
    entity.setPhoto_name(photo_name);

    Map<String, String> compare_map = new HashMap<>();
    compare_map.put("history_id", history_id);
    compare_map.put("change_datetime", change_datetime);
    compare_map.put("change_kinds", change_kinds);
    compare_map.put("operation_user", operation_user);
    compare_map.put("serial_num", serial_num);
    compare_map.put("faci_id", faci_id);
    compare_map.put("photo_name", photo_name);

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
