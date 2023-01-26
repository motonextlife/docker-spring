/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.FailDatabase
 * 
 * @brief [音源データ管理機能]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.AudioData.FailDatabase;





/** 
 **************************************************************************************
 * @file Audio_Data_Fail_Database_TestCase.java
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Audio_Data_TestCase_Make.Audio_Data_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Form.Audio_Data_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;









/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
 * @see Audio_Data_Form_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 * @see Audio_Data_Fail_Samba_TestCase
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Audio_Data_Fail_Database_TestCase{

  private final Audio_Data_Form_Yaml form_yml;
  private final History_TestCase_Make hist_testcase_make;
  private final Audio_Data_Fail_Samba_TestCase samba_fail_testcase;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String NORMAL_TABLE_NAME = "Audio_Data";
  private final String HISTORY_TABLE_NAME = "Audio_Data_History";
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
   * @param[in] samba_fail_testcase [音源データ情報]のファイルサーバー関連のエラーテストケースクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Audio_Data_Form_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   * @see Audio_Data_Fail_Samba_TestCase
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Audio_Data_Fail_Database_TestCase(ConfigureFactory config_factory, 
                                           History_TestCase_Make hist_testcase_make, 
                                           Audio_Data_Fail_Samba_TestCase samba_fail_testcase) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/AudioData/audio-data-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Audio_Data_Form_Yaml.class);
    }

    this.config_factory = config_factory;
    this.hist_testcase_make = hist_testcase_make;
    this.samba_fail_testcase = samba_fail_testcase;
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - フラグを立てることで、データベース単体のセットアップか、ファイルサーバーと連携したセットアップか、
   * 選ぶことが可能である。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * - なおセットアップ後は、データ検索に必要なシリアルナンバーと管理番号を返す。
   * 
   * @param[in] link_samba 「True」でファイルサーバーと連携してセットアップする。「False」でデータベースのみ。
   * 
   * @return データ検索に必要なシリアルナンバーと管理番号のマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# ファイルサーバーとのリンクが指定されていれば、指定のファイルをファイルサーバーに保存。
   * -# ファイルサーバーに保存した後に返ってきたハッシュ値を一旦変数に保存。
   * -# 初期化データを保存するクエリを組み立てる。その際に、ファイルサーバーとのリンクが指定されていれば
   * データベースにハッシュ値を保存する。
   * -# ファイルサーバーを使用しない設定であれば、そのままテストケースの内容を保存。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要なシリアルナンバーと管理番号を戻り値とする。
   * 
   * @note 
   * - ファイルサーバーとリンクしない場合のデータベースに格納するハッシュ値は、適当な文字列となる。
   * - 特に指定はないが、ファイルサーバーへの保存時の拡張子は[wav]とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(NORMAL_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<Audio_Data_TestKeys, String>> fail_list = getDatabaseFail(link_samba);
    Map<Integer, String> search_map = new HashMap<>();

    int i = 1;
    for(Map<Audio_Data_TestKeys, String> fail_map: fail_list){
      String sound_name = "";
      String audio_hash = "";

      if(link_samba){
        String filename = i + ".wav";
        sound_name = fail_map.get(Audio_Data_TestKeys.Audio_Data);
        audio_hash = this.samba_fail_testcase.failSambaSetup(sound_name, filename);
      }else{
        sound_name = fail_map.get(Audio_Data_TestKeys.Sound_Name);
        audio_hash = "EnterSuitableString";
      }

      Operation operation = Insert.into(NORMAL_TABLE_NAME)
                                  .columns("serial_num",
                                          "sound_id",
                                          "sound_name",
                                          "audio_hash")
                                  .values(i,
                                          fail_map.get(Audio_Data_TestKeys.Sound_Id), 
                                          sound_name, 
                                          audio_hash)
                                  .build();

      operation_list.add(operation);
      search_map.put(i++, fail_map.get(Audio_Data_TestKeys.Sound_Id));
    }

    this.config_factory.databaseExec(operation_list);

    return search_map;
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に使用したデータベース内のデータを全削除してリセットする。
   * 
   * @details
   * - フラグを立てることで、データベース単体のリセットか、ファイルサーバーを含んだリセットか、
   * 選ぶことが可能である。
   * - さらに、リセットデータベースを、本番環境の物か、モックの物かを選択することも可能。
   * 
   * @param[in] link_samba 「True」でファイルサーバーを含んでリセットする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# ファイルサーバーも対象なら、先にファイルサーバーを初期化する。
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void failDatabaseReset(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    if(link_samba){
      this.samba_fail_testcase.failSambaReset();
    }

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
   * - フラグを立てることで、データベース単体のセットアップか、ファイルサーバーと連携したセットアップか、
   * 選ぶことが可能である。
   * - なおセットアップ後は、データ検索に必要な履歴番号を返す。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * 
   * @param[in] link_samba 「True」でファイルサーバーと連携してセットアップする。「False」でデータベースのみ。
   * 
   * @return データ検索に必要な履歴番号をリストに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# ファイルサーバーとのリンクが指定されていれば、指定のファイルをファイルサーバーに保存。
   * -# ファイルサーバーに保存した後に返ってきたハッシュ値を一旦変数に保存。
   * -# 初期化データを保存するクエリを組み立てる。その際に、ファイルサーバーとのリンクが指定されていれば
   * データベースにハッシュ値を保存する。
   * -# ファイルサーバーを使用しない設定であれば、そのままテストケースの内容を保存。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要な履歴番号のリストを戻り値とする。
   * 
   * @note 
   * - ファイルサーバーとリンクしない場合のデータベースに格納するハッシュ値は、適当な文字列となる。
   * - 特に指定はないが、ファイルサーバーへの保存時の拡張子は[gz]とする。
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup(Boolean link_samba) throws IOException, ParseException{

    SimpleDateFormat parse_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(HISTORY_TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<String, String>> fail_list = getHistoryDatabaseFail(link_samba);
    List<Integer> search_list = new ArrayList<>();

    int i = 1;
    for(Map<String, String> fail_map: fail_list){
      String sound_name = "";
      String audio_hash = "";

      if(link_samba){
        String filename = i + ".gz";
        sound_name = fail_map.get("audio_data");
        audio_hash = this.samba_fail_testcase.historyFailSambaSetup(sound_name, filename);
      }else{
        sound_name = fail_map.get("sound_name");
        audio_hash = "EnterSuitableString";
      }

      Operation operation = Insert.into(HISTORY_TABLE_NAME)
                                  .columns("history_id",
                                          "change_datetime",
                                          "change_kinds",
                                          "operation_user",
                                          "serial_num",
                                          "sound_id",
                                          "sound_name",
                                          "audio_hash")
                                  .values(i,
                                          parse_datetime.parse(fail_map.get("change_datetime")),
                                          fail_map.get("change_kinds"), 
                                          fail_map.get("operation_user"), 
                                          i, 
                                          fail_map.get("sound_id"), 
                                          sound_name, 
                                          audio_hash)
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
   * @details
   * - フラグを立てることで、データベース単体のリセットか、ファイルサーバーを含んだリセットか、
   * 選ぶことが可能である。
   * - さらに、リセットデータベースを、本番環境の物か、モックの物かを選択することも可能。
   * 
   * @param[in] link_samba 「True」でファイルサーバーを含んでリセットする。「False」でデータベースのみ。
   * 
   * @par 処理の大まかな流れ
   * -# ファイルサーバーも対象なら、先にファイルサーバーを初期化する。
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void historyFailDatabaseReset(Boolean link_samba) throws IOException{

    List<Operation> operation_list = new ArrayList<>();

    if(link_samba){
      this.samba_fail_testcase.historyFailSambaReset();
    }

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
   * - ファイルサーバーと連携するかどうかで、リストに格納されるテストケースが異なる。
   * - このテストケースは、参照元のテーブルがエラーテストケースを格納した状態に初期化されている事が条件の元、
   * 使用可能。参照制約があるため、その条件を満たせないとテストできない。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @param[in] link_samba 「True」の場合はファイルサーバーとの連携用のエラーケースを、「False」の場合は
   * 連携が必要なくデータベースだけで完結できるエラーケースを選択。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 音源番号が参照元テーブルの音源番号を参照できるように、「音源番号がエラーのケース」以外のエラーケースに
   * 関しては、確実に参照できてエラーが起こらない音源番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Audio_Data_TestKeys, String>> getDatabaseFail(Boolean link_samba){

    List<Map<Audio_Data_TestKeys, String>> error_list = new ArrayList<>();

    if(link_samba){
      error_list.addAll(makeAudioDataFailedData());
    }else{
      error_list.addAll(makeSoundNameFailedData());
    }

    for(Map<Audio_Data_TestKeys, String> id_override: error_list){
      String sound_id = id_override.get(Audio_Data_TestKeys.Sound_Id);
      sound_id = sound_id.replaceFirst("....$", "") + "_" + 1;

      id_override.put(Audio_Data_TestKeys.Sound_Id, sound_id);
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
   * - ファイルサーバーと連携するかどうかで、リストに格納されるテストケースが異なる。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @param[in] link_samba 「True」の場合はファイルサーバーとの連携用のエラーケースを、「False」の場合は
   * 連携が必要なくデータベースだけで完結できるエラーケースを選択。
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
  private List<Map<String, String>> getHistoryDatabaseFail(Boolean link_samba){
    List<Map<String, String>> fail_list = new ArrayList<>();

    List<Map<Audio_Data_TestKeys, String>> normal_table = getDatabaseFail(link_samba);
    List<Map<History_TestKeys, String>> hist_table = this.hist_testcase_make.getHistoryDatabaseFailData();


    for(Map<Audio_Data_TestKeys, String> normal_map: normal_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", "1950-01-01 12:30:30");
      strkey_map.put("change_kinds", "update");
      strkey_map.put("operation_user", "testuser");
      strkey_map.put("sound_id", normal_map.get(Audio_Data_TestKeys.Sound_Id));

      if(link_samba){
        strkey_map.put("audio_data", normal_map.get(Audio_Data_TestKeys.Audio_Data));
      }else{
        strkey_map.put("sound_name", normal_map.get(Audio_Data_TestKeys.Sound_Name));
      }
      
      fail_list.add(strkey_map);
    }


    for(Map<History_TestKeys, String> hist_map: hist_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", hist_map.get(History_TestKeys.Change_Datetime));
      strkey_map.put("change_kinds", hist_map.get(History_TestKeys.Change_Kinds));
      strkey_map.put("operation_user", hist_map.get(History_TestKeys.Operation_User));
      strkey_map.put("sound_id", makeBaseData().get(Audio_Data_TestKeys.Sound_Id));

      if(link_samba){
        strkey_map.put("audio_data", makeBaseData().get(Audio_Data_TestKeys.Audio_Data));
      }else{
        strkey_map.put("sound_name", makeBaseData().get(Audio_Data_TestKeys.Sound_Name));
      }

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
  private Map<Audio_Data_TestKeys, String> makeBaseData(){

    Map<Audio_Data_TestKeys, String> base_map = new HashMap<>();
    String childkey = "ok";

    base_map.put(Audio_Data_TestKeys.Sound_Id, this.form_yml.getSound_id().get(childkey));
    base_map.put(Audio_Data_TestKeys.Sound_Name, this.form_yml.getSound_name().get(childkey));
    base_map.put(Audio_Data_TestKeys.Audio_Data, this.form_yml.getAudio_data().get(childkey));

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
  private List<Map<Audio_Data_TestKeys, String>> makeSoundIdFailedData(){

    List<Map<Audio_Data_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Audio_Data_TestKeys, String> base_data = makeBaseData();
      base_data.put(Audio_Data_TestKeys.Sound_Id, this.form_yml.getSound_id().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[音源データ名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Audio_Data_TestKeys, String>> makeSoundNameFailedData(){

    List<Map<Audio_Data_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_ext", 
                         "ng_html_escape", 
                         "ng_sql_escape", 
                         "ng_filepath_escape", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Audio_Data_TestKeys, String> base_data = makeBaseData();
      base_data.put(Audio_Data_TestKeys.Sound_Name, this.form_yml.getSound_name().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[音源データ]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Audio_Data_TestKeys, String>> makeAudioDataFailedData(){

    List<Map<Audio_Data_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_no_wav_mp3", 
                         "ng_no_wav_aac", 
                         "ng_overflow", 
                         "ng_filename_overflow", 
                         "ng_image", 
                         "ng_pdf", 
                         "ng_csv", 
                         "ng_zip", 
                         "ng_html_escape", 
                         "ng_sql_escape", 
                         "ng_filepath_escape", 
                         "ng_forgery_ext", 
                         "ng_forgery_data"};

    for(String key: key_name){
      Map<Audio_Data_TestKeys, String> base_data = makeBaseData();
      base_data.put(Audio_Data_TestKeys.Audio_Data, this.form_yml.getAudio_data().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }
}
