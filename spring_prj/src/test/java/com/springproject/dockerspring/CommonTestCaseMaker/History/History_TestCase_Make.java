/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.History
 * 
 * @brief テストケース生成クラスのうち、[共通の履歴管理機能]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.History;





/** 
 **************************************************************************************
 * @file History_TestCase_Make.java
 * @brief 主に[共通の履歴機能]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonTestCaseMaker.History.Database.History_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.History.FailDatabase.History_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.History.Form.History_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.History.RepoForm.History_RepoForm_TestCase;

import lombok.RequiredArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[共通の履歴機能]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラス。
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
 * 
 * @see History_Database_TestCase
 * @see History_Fail_Database_TestCase
 * @see History_Form_TestCase
 * @see History_RepoForm_TestCase
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class History_TestCase_Make{
  
  private final History_Database_TestCase database_clazz;
  private final History_Fail_Database_TestCase fail_database_clazz;
  private final History_Form_TestCase form_clazz;
  private final History_RepoForm_TestCase repoform_clazz;







  /** 
   **********************************************************************************************
   * @brief 履歴情報データを関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum History_TestKeys {
    History_Id,
    Id,
    Change_Datetime,
    Change_Kinds,
    Operation_User;
  }










  /** @name [履歴データのフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<History_TestKeys, String> getHistoryNormalData(){
    return this.form_clazz.getHistoryNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryOkData(){
    return this.form_clazz.getHistoryOkData();
  }

  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getHistoryIdFailedData(Boolean output_valid){
    return this.form_clazz.getHistoryIdFailedData(output_valid);
  }

  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeDatetimeFailedData(){
    return this.form_clazz.getChangeDatetimeFailedData();
  }

  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeKindsFailedData(){
    return this.form_clazz.getChangeKindsFailedData();
  }

  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getOperationUserFailedData(){
    return this.form_clazz.getOperationUserFailedData();
  }

  /** @} */









  /** @name [履歴データの検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public String historySubjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.historySubjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Form_TestCase
   **********************************************************************************************
   */ 
  public String[] historyOtherRepoForm(History_TestKeys subject_key, Integer testcase_num){
    return this.repoform_clazz.historyOtherRepoForm(subject_key, testcase_num);
  }

  /** @} */









  /** @name [履歴データのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public String getCompareId(Integer testcase_num){
    return this.database_clazz.getCompareId(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public Date getCompareChange_datetime(Integer testcase_num){
    return this.database_clazz.getCompareChange_datetime(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public String getCompareChange_kinds(Integer testcase_num){
    return this.database_clazz.getCompareChange_kinds(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public String getCompareOperation_user(Integer testcase_num){
    return this.database_clazz.getCompareOperation_user(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Date> getFailChange_datetime(){
    return this.database_clazz.getFailChange_datetime();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public List<String> getFailChange_kinds(){
    return this.database_clazz.getFailChange_kinds();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Database_TestCase
   **********************************************************************************************
   */ 
  public List<String> getFailOperation_user(){
    return this.database_clazz.getFailOperation_user();
  }

  /** @} */





  
  /** @name [履歴データのエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryDatabaseFailData(){
    return this.fail_database_clazz.getHistoryDatabaseFailData();
  }  
  
  /** @} */
}
