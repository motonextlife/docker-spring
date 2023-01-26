/** 
 **************************************************************************************
 * @file Member_Info_Repo_Update_Test.java
 * @brief 主に[団員管理]のリポジトリクラスで、データの更新を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Member_Info_Repo_Test;

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

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Member_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;











/** 
 **************************************************************************************
 * @brief 主に[団員管理]のリポジトリクラスで、データの更新を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Member_Info_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Member_Quad_Repository_TestUtils
 * @see Member_Info_Repo
 **************************************************************************************
 */ 
@Import(Member_Info_TestCase_Make.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Member_Info_Repo_Update_Test{
  
  public static Member_Info_TestCase_Make testcase;
  public static Member_Info_Repo test_repo;
  public static List<Member_Info> compare_entity;
  public static List<Member_Info> after_entity;
  public static Member_Info just_entity;
  public static Member_Info null_entity;
  public static List<Member_Info> failed_entity;
  public static Member_Info unique_miss;

  public static SoftAssertions softly = new SoftAssertions();
  
  
  




  
  
  
  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースへのデータ格納試験の際に用いるテストデータを用意する。
   *
   * @details
   * - 静的変数にインジェクションし、すべてのインナークラスで使用できるようにする。
   * - 用意するデータとしては、比較用の正常データの他、保存に失敗するデータや、保存できるデータの長さの限界値
   * 検査の為に、限界文字数まで文字列を敷き詰めたデータである。
   * - その他、Nullが許容されるカラムに対してNullを保存した際にエラーとならないことを確認するテストケースや、
   * 管理番号の一意制約に違反するテストケースも存在する。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] test_repo テスト対象のリポジトリ
   * 
   * @see Member_Info_Repo
   * @see Member_Quad_Repository_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Member_Info_TestCase_Make testcase, Member_Info_Repo test_repo) throws IOException, ParseException{

    Member_Info_Repo_Update_Test.testcase = testcase;
    Member_Info_Repo_Update_Test.test_repo = test_repo;

    List<Member_Info> compare_entity = new ArrayList<>();
    List<Member_Info> after_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Member_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i)));
      after_entity.add(Member_Quad_Repository_TestUtils.MockToReal(testcase.UpdateAfterEntityMake(i)));
    }

    Member_Info_Repo_Update_Test.compare_entity = compare_entity;
    Member_Info_Repo_Update_Test.after_entity = after_entity;
    Member_Info_Repo_Update_Test.just_entity = Member_Quad_Repository_TestUtils
                                               .MockToReal(testcase.justInTimeEntityMake());
    Member_Info_Repo_Update_Test.null_entity = Member_Quad_Repository_TestUtils
                                               .MockToReal(testcase.okNullEntityMake());
    Member_Info_Repo_Update_Test.unique_miss = Member_Quad_Repository_TestUtils
                                               .MockToReal(testcase.UniqueMissEntityMake());

    List<Member_Info> failed_ent_tmp = testcase.failedEntityMake();
    Member_Info_Repo_Update_Test.failed_entity = new ArrayList<>();

    for(Member_Info fail_ent: failed_ent_tmp){
      Member_Info_Repo_Update_Test.failed_entity.add(Member_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }
  
  
  








  
  
  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Member_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Update_Success {
  
    private final Member_Info_Repo test_repo;
    private final List<Member_Info> compare_entity;
    private final List<Member_Info> after_entity;
    private final Member_Info just_entity;
    private final Member_Info null_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 更新対象データの格納を行う。
     *
     * @details
     * - 更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Success() throws IOException{
      this.test_repo = Member_Info_Repo_Update_Test.test_repo;
      this.compare_entity = Member_Info_Repo_Update_Test.compare_entity;
      this.after_entity = Member_Info_Repo_Update_Test.after_entity;
      this.just_entity = Member_Info_Repo_Update_Test.just_entity;
      this.null_entity = Member_Info_Repo_Update_Test.null_entity;
      this.softly = Member_Info_Repo_Update_Test.softly;

      Member_Info_Repo_Update_Test.testcase.databaseSetup();
    }



    /** 
     **********************************************************************************************
     * @brief データの更新時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを
     * 取得。そのデータがテストケースと同一で、正常に更新されていることを確認する。
     * - また、まだ更新していないデータに関しては、更新前と変わらない状態であることを確認する。
     * - Nullが許容されるカラムに対してNullを保管した際に、エラーにならないことを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを保存した後、そのデータのシリアルナンバーで再度検索してデータを取得。
     * -# テストケースと取得したデータを比較する。
     * -# 更新したデータ以外を、更新前データのテストケースと比較し、一致することを確認する。
     * -# 限界文字数まで敷き詰めたデータを、保存済みの既存のデータの適当なシリアルナンバーで更新し、そのシリアル
     * ナンバーで再度検索した後、更新が成功かつ、データの内容が一致していることを確認する。
     * -# Null許容カラムにNullを、保存済みの既存のデータの適当なシリアルナンバーで更新し、そのシリアルナンバーで
     * 再度検索した後、更新が成功かつ、データの内容が一致していることを確認する。
     * 
     * @see Member_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 更新の成功ケース(){

      for(int i = 0; i < 10; i++){
        Member_Info save_ent = this.after_entity.get(i);
        this.test_repo.save(save_ent);

        Member_Info result = this.test_repo.findById(save_ent.getSerial_num()).get();
        Member_Quad_Repository_TestUtils.assertAllEquals(save_ent, result, this.softly, true);
      }

      for(int i = 10; i < 20; i++){
        Member_Info no_change = this.compare_entity.get(i);

        Member_Info result = this.test_repo.findById(no_change.getSerial_num()).get();
        Member_Quad_Repository_TestUtils.assertAllEquals(no_change, result, this.softly, true);
      }


      Integer just_ent_serial = this.after_entity.get(0).getSerial_num();
      this.just_entity.setSerial_num(just_ent_serial);
      this.test_repo.save(this.just_entity);

      Member_Info after = this.test_repo.findById(just_ent_serial).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after, this.softly, true);


      Integer null_serial = this.after_entity.get(0).getSerial_num();
      this.null_entity.setSerial_num(null_serial);
      this.test_repo.save(this.null_entity);

      Member_Info nulled = this.test_repo.findById(null_serial).get();
      Member_Quad_Repository_TestUtils.assertAllEquals(this.null_entity, nulled, this.softly, false);


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
      Member_Info_Repo_Update_Test.testcase.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Update_Failed {
  
    private final Member_Info_Repo test_repo;
    private final List<Member_Info> failed_entity;

    private final SoftAssertions softly;



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 更新対象データの格納を行う。
     *
     * @details
     * - 更新処理の為、テスト対象テーブルにも更新対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Update_Failed() throws IOException{
      this.test_repo = Member_Info_Repo_Update_Test.test_repo;
      this.failed_entity = Member_Info_Repo_Update_Test.failed_entity;
      this.softly = Member_Info_Repo_Update_Test.softly;

      Member_Info_Repo_Update_Test.testcase.databaseSetup();
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

      for(Member_Info ent: this.failed_entity){
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
      Member_Info_Repo_Update_Test.testcase.databaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの更新処理に関して、一意制約違反によって失敗するテストを実施するインナークラス。
   * 
   * @details なお、一意制約対象の値は、管理番号のみとなる。
   **********************************************************************************************
   */
  @Nested
  public class Unique_Failed {

    private final Member_Info_Repo test_repo;
    private final Member_Info unique_miss;
    private final Integer exclusion_serial;

    private final SoftAssertions softly;
  


    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 初期データを準備する。
     *
     * @details
     * - 一意制約のテストを行うため、あらかじめ内容が被るデータを入れておく必要がある。
     * - また、一意制約テストの際に、更新を除外するデータのシリアルナンバーを控えておく。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルへの初期データを準備する。
     * -# 更新を除外するデータのシリアルナンバー（一意制約エラーの管理番号と同じ物）を控える。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Unique_Failed() throws IOException{
      this.test_repo = Member_Info_Repo_Update_Test.test_repo;
      this.unique_miss = Member_Info_Repo_Update_Test.unique_miss;
      this.softly = Member_Info_Repo_Update_Test.softly;

      Member_Info_Repo_Update_Test.testcase.databaseSetup();

      this.exclusion_serial = this.test_repo.findByMember_id(unique_miss.getMember_id())
                                            .get()
                                            .getSerial_num();
    }



    /** 
     **********************************************************************************************
     * @brief データの更新を行った際に、一意制約違反によってエラーが出ることを確認する。
     *
     * @details
     * - テストの内容としては、データベースのカラムの一意制約に違反し保存に失敗するデータを保存した際に、例外が
     * 投げられエラーになることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 失敗テストケースからデータを取得し、保存時にエラーになることを確認する。
     * -# なお、エラー確認をする際には、一意制約違反として投入する管理番号に紐付いているシリアルナンバー
     * 「以外」のデータに対して行う。
     **********************************************************************************************
     */
    @Test
    public void 更新の一意制約違反のケース(){

      for(int i = 1; i <= 20; i++){
        if(i == this.exclusion_serial){
          continue;
        }

        unique_miss.setSerial_num(i);
        this.softly.assertThatThrownBy(() -> this.test_repo.save(unique_miss))
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
      Member_Info_Repo_Update_Test.testcase.databaseReset();
    }
  }
}
