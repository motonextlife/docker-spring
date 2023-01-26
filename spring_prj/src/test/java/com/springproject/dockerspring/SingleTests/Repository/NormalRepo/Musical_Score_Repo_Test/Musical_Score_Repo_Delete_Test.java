/** 
 **************************************************************************************
 * @file Musical_Score_Repo_Delete_Test.java
 * @brief 主に[楽譜管理]のリポジトリクラスで、データの削除を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Musical_Score_Repo_Test;

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

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Score_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.Repository.NormalRepo.Musical_Score_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;









/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]のリポジトリクラスで、データの削除を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Musical_Score_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Score_Quad_Repository_TestUtils
 * @see Musical_Score_Repo
 **************************************************************************************
 */ 
@Import({Musical_Score_TestCase_Make.class, Score_Pdf_TestCase_Make.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Musical_Score_Repo_Delete_Test{
 
  public static Musical_Score_Repo test_repo;
  public static Musical_Score_TestCase_Make testcase;
  public static Score_Pdf_TestCase_Make foreign_case;
  public static List<Musical_Score> compare_entity;
  public static List<Score_Pdf> foreign_entity;

  public static SoftAssertions softly = new SoftAssertions();







  

  
  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、データベースへのデータ格納試験の際に用いるテストデータを用意する。
   *
   * @details
   * - 静的変数にインジェクションし、すべてのインナークラスで使用できるようにする。
   * - その他、Nullが許容されるカラムに対してNullを保存した際にエラーとならないことを確認するテストケースや、
   * 管理番号の一意制約に違反するテストケースも存在する。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case テスト対象テーブルを参照するテストケース
   * @param[in] test_repo テスト対象のリポジトリ
   * 
   * @see Musical_Score_Repo
   * @see Score_Quad_Repository_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Musical_Score_TestCase_Make testcase, 
                            Score_Pdf_TestCase_Make foreign_case, 
                            Musical_Score_Repo test_repo) throws IOException{

    Musical_Score_Repo_Delete_Test.testcase = testcase;
    Musical_Score_Repo_Delete_Test.foreign_case = foreign_case;
    Musical_Score_Repo_Delete_Test.test_repo = test_repo;

    List<Musical_Score> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Score_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i)));
    }

    Musical_Score_Repo_Delete_Test.compare_entity = compare_entity;

    
    List<Score_Pdf> foreign_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      foreign_entity.add(Score_Quad_Repository_TestUtils.MockToReal(foreign_case.compareEntityMake(i, false)));
    }

    Musical_Score_Repo_Delete_Test.foreign_entity = foreign_entity;
  }
  
  







  
  
  
  /** 
   **********************************************************************************************
   * @brief データの削除処理に関して、正常に削除が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Delete_Success {
  
    private final Musical_Score_Repo test_repo;
    private final List<Musical_Score> compare_entity;

    private final SoftAssertions softly;
  



    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 削除対象データの格納を行う。
     *
     * @details
     * - 削除処理の為、テスト対象テーブルにも削除対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Delete_Success() throws IOException{
      this.test_repo = Musical_Score_Repo_Delete_Test.test_repo;
      this.compare_entity = Musical_Score_Repo_Delete_Test.compare_entity;
      this.softly = Musical_Score_Repo_Delete_Test.softly;

      Musical_Score_Repo_Delete_Test.testcase.databaseSetup();
    }



    /** 
     **********************************************************************************************
     * @brief データの削除時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、指定のデータを削除した後、再度そのデータを検索しデータが存在しないことを確認する。
     * - また、まだ削除していないデータに関しては、前と変わらない状態であることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# テストケースを削除した後、そのデータのシリアルナンバーで再度検索してデータを検索。
     * -# データが存在しないことを確認する。
     * -# 削除したデータ以外を、削除前データのテストケースと比較し、一致することを確認する。
     * 
     * @see Score_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 削除の成功ケース(){

      for(int i = 0; i < 10; i++){
        Musical_Score del_ent = this.compare_entity.get(i);
        this.test_repo.delete(del_ent);

        this.softly.assertThat(this.test_repo.findById(del_ent.getSerial_num()).isEmpty()).isTrue();
      }

      for(int i = 10; i < 20; i++){
        Musical_Score no_delete = this.compare_entity.get(i);

        Musical_Score result = this.test_repo.findById(no_delete.getSerial_num()).get();
        Score_Quad_Repository_TestUtils.assertAllEquals(no_delete, result, this.softly, true);
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
      Musical_Score_Repo_Delete_Test.testcase.databaseReset();
    }
  }











  /** 
   **********************************************************************************************
   * @brief 削除処理を行う際に、参照制約違反によってエラーが起こるパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Delete_Failed {

    private final Musical_Score_Repo test_repo;
    private final List<Score_Pdf> foreign_entity;

    private final SoftAssertions softly;
  


    /** 
     **********************************************************************************************
     * @brief このインナークラス内で使用するフィールド変数のセットアップと同時に、テスト対象テーブルへの
     * 削除対象データの格納を行う。
     *
     * @details
     * - 削除処理の為、テスト対象テーブルにも削除対象のデータがあらかじめ必要になる。
     * 
     * @par 大まかな処理の流れ
     * -# 使用するフィールド変数をセットする。
     * -# テスト対象テーブルに更新対象データをあらかじめセットアップする。
     * -# テスト対象テーブルを参照するテストケースデータを参照先にセットアップする。
     * 
     * @throw IOException
     **********************************************************************************************
     */
    public Delete_Failed() throws IOException{
      this.test_repo = Musical_Score_Repo_Delete_Test.test_repo;
      this.foreign_entity = Musical_Score_Repo_Delete_Test.foreign_entity;
      this.softly = Musical_Score_Repo_Delete_Test.softly;

      Musical_Score_Repo_Delete_Test.testcase.databaseSetup();
      Musical_Score_Repo_Delete_Test.foreign_case.databaseSetup(false);
    }




    /** 
     **********************************************************************************************
     * @brief データの削除時の動作の確認を行う。なお、これは失敗ケースである。
     *
     * @details
     * - テストの内容としては、指定のデータを削除しようとした際に、参照制約エラーにより削除が失敗することを
     * 確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 参照先のテーブルから、現状テスト対象テーブルから参照している管理番号を取得する。
     * -# 取得した該当の管理番号のデータを削除する。
     * -# その際にエラーが発生することを確認する。
     **********************************************************************************************
     */
    @Test
    public void 削除の失敗ケース(){
    
      for(Score_Pdf del_ent: this.foreign_entity){
        Musical_Score delete_entity = this.test_repo.findByScore_Id(del_ent.getScore_id()).get();

        this.softly.assertThatThrownBy(() -> this.test_repo.delete(delete_entity))
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
      Musical_Score_Repo_Delete_Test.foreign_case.databaseReset(false);
      Musical_Score_Repo_Delete_Test.testcase.databaseReset();
    }
  }
}
