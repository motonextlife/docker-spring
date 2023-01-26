/** 
 **************************************************************************************
 * @file System_User_Repo_Search_Test.java
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッド
 * を検査するクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;

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
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.System_User_TestCase_Make.System_User_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.SystemUser.RepoForm.System_User_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.UsageAuthority.Usage_Authority_TestCase_Make;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;











/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、独自に追加した検索結果を取得するメソッド
 * を検査するクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[System_User_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see System_User_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({System_User_TestCase_Make.class, 
         Member_Info_TestCase_Make.class, 
         Usage_Authority_TestCase_Make.class, 
         RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class System_User_Repo_Search_Test{

  private final System_User_Repo test_repo;
  private final System_User_TestCase_Make testcase;
  private final Member_Info_TestCase_Make foreign_case_1;
  private final Usage_Authority_TestCase_Make foreign_case_2;
  private static System_User_TestCase_Make testcase_static;
  private static Member_Info_TestCase_Make foreign_case_1_static;
  private static Usage_Authority_TestCase_Make foreign_case_2_static;

  private final SoftAssertions softly = new SoftAssertions();

  private final List<System_User> origin_compare;
  private final List<System_User> origin_compare_sys_security;








  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = System_User_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数
  public final int LIMIT = System_User_RepoForm_TestCase.LIMIT;

  //! オフセット値
  public final int OFFSET = System_User_RepoForm_TestCase.OFFSET;

  //** @} */










  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースにテストケースを準備するなどのセットアップを行う。
   *
   * @details
   * - テスト前に、テストに必要なテストケースをデータベース内に保存しておく。
   * - パスワードと失敗回数は、セキュリティ上データベースから出さない為、Nullになっていなければならない。
   * - ただし、認証時のアカウント情報の取得メソッドの際は、全部の要素を取得する必要があるため対象外。
   * 
   * @param[in] test_repo テスト対象のリポジトリ
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case_1 参照元のテストケースクラス一つ目
   * @param[in] foreign_case_2 参照元のテストケースクラス二つ目
   * 
   * @see System_User_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public System_User_Repo_Search_Test(System_User_Repo test_repo, 
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

    this.origin_compare = new ArrayList<>();
    this.origin_compare_sys_security = new ArrayList<>();
    for(int i = 1; i <= 10; i++){
      this.origin_compare.add(this.testcase.compareEntityMake(i));
      this.origin_compare_sys_security.add(this.testcase.compareEntityMake(i));
    }

    for(System_User ent: this.origin_compare){
      when(ent.getPassword()).thenReturn(null);
      when(ent.getFail_count()).thenReturn(null);
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
    System_User_Repo_Search_Test.testcase_static = this.testcase;
    System_User_Repo_Search_Test.foreign_case_1_static = this.foreign_case_1;
    System_User_Repo_Search_Test.foreign_case_2_static = this.foreign_case_2;
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
  private void findAllSearchCheck(Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method, 
                                  TestParamBuilder<System_User> test_builder) 
                                  throws ExecutionException, InterruptedException{

    BiConsumer<System_User, System_User> assert_method = (compare, result) -> {
      Member_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<System_User> test_param = test_builder.assert_method(assert_method)
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

    for(int i = 0; i < 10; i++){
      System_User compare_ent = this.origin_compare.get(i);
      String member_id = compare_ent.getMember_id();

      System_User result = this.test_repo.findByMember_id(member_id).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(compare_ent, result, this.softly, true);
    }

    Optional<System_User> empty_ent = this.test_repo.findByMember_id(null);
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

    int all_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_OFFSET);
    int limit_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_OFFSET);


    CompareParam<System_User> list_builder = CompareParam.<System_User>builder().origin_compare(this.origin_compare)
                                                          .build();
    CompareParam<System_User> list_builder_1 = list_builder.toBuilder().skip(all_offset).limit(all_max)
                                                            .build();
    CompareParam<System_User> list_builder_2 = list_builder.toBuilder().skip(limit_offset).limit(limit_offset)
                                                            .build();

    List<System_User> compare_1 = Common_Repository_TestUtils
                      .compareListMake(list_builder_1.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id))
                                                     .build());
    List<System_User> compare_2 = Common_Repository_TestUtils
                      .compareListMake(list_builder_1.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());
    List<System_User> compare_3 = Common_Repository_TestUtils
                      .compareListMake(list_builder_2.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id))
                                                     .build());
    List<System_User> compare_4 = Common_Repository_TestUtils
                      .compareListMake(list_builder_2.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


    Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_1 = s -> {
      return this.test_repo.findAllOriginal(s.getLimit(), s.getOffset());
    };

    Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_2 = s -> {
      return this.test_repo.findAllOriginalDESC(s.getLimit(), s.getOffset());
    };

    TestParam<System_User> test_builder = TestParam.<System_User>builder().all_judge(true).build();
    TestParam<System_User> test_builder_1 = test_builder.toBuilder().limit(all_max).offset(all_offset).build();
    TestParam<System_User> test_builder_2 = test_builder.toBuilder().limit(limit_max).offset(limit_offset).build();

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
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Member_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<System_User> list_builder = CompareParam
                                .<System_User>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getMember_id().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<System_User> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id))
                                                     .build());
      List<System_User> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByMember_id(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByMember_idDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<System_User> test_builder = TestParam.<System_User>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByUsername][findAllByUsernameDESC]を実行した際の
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
  public void 検索対象のカラムがユーザー名の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Username, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<System_User> list_builder = CompareParam
                                .<System_User>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getUsername().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<System_User> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getUsername))
                                                     .sort_second(Comparator.comparing(System_User::getMember_id))
                                                     .build());
      List<System_User> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getUsername, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(System_User::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByUsername(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByUsernameDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<System_User> test_builder = TestParam.<System_User>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByAuth_id][findAllByAuth_idDESC]を実行した際の
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
  public void 検索対象のカラムが権限番号の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Auth_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<System_User> list_builder = CompareParam
                                .<System_User>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getAuth_id().contains(word))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<System_User> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getAuth_id))
                                                     .sort_second(Comparator.comparing(System_User::getMember_id))
                                                     .build());
      List<System_User> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getAuth_id, 
                                                                                      Collections.reverseOrder()))
                                                     .sort_second(Comparator.comparing(System_User::getMember_id, 
                                                                                       Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByAuth_id(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByAuth_idDESC(s.getMain_word(), s.getLimit(), s.getOffset());
      };

      TestParam<System_User> test_builder = TestParam.<System_User>builder().main_word(word)
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
   * @brief リポジトリメソッド[findAllByLocking][findAllByLockingDESC]を実行した際の
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
  public void 検索対象のカラムがロック有無の際のテスト() throws InterruptedException, ExecutionException{

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Locking, i);

      String word = assume[WORD];
      Boolean bool = Boolean.parseBoolean(word);
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<System_User> list_builder = CompareParam
                                .<System_User>builder()
                                .origin_compare(this.origin_compare)
                                .filter(s -> s.getLocking().equals(bool))
                                .skip(offset)
                                .limit(limit)
                                .build();

      List<System_User> compare_1 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id))
                                                     .build());
      List<System_User> compare_2 = Common_Repository_TestUtils
                        .compareListMake(list_builder.toBuilder()
                                                     .sort_first(Comparator.comparing(System_User::getMember_id, 
                                                                                      Collections.reverseOrder()))
                                                     .build());


      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_1 = s -> {
        return this.test_repo.findAllByLocking(s.getMain_word() == null ? null : Boolean.parseBoolean(s.getMain_word()), 
                                               s.getLimit(), s.getOffset());
      };

      Function<TestParam<System_User>, CompletableFuture<List<System_User>>> find_all_method_2 = s -> {
        return this.test_repo.findAllByLockingDESC(s.getMain_word() == null ? null : Boolean.parseBoolean(s.getMain_word()), 
                                                   s.getLimit(), s.getOffset());
      };

      TestParam<System_User> test_builder = TestParam.<System_User>builder().main_word(word)
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
   * @brief リポジトリメソッド[sysSecurity]を実行した際の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、ユーザー名を完全一致指定して検索をした場合、想定された検索結果と、テスト対象
   * メソッドから返ってきた値が一致することを確認する。
   * - 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、比較対象のデータのユーザー名を取得する。
   * -# 取得したユーザー名を渡してテスト対象メソッドを実行し、比較用のエンティティリストで比較を行う。
   * -# その結果、想定通りの結果の内容であることを確認する。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Test
  public void 認証時のアカウント情報を取得する際のテスト(){

    for(int i = 0; i < 10; i++){
      System_User compare_ent = this.origin_compare_sys_security.get(i);
      String username = compare_ent.getUsername();

      System_User result = this.test_repo.sysSecurity(username).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(compare_ent, result, this.softly, true);
    }

    Optional<System_User> empty_ent = this.test_repo.sysSecurity(null);
    this.softly.assertThat(empty_ent.isEmpty()).isTrue();

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

    for(System_User item: this.origin_compare){
      Integer count = this.test_repo.checkUnique(item.getSerial_num(), item.getMember_id());
      this.softly.assertThat(count).isEqualTo(0);
    }

    for(System_User item: this.origin_compare){
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
   * @brief リポジトリメソッド[checkUsernameUnique]を実行した際の検索結果を判定する。
   *
   * @details
   * - テストの内容としては、ユーザー名とシリアルナンバーを完全一致指定で検索した場合、想定される結果件数が
   * 返ってくることを確認する。
   * - このメソッドは、指定したユーザー名の重複確認のため、件数は「1」か「0」のいずれかしか返ってこない。
   * 
   * @par 大まかな処理の流れ
   * -# テストケースから比較用のエンティティリストを取得し、比較対象のデータのユーザー名とシリアルナンバーを
   * 取得する。
   * -# 取得した二つのデータを指定してテスト対象メソッドを実行し、結果件数が必ず「0」になることを確認する。
   * 該当シリアルナンバー以外でユーザー名に重複はないので、必ず0件が返る。
   * -# 次に、メソッドを実行する際に指定するシリアルナンバーを、一つずらして実行した際に、結果件数が必ず
   * 「1」になることを確認する。必ずユーザー名に重複が発生するので1件になる。
   * -# 検索ワードにNullを渡した際には、検索結果が0件となることを確認する。
   **********************************************************************************************
   */
  @Test
  public void ユーザー名の重複チェックメソッドのテスト(){

    for(System_User item: this.origin_compare){
      Integer count = this.test_repo.checkUsernameUnique(item.getSerial_num(), item.getUsername());
      this.softly.assertThat(count).isEqualTo(0);
    }

    for(System_User item: this.origin_compare){
      Integer count = this.test_repo.checkUsernameUnique(item.getSerial_num() + 1, item.getUsername());
      this.softly.assertThat(count).isEqualTo(1);
    }

    Integer null_1 = this.test_repo.checkUsernameUnique(null, this.origin_compare.get(0).getUsername());
    Integer null_2 = this.test_repo.checkUsernameUnique(this.origin_compare.get(0).getSerial_num(), null);
    Integer null_3 = this.test_repo.checkUsernameUnique(null, null);
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
    System_User_Repo_Search_Test.testcase_static.databaseReset();
    System_User_Repo_Search_Test.foreign_case_2_static.databaseReset();
    System_User_Repo_Search_Test.foreign_case_1_static.databaseReset();
  }
}
