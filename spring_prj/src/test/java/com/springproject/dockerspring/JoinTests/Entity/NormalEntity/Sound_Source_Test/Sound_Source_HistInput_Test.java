/** 
 **************************************************************************************
 * @file Sound_Source_HistInput_Test.java
 * 
 * @brief [音源管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した際の
 * テストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Sound_Source_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_HistInput_Test;
import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 **************************************************************************************
 * @brief [音源管理]において、対象の通常エンティティに対応する履歴用エンティティを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Sound_Source]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Sound_Source
 * @see Entity_HistInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Sound_Source_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sound_Source_HistInput_Test implements Entity_HistInput_Test{

  private final Sound_Source_TestCase_Make testcase;
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

    Sound_Source_History depend_hist = new Sound_Source_History();
    depend_hist.setSerial_num(null);
    depend_hist.setSound_id(str);
    depend_hist.setSong_title(str);
    depend_hist.setComposer(str);
    depend_hist.setPerformer(str);
    depend_hist.setPublisher(str);
    depend_hist.setOther_comment(str);

    assertAllNull(new Sound_Source(depend_hist));
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
  private void assertAllNull(Sound_Source entity){
    this.softly.assertThat(entity.getSerial_num()).isNull();
    this.softly.assertThat(entity.getSound_id()).isNull();
    this.softly.assertThat(entity.getUpload_date()).isNull();
    this.softly.assertThat(entity.getSong_title()).isNull();
    this.softly.assertThat(entity.getComposer()).isNull();
    this.softly.assertThat(entity.getPerformer()).isNull();
    this.softly.assertThat(entity.getPublisher()).isNull();
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

    Map<Sound_Source_TestKeys, String> normal_case = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
    Integer serial_num = Integer.parseInt(normal_case.get(Sound_Source_TestKeys.Serial_Num));
    String sound_id = normal_case.get(Sound_Source_TestKeys.Sound_Id);
    Date upload_date = date.parse(normal_case.get(Sound_Source_TestKeys.Upload_Date));
    String song_title = normal_case.get(Sound_Source_TestKeys.Song_Title);
    String composer = normal_case.get(Sound_Source_TestKeys.Composer);
    String performer = normal_case.get(Sound_Source_TestKeys.Performer);
    String publisher = normal_case.get(Sound_Source_TestKeys.Publisher);
    String other_comment = normal_case.get(Sound_Source_TestKeys.Other_Comment);
    
    Sound_Source_History depend_hist = new Sound_Source_History();
    depend_hist.setSerial_num(serial_num);
    depend_hist.setSound_id(sound_id);
    depend_hist.setUpload_date(upload_date);
    depend_hist.setSong_title(song_title);
    depend_hist.setComposer(composer);
    depend_hist.setPerformer(performer);
    depend_hist.setPublisher(publisher);
    depend_hist.setOther_comment(other_comment);

    Sound_Source entity = new Sound_Source(depend_hist);

    this.softly.assertThat(entity.getSerial_num()).isEqualTo(serial_num);
    this.softly.assertThat(entity.getSound_id()).isEqualTo(sound_id);
    this.softly.assertThat(entity.getUpload_date()).isEqualTo(upload_date);
    this.softly.assertThat(entity.getSong_title()).isEqualTo(song_title);
    this.softly.assertThat(entity.getComposer()).isEqualTo(composer);
    this.softly.assertThat(entity.getPerformer()).isEqualTo(performer);
    this.softly.assertThat(entity.getPublisher()).isEqualTo(publisher);
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
