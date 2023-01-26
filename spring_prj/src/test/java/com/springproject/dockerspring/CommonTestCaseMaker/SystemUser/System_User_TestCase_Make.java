/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser
 * 
 * @brief テストケース生成クラスのうち、[システムユーザー管理]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.SystemUser;





/** 
 **************************************************************************************
 * @file System_User_TestCase_Make.java
 * @brief 主に[システムユーザー管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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

import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.CertifiDatabase.System_User_Certifi_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Database.System_User_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.FailDatabase.System_User_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.Form.System_User_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm.System_User_RepoForm_TestCase;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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
 * @see System_User_Form_TestCase
 * @see System_User_RepoForm_TestCase
 * @see System_User_Database_TestCase
 * @see System_User_Fail_Database_TestCase
 * @see System_User_Certifi_Database_TestCase
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class System_User_TestCase_Make{
  
  private final System_User_Form_TestCase form_clazz;
  private final System_User_RepoForm_TestCase repoform_clazz;
  private final System_User_Database_TestCase database_clazz;
  private final System_User_Fail_Database_TestCase fail_database_clazz;
  private final System_User_Certifi_Database_TestCase certifi_clazz;







  

  /** 
   **********************************************************************************************
   * @brief システムユーザー情報を関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum System_User_TestKeys {
    Serial_Num,
    Member_Id,
    Username,
    Password,
    Auth_Id,
    Fail_Count,
    Locking;
  }










  /** @name [システムユーザー情報のフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<System_User_TestKeys, String> getNormalData(){
    return this.form_clazz.getNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<System_User_TestKeys, String>> getOkData(Boolean new_tolerance){
    return this.form_clazz.getOkData(new_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){
    return this.form_clazz.getSerialNumFailedData(null_tolelance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getMemberIdFailedData(Boolean unique_tolerance, Boolean output_valid){
    return this.form_clazz.getMemberIdFailedData(unique_tolerance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getUsernameFailedData(Boolean unique_tolerance){
    return this.form_clazz.getUsernameFailedData(unique_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getPasswordFailedData(Boolean output_valid){
    return this.form_clazz.getPasswordFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getAuthIdFailedData(Boolean output_valid){
    return this.form_clazz.getAuthIdFailedData(output_valid);
  }

  /** @} */












  /** @name [システムユーザー情報の検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.subjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public Integer allRepoForm(Integer testcase_num){
    return this.repoform_clazz.allRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] existRemainRepoForm(System_User_TestKeys subject_key, Integer testcase_num){
    return this.repoform_clazz.existRemainRepoForm(subject_key, testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] otherRepoForm(System_User_TestKeys subject_key, Integer testcase_num){
    return this.repoform_clazz.otherRepoForm(subject_key, testcase_num);
  }

  /** @} */










  /** @name [システムユーザー情報のデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseSetup() throws IOException{
    this.database_clazz.databaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseReset() throws IOException{
    this.database_clazz.databaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public System_User compareEntityMake(Integer testcase_num){
    return this.database_clazz.compareEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public System_User UpdateAfterEntityMake(Integer testcase_num){
    return this.database_clazz.UpdateAfterEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public System_User justInTimeEntityMake(){
    return this.database_clazz.justInTimeEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public List<System_User> failedEntityMake(){
    return this.database_clazz.failedEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Database_TestCase
   **********************************************************************************************
   */ 
  public List<System_User> UniqueMissEntityMake(){
    return this.database_clazz.UniqueMissEntityMake();
  }

  /** @} */













  /** @name [システムユーザー情報のエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup(){
    return this.fail_database_clazz.failDatabaseSetup();
  }
  
  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void failDatabaseReset(){
    this.fail_database_clazz.failDatabaseReset();
  }

  /** @} */











  /** @name [システムユーザー情報のデータベース認証テストケース]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Certifi_Database_TestCase
   **********************************************************************************************
   */ 
  public List<String> certifiDatabaseSetup(){
    return this.certifi_clazz.cetifiDatabaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see System_User_Certifi_Database_TestCase
   **********************************************************************************************
   */ 
  public void cetifiDatabaseReset(){
    this.certifi_clazz.cetifiDatabaseReset();
  }

  /** @} */
}
