/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test
 * 
 * @brief 通常用リポジトリのうち、[システムユーザー情報]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、システムユーザー情報のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test;





/** 
 **************************************************************************************
 * @file System_User_Repo_Count_Test.java
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、検索結果のカウントを行うメソッドの
 * テストを行うクラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm.System_User_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;











/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、検索結果のカウントを行うメソッドの
 * テストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[System_User_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 仕様を許可しておく。
 * 
 * @see System_User_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Member_Info_TestCase_Make.class, 
         Usage_Authority_TestCase_Make.class, 
         System_User_TestCase_Make.class, 
         RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class System_User_Repo_Count_Test{

  private final System_User_Repo test_repo;

  private final System_User_TestCase_Make testcase;
  private final Member_Info_TestCase_Make foreign_case_1;
  private final Usage_Authority_TestCase_Make foreign_case_2;

  private static System_User_TestCase_Make testcase_static;
  private static Member_Info_TestCase_Make foreign_case_1_static;
  private static Usage_Authority_TestCase_Make foreign_case_2_static;

  private final SoftAssertions softly = new SoftAssertions();








  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = System_User_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数
  public final int LIMIT = System_User_RepoForm_TestCase.LIMIT;

  //! 検索結果の全件数を出力した際の結果数想定値
  public final int ANSWER_COUNT = System_User_RepoForm_TestCase.ANSWER_COUNT;

  //** @} */












  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースにテストケースを準備するなどのセットアップを行う。
   *
   * @details
   * - テスト前に、テストに必要なテストケースをデータベース内に保存しておく。
   * 
   * @param[in] test_repo テスト対象のリポジトリ
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case_1 参照を行うテストケース一つ目
   * @param[in] foreign_case_2 参照を行うテストケース二つ目
   * 
   * @see Facility_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public System_User_Repo_Count_Test(System_User_Repo test_repo, 
                                     System_User_TestCase_Make testcase, 
                                     Member_Info_TestCase_Make foreign_case_1, 
                                     Usage_Authority_TestCase_Make foreign_case_2) throws IOException{
    this.test_repo = test_repo;
    this.testcase = testcase;
    this.foreign_case_1 = foreign_case_1;
    this.foreign_case_2 = foreign_case_2;
    this.foreign_case_2.databaseSetup();
    this.foreign_case_1.databaseSetup();
    this.testcase.databaseSetup();
  }









  /** 
   **********************************************************************************************
   * @brief DIした環境変数を、静的変数でも使えるようにする。
   *
   * @details
   * - 設けた理由としては、テスト後のデータベースのリセット確認の際に環境変数を用いる必要があるが、AfterAll
   * は静的メソッドでしか実装できない為、通常のフィールド変数にDIされている環境変数が使えない。
   * - そのため、新たに静的フィールド変数を作成して、その中にDIした環境変数の参照を入れておくことで、テスト後の
   * データベースリセット確認にも環境変数を使用できるようにしている。
   * 
   * @par 使用アノテーション
   * - @PostConstruct
   * 
   * @note このメソッドは、セットアップのための非公開メソッドであり、外部に出さない。
   **********************************************************************************************
   */
  @PostConstruct
  private void configStaticSet(){
    System_User_Repo_Count_Test.testcase_static = this.testcase;
    System_User_Repo_Count_Test.foreign_case_1_static = this.foreign_case_1;
    System_User_Repo_Count_Test.foreign_case_2_static = this.foreign_case_2;
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllOriginalCount]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、出力件数最大値を切り替えて検索した場合、検索結果件数が想定通りの値であること
   * を確認する。
   * - また、本来Nullが許容されない出力件数最大値にNullを入れた際に、例外が発生することを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、設定する検索結果件数最大値と、その設定値を使った際に想定される結果件数の値を
   * 取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの件数で
   * あることを確認する。
   * -# 出力件数最大値にNullを設定して実行した場合、例外が発生し処理が止まることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void 保存されているデータを全カウントする際のテスト() throws InterruptedException, ExecutionException{

    int all_answer_count = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_ANSWER);
    int all_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_MAX);
    int limit_answer_count = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_ANSWER);
    int limit_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_MAX);

    CompletableFuture<Integer> result_1 = this.test_repo.findAllOriginalCount(all_max);
    CompletableFuture<Integer> result_2 = this.test_repo.findAllOriginalCount(limit_max);
    CompletableFuture<Integer> result_3 = this.test_repo.findAllOriginalCount(null);

    CompletableFuture.allOf(result_1, result_2).join();

    this.softly.assertThat(result_1.get()).isEqualTo(all_answer_count);
    this.softly.assertThat(result_2.get()).isEqualTo(limit_answer_count);
    this.softly.assertThatThrownBy(() -> result_3.get())
               .isInstanceOf(ExecutionException.class);

    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[nonExistRemainMember_id]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、完全一致で指定した管理番号を検索した際に、想定された通りにデータの存在確認の
   * 結果が出る事を確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、検索ワードとして渡す管理番号と、想定される検索結果を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの結果で
   * あることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void 団員番号の参照確認メソッドのテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 10; i++){
      String[] assume = this.testcase.existRemainRepoForm(System_User_TestKeys.Member_Id, i);

      String member_id = assume[WORD];
      Integer count = Integer.parseInt(assume[LIMIT]);

      CompletableFuture<Integer> result_1 = this.test_repo.nonExistRemainMember_id(member_id);
      CompletableFuture<Integer> result_2 = this.test_repo.nonExistRemainMember_id(null);
      
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(count);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
    }
    
    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[nonExistRemainAuth_id]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、完全一致で指定した管理番号を検索した際に、想定された通りにデータの存在確認の
   * 結果が出る事を確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、検索ワードとして渡す管理番号と、想定される検索結果を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの結果で
   * あることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void 権限番号の参照確認メソッドのテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 10; i++){
      String[] assume = this.testcase.existRemainRepoForm(System_User_TestKeys.Auth_Id, i);

      String auth_id = assume[WORD];
      Integer count = Integer.parseInt(assume[LIMIT]);

      CompletableFuture<Integer> result_1 = this.test_repo.nonExistRemainAuth_id(auth_id);
      CompletableFuture<Integer> result_2 = this.test_repo.nonExistRemainAuth_id(null);
      
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(count);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
    }  
      
    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByMember_idCount]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、出力件数最大値や検索ワードを切り替えて検索した場合、検索結果件数が想定通りの
   * 値であることを確認する。
   * - また、本来Nullが許容されない出力件数最大値にNullを入れた際に、例外が発生することを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、設定する検索結果件数最大値や検索ワードと、その設定値を使った際に想定される
   * 結果件数の値を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの件数で
   * あることを確認する。
   * -# 出力件数最大値にNullを設定して実行した場合、例外が発生し処理が止まることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void カウント対象のカラムが団員番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Member_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByMember_idCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByMember_idCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByMember_idCount(word, null);
  
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_3.get())
                 .isInstanceOf(ExecutionException.class);
    }
    
    this.softly.assertAll();
  }







  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByUsernameCount]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、出力件数最大値や検索ワードを切り替えて検索した場合、検索結果件数が想定通りの
   * 値であることを確認する。
   * - また、本来Nullが許容されない出力件数最大値にNullを入れた際に、例外が発生することを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、設定する検索結果件数最大値や検索ワードと、その設定値を使った際に想定される
   * 結果件数の値を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの件数で
   * あることを確認する。
   * -# 出力件数最大値にNullを設定して実行した場合、例外が発生し処理が止まることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void カウント対象のカラムがユーザー名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Username, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByUsernameCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByUsernameCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByUsernameCount(word, null);
  
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_3.get())
                 .isInstanceOf(ExecutionException.class);
    }
    
    this.softly.assertAll();
  }


  




  
  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByAuth_idCount]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、出力件数最大値や検索ワードを切り替えて検索した場合、検索結果件数が想定通りの
   * 値であることを確認する。
   * - また、本来Nullが許容されない出力件数最大値にNullを入れた際に、例外が発生することを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、設定する検索結果件数最大値や検索ワードと、その設定値を使った際に想定される
   * 結果件数の値を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの件数で
   * あることを確認する。
   * -# 出力件数最大値にNullを設定して実行した場合、例外が発生し処理が止まることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void カウント対象のカラムが権限番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Auth_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByAuth_idCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByAuth_idCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByAuth_idCount(word, null);
  
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_3.get())
                 .isInstanceOf(ExecutionException.class);
    }
    
    this.softly.assertAll();
  }







  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByLockingCount]に対してのテストを行う。
   *
   * @details
   * - テストの内容としては、出力件数最大値や検索ワードを切り替えて検索した場合、検索結果件数が想定通りの
   * 値であることを確認する。
   * - また、本来Nullが許容されない出力件数最大値にNullを入れた際に、例外が発生することを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから、設定する検索結果件数最大値や検索ワードと、その設定値を使った際に想定される
   * 結果件数の値を取得する。
   * -# 取得したテストケースの値を用いて、テスト対象メソッドを並列実行して結果を取得し、想定通りの件数で
   * あることを確認する。
   * -# 出力件数最大値にNullを設定して実行した場合、例外が発生し処理が止まることを確認する。
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   **********************************************************************************************
   */
  @Test
  public void カウント対象のカラムがロック有無の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Locking, i);

      Boolean bool = Boolean.parseBoolean(assume[WORD]);
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByLockingCount(bool, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByLockingCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByLockingCount(bool, null);
  
      CompletableFuture.allOf(result_1, result_2).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_3.get())
                 .isInstanceOf(ExecutionException.class);
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
    System_User_Repo_Count_Test.testcase_static.databaseReset();
    System_User_Repo_Count_Test.foreign_case_1_static.databaseReset();
    System_User_Repo_Count_Test.foreign_case_2_static.databaseReset();
  }
}
