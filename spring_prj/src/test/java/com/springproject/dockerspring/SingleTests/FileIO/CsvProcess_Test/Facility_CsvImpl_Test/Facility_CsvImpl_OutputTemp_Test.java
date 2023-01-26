/** 
 **************************************************************************************
 * @file Facility_CsvImpl_OutputTemp_Test.java
 * @brief 主に[設備管理]のCSVテンプレート出力機能のテストを行うクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Facility_CsvImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_OutputTemp_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Facility_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Facility_CsvImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;










/** 
 **************************************************************************************
 * @brief 主に[設備管理]のCSVテンプレート出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVテンプレート出力機能と、集約クラス側のCSVテンプレート出力機能のテスト
 * を行う。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see CsvProcess_OutputTemp_Test
 * @see Facility_CsvImpl
 * @see Facility_Csv
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Facility_TestCase_Make.class)
public class Facility_CsvImpl_OutputTemp_Test implements CsvProcess_OutputTemp_Test{

  private final Facility_TestCase_Make testcase;
  private final Common_Csv common_csv;
  private final Facility_Csv faci_csv;

  private final SoftAssertions softly = new SoftAssertions();


  //! データ項目の日本語名定義
  private String[] janame_order = {
    "設備番号",
    "設備名",
    "購入日",
    "製作者",
    "保管場所",
    "廃棄日",
    "その他コメント"
  };



  private final String target_func = "Facility";
  private final String target_dir = "compcsv";
  private final String prefix = "FacilityCsvImplOutputCsvTest";
  private final String extension = "csv";










  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースと検査対象クラスをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[検索結果限界数]は[100]として設定したうえでテストを行う。
   * - テスト対象クラスに関してはDIでインジェクションせず、手動でインスタンス化する際にモックの環境変数を
   * 渡すことで、モックの環境変数が有効なテスト対象クラスを作成する。
   *
   * @param[in] testcase テストケースクラス
   * 
   * @see Common_Csv
   * @see Facility_Csv
   **********************************************************************************************
   */
  @Autowired
  public Facility_CsvImpl_OutputTemp_Test(Facility_TestCase_Make testcase){
                                            
    Environment_Config config = mock(Environment_Config.class);
    when(config.getMax()).thenReturn(100);

    this.common_csv = new Common_Csv(config);
    this.faci_csv = new Facility_CsvImpl(this.common_csv);
    this.testcase = testcase;
  }











  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVテンプレート出力用の共通メソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# 比較対象となるテストケースのCSVテンプレートファイルを作成する。
   * -# テスト対象クラスを実行し、テンプレートファイルを作成する。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSVテンプレート出力機能の動作確認() throws IOException{

    File compare_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                            this.target_dir, 
                                                                            this.testcase.getOutputCsv(true), 
                                                                            this.prefix, 
                                                                            this.extension, 
                                                                            true);
                                                                            
    File tmp_file_path = this.common_csv.outputTemplateCom(Arrays.asList(janame_order));

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(compare_file));){

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }

    this.softly.assertAll();
  }










  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVテンプレート出力用のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# 比較対象となるテストケースのCSVテンプレートファイルを作成する。
   * -# テスト対象クラスを実行し、テンプレートファイルを作成する。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSVテンプレート出力機能の動作確認() throws IOException{

    File compare_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                            this.target_dir, 
                                                                            this.testcase.getOutputCsv(true), 
                                                                            this.prefix, 
                                                                            this.extension, 
                                                                            true);
                                                                            
    File tmp_file_path = this.faci_csv.outputTemplate();

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(compare_file));){

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }

    this.softly.assertAll();
  }









  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリが空っぽであることを確認する。
   * 
   * @throw InterruptedException
   **********************************************************************************************
   */
  @AfterAll
  public static void 一時ファイルディレクトリの初期化確認() throws InterruptedException{
    Facility_Double_Component_TestUtils.checkTmpDirClear();
  }
}
