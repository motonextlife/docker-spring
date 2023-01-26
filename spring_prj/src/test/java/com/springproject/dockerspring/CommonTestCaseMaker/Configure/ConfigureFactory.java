/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Configure
 * 
 * @brief テストケース生成クラスで用いる、データベース接続情報の設定や、ファイルサーバーへの
 * 接続情報の設定を行うクラスを格納する。
 * 
 * @details
 * - このパッケージは、プロダクトコード側のデータソース設定とは独立して接続情報を確立させるために
 * 設けたクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Configure;






/** 
 **************************************************************************************
 * @file ConfigureFactory.java
 * @brief テストケースに必要な設定の全般を行うクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.ninja_squad.dbsetup.operation.Operation;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief テストケースに必要な設定の全般を行うクラス。
 * 
 * @details 
 * - 当該パッケージ内で作成された各テストケースクラスをインジェクションし、このクラスから共通して
 * 使えるようにする。
 * - 各テストケースを集約し、メソッドを中継することが目的のクラスである。
 * - 各テストケースクラスのインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @ComponentScan
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigureFactory {

  private final TestDatabaseSetUp test_database_setup;
  private final TestSambaSetUp test_samba_setup;






  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see TestSambaSetUp
   **********************************************************************************************
   */ 
  public String sambaOutput(String read_file_name, String output_file_path) throws IOException{
    return this.test_samba_setup.sambaOutput(read_file_name, output_file_path);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see TestSambaSetUp
   **********************************************************************************************
   */ 
  public String sambaHistoryOutput(String read_file_name, String output_file_path) throws IOException{
    return this.test_samba_setup.sambaHistoryOutput(read_file_name, output_file_path);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see TestSambaSetUp
   **********************************************************************************************
   */ 
  public void sambaClear(String delete_dir_path) throws IOException{
    this.test_samba_setup.sambaClear(delete_dir_path);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see TestDatabaseSetUp
   **********************************************************************************************
   */ 
  public void databaseExec(List<Operation> operation){
    this.test_database_setup.databaseExec(operation);
  }
}
