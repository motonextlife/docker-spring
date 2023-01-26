/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto
 * 
 * @brief テストケース生成クラスのうち、[設備写真データ管理機能]に関するテストケースを生成する
 * 処理のパッケージ
 * 
 * @details
 * - このパッケージは、データベースやファイルサーバーの接続テストの際に用いるテストケースや、
 * フォームバリデーション時のテストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto;





/** 
 **************************************************************************************
 * @file Facility_Photo_TestCase_Make.java
 * @brief 主に[設備写真データ管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Database.Facility_Photo_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.FailDatabase.Facility_Photo_Fail_Database_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Form.Facility_Photo_Form_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.RepoForm.Facility_Photo_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Zip.Facility_Photo_Zip_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_Photo_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility_Photo;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
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
 * @see Facility_Photo_Form_TestCase
 * @see Facility_Photo_RepoForm_TestCase
 * @see Facility_Photo_Zip_TestCase
 * @see Facility_Photo_Database_TestCase
 * @see Facility_Photo_Fail_Database_TestCase
 * @see History_TestCase_Make
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_Photo_TestCase_Make{
  
  private final Facility_Photo_Form_TestCase form_clazz;
  private final Facility_Photo_RepoForm_TestCase repoform_clazz;
  private final Facility_Photo_Zip_TestCase zip_clazz;
  private final Facility_Photo_Database_TestCase database_clazz;
  private final Facility_Photo_Fail_Database_TestCase fail_database_clazz;
  private final History_TestCase_Make hist_testcase_clazz;







  /** 
   **********************************************************************************************
   * @brief 設備写真データを関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum Facility_Photo_TestKeys {
    Serial_Num,
    Faci_Id,
    Photo_Name,
    Photo_Data;
  }









  /** @name [設備写真データ情報のフォームバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Facility_Photo_TestKeys, String> getNormalData(){
    return this.form_clazz.getNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public Map<Facility_Photo_TestKeys, String> getOkData(){
    return this.form_clazz.getOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getSerialNumFailedData(Boolean null_tolelance, Boolean output_valid){
    return this.form_clazz.getSerialNumFailedData(null_tolelance, output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getFaciIdFailedData(Boolean output_valid){
    return this.form_clazz.getFaciIdFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getPhotoNameFailedData(){
    return this.form_clazz.getPhotoNameFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Form_TestCase
   **********************************************************************************************
   */ 
  public List<String> getPhotoDataFailedData(){
    return this.form_clazz.getPhotoDataFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public Map<History_TestKeys, String> getHistoryNormalData(){
    return this.hist_testcase_clazz.getHistoryNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public List<Map<History_TestKeys, String>> getHistoryOkData(){
    return this.hist_testcase_clazz.getHistoryOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public List<String> getHistoryIdFailedData(Boolean output_valid){
    return this.hist_testcase_clazz.getHistoryIdFailedData(output_valid);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public List<String> getChangeDatetimeFailedData(){
    return this.hist_testcase_clazz.getChangeDatetimeFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public List<String> getChangeKindsFailedData(){
    return this.hist_testcase_clazz.getChangeKindsFailedData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see History_TestCase_Make
   **********************************************************************************************
   */ 
  public List<String> getOperationUserFailedData(){
    return this.hist_testcase_clazz.getOperationUserFailedData();
  }

  /** @} */













  /** @name [設備写真データ情報のZIP入出力テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Zip_TestCase
   **********************************************************************************************
   */ 
  public String getZipNormalData(){
    return this.zip_clazz.getZipNormalData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Zip_TestCase
   **********************************************************************************************
   */ 
  public String getZipOkData(){
    return this.zip_clazz.getZipOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Zip_TestCase
   **********************************************************************************************
   */ 
  public List<String> getZipFailedData(Boolean append_form){
    return this.zip_clazz.getZipFailedData(append_form);
  }

  /** @} */












  /** @name [設備写真データ情報の検索テスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String subjectRepoForm(Integer testcase_num){
    return this.repoform_clazz.subjectRepoForm(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_RepoForm_TestCase
   **********************************************************************************************
   */ 
  public String[] otherRepoForm(Facility_Photo_TestKeys subject_key, Integer testcase_num){
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












  /** @name [設備写真データ情報のデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseSetup(Boolean link_samba) throws IOException{
    this.database_clazz.databaseSetup(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public void databaseReset(Boolean link_samba) throws IOException{
    this.database_clazz.databaseReset(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_Photo compareEntityMake(Integer testcase_num, Boolean link_samba) throws IOException{
    return this.database_clazz.compareEntityMake(testcase_num, link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_Photo UpdateAfterEntityMake(Integer testcase_num){
    return this.database_clazz.UpdateAfterEntityMake(testcase_num);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_Photo justInTimeEntityMake(){
    return this.database_clazz.justInTimeEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Facility_Photo> failedEntityMake() throws IOException{
    return this.database_clazz.failedEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseSetup(Boolean link_samba) throws IOException{
    this.database_clazz.historyDatabaseSetup(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyDatabaseReset(Boolean link_samba) throws IOException{
    this.database_clazz.historyDatabaseReset(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_Photo_History compareHistoryEntityMake(Integer testcase_num, Boolean link_samba) throws IOException{
    return this.database_clazz.compareHistoryEntityMake(testcase_num, link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public Facility_Photo_History justInTimeHistoryEntityMake(){
    return this.database_clazz.justInTimeHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Facility_Photo_History> failedHistoryEntityMake() throws IOException{
    return this.database_clazz.failedHistoryEntityMake();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Audio_Data_Database_TestCase
   **********************************************************************************************
   */ 
  public String compareSambaFileMake(Integer testcase_num){
    return this.database_clazz.compareSambaFileMake(testcase_num);
  }

  /** @} */












  /** @name [設備写真データ情報のエラーのデータベーステスト]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public Map<Integer, String> failDatabaseSetup(Boolean link_samba) throws IOException{
    return this.fail_database_clazz.failDatabaseSetup(link_samba);
  }
  
  
  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void failDatabaseReset(Boolean link_samba) throws IOException{
    this.fail_database_clazz.failDatabaseReset(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public List<Integer> historyFailDatabaseSetup(Boolean link_samba) throws IOException, ParseException{
    return this.fail_database_clazz.historyFailDatabaseSetup(link_samba);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Facility_Photo_Fail_Database_TestCase
   **********************************************************************************************
   */ 
  public void historyFailDatabaseReset(Boolean link_samba) throws IOException{
    this.fail_database_clazz.historyFailDatabaseReset(link_samba);
  }
  
  /** @} */
}
