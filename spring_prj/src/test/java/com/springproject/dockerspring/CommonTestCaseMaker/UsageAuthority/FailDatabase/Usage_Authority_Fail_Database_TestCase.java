/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.FailDatabase
 * 
 * @brief [権限管理]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.FailDatabase;






/** 
 **************************************************************************************
 * @file Usage_Authority_Fail_Database_TestCase.java
 * @brief 主に[権限管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Form.Usage_Authority_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make.Usage_Authority_TestKeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;








/** 
 **************************************************************************************
 * @brief 主に[権限管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内にエラーとなる初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - 必要に応じてファイルサーバーと連携してデータベースとファイルサーバーのどちらも初期化すること
 * が可能である。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import(ConfigureFactory.class)
 * 
 * @see Usage_Authority_Form_Yaml
 * @see ConfigureFactory
 **************************************************************************************
 */ 
@Component
@Import(ConfigureFactory.class)
public class Usage_Authority_Fail_Database_TestCase{
  
  private final Usage_Authority_Form_Yaml form_yml;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String TABLE_NAME = "Usage_Authority";
  private final Truncate TABLE = Truncate.table(TABLE_NAME);









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
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡されたクラスを、コンストラクタインジェクションする。
   * -# 指定されたYMLファイルのテストケースを読み込む。
   * -# 読み込んだYMLファイルを専用のエンティティにマッピングする。
   * -# インジェクションしたクラスを、フィールド変数に格納する。
   * 
   * @see Usage_Authority_Form_Yaml
   * @see ConfigureFactory
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public Usage_Authority_Fail_Database_TestCase(ConfigureFactory config_factory) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/UsegeAuthority/usage-authority-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, Usage_Authority_Form_Yaml.class);
    }

    this.config_factory = config_factory;
  }









  /** 
   **********************************************************************************************
   * @brief リポジトリの検査時に用いるデータベースに、エラーテストケースのデータを保存する。
   * 
   * @details
   * - なおセットアップ後は、データ検索に必要なシリアルナンバーと管理番号を返す。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * 
   * @return データ検索に必要なシリアルナンバーと管理番号のマップリスト
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# クエリ組み上げの過程で保存しておいた、データ検索に必要なシリアルナンバーと管理番号を戻り値とする。
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<Map<Usage_Authority_TestKeys, String>> fail_list = getDatabaseFail();
    Map<Integer, String> search_map = new HashMap<>();

    int i = 1;
    for(Map<Usage_Authority_TestKeys, String> fail_map: fail_list){
      Operation operation = Insert.into(TABLE_NAME)
                                  .columns("serial_num",
                                          "auth_id",
                                          "auth_name",
                                          "admin",
                                          "member_info",
                                          "facility",
                                          "musical_score",
                                          "sound_source")
                                  .values(i,
                                          fail_map.get(Usage_Authority_TestKeys.Auth_Id), 
                                          fail_map.get(Usage_Authority_TestKeys.Auth_Name), 
                                          Boolean.parseBoolean(fail_map.get(Usage_Authority_TestKeys.Admin)), 
                                          fail_map.get(Usage_Authority_TestKeys.Member_Info), 
                                          fail_map.get(Usage_Authority_TestKeys.Facility), 
                                          fail_map.get(Usage_Authority_TestKeys.Musical_Score), 
                                          fail_map.get(Usage_Authority_TestKeys.Sound_Source))
                                  .build();

      operation_list.add(operation);
      search_map.put(i++, fail_map.get(Usage_Authority_TestKeys.Auth_Id));
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
    operation_list.add(TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }










  /** 
   **********************************************************************************************
   * @brief エラーテストケースを全て格納したリストを作成する。
   * 
   * @details
   * - テストケースに登録されている全てのエラーケースをリスト化したものを作成する。
   * - このテストケースは、参照元のテーブルがエラーテストケースを格納した状態に初期化されている事が条件の元、
   * 使用可能。参照制約があるため、その条件を満たせないとテストできない。
   * - ただし、データベースに格納できないようなエラーケース（例、列挙型や日付型）は対象外。
   * これは、データベースに格納する前に制約に違反し保存できない為、テーブル内にテストケースを用意できないから。
   * 
   * @return エラーケースのマップが順番に格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、エラーテストケースのみを取り出す。
   * -# 権限番号が参照元テーブルの権限番号を参照できるように、「権限番号がエラーのケース」以外のエラーケースに
   * 関しては、確実に参照できてエラーが起こらない権限番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<Usage_Authority_TestKeys, String>> getDatabaseFail(){
    List<Map<Usage_Authority_TestKeys, String>> error_list = new ArrayList<>();

    error_list.addAll(makeAuthNameFailedData());
    error_list.addAll(makeAuthKindFailedData());

    int i = 1;
    for(Map<Usage_Authority_TestKeys, String> id_override: error_list){
      String auth_id = id_override.get(Usage_Authority_TestKeys.Auth_Id);
      auth_id = auth_id.replaceFirst("....$", "") + "_" + i;

      id_override.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      i++;
    }

    error_list.addAll(makeAuthIdFailedData());

    return error_list;
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
  private List<Map<Usage_Authority_TestKeys, String>> makeBaseData(){

    List<Map<Usage_Authority_TestKeys, String>> base_list = new ArrayList<>();
    String childkey = "ok";

    String auth_id = this.form_yml.getAuth_id().get(childkey);
    String auth_name = this.form_yml.getAuth_name().get(childkey);

    Map<Usage_Authority_TestKeys, String> only_admin = new HashMap<>();
    only_admin.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
    only_admin.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
		only_admin.put(Usage_Authority_TestKeys.Admin, this.form_yml.getAuth_ok().get("auth_ok_admin").get("admin"));
		only_admin.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ok().get("auth_ok_admin").get("member_info"));
		only_admin.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ok().get("auth_ok_admin").get("facility"));
		only_admin.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ok().get("auth_ok_admin").get("musical_score"));
		only_admin.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ok().get("auth_ok_admin").get("sound_source"));
    base_list.add(only_admin);

    String[] key_name = {"auth_ok_1", "auth_ok_2", "auth_ok_3", "auth_ok_4", "auth_ok_5"};

    for(String key: key_name){
      Map<Usage_Authority_TestKeys, String> not_admin = new HashMap<>();
      not_admin.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      not_admin.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      not_admin.put(Usage_Authority_TestKeys.Admin, this.form_yml.getAuth_ok().get(key).get("admin"));
      not_admin.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ok().get(key).get("other"));
      not_admin.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ok().get(key).get("other"));
      base_list.add(not_admin);
    }

    return base_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[権限番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Usage_Authority_TestKeys, String>> makeAuthIdFailedData(){

    List<Map<Usage_Authority_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<Usage_Authority_TestKeys, String>> ok_list = makeBaseData();

      for(Map<Usage_Authority_TestKeys, String> base_data: ok_list){
        base_data.put(Usage_Authority_TestKeys.Auth_Id, this.form_yml.getAuth_id().get(key));
        error_list.add(base_data);
      }
    }

    return error_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[権限名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Usage_Authority_TestKeys, String>> makeAuthNameFailedData(){

    List<Map<Usage_Authority_TestKeys, String>> error_list = new ArrayList<>();

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
      List<Map<Usage_Authority_TestKeys, String>> ok_list = makeBaseData();

      for(Map<Usage_Authority_TestKeys, String> base_data: ok_list){
        base_data.put(Usage_Authority_TestKeys.Auth_Name, this.form_yml.getAuth_name().get(key));
        error_list.add(base_data);
      }
    }

    return error_list;
  }










  /** 
   **********************************************************************************************
   * @brief 項目[権限内容]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<Usage_Authority_TestKeys, String>> makeAuthKindFailedData(){

    List<Map<Usage_Authority_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"admin_1", "admin_2", "admin_3", "admin_4", "admin_5"};

    for(String key: key_name){
      String admin = this.form_yml.getAuth_ng().get("auth_ng_" + key).get("admin");
      String auth_id = makeBaseData().get(0).get(Usage_Authority_TestKeys.Auth_Id);
      String auth_name = makeBaseData().get(0).get(Usage_Authority_TestKeys.Auth_Name);

      Map<Usage_Authority_TestKeys, String> member_info = new HashMap<>();
      member_info.put(Usage_Authority_TestKeys.Auth_Id,auth_id);
      member_info.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      member_info.put(Usage_Authority_TestKeys.Admin, admin);
      member_info.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      member_info.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      member_info.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      member_info.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      error_list.add(member_info);

      Map<Usage_Authority_TestKeys, String> facility = new HashMap<>();
      facility.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      facility.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      facility.put(Usage_Authority_TestKeys.Admin, admin);
      facility.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      facility.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      facility.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      facility.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      error_list.add(facility);

      Map<Usage_Authority_TestKeys, String> musical_score = new HashMap<>();
      musical_score.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      musical_score.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      musical_score.put(Usage_Authority_TestKeys.Admin, admin);
      musical_score.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      musical_score.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      musical_score.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      musical_score.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      error_list.add(musical_score);

      Map<Usage_Authority_TestKeys, String> sound_source = new HashMap<>();
      sound_source.put(Usage_Authority_TestKeys.Auth_Id, auth_id);
      sound_source.put(Usage_Authority_TestKeys.Auth_Name, auth_name);
      sound_source.put(Usage_Authority_TestKeys.Admin, admin);
      sound_source.put(Usage_Authority_TestKeys.Member_Info, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Facility, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Musical_Score, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("other"));
      sound_source.put(Usage_Authority_TestKeys.Sound_Source, this.form_yml.getAuth_ng().get("auth_ng_" + key).get("target"));
      error_list.add(sound_source);
    }

    return error_list;
  }
}
