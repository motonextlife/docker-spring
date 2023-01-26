/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Sound_Source_Repo_Test
 * 
 * @brief 通常用リポジトリのうち、[音源情報]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、音源情報のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Sound_Source_Repo_Test;





/** 
 **************************************************************************************
 * @file Sound_Source_Repo_Count_Test.java
 * @brief 主に[音源管理]のリポジトリクラスで、検索結果のカウントを行うメソッドのテストを行う
 * クラスを格納したファイル。
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

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.RepoForm.Sound_Source_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make.Sound_Source_TestKeys;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Sound_Source_Repo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;










/** 
 **************************************************************************************
 * @brief 主に[音源管理]のリポジトリクラスで、検索結果のカウントを行うメソッドのテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Sound_Source_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 仕様を許可しておく。
 * 
 * @see Sound_Source_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Sound_Source_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Sound_Source_Repo_Count_Test{

  private final Sound_Source_Repo test_repo;
  private final Sound_Source_TestCase_Make testcase;
  private static Sound_Source_TestCase_Make testcase_static;

  private final SoftAssertions softly = new SoftAssertions();
  private final SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");







  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = Sound_Source_RepoForm_TestCase.WORD;

  //! 検索開始日
  public final int START_DATE = Sound_Source_RepoForm_TestCase.START_DATE;

  //! 検索終了日
  public final int END_DATE = Sound_Source_RepoForm_TestCase.END_DATE;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが通常の文字列の場合)
  public final int LIMIT_WORD = Sound_Source_RepoForm_TestCase.LIMIT_WORD;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが日付の期間の場合)
  public final int LIMIT_DATE = Sound_Source_RepoForm_TestCase.LIMIT_DATE;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが通常の文字列の場合)
  public final int ANSWER_COUNT_WORD = Sound_Source_RepoForm_TestCase.ANSWER_COUNT_WORD;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが日付の期間の場合)
  public final int ANSWER_COUNT_DATE = Sound_Source_RepoForm_TestCase.ANSWER_COUNT_DATE;

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
   * 
   * @see Facility_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Sound_Source_Repo_Count_Test(Sound_Source_Repo test_repo, Sound_Source_TestCase_Make testcase) throws IOException{
    this.test_repo = test_repo;
    this.testcase = testcase;
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
    Sound_Source_Repo_Count_Test.testcase_static = this.testcase;
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

    int all_answer_count = this.testcase.allRepoForm(Sound_Source_RepoForm_TestCase.ALL_ANSWER);
    int all_max = this.testcase.allRepoForm(Sound_Source_RepoForm_TestCase.ALL_MAX);
    int limit_answer_count = this.testcase.allRepoForm(Sound_Source_RepoForm_TestCase.LIMIT_ANSWER);
    int limit_max = this.testcase.allRepoForm(Sound_Source_RepoForm_TestCase.LIMIT_MAX);

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
   * @brief リポジトリメソッド[findAllBySound_idCount]に対してのテストを行う。
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
  public void カウント対象のカラムが音源番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Sound_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllBySound_idCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllBySound_idCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllBySound_idCount(word, null);
  
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
   * @brief リポジトリメソッド[findAllByUpload_dateCount]に対してのテストを行う。
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
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void カウント対象のカラムが登録日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Upload_Date, i);

      Date before = this.parse_date.parse(assume[START_DATE]);
      Date after = this.parse_date.parse(assume[END_DATE]);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_DATE]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByUpload_dateCount(before, after, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByUpload_dateCount(before, null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByUpload_dateCount(null, after, limit);
      CompletableFuture<Integer> result_4 = this.test_repo.findAllByUpload_dateCount(before, after, null);
  
      CompletableFuture.allOf(result_1, result_2, result_3).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThat(result_3.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_4.get())
                 .isInstanceOf(ExecutionException.class);
    }
    
    this.softly.assertAll();
  }










  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBySong_titleCount]に対してのテストを行う。
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
  public void カウント対象のカラムが曲名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Song_Title, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllBySong_titleCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllBySong_titleCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllBySong_titleCount(word, null);
  
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
   * @brief リポジトリメソッド[findAllByComposerCount]に対してのテストを行う。
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
  public void カウント対象のカラムが作曲者の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Composer, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByComposerCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByComposerCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByComposerCount(word, null);
  
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
   * @brief リポジトリメソッド[findAllByPerformerCount]に対してのテストを行う。
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
  public void カウント対象のカラムが演奏者の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Performer, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByPerformerCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByPerformerCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByPerformerCount(word, null);
  
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
   * @brief リポジトリメソッド[findAllByPublisherCount]に対してのテストを行う。
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
  public void カウント対象のカラムが出版社の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Publisher, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByPublisherCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByPublisherCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByPublisherCount(word, null);
  
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
   * @brief リポジトリメソッド[findAllByCommentCount]に対してのテストを行う。
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
  public void カウント対象のカラムがその他コメントの際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Sound_Source_TestKeys.Other_Comment, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_WORD]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByCommentCount(word, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByCommentCount(null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByCommentCount(word, null);
  
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
    Sound_Source_Repo_Count_Test.testcase_static.databaseReset();
  }
}
