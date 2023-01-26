/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.Repository.HistoryRepo.Musical_Score_History_Repo_Test
 * 
 * @brief 履歴用リポジトリのうち、[楽譜情報変更履歴]に関するテストを格納するパッケージ
 * 
 * @details
 * - このパッケージは、楽譜情報変更履歴のテストを行うクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.HistoryRepo.Musical_Score_History_Repo_Test;






/** 
 **************************************************************************************
 * @file Musical_Score_History_Repo_Delete_Test.java
 * @brief 主に[楽譜情報変更履歴]のリポジトリクラスで、データの削除を行うメソッドのテストを行う
 * クラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
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

import com.springproject.dockerspring.CommonTestCaseMaker.History.History_TestCase_Make.History_TestKeys;
import com.springproject.dockerspring.CommonTestCaseMaker.History.RepoForm.History_RepoForm_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Common_Repository_TestUtils;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.CompareParam;
import com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils.Score_Quad_Repository_TestUtils;
import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Repository.HistoryRepo.Musical_Score_History_Repo;

import static java.util.Comparator.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;











/** 
 **************************************************************************************
 * @brief 主に[楽譜情報変更履歴]のリポジトリクラスで、データの削除を行うメソッドのテストを
 * 行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Musical_Score_History_Repo]である。
 * - このテストに使用するデータベースは、本番環境と同一の種類の物を使用する。
 * - このテストクラス内でインナークラスとしてテストを分割し、テスト毎にデータベースのリセットを
 * 行う。
 * 
 * @see Score_Quad_Repository_TestUtils
 * @see Musical_Score_History_Repo
 **************************************************************************************
 */ 
@Import(Musical_Score_TestCase_Make.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Musical_Score_History_Repo_Delete_Test{

  public static Musical_Score_TestCase_Make testcase;
  public static Musical_Score_History_Repo test_repo;
  public static List<Musical_Score_History> compare_entity;

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
   * @see Musical_Score_History_Repo
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Autowired
  public void testcaseSetup(Musical_Score_TestCase_Make testcase, 
                            Musical_Score_History_Repo test_repo){

    Musical_Score_History_Repo_Delete_Test.testcase = testcase;
    Musical_Score_History_Repo_Delete_Test.test_repo = test_repo;

    List<Musical_Score_History> compare_entity = new ArrayList<>();
    for(int i = 1; i <= 20; i++){
      compare_entity.add(Score_Quad_Repository_TestUtils.MockToReal(testcase.compareHistoryEntityMake(i)));
    }

    Musical_Score_History_Repo_Delete_Test.compare_entity = compare_entity;
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
  
    private final Musical_Score_History_Repo test_repo;
    private final List<Musical_Score_History> compare_entity;

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
      this.test_repo = Musical_Score_History_Repo_Delete_Test.test_repo;
      this.compare_entity = Musical_Score_History_Repo_Delete_Test.compare_entity;
      this.softly = Musical_Score_History_Repo_Delete_Test.softly;

      Musical_Score_History_Repo_Delete_Test.testcase.historyDatabaseSetup();
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
        Musical_Score_History del_ent = this.compare_entity.get(i);
        this.test_repo.delete(del_ent);

        this.softly.assertThat(this.test_repo.findById(del_ent.getSerial_num()).isEmpty()).isTrue();
      }

      for(int i = 10; i < 20; i++){
        Musical_Score_History no_delete = this.compare_entity.get(i);

        Musical_Score_History result = this.test_repo.findById(no_delete.getSerial_num()).get();
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
      Musical_Score_History_Repo_Delete_Test.testcase.historyDatabaseReset();
    }
  }












  /** 
   **********************************************************************************************
   * @brief 期間を指定した履歴情報の削除において、正常に該当期間の削除が完了するパターンのテストを実施する
   * インナークラス。
   * 
   * @see Score_Quad_Repository_TestUtils
   **********************************************************************************************
   */
  @Nested
  public class Period_Delete_Success {

    private final Musical_Score_History_Repo test_repo;
    private final List<Musical_Score_History> compare_entity;

    private final SoftAssertions softly;

    private final SimpleDateFormat parse_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    //** @name 検索時のテストケースの配列のインデックス */
    //** @{ */

    //! 検索開始日時
    public final int START_DATETIME = History_RepoForm_TestCase.START_DATETIME;

    //! 検索終了日時
    public final int END_DATETIME = History_RepoForm_TestCase.END_DATETIME;

    //** @} */




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
    public Period_Delete_Success() throws IOException{
      this.test_repo = Musical_Score_History_Repo_Delete_Test.test_repo;
      this.compare_entity = Musical_Score_History_Repo_Delete_Test.compare_entity;
      this.softly = Musical_Score_History_Repo_Delete_Test.softly;

      Musical_Score_History_Repo_Delete_Test.testcase.historyDatabaseSetup();
    }
      


  
    /** 
     **********************************************************************************************
     * @brief 履歴データの期間削除時の動作の確認を行う。なお、これは成功ケースである。
     *
     * @details
     * - テストの内容としては、指定のデータを削除した後、再度そのデータを検索しデータが存在しないことを確認する。
     * - また、まだ削除していないデータに関しては、前と変わらない状態であることを確認する。
     * 
     * @par 大まかな処理の流れ
     * -# 削除を行う前に、想定される削除後に残ったデータのリストを、比較用に作成する。
     * -# 指定期間のデータを削除した後、テーブルを全検索し、残ったデータをすべて取得する。
     * -# 残ったデータが、想定されるデータ数かつ、内容が比較用のリストと同一であり、想定された削除になっている
     * 事を確認する。
     * 
     * @see Score_Quad_Repository_TestUtils
     **********************************************************************************************
     */
    @Test
    public void 該当期間データの削除の成功ケース() throws ParseException, InterruptedException{
  
      String[] assume = Musical_Score_History_Repo_Delete_Test.testcase
                                                              .historyOtherRepoForm(History_TestKeys.Change_Datetime, 1);
      
      Date start = this.parse_datetime.parse(assume[START_DATETIME]);
      Date end = this.parse_datetime.parse(assume[END_DATETIME]);
      
      List<Musical_Score_History> compare = Common_Repository_TestUtils
        .compareListMake(CompareParam
                         .<Musical_Score_History>builder()
                         .origin_compare(this.compare_entity)
                         .filter(s -> !((s.getChange_datetime().equals(start) || s.getChange_datetime().after(start)) 
                                     && (s.getChange_datetime().equals(end) || s.getChange_datetime().before(end))))
                         .sort_first(Comparator.comparing(Musical_Score_History::getHistory_id))
                         .skip(0)
                         .limit(Integer.MAX_VALUE)
                         .build());

      this.test_repo.deleteByDateBetween(start, end);

      List<Musical_Score_History> get_data = (List<Musical_Score_History>) this.test_repo.findAll();
      List<Musical_Score_History> result = get_data.stream()
                                                   .sorted(comparing(Musical_Score_History::getHistory_id))
                                                   .collect(Collectors.toList());

      this.softly.assertThat(result.size()).isEqualTo(compare.size());

      int compare_size = compare.size();
      for(int i = 0; i < compare_size; i++){
        Score_Quad_Repository_TestUtils.assertAllEquals(compare.get(i), result.get(i), this.softly, true);
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
      Musical_Score_History_Repo_Delete_Test.testcase.historyDatabaseReset();
    }
  }
}
