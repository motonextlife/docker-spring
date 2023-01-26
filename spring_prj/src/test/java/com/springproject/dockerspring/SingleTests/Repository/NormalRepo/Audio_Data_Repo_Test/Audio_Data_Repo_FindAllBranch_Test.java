/** 
 **************************************************************************************
 * @file Audio_Data_Repo_FindAllBranch_Test.java
 * @brief 主に[音源データ管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッドのテスト
 * を行うクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Audio_Data_Repo_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Audio_Data_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Audio_Data_TestCase_Make.Audio_Data_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.RepoForm.Audio_Data_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Sound_Quad_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.TestParam.TestParamBuilder;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Audio_Data_Repo;

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
 * @brief 主に[音源データ管理]のリポジトリクラスで、検索メソッドの分岐と実行を行うメソッドのテスト
 * を行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Audio_Data_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - 並列処理が可能なメソッドのテストの為、並列処理用の設定ファイルをインポートし、並列処理の
 * 使用を許可しておく。
 * 
 * @see Audio_Data_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Audio_Data_TestCase_Make.class, Sound_Source_TestCase_Make.class, RepoConfig.class})
@EnableAsync
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Audio_Data_Repo_FindAllBranch_Test{

  private final Audio_Data_Repo test_repo;
  private final Audio_Data_TestCase_Make testcase;
  private final Sound_Source_TestCase_Make foreign_case;
  private static Audio_Data_TestCase_Make testcase_static;
  private static Sound_Source_TestCase_Make foreign_case_static;

  private final SoftAssertions softly = new SoftAssertions();

  private final List<Audio_Data> origin_compare;





  //** @name 検索時のテストケースの配列のインデックス */
  //** @{ */
    
  //! 検索ワード
  public final int WORD = Audio_Data_RepoForm_TestCase.WORD;

  //! 1ページ当たりの検索結果の出力限界数
  public final int LIMIT = Audio_Data_RepoForm_TestCase.LIMIT;

  //! 検索結果の全件数を出力した際の結果数想定値
  public final int ANSWER_COUNT = Audio_Data_RepoForm_TestCase.ANSWER_COUNT;

  //! オフセット値
  public final int OFFSET = Audio_Data_RepoForm_TestCase.OFFSET;

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
   * @see Audio_Data_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Audio_Data_Repo_FindAllBranch_Test(Audio_Data_Repo test_repo, 
                                            Audio_Data_TestCase_Make testcase, 
                                            Sound_Source_TestCase_Make foreign_case) throws IOException{
    this.test_repo = test_repo;
    this.testcase = testcase;
    this.foreign_case = foreign_case;
    this.foreign_case.databaseSetup();
    this.testcase.databaseSetup(false);

    this.origin_compare = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      this.origin_compare.add(this.testcase.compareEntityMake(i, false));
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
    Audio_Data_Repo_FindAllBranch_Test.testcase_static = this.testcase;
    Audio_Data_Repo_FindAllBranch_Test.foreign_case_static = this.foreign_case;
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
   * @see Sound_Quad_Repository_TestUtils
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  private void findAllBranchCheck(TestParamBuilder<Audio_Data> test_builder) 
      throws ExecutionException, InterruptedException, ParseException{

    BiConsumer<Audio_Data, Audio_Data> assert_method = (compare, result) -> {
      Sound_Quad_Repository_TestUtils.assertAllEquals(compare, result, this.softly, true);
    };

    TestParam<Audio_Data> test_param = test_builder.assert_method(assert_method)
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
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が全検索の際のテスト() throws ExecutionException, InterruptedException, ParseException{

    String subject = this.testcase.subjectRepoForm(Audio_Data_RepoForm_TestCase.ALL);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Audio_Data_TestKeys.Sound_Id, i);

      String word = assume[WORD];
      int limit = Integer.parseInt(assume[LIMIT]);
      int answer_count = Integer.parseInt(assume[ANSWER_COUNT]);
      int offset = Integer.parseInt(assume[OFFSET]);

      CompareParam<Audio_Data> list_builder = CompareParam
                               .<Audio_Data>builder()
                               .origin_compare(this.origin_compare)
                               .filter(s -> s.getSound_id().equals(word))
                               .skip(offset)
                               .limit(limit)
                               .build();

      List<Audio_Data> compare_1 = Common_Repository_TestUtils
                       .compareListMake(list_builder.toBuilder()
                                                    .sort_first(Comparator.comparing(Audio_Data::getSound_name))
                                                    .sort_second(Comparator.comparing(Audio_Data::getSerial_num))
                                                    .build());
      List<Audio_Data> compare_2 = Common_Repository_TestUtils
                       .compareListMake(list_builder.toBuilder()
                                                    .sort_first(Comparator.comparing(Audio_Data::getSound_name, 
                                                                                     Collections.reverseOrder()))
                                                    .sort_second(Comparator.comparing(Audio_Data::getSerial_num, 
                                                                                      Collections.reverseOrder()))
                                                    .build());

      TestParam<Audio_Data> test_builder = TestParam.<Audio_Data>builder()
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
   * @brief リポジトリメソッド[findAllBranch]において、検索種別を[SoundName]にした時の検索結果を判定する。
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
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void 検索分岐メソッドにおいて検索種別が音源ファイル名の際のテスト() throws ExecutionException, InterruptedException, ParseException{

    String subject = this.testcase.subjectRepoForm(Audio_Data_RepoForm_TestCase.SOUND_NAME);

    for(int i = 1; i <= 20; i++){
      String[] assume_id = this.testcase.otherRepoForm(Audio_Data_TestKeys.Sound_Id, i);
      String[] assume_name = this.testcase.otherRepoForm(Audio_Data_TestKeys.Sound_Name, i);

      String id = assume_id[WORD];
      String word = assume_name[WORD];
      int limit = Integer.parseInt(assume_name[LIMIT]);
      int answer_count = Integer.parseInt(assume_name[ANSWER_COUNT]);
      int offset = Integer.parseInt(assume_name[OFFSET]);

      CompareParam<Audio_Data> list_builder = CompareParam
                               .<Audio_Data>builder()
                               .origin_compare(this.origin_compare)
                               .filter(s -> s.getSound_id().equals(id)
                                         && s.getSound_name().contains(word))
                               .skip(offset)
                               .limit(limit)
                               .build();

      List<Audio_Data> compare_1 = Common_Repository_TestUtils
                       .compareListMake(list_builder.toBuilder()
                                                    .sort_first(Comparator.comparing(Audio_Data::getSound_name))
                                                    .sort_second(Comparator.comparing(Audio_Data::getSerial_num))
                                                    .build());
      List<Audio_Data> compare_2 = Common_Repository_TestUtils
                       .compareListMake(list_builder.toBuilder()
                                                    .sort_first(Comparator.comparing(Audio_Data::getSound_name, 
                                                                                     Collections.reverseOrder()))
                                                    .sort_second(Comparator.comparing(Audio_Data::getSerial_num, 
                                                                                      Collections.reverseOrder()))
                                                    .build());

      TestParam<Audio_Data> test_builder = TestParam.<Audio_Data>builder()
                                                    .main_word(id)
                                                    .sub_word(word)
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

    String subject = this.testcase.subjectRepoForm(Audio_Data_RepoForm_TestCase.MISSING);

    for(int i = 1; i <= 20; i++){
      String[] assume = this.testcase.otherRepoForm(Audio_Data_TestKeys.Sound_Id, i);

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
    Audio_Data_Repo_FindAllBranch_Test.testcase_static.databaseReset(false);
    Audio_Data_Repo_FindAllBranch_Test.foreign_case_static.databaseReset();
  }
}
