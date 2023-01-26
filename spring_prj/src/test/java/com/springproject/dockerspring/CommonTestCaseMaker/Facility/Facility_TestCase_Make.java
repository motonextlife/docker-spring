/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Facility
 * 
 * @brief テストケース生成クラスのうち、[設備管理]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Facility;





/** 
 **************************************************************************************
 * @file Facility_TestCase_Make.java
 * @brief 主に[設備管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Csv.Facility_Csv_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Database.Facility_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.FailDatabase.Facility_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Form.Facility_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.RepoForm.Facility_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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
 * @see Facility_Form_TestCase
 * @see Facility_RepoForm_TestCase
 * @see Facility_Csv_TestCase
 * @see Facility_Database_TestCase
 * @see Facility_Fail_Database_TestCase
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_TestCase_Make{
  
  private final Facility_Form_TestCase form_clazz;
  private final Facility_RepoForm_TestCase repoform_clazz;
  private final Facility_Csv_TestCase csv_clazz;
  private final Facility_Database_TestCase database_clazz;
  private final Facility_Fail_Database_TestCase fail_database_clazz;
  private final History_TestCase_Make hist_testcase_clazz;









  /** 
   **********************************************************************************************
   * @brief 設備情報を関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum Facility_TestKeys {
    Serial_Num,
    Faci_Id,
    Faci_Name,
    Buy_Date,
    Producer,
    Storage_Loc,
    Disp_Date,
    Other_Comment;
  }











  /** @name [設備情報のフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Facility_TestKeys, String> getNormalData(){
    return this.form_clazz.getNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Facility_TestKeys, String> getOkData(Boolean new_tolerance){
    return this.form_clazz.getOkData(new_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){
    return this.form_clazz.getSerialNumFailedData(null_tolelance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getFaciIdFailedData(Boolean unique_tolerance){
    return this.form_clazz.getFaciIdFailedData(unique_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getFaciNameFailedData(){
    return this.form_clazz.getFaciNameFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getBuyDateFailedData(Boolean output_valid){
    return this.form_clazz.getBuyDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getProducerFailedData(){
    return this.form_clazz.getProducerFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getStolageLocFailedData(){
    return this.form_clazz.getStolageLocFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getDispDateFailedData(Boolean output_valid){
    return this.form_clazz.getDispDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getOtherCommentFailedData(){
    return this.form_clazz.getOtherCommentFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<History_TestKeys, String> getHistoryNormalData(){
    return this.hist_testcase_clazz.getHistoryNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryOkData(){
    return this.hist_testcase_clazz.getHistoryOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getHistoryIdFailedData(Boolean output_valid){
    return this.hist_testcase_clazz.getHistoryIdFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeDatetimeFailedData(){
    return this.hist_testcase_clazz.getChangeDatetimeFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeKindsFailedData(){
    return this.hist_testcase_clazz.getChangeKindsFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getOperationUserFailedData(){
    return this.hist_testcase_clazz.getOperationUserFailedData();
  }

  /** @} */











  /** @name [設備情報のCSV入出力テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvNormalData(){
    return this.csv_clazz.getCsvNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvOkData(){
    return this.csv_clazz.getCsvOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public List<String> getCsvFailedData(Boolean form_only){
    return this.csv_clazz.getCsvFailedData(form_only);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvValidError(){
    return this.csv_clazz.getCsvValidError();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public LinkedHashMap<Integer, List<Integer>> getCsvErrorPos(){
    return this.csv_clazz.getCsvErrorPos();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getOutputCsv(Boolean tmp_file){
    return this.csv_clazz.getOutputCsv(tmp_file);
  }

  /** @} */











  /** @name [設備情報の検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.subjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public Integer allRepoForm(Integer testcase_num){
    return this.repoform_clazz.allRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] otherRepoForm(Facility_TestKeys subject_key, Integer testcase_num){
    return this.repoform_clazz.otherRepoForm(subject_key, testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public String historySubjectRepoForm(Integer testcase_num){
    return this.hist_testcase_clazz.historySubjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public String[] historyOtherRepoForm(History_TestKeys subject_key, Integer testcase_num){
    return this.hist_testcase_clazz.historyOtherRepoForm(subject_key, testcase_num);
  }

  /** @} */














  /** @name [設備情報のデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseSetup() throws IOException{
    this.database_clazz.databaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseReset() throws IOException{
    this.database_clazz.databaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility compareEntityMake(Integer testcase_num){
    return this.database_clazz.compareEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility UpdateAfterEntityMake(Integer testcase_num) throws ParseException{
    return this.database_clazz.UpdateAfterEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility justInTimeEntityMake(){
    return this.database_clazz.justInTimeEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility okNullEntityMake(){
    return this.database_clazz.okNullEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Facility> failedEntityMake(){
    return this.database_clazz.failedEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility UniqueMissEntityMake(){
    return this.database_clazz.UniqueMissEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup() throws IOException{
    this.database_clazz.historyDatabaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseReset() throws IOException{
    this.database_clazz.historyDatabaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_History compareHistoryEntityMake(Integer testcase_num){
    return this.database_clazz.compareHistoryEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_History justInTimeHistoryEntityMake(){
    return this.database_clazz.justInTimeHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_History okNullHistoryEntityMake(){
    return this.database_clazz.okNullHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Facility_History> failedHistoryEntityMake(){
    return this.database_clazz.failedHistoryEntityMake();
  }

  /** @} */











  /** @name [設備情報のエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup() throws IOException, ParseException{
    return this.fail_database_clazz.failDatabaseSetup();
  }
  
  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void failDatabaseReset() throws IOException{
    this.fail_database_clazz.failDatabaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup() throws IOException, ParseException{
    return this.fail_database_clazz.historyFailDatabaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyFailDatabaseReset() throws IOException{
    this.fail_database_clazz.historyFailDatabaseReset();
  }


public void historyDatabaseReset(boolean b) {
}
  
  /** @} */
}
