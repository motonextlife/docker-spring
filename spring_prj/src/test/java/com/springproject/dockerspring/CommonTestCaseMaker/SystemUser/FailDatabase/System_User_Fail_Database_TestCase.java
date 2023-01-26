/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.FailDatabase
 * 
 * @brief [システムユーザー管理]に関するテストケース生成処理のうち、[出力失敗ケースのデータベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.FailDatabase;





/** 
 **************************************************************************************
 * @file System_User_Fail_Database_TestCase.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Form.System_User_Form_Yaml;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;








/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
 * @see System_User_Form_Yaml
 * @see ConfigureFactory
 **************************************************************************************
 */ 
@Component
@Import(ConfigureFactory.class)
public class System_User_Fail_Database_TestCase{
  
  private final System_User_Form_Yaml form_yml;
  private final ConfigureFactory config_factory;


  private final SqlOperation FOREIGN_KEY_CHECK_0 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 0");
  private final SqlOperation FOREIGN_KEY_CHECK_1 = SqlOperation.of("SET FOREIGN_KEY_CHECKS = 1");
  private final String TABLE_NAME = "System_User";
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
   * @see System_User_Form_Yaml
   * @see ConfigureFactory
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public System_User_Fail_Database_TestCase(ConfigureFactory config_factory) throws IOException{

    try(InputStream in_form_yml = new ClassPathResource("TestCaseFile/SystemUser/system-user-form.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.form_yml = yaml.loadAs(in_form_yml, System_User_Form_Yaml.class);
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

    List<Map<System_User_TestKeys, String>> fail_list = getDatabaseFail();
    Map<Integer, String> search_map = new HashMap<>();

    int i = 1;
    for(Map<System_User_TestKeys, String> fail_map: fail_list){
      Operation operation = Insert.into(TABLE_NAME)
                                  .columns("serial_num",
                                          "member_id",
                                          "username",
                                          "password",
                                          "auth_id",
                                          "fail_count",
                                          "locking")
                                  .values(i,
                                          fail_map.get(System_User_TestKeys.Member_Id), 
                                          fail_map.get(System_User_TestKeys.Username), 
                                          fail_map.get(System_User_TestKeys.Password), 
                                          fail_map.get(System_User_TestKeys.Auth_Id), 
                                          5, 
                                          false)
                                  .build();

      operation_list.add(operation);
      search_map.put(i++, fail_map.get(System_User_TestKeys.Member_Id));
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
   * -# 団員番号が参照元テーブルの団員番号を参照できるように、「団員番号がエラーのケース」以外のエラーケースに
   * 関しては、確実に参照できてエラーが起こらない団員番号に上書きする。また、一意に保つ。
   * -# ユーザー名が一意制約に違反しないように、「ユーザー名がエラーのケース」以外のエラーケースに関しては、
   * 確実にエラーが起こらないユーザー名に上書きする。
   * -# 権限番号が参照元テーブルの権限番号を参照できるように、「権限番号がエラーのケース」以外のエラーケースに
   * 関しては、確実に参照できてエラーが起こらない権限番号に上書きする。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 各項目の格納が終わったリストを全て結合し、一つのリストにしたものを戻り値とする。
   **********************************************************************************************
   */ 
  private List<Map<System_User_TestKeys, String>> getDatabaseFail(){

    List<Map<System_User_TestKeys, String>> error_list = new ArrayList<>();

    Integer member_id_append_num = 1;
    Integer username_append_num = 1;

    List<Map<System_User_TestKeys, String>> tmp_member_id_list = makeMemberIdFailedData();
    List<Map<System_User_TestKeys, String>> tmp_username_list = makeUsernameFailedData();
    List<Map<System_User_TestKeys, String>> tmp_auth_id_list = makeAuthIdFailedData();


    BiConsumer<List<Map<System_User_TestKeys, String>>, Integer> member_override = (tmp_list, current_num) -> {

      for(Map<System_User_TestKeys, String> map: tmp_list){
        String member_id = map.get(System_User_TestKeys.Member_Id);
        member_id = member_id.replaceFirst("....$", "") + "_" + current_num;
  
        map.put(System_User_TestKeys.Member_Id, member_id);
        current_num++;
      }
    };


    BiConsumer<List<Map<System_User_TestKeys, String>>, Integer> username_override = (tmp_list, current_num) -> {

      for(Map<System_User_TestKeys, String> map: tmp_list){
        String username = map.get(System_User_TestKeys.Username);
        username = username.replaceFirst("....$", "") + "_" + current_num;
  
        map.put(System_User_TestKeys.Username, username);
        current_num++;
      }
    };


    Consumer<List<Map<System_User_TestKeys, String>>> auth_override = tmp_list -> {

      for(Map<System_User_TestKeys, String> map: tmp_list){
        String auth_id = map.get(System_User_TestKeys.Auth_Id);
        auth_id = auth_id.replaceFirst("....$", "") + "_" + 1;
  
        map.put(System_User_TestKeys.Auth_Id, auth_id);
      }
    };


    username_override.accept(tmp_member_id_list, username_append_num);
    auth_override.accept(tmp_member_id_list);

    member_override.accept(tmp_username_list, member_id_append_num);
    auth_override.accept(tmp_username_list);

    member_override.accept(tmp_auth_id_list, member_id_append_num);
    username_override.accept(tmp_auth_id_list, username_append_num);


    error_list.addAll(tmp_member_id_list);
    error_list.addAll(tmp_username_list);
    error_list.addAll(tmp_auth_id_list);

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
  private List<Map<System_User_TestKeys, String>> makeBaseData(){

    List<Map<System_User_TestKeys, String>> base_list = new ArrayList<>();
    String childkey = "ok";
    String[] key_name = {"_min", "_max"};

    for(String ok_word: key_name){
      Map<System_User_TestKeys, String> base_map = new HashMap<>();

      base_map.put(System_User_TestKeys.Member_Id, this.form_yml.getMember_id().get(childkey));
      base_map.put(System_User_TestKeys.Username, this.form_yml.getUsername().get(childkey + ok_word));
      base_map.put(System_User_TestKeys.Password, this.form_yml.getPassword().get(childkey + ok_word));
      base_map.put(System_User_TestKeys.Auth_Id, this.form_yml.getAuth_id().get(childkey));

      base_list.add(base_map);
    }

    return base_list;
  }











  /** 
   **********************************************************************************************
   * @brief 項目[団員番号]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<System_User_TestKeys, String>> makeMemberIdFailedData(){

    List<Map<System_User_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<System_User_TestKeys, String>> ok_list = makeBaseData();

      for(Map<System_User_TestKeys, String> base_data: ok_list){
        base_data.put(System_User_TestKeys.Member_Id, this.form_yml.getMember_id().get(key));
        error_list.add(base_data);
      }
    }

    return error_list;
  }









  /** 
   **********************************************************************************************
   * @brief 項目[ユーザー名]でエラーになるテストケースを格納したマップリストを作成する。
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
  private List<Map<System_User_TestKeys, String>> makeUsernameFailedData(){

    List<Map<System_User_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_less_than", 
                         "ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<System_User_TestKeys, String>> ok_list = makeBaseData();

      for(Map<System_User_TestKeys, String> base_data: ok_list){
        base_data.put(System_User_TestKeys.Username, this.form_yml.getUsername().get(key));
        error_list.add(base_data);
      }
    }

    return error_list;
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
  private List<Map<System_User_TestKeys, String>> makeAuthIdFailedData(){

    List<Map<System_User_TestKeys, String>> error_list = new ArrayList<>();

    String[] key_name = {"ng_overflow", 
                         "ng_symbol", 
                         "ng_zenkaku", 
                         "ng_hankaku_kana", 
                         "ng_empty", 
                         "ng_blank", 
                         "ng_blank_zenkaku", 
                         "ng_blank_tab", 
                         "ng_blank_newline"};

    for(String key: key_name){
      List<Map<System_User_TestKeys, String>> ok_list = makeBaseData();

      for(Map<System_User_TestKeys, String> base_data: ok_list){
        base_data.put(System_User_TestKeys.Auth_Id, this.form_yml.getAuth_id().get(key));
        error_list.add(base_data);
      }
    }

    return error_list;
  }
}
