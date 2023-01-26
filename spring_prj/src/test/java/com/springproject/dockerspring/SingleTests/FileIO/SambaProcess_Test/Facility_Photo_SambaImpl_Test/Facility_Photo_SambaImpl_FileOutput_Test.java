/** 
 **************************************************************************************
 * @file Facility_Photo_SambaImpl_FileOutput_Test.java
 * @brief 主に[設備写真データ管理]のファイルサーバーのデータ出力機能のテストを行うクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.Facility_Photo_SambaImpl_Test;

import com.springproject.dockerspring.FileIO.CompInterface.Facility_Photo_Samba;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.FileIO.SambaProcess.Facility_Photo_SambaImpl;
import com.springproject.dockerspring.FileIO.SambaProcess.Common_Samba;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make.Facility_Photo_TestKeys;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface.SambaProcess_FileOutput_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;

import jcifs.CIFSException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;









/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]のファイルサーバーのデータ出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Common_Samba]と[Facility_Photo_SambaImpl]である。
 * - 主に、共通クラス側のファイルサーバー上のデータ出力機能と、集約クラス側のファイルサーバー上
 * のデータ出力をテストする。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see Facility_Photo_SambaImpl_FileOutput_Test
 * @see Facility_Photo_SambaImpl
 * @see Facility_Photo_Samba
 * @see Common_Samba
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Facility_Photo_TestCase_Make.class, 
                           Facility_Photo_SambaImpl.class, 
                           Common_Samba.class, 
                           Environment_Config.class})
public class Facility_Photo_SambaImpl_FileOutput_Test implements SambaProcess_FileOutput_Test{

  private final Facility_Photo_TestCase_Make testcase;
  private final Facility_Photo_Samba faci_samba;
  private final Common_Samba common_samba;
  private final Environment_Config config;

  private final SoftAssertions softly = new SoftAssertions();

  private static Environment_Config config_static;

  private final String target_func = "FacilityPhoto";
  private final String target_dir = "form";
  private final String prefix = "FacilityPhotoSambaImplFileOutputTest";
  private final String ext = "png";
  private final String hist_ext = "gz";

  private final File normal_file;
  private final File hist_file;
  private final File other_file;
  private final File hist_other_file;








  /** 
   **********************************************************************************************
   * @brief 必要なクラスをDIした後に、ファイルサーバー内に必要なテストケースのファイルを事前に保存する。
   *
   * @details
   * - ファイルサーバー内には、違う種類のテストケースファイルのほか、空のディレクトリを保存する。
   * 
   * @param[in] testcase テストケースクラス
   * @param[in] faci_samba テスト対象の集約クラス
   * @param[in] common_samba テスト対象の共通クラス
   * @param[in] config 環境変数のインスタンス
   * 
   * @see Facility_Photo_Samba
   * @see Common_Samba
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Facility_Photo_SambaImpl_FileOutput_Test(Facility_Photo_TestCase_Make testcase, 
                                                  Facility_Photo_Samba faci_samba, 
                                                  Common_Samba common_samba, 
                                                  Environment_Config config) throws IOException{
                                   
    this.testcase = testcase;
    this.faci_samba = faci_samba;
    this.common_samba = common_samba;
    this.config = config;


    String normal_data = this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data);
    String other_data = this.testcase.getPhotoDataFailedData().get(0);

    this.normal_file = Facility_Double_Component_TestUtils.
                       generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                       this.prefix, this.ext, true);

    this.hist_file = this.common_samba.compress(
                        Facility_Double_Component_TestUtils.
                        generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                        this.prefix, this.ext, false)
                     );
                     
    this.other_file = Facility_Double_Component_TestUtils.
                      generateTmpFile(this.target_func, this.target_dir, other_data, 
                                      this.prefix, this.ext, true);

    this.hist_other_file = this.common_samba.compress(
                              Facility_Double_Component_TestUtils.
                              generateTmpFile(this.target_func, this.target_dir, other_data, 
                                              this.prefix, this.ext, false)
                           );


    String normal_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.ext, false, this.config);
    String normal_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.ext, false, this.config);
    String normal_dir = Facility_Double_Component_TestUtils.sambaMakePath("3", this.ext, false, this.config);
    String history_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.hist_ext, true, this.config);
    String history_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.hist_ext, true, this.config);
    String history_dir = Facility_Double_Component_TestUtils.sambaMakePath("3", this.hist_ext, true, this.config);
    
    Facility_Double_Component_TestUtils.preparaSambaData(this.normal_file, normal_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(this.other_file, normal_path_2, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(null, normal_dir, this.config, true);
    Facility_Double_Component_TestUtils.preparaSambaData(this.hist_file, history_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(this.hist_other_file, history_path_2, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(null, history_dir, this.config, true);
  }









  /** 
   **********************************************************************************************
   * @brief DIした環境変数を、静的変数でも使えるようにする。
   *
   * @details
   * - 設けた理由としては、テスト後のファイルサーバーのリセット確認の際に環境変数を用いる必要があるが、AfterAll
   * は静的メソッドでしか実装できない為、通常のフィールド変数にDIされている環境変数が使えない。
   * - そのため、新たに静的フィールド変数を作成して、その中にDIした環境変数の参照を入れておくことで、テスト後の
   * ファイルサーバーリセット確認にも環境変数を使用できるようにしている。
   * 
   * @par 使用アノテーション
   * - @PostConstruct
   * 
   * @note このメソッドは、セットアップのための非公開メソッドであり、外部に出さない。
   **********************************************************************************************
   */
  @PostConstruct
  private void configStaticSet(){
    Facility_Photo_SambaImpl_FileOutput_Test.config_static = this.config;
  }










  /** 
   **********************************************************************************************
   * @brief 取得したファイルの同一性確認を行う。
   *
   * @details
   * - あらかじめ用意してあるテストケースのファイルと、テスト対象クラスから取得してきたファイルをハッシュ化し
   * 同一性を確認する。
   * 
   * @param[in] compare_file 比較対象となるファイル
   * @param[in] get_file テスト対象クラスから取得してきたファイル
   * 
   * @note このメソッドは、アサーションのための非公開メソッドであり、外部に出さない。
   * 
   * @throw CIFSException
   * @throw IOException
   **********************************************************************************************
   */
  private void fileCheck(File compare_file, File get_file) throws CIFSException, IOException{

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(compare_file));
        BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(get_file));) {

      Facility_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);
    }
  }
  









  /** 
   **********************************************************************************************
   * @brief 共通クラス側のファイルサーバー内のファイル取得機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用してファイルサーバーからファイルを取得する。
   * その後、テストケースファイルと取得したファイルの同一性を確認する。
   * - なお、このテストは通常用データと履歴用データの両方を行う。
   *
   * @par 大まかな処理の流れ
   * -# ファイルサーバーからテスト対象メソッドを用いてファイルを取得する。
   * -# 取得したファイルが、テストケースファイルと同一であることを、ハッシュ化して確認する。
   * -# 取得しようとしたファイルが、ディレクトリだった場合にエラーになることを確認する。
   * -# 取得しようとしたファイルが、ファイルサーバー内に存在しなければエラーになることを確認する。
   * -# 全てのテストの終了後に、一時ファイルディレクトリとファイルサーバーをリセットする。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のファイルサーバーからのデータ出力機能の動作確認() throws IOException{

    File normal_1 = this.common_samba.fileInputCom(this.target_func, "1", this.ext, false);
    fileCheck(this.normal_file, normal_1);

    File normal_2 = this.common_samba.fileInputCom(this.target_func, "2", this.ext, false);
    fileCheck(this.other_file, normal_2);

    this.softly.assertThatThrownBy(() -> this.common_samba.fileInputCom(this.target_func, "3", this.ext, false))
               .isInstanceOf(IOException.class);
    this.softly.assertThatThrownBy(() -> this.common_samba.fileInputCom(this.target_func, "4", this.ext, false))
               .isInstanceOf(IOException.class);


    File history_1 = this.common_samba.fileInputCom(this.target_func, "1", this.hist_ext, true);
    fileCheck(this.hist_file, history_1);

    File history_2 = this.common_samba.fileInputCom(this.target_func, "2", this.hist_ext, true);
    fileCheck(this.hist_other_file, history_2);

    this.softly.assertThatThrownBy(() -> this.common_samba.fileInputCom(this.target_func, "3", this.hist_ext, true))
               .isInstanceOf(IOException.class);
    this.softly.assertThatThrownBy(() -> this.common_samba.fileInputCom(this.target_func, "4", this.hist_ext, true))
               .isInstanceOf(IOException.class);

    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の通常データファイル取得機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用してファイルサーバーからファイルを取得する。
   * その後、テストケースファイルと取得したファイルの同一性を確認する。
   *
   * @par 大まかな処理の流れ
   * -# ファイルサーバーからテスト対象メソッドを用いてファイルを取得する。
   * -# 取得したファイルが、テストケースファイルと同一であることを、ハッシュ化して確認する。
   * -# 引数で渡したハッシュ値と、取得してきたファイルのハッシュ値が一致しなかったらエラーになることを
   * 確認する。
   * -# 取得しようとしたファイルが、ディレクトリだった場合にエラーになることを確認する。
   * -# 取得しようとしたファイルが、ファイルサーバー内に存在しなければエラーになることを確認する。
   * -# 全てのテストの終了後に、一時ファイルディレクトリとファイルサーバーをリセットする。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバーからの通常データ出力機能の動作確認() throws IOException, InputFileDifferException{

    File normal_1 = this.faci_samba.fileOutput(1, this.faci_samba.makeHash(this.normal_file));
    fileCheck(this.normal_file, normal_1);

    File normal_2 = this.faci_samba.fileOutput(2, this.faci_samba.makeHash(this.other_file));
    fileCheck(this.other_file, normal_2);

    this.softly.assertThatThrownBy(() -> this.faci_samba.fileOutput(1, "blank"))
               .isInstanceOf(InputFileDifferException.class);
    this.softly.assertThatThrownBy(() -> this.faci_samba.fileOutput(3, "blank"))
               .isInstanceOf(IOException.class);
    this.softly.assertThatThrownBy(() -> this.faci_samba.fileOutput(4, "blank"))
               .isInstanceOf(IOException.class);

    this.softly.assertAll();
  }








  



  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の履歴データファイル取得機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用してファイルサーバーからファイルを取得する。
   * その後、テストケースファイルと取得したファイルの同一性を確認する。
   *
   * @par 大まかな処理の流れ
   * -# ファイルサーバーからテスト対象メソッドを用いてファイルを取得する。
   * -# 取得したファイルが、テストケースファイルと同一であることを、ハッシュ化して確認する。
   * -# 引数で渡したハッシュ値と、取得してきたファイルのハッシュ値が一致しなかったらエラーになることを
   * 確認する。
   * -# 取得しようとしたファイルが、ディレクトリだった場合にエラーになることを確認する。
   * -# 取得しようとしたファイルが、ファイルサーバー内に存在しなければエラーになることを確認する。
   * -# 全てのテストの終了後に、一時ファイルディレクトリとファイルサーバーをリセットする。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバーからの履歴データ出力機能の動作確認() throws IOException, InputFileDifferException{

    File history_1 = this.faci_samba.historyFileOutput(1, this.faci_samba.makeHash(this.normal_file));
    fileCheck(this.normal_file, history_1);

    File history_2 = this.faci_samba.historyFileOutput(2, this.faci_samba.makeHash(this.other_file));
    fileCheck(this.other_file, history_2);

    this.softly.assertThatThrownBy(() -> this.faci_samba.historyFileOutput(1, "blank"))
               .isInstanceOf(InputFileDifferException.class);
    this.softly.assertThatThrownBy(() -> this.faci_samba.historyFileOutput(3, "blank"))
               .isInstanceOf(IOException.class);
    this.softly.assertThatThrownBy(() -> this.faci_samba.historyFileOutput(4, "blank"))
               .isInstanceOf(IOException.class);

    this.softly.assertAll();
  }










  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリと、ファイルサーバー内が空っぽであることを確認する。
   * 
   * @throw InterruptedException
   * @throw IOException
   **********************************************************************************************
   */
  @AfterAll
  public static void ファイルサーバーと一時ファイルディレクトリの初期化確認() throws InterruptedException, IOException{
    Facility_Double_Component_TestUtils.resetDir(Facility_Photo_SambaImpl_FileOutput_Test.config_static);
    Facility_Double_Component_TestUtils.checkTmpDirClear();
    Facility_Double_Component_TestUtils.checkSambaClear(Facility_Photo_SambaImpl_FileOutput_Test.config_static);
  }
}
