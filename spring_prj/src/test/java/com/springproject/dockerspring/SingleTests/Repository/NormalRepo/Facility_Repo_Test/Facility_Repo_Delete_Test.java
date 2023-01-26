/** 
 **************************************************************************************
 * @file Facility_Repo_Delete_Test.java
 * @brief 主に[設備管理]のリポジトリクラスで、データの削除を行うメソッドのテストを行う
 * クラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Facility_Repo_Test;

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

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Facility_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Entity.NormalEntity.Facility_Photo;
import com.springproject.dockerspring.Repository.NormalRepo.Facility_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;










/** 
 **************************************************************************************
 * @brief 主に[設備管理]のリポジトリクラスで、データの削除を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Facility_Quad_Repository_TestUtils
 * @see Facility_Repo
 **************************************************************************************
 */ 
@Import({Facility_TestCase_Make.class, Facility_Photo_TestCase_Make.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Facility_Repo_Delete_Test{
  
  public static Facility_TestCase_Make testcase;
  public static Facility_Photo_TestCase_Make foreign_case;
  public static Facility_Repo test_repo;
  public static List<Facility> compare_entity;
  public static List<Facility_Photo> foreign_entity;

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
   * @param[in] test_repo テスト対象のリポジトリ
   * 
   * @see Facility_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Facility_TestCase_Make testcase, 
                            Facility_Photo_TestCase_Make foreign_case, 
                            Facility_Repo test_repo) throws IOException{

    Facility_Repo_Delete_Test.testcase = testcase;
    Facility_Repo_Delete_Test.foreign_case = foreign_case;
    Facility_Repo_Delete_Test.test_repo = test_repo;

    List<Facility> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Facility_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i)));
    }

    Facility_Repo_Delete_Test.compare_entity = compare_entity;

    
    List<Facility_Photo> foreign_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      foreign_entity.add(Facility_Quad_Repository_TestUtils.MockToReal(foreign_case.compareEntityMake(i, false)));
    }

    Facility_Repo_Delete_Test.foreign_entity = foreign_entity;
  }
  
  
  





  
  
  /** 
   **********************************************************************************************
   * @brief データの削除処理に関して、正常に削除が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Facility_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Delete_Success {
  
    private final Facility_Repo test_repo;
    private final List<Facility> compare_entity;

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
      this.test_repo = Facility_Repo_Delete_Test.test_repo;
      this.compare_entity = Facility_Repo_Delete_Test.compare_entity;
      this.softly = Facility_Repo_Delete_Test.softly;

      Facility_Repo_Delete_Test.testcase.databaseSetup();
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
     * @see Facility_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 削除の成功ケース(){

      for(int i = 0; i < 10; i++){
        Facility del_ent = this.compare_entity.get(i);
        this.test_repo.delete(del_ent);

        this.softly.assertThat(this.test_repo.findById(del_ent.getSerial_num()).isEmpty()).isTrue();
      }

      for(int i = 10; i < 20; i++){
        Facility no_delete = this.compare_entity.get(i);

        Facility result = this.test_repo.findById(no_delete.getSerial_num()).get();
        Facility_Quad_Repository_TestUtils.assertAllEquals(no_delete, result, this.softly, true);
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
      Facility_Repo_Delete_Test.testcase.databaseReset();
    }
  }














  /** 
   **********************************************************************************************
   * @brief 削除処理を行う際に、参照制約違反によってエラーが起こるパターンのテストを実施するインナークラス。
   **********************************************************************************************
   */
  @Nested
  public class Delete_Failed {

    private final Facility_Repo test_repo;
    private final List<Facility_Photo> foreign_entity;

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
      this.test_repo = Facility_Repo_Delete_Test.test_repo;
      this.foreign_entity = Facility_Repo_Delete_Test.foreign_entity;
      this.softly = Facility_Repo_Delete_Test.softly;

      Facility_Repo_Delete_Test.testcase.databaseSetup();
      Facility_Repo_Delete_Test.foreign_case.databaseSetup(false);
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
    
      for(Facility_Photo del_ent: this.foreign_entity){
        Facility delete_entity = this.test_repo.findByFaci_Id(del_ent.getFaci_id()).get();

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
      Facility_Repo_Delete_Test.foreign_case.databaseReset(false);
      Facility_Repo_Delete_Test.testcase.databaseReset();
    }
  }
}
