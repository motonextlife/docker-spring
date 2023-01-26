/** 
 **************************************************************************************
 * @file Facility_CsvImpl_ExtractCsv_Test.java
 * @brief 主に[設備管理]のCSVデータのデータ抽出機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Facility_CsvImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_ExtractCsv_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Facility_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Facility_CsvImpl;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;









/** 
 **************************************************************************************
 * @brief 主に[設備管理]のCSVデータのデータ抽出機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVデータ抽出機能と、集約クラス側のCSVデータ抽出機能のテストを行う。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see CsvProcess_ExtractCsv_Test
 * @see Facility_CsvImpl
 * @see Facility_Csv
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Facility_TestCase_Make.class)
public class Facility_CsvImpl_ExtractCsv_Test implements CsvProcess_ExtractCsv_Test{

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

  //! データ項目の英語名定義
  private String[] enname_order = {
    "faci_id",
    "faci_name",
    "buy_date",
    "producer",
    "storage_loc",
    "disp_date",
    "other_comment"
  };


  private final String target_func = "Facility";
  private final String target_dir = "compcsv";
  private final String prefix = "FacilityCsvImplExtractCsvTest";
  private final String extension = "csv";





  




  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースと検査対象クラスをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[検索結果限界数]は[100]として設定したうえでテストを行う。
   * - モックの環境変数において、[オフセット値]は[30]として設定したうえでテストを行う。
   * なおここでのオフセット値は、バッファのカウント数として用いる。
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
  public Facility_CsvImpl_ExtractCsv_Test(Facility_TestCase_Make testcase){

    Environment_Config config = mock(Environment_Config.class);
    when(config.getMax()).thenReturn(100);
    when(config.getOffset()).thenReturn(30);

    this.common_csv = new Common_Csv(config);
    this.faci_csv = new Facility_CsvImpl(this.common_csv);
    this.testcase = testcase;
  }







  



  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVファイルデータ抽出用の共通メソッドの検査を行う。このテストは、抽出が
   * 成功するパターンである。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された抽出済みの一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するCSVファイルをテストケースクラスから取得する。
   * -# 検査対象クラスに投入する列挙型マップリストを作成。
   * -# テストケースのファイルを利用してモックのマルチパートファイルを作成し、検査対象のクラスに投入後、
   * 戻り値のファイルを取得する。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSV入力データ抽出機能確認の抽出成功パターン() throws IOException, InputFileDifferException{

    LinkedHashMap<String, String> enum_map = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enum_map.put(janame_order[i], enname_order[i]);
    }

    File ok_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                       this.target_dir, 
                                                                       this.testcase.getCsvOkData(), 
                                                                       this.prefix, 
                                                                       this.extension, 
                                                                       true);

    File tmp_file_path = null;
    try(FileInputStream fis = new FileInputStream(ok_file);
        BufferedInputStream bis = new BufferedInputStream(fis);){

      MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
      tmp_file_path = this.common_csv.extractCsvCom(mock_file, enum_map);
    }


    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(ok_file));){

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }

    this.softly.assertAll();
  }









  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVファイルデータ抽出用のメソッドの検査を行う。このテストは、抽出が
   * 成功するパターンである。
   * 
   * @details
   * - 検査方法としては、比較用のCSVファイルと、テスト対象クラスから生成された抽出済みの一時ファイルを
   * ハッシュ化して比較し、データが同一であることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するCSVファイルをテストケースクラスから取得する。
   * -# テストケースのファイルを利用してモックのマルチパートファイルを作成し、検査対象のクラスに投入後、
   * 戻り値のファイルを取得する。
   * -# 戻り値の一時ファイルと、比較用のファイルをハッシュ化して同一性を検査するアサーションを実行。
   * -# エラーの有無にかかわらず、テストが終了したら一時ファイルを全削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSV入力データ抽出機能確認の抽出成功パターン() throws IOException, InputFileDifferException{

    File ok_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                       this.target_dir, 
                                                                       this.testcase.getCsvOkData(), 
                                                                       this.prefix, 
                                                                       this.extension, 
                                                                       true);

    File tmp_file_path = null;
    try(FileInputStream fis = new FileInputStream(ok_file);
        BufferedInputStream bis = new BufferedInputStream(fis);){

      MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
      tmp_file_path = this.faci_csv.extractCsv(mock_file);
    }


    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(ok_file));){

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }

    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 共通クラス側のCSVファイルデータ抽出用の共通メソッドの検査を行う。このテストは、抽出が
   * 失敗するパターンである。
   * 
   * @details
   * - 検査方法としては、抽出時にエラーとなるファイルをテスト対象クラスに渡した際に、エラーとなって
   * 処理が止まることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するCSVファイルをテストケースクラスから取得する。
   * -# 検査対象クラスに投入する列挙型マップリストを作成。
   * -# テストケースのファイルを利用してモックのマルチパートファイルを作成し、検査対象のクラスに投入。
   * -# ファイル不適合を示す例外だ発生し、処理が進まないことを確認する。
   * -# ファイルをあえて指定せずNullの状態にして投入し、例外によって処理が進まない事を確認する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のCSV入力データ抽出機能確認の抽出失敗パターン() throws IOException, InputFileDifferException{

    LinkedHashMap<String, String> enum_map = new LinkedHashMap<>();
    for(int i = 0; i < janame_order.length; i++){
      enum_map.put(janame_order[i], enname_order[i]);
    }
    

    List<String> fail_list = testcase.getCsvFailedData(false);

    for(String filename: fail_list){
      File fail_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                           this.target_dir, 
                                                                           filename, 
                                                                           this.prefix, 
                                                                           this.extension, 
                                                                           true);

      try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fail_file))){

        MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
        this.softly.assertThatThrownBy(() -> this.common_csv.extractCsvCom(mock_file, enum_map))
                   .isInstanceOf(InputFileDifferException.class);
      }
    }

    this.softly.assertThatThrownBy(() -> this.common_csv.extractCsvCom(null, enum_map))
               .isInstanceOf(NullPointerException.class);

    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 集約クラス側のCSVファイルデータ抽出用のメソッドの検査を行う。このテストは、抽出が
   * 失敗するパターンである。
   * 
   * @details
   * - 検査方法としては、抽出時にエラーとなるファイルをテスト対象クラスに渡した際に、エラーとなって
   * 処理が止まることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するCSVファイルをテストケースクラスから取得する。
   * -# テストケースのファイルを利用してモックのマルチパートファイルを作成し、検査対象のクラスに投入。
   * -# ファイル不適合を示す例外だ発生し、処理が進まないことを確認する。
   * -# ファイルをあえて指定せずNullの状態にして投入し、例外によって処理が進まない事を確認する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSV入力データ抽出機能確認の抽出失敗パターン() throws IOException, InputFileDifferException{

    List<String> fail_list = testcase.getCsvFailedData(false);

    for(String filename: fail_list){
      File fail_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                           this.target_dir, 
                                                                           filename, 
                                                                           this.prefix, 
                                                                           this.extension, 
                                                                           true);

      try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fail_file))){

        MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
        this.softly.assertThatThrownBy(() -> this.faci_csv.extractCsv(mock_file))
                   .isInstanceOf(InputFileDifferException.class);
      }
    }

    this.softly.assertThatThrownBy(() -> this.faci_csv.extractCsv(null))
               .isInstanceOf(NullPointerException.class);

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
