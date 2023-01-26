/** 
 **************************************************************************************
 * @file System_User_Repo_FindAllBranch_Test.java
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッド
 * のテストを行うクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.System_User_Repo_Test;

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
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;












/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッド
 * のテストを行うクラス
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
public class System_User_Repo_FindAllBranch_Test{

  private final System_User_Repo test_repo;
  private final System_User_TestCase_Make testcase;
  private final Member_Info_TestCase_Make foreign_case_1;
  private final Usage_Authority_TestCase_Make foreign_case_2;
  private static System_User_TestCase_Make testcase_static;
  private static Member_Info_TestCase_Make foreign_case_1_static;
  private static Usage_Authority_TestCase_Make foreign_case_2_static;

  private final SoftAssertions softly = new SoftAssertions();

  private final List<System_User> origin_compare;








  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */

  //! 検索ワード
  public final int WORD = System_User_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数
  public final int LIMIT = System_User_RepoForm_TestCase.LIMIT;

  //! 検索結果の全件数を出力した際の結果数想定値
  public final int ANSWER_COUNT = System_User_RepoForm_TestCase.ANSWER_COUNT;

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
  public System_User_Repo_FindAllBranch_Test(System_User_Repo test_repo, 
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
    for(int i = 1; i <= 10; i++){
      this.origin_compare.add(this.testcase.compareEntityMake(i));
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
    System_User_Repo_FindAllBranch_Test.testcase_static = this.testcase;
    System_User_Repo_FindAllBranch_Test.foreign_case_1_static = this.foreign_case_1;
    System_User_Repo_FindAllBranch_Test.foreign_case_2_static = this.foreign_case_2;
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
   * @see Member_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  private void findAllBranchCheck(TestParamBuilder<System_User> test_builder) 
      throws ExecutionException, InterruptedException, ParseException{

    BiConsumer<System_User, System_User> assert_method = (compare, result) -> {
      Member_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<System_User> test_param = test_builder.assert_method(assert_method)
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

    int all_answer_count = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_ANSWER);
    int all_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_MAX);
    int all_offset = this.testcase.allRepoForm(System_User_RepoForm_TestCase.ALL_OFFSET);
    int limit_answer_count = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_ANSWER);
    int limit_max = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_MAX);
    int limit_offset = this.testcase.allRepoForm(System_User_RepoForm_TestCase.LIMIT_OFFSET);

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.ALL);

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

    TestParam<System_User> test_builder = TestParam.<System_User>builder().subject(subject).all_judge(true)
                                                   .build();
    TestParam<System_User> test_builder_1 = test_builder.toBuilder()
                           .max(all_max).limit(all_max).offset(all_offset).answer_count(all_answer_count)
                           .build();
    TestParam<System_User> test_builder_2 = test_builder.toBuilder()
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
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[MemberId]にした時の検索結果を判定する。
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
  public void 検索分岐メソッドにおいて検索種別が団員番号の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.MEMBER_ID);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Member_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT]);
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

      TestParam<System_User> test_builder = TestParam.<System_User>builder()
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
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Username]にした時の検索結果を判定する。
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
  public void 検索分岐メソッドにおいて検索種別がユーザー名の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.USERNAME);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Username, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT]);
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

      TestParam<System_User> test_builder = TestParam.<System_User>builder()
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
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[AuthId]にした時の検索結果を判定する。
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
  public void 検索分岐メソッドにおいて検索種別が権限番号の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.AUTH_ID);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Auth_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT]);
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

      TestParam<System_User> test_builder = TestParam.<System_User>builder()
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
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[Locking]にした時の検索結果を判定する。
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
  public void 検索分岐メソッドにおいて検索種別がロック有無の際のテスト() throws InterruptedException, ExecutionException, ParseException{

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.LOCKING);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Locking, i);

      String word = assume[WORD];
      Boolean bool = Boolean.parseBoolean(word);
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT]);
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

      TestParam<System_User> test_builder = TestParam.<System_User>builder()
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

    String subject = this.testcase.subjectRepoForm(System_User_RepoForm_TestCase.MISSING);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(System_User_TestKeys.Member_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int offset = Integer.parseInt(assume[OFFSET]);

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
    System_User_Repo_FindAllBranch_Test.testcase_static.databaseReset();
    System_User_Repo_FindAllBranch_Test.foreign_case_1_static.databaseReset();
    System_User_Repo_FindAllBranch_Test.foreign_case_2_static.databaseReset();
  }
}
