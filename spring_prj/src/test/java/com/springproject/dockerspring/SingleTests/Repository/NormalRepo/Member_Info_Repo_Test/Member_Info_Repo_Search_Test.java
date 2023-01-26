/** 
 **************************************************************************************
 * @file Member_Info_Repo_Search_Test.java
 * @brief 主に[団員管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Member_Info_Repo_Test;

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
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make.Member_Info_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.RepoForm.Member_Info_RepoForm_TestCase;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;

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
 * @brief 主に[団員管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
 * クラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Member_Info_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see Member_Info_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Member_Info_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Member_Info_Repo_Search_Test{

  private final Member_Info_Repo test_repo;
  private final Member_Info_TestCase_Make testcase;
  private static Member_Info_TestCase_Make testcase_static;

  private final SoftAssertions softly = new SoftAssertions();
  private final SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

  private final List<Member_Info> origin_compare;








  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = Member_Info_RepoForm_TestCase.WORD;

  //! 検索開始日
  public final int START_DATE = Member_Info_RepoForm_TestCase.START_DATE;

  //! 検索終了日
  public final int END_DATE = Member_Info_RepoForm_TestCase.END_DATE;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが通常の文字列の場合)
  public final int LIMIT_WORD = Member_Info_RepoForm_TestCase.LIMIT_WORD;

  //! 1ページ当たりの検索結果の出力限界数(検索ワードが日付の期間の場合)
  public final int LIMIT_DATE = Member_Info_RepoForm_TestCase.LIMIT_DATE;

  //! オフセット値(検索ワードが通常の文字列の場合)
  public final int OFFSET_WORD = Member_Info_RepoForm_TestCase.OFFSET_WORD;

  //! オフセット値(検索ワードが日付の期間の場合)
  public final int OFFSET_DATE = Member_Info_RepoForm_TestCase.OFFSET_DATE;

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
   * @see Member_Info_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Member_Info_Repo_Search_Test(Member_Info_Repo test_repo, 
                                      Member_Info_TestCase_Make testcase) throws IOException{
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
    Member_Info_Repo_Search_Test.testcase_static = this.testcase;
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
   * @see Member_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  private void findAllSearchCheck(Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method, 
                                  TestParamBuilder<Member_Info> test_builder) 
                                  throws ExecutionException, InterruptedException{

    BiConsumer<Member_Info, Member_Info> assert_method = (compare, result) -> {
      Member_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Member_Info> test_param = test_builder.assert_method(assert_method)
                                                    .softly(this.softly)
                                                    .build();

    Common_Repository_TestUtils.findAllSearchCheck_Normal(find_all_method, test_param);
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByMember_id]を実行した際の検索結果を判定する。
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
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Test
  public void 対象の団員番号のデータを個別取得する際のテスト(){

    for(int i = 0; i < 20; i++){
      Member_Info compare_ent = this.origin_compare.get(i);
      String member_id = compare_ent.getMember_id();

      Member_Info result = this.test_repo.findByMember_id(member_id).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(compare_ent, result, this.softly, true);
    }

    Optional<Member_Info> empty_ent = this.test_repo.findByMember_id(null);
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

    int all_max = this.testcase.allRepoForm(Member_Info_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(Member_Info_RepoForm_TestCase.ALL_OFFSET);
    int limit_max = this.testcase.allRepoForm(Member_Info_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(Member_Info_RepoForm_TestCase.LIMIT_OFFSET);


    CompareParam<Member_Info> list_builder = CompareParam.<Member_Info>builder().origin_compare(this.origin_compare)
                                                         .build();
    CompareParam<Member_Info> list_builder_1 = list_builder.toBuilder().skip(all_offset).limit(all_max)
                                                           .build();
    CompareParam<Member_Info> list_builder_2 = list_builder.toBuilder().skip(limit_offset).limit(limit_offset)
                                                           .build();

    List<Member_Info> compare_1 = Common_Repository_TestUtils
                      .compareListMake(list_builder_1.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
    List<Member_Info> compare_2 = Common_Repository_TestUtils
                      .compareListMake(list_builder_1.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());
    List<Member_Info> compare_3 = Common_Repository_TestUtils
                      .compareListMake(list_builder_2.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
    List<Member_Info> compare_4 = Common_Repository_TestUtils
                      .compareListMake(list_builder_2.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


    Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
      return this.test_repo.findAllOriginal(s.getLimit(), s.getOffset());
    };

    Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
      return this.test_repo.findAllOriginalDESC(s.getLimit(), s.getOffset());
    };

    TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().all_judge(true).build();
    TestParam<Member_Info> test_builder_1 = test_builder.toBuilder().limit(all_max).offset(all_offset).build();
    TestParam<Member_Info> test_builder_2 = test_builder.toBuilder().limit(limit_max).offset(limit_offset).build();

    findAllSearchCheck(find_all_method_1, test_builder_1.toBuilder().compare(compare_1));
    findAllSearchCheck(find_all_method_2, test_builder_1.toBuilder().compare(compare_2));
    findAllSearchCheck(find_all_method_1, test_builder_2.toBuilder().compare(compare_3));
    findAllSearchCheck(find_all_method_2, test_builder_2.toBuilder().compare(compare_4));
    
    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByMember_id][findAllByMember_idDESC]を実行した際の検索結果を判定する。
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
  public void 検索対象のカラムが団員番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Member_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getMember_id().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByMember_id(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByMember_idDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByName][findAllByNameDESC]を実行した際の
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
  public void 検索対象のカラムが名前の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Name, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getName().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getName))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getName, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByName(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByNameDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByName_pronu][findAllByName_pronuDESC]を実行した際の
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
  public void 検索対象のカラムがフリガナの際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Name_Pronu, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getName_pronu().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getName_pronu))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getName_pronu, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByName_pronu(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByName_pronuDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllBySex][findAllBySexDESC]を実行した際の
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
  public void 検索対象のカラムが性別の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Sex, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getSex().equals(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllBySex(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllBySexDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByBirthday][findAllByBirthdayDESC]を実行した際の
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
  public void 検索対象のカラムが誕生日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Birthday, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> (s.getBirthday().equals(date_before) || s.getBirthday().after(date_before)) 
                                          && (s.getBirthday().equals(date_after) || s.getBirthday().before(date_after)))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getBirthday))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getBirthday, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByBirthday(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                  s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                  s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByBirthdayDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                      s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                      s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(before)
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
   * @brief リポジトリメソッド[findAllByJoin_date][findAllByJoin_dateDESC]を実行した際の
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
  public void 検索対象のカラムが入団日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Join_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> (s.getJoin_date().equals(date_before) || s.getJoin_date().after(date_before)) 
                                          && (s.getJoin_date().equals(date_after) || s.getJoin_date().before(date_after)))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getJoin_date))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getJoin_date, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByJoin_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                   s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                   s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByJoin_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                       s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                       s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(before)
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
   * @brief リポジトリメソッド[findAllByRet_date][findAllByRet_dateDESC]を実行した際の
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
  public void 検索対象のカラムが退団日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Ret_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> (s.getRet_date().equals(date_before) || s.getRet_date().after(date_before)) 
                                          && (s.getRet_date().equals(date_after) || s.getRet_date().before(date_after)))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getRet_date))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getRet_date, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByRet_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                  s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                  s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByRet_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                      s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                      s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(before)
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
   * @brief リポジトリメソッド[findAllByEmail][findAllByEmailDESC]を実行した際の
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
  public void 検索対象のカラムがメールアドレスの際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Email, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getEmail_1().contains(word)
                                          || s.getEmail_2().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getEmail_1))
                                                     .sort_second(Comparator.comparing(Member_Info::getEmail_2))
                                                     .sort_third(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getEmail_1, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getEmail_2, 
                                                                                       Collections.reverseOrder()))
                                                     .sort_third(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByEmail(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByEmailDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByTel][findAllByTelDESC]を実行した際の
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
  public void 検索対象のカラムが電話番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Tel, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getTel_1().contains(word)
                                          || s.getTel_2().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getTel_1))
                                                     .sort_second(Comparator.comparing(Member_Info::getTel_2))
                                                     .sort_third(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getTel_1, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getTel_2, 
                                                                                       Collections.reverseOrder()))
                                                     .sort_third(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByTel(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByTelDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByPostcode][findAllByPostcodeDESC]を実行した際の
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
  public void 検索対象のカラムが現住所郵便番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Addr_Postcode, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getAddr_postcode().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAddr_postcode))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAddr_postcode, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByPostcode(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByPostcodeDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAddr][findAllByAddrDESC]を実行した際の
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
  public void 検索対象のカラムが現住所の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Addr, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getAddr().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAddr))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAddr, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAddr(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAddrDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByPosition][findAllByPositionDESC]を実行した際の
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
  public void 検索対象のカラムが役職名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Position, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getPosition().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getPosition))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getPosition, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByPosition(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByPositionDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByArri_date][findAllByArri_dateDESC]を実行した際の
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
  public void 検索対象のカラムが現役職着任日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Position_Arri_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Member_Info> list_builder = CompareParam
        .<Member_Info>builder()
        .origin_compare(this.origin_compare)
        .filter(s -> (s.getPosition_arri_date().equals(date_before) || s.getPosition_arri_date().after(date_before)) 
                  && (s.getPosition_arri_date().equals(date_after) || s.getPosition_arri_date().before(date_after)))
        .skip(offset)
        .limit(limit)
        .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getPosition_arri_date))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getPosition_arri_date, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByArri_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                   s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                   s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByArri_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                       s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                       s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(before)
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
   * @brief リポジトリメソッド[findAllByJob][findAllByJobDESC]を実行した際の
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
  public void 検索対象のカラムが職種名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Job, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getJob().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getJob))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getJob, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByJob(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByJobDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAssign_dept][findAllByAssign_deptDESC]を実行した際の
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
  public void 検索対象のカラムが配属部署の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Assign_Dept, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getAssign_dept().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAssign_dept))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAssign_dept, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAssign_dept(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAssign_deptDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAssign_date][findAllByAssign_dateDESC]を実行した際の
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
  public void 検索対象のカラムが配属日の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Assign_Date, i);

      String before = assume[START_DATE];
      String after = assume[END_DATE];
      Date date_before = this.parse_date.parse(before);
      Date date_after = this.parse_date.parse(after);
      int limit = Integer.parseInt(assume[LIMIT_DATE]);
      int offset = Integer.parseInt(assume[OFFSET_DATE]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> (s.getAssign_date().equals(date_before) || s.getAssign_date().after(date_before)) 
                                          && (s.getAssign_date().equals(date_after) || s.getAssign_date().before(date_after)))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAssign_date))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getAssign_date, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        try {
          return this.test_repo.findAllByAssign_date(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                     s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                     s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        try {
          return this.test_repo.findAllByAssign_dateDESC(s.getMain_word() == null ? null : this.parse_date.parse(s.getMain_word()), 
                                                         s.getSub_word() == null ? null : this.parse_date.parse(s.getSub_word()), 
                                                         s.getLimit(), s.getOffset());
        } catch (ParseException e) {
          throw new IllegalCallerException(e);
        }
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(before)
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
   * @brief リポジトリメソッド[findAllByInst_charge][findAllByInst_chargeDESC]を実行した際の
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
  public void 検索対象のカラムが担当楽器の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Inst_Charge, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getInst_charge().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getInst_charge))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getInst_charge, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByInst_charge(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByInst_chargeDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
      String[] assume = this.testcase.otherRepoForm(Member_Info_TestKeys.Other_Comment, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT_WORD]);
      int offset = Integer.parseInt(assume[OFFSET_WORD]);

      CompareParam<Member_Info> list_builder = CompareParam
                                .<Member_Info>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getOther_comment().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<Member_Info> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getOther_comment))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id))
                                                     .build());
      List<Member_Info> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(Member_Info::getOther_comment, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(Member_Info::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByComment(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Member_Info>, CompletableFuture<List<Member_Info>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByCommentDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Member_Info> test_builder = TestParam.<Member_Info>builder().main_word(word)
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
  public void 団員番号の重複チェックメソッドのテスト(){

    for(Member_Info item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num(), item.getMember_id());
      this.softly.assertThat(count).isEqualTo(0);
    }

    for(Member_Info item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num() + 1, item.getMember_id());
      this.softly.assertThat(count).isEqualTo(1);
    }

    Integer null_1 = this.test_repo.checkUnique(null, this.origin_compare.get(0).getMember_id());
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
    Member_Info_Repo_Search_Test.testcase_static.databaseReset();
  }
}
