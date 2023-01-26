/** 
 **************************************************************************************
 * @file System_User_Repo_Insert_Test.java
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、データの新規追加を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;











/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、データの新規追加を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[System_User_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Member_Quad_Repository_TestUtils
 * @see System_User_Repo
 * @see Member_Info_Repo
 * @see Usage_Authority_Repo
 **************************************************************************************
 */ 
@Import({System_User_TestCase_Make.class, 
         Member_Info_TestCase_Make.class, 
         Usage_Authority_TestCase_Make.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class System_User_Repo_Insert_Test{

  public static System_User_TestCase_Make testcase;
  public static Member_Info_TestCase_Make foreign_case_1;
  public static Usage_Authority_TestCase_Make foreign_case_2;
  public static System_User_Repo test_repo;
  public static Member_Info_Repo foreign_repo_1;
  public static Usage_Authority_Repo foreign_repo_2;
  public static List<System_User> compare_entity;
  public static System_User just_entity;
  public static List<System_User> failed_entity;
  public static List<System_User> unique_miss;

  public static SoftAssertions softly = new SoftAssertions();
  
  
  
  

  




  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースへのデータ格納試験の際に用いるテストデータを用意する。
   *
   * @details
   * - 静的変数にインジェクションし、すべてのインナークラスで使用できるようにする。
   * - 用意するデータとしては、比較用の正常データの他、保存に失敗するデータや、保存できるデータの長さの限界値
   * 検査の為に、限界文字数まで文字列を敷き詰めたデータである。
   * - その他、管理番号やユーザー名の一意制約に違反するテストケースが存在する。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case_1 参照するテストケースクラス一つ目
   * @param[in] foreign_case_2 参照するテストケースクラス二つ目
   * @param[in] test_repo テスト対象のリポジトリ
   * @param[in] foreign_repo_1 参照するテーブルのリポジトリ一つ目
   * @param[in] foreign_repo_2 参照するテーブルのリポジトリ二つ目
   * 
   * @see System_User_Repo
   * @see Member_Info_Repo
   * @see Usage_Authority_Repo
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(System_User_TestCase_Make testcase, 
                            Member_Info_TestCase_Make foreign_case_1, 
                            Usage_Authority_TestCase_Make foreign_case_2, 
                            System_User_Repo test_repo, 
                            Member_Info_Repo foreign_repo_1, 
                            Usage_Authority_Repo foreign_repo_2){

    System_User_Repo_Insert_Test.testcase = testcase;
    System_User_Repo_Insert_Test.foreign_case_1 = foreign_case_1;
    System_User_Repo_Insert_Test.foreign_case_2 = foreign_case_2;
    System_User_Repo_Insert_Test.test_repo = test_repo;
    System_User_Repo_Insert_Test.foreign_repo_1 = foreign_repo_1;
    System_User_Repo_Insert_Test.foreign_repo_2 = foreign_repo_2;

    List<System_User> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 10; i++){
      compare_entity.add(Member_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i)));
    }

    System_User_Repo_Insert_Test.compare_entity = compare_entity;
    System_User_Repo_Insert_Test.just_entity = Member_Quad_Repository_TestUtils
                                               .MockToReal(testcase.justInTimeEntityMake());

    List<System_User> unique_miss_tmp = testcase.UniqueMissEntityMake();
    System_User_Repo_Insert_Test.unique_miss = new ArrayList<>();

    for(System_User unique_ent: unique_miss_tmp){
      System_User_Repo_Insert_Test.unique_miss.add(Member_Quad_Repository_TestUtils.MockToReal(unique_ent));
    }

    List<System_User> failed_ent_tmp = testcase.failedEntityMake();
    System_User_Repo_Insert_Test.failed_entity = new ArrayList<>();

    for(System_User fail_ent: failed_ent_tmp){
      System_User_Repo_Insert_Test.failed_entity.add(Member_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }







  
  
  
  
  
  /** 
   **********************************************************************************************
   * @brief データの新規追加処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Insert_Success {
  
    private final System_User_Repo test_repo;
    private final List<System_User> compare_entity;
    private final System_User just_entity;

    private final SoftAssertions softly;



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、新規追加処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - テスト前の段階でテーブル内にデータが残っている場合があるため、明示的にリセットが必要である。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * 
     * @see Member_Quad_Repository_TestUtils
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Insert_Success() throws IOException{
      this.test_repo = System_User_Repo_Insert_Test.test_repo;
      this.compare_entity = System_User_Repo_Insert_Test.compare_entity;
      this.just_entity = System_User_Repo_Insert_Test.just_entity;
      this.softly = System_User_Repo_Insert_Test.softly;

      System_User_Repo_Insert_Test.testcase.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Insert_Test.foreign_case_2.databaseSetup();

      Member_Info foreign_just_entity_1 = Member_Quad_Repository_TestUtils
                                         .MockToReal(foreign_case_1.justInTimeEntityMake());

      foreign_just_entity_1.setSerial_num(null);
      System_User_Repo_Insert_Test.foreign_repo_1.save(foreign_just_entity_1);

      Usage_Authority foreign_just_entity_2 = Member_Quad_Repository_TestUtils
                                         .MockToReal(foreign_case_2.justInTimeEntityMake());

      foreign_just_entity_2.setSerial_num(null);
      System_User_Repo_Insert_Test.foreign_repo_2.save(foreign_just_entity_2);
    }




    /** 
     **********************************************************************************************
     * @brief データの新規追加時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、テストケースを一個だけ保存した後、データを全取得して最初のデータを比較した際に、
     * 最初に保存したテストケースと内容が一致することを確認する。
     * - また、限界値検査の為の、限界文字数まで敷き詰めたデータを保存した場合に成功し、データも一致している事を
     * 確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを一個ずつ保存した後、全取得メソッドでデータを取得し、リストの最初のデータを比較する。
     * -# 比較を終了したら、テーブル内の全削除メソッドで削除し、この一連の処理をテストケースの数だけ繰り返す。
     * -# 限界文字数まで敷き詰めたデータを保存し、保存が成功かつ、データの内容が一致していることを確認する。
     * 
     * @see Member_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 新規追加の成功ケース(){

      for(System_User ent: this.compare_entity){
        ent.setSerial_num(null);
        this.test_repo.save(ent);

        List<System_User> result = (List<System_User>)this.test_repo.findAll();
        Member_Quad_Repository_TestUtils.assertAllEquals(ent, result.get(0), this.softly, false);

        this.test_repo.deleteAll();
      }


      this.just_entity.setSerial_num(null);
      this.test_repo.save(this.just_entity);

      List<System_User> after = (List<System_User>)this.test_repo.findAll();
      Member_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after.get(0), this.softly, false);

      this.test_repo.deleteAll();


      this.softly.assertAll();
    }
  
  
  
    /** 
     **********************************************************************************************
     * @brief テスト終了後に、テストケースとして利用したデータを、データベースから削除する。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    @AfterAll
    public static void testcaseReset() throws IOException{
      System_User_Repo_Insert_Test.testcase.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_2.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_1.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの新規追加処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Insert_Failed {
  
    private final System_User_Repo test_repo;
    private final List<System_User> failed_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、新規追加処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - テスト前の段階でテーブル内にデータが残っている場合があるため、明示的にリセットが必要である。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Insert_Failed() throws IOException{
      this.test_repo = System_User_Repo_Insert_Test.test_repo;
      this.failed_entity = System_User_Repo_Insert_Test.failed_entity;
      this.softly = System_User_Repo_Insert_Test.softly;

      System_User_Repo_Insert_Test.testcase.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Insert_Test.foreign_case_2.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief データの新規追加時の動作の確認を行う。なお、これは失敗ケースである。
     *
     * @details
     * - テストの内容としては、データベースのカラムの制約に違反し保存に失敗するデータを保存した際に、例外が
     * 投げられエラーになることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 失敗テストケースからデータを取得し、保存時にエラーになることを確認する。
     * -# この一連の処理をテストケースの数だけ繰り返す。
     **********************************************************************************************
     */
    @Test
    public void 新規追加の失敗ケース(){

      for(System_User ent: this.failed_entity){
        ent.setSerial_num(null);

        this.softly.assertThatThrownBy(() -> this.test_repo.save(ent))
                   .isInstanceOf(DbActionExecutionException.class);
      }

      this.softly.assertAll();
    }
  
  
  
    /** 
     **********************************************************************************************
     * @brief テスト終了後に、テストケースとして利用したデータを、データベースから削除する。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    @AfterAll
    public static void testcaseReset() throws IOException{
      System_User_Repo_Insert_Test.testcase.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_2.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_1.databaseReset();
    }
  }














  /** 
   **********************************************************************************************
   * @brief データの新規追加処理に関して、一意制約違反によって失敗するテストを実施するインナークラス。
   * 
   * @details なお、一意制約対象の値は、管理番号とユーザー名である。
   **********************************************************************************************
   */
  @Nested
  public class Unique_Failed {

    private final System_User_Repo test_repo;
    private final List<System_User> unique_miss;

    private final SoftAssertions softly;
  



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 初期データを準備する。
     *
     * @details
     * - 一意制約のテストを行うため、あらかじめ内容が被るデータを入れておく必要がある。
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、新規追加処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルへの初期データを準備する。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Unique_Failed() throws IOException{
      this.test_repo = System_User_Repo_Insert_Test.test_repo;
      this.unique_miss = System_User_Repo_Insert_Test.unique_miss;
      this.softly = System_User_Repo_Insert_Test.softly;

      System_User_Repo_Insert_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Insert_Test.foreign_case_2.databaseSetup();
      System_User_Repo_Insert_Test.testcase.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief データの新規追加を行った際に、一意制約違反によってエラーが出ることを確認する。
     *
     * @details
     * - テストの内容としては、データベースのカラムの一意制約に違反し保存に失敗するデータを保存した際に、例外が
     * 投げられエラーになることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 失敗テストケースからデータを取得し、保存時にエラーになることを確認する。
     **********************************************************************************************
     */
    @Test
    public void 新規追加の一意制約違反のケース(){

      for(System_User ent: unique_miss){
        ent.setSerial_num(null);
        
        this.softly.assertThatThrownBy(() -> this.test_repo.save(ent))
                   .isInstanceOf(DbActionExecutionException.class);
      }

      this.softly.assertAll();
    }
  
  
  
    /** 
     **********************************************************************************************
     * @brief テスト終了後に、テストケースとして利用したデータを、データベースから削除する。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    @AfterAll
    public static void testcaseReset() throws IOException{
      System_User_Repo_Insert_Test.testcase.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_2.databaseReset();
      System_User_Repo_Insert_Test.foreign_case_1.databaseReset();
    }
  }
}
