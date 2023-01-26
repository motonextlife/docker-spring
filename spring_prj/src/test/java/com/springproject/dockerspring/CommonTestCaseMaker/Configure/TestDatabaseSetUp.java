/** 
 **************************************************************************************
 * @file TestDatabaseSetUp.java
 * @brief テストコード全体において、データベースを使用可能なように接続情報などを定義して、
 * 初期化をするクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Configure;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.CompositeOperation;
import com.ninja_squad.dbsetup.operation.Operation;








/** 
 **************************************************************************************
 * @brief テストコード全体において、データベースを使用可能なように接続情報などを定義して、
 * 初期化をするクラスを格納するファイル。
 * 
 * @details 
 * - データベースへのアクセスのための接続情報を、インスタンスの作成の際に定義する。
 * - このクラスへのSQLの実行指令があったら、それを実行する。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * - 本番環境用のデータベースと、モック用内蔵データベースの、両方の接続情報定義で使用する。
 * 
 * @par 使用アノテーション
 * - @Component
 **************************************************************************************
 */ 
@Component
public class TestDatabaseSetUp {

  private final Destination destination;







  /** 
   **********************************************************************************************
   * @brief データベースへの接続情報の設定を行う。
   * 
   * @details
   * - 環境変数に指定されたURLをもとに定義する。
   * - 接続情報は、本番環境用データベースのほかに、モック用内蔵データベースの両方の設定を行う。
   * 
   * @param[in] date_source テスト用の環境変数設定ファイル。
   * 
   * @par 処理の大まかな流れ
   * -# 本番環境用データベースの、ドライバーマネージャを作成。
   * -# モック用内蔵データベースの、ドライバーマネージャを作成。
   * -# この段階では実行などは特にせず、フィールド変数に両方を格納。
   * 
   * @throw IOException
   * 
   * @see TestDataSource
   **********************************************************************************************
   */ 
  @Autowired
  public TestDatabaseSetUp(TestDataSource date_source) throws IOException{
    this.destination = new DriverManagerDestination(date_source.getUrl(), 
                                                    date_source.getUsername(), 
                                                    date_source.getPassword());
  }









  /** 
   **********************************************************************************************
   * @brief 引数で渡されたSQLの実行を行う。
   * 
   * @details
   * - 指定されたSQLの格納リストを読み取って、実行する。
   * - 真偽値フラグの値を元に、本番環境用かモック用内蔵か選べる。
   * 
   * @param[in] operation 指定されたSQLの格納リスト
   * 
   * @par 処理の大まかな流れ
   * -# 指定されたSQLの格納リストを連結し、実行可能な状態に変換する。
   * -# 真偽値フラグを判定し、それに応じてドライバマネージャーを選択し、実行。なお、
   **********************************************************************************************
   */ 
  public void databaseExec(List<Operation> operation){
    Operation join_operation = CompositeOperation.sequenceOf(operation);
    new DbSetup(this.destination, join_operation).launch();
  }
}
