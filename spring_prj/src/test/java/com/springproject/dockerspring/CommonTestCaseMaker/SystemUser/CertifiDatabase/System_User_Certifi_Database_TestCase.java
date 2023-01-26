/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.CertifiDatabase
 * 
 * @brief テストケース生成クラスのうち、[アカウント認証用のデータベース]に関するテストケースを
 * 生成する処理のパッケージ
 * 
 * @details
 * - このパッケージは、アカウントの認証テストの際にデータベースにあらかじめ準備しておくデータを
 * データベースにセットアップするクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.CertifiDatabase;





/** 
 **************************************************************************************
 * @file System_User_Certifi_Database_TestCase.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、ログイン機能のテストの際に使用する
 * データをデータベースにセットアップするクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import com.ninja_squad.dbsetup.operation.SqlOperation;
import com.ninja_squad.dbsetup.operation.Truncate;
import com.springproject.dockerspring.CommonTestCaseMaker.Configure.ConfigureFactory;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Database.System_User_Database_Yaml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;








/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、ログイン機能のテストの際に使用する
 * データをデータベースにセットアップするクラス。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @Import(ConfigureFactory.class)
 * 
 * @see System_User_Database_Yaml
 * @see System_User_Samba_TestCase
 **************************************************************************************
 */ 
@Component
@Import(ConfigureFactory.class)
public class System_User_Certifi_Database_TestCase{
  
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
  public System_User_Certifi_Database_TestCase(ConfigureFactory config_factory) throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/SystemUser/system-user-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, System_User_Database_Yaml.class);
    }

    this.config_factory = config_factory;
  }











  /** 
   **********************************************************************************************
   * @brief ログイン検索時に用いるデータベースに、認証情報テストケースを保存する。
   * 
   * @details
   * - 処理後には、検査で使用する際のパスワード（ユーザー名と兼用）を返却し、ログインテストに使用できる。
   * 選ぶことが可能である。
   * 
   * @return 保存したハッシュ化前のパスワード（ユーザー名と兼用）
   * 
   * @par 処理の大まかな流れ
   * -# 保存前に保存対象のテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * -# テストケースに保存されている、ハッシュ化前のパスワードを返却する。
   **********************************************************************************************
   */ 
  public List<String> cetifiDatabaseSetup(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    List<String> username_list = new ArrayList<>();

    for(int i = 1; i <= 10; i++){
      String username = this.database_yml.getCertification().get("case_" + i)[0];
      username_list.add(username);

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
                                          username,
                                          this.database_yml.getCertification().get("case_" + i)[1],
                                          this.database_yml.getAuth_id().get("case_" + i),
                                          this.database_yml.getFail_count().get("case_" + i),
                                          this.database_yml.getLocking().get("case_" + i)
                                          )
                                  .build();

      operation_list.add(operation);
    }

    this.config_factory.databaseExec(operation_list);

    return username_list;
  }










  /** 
   **********************************************************************************************
   * @brief ログイン検索時に用いるデータベースに、認証情報テストケースを全削除してリセットする。
   * 
   * @par 処理の大まかな流れ
   * -# 対象のデータベースのテーブルを初期化するクエリを組み立てる。
   * -# 組み上げたクエリを実行する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void cetifiDatabaseReset(){

    List<Operation> operation_list = new ArrayList<>();

    operation_list.add(FOREIGN_KEY_CHECK_0);
    operation_list.add(TABLE);
    operation_list.add(FOREIGN_KEY_CHECK_1);

    this.config_factory.databaseExec(operation_list);
  }
}
