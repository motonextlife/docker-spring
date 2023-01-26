/** 
 **************************************************************************************
 * @file Facility_Photo_Repo_Update_Test.java
 * @brief 主に[設備写真データ管理]のリポジトリクラスで、データの更新を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Facility_Photo_Repo_Test;

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

import com.springproject.dockerspring.Entity.NormalEntity.Facility_Photo;
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Facility_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Repository.RepoConfig;
import com.springproject.dockerspring.Repository.NormalRepo.Facility_Photo_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Facility_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;










/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]のリポジトリクラスで、データの更新を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_Photo_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Facility_Quad_Repository_TestUtils
 * @see Facility_Photo_Repo
 * @see Facility_Repo
 * @see RepoConfig
 **************************************************************************************
 */ 
@Import({Facility_Photo_TestCase_Make.class, Facility_TestCase_Make.class, RepoConfig.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Facility_Photo_Repo_Update_Test{
  
  public static Facility_Photo_TestCase_Make testcase;
  public static Facility_TestCase_Make foreign_case;
  public static Facility_Photo_Repo test_repo;
  public static Facility_Repo foreign_repo;
  public static List<Facility_Photo> compare_entity;
  public static List<Facility_Photo> after_entity;
  public static Facility_Photo just_entity;
  public static List<Facility_Photo> failed_entity;

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
   * @see Facility_Photo_Repo
   * @see Sound_Source_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Facility_Photo_TestCase_Make testcase, 
                            Facility_TestCase_Make foreign_case, 
                            Facility_Photo_Repo test_repo, 
                            Facility_Repo foreign_repo) throws IOException{

    Facility_Photo_Repo_Update_Test.testcase = testcase;
    Facility_Photo_Repo_Update_Test.foreign_case = foreign_case;
    Facility_Photo_Repo_Update_Test.test_repo = test_repo;
    Facility_Photo_Repo_Update_Test.foreign_repo = foreign_repo;

    List<Facility_Photo> compare_entity = new ArrayList<>();
    List<Facility_Photo> after_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Facility_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i, false)));
      after_entity.add(Facility_Quad_Repository_TestUtils.MockToReal(testcase.UpdateAfterEntityMake(i)));
    }

    Facility_Photo_Repo_Update_Test.compare_entity = compare_entity;
    Facility_Photo_Repo_Update_Test.after_entity = after_entity;
    Facility_Photo_Repo_Update_Test.just_entity = Facility_Quad_Repository_TestUtils
                                                  .MockToReal(testcase.justInTimeEntityMake());

    List<Facility_Photo> failed_ent_tmp = testcase.failedEntityMake();
    Facility_Photo_Repo_Update_Test.failed_entity = new ArrayList<>();

    for(Facility_Photo fail_ent: failed_ent_tmp){
      Facility_Photo_Repo_Update_Test.failed_entity.add(Facility_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }
  
  
  







  
  
  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Facility_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Update_Success {
  
    private final Facility_Photo_Repo test_repo;
    private final List<Facility_Photo> compare_entity;
    private final List<Facility_Photo> after_entity;
    private final Facility_Photo just_entity;

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
    public Update_Success() throws IOException{
      this.test_repo = Facility_Photo_Repo_Update_Test.test_repo;
      this.compare_entity = Facility_Photo_Repo_Update_Test.compare_entity;
      this.after_entity = Facility_Photo_Repo_Update_Test.after_entity;
      this.just_entity = Facility_Photo_Repo_Update_Test.just_entity;
      this.softly = Facility_Photo_Repo_Update_Test.softly;

      Facility_Photo_Repo_Update_Test.foreign_case.databaseSetup();
      
      Facility foreign_just_entity = Facility_Quad_Repository_TestUtils
                                     .MockToReal(foreign_case.justInTimeEntityMake());

      foreign_just_entity.setSerial_num(null);
      Facility_Photo_Repo_Update_Test.foreign_repo.save(foreign_just_entity);

      Facility_Photo_Repo_Update_Test.testcase.databaseSetup(false);
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
     * @see Facility_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 更新の成功ケース(){

      for(int i = 0; i < 10; i++){
        Facility_Photo save_ent = this.after_entity.get(i);
        this.test_repo.save(save_ent);

        Facility_Photo result = this.test_repo.findById(save_ent.getSerial_num()).get();
        Facility_Quad_Repository_TestUtils.assertAllEquals(save_ent, result, this.softly, true);
      }

      for(int i = 10; i < 20; i++){
        Facility_Photo no_change = this.compare_entity.get(i);

        Facility_Photo result = this.test_repo.findById(no_change.getSerial_num()).get();
        Facility_Quad_Repository_TestUtils.assertAllEquals(no_change, result, this.softly, true);
      }


      Integer just_ent_serial = this.after_entity.get(0).getSerial_num();
      this.just_entity.setSerial_num(just_ent_serial);
      this.test_repo.save(this.just_entity);

      Facility_Photo after = this.test_repo.findById(just_ent_serial).get();
      Facility_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after, this.softly, true);

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
      Facility_Photo_Repo_Update_Test.testcase.databaseReset(false);
      Facility_Photo_Repo_Update_Test.foreign_case.databaseReset();
    }
  }












  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Update_Failed {
  
    private final Facility_Photo_Repo test_repo;
    private final List<Facility_Photo> failed_entity;

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
      this.test_repo = Facility_Photo_Repo_Update_Test.test_repo;
      this.failed_entity = Facility_Photo_Repo_Update_Test.failed_entity;
      this.softly = Facility_Photo_Repo_Update_Test.softly;

      Facility_Photo_Repo_Update_Test.foreign_case.databaseSetup();
      Facility_Photo_Repo_Update_Test.testcase.databaseSetup(false);
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

      for(Facility_Photo ent: this.failed_entity){
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
      Facility_Photo_Repo_Update_Test.testcase.databaseReset(false);
      Facility_Photo_Repo_Update_Test.foreign_case.databaseReset();
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
   * @see Facility_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Foreign_Success {

    private final Facility_Photo_Repo test_repo;
    private final Facility_Repo foreign_repo;
    private final Facility_Photo before_entity;

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
      this.test_repo = Facility_Photo_Repo_Update_Test.test_repo;
      this.foreign_repo = Facility_Photo_Repo_Update_Test.foreign_repo;
      this.softly = Facility_Photo_Repo_Update_Test.softly;
      
      this.before_entity = Facility_Photo_Repo_Update_Test.compare_entity.get(0);

      Facility_Photo_Repo_Update_Test.foreign_case.databaseSetup();
      Facility_Photo_Repo_Update_Test.testcase.databaseSetup(false);
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
     * @see Facility_Quad_Repository_TestUtils
     * 
     * @throw InterruptedException
     * @throw ExecutionException
     **********************************************************************************************
     */
    @Test
    public void 更新の参照元への追従確認() throws InterruptedException, ExecutionException{

      String target_id = this.before_entity.getFaci_id();
      List<Facility_Photo> target = this.test_repo.findByFaci_id(target_id, 1, 0).get();

      String after_id = "change-after-001";
      Facility foreign_entity = this.foreign_repo.findByFaci_Id(target_id).get();
      foreign_entity.setFaci_id(after_id);

      this.foreign_repo.save(foreign_entity);


      for(Facility_Photo ent: target){
        Facility_Photo after = this.test_repo.findById(ent.getSerial_num()).get();

        this.softly.assertThat(after.getFaci_id()).isEqualTo(after_id);
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
      Facility_Photo_Repo_Update_Test.testcase.databaseReset(false);
      Facility_Photo_Repo_Update_Test.foreign_case.databaseReset();
    }
  }
}
