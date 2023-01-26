/** 
 **************************************************************************************
 * @file Musical_Score_History_NormalEntityInput_Test.java
 * 
 * @brief  [楽譜情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.HistoryEntity.Musical_Score_History_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make.Musical_Score_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_NormalEntityInput_Test;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
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
 * @brief  [楽譜情報変更履歴]において、テスト対象履歴用エンティティに対応する通常エンティティを
 * 入力した際のテストを行うクラス
 * 
 * 
 * @details 
 * - 検査対象のクラス名は[Musical_Score_History]である。
 * - テスト内容としては、入力が終わったデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Musical_Score_History
 * @see Entity_NormalEntityInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Musical_Score_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_History_NormalEntityInput_Test implements Entity_NormalEntityInput_Test{

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

    Musical_Score mock_entity = mock(Musical_Score.class);
    when(mock_entity.getSerial_num()).thenReturn(null);
    when(mock_entity.getScore_id()).thenReturn(str);
    when(mock_entity.getSong_title()).thenReturn(str);
    when(mock_entity.getComposer()).thenReturn(str);
    when(mock_entity.getArranger()).thenReturn(str);
    when(mock_entity.getPublisher()).thenReturn(str);
    when(mock_entity.getStorage_loc()).thenReturn(str);
    when(mock_entity.getOther_comment()).thenReturn(str);

    assertAllNull(new Musical_Score_History(mock_entity, null, str, null));
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
  private void assertAllNull(Musical_Score_History entity){
    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isNull();
    this.softly.assertThat(entity.getOperation_user()).isNull();
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
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した通常エンティティデータの同一性確認() throws ParseException{

    Map<Musical_Score_TestKeys, String> normalcase = testcase.getNormalData();
    Map<History_TestKeys, String> historycase = testcase.getHistoryNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    Date change_datetime = datetime.parse(historycase.get(History_TestKeys.Change_Datetime));
    String change_kinds = historycase.get(History_TestKeys.Change_Kinds);
    String operation_user = historycase.get(History_TestKeys.Operation_User);
    Integer serial_num = Integer.parseInt(normalcase.get(Musical_Score_TestKeys.Serial_Num));
    String score_id = normalcase.get(Musical_Score_TestKeys.Score_Id);
    Date buy_date = date.parse(normalcase.get(Musical_Score_TestKeys.Buy_Date));
    String song_title = normalcase.get(Musical_Score_TestKeys.Song_Title);
    String composer = normalcase.get(Musical_Score_TestKeys.Composer);
    String arranger = normalcase.get(Musical_Score_TestKeys.Arranger);
    String publisher = normalcase.get(Musical_Score_TestKeys.Publisher);
    String storage_loc = normalcase.get(Musical_Score_TestKeys.Storage_Loc);
    Date disp_date = date.parse(normalcase.get(Musical_Score_TestKeys.Disp_Date));
    String other_comment = normalcase.get(Musical_Score_TestKeys.Other_Comment);
    
    Musical_Score mockentity = mock(Musical_Score.class);
    when(mockentity.getSerial_num()).thenReturn(serial_num);
    when(mockentity.getScore_id()).thenReturn(score_id);
    when(mockentity.getBuy_date()).thenReturn(buy_date);
    when(mockentity.getSong_title()).thenReturn(song_title);
    when(mockentity.getComposer()).thenReturn(composer);
    when(mockentity.getArranger()).thenReturn(arranger);
    when(mockentity.getPublisher()).thenReturn(publisher);
    when(mockentity.getStorage_loc()).thenReturn(storage_loc);
    when(mockentity.getDisp_date()).thenReturn(disp_date);
    when(mockentity.getOther_comment()).thenReturn(other_comment);

    Musical_Score_History entity = new Musical_Score_History(mockentity, 
                                                             History_Kind_Enum.INSERT, 
                                                             operation_user, 
                                                             change_datetime);

    this.softly.assertThat(entity.getHistory_id()).isNull();
    this.softly.assertThat(entity.getChange_datetime()).isEqualTo(change_datetime);
    this.softly.assertThat(entity.getChange_kinds()).isEqualTo(change_kinds);
    this.softly.assertThat(entity.getOperation_user()).isEqualTo(operation_user);
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

    Musical_Score mock_entity = mock(Musical_Score.class);
    Musical_Score_History entity;

    History_Kind_Enum[] hist_enum = {History_Kind_Enum.INSERT, 
                                     History_Kind_Enum.UPDATE, 
                                     History_Kind_Enum.DELETE, 
                                     History_Kind_Enum.ROLLBACK};

    String[] hist_str = {"insert", "update", "delete", "rollback"};
    int size = hist_enum.length;

    for(int i = 0; i < size; i++){
      entity = new Musical_Score_History(mock_entity, hist_enum[i], null, null);
      this.softly.assertThat(entity.getChange_kinds()).isEqualTo(hist_str[i]);
    }

    this.softly.assertAll();
  }
}
