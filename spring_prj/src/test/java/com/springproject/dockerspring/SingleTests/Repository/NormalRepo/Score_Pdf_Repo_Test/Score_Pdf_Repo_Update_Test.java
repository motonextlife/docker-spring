/** 
 **************************************************************************************
 * @file Score_Pdf_Repo_Update_Test.java
 * @brief 主に[楽譜データ管理]のリポジトリクラスで、データの更新を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Score_Pdf_Repo_Test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Score_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Repository.NormalRepo.Score_Pdf_Repo;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Musical_Score_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;










/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]のリポジトリクラスで、データの更新を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Score_Pdf_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Score_Quad_Repository_TestUtils
 * @see Score_Pdf_Repo
 * @see Musical_Score_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Score_Pdf_TestCase_Make.class, Musical_Score_TestCase_Make.class, RepoConfig.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Score_Pdf_Repo_Update_Test{
  
  public static Score_Pdf_TestCase_Make testcase;
  public static Musical_Score_TestCase_Make foreign_case;
  public static Score_Pdf_Repo test_repo;
  public static Musical_Score_Repo foreign_repo;
  public static List<Score_Pdf> compare_entity;
  public static List<Score_Pdf> after_entity;
  public static Score_Pdf just_entity;
  public static List<Score_Pdf> failed_entity;

  public static SoftAssertions softly = new SoftAssertions();
  
  
  
  





  
  
  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースへのデータ格納試験の際に用いるテストデータを用意する。
   *
   * @details
   * - 静的変数にインジェクションし、すべてのインナークラスで使用できるようにする。
   * - 用意するデータとしては、比較用の正常データの他、保存に失敗するデータや、保存できるデータの長さの限界値
   * 検査の為に、限界文字数まで文字列を敷き詰めたデータである。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case 参照を行うテーブルのテストケース
   * @param[in] test_repo テスト対象のリポジトリ
   * @param[in] foreign_repo 参照を行うテーブルのリポジトリ
   * 
   * @see Score_Pdf_Repo
   * @see Musical_Score_Repo
   * @see Score_Quad_Repository_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Score_Pdf_TestCase_Make testcase, 
                            Musical_Score_TestCase_Make foreign_case, 
                            Score_Pdf_Repo test_repo, 
                            Musical_Score_Repo foreign_repo) throws IOException{

    Score_Pdf_Repo_Update_Test.testcase = testcase;
    Score_Pdf_Repo_Update_Test.foreign_case = foreign_case;
    Score_Pdf_Repo_Update_Test.test_repo = test_repo;
    Score_Pdf_Repo_Update_Test.foreign_repo = foreign_repo;

    List<Score_Pdf> compare_entity = new ArrayList<>();
    List<Score_Pdf> after_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Score_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i, false)));
      after_entity.add(Score_Quad_Repository_TestUtils.MockToReal(testcase.UpdateAfterEntityMake(i)));
    }

    Score_Pdf_Repo_Update_Test.compare_entity = compare_entity;
    Score_Pdf_Repo_Update_Test.after_entity = after_entity;
    Score_Pdf_Repo_Update_Test.just_entity = Score_Quad_Repository_TestUtils
                                             .MockToReal(testcase.justInTimeEntityMake());

    List<Score_Pdf> failed_ent_tmp = testcase.failedEntityMake();
    Score_Pdf_Repo_Update_Test.failed_entity = new ArrayList<>();

    for(Score_Pdf fail_ent: failed_ent_tmp){
      Score_Pdf_Repo_Update_Test.failed_entity.add(Score_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }
  








  
  
  
  
  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Update_Success {
  
    private final Score_Pdf_Repo test_repo;
    private final List<Score_Pdf> compare_entity;
    private final List<Score_Pdf> after_entity;
    private final Score_Pdf just_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - なお、更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @see Score_Quad_Repository_TestUtils
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Success() throws IOException{
      this.test_repo = Score_Pdf_Repo_Update_Test.test_repo;
      this.compare_entity = Score_Pdf_Repo_Update_Test.compare_entity;
      this.after_entity = Score_Pdf_Repo_Update_Test.after_entity;
      this.just_entity = Score_Pdf_Repo_Update_Test.just_entity;
      this.softly = Score_Pdf_Repo_Update_Test.softly;

      Score_Pdf_Repo_Update_Test.foreign_case.databaseSetup();
      
      Musical_Score foreign_just_entity = Score_Quad_Repository_TestUtils
                                          .MockToReal(foreign_case.justInTimeEntityMake());

      foreign_just_entity.setSerial_num(null);
      Score_Pdf_Repo_Update_Test.foreign_repo.save(foreign_just_entity);

      Score_Pdf_Repo_Update_Test.testcase.databaseSetup(false);
    }




    /** 
     **********************************************************************************************
     * @brief データの更新時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを
     * 取得。そのデータがテストケースと同一で、正常に更新されていることを確認する。
     * - また、まだ更新していないデータに関しては、更新前と変わらない状態であることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを取得。
     * -# テストケースと取得したデータを比較する。
     * -# 更新したデータ以外を、更新前データのテストケースと比較し、一致することを確認する。
     * -# 限界文字数まで敷き詰めたデータを、保存済みの既存のデータの適当なシリアルナンバーで更新し、そのシリアル
     * ナンバーで再度検索した後、更新が成功かつ、データの内容が一致していることを確認する。
     * 
     * @see Score_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 更新の成功ケース(){

      for(int i = 0; i < 10; i++){
        Score_Pdf save_ent = this.after_entity.get(i);
        this.test_repo.save(save_ent);

        Score_Pdf result = this.test_repo.findById(save_ent.getSerial_num()).get();
        Score_Quad_Repository_TestUtils.assertAllEquals(save_ent, result, this.softly, true);
      }

      for(int i = 10; i < 20; i++){
        Score_Pdf no_change = this.compare_entity.get(i);

        Score_Pdf result = this.test_repo.findById(no_change.getSerial_num()).get();
        Score_Quad_Repository_TestUtils.assertAllEquals(no_change, result, this.softly, true);
      }


      Integer just_ent_serial = this.after_entity.get(0).getSerial_num();
      this.just_entity.setSerial_num(just_ent_serial);
      this.test_repo.save(this.just_entity);

      Score_Pdf after = this.test_repo.findById(just_ent_serial).get();
      Score_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after, this.softly, true);

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
      Score_Pdf_Repo_Update_Test.testcase.databaseReset(false);
      Score_Pdf_Repo_Update_Test.foreign_case.databaseReset();
    }
  }












  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Update_Failed {
  
    private final Score_Pdf_Repo test_repo;
    private final List<Score_Pdf> failed_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - なお、更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Failed() throws IOException{
      this.test_repo = Score_Pdf_Repo_Update_Test.test_repo;
      this.failed_entity = Score_Pdf_Repo_Update_Test.failed_entity;
      this.softly = Score_Pdf_Repo_Update_Test.softly;

      Score_Pdf_Repo_Update_Test.foreign_case.databaseSetup();
      Score_Pdf_Repo_Update_Test.testcase.databaseSetup(false);
    }




    /** 
     **********************************************************************************************
     * @brief データの更新時の動作の確認を行う。なお、これは失敗ケースである。
     *
     * @details
     * - テストの内容としては、データベースのカラムの制約に違反し保存に失敗するデータを保存した際に、例外が
     * 投げられエラーになることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 失敗テストケースからデータを取得し、保存時にエラーになることを確認する。
     * -# この一連の処理をテストケースの数だけ繰り返す。
     **********************************************************************************************
     */
    @Test
    public void 更新の失敗ケース(){

      for(Score_Pdf ent: this.failed_entity){
        this.softly.assertThatThrownBy(() -> this.test_repo.save(ent))
                   .isInstanceOf(DbActionExecutionException.class);
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
      Score_Pdf_Repo_Update_Test.testcase.databaseReset(false);
      Score_Pdf_Repo_Update_Test.foreign_case.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief 参照元のテーブルの情報を変更した際に、こちらのテスト対象テーブルの内容も追従することを確認する。
   * 
   * @details 
   * - データベースの制約として、参照制約のある値に関しては、「参照元のテーブルの変更に、参照先が追従」という
   * 設定になっているので、その確認を行う。
   * - なお、参照制約がついているの管理番号のみになるため、検査対象は管理番号の値のみとする。
   * 
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Foreign_Success {

    private final Score_Pdf_Repo test_repo;
    private final Musical_Score_Repo foreign_repo;
    private final Score_Pdf before_entity;

    private final SoftAssertions softly;
  



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、参照元のテーブルへの
     * データのセットアップを行う。
     *
     * @details
     * - このテスト対象のテーブルは、他のテーブルを参照しているため、更新処理を行う場合は参照元のテーブルに
     * あらかじめ参照可能なデータを入れておく必要がある。
     * - なお、更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# 参照制約のテスト対象とする管理番号のエンティティを一つ抽出しておく。
     * -# 参照元のテーブルに参照可能なデータをあらかじめセットアップする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Foreign_Success() throws IOException{
      this.test_repo = Score_Pdf_Repo_Update_Test.test_repo;
      this.foreign_repo = Score_Pdf_Repo_Update_Test.foreign_repo;
      this.softly = Score_Pdf_Repo_Update_Test.softly;
      
      this.before_entity = Score_Pdf_Repo_Update_Test.compare_entity.get(0);

      Score_Pdf_Repo_Update_Test.foreign_case.databaseSetup();
      Score_Pdf_Repo_Update_Test.testcase.databaseSetup(false);
    }




    /** 
     **********************************************************************************************
     * @brief 参照元の管理番号を変更した際に、参照先の管理番号が追従して変更されることを確認する。
     *
     * @details
     * - テストの内容は、参照先のテスト対象テーブルの保存データから、テスト対象とする管理番号を選び、その
     * 番号と同じ参照元テーブル内のデータを変更する。そうすれば、参照先のデータも追従する。
     * 
     * @par 大まかな処理の流れ
     * -# 更新するデータのシリアルナンバーを記憶しておき、参照元のデータを上書きする管理番号で更新する。
     * -# 更新後、記憶しておいたシリアルナンバーで参照先テーブルを検索し、取得したデータの管理番号が
     * 参照元テーブル側で更新した管理番号に変更されていることを確認する。
     * 
     * @see Score_Quad_Repository_TestUtils
     * 
     * @throw InterruptedException
     * @throw ExecutionException
     **********************************************************************************************
     */
    @Test
    public void 更新の参照元への追従確認() throws InterruptedException, ExecutionException{

      String target_id = this.before_entity.getScore_id();
      List<Score_Pdf> target = this.test_repo.findByScore_id(target_id, 1, 0).get();

      String after_id = "change-after-001";
      Musical_Score foreign_entity = this.foreign_repo.findByScore_Id(target_id).get();
      foreign_entity.setScore_id(after_id);

      this.foreign_repo.save(foreign_entity);


      for(Score_Pdf ent: target){
        Score_Pdf after = this.test_repo.findById(ent.getSerial_num()).get();

        this.softly.assertThat(after.getScore_id()).isEqualTo(after_id);
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
      Score_Pdf_Repo_Update_Test.testcase.databaseReset(false);
      Score_Pdf_Repo_Update_Test.foreign_case.databaseReset();
    }
  }
}
