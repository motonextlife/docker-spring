/** 
 **************************************************************************************
 * @file Sound_Source_MakeMap_Test.java
 * 
 * @brief [音源管理]において、エンティティ内部のデータを画面出力用に文字列に変換して出力する
 * メソッドをテストするクラスを格納するファイルである。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Entity.NormalEntity.Sound_Source_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;
import com.springproject.dockerspring.SingleTests.Entity.TestInterface.Entity_MakeMap_Test;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;

import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;








/** 
 **************************************************************************************
 * @brief [音源管理]において、エンティティ内部のデータを画面出力用に文字列に変換して出力する
 * メソッドをテストするクラス
 * 
 * @details 
 * - 検査対象のクラス名は[Sound_Source]で、検査対象メソッドは[makeMap]である。
 * - テスト内容としては、出力されたデータの同一性テストと、不正なデータの初期化テストである。
 * 
 * @see Sound_Source
 * @see Entity_MakeMap_Test
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Sound_Source_TestCase_Make.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sound_Source_MakeMap_Test implements Entity_MakeMap_Test{

  private final Sound_Source_TestCase_Make testcase;
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

    init_map.put("serial_num", "");
    init_map.put("sound_id", "");
    init_map.put("upload_date", "");
    init_map.put("song_title", "");
    init_map.put("composer", "");
    init_map.put("performer", "");
    init_map.put("publisher", "");
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

    Sound_Source entity = new Sound_Source();
    entity.setSound_id(str);
    entity.setSong_title(str);
    entity.setComposer(str);
    entity.setPerformer(str);
    entity.setPublisher(str);
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
   ****************************************************************************************
   */ 
  @Test
  @Override
  public void 入力した文字列マップリストデータの同一性確認() throws ParseException{

    Map<Sound_Source_TestKeys, String> normal_case = testcase.getNormalData();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    String serial_num = normal_case.get(Sound_Source_TestKeys.Serial_Num);
    String sound_id = normal_case.get(Sound_Source_TestKeys.Sound_Id);
    String upload_date = normal_case.get(Sound_Source_TestKeys.Upload_Date);
    String song_title = normal_case.get(Sound_Source_TestKeys.Song_Title);
    String composer = normal_case.get(Sound_Source_TestKeys.Composer);
    String performer = normal_case.get(Sound_Source_TestKeys.Performer);
    String publisher = normal_case.get(Sound_Source_TestKeys.Publisher);
    String other_comment = normal_case.get(Sound_Source_TestKeys.Other_Comment);

    Sound_Source entity = new Sound_Source();
    entity.setSerial_num(Integer.parseInt(serial_num));
    entity.setSound_id(sound_id);
    entity.setUpload_date(date.parse(upload_date));
    entity.setSong_title(song_title);
    entity.setComposer(composer);
    entity.setPerformer(performer);
    entity.setPublisher(publisher);
    entity.setOther_comment(other_comment);

    Map<String, String> compare_map = new HashMap<>();
    compare_map.put("serial_num", serial_num);
    compare_map.put("sound_id", sound_id);
    compare_map.put("upload_date", upload_date);
    compare_map.put("song_title", song_title);
    compare_map.put("composer", composer);
    compare_map.put("performer", performer);
    compare_map.put("publisher", publisher);
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
