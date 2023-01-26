/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Score_Pdf_Repo_Test
 * 
 * @brief 通常用リポジトリのうち、[楽譜データ情報]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、楽譜データ情報のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Score_Pdf_Repo_Test;





/** 
 **************************************************************************************
 * @file Score_Pdf_Repo_Delete_Test.java
 * @brief 主に[楽譜データ管理]のリポジトリクラスで、データの削除を行うメソッドのテストを行う
 * クラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
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

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make;
import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.Repository.NormalRepo.Score_Pdf_Repo;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Score_Quad_Repository_TestUtils;










/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]のリポジトリクラスで、データの削除を行うメソッドのテストを
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
 **************************************************************************************
 */ 
@Import({Musical_Score_TestCase_Make.class, Score_Pdf_TestCase_Make.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Score_Pdf_Repo_Delete_Test{

  public static Score_Pdf_Repo test_repo;
  public static Score_Pdf_TestCase_Make testcase;
  public static Musical_Score_TestCase_Make foreign_case;
  public static List<Score_Pdf> compare_entity;

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
   * @see Score_Pdf_Repo
   * @see Score_Quad_Repository_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Score_Pdf_TestCase_Make testcase, 
                            Musical_Score_TestCase_Make foreign_case, 
                            Score_Pdf_Repo test_repo) throws IOException{

    Score_Pdf_Repo_Delete_Test.testcase = testcase;
    Score_Pdf_Repo_Delete_Test.foreign_case = foreign_case;
    Score_Pdf_Repo_Delete_Test.test_repo = test_repo;

    List<Score_Pdf> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Score_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i, false)));
    }

    Score_Pdf_Repo_Delete_Test.compare_entity = compare_entity;
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
  
    private final Score_Pdf_Repo test_repo;
    private final List<Score_Pdf> compare_entity;

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
      this.test_repo = Score_Pdf_Repo_Delete_Test.test_repo;
      this.compare_entity = Score_Pdf_Repo_Delete_Test.compare_entity;
      this.softly = Score_Pdf_Repo_Delete_Test.softly;

      Score_Pdf_Repo_Delete_Test.foreign_case.databaseSetup();
      Score_Pdf_Repo_Delete_Test.testcase.databaseSetup(false);
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
        Score_Pdf del_ent = this.compare_entity.get(i);
        this.test_repo.delete(del_ent);

        this.softly.assertThat(this.test_repo.findById(del_ent.getSerial_num()).isEmpty()).isTrue();
      }

      for(int i = 10; i < 20; i++){
        Score_Pdf no_delete = this.compare_entity.get(i);

        Score_Pdf result = this.test_repo.findById(no_delete.getSerial_num()).get();
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
      Score_Pdf_Repo_Delete_Test.testcase.databaseReset(false);
      Score_Pdf_Repo_Delete_Test.foreign_case.databaseReset();
    }
  }
}
