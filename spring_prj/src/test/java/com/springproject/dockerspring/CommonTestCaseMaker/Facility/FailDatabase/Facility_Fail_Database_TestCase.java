/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Facility.FailDatabase
 * 
 * @brief [設備管理]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility.FailDatabase;





/** 
 **************************************************************************************
 * @file Facility_Fail_Database_TestCase.java
 * @brief 主に[設備管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make.Facility_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Form.Facility_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;









/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
 * @see Facility_Form_Yaml
 * @see ConfigureFactory
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@Import({ConfigureFactory.class, History_TestCase_Make.class})
public class Facility_Fail_Database_TestCase{
  
  private final Facility_Form_Yaml form_yml;
  private final History_TestCase_Make hist_testcase_make;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String NORMAL_TABLE_NAME = "Facility";
  private final String HISTORY_TABLE_NAME = "Facility_History";
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
   * @see Facility_Form_Yaml
   * @see ConfigureFactory
   * @see History_TestCase_Make
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Facility_Fail_Database_TestCase(ConfigureFactory config_factory, 
                                         History_TestCase_Make hist_testcase_make) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/Facility/facility-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Facility_Form_Yaml.class);
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

    List<Map<Facility_TestKeys, String>> fail_list = getDatabaseFail();
    Map<Integer, String> search_map = new HashMap<>();

    int i = 1;
    for(Map<Facility_TestKeys, String> fail_map: fail_list){
      Operation operation = Insert.into(NORMAL_TABLE_NAME)
                                  .columns("serial_num",
                                          "faci_id",
                                          "faci_name",
                                          "buy_date",
                                          "producer",
                                          "storage_loc",
                                          "disp_date",
                                          "other_comment")
                                  .values(i,
                                          fail_map.get(Facility_TestKeys.Faci_Id), 
                                          fail_map.get(Facility_TestKeys.Faci_Name), 
                                          parse_date.parse(fail_map.get(Facility_TestKeys.Buy_Date)), 
                                          fail_map.get(Facility_TestKeys.Producer), 
                                          fail_map.get(Facility_TestKeys.Storage_Loc), 
                                          parse_date.parse(fail_map.get(Facility_TestKeys.Disp_Date)), 
                                          fail_map.get(Facility_TestKeys.Other_Comment))
                                  .build();

      operation_list.add(operation);
      search_map.put(i++, fail_map.get(Facility_TestKeys.Faci_Id));
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
                                    "faci_id",
                                    "faci_name",
                                    "buy_date",
                                    "producer",
                                    "storage_loc",
                                    "disp_date",
                                    "other_comment")
                            .values(i,
                                    parse_datetime.parse(fail_map.get("change_datetime")),
                                    fail_map.get("change_kinds"), 
                                    fail_map.get("operation_user"), 
                                    i, 
                                    fail_map.get("faci_id"), 
                                    fail_map.get("faci_name"), 
                                    parse_date.parse(fail_map.get("buy_date")), 
                                    fail_map.get("producer"), 
                                    fail_map.get("storage_loc"), 
                                    parse_date.parse(fail_map.get("disp_date")), 
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
   * -# 設備番号が一意制約に違反しないように、「設備番号がエラーのケース」以外のエラーケースに関しては、
   * 確実に一意のエラーが起こらない設備番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Facility_TestKeys, String>> getDatabaseFail(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    error_list.addAll(makeFaciNameFailedData());
    error_list.addAll(makeProducerFailedData());
    error_list.addAll(makeStolageLocFailedData());
    error_list.addAll(makeDispDateFailedData());
    error_list.addAll(makeOtherCommentFailedData());

    int i = 1;
    for(Map<Facility_TestKeys, String> id_override: error_list){
      String faci_id = id_override.get(Facility_TestKeys.Faci_Id);
      faci_id = faci_id.replaceFirst("....$", "") + "_" + i;

      id_override.put(Facility_TestKeys.Faci_Id, faci_id);
      i++;
    }

    error_list.addAll(makeFaciIdFailedData());

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

    List<Map<Facility_TestKeys, String>> normal_table = getDatabaseFail();
    List<Map<History_TestKeys, String>> hist_table = this.hist_testcase_make.getHistoryDatabaseFailData();


    for(Map<Facility_TestKeys, String> normal_map: normal_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", "1950-01-01 12:30:30");
      strkey_map.put("change_kinds", "update");
      strkey_map.put("operation_user", "testuser");
      strkey_map.put("faci_id", normal_map.get(Facility_TestKeys.Faci_Id));
      strkey_map.put("faci_name", normal_map.get(Facility_TestKeys.Faci_Name));
      strkey_map.put("buy_date", normal_map.get(Facility_TestKeys.Buy_Date));
      strkey_map.put("producer", normal_map.get(Facility_TestKeys.Producer));
      strkey_map.put("storage_loc", normal_map.get(Facility_TestKeys.Storage_Loc));
      strkey_map.put("disp_date", normal_map.get(Facility_TestKeys.Disp_Date));
      strkey_map.put("other_comment", normal_map.get(Facility_TestKeys.Other_Comment));

      fail_list.add(strkey_map);
    }


    for(Map<History_TestKeys, String> hist_map: hist_table){

      Map<String, String> strkey_map = new HashMap<>();
      strkey_map.put("change_datetime", hist_map.get(History_TestKeys.Change_Datetime));
      strkey_map.put("change_kinds", hist_map.get(History_TestKeys.Change_Kinds));
      strkey_map.put("operation_user", hist_map.get(History_TestKeys.Operation_User));
      strkey_map.put("faci_id", makeBaseData().get(Facility_TestKeys.Faci_Id));
      strkey_map.put("faci_name", makeBaseData().get(Facility_TestKeys.Faci_Name));
      strkey_map.put("buy_date", makeBaseData().get(Facility_TestKeys.Buy_Date));
      strkey_map.put("producer", makeBaseData().get(Facility_TestKeys.Producer));
      strkey_map.put("storage_loc", makeBaseData().get(Facility_TestKeys.Storage_Loc));
      strkey_map.put("disp_date", makeBaseData().get(Facility_TestKeys.Disp_Date));
      strkey_map.put("other_comment", makeBaseData().get(Facility_TestKeys.Other_Comment));

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
  private Map<Facility_TestKeys, String> makeBaseData(){

    Map<Facility_TestKeys, String> base_map = new HashMap<>();
    String childkey = "ok";

    base_map.put(Facility_TestKeys.Faci_Id, this.form_yml.getFaci_id().get(childkey));
    base_map.put(Facility_TestKeys.Faci_Name, this.form_yml.getFaci_name().get(childkey));
    base_map.put(Facility_TestKeys.Buy_Date, this.form_yml.getBuy_date().get(childkey));
    base_map.put(Facility_TestKeys.Producer, this.form_yml.getProducer().get(childkey));
    base_map.put(Facility_TestKeys.Storage_Loc, this.form_yml.getStorage_loc().get(childkey));
    base_map.put(Facility_TestKeys.Disp_Date, this.form_yml.getDisp_date().get(childkey));
    base_map.put(Facility_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(childkey));

    return base_map;
  }











  /** 
   **********************************************************************************************
   * @brief 項目[設備番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Facility_TestKeys, String>> makeFaciIdFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Faci_Id, this.form_yml.getFaci_id().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[設備名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Facility_TestKeys, String>> makeFaciNameFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Faci_Name, this.form_yml.getFaci_name().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[製作者]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Facility_TestKeys, String>> makeProducerFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Producer, this.form_yml.getProducer().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }








  /** 
   **********************************************************************************************
   * @brief 項目[保管場所]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Facility_TestKeys, String>> makeStolageLocFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Storage_Loc, this.form_yml.getStorage_loc().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[廃棄日]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Facility_TestKeys, String>> makeDispDateFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_before_buy_date"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Disp_Date, this.form_yml.getDisp_date().get(key));
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
  private List<Map<Facility_TestKeys, String>> makeOtherCommentFailedData(){

    List<Map<Facility_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_alpha", 
                         "ng_digit", 
                         "ng_hankaku_kana"};

    for(String key: key_name){
      Map<Facility_TestKeys, String> base_data = makeBaseData();
      base_data.put(Facility_TestKeys.Other_Comment, this.form_yml.getOther_comment().get(key));
      error_list.add(base_data);
    }

    return error_list;
  }
}
