/** 
 **************************************************************************************
 * @file Member_Info_CsvImpl_ExtractCsv_Test.java
 * @brief 主に[団員管理]のCSVデータのデータ抽出機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.Member_Info_CsvImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.MemberInfo.Member_Info_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Member_Info_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.CsvProcess_Test.TestInterface.CsvProcess_ExtractCsv_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Member_Info_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Common_Csv;
import com.springproject.dockerspring.FileIO.CsvProcess.Member_Info_CsvImpl;
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
 * @brief 主に[団員管理]のCSVデータのデータ抽出機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Member_Info_CsvImpl]と[Common_Csv]である。
 * - 主に、共通クラス側のCSVデータ抽出機能と、集約クラス側のCSVデータ抽出機能のテストを行う。
 * 
 * @see Member_Info_Component_TestUtils
 * @see CsvProcess_ExtractCsv_Test
 * @see Member_Info_CsvImpl
 * @see Member_Info_Csv
 * @see Common_Csv
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = Member_Info_TestCase_Make.class)
public class Member_Info_CsvImpl_ExtractCsv_Test implements CsvProcess_ExtractCsv_Test{

  private final Member_Info_TestCase_Make testcase;
  private final Common_Csv common_csv;
  private final Member_Info_Csv member_csv;

  private final SoftAssertions softly = new SoftAssertions();


  //! データ項目の日本語名定義
  private String[] janame_order = {
    "団員番号",
    "名前",
    "ふりがな",
    "性別",
    "誕生日",
    "入団日",
    "退団日",
    "メールアドレス１",
    "メールアドレス２",
    "電話番号１",
    "電話番号２",
    "現住所郵便番号",
    "現住所",
    "役職名",
    "現役職着任日",
    "職種名",
    "配属部署",
    "配属日",
    "担当楽器",
    "その他コメント"
  };

  //! データ項目の英語名定義
  private String[] enname_order = {
    "member_id",
    "name",
    "name_pronu",
    "sex",
    "birthday",
    "join_date",
    "ret_date",
    "email_1",
    "email_2",
    "tel_1",
    "tel_2",
    "addr_postcode",
    "addr",
    "position",
    "position_arri_date",
    "job",
    "assign_dept",
    "assign_date",
    "inst_charge",
    "other_comment"
  };


  private final String target_func = "MemberInfo";
  private final String target_dir = "compcsv";
  private final String prefix = "MemberInfoCsvImplExtractCsvTest";
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
   * @see Member_Info_Csv
   **********************************************************************************************
   */
  @Autowired
  public Member_Info_CsvImpl_ExtractCsv_Test(Member_Info_TestCase_Make testcase){

    Environment_Config config = mock(Environment_Config.class);
    when(config.getMax()).thenReturn(100);
    when(config.getOffset()).thenReturn(30);

    this.common_csv = new Common_Csv(config);
    this.member_csv = new Member_Info_CsvImpl(this.common_csv);
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
   * @see Member_Info_Component_TestUtils
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

    File ok_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
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

      Member_Info_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Member_Info_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
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
   * @see Member_Info_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のCSV入力データ抽出機能確認の抽出成功パターン() throws IOException, InputFileDifferException{

    File ok_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                   this.target_dir, 
                                                                   this.testcase.getCsvOkData(), 
                                                                   this.prefix, 
                                                                   this.extension, 
                                                                   true);

    File tmp_file_path = null;
    try(FileInputStream fis = new FileInputStream(ok_file);
        BufferedInputStream bis = new BufferedInputStream(fis);){

      MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
      tmp_file_path = this.member_csv.extractCsv(mock_file);
    }


    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(tmp_file_path));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(ok_file));){

      Member_Info_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Member_Info_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
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
   * @see Member_Info_Component_TestUtils
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
      File fail_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
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
   * @see Member_Info_Component_TestUtils
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
      File fail_file = Member_Info_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                       this.target_dir, 
                                                                       filename, 
                                                                       this.prefix, 
                                                                       this.extension, 
                                                                       true);

      try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fail_file))){

        MockMultipartFile mock_file = new MockMultipartFile("no_name", bis);
        this.softly.assertThatThrownBy(() -> this.member_csv.extractCsv(mock_file))
                   .isInstanceOf(InputFileDifferException.class);
      }
    }

    this.softly.assertThatThrownBy(() -> this.member_csv.extractCsv(null))
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
    Member_Info_Component_TestUtils.checkTmpDirClear();
  }
}
