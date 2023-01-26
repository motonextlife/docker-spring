/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Musical_Score_Test
 * 
 * @brief 通常用エンティティの結合テストのうち、[楽譜情報]に関するテストを格納したパッケージ
 * 
 * @details
 * - このパッケージは、楽譜情報で用いるエンティティの結合テストを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.Entity.NormalEntity.Musical_Score_Test;





/** 
 **************************************************************************************
 * @file Musical_Score_FormInput_Test.java
 * 
 * @brief [楽譜管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
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

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make.Musical_Score_TestKeys;
import com.springproject.dockerspring.JoinTests.Entity.TestInterface.Entity_FormInput_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Form.CsvImplForm.Musical_Score_Form;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;






/** 
 * 
 * 
 **************************************************************************************
 * @brief [楽譜管理]において、テスト対象エンティティに対応するフォームクラスを入力した際の
 * テストを行うクラス。
 * 
 * @details 
 * - 検査対象のクラス名は[Musical_Score]である。
 * - テスト内容としては、入力したデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Musical_Score
 * @see Entity_FormInput_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Musical_Score_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_FormInput_Test implements Entity_FormInput_Test{

  private final Musical_Score_TestCase_Make testcase;
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
   ****************************************************************************************
   */ 
  private void setInitTargetForm(String str){

    Musical_Score_Form depend_form = new Musical_Score_Form();
    depend_form.setSerial_num(null);
    depend_form.setScore_id(str);
    depend_form.setSong_title(str);
    depend_form.setComposer(str);
    depend_form.setArranger(str);
    depend_form.setPublisher(str);
    depend_form.setStorage_loc(str);
    depend_form.setOther_comment(str);

    assertAllNull(new Musical_Score(depend_form));
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
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力したフォームクラスデータの同一性確認() throws ParseException{

    Map<Musical_Score_TestKeys, String> normalcase = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    
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
    
    Musical_Score_Form depend_form = new Musical_Score_Form();
    depend_form.setSerial_num(serial_num);
    depend_form.setScore_id(score_id);
    depend_form.setBuy_date(buy_date);
    depend_form.setSong_title(song_title);
    depend_form.setComposer(composer);
    depend_form.setArranger(arranger);
    depend_form.setPublisher(publisher);
    depend_form.setStorage_loc(storage_loc);
    depend_form.setDisp_date(disp_date);
    depend_form.setOther_comment(other_comment);

    Musical_Score entity = new Musical_Score(depend_form);

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
  public void フォームクラスの初期化対象文字のNull初期化確認(String init_str){
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
