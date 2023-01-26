/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.Facility_Photo_ZipImpl_Test
 * 
 * @brief ZIPファイルの入出力結合テストのうち、[設備写真データ管理]に関するテストを行うクラスを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージは、設備写真データ管理に関するZIPファイルの処理機能をテストするクラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.Facility_Photo_ZipImpl_Test;





/** 
 **************************************************************************************
 * @file Facility_Photo_ZipImpl_ExtractZip_Test.java
 * @brief 主に[設備写真データ管理]の圧縮ファイルの解凍抽出機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make.Facility_Photo_TestKeys;
import com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.TestInterface.ZipProcess_ExtractZip_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Facility_Photo_Zip;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.FileIO.ZipProcess.Common_Zip;
import com.springproject.dockerspring.FileIO.ZipProcess.Facility_Photo_ZipImpl;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;










/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]の圧縮ファイルの解凍抽出機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_Photo_ZipImpl]である。
 * - 主に、集約クラス側の圧縮ファイルの解凍抽出機能をテストする。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see ZipProcess_ExtractZip_Test
 * @see Facility_Photo_ZipImpl
 * @see Facility_Photo_Zip
 * @see Common_Zip
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Facility_Photo_TestCase_Make.class, Common_Zip.class})
public class Facility_Photo_ZipImpl_ExtractZip_Test implements ZipProcess_ExtractZip_Test{

  private final Facility_Photo_TestCase_Make testcase;
  private final Facility_Photo_Zip faci_zip;

  private final SoftAssertions softly = new SoftAssertions();

  private final String target_func = "FacilityPhoto";
  private final String prefix = "FacilityPhotoZipImplExtractZipTest";









  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースと検査対象クラスをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - モックの環境変数において、[画像データ受付可能数]は[5]として設定したうえでテストを行う。
   * - モックの環境変数において、[画像データ量制限値]は[512000]として設定したうえでテストを行う。
   * - テスト対象クラスに関してはDIでインジェクションせず、手動でインスタンス化する際にモックの環境変数を
   * 渡すことで、モックの環境変数が有効なテスト対象クラスを作成する。
   *
   * @param[in] testcase テストケースクラス
   * @param[in] common_zip 共通クラス側
   * 
   * @see Common_Zip
   * @see Facility_Photo_ZipImpl
   **********************************************************************************************
   */
  @Autowired
  public Facility_Photo_ZipImpl_ExtractZip_Test(Facility_Photo_TestCase_Make testcase, Common_Zip common_zip){

    Environment_Config config = mock(Environment_Config.class);
    when(config.getPhoto_list_size()).thenReturn(5);
    when(config.getPhoto_limit()).thenReturn(512000);

    this.faci_zip = new Facility_Photo_ZipImpl(config, common_zip);
    this.testcase = testcase;
  }












  /** 
   **********************************************************************************************
   * @brief 集約クラス側の圧縮ファイル解凍抽出メソッドの成功パターンのテストを行う。
   * 
   * @details
   * - 検査方法としては、抽出したデータをテストケースのファイルを、ハッシュ化して同一性確認を行う。
   *
   * @par 大まかな処理の流れ
   * -# テストケースから抽出テスト用の圧縮ファイルと、その圧縮ファイルの中に入っているファイルと
   * 同一のテストケースファイルを準備する。
   * -# テスト用の圧縮ファイルをテスト対象メソッドに投入し、結果のファイルリストを受け取る。
   * -# ファイルリストの中のファイルと、テストケースファイルとの同一性をハッシュ化して確認し、同一の
   * ファイルが含まれているか確認する。
   * -# テストの終了後に、抽出によって作成された一時ファイルを削除する。
   * 
   * @note 本来であれば、集約クラスのテストを行う前に共通クラス側を先に単体テストするべきだと思うが、
   * 集約クラス側の実装内容が共通クラス側のメソッドを呼び出しているだけの極めてシンプルな内容の為、
   * 集約クラス側だけのテストでも十分共通クラス側の動作保証ができると判断したので、このテストのみである。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 圧縮ファイルからのデータ抽出機能の成功パターンの確認() throws IOException, InputFileDifferException{

    File zip_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                        "compzip", 
                                                                        this.testcase.getZipOkData(), 
                                                                        this.prefix, 
                                                                        "zip", 
                                                                        true);

    File compare_file_1 = Facility_Double_Component_TestUtils.
                          generateTmpFile(this.target_func, 
                                          "form", 
                                          this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data), 
                                          this.prefix, 
                                          "png", 
                                          true);

    File compare_file_2 = Facility_Double_Component_TestUtils.
                          generateTmpFile(this.target_func, 
                                          "form", 
                                          this.testcase.getOkData().get(Facility_Photo_TestKeys.Photo_Data), 
                                          this.prefix, 
                                          "png", 
                                          true);


    List<FileEntity> tmp_file_list = new ArrayList<>();
    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zip_file))){

      MockMultipartFile mock = new MockMultipartFile("no_name", bis);
      tmp_file_list = this.faci_zip.extractZip(mock);
    }


    for(FileEntity item: tmp_file_list){
      try(BufferedInputStream bis_zip = new BufferedInputStream(new FileInputStream(item.getTmpfile()));
          BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(compare_file_1));
          BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(compare_file_2));){

        String zip_hash = DigestUtils.sha512_256Hex(bis_zip);
        String one_hash = DigestUtils.sha512_256Hex(bis_1);
        String two_hash = DigestUtils.sha512_256Hex(bis_2);

        this.softly.assertThat(zip_hash.equals(one_hash) || zip_hash.equals(two_hash)).isTrue();

      }finally{
        Facility_Double_Component_TestUtils.deleteTmpFileCheck(item.getTmpfile(), this.softly);
      }
    }

    this.softly.assertAll();
  }









  /** 
   **********************************************************************************************
   * @brief 集約クラス側の圧縮ファイル解凍抽出メソッドの失敗パターンのテストを行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドに処理に失敗するパターンの圧縮ファイルを投入した際に、エラーとなり
   * 処理が止まることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースから抽出処理でエラーになるパターンのテストケースを取得する。
   * -# テスト用の圧縮ファイルをテスト対象メソッドに投入した際に、エラーとなることを確認する。
   * 
   * @note 本来であれば、集約クラスのテストを行う前に共通クラス側を先に単体テストするべきだと思うが、
   * 集約クラス側の実装内容が共通クラス側のメソッドを呼び出しているだけの極めてシンプルな内容の為、
   * 集約クラス側だけのテストでも十分共通クラス側の動作保証ができると判断したので、このテストのみである。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 圧縮ファイルからのデータ抽出機能の失敗パターンの確認() throws IOException, InputFileDifferException{

    List<String> fail_data = testcase.getZipFailedData(false);

    for(String file_name: fail_data){
      File zip_file = Facility_Double_Component_TestUtils.generateTmpFile(this.target_func, 
                                                                          "compzip", 
                                                                          file_name, 
                                                                          this.prefix, 
                                                                          "zip", 
                                                                          true);

      try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zip_file))){
        MockMultipartFile mock = new MockMultipartFile("no_name", bis);

        this.softly.assertThatThrownBy(() -> this.faci_zip.extractZip(mock))
                   .isInstanceOf(InputFileDifferException.class);
      }
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
