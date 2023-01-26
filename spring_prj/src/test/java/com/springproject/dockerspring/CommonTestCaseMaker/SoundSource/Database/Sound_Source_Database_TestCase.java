/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Database
 * 
 * @brief [音源管理]に関するテストケース生成処理のうち、[データベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Database;





/** 
 **************************************************************************************
 * @file Sound_Source_Database_TestCase.java
 * @brief 主に[音源管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;









/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - 必要に応じてファイルサーバーと連携してデータベースとファイルサーバーのどちらも初期化すること
 * が可能である。
 * - データベースへの保存に失敗するテストケースを、モックのエンティティに格納して返却が可能。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import({ConfigureFactory.class, History_TestCase_Make.class})
 * 
 * @see Sound_Source_Database_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Sound_Source_Database_TestCase{
  
  private final Sound_Source_Database_Yaml database_yml;
  private final History_TestCase_Make hist_testcase_make;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String NORMAL_TABLE_NAME = "Sound_Source";
  private final String HISTORY_TABLE_NAME = "Sound_Source_History";
  private final Truncate NORMAL_TABLE = Truncate.table(NORMAL_TABLE_NAME);
  private final Truncate HISTORY_TABLE = Truncate.table(HISTORY_TABLE_NAME);










  /** 
   **********************************************************************************************
   * @brief 使用するテストケースクラスのDIのほか、YMLファイルからデータを読み取って専用エンティティへの
   * マッピングを行う。
   * 
   * @details
   * - データベースやファイルサーバーのセットアップに必要なクラスを、インジェクションする。
   * - データベースに格納するテストケースが記述されたYMLファイルを読み込んで、専用のエンティティに
   * マッピングする。
   * 
   * @param[in] config_factory テスト設定ファイルの総合クラス
   * @param[in] hist_testcase_make 履歴情報のデータベース関連のテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Sound_Source_Database_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  @Autowired
  public Sound_Source_Database_TestCase(ConfigureFactory config_factory, 
                                        History_TestCase_Make hist_testcase_make) throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/SoundSource/sound-source-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, Sound_Source_Database_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
  }










  /** @name 通常データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void databaseSetup(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    for(int i = 1; i <= 20; i++){
      Operation operation = Insert.into(NORMAL_TABLE_NAME)
                                  .columns("serial_num",
                                           "sound_id", 
                                           "upload_date", 
                                           "song_title", 
                                           "composer", 
                                           "performer", 
                                           "publisher", 
                                           "other_comment")
                                  .values(i,
                                          this.database_yml.getSound_id().get("case_" + i), 
                                          this.database_yml.getUpload_date().get("case_" + i),
                                          this.database_yml.getSong_title().get("case_" + i), 
                                          this.database_yml.getComposer().get("case_" + i), 
                                          this.database_yml.getPerformer().get("case_" + i), 
                                          this.database_yml.getPublisher().get("case_" + i), 
                                          this.database_yml.getOther_comment().get("case_" + i))
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
  }  








  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @par 処理の大まかな流れ
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void databaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }









  /** 
   **********************************************************************************************
   * @brief データベースの初期化データとして使ったデータを用いて、テストの際に比較用として用いるモックの
   * エンティティを作成する。
   * 
   * @details
   * - データベースの初期化データと内容が同じなので、検索メソッドテストの際の結果の比較などに幅広く用いる
   * 事が可能である。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source compareEntityMake(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Sound_Source mock_entity = mock(Sound_Source.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("case_" + testcase_num));
      when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_" + testcase_num));
      when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("case_" + testcase_num));
      when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("case_" + testcase_num));
      when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("case_" + testcase_num));
      when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("case_" + testcase_num));
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_" + testcase_num));

      return mock_entity;
    }
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新処理の際に使用する、更新後のデータのモックエンティティを作成する。
   * 
   * @details
   * - 更新後のデータの内容としては、明らかに既存のテストケースの値とは被らないような適当な値を用いている。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 適当な被らない値を、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   * 
   * @throw ParseException
   **********************************************************************************************
   */ 
  public Sound_Source UpdateAfterEntityMake(Integer testcase_num) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Sound_Source mock_entity = mock(Sound_Source.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn("id" + testcase_num);
      when(mock_entity.getUpload_date()).thenReturn(parse_date.parse("1996-2-19"));
      when(mock_entity.getSong_title()).thenReturn("Blank");
      when(mock_entity.getComposer()).thenReturn("Blank");
      when(mock_entity.getPerformer()).thenReturn("Blank");
      when(mock_entity.getPublisher()).thenReturn("Blank");
      when(mock_entity.getOther_comment()).thenReturn("Blank");

      return mock_entity;
    }
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容される文字数ギリギリの文字数を格納した
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source justInTimeEntityMake(){

    Sound_Source mock_entity = mock(Sound_Source.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ok_length"));
    when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_1"));
    when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("ok_length"));
    when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("ok_length"));
    when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("ok_length"));
    when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("ok_length"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_length"));

    return mock_entity;
  }  









  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容されるNullの値を入れた場合の
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source okNullEntityMake(){

    Sound_Source mock_entity = mock(Sound_Source.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ok_null_inspect"));
    when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_1"));
    when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("case_1"));
    when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("case_1"));
    when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("case_1"));
    when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("ok_null"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_null"));

    return mock_entity;
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、保存に失敗するテストケースのモックエンティティを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<Sound_Source> failedEntityMake(){

    List<Sound_Source> fail_mock_list = new ArrayList<>();
    int i = 1;

    String[] sound_id = {"ng_overflow", "ng_null"};
    for(String key_name: sound_id){
      Sound_Source mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }
    
    String[] upload_date = {"ng_null"};
    for(String key_name: upload_date){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get(key_name));

      fail_mock_list.add(mock_entity);

    }
    String[] song_title = {"ng_overflow", "ng_null"};
    for(String key_name: song_title){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] composer = {"ng_overflow", "ng_null"};
    for(String key_name: composer){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] performer = {"ng_overflow", "ng_null"};
    for(String key_name: performer){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] publisher = {"ng_overflow"};
    for(String key_name: publisher){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] comment = {"ng_overflow"};
    for(String key_name: comment){
      Sound_Source mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、一意制約違反により失敗するテストケースを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source UniqueMissEntityMake(){

    Sound_Source mock_entity = mock(Sound_Source.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ng_unique"));
    when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_1"));
    when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("case_1"));
    when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("case_1"));
    when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("case_1"));
    when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("case_1"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_1"));

    return mock_entity;
  }

  /** @} */













  /** @name 履歴データのテスト時のセットアップ */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に用いるデータベースに、テストケースのデータを保存する。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    for(int i = 1; i <= 20; i++){
      Operation operation = Insert.into(HISTORY_TABLE_NAME)
                                  .columns("history_id",
                                          "change_datetime",
                                          "change_kinds",
                                          "operation_user",
                                          "serial_num",
                                          "sound_id", 
                                          "upload_date", 
                                          "song_title", 
                                          "composer", 
                                          "performer", 
                                          "publisher", 
                                          "other_comment")
                                  .values(i,
                                          this.hist_testcase_make.getCompareChange_datetime(i), 
                                          this.hist_testcase_make.getCompareChange_kinds(i), 
                                          this.hist_testcase_make.getCompareOperation_user(i), 
                                          i, 
                                          this.hist_testcase_make.getCompareId(i), 
                                          this.database_yml.getUpload_date().get("case_" + i),
                                          this.database_yml.getSong_title().get("case_" + i), 
                                          this.database_yml.getComposer().get("case_" + i), 
                                          this.database_yml.getPerformer().get("case_" + i), 
                                          this.database_yml.getPublisher().get("case_" + i), 
                                          this.database_yml.getOther_comment().get("case_" + i))
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);
  }











  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @par 処理の大まかな流れ
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   **********************************************************************************************
   */ 
  public void historyDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 履歴用データベースの初期化データとして使ったデータを用いて、テストの際に比較用として用いる
   * モックのエンティティを作成する。
   * 
   * @details
   * - データベースの初期化データと内容が同じなので、検索メソッドテストの際の結果の比較などに幅広く用いる
   * 事が可能である。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source_History compareHistoryEntityMake(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      Sound_Source_History mock_entity = mock(Sound_Source_History.class);

      when(mock_entity.getHistory_id()).thenReturn(testcase_num);
      when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(testcase_num));
      when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(testcase_num));
      when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(testcase_num));
      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getSound_id()).thenReturn(this.hist_testcase_make.getCompareId(testcase_num));
      when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_" + testcase_num));
      when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("case_" + testcase_num));
      when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("case_" + testcase_num));
      when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("case_" + testcase_num));
      when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("case_" + testcase_num));
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("case_" + testcase_num));

      return mock_entity;
    }
  }












  /** 
   **********************************************************************************************
   * @brief 履歴用リポジトリでの更新＆追加処理の際に使用する、カラムに許容される文字数ギリギリの文字数を
   * 格納したモックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source_History justInTimeHistoryEntityMake(){

    Sound_Source_History mock_entity = mock(Sound_Source_History.class);

    when(mock_entity.getHistory_id()).thenReturn(1);
    when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(1));
    when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(1));
    when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(null));
    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("ok_length"));
    when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_1"));
    when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("ok_length"));
    when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("ok_length"));
    when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("ok_length"));
    when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("ok_length"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_length"));

    return mock_entity;
  }








  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新＆追加処理の際に使用する、カラムに許容されるNullの値を入れた場合の
   * モックエンティティを作成する。
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * -# 取り出したデータを、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public Sound_Source_History okNullHistoryEntityMake(){

    Sound_Source_History mock_entity = mock(Sound_Source_History.class);

    when(mock_entity.getHistory_id()).thenReturn(1);
    when(mock_entity.getChange_datetime()).thenReturn(this.hist_testcase_make.getCompareChange_datetime(1));
    when(mock_entity.getChange_kinds()).thenReturn(this.hist_testcase_make.getCompareChange_kinds(1));
    when(mock_entity.getOperation_user()).thenReturn(this.hist_testcase_make.getCompareOperation_user(1));
    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get("case_1"));
    when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get("case_1"));
    when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get("case_1"));
    when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get("case_1"));
    when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get("case_1"));
    when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get("ok_null"));
    when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get("ok_null"));

    return mock_entity;
  }








  /** 
   **********************************************************************************************
   * @brief 履歴用リポジトリでの更新＆追加処理の際に使用する、保存に失敗するテストケースのモック
   * エンティティを作成。
   * 
   * @details
   * - ベースとなる成功ケースのモックエンティティを上書きすることで、実現する。
   * 
   * @return データが格納されているモックエンティティのリスト
   * 
   * @par 処理の大まかな流れ
   * -# ベースとなる成功ケースのモックエンティティを取得。
   * -# 取り出したデータを、作成したモックオブジェクトに上書きする。
   * -# 格納が終わったモックを、リストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  public List<Sound_Source_History> failedHistoryEntityMake(){

    List<Sound_Source_History> fail_mock_list = new ArrayList<>();
    int i = 0;

    for(Date fail_data: this.hist_testcase_make.getFailChange_datetime()){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
      
      i++;
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getChange_datetime()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailChange_kinds()){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getChange_kinds()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    for(String fail_data: this.hist_testcase_make.getFailOperation_user()){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getOperation_user()).thenReturn(fail_data);

      fail_mock_list.add(mock_entity);
    }

    String[] sound_id = {"ng_overflow", "ng_null"};
    for(String key_name: sound_id){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);

      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getSound_id()).thenReturn(this.database_yml.getSound_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }
    
    String[] upload_date = {"ng_null"};
    for(String key_name: upload_date){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getUpload_date()).thenReturn(this.database_yml.getUpload_date().get(key_name));

      fail_mock_list.add(mock_entity);

    }
    String[] song_title = {"ng_overflow", "ng_null"};
    for(String key_name: song_title){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getSong_title()).thenReturn(this.database_yml.getSong_title().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] composer = {"ng_overflow", "ng_null"};
    for(String key_name: composer){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getComposer()).thenReturn(this.database_yml.getComposer().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] performer = {"ng_overflow", "ng_null"};
    for(String key_name: performer){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getPerformer()).thenReturn(this.database_yml.getPerformer().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] publisher = {"ng_overflow"};
    for(String key_name: publisher){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getPublisher()).thenReturn(this.database_yml.getPublisher().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] comment = {"ng_overflow"};
    for(String key_name: comment){
      Sound_Source_History mock_entity = compareHistoryEntityMake(1);
            
      i++;
      when(mock_entity.getHistory_id()).thenReturn(i);
      when(mock_entity.getSerial_num()).thenReturn(i);
      when(mock_entity.getOther_comment()).thenReturn(this.database_yml.getOther_comment().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    return fail_mock_list;
  }
}
