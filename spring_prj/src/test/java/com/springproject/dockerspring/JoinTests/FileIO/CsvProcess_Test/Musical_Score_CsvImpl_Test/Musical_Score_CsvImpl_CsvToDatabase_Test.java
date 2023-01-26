/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.Musical_Score_CsvImpl_Test
 * 
 * @brief CSVファイルの入出力結合テストのうち、[楽譜情報]に関するテストを行うクラスを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージは、楽譜情報に関するCSVファイルの処理機能をテストするクラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.Musical_Score_CsvImpl_Test;





/** 
 **************************************************************************************
 * @file Musical_Score_CsvImpl_CsvToDatabase_Test.java
 * @brief 主に[楽譜管理]のCSVデータのデータベースへの格納機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils.Score_Double_Component_TestUtils;
import com.springproject.dockerspring.JoinTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_CsvToDatabase_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Musical_Score_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Musical_Score_CsvImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Repository.NormalRepo.Musical_Score_Repo;

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

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;







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
 * @see Musical_Score_Repo
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@Import(Musical_Score_TestCase_Make.class)
@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class Musical_Score_CsvImpl_CsvToDatabase_Test implements CsvProcess_CsvToDatabase_Test{

  private final Musical_Score_TestCase_Make testcase;
  private static Musical_Score_TestCase_Make testcase_static;
  private final Common_Csv common_csv;
  private final Musical_Score_Csv score_csv;
  private final Musical_Score_Repo score_repo;

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
   * @see Musical_Score_Repo
   **********************************************************************************************
   */
  @Autowired
  public Musical_Score_CsvImpl_CsvToDatabase_Test(Musical_Score_TestCase_Make testcase, 
                                                  Musical_Score_Repo score_repo) throws IOException{
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getOffset()).thenReturn(8);

    this.common_csv = new Common_Csv(config);
    this.score_csv = new Musical_Score_CsvImpl(this.common_csv);
    this.testcase = testcase;
    this.score_repo = score_repo;

    this.testcase.databaseReset();
  }











  /** 
   **********************************************************************************************
   * @brief DIした環境変数を、静的変数でも使えるようにする。
   *
   * @details
   * - 設けた理由としては、テスト後のデータベースのリセット確認の際に環境変数を用いる必要があるが、AfterAll
   * は静的メソッドでしか実装できない為、通常のフィールド変数にDIされている環境変数が使えない。
   * - そのため、新たに静的フィールド変数を作成して、その中にDIした環境変数の参照を入れておくことで、テスト後の
   * データベースリセット確認にも環境変数を使用できるようにしている。
   * 
   * @par 使用アノテーション
   * - @PostConstruct
   * 
   * @note このメソッドは、セットアップのための非公開メソッドであり、外部に出さない。
   **********************************************************************************************
   */
  @PostConstruct
  private void configStaticSet(){
    Musical_Score_CsvImpl_CsvToDatabase_Test.testcase_static = this.testcase;
  }











  /** 
   **********************************************************************************************
   * @brief 保存終了後にデータベースから取得してきたデータと、比較用のエンティティリストを比較して、想定
   * 通りのデータが格納されていることを確認する。
   * 
   * @param[in] compare 比較用のエンティティリスト
   * @param[in] result データベースから取得したエンティティリスト
   * 
   * @par 大まかな処理の流れ
   * -# データベースから格納したデータを取得し、比較用のエンティティの数量と、取得してきたデータの終了が
   * 同一であることを確認する。
   * -# 比較用エンティティとテスト対象エンティティを一つづつ比較していく際に、管理番号が同一であれば、
   * 比較アサーションを実行し、同一であることを確認する。
   * -# 一つのエンティティの確認が終わったら、テスト対象エンティティをリストから一つづつ削除していく。
   * -# 比較終了後に、テスト対象エンティティが一つも残っておらず全てチェックしたことを確認する。
   * 
   * @see Score_Double_Component_TestUtils
   **********************************************************************************************
   */
  private void checkIdentity(List<Musical_Score> compare){

    List<Musical_Score> result = (List<Musical_Score>)this.score_repo.findAll();
    this.softly.assertThat(result.size()).isEqualTo(compare.size());

    for(Musical_Score comp_ent: compare){
      for(Musical_Score result_ent: result){
        
        if(comp_ent.getScore_id().equals(result_ent.getScore_id())){
          Score_Double_Component_TestUtils.assertAllEquals(comp_ent, result_ent, this.softly);
          result.remove(result_ent);
          break;
        }
      }
    }

    this.softly.assertThat(result).isEmpty();
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
   * 内容としては、渡されたマップリストのデータを、エンティティに格納してリストとして返すものである。
   * -# 次にモックの関数型インターフェースとして、[エンティティのデータベース格納]の関数を用意する。
   * 格納対象となるエンティティを受け取り、その関数の中で、対象のデータベースの中にデータを格納する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# 格納対象のデータベースからデータを取り出し、比較用エンティティと比較することで想定通りのデータが
   * 格納されていることを確認する。なお、新規追加扱いの事から、データの順番が保証されていない為、
   * 「データベース内部に同じデータが存在すれば良い」物とし、順番は問わない。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSV抽出データのデータベース格納機能の動作確認() throws IOException{

    List<Musical_Score> comp_ent = new ArrayList<>(
                                IntStream.rangeClosed(1, 20)
                                         .mapToObj(i -> this.testcase.compareEntityMake(i))
                                         .toList()
                              );


    //エンティティ格納関数
    Function<List<Map<String, String>>, List<Musical_Score>> goto_ent = s -> {
      return s.stream().map(map -> {
                         try {
                           return new Musical_Score(map);
                         } catch (ParseException e) {
                           throw new IllegalStateException();
                         }
                       })
                       .toList();
    };


    //データベース格納関数
    Consumer<List<Musical_Score>> goto_database = s -> {
      this.score_repo.saveAll(s);
    };


    //列挙型マップリスト作成
    LinkedHashMap<String, String> enum_map = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enum_map.put(janame_order[i], enname_order[i]);
    }


    //テストケースCSVファイルのコピー作成
    File temp_file = Score_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                      this.target_dir, 
                                                                      this.testcase.getOutputCsv(false), 
                                                                      this.prefix, 
                                                                      this.extension, 
                                                                      false);

    this.common_csv.csvToDatabaseCom(temp_file, enum_map, this.builder, goto_ent, goto_database);

    checkIdentity(comp_ent);
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
   * 格納対象となるエンティティを受け取り、その関数の中で、対象のデータベースの中にデータを格納する。
   * -# テスト対象クラスに渡す、列挙型のマップリストと、投入するCSVファイルをコピーした物を作成し、
   * これまで作った関数型インターフェースなどと一緒に全て検査対象クラスに渡す。
   * -# 格納対象のデータベースからデータを取り出し、比較用エンティティと比較することで想定通りのデータが
   * 格納されていることを確認する。なお、新規追加扱いの事から、データの順番が保証されていない為、
   * 「データベース内部に同じデータが存在すれば良い」物とし、順番は問わない。
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

    List<Musical_Score> comp_ent = new ArrayList<>(
                                IntStream.rangeClosed(1, 20)
                                         .mapToObj(i -> this.testcase.compareEntityMake(i))
                                         .toList()
                              );


    //データベース格納関数
    Consumer<List<Musical_Score>> goto_database = s -> {
      this.score_repo.saveAll(s);
    };



    //テストケースCSVファイルのコピー作成
    File temp_file = Score_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                      this.target_dir, 
                                                                      this.testcase.getOutputCsv(false), 
                                                                      this.prefix, 
                                                                      this.extension, 
                                                                      false);

    this.score_csv.csvToDatabase(temp_file, goto_database);


    checkIdentity(comp_ent);
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
    Musical_Score_CsvImpl_CsvToDatabase_Test.testcase_static.databaseReset();
  }
}
