/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Musical_Score_Test
 * 
 * @brief 通常用エンティティのうち、[楽譜管理]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、楽譜管理のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Musical_Score_Test;





/** 
 **************************************************************************************
 * @file Musical_Score_CsvMapInput_Test.java
 * 
 * @brief [楽譜管理]において、エンティティへのCSV入力テストをテストするクラスを格納する
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

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make.Musical_Score_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_CsvMapInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;

import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;







/** 
 **************************************************************************************
 * @brief [楽譜管理]において、エンティティへのCSV入力テストをテストするクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Musical_Score]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Musical_Score
 * @see Entity_CsvMapInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Musical_Score_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_CsvMapInput_Test implements Entity_CsvMapInput_Test{

  private final Musical_Score_TestCase_Make testcase;
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
    csv_map.put("score_id", str);
    csv_map.put("song_title", str);
    csv_map.put("composer", str);
    csv_map.put("arranger", str);
    csv_map.put("publisher", str);
    csv_map.put("storage_loc", str);
    csv_map.put("other_comment", str);

    assertAllNull(new Musical_Score(csv_map));
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
  private void assertAllNull(Musical_Score entity){
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getScore_id()).isNull();
    this.softly.assertThat(entity.getBuy_date()).isNull();
    this.softly.assertThat(entity.getSong_title()).isNull();
    this.softly.assertThat(entity.getComposer()).isNull();
    this.softly.assertThat(entity.getArranger()).isNull();
    this.softly.assertThat(entity.getPublisher()).isNull();
    this.softly.assertThat(entity.getStorage_loc()).isNull();
    this.softly.assertThat(entity.getDisp_date()).isNull();
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
   * 
   * @throw ParseException
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力したCSVマップデータの同一性確認() throws ParseException{

    Map<Musical_Score_TestKeys, String> normalcase = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");

    String serial_num = normalcase.get(Musical_Score_TestKeys.Serial_Num);
    String score_id = normalcase.get(Musical_Score_TestKeys.Score_Id);
    String buy_date = normalcase.get(Musical_Score_TestKeys.Buy_Date).replace("-", "");
    String song_title = normalcase.get(Musical_Score_TestKeys.Song_Title);
    String composer = normalcase.get(Musical_Score_TestKeys.Composer);
    String arranger = normalcase.get(Musical_Score_TestKeys.Arranger);
    String publisher = normalcase.get(Musical_Score_TestKeys.Publisher);
    String storage_loc = normalcase.get(Musical_Score_TestKeys.Storage_Loc);
    String disp_date = normalcase.get(Musical_Score_TestKeys.Disp_Date).replace("-", "");
    String other_comment = normalcase.get(Musical_Score_TestKeys.Other_Comment);
    
    Map<String, String> csv_map = new HashMap<>();
    csv_map.put("serial_num", serial_num);
    csv_map.put("score_id", score_id);
    csv_map.put("buy_date", buy_date);
    csv_map.put("song_title", song_title);
    csv_map.put("composer", composer);
    csv_map.put("arranger", arranger);
    csv_map.put("publisher", publisher);
    csv_map.put("storage_loc", storage_loc);
    csv_map.put("disp_date", disp_date);
    csv_map.put("other_comment", other_comment);

    Musical_Score entity = new Musical_Score(csv_map);

    assertThat(entity.getSerial_num()).isNull();
    assertThat(entity.getScore_id()).isEqualTo(score_id);
    assertThat(entity.getBuy_date()).isEqualTo(date.parse(buy_date));
    assertThat(entity.getSong_title()).isEqualTo(song_title);
    assertThat(entity.getComposer()).isEqualTo(composer);
    assertThat(entity.getArranger()).isEqualTo(arranger);
    assertThat(entity.getPublisher()).isEqualTo(publisher);
    assertThat(entity.getStorage_loc()).isEqualTo(storage_loc);
    assertThat(entity.getDisp_date()).isEqualTo(date.parse(disp_date));
    assertThat(entity.getOther_comment()).isEqualTo(other_comment);
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
  @ValueSource(strings = {"buy_date", "disp_date"})
  @Override
  public void CSVの日付文字列不正時のParseException発生確認(String key_name){

    Map<String, String> csv_map = new HashMap<>();

    csv_map.put(key_name, "1996/02/19");
    this.softly.assertThatThrownBy(() -> new Musical_Score(csv_map)).isInstanceOf(ParseException.class);

    this.softly.assertAll();
  }
}
