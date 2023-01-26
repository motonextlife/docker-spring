/** 
 **************************************************************************************
 * @file Usage_Authority_Repo_Search_Test.java
 * @brief 主に[権限管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Usage_Authority_Repo_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.RepoForm.Usage_Authority_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make.Usage_Authority_TestKeys;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;











/** 
 **************************************************************************************
 * @brief 主に[権限管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッドを検査する
 * クラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Usage_Authority_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see Usage_Authority_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Usage_Authority_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Usage_Authority_Repo_Search_Test{

  private final Usage_Authority_Repo test_repo;
  private final Usage_Authority_TestCase_Make testcase;
  private static Usage_Authority_TestCase_Make testcase_static;

  private final SoftAssertions softly = new SoftAssertions();

  private final List<Usage_Authority> origin_compare;








  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = Usage_Authority_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数
  public final int LIMIT = Usage_Authority_RepoForm_TestCase.LIMIT;

  //! オフセット値
  public final int OFFSET = Usage_Authority_RepoForm_TestCase.OFFSET;

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
   * @see Usage_Authority_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Usage_Authority_Repo_Search_Test(Usage_Authority_Repo test_repo, 
                                          Usage_Authority_TestCase_Make testcase) throws IOException{
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
    Usage_Authority_Repo_Search_Test.testcase_static = this.testcase;
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
  private void findAllSearchCheck(Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method, 
                                  TestParamBuilder<Usage_Authority> test_builder) 
                                  throws ExecutionException, InterruptedException{

    BiConsumer<Usage_Authority, Usage_Authority> assert_method = (compare, result) -> {
      Member_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Usage_Authority> test_param = test_builder.assert_method(assert_method)
                                                 .softly(this.softly)
                                                 .build();

    Common_Repository_TestUtils.findAllSearchCheck_Normal(find_all_method, test_param);
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findByAuth_id]を実行した際の検索結果を判定する。
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
  public void 対象の権限番号のデータを個別取得する際のテスト(){

    for(int i = 0; i < 20; i++){
      Usage_Authority compare_ent = this.origin_compare.get(i);
      String auth_id = compare_ent.getAuth_id();

      Usage_Authority result = this.test_repo.findByAuth_id(auth_id).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(compare_ent, result, this.softly, true);
    }

    Optional<Usage_Authority> empty_ent = this.test_repo.findByAuth_id(null);
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

    int all_max = this.testcase.allRepoForm(Usage_Authority_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(Usage_Authority_RepoForm_TestCase.ALL_OFFSET);
    int limit_max = this.testcase.allRepoForm(Usage_Authority_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(Usage_Authority_RepoForm_TestCase.LIMIT_OFFSET);


    CompareParam<Usage_Authority> list_builder = CompareParam.<Usage_Authority>builder().origin_compare(this.origin_compare)
                                                             .build();
    CompareParam<Usage_Authority> list_builder_1 = list_builder.toBuilder().skip(all_offset).limit(all_max)
                                                               .build();
    CompareParam<Usage_Authority> list_builder_2 = list_builder.toBuilder().skip(limit_offset).limit(limit_offset)
                                                               .build();

    List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                          .compareListMake(list_builder_1.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
    List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                          .compareListMake(list_builder_1.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());
    List<Usage_Authority> compare_3 = Common_Repository_TestUtils
                          .compareListMake(list_builder_2.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
    List<Usage_Authority> compare_4 = Common_Repository_TestUtils
                          .compareListMake(list_builder_2.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


    Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
      return this.test_repo.findAllOriginal(s.getLimit(), s.getOffset());
    };

    Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
      return this.test_repo.findAllOriginalDESC(s.getLimit(), s.getOffset());
    };

    TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().all_judge(true).build();
    TestParam<Usage_Authority> test_builder_1 = test_builder.toBuilder().limit(all_max).offset(all_offset).build();
    TestParam<Usage_Authority> test_builder_2 = test_builder.toBuilder().limit(limit_max).offset(limit_offset).build();

    findAllSearchCheck(find_all_method_1, test_builder_1.toBuilder().compare(compare_1));
    findAllSearchCheck(find_all_method_2, test_builder_1.toBuilder().compare(compare_2));
    findAllSearchCheck(find_all_method_1, test_builder_2.toBuilder().compare(compare_3));
    findAllSearchCheck(find_all_method_2, test_builder_2.toBuilder().compare(compare_4));
  }












  /** 
   **********************************************************************************************
   * @brief リポジトリメソッド[findAllByAuth_id][findAllByAuth_idDESC]を実行した際の検索結果を判定する。
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
  public void 検索対象のカラムが権限番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Auth_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getAuth_id().contains(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAuth_id(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAuth_idDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAuth_name][findAllByAuth_nameDESC]を実行した際の
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
  public void 検索対象のカラムが権限名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Auth_Name, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getAuth_name().contains(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_name))
                                                         .sort_second(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_name, 
                                                                                          Collections.reverseOrder()))
                                                         .sort_second(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                           Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAuth_name(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAuth_nameDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAdmin][findAllByAdminDESC]を実行した際の
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
  public void 検索対象のカラムが管理者権限の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Admin, i);

      String word = assume[WORD];
      Boolean bool = Boolean.parseBoolean(word);
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getAdmin().equals(bool))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAdmin(s.getMain_word() == null ? null : Boolean.parseBoolean(s.getMain_word()), 
                                             s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAdminDESC(s.getMain_word() == null ? null : Boolean.parseBoolean(s.getMain_word()), 
                                                 s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByMember_info][findAllByMember_infoDESC]を実行した際の
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
  public void 検索対象のカラムが団員管理権限の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Member_Info, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getMember_info().equals(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByMember_info(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByMember_infoDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByUsage_Authority][findAllByUsage_AuthorityDESC]を実行した際の
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
  public void 検索対象のカラムが設備管理権限の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Facility, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getFacility().equals(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByFacility(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByFacilityDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByMusical_score][findAllByMusical_scoreDESC]を実行した際の
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
  public void 検索対象のカラムが楽譜管理権限の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Musical_Score, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getMusical_score().equals(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByMusical_score(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByMusical_scoreDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllBySound_source][findAllBySound_sourceDESC]を実行した際の
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
  public void 検索対象のカラムが音源管理権限の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Usage_Authority_TestKeys.Sound_Source, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Usage_Authority> list_builder = CompareParam
                                    .<Usage_Authority>builder()
                                    .origin_compare(this.origin_compare)
                                    .filter(s -> s.getSound_source().equals(word))
                                    .skip(offset)
                                    .limit(limit)
                                    .build();

      List<Usage_Authority> compare_1 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id))
                                                         .build());
      List<Usage_Authority> compare_2 = Common_Repository_TestUtils
                            .compareListMake(list_builder.toBuilder()
                                                         .sort_first(Comparator.comparing(Usage_Authority::getAuth_id, 
                                                                                          Collections.reverseOrder()))
                                                         .build());


      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_1 = s -> {
        return this.test_repo.findAllBySound_source(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<Usage_Authority>, CompletableFuture<List<Usage_Authority>>> find_all_method_2 = s -> {
        return this.test_repo.findAllBySound_sourceDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<Usage_Authority> test_builder = TestParam.<Usage_Authority>builder().main_word(word)
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

    for(Usage_Authority item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num(), item.getAuth_id());
      this.softly.assertThat(count).isEqualTo(0);
    }

    for(Usage_Authority item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num() + 1, item.getAuth_id());
      this.softly.assertThat(count).isEqualTo(1);
    }

    Integer null_1 = this.test_repo.checkUnique(null, this.origin_compare.get(0).getAuth_id());
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
    Usage_Authority_Repo_Search_Test.testcase_static.databaseReset();
  }
}
