/** 
 **************************************************************************************
 * @file Facility_History_Repo_SearchCount_Test.java
 * @brief 主に[設備情報変更履歴]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドと
 * 検索結果件数を取得するメソッドのテストを行うクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.HistoryRepo.Facility_History_Repo_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.History.RepoForm.History_RepoForm_TestCase;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Facility_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.HistoryRepo.Facility_History_Repo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;











/** 
 **************************************************************************************
 * @brief 主に[設備情報変更履歴]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドと
 * 検索結果件数を取得するメソッドのテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_History_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see Facility_History_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Facility_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Facility_History_Repo_SearchCount_Test{

  private final Facility_History_Repo test_repo;
  private final Facility_TestCase_Make testcase;
  private static Facility_TestCase_Make testcase_static;

  private final SoftAssertions softly = new SoftAssertions();
  private final SimpleDateFormat parse_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private final List<Facility_History> origin_compare;







  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索開始日時
  public final int START_DATETIME = History_RepoForm_TestCase.START_DATETIME;

  //! 検索終了日時
  public final int END_DATETIME = History_RepoForm_TestCase.END_DATETIME;

  //! 検索ワード
  public final int WORD = History_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数(履歴日時単体での検索時)
  public final int LIMIT_SINGLE = History_RepoForm_TestCase.LIMIT_SINGLE;

  //! 1ページ当たりの検索結果の出力限界数(履歴日時と検索ワードを組み合わせた検索時)
  public final int LIMIT_DOUBLE = History_RepoForm_TestCase.LIMIT_DOUBLE;

  //! 検索結果の全件数を出力した際の結果数想定値(履歴日時単体での検索時)
  public final int ANSWER_COUNT_SINGLE = History_RepoForm_TestCase.ANSWER_COUNT_SINGLE;

  //! 検索結果の全件数を出力した際の結果数想定値(履歴日時と検索ワードを組み合わせた検索時)
  public final int ANSWER_COUNT_DOUBLE = History_RepoForm_TestCase.ANSWER_COUNT_DOUBLE;

  //! オフセット値(履歴日時単体での検索時)
  public final int OFFSET_SINGLE = History_RepoForm_TestCase.OFFSET_SINGLE;

  //! オフセット値(履歴日時と検索ワードを組み合わせた検索時)
  public final int OFFSET_DOUBLE = History_RepoForm_TestCase.OFFSET_DOUBLE;

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
   * @see Facility_History_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Facility_History_Repo_SearchCount_Test(Facility_History_Repo test_repo, 
                                                Facility_TestCase_Make testcase) throws IOException{
    this.test_repo = test_repo;
    this.testcase = testcase;
    this.testcase.historyDatabaseSetup();

    this.origin_compare = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      this.origin_compare.add(this.testcase.compareHistoryEntityMake(i));
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
    Facility_History_Repo_SearchCount_Test.testcase_static = this.testcase;
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリの検索メソッドを実行し、返された結果の判定を行う。
   * 
   * @details
   * - 実際の判定結果は、このクラスでは行わず、共通のユーティリティクラスに処理を委託する。
   * - こちらのクラスでは、エンティティの比較に用いるユーティリティメソッドを、関数型インターフェース内で
   * 指定して、委託先のメソッドに渡すだけである。
   * - 検査対象となる検索メソッドが格納された関数型インターフェースの実行も、ユーティリティクラスに委託する。
   *
   * @param[in] test_builder 組立途中の引数のビルダー
   * 
   * @see Common_Repository_TestUtils
   * @see Facility_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  private void findAllSearchCheck(Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method, 
                                  TestParamBuilder<Facility_History> test_builder) 
                                  throws ExecutionException, InterruptedException{

    BiConsumer<Facility_History, Facility_History> assert_method = (compare, result) -> {
      Facility_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Facility_History> test_param = test_builder.assert_method(assert_method)
                                                         .softly(this.softly)
                                                         .build();

    Common_Repository_TestUtils.findAllSearchCheck_History(find_all_method, test_param);
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByDateBetween][findAllByDateBetweenDESC]を実行した際の検索結果を
   * 判定する。
   *
   * @details
   * - テストの内容としては、検索ワードを渡してメソッドを実行した際に、想定された検索結果と、テスト対象メソッド
   * から返ってきた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。なお、テスト対象メソッドは
   * 関数型インターフェースで作成し、共通のユーティリティクラス側で実行する。
   * -# その結果、想定通りの結果の順番、結果の内容であることを確認する。
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
  public void 検索対象のカラムが履歴日時の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);

      Date start = this.parse_datetime.parse(assume[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume[END_DATETIME]);
      int limit = Integer.parseInt(assume[LIMIT_SINGLE]);
      int offset = Integer.parseInt(assume[OFFSET_SINGLE]);

      CompareParam<Facility_History> list_builder = CompareParam
                                     .<Facility_History>builder()
                                     .origin_compare(this.origin_compare)
                                     .filter(s -> (s.getChange_datetime().equals(start) || s.getChange_datetime().after(start)) 
                                               && (s.getChange_datetime().equals(end) || s.getChange_datetime().before(end)))
                                     .skip(offset)
                                     .limit(limit)
                                     .build();

      List<Facility_History> compare_1 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getChange_datetime))
                                                          .sort_second(Comparator.comparing(Facility_History::getHistory_id))
                                                          .build());
      List<Facility_History> compare_2 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getChange_datetime, 
                                                                                           Collections.reverseOrder()))
                                                          .sort_second(Comparator.comparing(Facility_History::getHistory_id, 
                                                                                            Collections.reverseOrder()))
                                                          .build());


      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByDateBetween(s.getStart_datetime(), s.getEnd_datetime(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByDateBetweenDESC(s.getStart_datetime(), s.getEnd_datetime(), s.getLimit(), s.getOffset());
      };

      TestParam<Facility_History> test_builder = TestParam.<Facility_History>builder().start_datetime(start)
                                                                                      .end_datetime(end)
                                                                                      .limit(limit)
                                                                                      .offset(offset)
                                                                                      .all_judge(true)
                                                                                      .build();

      findAllSearchCheck(find_all_method_1, test_builder.toBuilder().compare(compare_1));
      findAllSearchCheck(find_all_method_2, test_builder.toBuilder().compare(compare_2));
    }

    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByFaci_id][findByFaci_idDESC]を実行した際の
   * 検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードを渡してメソッドを実行した際に、想定された検索結果と、テスト対象メソッド
   * から返ってきた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。なお、テスト対象メソッドは
   * 関数型インターフェースで作成し、共通のユーティリティクラス側で実行する。
   * -# その結果、想定通りの結果の順番、結果の内容であることを確認する。
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
  public void 検索対象のカラムが設備番号の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Id, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int offset = Integer.parseInt(assume_str[OFFSET_DOUBLE]);

      CompareParam<Facility_History> list_builder = CompareParam
                                     .<Facility_History>builder()
                                     .origin_compare(this.origin_compare)
                                     .filter(s -> (s.getChange_datetime().equals(start) || s.getChange_datetime().after(start)) 
                                               && (s.getChange_datetime().equals(end) || s.getChange_datetime().before(end))
                                               && s.getFaci_id().contains(word))
                                     .skip(offset)
                                     .limit(limit)
                                     .build();

      List<Facility_History> compare_1 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getFaci_id))
                                                          .sort_second(Comparator.comparing(Facility_History::getChange_datetime))
                                                          .sort_third(Comparator.comparing(Facility_History::getHistory_id))
                                                          .build());
      List<Facility_History> compare_2 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getFaci_id, 
                                                                                           Collections.reverseOrder()))
                                                          .sort_second(Comparator.comparing(Facility_History::getChange_datetime, 
                                                                                            Collections.reverseOrder()))
                                                          .sort_third(Comparator.comparing(Facility_History::getHistory_id, 
                                                                                           Collections.reverseOrder()))
                                                          .build());


      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_1 = s -> {
        return this.test_repo.findByFaci_id(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                            s.getLimit(), s.getOffset());
      };

      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_2 = s -> {
        return this.test_repo.findByFaci_idDESC(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                                s.getLimit(), s.getOffset());
      };

      TestParam<Facility_History> test_builder = TestParam.<Facility_History>builder().main_word(word)
                                                                                      .start_datetime(start)
                                                                                      .end_datetime(end)
                                                                                      .limit(limit)
                                                                                      .offset(offset)
                                                                                      .all_judge(false)
                                                                                      .build();

      findAllSearchCheck(find_all_method_1, test_builder.toBuilder().compare(compare_1));
      findAllSearchCheck(find_all_method_2, test_builder.toBuilder().compare(compare_2));
    }

    this.softly.assertAll();
  }














  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByChange_kinds][findByChange_kindsDESC]を実行した際の
   * 検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードを渡してメソッドを実行した際に、想定された検索結果と、テスト対象メソッド
   * から返ってきた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。なお、テスト対象メソッドは
   * 関数型インターフェースで作成し、共通のユーティリティクラス側で実行する。
   * -# その結果、想定通りの結果の順番、結果の内容であることを確認する。
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
  public void 検索対象のカラムが履歴種別の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Kinds, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int offset = Integer.parseInt(assume_str[OFFSET_DOUBLE]);

      CompareParam<Facility_History> list_builder = CompareParam
                                     .<Facility_History>builder()
                                     .origin_compare(this.origin_compare)
                                     .filter(s -> (s.getChange_datetime().equals(start) || s.getChange_datetime().after(start)) 
                                               && (s.getChange_datetime().equals(end) || s.getChange_datetime().before(end))
                                               && s.getChange_kinds().equals(word))
                                     .skip(offset)
                                     .limit(limit)
                                     .build();

      List<Facility_History> compare_1 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getChange_datetime))
                                                          .sort_second(Comparator.comparing(Facility_History::getHistory_id))
                                                          .build());
      List<Facility_History> compare_2 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getChange_datetime, 
                                                                                           Collections.reverseOrder()))
                                                          .sort_second(Comparator.comparing(Facility_History::getHistory_id, 
                                                                                            Collections.reverseOrder()))
                                                          .build());


      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_1 = s -> {
        return this.test_repo.findByChange_kinds(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                                 s.getLimit(), s.getOffset());
      };

      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_2 = s -> {
        return this.test_repo.findByChange_kindsDESC(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                                     s.getLimit(), s.getOffset());
      };

      TestParam<Facility_History> test_builder = TestParam.<Facility_History>builder().main_word(word)
                                                                                      .start_datetime(start)
                                                                                      .end_datetime(end)
                                                                                      .limit(limit)
                                                                                      .offset(offset)
                                                                                      .all_judge(false)
                                                                                      .build();

      findAllSearchCheck(find_all_method_1, test_builder.toBuilder().compare(compare_1));
      findAllSearchCheck(find_all_method_2, test_builder.toBuilder().compare(compare_2));
    }

    this.softly.assertAll();
  }














  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByOperation_user][findByOperation_userDESC]を実行した際の
   * 検索結果を判定する。
   *
   * @details
   * - テストの内容としては、検索ワードを渡してメソッドを実行した際に、想定された検索結果と、テスト対象メソッド
   * から返ってきた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミット、検索ワードで
   * 加工し、検索条件に応じた比較用のエンティティリストを作成する。
   * -# テスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。なお、テスト対象メソッドは
   * 関数型インターフェースで作成し、共通のユーティリティクラス側で実行する。
   * -# その結果、想定通りの結果の順番、結果の内容であることを確認する。
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
  public void 検索対象のカラムが操作ユーザー名の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Operation_User, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int offset = Integer.parseInt(assume_str[OFFSET_DOUBLE]);

      CompareParam<Facility_History> list_builder = CompareParam
                                     .<Facility_History>builder()
                                     .origin_compare(this.origin_compare)
                                     .filter(s -> (s.getChange_datetime().equals(start) || s.getChange_datetime().after(start)) 
                                               && (s.getChange_datetime().equals(end) || s.getChange_datetime().before(end))
                                               && s.getOperation_user().contains(word))
                                     .skip(offset)
                                     .limit(limit)
                                     .build();

      List<Facility_History> compare_1 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getOperation_user))
                                                          .sort_second(Comparator.comparing(Facility_History::getChange_datetime))
                                                          .sort_third(Comparator.comparing(Facility_History::getHistory_id))
                                                          .build());
      List<Facility_History> compare_2 = Common_Repository_TestUtils
                             .compareListMake(list_builder.toBuilder()
                                                          .sort_first(Comparator.comparing(Facility_History::getOperation_user, 
                                                                                           Collections.reverseOrder()))
                                                          .sort_second(Comparator.comparing(Facility_History::getChange_datetime, 
                                                                                            Collections.reverseOrder()))
                                                          .sort_third(Comparator.comparing(Facility_History::getHistory_id, 
                                                                                           Collections.reverseOrder()))
                                                          .build());


      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_1 = s -> {
        return this.test_repo.findByOperation_user(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                                   s.getLimit(), s.getOffset());
      };

      Function<TestParam<Facility_History>, CompletableFuture<List<Facility_History>>> find_all_method_2 = s -> {
        return this.test_repo.findByOperation_userDESC(s.getMain_word(), s.getStart_datetime(), s.getEnd_datetime(), 
                                                       s.getLimit(), s.getOffset());
      };

      TestParam<Facility_History> test_builder = TestParam.<Facility_History>builder().main_word(word)
                                                                                      .start_datetime(start)
                                                                                      .end_datetime(end)
                                                                                      .limit(limit)
                                                                                      .offset(offset)
                                                                                      .all_judge(false)
                                                                                      .build();

      findAllSearchCheck(find_all_method_1, test_builder.toBuilder().compare(compare_1));
      findAllSearchCheck(find_all_method_2, test_builder.toBuilder().compare(compare_2));
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByDateBetweenCount]に対してのテストを行う。
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
  public void カウント対象のカラムが履歴日時の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);

      Date start = this.parse_datetime.parse(assume[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume[END_DATETIME]);
      int limit = Integer.parseInt(assume[LIMIT_SINGLE]);
      int answer = Integer.parseInt(assume[ANSWER_COUNT_SINGLE]);

      CompletableFuture<Integer> result_1 = this.test_repo.findAllByDateBetweenCount(start, end, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findAllByDateBetweenCount(start, null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findAllByDateBetweenCount(null, end, limit);
      CompletableFuture<Integer> result_4 = this.test_repo.findAllByDateBetweenCount(start, end, null);

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
   * @brief リポジトリメソッド[findByFaci_idCount]に対してのテストを行う。
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
  public void カウント対象のカラムが設備番号の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Id, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int answer = Integer.parseInt(assume_str[ANSWER_COUNT_DOUBLE]);

      CompletableFuture<Integer> result_1 = this.test_repo.findByFaci_idCount(word, start, end, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findByFaci_idCount(word, start, null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findByFaci_idCount(word, null, end, limit);
      CompletableFuture<Integer> result_4 = this.test_repo.findByFaci_idCount(null, start, end, limit);
      CompletableFuture<Integer> result_5 = this.test_repo.findByFaci_idCount(word, start, end, null);

      CompletableFuture.allOf(result_1, result_2, result_3, result_4).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThat(result_3.get()).isEqualTo(0);
      this.softly.assertThat(result_4.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_5.get())
                 .isInstanceOf(ExecutionException.class);
    }

    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByChange_kindsCount]に対してのテストを行う。
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
  public void カウント対象のカラムが履歴種別の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Kinds, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int answer = Integer.parseInt(assume_str[ANSWER_COUNT_DOUBLE]);

      CompletableFuture<Integer> result_1 = this.test_repo.findByChange_kindsCount(word, start, end, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findByChange_kindsCount(word, start, null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findByChange_kindsCount(word, null, end, limit);
      CompletableFuture<Integer> result_4 = this.test_repo.findByChange_kindsCount(null, start, end, limit);
      CompletableFuture<Integer> result_5 = this.test_repo.findByChange_kindsCount(word, start, end, null);

      CompletableFuture.allOf(result_1, result_2, result_3, result_4).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThat(result_3.get()).isEqualTo(0);
      this.softly.assertThat(result_4.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_5.get())
                 .isInstanceOf(ExecutionException.class);
    }

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByOperation_userCount]に対してのテストを行う。
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
  public void カウント対象のカラムが操作ユーザー名の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume_datetime = this.testcase.historyOtherRepoForm(History_TestKeys.Change_Datetime, i);
      String[] assume_str = this.testcase.historyOtherRepoForm(History_TestKeys.Operation_User, i);

      String word = assume_str[WORD];
      Date start = this.parse_datetime.parse(assume_datetime[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume_datetime[END_DATETIME]);
      int limit = Integer.parseInt(assume_str[LIMIT_DOUBLE]);
      int answer = Integer.parseInt(assume_str[ANSWER_COUNT_DOUBLE]);

      CompletableFuture<Integer> result_1 = this.test_repo.findByOperation_userCount(word, start, end, limit);
      CompletableFuture<Integer> result_2 = this.test_repo.findByOperation_userCount(word, start, null, limit);
      CompletableFuture<Integer> result_3 = this.test_repo.findByOperation_userCount(word, null, end, limit);
      CompletableFuture<Integer> result_4 = this.test_repo.findByOperation_userCount(null, start, end, limit);
      CompletableFuture<Integer> result_5 = this.test_repo.findByOperation_userCount(word, start, end, null);

      CompletableFuture.allOf(result_1, result_2, result_3, result_4).join();

      this.softly.assertThat(result_1.get()).isEqualTo(answer);
      this.softly.assertThat(result_2.get()).isEqualTo(0);
      this.softly.assertThat(result_3.get()).isEqualTo(0);
      this.softly.assertThat(result_4.get()).isEqualTo(0);
      this.softly.assertThatThrownBy(() -> result_5.get())
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
    Facility_History_Repo_SearchCount_Test.testcase_static.historyDatabaseReset(false);
  }
}
