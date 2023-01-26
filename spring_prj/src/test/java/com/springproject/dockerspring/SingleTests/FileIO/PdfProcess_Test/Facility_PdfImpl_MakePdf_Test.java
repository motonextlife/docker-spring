/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.FileIO.PdfProcess_Test
 * 
 * @brief 単体テストのファイル入出力テストのうち、[PDFファイルの入出力テスト]に関するテストを
 * 格納したパッケージ
 * 
 * @details
 * - このパッケージは、指定された機能の保存されているデータを、PDF形式のファイルとして正常に
 * 出力できるかをテストするクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.PdfProcess_Test;





/** 
 **************************************************************************************
 * @file Facility_PdfImpl_MakePdf_Test.java
 * @brief 主に[設備管理]のPDFデータの出力機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.CommonTestCaseMaker.Facility.Facility_TestCase_Make;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.FileIO.CompInterface.Facility_Pdf;
import com.springproject.dockerspring.FileIO.PdfProcess.Common_Pdf;
import com.springproject.dockerspring.FileIO.PdfProcess.Facility_PdfImpl;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;










/** 
 **************************************************************************************
 * @brief 主に[設備管理]のPDFデータの出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Facility_PdfImpl]である。
 * - 主に、PDFデータが正常に出力されるかどうかのテストを行う。
 * - デザインに関しては、このテストでは検査できないので、出力した後に目視で確認を行うしかない。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see PdfProcess_MakePdf_Test
 * @see Facility_PdfImpl
 * @see Facility_Pdf
 * @see Common_Pdf
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Facility_TestCase_Make.class, Facility_PdfImpl.class, Common_Pdf.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_PdfImpl_MakePdf_Test implements PdfProcess_MakePdf_Test{

  private final Facility_TestCase_Make testcase;
  private final Facility_Pdf faci_pdf;

  private final SoftAssertions softly = new SoftAssertions();











  /** 
   **********************************************************************************************
   * @brief PDFデータの出力テストを行う。
   * 
   * @details
   * - 検査方法としては、テスト対象クラスを実行した後に、PDFデータが存在するかを確認する。
   * - 本来であれば、PDFデータの同一性チェックを行うべきだが、PDFのデザインは今後頻繁に変わる不定値の
   * 為、jUnitでの自動テストが困難。
   * - そのため、自動テストでは生成ファイルの存在確認のみにとどめる。デザインは目視で確認を行う事。
   *
   * @par 大まかな処理の流れ
   * -# テスト対象クラスを実行し、返ってきた一時ファイルの存在確認。
   * -# テスト対象クラスにNullを渡して、エラーが発生することを確認。
   * -# テスト後は一時ファイルを削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  @Test
  public void PDF出力機能の動作確認と生成ファイルの存在確認() throws IOException, ParseException{

    Facility mock_ent = this.testcase.compareEntityMake(1);
    mock_ent = Facility_Double_Component_TestUtils.methodMockMakeMap(mock_ent);
    File tmp_file_path = faci_pdf.makePdf(mock_ent);

    try{
      this.softly.assertThat(tmp_file_path.exists()).isTrue();
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(tmp_file_path, this.softly);
    }
        
    this.softly.assertThatThrownBy(() -> faci_pdf.makePdf(null))
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
