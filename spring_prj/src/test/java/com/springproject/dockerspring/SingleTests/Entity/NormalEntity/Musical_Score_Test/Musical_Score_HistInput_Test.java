/** 
 **************************************************************************************
 * @file Musical_Score_HistInput_Test.java
 * 
 * @brief [楽譜管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した
 * 際のテストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Musical_Score_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make.Musical_Score_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_HistInput_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [楽譜管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Musical_Score]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Musical_Score
 * @see Entity_HistInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Musical_Score_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_HistInput_Test implements Entity_HistInput_Test{

  private final Musical_Score_TestCase_Make testcase;
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

    Musical_Score_History mock_hist = mock(Musical_Score_History.class);
    when(mock_hist.getSerial_num()).thenReturn(null);
    when(mock_hist.getScore_id()).thenReturn(str);
    when(mock_hist.getSong_title()).thenReturn(str);
    when(mock_hist.getComposer()).thenReturn(str);
    when(mock_hist.getArranger()).thenReturn(str);
    when(mock_hist.getPublisher()).thenReturn(str);
    when(mock_hist.getStorage_loc()).thenReturn(str);
    when(mock_hist.getOther_comment()).thenReturn(str);

    assertAllNull(new Musical_Score(mock_hist));
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
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した履歴用エンティティデータの同一性確認() throws ParseException{

    Map<Musical_Score_TestKeys, String> normal_case = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    Integer serial_num = Integer.parseInt(normal_case.get(Musical_Score_TestKeys.Serial_Num));
    String score_id = normal_case.get(Musical_Score_TestKeys.Score_Id);
    Date buy_date = date.parse(normal_case.get(Musical_Score_TestKeys.Buy_Date));
    String song_title = normal_case.get(Musical_Score_TestKeys.Song_Title);
    String composer = normal_case.get(Musical_Score_TestKeys.Composer);
    String arranger = normal_case.get(Musical_Score_TestKeys.Arranger);
    String publisher = normal_case.get(Musical_Score_TestKeys.Publisher);
    String storage_loc = normal_case.get(Musical_Score_TestKeys.Storage_Loc);
    Date disp_date = date.parse(normal_case.get(Musical_Score_TestKeys.Disp_Date));
    String other_comment = normal_case.get(Musical_Score_TestKeys.Other_Comment);
    
    Musical_Score_History mock_hist = mock(Musical_Score_History.class);
    when(mock_hist.getSerial_num()).thenReturn(serial_num);
    when(mock_hist.getScore_id()).thenReturn(score_id);
    when(mock_hist.getBuy_date()).thenReturn(buy_date);
    when(mock_hist.getSong_title()).thenReturn(song_title);
    when(mock_hist.getComposer()).thenReturn(composer);
    when(mock_hist.getArranger()).thenReturn(arranger);
    when(mock_hist.getPublisher()).thenReturn(publisher);
    when(mock_hist.getStorage_loc()).thenReturn(storage_loc);
    when(mock_hist.getDisp_date()).thenReturn(disp_date);
    when(mock_hist.getOther_comment()).thenReturn(other_comment);

    Musical_Score entity = new Musical_Score(mock_hist);

    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getScore_id()).isEqualTo(score_id);
    this.softly.assertThat(entity.getBuy_date()).isEqualTo(buy_date);
    this.softly.assertThat(entity.getSong_title()).isEqualTo(song_title);
    this.softly.assertThat(entity.getComposer()).isEqualTo(composer);
    this.softly.assertThat(entity.getArranger()).isEqualTo(arranger);
    this.softly.assertThat(entity.getPublisher()).isEqualTo(publisher);
    this.softly.assertThat(entity.getStorage_loc()).isEqualTo(storage_loc);
    this.softly.assertThat(entity.getDisp_date()).isEqualTo(disp_date);
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
