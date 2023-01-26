/** 
 **************************************************************************************
 * @file System_User_Repo_Update_Test.java
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、データの更新を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test;

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
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;










/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、データの更新を行うメソッドのテストを
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
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({System_User_TestCase_Make.class, 
         Member_Info_TestCase_Make.class, 
         Usage_Authority_TestCase_Make.class, 
         RepoConfig.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class System_User_Repo_Update_Test{

  public static System_User_TestCase_Make testcase;
  public static Member_Info_TestCase_Make foreign_case_1;
  public static Usage_Authority_TestCase_Make foreign_case_2;
  public static System_User_Repo test_repo;
  public static Member_Info_Repo foreign_repo_1;
  public static Usage_Authority_Repo foreign_repo_2;
  public static List<System_User> compare_entity;
  public static List<System_User> after_entity;
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

    System_User_Repo_Update_Test.testcase = testcase;
    System_User_Repo_Update_Test.foreign_case_1 = foreign_case_1;
    System_User_Repo_Update_Test.foreign_case_2 = foreign_case_2;
    System_User_Repo_Update_Test.test_repo = test_repo;
    System_User_Repo_Update_Test.foreign_repo_1 = foreign_repo_1;
    System_User_Repo_Update_Test.foreign_repo_2 = foreign_repo_2;

    List<System_User> compare_entity = new ArrayList<>();
    List<System_User> after_entity = new ArrayList<>();
    for(int i = 1; i <= 10; i++){
      compare_entity.add(Member_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i)));
      after_entity.add(Member_Quad_Repository_TestUtils.MockToReal(testcase.UpdateAfterEntityMake(i)));
    }

    System_User_Repo_Update_Test.compare_entity = compare_entity;
    System_User_Repo_Update_Test.after_entity = after_entity;
    System_User_Repo_Update_Test.just_entity = Member_Quad_Repository_TestUtils
                                               .MockToReal(testcase.justInTimeEntityMake());

    List<System_User> unique_miss_tmp = testcase.UniqueMissEntityMake();
    System_User_Repo_Update_Test.unique_miss = new ArrayList<>();

    for(System_User unique_ent: unique_miss_tmp){
      System_User_Repo_Update_Test.unique_miss.add(Member_Quad_Repository_TestUtils.MockToReal(unique_ent));
    }

    List<System_User> failed_ent_tmp = testcase.failedEntityMake();
    System_User_Repo_Update_Test.failed_entity = new ArrayList<>();

    for(System_User fail_ent: failed_ent_tmp){
      System_User_Repo_Update_Test.failed_entity.add(Member_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }
  
  
  
  







  
  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Update_Success {

    private final System_User_Repo test_repo;
    private final List<System_User> compare_entity;
    private final List<System_User> after_entity;
    private final System_User just_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 更新対象データの格納を行う。
     *
     * @details
     * - 更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @see Member_Quad_Repository_TestUtils
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Success() throws IOException{
      this.test_repo = System_User_Repo_Update_Test.test_repo;
      this.compare_entity = System_User_Repo_Update_Test.compare_entity;
      this.after_entity = System_User_Repo_Update_Test.after_entity;
      this.just_entity = System_User_Repo_Update_Test.just_entity;
      this.softly = System_User_Repo_Update_Test.softly;

      System_User_Repo_Update_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Update_Test.foreign_case_2.databaseSetup();
      
      Member_Info foreign_just_entity_1 = Member_Quad_Repository_TestUtils
                                         .MockToReal(foreign_case_1.justInTimeEntityMake());

      foreign_just_entity_1.setSerial_num(null);
      System_User_Repo_Update_Test.foreign_repo_1.save(foreign_just_entity_1);

      Usage_Authority foreign_just_entity_2 = Member_Quad_Repository_TestUtils
                                         .MockToReal(foreign_case_2.justInTimeEntityMake());

      foreign_just_entity_2.setSerial_num(null);
      System_User_Repo_Update_Test.foreign_repo_2.save(foreign_just_entity_2);

      System_User_Repo_Update_Test.testcase.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief データの更新時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを
     * 取得。そのデータがテストケースと同一で、正常に更新されていることを確認する。
     * - また、まだ更新していないデータに関しては、更新前と変わらない状態であることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを取得。
     * -# テストケースと取得したデータを比較する。
     * -# 更新したデータ以外を、更新前データのテストケースと比較し、一致することを確認する。
     * -# 限界文字数まで敷き詰めたデータを、保存済みの既存のデータの適当なシリアルナンバーで更新し、そのシリアル
     * ナンバーで再度検索した後、更新が成功かつ、データの内容が一致していることを確認する。
     * 
     * @see Member_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 更新の成功ケース(){

      for(int i = 0; i < 5; i++){
        System_User save_ent = this.after_entity.get(i);
        this.test_repo.save(save_ent);

        System_User result = this.test_repo.findById(save_ent.getSerial_num()).get();
        Member_Quad_Repository_TestUtils.assertAllEquals(save_ent, result, this.softly, true);
      }

      for(int i = 5; i < 10; i++){
        System_User no_change = this.compare_entity.get(i);

        System_User result = this.test_repo.findById(no_change.getSerial_num()).get();
        Member_Quad_Repository_TestUtils.assertAllEquals(no_change, result, this.softly, true);
      }


      Integer just_ent_serial = this.after_entity.get(0).getSerial_num();
      this.just_entity.setSerial_num(just_ent_serial);
      this.test_repo.save(this.just_entity);

      System_User after = this.test_repo.findById(just_ent_serial).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after, this.softly, true);


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
      System_User_Repo_Update_Test.testcase.databaseReset();
      System_User_Repo_Update_Test.foreign_case_2.databaseReset();
      System_User_Repo_Update_Test.foreign_case_1.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief アカウントのログイン失敗回数とロックの有無を記録するメソッドの動作を検証する。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class UpdateLock_Success {

    private final System_User_Repo test_repo;
    private final List<System_User> compare_entity;

    private final SoftAssertions softly;



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 更新対象データの格納を行う。
     *
     * @details
     * - 更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public UpdateLock_Success() throws IOException{
      this.test_repo = System_User_Repo_Update_Test.test_repo;
      this.compare_entity = System_User_Repo_Update_Test.compare_entity;
      this.softly = System_User_Repo_Update_Test.softly;

      System_User_Repo_Update_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Update_Test.foreign_case_2.databaseSetup();
      System_User_Repo_Update_Test.testcase.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief データのログイン失敗回数とロック有無を更新時の動作の確認を行う。
     *
     * @details
     * - テストの内容としては、テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを
     * 取得。そのデータがテストケースと同一で、正常に更新されていることを確認する。
     * - また、まだ更新していないデータに関しては、更新前と変わらない状態であることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを取得。
     * -# テストケースと取得したデータを比較する。
     * -# 更新したデータ以外を、更新前データのテストケースと比較し、一致することを確認する。
     * 
     * @see Member_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void アカウントのログイン失敗回数とロック有無の更新テスト(){

      for(int i = 0; i < 5; i++){
        System_User save_ent = this.compare_entity.get(i);
        this.test_repo.updateLock(save_ent.getMember_id(), 100, true);

        System_User result = this.test_repo.findById(save_ent.getSerial_num()).get();

        save_ent.setFail_count(100);
        save_ent.setLocking(true);

        Member_Quad_Repository_TestUtils.assertAllEquals(save_ent, result, this.softly, true);
      }

      for(int i = 5; i < 10; i++){
        System_User no_change = this.compare_entity.get(i);

        System_User result = this.test_repo.findById(no_change.getSerial_num()).get();
        Member_Quad_Repository_TestUtils.assertAllEquals(no_change, result, this.softly, true);
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
      System_User_Repo_Update_Test.testcase.databaseReset();
      System_User_Repo_Update_Test.foreign_case_2.databaseReset();
      System_User_Repo_Update_Test.foreign_case_1.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Update_Failed {
  
    private final System_User_Repo test_repo;
    private final List<System_User> failed_entity;

    private final SoftAssertions softly;



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 更新対象データの格納を行う。
     *
     * @details
     * - 更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Failed() throws IOException{
      this.test_repo = System_User_Repo_Update_Test.test_repo;
      this.failed_entity = System_User_Repo_Update_Test.failed_entity;
      this.softly = System_User_Repo_Update_Test.softly;

      System_User_Repo_Update_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Update_Test.foreign_case_2.databaseSetup();
      System_User_Repo_Update_Test.testcase.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief データの更新時の動作の確認を行う。なお、これは失敗ケースである。
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
    public void 更新の失敗ケース(){

      for(System_User ent: this.failed_entity){
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
      System_User_Repo_Update_Test.testcase.databaseReset();
      System_User_Repo_Update_Test.foreign_case_2.databaseReset();
      System_User_Repo_Update_Test.foreign_case_1.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、一意制約違反によって失敗するテストを実施するインナークラス。
   * 
   * @details なお、一意制約対象の値は、管理番号とユーザー名である。
   **********************************************************************************************
   */
  @Nested
  public class Unique_Failed {

    private final System_User_Repo test_repo;
    private final List<System_User> unique_miss;
    private final List<Integer> exclusion_serial;

    private final SoftAssertions softly;
  



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 初期データを準備する。
     *
     * @details
     * - 一意制約のテストを行うため、あらかじめ内容が被るデータを入れておく必要がある。
     * - また、一意制約テストの際に、更新を除外するデータのシリアルナンバーを控えておく。
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルへの初期データを準備する。
     * -# 更新を除外するデータのシリアルナンバー（一意制約エラーの管理番号と同じ物）を控える。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Unique_Failed() throws IOException{
      this.test_repo = System_User_Repo_Update_Test.test_repo;
      this.unique_miss = System_User_Repo_Update_Test.unique_miss;
      this.softly = System_User_Repo_Update_Test.softly;

      System_User_Repo_Update_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Update_Test.foreign_case_2.databaseSetup();
      System_User_Repo_Update_Test.testcase.databaseSetup();

      this.exclusion_serial = new ArrayList<>();
      for(System_User ent: unique_miss){
        Integer ask_serial = this.test_repo.findByMember_id(ent.getMember_id())
                                           .get()
                                           .getSerial_num();

        this.exclusion_serial.add(ask_serial);
      }
    }



    /** 
     **********************************************************************************************
     * @brief データの更新を行った際に、一意制約違反によってエラーが出ることを確認する。
     *
     * @details
     * - テストの内容としては、データベースのカラムの一意制約に違反し保存に失敗するデータを保存した際に、例外が
     * 投げられエラーになることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 失敗テストケースからデータを取得し、保存時にエラーになることを確認する。
     * -# なお、エラー確認をする際には、一意制約違反として投入する管理番号やユーザー名に紐付いている
     * シリアルナンバー「以外」のデータに対して行う。
     **********************************************************************************************
     */
    @Test
    public void 更新の一意制約違反のケース(){

      for(int i = 1; i <= 20; i++){
        int current = i;
        Boolean exclusion_judge = this.exclusion_serial.stream()
                                                       .anyMatch(s -> s == current);
        if(exclusion_judge){
          continue;
        }

        for(System_User ent: unique_miss){
          ent.setSerial_num(i);
          this.softly.assertThatThrownBy(() -> this.test_repo.save(ent))
                     .isInstanceOf(DbActionExecutionException.class);
        }
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
      System_User_Repo_Update_Test.testcase.databaseReset();
      System_User_Repo_Update_Test.foreign_case_2.databaseReset();
      System_User_Repo_Update_Test.foreign_case_1.databaseReset();
    }
  }











  /** 
   **********************************************************************************************
   * @brief 参照元のテーブルの情報を変更した際に、こちらのテスト対象テーブルの内容も追従することを確認する。
   * 
   * @details 
   * - データベースの制約として、参照制約のある値に関しては、「参照元のテーブルの変更に、参照先が追従」という
   * 設定になっているので、その確認を行う。
   * - なお、参照制約がついているの管理番号のみになるため、検査対象は管理番号の値のみとする。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Foreign_Success {

    private final System_User_Repo test_repo;
    private final Member_Info_Repo foreign_repo_1;
    private final Usage_Authority_Repo foreign_repo_2;
    private final System_User before_entity;

    private final SoftAssertions softly;
  


    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - なお、更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照制約のテスト対象とする管理番号のエンティティを一つ抽出しておく。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Foreign_Success() throws IOException{
      this.test_repo = System_User_Repo_Update_Test.test_repo;
      this.foreign_repo_1 = System_User_Repo_Update_Test.foreign_repo_1;
      this.foreign_repo_2 = System_User_Repo_Update_Test.foreign_repo_2;
      this.softly = System_User_Repo_Update_Test.softly;
      
      this.before_entity = System_User_Repo_Update_Test.compare_entity.get(0);

      System_User_Repo_Update_Test.foreign_case_1.databaseSetup();
      System_User_Repo_Update_Test.foreign_case_2.databaseSetup();
      System_User_Repo_Update_Test.testcase.databaseSetup();
    }




    /** 
     **********************************************************************************************
     * @brief 参照元の管理番号を変更した際に、参照先の管理番号が追従して変更されることを確認する。
     *
     * @details
     * - テストの内容は、参照先のテスト対象テーブルの保存データから、テスト対象とする管理番号を選び、その
     * 番号と同じ参照元テーブル内のデータを変更する。そうすれば、参照先のデータも追従する。
     * 
     * @par 大まかな処理の流れ
     * -# 更新するデータのシリアルナンバーを記憶しておき、参照元のデータを上書きする管理番号で更新する。
     * -# 更新後、記憶しておいたシリアルナンバーで参照先テーブルを検索し、取得したデータの管理番号が
     * 参照元テーブル側で更新した管理番号に変更されていることを確認する。
     * 
     * @see Member_Quad_Repository_TestUtils
     * 
     * @throw InterruptedException
     * @throw ExecutionException
     **********************************************************************************************
     */
    @Test
    public void 更新の参照元への追従確認() throws InterruptedException, ExecutionException{

      String target_id_member = this.before_entity.getMember_id();
      String target_id_auth = this.before_entity.getAuth_id();

      List<System_User> target_member = this.test_repo.findAllByMember_id(target_id_member, 1, 0).get();
      List<System_User> target_auth = this.test_repo.findAllByAuth_id(target_id_auth, 1, 0).get();

      Member_Info foreign_entity_member = foreign_repo_1.findByMember_id(target_id_member).get();
      Usage_Authority foreign_entity_auth = foreign_repo_2.findByAuth_id(target_id_auth).get();
      foreign_entity_member.setMember_id("change-after-001-member");
      foreign_entity_auth.setAuth_id("change-after-001-auth");

      foreign_repo_1.save(foreign_entity_member);
      foreign_repo_2.save(foreign_entity_auth);


      for(System_User ent: target_member){
        System_User after = this.test_repo.findById(ent.getSerial_num()).get();
        this.softly.assertThat(after.getMember_id()).isEqualTo("change-after-001-member");
      }

      for(System_User ent: target_auth){
        System_User after = this.test_repo.findById(ent.getSerial_num()).get();
        this.softly.assertThat(after.getAuth_id()).isEqualTo("change-after-001-auth");
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
      System_User_Repo_Update_Test.testcase.databaseReset();
      System_User_Repo_Update_Test.foreign_case_2.databaseReset();
      System_User_Repo_Update_Test.foreign_case_1.databaseReset();
    }
  }
}
