/** 
 **************************************************************************************
 * @file Sound_Source_History_Repo_Insert_Test.java
 * @brief 主に[音源情報変更履歴]のリポジトリクラスで、データの新規追加を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.HistoryRepo.Sound_Source_History_Repo_Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Repository.HistoryRepo.Sound_Source_History_Repo;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Sound_Quad_Repository_TestUtils;









/** 
 **************************************************************************************
 * @brief 主に[音源情報変更履歴]のリポジトリクラスで、データの新規追加を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Sound_Source_History_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Sound_Quad_Repository_TestUtils
 * @see Sound_Source_History_Repo
 **************************************************************************************
 */ 
@Import(Sound_Source_TestCase_Make.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Sound_Source_History_Repo_Insert_Test{
  
  public static Sound_Source_TestCase_Make testcase;
  public static Sound_Source_History_Repo test_repo;
  public static List<Sound_Source_History> compare_entity;
  public static Sound_Source_History just_entity;
  public static Sound_Source_History null_entity;
  public static List<Sound_Source_History> failed_entity;

  public static SoftAssertions softly = new SoftAssertions();
  
  
  



  
  
  
  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースへのデータ格納試験の際に用いるテストデータを用意する。
   *
   * @details
   * - 静的変数にインジェクションし、すべてのインナークラスで使用できるようにする。
   * - 用意するデータとしては、比較用の正常データの他、保存に失敗するデータや、保存できるデータの長さの限界値
   * 検査の為に、限界文字数まで文字列を敷き詰めたデータである。
   * - その他、Nullが許容されるカラムに対してNullを保存した際にエラーとならないことを確認するテストケースも
   * 存在する。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] test_repo テスト対象のリポジトリ
   * 
   * @see Sound_Source_History_Repo
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Sound_Source_TestCase_Make testcase, Sound_Source_History_Repo test_repo){
    Sound_Source_History_Repo_Insert_Test.testcase = testcase;
    Sound_Source_History_Repo_Insert_Test.test_repo = test_repo;

    List<Sound_Source_History> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Sound_Quad_Repository_TestUtils.MockToReal(testcase.compareHistoryEntityMake(i)));
    }

    Sound_Source_History_Repo_Insert_Test.compare_entity = compare_entity;
    Sound_Source_History_Repo_Insert_Test.just_entity = Sound_Quad_Repository_TestUtils
                                                        .MockToReal(testcase.justInTimeHistoryEntityMake());
    Sound_Source_History_Repo_Insert_Test.null_entity = Sound_Quad_Repository_TestUtils
                                                        .MockToReal(testcase.okNullHistoryEntityMake());

    List<Sound_Source_History> failed_ent_tmp = testcase.failedHistoryEntityMake();
    Sound_Source_History_Repo_Insert_Test.failed_entity = new ArrayList<>();

    for(Sound_Source_History fail_ent: failed_ent_tmp){
      Sound_Source_History_Repo_Insert_Test.failed_entity.add(Sound_Quad_Repository_TestUtils.MockToReal(fail_ent));
    }
  }
  
  
  






  
  
  /** 
   **********************************************************************************************
   * @brief データの新規追加処理に関して、正常に保存が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Sound_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Insert_Success {
  
    private final Sound_Source_History_Repo test_repo;
    private final List<Sound_Source_History> compare_entity;
    private final Sound_Source_History just_entity;
    private final Sound_Source_History null_entity;

    private final SoftAssertions softly;




    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象のテーブルを
     * リセットする。
     *
     * @details
     * - テスト前の段階でテーブル内にデータが残っている場合があるため、明示的にリセットが必要である。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブル内のデータをすべて削除し、リセットする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Insert_Success() throws IOException{
      this.test_repo = Sound_Source_History_Repo_Insert_Test.test_repo;
      this.compare_entity = Sound_Source_History_Repo_Insert_Test.compare_entity;
      this.just_entity = Sound_Source_History_Repo_Insert_Test.just_entity;
      this.null_entity = Sound_Source_History_Repo_Insert_Test.null_entity;
      this.softly = Sound_Source_History_Repo_Insert_Test.softly;

      Sound_Source_History_Repo_Insert_Test.testcase.historyDatabaseReset();
    }





    /** 
     **********************************************************************************************
     * @brief データの新規追加時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、テストケースを一個だけ保存した後、データを全取得して最初のデータを比較した際に、
     * 最初に保存したテストケースと内容が一致することを確認する。
     * - また、限界値検査の為の、限界文字数まで敷き詰めたデータを保存した場合に成功し、データも一致している事を
     * 確認する。
     * - Nullが許容されるカラムに対してNullを保管した際に、エラーにならないことを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを一個ずつ保存した後、全取得メソッドでデータを取得し、リストの最初のデータを比較する。
     * -# 比較を終了したら、テーブル内の全削除メソッドで削除し、この一連の処理をテストケースの数だけ繰り返す。
     * -# 限界文字数まで敷き詰めたデータを保存し、保存が成功かつ、データの内容が一致していることを確認する。
     * -# Null許容カラムにNullを保存し、保存が成功かつ、データの内容が一致していることを確認する。
     * 
     * @see Sound_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 新規追加の成功ケース(){

      for(Sound_Source_History ent: this.compare_entity){
        ent.setHistory_id(null);
        this.test_repo.save(ent);

        List<Sound_Source_History> result = (List<Sound_Source_History>)this.test_repo.findAll();
        Sound_Quad_Repository_TestUtils.assertAllEquals(ent, result.get(0), this.softly, false);

        this.test_repo.deleteAll();
      }


      this.just_entity.setHistory_id(null);
      this.test_repo.save(this.just_entity);

      List<Sound_Source_History> after = (List<Sound_Source_History>)this.test_repo.findAll();
      Sound_Quad_Repository_TestUtils.assertAllEquals(this.just_entity, after.get(0), this.softly, false);

      this.test_repo.deleteAll();


      this.null_entity.setHistory_id(null);
      this.test_repo.save(this.null_entity);

      List<Sound_Source_History> nulled = (List<Sound_Source_History>)this.test_repo.findAll();
      Sound_Quad_Repository_TestUtils.assertAllEquals(this.null_entity, nulled.get(0), this.softly, false);

      this.test_repo.deleteAll();


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
      Sound_Source_History_Repo_Insert_Test.testcase.historyDatabaseReset();
    }
  }













  /** 
   **********************************************************************************************
   * @brief データの新規追加処理に関して、正常に失敗するパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Insert_Failed {
  
    private final Sound_Source_History_Repo test_repo;
    private final List<Sound_Source_History> failed_entity;

    private final SoftAssertions softly;



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象のテーブルを
     * リセットする。
     *
     * @details
     * - テスト前の段階でテーブル内にデータが残っている場合があるため、明示的にリセットが必要である。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブル内のデータをすべて削除し、リセットする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Insert_Failed() throws IOException{
      this.test_repo = Sound_Source_History_Repo_Insert_Test.test_repo;
      this.failed_entity = Sound_Source_History_Repo_Insert_Test.failed_entity;
      this.softly = Sound_Source_History_Repo_Insert_Test.softly;

      Sound_Source_History_Repo_Insert_Test.testcase.historyDatabaseReset();
    }




    /** 
     **********************************************************************************************
     * @brief データの新規追加時の動作の確認を行う。なお、これは失敗ケースである。
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
    public void 新規追加の失敗ケース(){

      for(Sound_Source_History ent: this.failed_entity){
        ent.setHistory_id(null);

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
      Sound_Source_History_Repo_Insert_Test.testcase.historyDatabaseReset();
    }
  }
}
