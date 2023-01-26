/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority
 * 
 * @brief テストケース生成クラスのうち、[権限管理]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority;





/** 
 **************************************************************************************
 * @file Usage_Authority_TestCase_Make.java
 * @brief 主に[権限管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Database.Usage_Authority_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.FailDatabase.Usage_Authority_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Form.Usage_Authority_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.RepoForm.Usage_Authority_RepoForm_TestCase;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[権限管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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
 * @see Usage_Authority_Form_TestCase
 * @see Usage_Authority_RepoForm_TestCase
 * @see Usage_Authority_Database_TestCase
 * @see Usage_Authority_Fail_Database_TestCase
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Usage_Authority_TestCase_Make{
  
  private final Usage_Authority_Form_TestCase form_clazz;
  private final Usage_Authority_RepoForm_TestCase repoform_clazz;
  private final Usage_Authority_Database_TestCase database_clazz;
  private final Usage_Authority_Fail_Database_TestCase fail_database_clazz;






  
  /** 
   **********************************************************************************************
   * @brief 権限情報を関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum Usage_Authority_TestKeys {
    Serial_Num,
    Auth_Id,
    Auth_Name,
    Admin,
    Member_Info,
    Facility,
    Musical_Score,
    Sound_Source;
  }









  /** @name [権限情報のフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Usage_Authority_TestKeys, String> getNormalData(){
    return this.form_clazz.getNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<Usage_Authority_TestKeys, String>> getOkData(Boolean new_tolerance){
    return this.form_clazz.getOkData(new_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){
    return this.form_clazz.getSerialNumFailedData(null_tolelance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getAuthIdFailedData(Boolean unique_tolerance){
    return this.form_clazz.getAuthIdFailedData(unique_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getAuthNameFailedData(){
    return this.form_clazz.getAuthNameFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<Usage_Authority_TestKeys, String>> getAuthKindFailedData(){
    return this.form_clazz.getAuthKindFailedData();
  }










  /** @name [権限情報の検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.subjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public Integer allRepoForm(Integer testcase_num){
    return this.repoform_clazz.allRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] otherRepoForm(Usage_Authority_TestKeys subject_key, Integer testcase_num){
    return this.repoform_clazz.otherRepoForm(subject_key, testcase_num);
  }

  /** @} */












  /** @name [権限情報のデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseSetup() throws IOException{
    this.database_clazz.databaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseReset() throws IOException{
    this.database_clazz.databaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public Usage_Authority compareEntityMake(Integer testcase_num){
    return this.database_clazz.compareEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public Usage_Authority UpdateAfterEntityMake(Integer testcase_num){
    return this.database_clazz.UpdateAfterEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public Usage_Authority justInTimeEntityMake(){
    return this.database_clazz.justInTimeEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Usage_Authority> failedEntityMake(){
    return this.database_clazz.failedEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Database_TestCase
   **********************************************************************************************
   */ 
  public Usage_Authority UniqueMissEntityMake(){
    return this.database_clazz.UniqueMissEntityMake();
  }

  /** @} */












  /** @name [権限情報のエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup(){
    return this.fail_database_clazz.failDatabaseSetup();
  }
  
  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Usage_Authority_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void failDatabaseReset(){
    this.fail_database_clazz.failDatabaseReset();
  }

  /** @} */
}
