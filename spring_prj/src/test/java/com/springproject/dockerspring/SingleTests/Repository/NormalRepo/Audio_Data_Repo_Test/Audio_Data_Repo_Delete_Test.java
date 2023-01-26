/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.NormalRepo
 * 
 * @brief リポジトリの単体テストのうち、[通常用リポジトリ]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、通常用のリポジトリの検索メソッドやデータ更新メソッドの動作確認を行う
 * テストを格納する。
 **************************************************************************************
 */ 

/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Audio_Data_Repo_Test
 * 
 * @brief 通常用リポジトリのうち、[音源データ情報]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、音源データ情報のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.NormalRepo.Audio_Data_Repo_Test;





/** 
 **************************************************************************************
 * @file Audio_Data_Repo_Delete_Test.java
 * @brief 主に[音源データ管理]のリポジトリクラスで、データの削除を行うメソッドのテストを行う
 * クラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。また、現在位置のパッケージの上の階層の
 * パッケージの説明の記載も行う。
 **************************************************************************************
 */ 
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;

import com.springproject.dockerspring.CommonTestCaseMaker.AudioData.Audio_Data_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.SoundSource.Sound_Source_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Sound_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Repository.NormalRepo.Audio_Data_Repo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;










/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]のリポジトリクラスで、データの削除を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Audio_Data_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Sound_Quad_Repository_TestUtils
 * @see Audio_Data_Repo
 **************************************************************************************
 */ 
@Import({Sound_Source_TestCase_Make.class, Audio_Data_TestCase_Make.class})
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Audio_Data_Repo_Delete_Test{

  public static Audio_Data_Repo test_repo;
  public static Audio_Data_TestCase_Make testcase;
  public static Sound_Source_TestCase_Make foreign_case;
  public static List<Audio_Data> compare_entity;

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
   * @see Audio_Data_Repo
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Audio_Data_TestCase_Make testcase, 
                            Sound_Source_TestCase_Make foreign_case, 
                            Audio_Data_Repo test_repo) throws IOException{

    Audio_Data_Repo_Delete_Test.testcase = testcase;
    Audio_Data_Repo_Delete_Test.foreign_case = foreign_case;
    Audio_Data_Repo_Delete_Test.test_repo = test_repo;

    List<Audio_Data> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Sound_Quad_Repository_TestUtils.MockToReal(testcase.compareEntityMake(i, false)));
    }

    Audio_Data_Repo_Delete_Test.compare_entity = compare_entity;
  }
  
  
  





  
  
  /** 
   **********************************************************************************************
   * @brief データの削除処理に関して、正常に削除が成功するパターンのテストを実施するインナークラス。
   * 
   * @see Sound_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Delete_Success {
  
    private final Audio_Data_Repo test_repo;
    private final List<Audio_Data> compare_entity;

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
      this.test_repo = Audio_Data_Repo_Delete_Test.test_repo;
      this.compare_entity = Audio_Data_Repo_Delete_Test.compare_entity;
      this.softly = Audio_Data_Repo_Delete_Test.softly;

      Audio_Data_Repo_Delete_Test.foreign_case.databaseSetup();
      Audio_Data_Repo_Delete_Test.testcase.databaseSetup(false);
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
     * @see Sound_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 削除の成功ケース(){

      for(int i = 0; i < 10; i++){
        Audio_Data del_ent = this.compare_entity.get(i);
        this.test_repo.delete(del_ent);

        this.softly.assertThat(this.test_repo.findById(del_ent.getSerial_num()).isEmpty()).isTrue();
      }

      for(int i = 10; i < 20; i++){
        Audio_Data no_delete = this.compare_entity.get(i);

        Audio_Data result = this.test_repo.findById(no_delete.getSerial_num()).get();
        Sound_Quad_Repository_TestUtils.assertAllEquals(no_delete, result, this.softly, true);
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
      Audio_Data_Repo_Delete_Test.testcase.databaseReset(false);
      Audio_Data_Repo_Delete_Test.foreign_case.databaseReset();
    }
  }
}
