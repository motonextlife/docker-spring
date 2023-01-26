/** 
 **************************************************************************************
 * @file Musical_Score_Repo_Search_Test.java
 * @brief 主に[楽譜管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;











/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
 * クラス
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
public class Musical_Score_Repo_Search_Test{

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
  public Musical_Score_Repo_Search_Test(Musical_Score_Repo test_repo, 
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
    Musical_Score_Repo_Search_Test.testcase_static = this.testcase;
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
   * @see Score_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  private void findAllSearchCheck(Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method, 
                                  TestParamBuilder<Musical_Score> test_builder) 
                                  throws ExecutionException, InterruptedException{

    BiConsumer<Musical_Score, Musical_Score> assert_method = (compare, result) -> {
      Score_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Musical_Score> test_param = test_builder.assert_method(assert_method)
                                                      .softly(this.softly)
                                                      .build();

    Common_Repository_TestUtils.findAllSearchCheck_Normal(find_all_method, test_param);
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByScore_Id]を実行した際の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、管理番号を完全一致指定して検索をした場合、想定された検索結果と、テスト対象
   * メソッドから返ってきた値が一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、比較対象のデータの管理番号を取得する。
   * -# 取得した管理番号を渡してテスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * 
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Test
  public void 対象の楽譜番号のデータを個別取得する際のテスト(){

    for(int i = 0; i < 20; i++){
      Musical_Score compare_ent = this.origin_compare.get(i);
      String score_id = compare_ent.getScore_id();

      Musical_Score result = this.test_repo.findByScore_Id(score_id).get();
      Score_Quad_Repository_TestUtils.assertAllEquals(compare_ent, result, this.softly, true);
    }

    Optional<Musical_Score> empty_ent = this.test_repo.findByScore_Id(null);
    this.softly.assertThat(empty_ent.isEmpty()).isTrue();

    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllOriginal][findAllOriginalDESC]を実行した際の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、メソッドを実行した際に、想定された検索結果と、テスト対象メソッドから返って
   * きた値が、順番含めて一致することを確認する。
   * - また、本来Nullが許容されない引数に対してNullを渡した際に、エラーとなることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、そのリストをオフセット値やリミットで
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
   **********************************************************************************************
   */
  @Test
  public void 保存されているデータを全取得する際のテスト() throws InterruptedException, ExecutionException{

    int all_max = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.ALL_OFFSET);
    int limit_max = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(Musical_Score_RepoForm_TestCase.LIMIT_OFFSET);


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


    Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
      return this.test_repo.findAllOriginal(s.getLimit(), s.getOffset());
    };

    Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
      return this.test_repo.findAllOriginalDESC(s.getLimit(), s.getOffset());
    };

    TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().all_judge(true).build();
    TestParam<Musical_Score> test_builder_1 = test_builder.toBuilder().limit(all_max).offset(all_offset).build();
    TestParam<Musical_Score> test_builder_2 = test_builder.toBuilder().limit(limit_max).offset(limit_offset).build();

    findAllSearchCheck(find_all_method_1, test_builder_1.toBuilder().compare(compare_1));
    findAllSearchCheck(find_all_method_2, test_builder_1.toBuilder().compare(compare_2));
    findAllSearchCheck(find_all_method_1, test_builder_2.toBuilder().compare(compare_3));
    findAllSearchCheck(find_all_method_2, test_builder_2.toBuilder().compare(compare_4));
    
    this.softly.assertAll();
  }






  







  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByScore_id][findAllByScore_idDESC]を実行した際の検索結果を判定する。
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが楽譜番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Score_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByScore_id(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByScore_idDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByDisp_date][findAllByDisp_dateDESC]を実行した際の
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
  public void 検索対象のカラムが購入日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Buy_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
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

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByDisp_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                  s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                  s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByDisp_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                      s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                      s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(before)
                                                                                .sub_word(after)
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
   * @brief リポジトリメソッド[findAllBySong_title][findAllBySong_titleDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが曲名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Song_Title, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllBySong_title(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllBySong_titleDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByComposer][findAllByComposerDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが作曲者の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Composer, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByComposer(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByComposerDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByArranger][findAllByArrangerDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが編曲者の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Arranger, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByArranger(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByArrangerDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByPublisher][findAllByPublisherDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが出版社の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Publisher, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByPublisher(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByPublisherDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByStorage_loc][findAllByStorage_locDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムが保管場所の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Storage_Loc, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByStorage_loc(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByStorage_locDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByDisp_date][findAllByDisp_dateDESC]を実行した際の
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
  public void 検索対象のカラムが廃棄日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Disp_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
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

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByDisp_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                   s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                   s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByDisp_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                       s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                       s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(before)
                                                                                .sub_word(after)
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
   * @brief リポジトリメソッド[findAllByComment][findAllByCommentDESC]を実行した際の
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
   **********************************************************************************************
   */
  @Test
  public void 検索対象のカラムがその他コメントの際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Musical_Score_TestKeys.Other_Comment, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
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


      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByComment(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Musical_Score>, CompletableFuture<List<Musical_Score>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByCommentDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Musical_Score> test_builder = TestParam.<Musical_Score>builder().main_word(word)
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
   * @brief リポジトリメソッド[checkUnique]を実行した際の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、管理番号とシリアルナンバーを完全一致指定で検索した場合、想定される結果件数が
   * 返ってくることを確認する。
   * - このメソッドは、指定した管理番号の重複確認のため、件数は「1」か「0」のいずれかしか返ってこない。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、比較対象のデータの管理番号とシリアルナンバーを
   * 取得する。
   * -# 取得した二つのデータを指定してテスト対象メソッドを実行し、結果件数が必ず「0」になることを確認する。
   * 該当シリアルナンバー以外で管理番号に重複はないので、必ず0件が返る。
   * -# 次に、メソッドを実行する際に指定するシリアルナンバーを、一つずらして実行した際に、結果件数が必ず
   * 「1」になることを確認する。必ず管理番号に重複が発生するので1件になる。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   **********************************************************************************************
   */
  @Test
  public void 設備番号の重複チェックメソッドのテスト(){

    for(Musical_Score item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num(), item.getScore_id());
      this.softly.assertThat(count).isEqualTo(0);
    }

    for(Musical_Score item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num() + 1, item.getScore_id());
      this.softly.assertThat(count).isEqualTo(1);
    }

    Integer null_1 = this.test_repo.checkUnique(null, this.origin_compare.get(0).getScore_id());
    Integer null_2 = this.test_repo.checkUnique(this.origin_compare.get(0).getSerial_num(), null);
    Integer null_3 = this.test_repo.checkUnique(null, null);
    this.softly.assertThat(null_1).isEqualTo(0);
    this.softly.assertThat(null_2).isEqualTo(0);
    this.softly.assertThat(null_3).isEqualTo(0);

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
    Musical_Score_Repo_Search_Test.testcase_static.databaseReset();
  }
}
