/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo
 * 
 * @brief テストケース生成クラスのうち、[団員管理]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo;





/** 
 **************************************************************************************
 * @file Member_Info_TestCase_Make.java
 * @brief 主に[団員管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Csv.Member_Info_Csv_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Database.Member_Info_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.FailDatabase.Member_Info_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Form.Member_Info_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.RepoForm.Member_Info_RepoForm_TestCase;
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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
 * @see Member_Info_Form_TestCase
 * @see Member_Info_RepoForm_TestCase
 * @see Member_Info_Csv_TestCase
 * @see Member_Info_Database_TestCase
 * @see Member_Info_Fail_Database_TestCase
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Member_Info_TestCase_Make{
  
  private final Member_Info_Form_TestCase form_clazz;
  private final Member_Info_RepoForm_TestCase repoform_clazz;
  private final Member_Info_Csv_TestCase csv_clazz;
  private final Member_Info_Database_TestCase database_clazz;
  private final Member_Info_Fail_Database_TestCase fail_database_clazz;
  private final History_TestCase_Make hist_testcase_clazz;









  /** 
   **********************************************************************************************
   * @brief 団員情報を関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum Member_Info_TestKeys {
    Serial_Num,
    Member_Id,
    Name,
    Name_Pronu,
    Sex,
    Birthday,
    Face_Photo,
    Join_Date,
    Ret_Date,
    Email,
    Tel,
    Addr_Postcode,
    Addr,
    Position,
    Position_Arri_Date,
    Job,
    Assign_Dept,
    Assign_Date,
    Inst_Charge,
    Other_Comment;
  }









  /** @name [団員情報のフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Member_Info_TestKeys, String> getNormalData(){
    return this.form_clazz.getNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<Member_Info_TestKeys, String>> getOkData(Boolean new_tolerance){
    return this.form_clazz.getOkData(new_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){
    return this.form_clazz.getSerialNumFailedData(null_tolelance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getMemberIdFailedData(Boolean unique_tolerance){
    return this.form_clazz.getMemberIdFailedData(unique_tolerance);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getNameFailedData(){
    return this.form_clazz.getNameFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getNamePronuFailedData(){
    return this.form_clazz.getNamePronuFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getSexFailedData(){
    return this.form_clazz.getSexFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getBirthdayFailedData(Boolean output_valid){
    return this.form_clazz.getBirthdayFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getFacePhotoFailedData(Boolean output_valid){
    return this.form_clazz.getFacePhotoFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getJoinDateFailedData(Boolean output_valid){
    return this.form_clazz.getJoinDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getRetDateFailedData(Boolean output_valid){
    return this.form_clazz.getRetDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getEmailFailedData(){
    return this.form_clazz.getEmailFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getTelFailedData(){
    return this.form_clazz.getTelFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getAddrPostcodeFailedData(){
    return this.form_clazz.getAddrPostcodeFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getAddrFailedData(){
    return this.form_clazz.getAddrFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getPositionFailedData(){
    return this.form_clazz.getPositionFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getPositionArriDateFailedData(Boolean output_valid){
    return this.form_clazz.getPositionArriDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getJobFailedData(){
    return this.form_clazz.getJobFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getAssignDeptFailedData(){
    return this.form_clazz.getAssignDeptFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getAssignDateFailedData(Boolean output_valid){
    return this.form_clazz.getAssignDateFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getInstChargeFailedData(){
    return this.form_clazz.getInstChargeFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */   
  public List<String> getOtherCommentFailedData(){
    return this.form_clazz.getOtherCommentFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<History_TestKeys, String> getHistoryNormalData(){
    return this.hist_testcase_clazz.getHistoryNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryOkData(){
    return this.hist_testcase_clazz.getHistoryOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getHistoryIdFailedData(Boolean output_valid){
    return this.hist_testcase_clazz.getHistoryIdFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeDatetimeFailedData(){
    return this.hist_testcase_clazz.getChangeDatetimeFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getChangeKindsFailedData(){
    return this.hist_testcase_clazz.getChangeKindsFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getOperationUserFailedData(){
    return this.hist_testcase_clazz.getOperationUserFailedData();
  }

  /** @} */












  /** @name [団員情報のCSV入出力テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvNormalData(){
    return this.csv_clazz.getCsvNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvOkData(){
    return this.csv_clazz.getCsvOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public List<String> getCsvFailedData(Boolean form_only){
    return this.csv_clazz.getCsvFailedData(form_only);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getCsvValidError(){
    return this.csv_clazz.getCsvValidError();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public LinkedHashMap<Integer, List<Integer>> getCsvErrorPos(){
    return this.csv_clazz.getCsvErrorPos();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Csv_TestCase
   **********************************************************************************************
   */ 
  public String getOutputCsv(Boolean tmp_file){
    return this.csv_clazz.getOutputCsv(tmp_file);
  }

  /** @} */











  /** @name [団員情報の検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.subjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public Integer allRepoForm(Integer testcase_num){
    return this.repoform_clazz.allRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] otherRepoForm(Member_Info_TestKeys subject_key, Integer testcase_num){
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












  /** @name [団員情報のデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseSetup() throws IOException{
    this.database_clazz.databaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseReset() throws IOException{
    this.database_clazz.databaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info compareEntityMake(Integer testcase_num) throws IOException{
    return this.database_clazz.compareEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info UpdateAfterEntityMake(Integer testcase_num) throws ParseException{
    return this.database_clazz.UpdateAfterEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info justInTimeEntityMake(){
    return this.database_clazz.justInTimeEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info okNullEntityMake(){
    return this.database_clazz.okNullEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Member_Info> failedEntityMake() throws IOException{
    return this.database_clazz.failedEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup() throws IOException{
    this.database_clazz.historyDatabaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseReset() throws IOException{
    this.database_clazz.historyDatabaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info_History compareHistoryEntityMake(Integer testcase_num) throws IOException{
    return this.database_clazz.compareHistoryEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info_History justInTimeHistoryEntityMake(){
    return this.database_clazz.justInTimeHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info UniqueMissEntityMake(){
    return this.database_clazz.UniqueMissEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public Member_Info_History okNullHistoryEntityMake(){
    return this.database_clazz.okNullHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Member_Info_History> failedHistoryEntityMake() throws IOException{
    return this.database_clazz.failedHistoryEntityMake();
  }

  /** @} */









  /** @name [団員情報のエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup() throws IOException, ParseException{
    return this.fail_database_clazz.failDatabaseSetup();
  }
  
  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void failDatabaseReset() throws IOException{
    this.fail_database_clazz.failDatabaseReset();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup() throws IOException, ParseException{
    return this.fail_database_clazz.historyFailDatabaseSetup();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Member_Info_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyFailDatabaseReset() throws IOException{
    this.fail_database_clazz.historyFailDatabaseReset();
  }
  
  /** @} */
}
