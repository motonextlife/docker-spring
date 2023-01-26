/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Musical_Score_CsvImpl_Test
 * 
 * @brief CSVファイルの入出力単体テストのうち、[楽譜情報]に関するテストを行うクラスを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージは、楽譜情報に関するCSVファイルの処理機能をテストするクラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Musical_Score_CsvImpl_Test;





/** 
 **************************************************************************************
 * @file Musical_Score_CsvImpl_CsvToDatabase_Test.java
 * @brief 主に[楽譜管理]のCSVデータのデータベースへの格納機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Score_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_CsvToDatabase_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Musical_Score_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Musical_Score_CsvImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;







/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]のCSVデータのデータベースへの格納機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Musical_Score_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVデータベース格納機能と、集約クラス側のCSVデータベース格納機能のて
 * テストを行う。
 * 
 * @see Score_Double_Component_TestUtils
 * @see CsvProcess_CsvToDatabase_Test
 * @see Musical_Score_Csv
 * @see Musical_Score_CsvImpl
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Musical_Score_TestCase_Make.class)
public class Musical_Score_CsvImpl_CsvToDatabase_Test implements CsvProcess_CsvToDatabase_Test{

  private final Musical_Score_TestCase_Make testcase;
  private final Common_Csv common_csv;
  private final Musical_Score_Csv score_csv;

  private final SoftAssertions softly = new SoftAssertions();


  //! CSV抽出の際のヘッダー定義
  private Builder builder = CSVFormat.DEFAULT
                                     .builder()
                                     .setHeader("楽譜番号",
                                                "購入日",
                                                "曲名",
                                                "作曲者",
                                                "編曲者",
                                                "出版社",
                                                "保管場所",
                                                "廃棄日",
                                                "その他コメント");
                                                
  //! データ項目の日本語名定義
  private String[] janame_order = {
    "楽譜番号",
    "購入日",
    "曲名",
    "作曲者",
    "編曲者",
    "出版社",
    "保管場所",
    "廃棄日",
    "その他コメント"
  };

  //! データ項目の英語名定義
  private String[] enname_order = {
    "score_id",
    "buy_date",
    "song_title",
    "composer",
    "arranger",
    "publisher",
    "storage_loc",
    "disp_date",
    "other_comment"
  };


  private final String target_func = "MusicalScore";
  private final String target_dir = "compcsv";
  private final String prefix = "MusicalScoreCsvImplCsvToDatabaseTest";
  private final String extension = "csv";











  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[オフセット値]は[8]として設定したうえでテストを行う。
   * - テスト対象クラスに関してはDIでインジェクションせず、手動でインスタンス化する際にモックの環境変数を
   * 渡すことで、モックの環境変数が有効なテスト対象クラスを作成する。
   *
   * @param[in] testcase テストケースクラス
   * 
   * @see Common_Csv
   * @see Musical_Score_CsvImpl
   **********************************************************************************************
   */
  @Autowired
  public Musical_Score_CsvImpl_CsvToDatabase_Test(Musical_Score_TestCase_Make testcase){
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getOffset()).thenReturn(8);

    this.common_csv = new Common_Csv(config);
    this.score_csv = new Musical_Score_CsvImpl(this.common_csv);
    this.testcase = testcase;
  }












  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVファイルデータベース保存用の共通メソッドの検査を行う。
   * 
   * @details
   * - 検査対象には、モックとなる関数型インターフェースを作成して、それを渡し実行する。
   * - テストケースとなるCSVファイルを渡して処理をした際に、データベース格納関数となる関数型インターフェースに
   * 渡されたエンティティのデータが、比較用のエンティティと同一であることで、正常にデータが渡ったことを判別。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして検査対象クラスに投入するCSVファイルと、比較用エンティティのリストをテストケース
   * クラスから取得する。
   * -# モックとなる関数型インターフェースとして、[マップデータのエンティティ格納]の関数を準備する。
   * 内容としては、渡されたマップリストのデータを、モックエンティティに格納してリストとして返すものである。
   * -# 次にモックの関数型インターフェースとして、[エンティティのデータベース格納]の関数を用意する。
   * 格納対象となるエンティティを受け取り、その関数の中で、渡されたエンティティの中身が比較用のエンティティと
   * 同一であることを確認する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# テスト対象クラスの処理を実行し、アサーションを実行する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSV抽出データのデータベース格納機能の動作確認() throws IOException{
    String compare_file = this.testcase.getOutputCsv(false);

    List<Musical_Score> comp_ent = new ArrayList<>(
                                IntStream.rangeClosed(1, 20)
                                         .mapToObj(i -> this.testcase.compareEntityMake(i))
                                         .toList()
                              );


    //エンティティ格納関数
    Function<List<Map<String, String>>, List<Musical_Score>> goto_ent = s -> {
      return s.stream().map(map -> {
                         try {
                           return Score_Double_Component_TestUtils.makeMockEntity(map);
                         } catch (ParseException e) {
                           throw new IllegalStateException();
                         }
                       })
                       .toList();
    };


    //データベース格納関数
    Consumer<List<Musical_Score>> goto_database = s -> {
      for(int i = 0; i < s.size(); i++){
        Score_Double_Component_TestUtils.assertAllEquals(comp_ent.get(i), s.get(i), this.softly);
      }

      comp_ent.subList(0, s.size()).clear();
    };


    //列挙型マップリスト作成
    LinkedHashMap<String, String> enum_map = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enum_map.put(janame_order[i], enname_order[i]);
    }


    //テストケースCSVファイルのコピー作成
    File temp_file = Score_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                      this.target_dir, 
                                                                      compare_file, 
                                                                      this.prefix, 
                                                                      this.extension, 
                                                                      false);

    this.common_csv.csvToDatabaseCom(temp_file, enum_map, this.builder, goto_ent, goto_database);
    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVファイルデータベース保存用のメソッドの検査を行う。
   * 
   * @details
   * - 検査対象には、モックとなる関数型インターフェースを作成して、それを渡し実行する。
   * - テストケースとなるCSVファイルを渡して処理をした際に、データベース格納関数となる関数型インターフェースに
   * 渡されたエンティティのデータが、比較用のエンティティと同一であることで、正常にデータが渡ったことを判別。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして検査対象クラスに投入するCSVファイルと、比較用エンティティのリストをテストケース
   * クラスから取得する。
   * -# モックの関数型インターフェースとして、[エンティティのデータベース格納]の関数を用意する。
   * 格納対象となるエンティティを受け取り、その関数の中で、渡されたエンティティの中身が比較用のエンティティと
   * 同一であることを確認する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# テスト対象クラスの処理を実行し、アサーションを実行する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSV抽出データのデータベース格納機能の動作確認() throws IOException, ParseException{
    String compare_file = testcase.getOutputCsv(false);

    List<Musical_Score> comp_ent = new ArrayList<>(
                                IntStream.rangeClosed(1, 20)
                                         .mapToObj(i -> this.testcase.compareEntityMake(i))
                                         .toList()
                              );


    //データベース格納関数
    Consumer<List<Musical_Score>> goto_database = s -> {
      for(int i = 0; i < s.size(); i++){
        Score_Double_Component_TestUtils.assertAllEquals(comp_ent.get(i), s.get(i), this.softly);
      }

      comp_ent.subList(0, s.size()).clear();
    };


    //テストケースCSVファイルのコピー作成
    File temp_file = Score_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                      this.target_dir, 
                                                                      compare_file, 
                                                                      this.prefix, 
                                                                      this.extension, 
                                                                      false);

    this.score_csv.csvToDatabase(temp_file, goto_database);
    this.softly.assertAll();
  }










  /** 
   **********************************************************************************************
   * @brief テスト終了後に、テストケースとして利用したデータを、データベースから削除する。
   * 
   * @throw IOException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @AfterAll
  public static void testcaseReset() throws IOException, InterruptedException{
    Score_Double_Component_TestUtils.checkTmpDirClear();
  }
}
