/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Database
 * 
 * @brief [システムユーザー管理]に関するテストケース生成処理のうち、[データベース]
 * 関連のテストケースを生成する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージには、テストケースを記述したYAMLファイルとマッピングするエンティティや、
 * 取り込んだテストケースをデータベースにセットアップしたりする機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Database;





/** 
 **************************************************************************************
 * @file System_User_Database_TestCase.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
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
import java.util.ArrayList;
import java.util.List;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;









/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、データベースとの通信テストの際に、あらかじめ
 * データベース内に初期値データを格納してセットアップするクラス。
 * 
 * @details 
 * - このクラスでは、データベースの初期化のほか、初期化時に使用したデータをテスト時の比較用として
 * モックのエンティティに格納して返却が可能。
 * - データベースへの保存に失敗するテストケースを、モックのエンティティに格納して返却が可能。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import(ConfigureFactory.class)
 * 
 * @see System_User_Database_Yaml
 * @see ConfigureFactory
 **************************************************************************************
 */ 
@Component
@Import(ConfigureFactory.class)
public class System_User_Database_TestCase{
  
  private final System_User_Database_Yaml database_yml;
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
   * @see System_User_Database_Yaml
   * @see ConfigureFactory
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  @Autowired
  public System_User_Database_TestCase(ConfigureFactory config_factory) throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/SystemUser/system-user-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, System_User_Database_Yaml.class);
    }

    this.config_factory = config_factory;
  }







  




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
    operation_list.add(TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    for(int i = 1; i <= 10; i++){
      Operation operation = Insert.into(TABLE_NAME)
                                  .columns("serial_num",
                                          "member_id",
                                          "username",
                                          "password",
                                          "auth_id",
                                          "fail_count",
                                          "locking")
                                  .values(i,
                                          this.database_yml.getMember_id().get("case_" + i),
                                          this.database_yml.getUsername().get("case_" + i),
                                          this.database_yml.getPassword().get("case_" + i),
                                          this.database_yml.getAuth_id().get("case_" + i),
                                          this.database_yml.getFail_count().get("case_" + i),
                                          this.database_yml.getLocking().get("case_" + i)
                                          )
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
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void databaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(TABLE);
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
  public System_User compareEntityMake(Integer testcase_num){

    if(testcase_num > 10 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      System_User mock_entity = mock(System_User.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("case_" + testcase_num));
      when(mock_entity.getUsername()).thenReturn(this.database_yml.getUsername().get("case_" + testcase_num));
      when(mock_entity.getPassword()).thenReturn(this.database_yml.getPassword().get("case_" + testcase_num));
      when(mock_entity.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get("case_" + testcase_num));
      when(mock_entity.getFail_count()).thenReturn(this.database_yml.getFail_count().get("case_" + testcase_num));
      when(mock_entity.getLocking()).thenReturn(this.database_yml.getLocking().get("case_" + testcase_num));

      return mock_entity;
    }
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリでの更新処理の際に使用する、更新後のデータのモックエンティティを作成する。
   * 
   * @details
   * - 更新後のデータの内容としては、明らかに既存のテストケースの値とは被らないような適当な値を用いている。
   * - ただし権限番号に関しては参照制約があり、適当な値が入れれないので、若干既存のテストケースと被る。
   * - 団員番号に関しては参照制約かつ一意制約があるので、そのままの値として変更しない。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return データが格納されているモックエンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 適当な被らない値を、作成したモックオブジェクトに格納する。
   * -# 格納が終わったモックを、戻り値とする。
   **********************************************************************************************
   */ 
  public System_User UpdateAfterEntityMake(Integer testcase_num){

    if(testcase_num > 10 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      System_User mock_entity = mock(System_User.class);

      when(mock_entity.getSerial_num()).thenReturn(testcase_num);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("case_" + testcase_num));
      when(mock_entity.getUsername()).thenReturn("Blank" + testcase_num);
      when(mock_entity.getPassword()).thenReturn("Blank");
      when(mock_entity.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get("case_1"));
      when(mock_entity.getFail_count()).thenReturn(0);
      when(mock_entity.getLocking()).thenReturn(false);

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
  public System_User justInTimeEntityMake(){

    System_User mock_entity = mock(System_User.class);

    when(mock_entity.getSerial_num()).thenReturn(1);
    when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ok_length"));
    when(mock_entity.getUsername()).thenReturn(this.database_yml.getUsername().get("ok_length"));
    when(mock_entity.getPassword()).thenReturn(this.database_yml.getPassword().get("ok_length"));
    when(mock_entity.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get("ok_length"));
    when(mock_entity.getFail_count()).thenReturn(255);
    when(mock_entity.getLocking()).thenReturn(false);

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
  public List<System_User> failedEntityMake(){

    List<System_User> fail_mock_list = new ArrayList<>();
    int i = 1;

    String[] member_id = {"ng_overflow", "ng_foreign", "ng_null"};
    for(String key_name: member_id){
      System_User mock_entity = compareEntityMake(1);

      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getMember_id()).thenReturn(this.database_yml.getMember_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }
    
    String[] username = {"ng_overflow", "ng_null"};
    for(String key_name: username){
      System_User mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getUsername()).thenReturn(this.database_yml.getUsername().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] password = {"ng_overflow", "ng_null"};
    for(String key_name: password){
      System_User mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getPassword()).thenReturn(this.database_yml.getPassword().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] auth_id = {"ng_overflow", "ng_foreign", "ng_null"};
    for(String key_name: auth_id){
      System_User mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] fail_count = {"ng_null"};
    for(String key_name: fail_count){
      System_User mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getFail_count()).thenReturn(this.database_yml.getFail_count().get(key_name));

      fail_mock_list.add(mock_entity);
    }

    String[] locking = {"ng_null"};
    for(String key_name: locking){
      System_User mock_entity = compareEntityMake(1);
            
      when(mock_entity.getSerial_num()).thenReturn(i++);
      when(mock_entity.getLocking()).thenReturn(this.database_yml.getLocking().get(key_name));

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
  public List<System_User> UniqueMissEntityMake(){

    List<System_User> fail_list = new ArrayList<>();

    System_User mock_entity_member = mock(System_User.class);

    when(mock_entity_member.getSerial_num()).thenReturn(11);
    when(mock_entity_member.getMember_id()).thenReturn(this.database_yml.getMember_id().get("ng_unique"));
    when(mock_entity_member.getUsername()).thenReturn(this.database_yml.getUsername().get("case_1"));
    when(mock_entity_member.getPassword()).thenReturn(this.database_yml.getPassword().get("case_1"));
    when(mock_entity_member.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get("case_1"));
    when(mock_entity_member.getFail_count()).thenReturn(255);
    when(mock_entity_member.getLocking()).thenReturn(false);

    System_User mock_entity_auth = mock(System_User.class);

    when(mock_entity_auth.getSerial_num()).thenReturn(1);
    when(mock_entity_auth.getMember_id()).thenReturn(this.database_yml.getMember_id().get("case_1"));
    when(mock_entity_auth.getUsername()).thenReturn(this.database_yml.getUsername().get("ng_unique"));
    when(mock_entity_auth.getPassword()).thenReturn(this.database_yml.getPassword().get("case_1"));
    when(mock_entity_auth.getAuth_id()).thenReturn(this.database_yml.getAuth_id().get("case_1"));
    when(mock_entity_auth.getFail_count()).thenReturn(255);
    when(mock_entity_auth.getLocking()).thenReturn(false);

    fail_list.add(mock_entity_member);
    fail_list.add(mock_entity_auth);

    return fail_list;
  }
}
