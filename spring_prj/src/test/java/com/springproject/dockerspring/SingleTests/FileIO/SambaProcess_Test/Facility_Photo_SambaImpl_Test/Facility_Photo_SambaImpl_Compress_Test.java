/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.Facility_Photo_SambaImpl_Test
 * 
 * @brief ファイルサーバーへの入出力単体テストのうち、[設備写真データ管理機能]に関するテストを行うクラスを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージは、設備写真データ管理機能に関するファイルサーバーへの処理機能をテストするクラスを格納している。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.Facility_Photo_SambaImpl_Test;





/** 
 **************************************************************************************
 * @file Facility_Photo_SambaImpl_Compress_Test.java
 * @brief 主に[設備写真データ管理]のバイナリデータ圧縮解凍機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import com.springproject.dockerspring.FileIO.SambaProcess.Common_Samba;

import lombok.RequiredArgsConstructor;

import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make.Facility_Photo_TestKeys;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface.SambaProcess_Compress_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;








/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]のバイナリデータ圧縮解凍機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Common_Samba]である。
 * - 主に、共通クラス側のバイナリデータ圧縮＆解凍メソッドのテストを行う。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see SambaProcess_Compress_Test
 * @see Common_Samba
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Facility_Photo_TestCase_Make.class, Environment_Config.class, Common_Samba.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_Photo_SambaImpl_Compress_Test implements SambaProcess_Compress_Test{

  private final Facility_Photo_TestCase_Make testcase;
  private final Common_Samba common_samba;

  private final SoftAssertions softly = new SoftAssertions();


  private final String target_func = "FacilityPhoto";
  private final String target_dir = "form";
  private final String prefix = "FacilityPhotoSambaImplCompressTest";
  private final String extension = "png";










  /** 
   **********************************************************************************************
   * @brief 共通クラス側のバイナリデータ圧縮解凍メソッドの動作確認を行う。
   * 
   * @details
   * - 検査方法としては、テストケースのバイナリデータを圧縮し、元のデータのデータ量よりも明らかに少なくなって
   * いることを確認し、その後圧縮したファイルを解凍して、元通りになっていることを確認する。
   * - 比較方法としては、ハッシュ化して行う。
   *
   * @par 大まかな処理の流れ
   * -# テストケースとして投入するバイナリデータをテストケースから取得する。
   * -# 取得したバイナリデータを圧縮メソッドに投入して、圧縮されたファイルを取得する。
   * -# 元のテストケースファイルと、圧縮されたファイルのデータ量を比較して、圧縮後が明らかにデータ量が少なく
   * なっていることを確認する。
   * -# 次に圧縮されてるファイルを、解凍メソッドに投入し、解凍済みのファイルを取得する。
   * -# 解凍済みのファイルと、元のテストケースファイルを比較し、全く同じデータであることを確認する。
   * -# テスト後は、作成した一時ファイルを削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void バイナリデータの圧縮と解凍機能の動作確認() throws IOException{

    String normal_data = this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data);
  
    File source = Facility_Double_Component_TestUtils.
                  generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                  this.prefix, this.extension, true);

    File temp_file = Facility_Double_Component_TestUtils.
                     generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                     this.prefix, this.extension, false);


    File compress = this.common_samba.compress(temp_file);
    this.softly.assertThat(compress.length() < source.length()).isTrue();

    File decompress = this.common_samba.decompress(compress, "png");


    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(source));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(decompress));){

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }finally{
      Facility_Double_Component_TestUtils.deleteTmpFileCheck(decompress, this.softly);
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
