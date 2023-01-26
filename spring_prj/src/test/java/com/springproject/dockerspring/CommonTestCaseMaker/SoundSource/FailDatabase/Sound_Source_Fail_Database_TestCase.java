/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.FailDatabase
 * 
 * @brief [音源管理]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.FailDatabase;





/** 
 **************************************************************************************
 * @file Sound_Source_Fail_Database_TestCase.java
 * @brief 主に[音源管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Form.Sound_Source_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;









/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - 必要に応じてファイルサーバーと連携してデータベースとファイルサーバーのどちらも初期化すること
 * が可能である。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import({ConfigureFactory.class, History_TestCase_Make.class})
 * 
 * @see Sound_Source_Form_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Sound_Source_Fail_Database_TestCase{
  
  private final Sound_Source_Form_Yaml form_yml;
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
   * @param[in] hist_testcase_make 履歴情報のデータベース関連のエラーテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Sound_Source_Form_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Sound_Source_Fail_Database_TestCase(ConfigureFactory config_factory, 
                                             History_TestCase_Make hist_testcase_make) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/SoundSource/sound-source-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Sound_Source_Form_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * - なおセットアップ後は、データ検索に必要なシリアルナンバーと管理番号を返す。
   * 
   * @return データ検索に必要なシリアルナンバーと管理番号のマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要なシリアルナンバーと管理番号を戻り値とする。
   * 
   * @throw ParseException
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup() throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");
    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<Sound_Source_TestKeys, String>> fail_list = getDatabaseFail();
    Map<Integer, String> search_map = new HashMap<>();

    int i = 1;
    for(Map<Sound_Source_TestKeys, String> fail_map: fail_list){
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
                                          fail_map.get(Sound_Source_TestKeys.Sound_Id), 
                                          parse_date.parse(fail_map.get(Sound_Source_TestKeys.Upload_Date)), 
                                          fail_map.get(Sound_Source_TestKeys.Song_Title), 
                                          fail_map.get(Sound_Source_TestKeys.Composer), 
                                          fail_map.get(Sound_Source_TestKeys.Performer), 
                                          fail_map.get(Sound_Source_TestKeys.Publisher), 
                                          fail_map.get(Sound_Source_TestKeys.Other_Comment))
                                  .build();

      operation_list.add(operation);
      search_map.put(i++, fail_map.get(Sound_Source_TestKeys.Sound_Id));
    }

    this.config_factory.databaseExec(operation_list);

    return search_map;
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
  public void failDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 履歴用のリポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - セットアップ後は、データ検索に必要な履歴番号を返す。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * 
   * @return データ検索に必要な履歴番号をリストに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要な履歴番号のリストを戻り値とする。
   * 
   * @throw ParseException
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup() throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat parse_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<String, String>> fail_list = getHistoryDatabaseFail();
    List<Integer> search_list = new ArrayList<>();


    int i = 1;
    for(Map<String, String> fail_map: fail_list){
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
                                          parse_datetime.parse(fail_map.get("change_datetime")),
                                          fail_map.get("change_kinds"), 
                                          fail_map.get("operation_user"), 
                                          i, 
                                          fail_map.get("sound_id"), 
                                          parse_date.parse(fail_map.get("upload_date")), 
                                          fail_map.get("song_title"), 
                                          fail_map.get("composer"), 
                                          fail_map.get("performer"), 
                                          fail_map.get("publisher"), 
                                          fail_map.get("other_comment"))
                                  .build();

      operation_list.add(operation);
      search_list.add(i++);
    }

    this.config_factory.databaseExec(operation_list);

    return search_list;
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
  public void historyFailDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief 通常テーブルで用いるエラーテストケースを全て格納したリストを作成する。
   * 
   * @details
   * - テストケースに登録されている全てのエラーケースをリスト化したものを作成する。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 音源番号が一意制約に違反しないように、「音源番号がエラーのケース」以外のエラーケースに関しては、
   * 確実に一意のエラーが起こらない音源番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> getDatabaseFail(){
    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    error_list.addAll(makeSongTitleFailedData());
    error_list.addAll(makeComposerFailedData());
    error_list.addAll(makePerformerFailedData());
    error_list.addAll(makePublisherFailedData());
    error_list.addAll(makeOtherCommentFailedData());

    int i = 1;
    for(Map<Sound_Source_TestKeys, String> id_override: error_list){
      String sound_id = id_override.get(Sound_Source_TestKeys.Sound_Id);
      sound_id = sound_id.replaceFirst("....$", "") + "_" + i;

      id_override.put(Sound_Source_TestKeys.Sound_Id, sound_id);
      i++;
    }

    error_list.addAll(makeSoundIdFailedData());

    return error_list;
  }










  /** 
   **********************************************************************************************
   * @brief 履歴テーブルで用いるエラーテストケースを全て格納したリストを作成する。
   * 
   * @details
   * - テストケースに登録されている全てのエラーケースをリスト化したものを作成する。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @note 
   * - 履歴内の履歴情報データがエラーになるケースでは、通常データはエラーにならないような適当な値を格納。
   * - 履歴内の通常データがエラーになるケースでは、履歴付随情報はエラーにならないような適当な値を格納。
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<String, String>> getHistoryDatabaseFail(){
    List<Map<String, String>> fail_list = new ArrayList<>();

    List<Map<Sound_Source_TestKeys, String>> normal_table = getDatabaseFail();
    List<Map<History_TestKeys, String>> hist_table = this.hist_testcase_make.getHistoryDatabaseFailData();


    for(Map<Sound_Source_TestKeys, String> normal_map: normal_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", "1950-01-01 12:30:30");
      strkey_map.put("change_kinds", "update");
      strkey_map.put("operation_user", "testuser");
      strkey_map.put("sound_id", normal_map.get(Sound_Source_TestKeys.Sound_Id));
      strkey_map.put("upload_date", normal_map.get(Sound_Source_TestKeys.Upload_Date));
      strkey_map.put("song_title", normal_map.get(Sound_Source_TestKeys.Song_Title));
      strkey_map.put("composer", normal_map.get(Sound_Source_TestKeys.Composer));
      strkey_map.put("performer", normal_map.get(Sound_Source_TestKeys.Performer));
      strkey_map.put("publisher", normal_map.get(Sound_Source_TestKeys.Publisher));
      strkey_map.put("other_comment", normal_map.get(Sound_Source_TestKeys.Other_Comment));

      fail_list.add(strkey_map);
    }


    for(Map<History_TestKeys, String> hist_map: hist_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", hist_map.get(History_TestKeys.Change_Datetime));
      strkey_map.put("change_kinds", hist_map.get(History_TestKeys.Change_Kinds));
      strkey_map.put("operation_user", hist_map.get(History_TestKeys.Operation_User));
      strkey_map.put("sound_id", makeBaseData().get(Sound_Source_TestKeys.Sound_Id));
      strkey_map.put("upload_date", makeBaseData().get(Sound_Source_TestKeys.Upload_Date));
      strkey_map.put("song_title", makeBaseData().get(Sound_Source_TestKeys.Song_Title));
      strkey_map.put("composer", makeBaseData().get(Sound_Source_TestKeys.Composer));
      strkey_map.put("performer", makeBaseData().get(Sound_Source_TestKeys.Performer));
      strkey_map.put("publisher", makeBaseData().get(Sound_Source_TestKeys.Publisher));
      strkey_map.put("other_comment", makeBaseData().get(Sound_Source_TestKeys.Other_Comment));

      fail_list.add(strkey_map);
    }

    return fail_list;
  }











  /** 
   **********************************************************************************************
   * @brief エラーケースのデータを作成するうえで基礎となる、合格ケースのリストを用意する。
   * 
   * @details
   * - ここで作成するリストを、項目一つづつ上書きすることで、一つの項目のみがエラーであるマップリストを
   * 作成することが可能である。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return 正常データのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */
  private Map<Sound_Source_TestKeys, String> makeBaseData(){

    Map<Sound_Source_TestKeys, String> base_map = new HashMap<>();
    String childkey = "ok";

    base_map.put(Sound_Source_TestKeys.Sound_Id, this.form_yml.getSound_id().get(childkey));
    base_map.put(Sound_Source_TestKeys.Upload_Date, this.form_yml.getUpload_date().get(childkey));
    base_map.put(Sound_Source_TestKeys.Song_Title, this.form_yml.getSong_title().get(childkey));
    base_map.put(Sound_Source_TestKeys.Composer, this.form_yml.getComposer().get(childkey));
    base_map.put(Sound_Source_TestKeys.Performer, this.form_yml.getPerformer().get(childkey));
    base_map.put(Sound_Source_TestKeys.Publisher, this.form_yml.getPublisher().get(childkey));
    base_map.put(Sound_Source_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

    return base_map;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[音源番号]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makeSoundIdFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Sound_Id, this.form_yml.getSound_id().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[曲名]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makeSongTitleFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Song_Title, this.form_yml.getSong_title().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[作曲者]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makeComposerFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Composer, this.form_yml.getComposer().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[演奏者]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makePerformerFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Performer, this.form_yml.getPerformer().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }







  /** 
   **********************************************************************************************
   * @brief 項目[出版社]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makePublisherFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Publisher, this.form_yml.getPublisher().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }



  



  /** 
   **********************************************************************************************
   * @brief 項目[その他コメント]でエラーになるテストケースを格納したマップリストを作成する。
   * 
   * @details
   * - マッピングしたエンティティから該当項目のエラーケースを取り出し、ベースとなる合格ケースのリストを
   * 上書きすることで作成する。
   * - 一つの項目のみがエラーとなるようにしているので、テストエラー時の原因を把握しやすくなる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# あらかじめ定義した配列内の文字を連結して、データを取得する際のキー名を組む。
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# あらかじめ作成した合格ケースのリストを取得し、そのリストの該当項目をエラーケースで上書きする。
   * -# 上書きしたデータは、順番に新規のリストに格納する。
   * -# 格納が終わったリストを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Sound_Source_TestKeys, String>> makeOtherCommentFailedData(){

    List<Map<Sound_Source_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      Map<Sound_Source_TestKeys, String> base_data = makeBaseData();
      base_data.put(Sound_Source_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }
}
