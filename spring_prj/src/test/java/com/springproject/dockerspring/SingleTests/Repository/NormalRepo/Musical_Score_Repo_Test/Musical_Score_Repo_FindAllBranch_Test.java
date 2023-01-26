/** 
 **************************************************************************************
 * @file Musical_Score_Repo_FindAllBranch_Test.java
 * @brief 主に[楽譜管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Musical_Score_Repo_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make.Musical_Score_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.RepoForm.Musical_Score_RepoForm_TestCase;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Score_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Musical_Score_Repo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;










/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッドのテスト
 * を行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Musical_Score_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see Musical_Score_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Musical_Score_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Musical_Score_Repo_FindAllBranch_Test{

  private final Musical_Score_Repo test_repo;
  private final Musical_Score_TestCase_Make testcase;
  private static Musical_Score_TestCase_Make testcase_static;

  private final SoftAssertions softly = new SoftAssertions();
  private final SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

  private final List<Musical_Score> origin_compare;






  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = Musical_Score_RepoForm_TestCase.WORD;

  //! 検索開始日
  public final int START_DATE = Musical_Score_RepoForm_TestCase.START_DATE;

  //! 検索終了日
  public final int END_DATE = Musical_Score_RepoForm_TestCase.END_DATE;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが通常の文字列の場合)
  public final int LIMIT_WORD = Musical_Score_RepoForm_TestCase.LIMIT_WORD;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが日付の期間の場合)
  public final int LIMIT_DATE = Musical_Score_RepoForm_TestCase.LIMIT_DATE;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが通常の文字列の場合)
  public final int ANSWER_COUNT_WORD = Musical_Score_RepoForm_TestCase.ANSWER_COUNT_WORD;

  //! 検索結果の全件数を出力した際の結果数想定値(検索ワードが日付の期間の場合)
  public final int ANSWER_COUNT_DATE = Musical_Score_RepoForm_TestCase.ANSWER_COUNT_DATE;

  //! オフセット値(検索ワードが通常の文字列の場合)
  public final int OFFSET_WORD = Musical_Score_RepoForm_TestCase.OFFSET_WORD;

  //! オフセット値(検索ワードが日付の期間の場合)
  public final int OFFSET_DATE = Musical_Score_RepoForm_TestCase.OFFSET_DATE;

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
   * @see Musical_Score_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Musical_Score_Repo_FindAllBranch_Test(Musical_Score_Repo test_repo, 
                                               Musical_Score_TestCase_Make testcase) throws IOException{
    this.test_repo = test_repo;
    this.testcase = testcase;
    this.testcase.databaseSetup();

    this.origin_compare = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      this.origin_compare.add(this.testcase.compareEntityMake(i));
    }
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
    Musical_Score_Repo_FindAllBranch_Test.testcase_static = this.testcase;
  }











  /** 
   **********************************************************************************************
   * @brief リポジトリの検索メソッドの分岐と実行メソッドを実行し、返された結果の判定を行う。
   * 
   * @details
   * - 実際の判定結果は、このクラスでは行わず、共通のユーティリティクラスに処理を委託する。
   * - こちらのクラスでは、エンティティの比較に用いるユーティリティメソッドを、関数型インターフェース内で
   * 指定して、委託先のメソッドに渡すだけである。
   *
   * @param[in] test_builder 組立途中の引数のビルダー
   * 
   * @see Common_Repository_TestUtils
   * @see Score_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  private void findAllBranchCheck(TestParamBuilder<Musical_Score> test_builder) 
      throws ExecutionException, InterruptedException, ParseException{

    BiConsumer<Musical_Score, Musical_Score> assert_method = (compare, result) -> {
      Score_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Musical_Score> test_param = test_builder.assert_method(assert_method)
                                                      .test_repo(this.test_repo)
                                                      .softly(this.softly)
                                                      .build();

    Common_Repository_TestUtils.findAllBranchCheck_Normal(test_param);
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[All]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が全検索の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    int all_answer_count = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.ALL_ANSWER);
    int all_max = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.ALL_OFFSET);
    int limit_answer_count = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.LIMIT_ANSWER);
    int limit_max = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.LIMIT_OFFSET);

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.ALL);

    CompareParam<Musical_Score> list_builder = CompareParam.<Musical_Score>builder().origin_compare(this.origin_compare)
                                                           .build();
    CompareParam<Musical_Score> list_builder_1 = list_builder.toBuilder().skip(all_offset).limit(all_max)
                                                             .build();
    CompareParam<Musical_Score> list_builder_2 = list_builder.toBuilder().skip(limit_offset).limit(limit_offset)
                                                             .build();

    List<Musical_Score> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder_1.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
    List<Musical_Score> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder_1.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                        Collections.reverseOrder()))
                                                       .build());
    List<Musical_Score> compare_3 = Common_Repository_TestUtils
                        .compareListMake(list_builder_2.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
    List<Musical_Score> compare_4 = Common_Repository_TestUtils
                        .compareListMake(list_builder_2.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                        Collections.reverseOrder()))
                                                       .build());

    TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().subject(subject).all_judge(true)
                                                     .build();
    TestParam<Musical_Score> test_builder_1 = test_builder.toBuilder()
                             .max(all_max).limit(all_max).offset(all_offset).answer_count(all_answer_count)
                             .build();
    TestParam<Musical_Score> test_builder_2 = test_builder.toBuilder()
                             .max(limit_max).limit(limit_max).offset(limit_offset).answer_count(limit_answer_count)
                             .build();

    findAllBranchCheck(test_builder_1.toBuilder().compare(compare_1).order(true));
    findAllBranchCheck(test_builder_1.toBuilder().compare(compare_2).order(false));
    findAllBranchCheck(test_builder_2.toBuilder().compare(compare_3).order(true));
    findAllBranchCheck(test_builder_2.toBuilder().compare(compare_4).order(false));
    
    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[ScoreId]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が楽譜番号の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.SCORE_ID);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Score_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getScore_id().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                        Collections.reverseOrder()))
                                                     .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[BuyDate]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が購入日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.BUY_DATE);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Buy_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> (s.getBuy_date().equals(date_before) || s.getBuy_date().after(date_before)) 
                                            && (s.getBuy_date().equals(date_after) || s.getBuy_date().before(date_after)))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getBuy_date))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getBuy_date, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(before)
                                                       .sub_word(after)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }














  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[SongTitle]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が曲名の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.SONG_TITLE);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Song_Title, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getSong_title().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getSong_title))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getSong_title, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Composer]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が作曲者の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.COMPOSER);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Composer, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getComposer().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getComposer))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getComposer, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Arranger]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が編曲者の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.ARRANGER);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Arranger, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getArranger().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getArranger))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getArranger, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Publisher]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が出版社の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.PUBLISHER);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Publisher, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getPublisher().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getPublisher))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getPublisher, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[StorageLoc]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が保管場所の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.STORAGE_LOC);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Storage_Loc, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getPublisher().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getPublisher))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getPublisher, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[DispDate]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が廃棄日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.DISP_DATE);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Disp_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> (s.getDisp_date().equals(date_before) || s.getDisp_date().after(date_before)) 
                                            && (s.getDisp_date().equals(date_after) || s.getDisp_date().before(date_after)))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getDisp_date))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getDisp_date, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(before)
                                                       .sub_word(after)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Comment]にした時の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードや検索種別を指定して検索をした場合、想定された検索結果と、
   * テスト対象メソッドから返ってきた値が、順番含めて一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果件数や、結果の順番、結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * -# また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @see Common_Repository_TestUtils
   * @see CompareParam
   * @see TestParam
   * 
   * @throw InterruptedException
   * @throw ExecutionException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別がその他コメントの際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.COMMENT);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Other_Comment, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Musical_Score> list_builder = CompareParam
                                  .<Musical_Score>builder()
                                  .origin_compare(this.origin_compare)
                                  .filter(s -> s.getOther_comment().contains(word))
                                  .skip(offset)
                                  .limit(limit)
                                  .build();

      List<Musical_Score> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getOther_comment))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id))
                                                       .build());
      List<Musical_Score> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder.toBuilder()
                                                       .sort_first(Comparator.comparing(Musical_Score::getOther_comment, 
                                                                                        Collections.reverseOrder()))
                                                       .sort_second(Comparator.comparing(Musical_Score::getScore_id, 
                                                                                         Collections.reverseOrder()))
                                                       .build());

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder()
                                                       .main_word(word)
                                                       .subject(subject)
                                                       .max(limit)
                                                       .limit(limit)
                                                       .offset(offset)
                                                       .all_judge(false)
                                                       .answer_count(answer_count)
                                                       .build();
      findAllBranchCheck(test_builder.toBuilder().compare(compare_1).order(true));
      findAllBranchCheck(test_builder.toBuilder().compare(compare_2).order(false));
    }

    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を違反した物とした場合にエラーになることを
   * 確認する。
   *
   * @details
   * - テストの内容としては、受付ができない検索種別を渡した際に、想定されるエラーが出力されることを確認する。
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が不適合の際のエラーテスト(){

    String subject = this.testcase.subjectRepoForm(Musical_Score_RepoForm_TestCase.MISSING);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Score_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);
      
      FindAllParam param = FindAllParam.builder().main_word(word)
                                                 .subject(subject)
                                                 .order(true)
                                                 .max(limit)
                                                 .limit(limit)
                                                 .offset(offset)
                                                 .build();

      this.softly.assertThatThrownBy(() -> this.test_repo.findAllBranch(param))
                 .isInstanceOf(IllegalArgumentException.class);
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
    Musical_Score_Repo_FindAllBranch_Test.testcase_static.databaseReset();
  }
}
