/** 
 **************************************************************************************
 * @file Facility_Photo_SambaImpl_FileDelete_Test.java
 * @brief 主に[設備写真データ管理]のファイルサーバーのデータ削除機能のテストを行うクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.Facility_Photo_SambaImpl_Test;

import com.springproject.dockerspring.FileIO.CompInterface.Facility_Photo_Samba;
import com.springproject.dockerspring.FileIO.SambaProcess.Facility_Photo_SambaImpl;
import com.springproject.dockerspring.FileIO.SambaProcess.Common_Samba;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.FacilityPhoto.Facility_Photo_TestCase_Make.Facility_Photo_TestKeys;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Facility_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface.SambaProcess_FileDelete_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;

import jcifs.CIFSException;
import jcifs.smb.SmbFile;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;









/** 
 **************************************************************************************
 * @brief 主に[設備写真データ管理]のファイルサーバーのデータ削除機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Common_Samba]と[Facility_Photo_SambaImpl]である。
 * - 主に、共通クラス側のファイルサーバー上のデータ削除機能と、集約クラス側のファイルサーバー上
 * のデータ削除をテストする。
 * 
 * @see Facility_Double_Component_TestUtils
 * @see SambaProcess_FileDelete_Test
 * @see Facility_Photo_SambaImpl
 * @see Facility_Photo_Samba
 * @see Common_Samba
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Facility_Photo_TestCase_Make.class, 
                           Facility_Photo_SambaImpl.class, 
                           Common_Samba.class, 
                           Environment_Config.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Facility_Photo_SambaImpl_FileDelete_Test implements SambaProcess_FileDelete_Test{

  private final Facility_Photo_TestCase_Make testcase;
  private final Facility_Photo_Samba faci_samba;
  private final Common_Samba common_samba;
  private final Environment_Config config;

  private final SoftAssertions softly = new SoftAssertions();

  private static Environment_Config config_static;

  private final String target_func = "FacilityPhoto";
  private final String target_dir = "form";
  private final String prefix = "FacilityPhotoSambaImplFileDeleteTest";
  private final String ext = "png";
  private final String hist_ext = "gz";






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
    Facility_Photo_SambaImpl_FileDelete_Test.config_static = this.config;
  }








  /** 
   **********************************************************************************************
   * @brief 指定されたファイルパスのデータの、ファイルサーバー内での有無を確認する。
   *
   * @details
   * - 削除対象であったファイルに関しては、ファイルサーバー内を検索した際にファイルが存在しないことを
   * 確認する。
   * - 削除対象ではないファイルに関しては、ファイルサーバー内を検索した際にファイルが残っており存在する
   * 事を確認する。
   * 
   * @param[in] delete_filename 削除したファイル名
   * @param[in] remain_filename 削除していない残っているファイル名
   * @param[in] ext 検索指定するファイルの拡張子文字列
   * @param[in] hist 「True」で履歴用データのディレクトリ検索、「False」で通常用データのディレクトリ検索
   * 
   * @note このメソッドは、アサーションのための非公開メソッドであり、外部に出さない。
   * 
   * @throw CIFSException
   * @throw IOException
   **********************************************************************************************
   */
  private void fileDeleteCheck(String delete_filename, String remain_filename, String ext, Boolean hist)
                               throws CIFSException, IOException{

    String delete_filename_path = Facility_Double_Component_TestUtils.sambaMakePath(delete_filename, ext, hist, this.config);
    String remain_filename_path = Facility_Double_Component_TestUtils.sambaMakePath(remain_filename, ext, hist, this.config);

    try(SmbFile smb = new SmbFile(delete_filename_path, Facility_Double_Component_TestUtils.sambaTestSetup(this.config))) {
      this.softly.assertThat(smb.exists()).isFalse();
    }

    try(SmbFile smb = new SmbFile(remain_filename_path, Facility_Double_Component_TestUtils.sambaTestSetup(this.config))) {
      this.softly.assertThat(smb.exists()).isTrue();
    }
  }
  










  /** 
   **********************************************************************************************
   * @brief 共通クラス側のファイルサーバー内のファイルの削除機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト前にファイルサーバー上にテストケースとなるファイルを保存しておき、テスト対象
   * クラスを実行してファイルを削除する。
   * - 削除実行後にファイルサーバーにアクセスし、目的のファイルが削除されていて、それ以外のファイルは削除されずに
   * 残っていることを確認する。
   * - なお、このテストは通常用データと履歴用データの両方を行う。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、あらかじめファイルサーバーに保存しておくファイルを取得する。
   * -# ファイルサーバーにテストケースのファイルを保存後、テスト対象メソッドを実行する。
   * -# ファイルサーバーにアクセスしてファイルを取り出し、削除対象のファイルが消えており、それ以外のファイルは
   * 残っていることを確認する。
   * -# ファイルサーバーに保存していないファイルを削除しようとした際に、エラーが発生することを確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のファイルサーバー内のデータ削除機能の動作確認() throws IOException{

    String normal_data = this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data);

    File normal_file = Facility_Double_Component_TestUtils.
                       generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                       this.prefix, this.ext, true);

    File hist_file = this.common_samba.compress(
                        Facility_Double_Component_TestUtils.
                        generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                        this.prefix, this.ext, false)
                     );

    String normal_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.ext, false, this.config);
    String normal_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.ext, false, this.config);
    String history_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.hist_ext, true, this.config);
    String history_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.hist_ext, true, this.config);

    Facility_Double_Component_TestUtils.preparaSambaData(normal_file, normal_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(normal_file, normal_path_2, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(hist_file, history_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(hist_file, history_path_2, this.config, false);


    this.common_samba.fileDeleteCom(this.target_func, "1", this.ext, false);
    this.common_samba.fileDeleteCom(this.target_func, "1", this.hist_ext, true);

    fileDeleteCheck("1", "2", this.ext, false);
    fileDeleteCheck("1", "2", this.hist_ext, true);

    this.softly.assertThatThrownBy(() -> this.common_samba.fileDeleteCom(this.target_func, "3", this.ext, false))
               .isInstanceOf(IOException.class);
    this.softly.assertThatThrownBy(() -> this.common_samba.fileDeleteCom(this.target_func, "3", this.hist_ext, false))
               .isInstanceOf(IOException.class);


    Facility_Double_Component_TestUtils.deleteTmpFileCheck(hist_file, this.softly);
    Facility_Double_Component_TestUtils.deleteSambaFileCheck("2", this.ext, false, this.config, this.softly);
    Facility_Double_Component_TestUtils.deleteSambaFileCheck("2", this.hist_ext, true, this.config, this.softly);
    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の通常データの削除機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト前にファイルサーバー上にテストケースとなるファイルを保存しておき、テスト対象
   * クラスを実行してファイルを削除する。
   * - 削除実行後にファイルサーバーにアクセスし、目的のファイルが削除されていて、それ以外のファイルは削除されずに
   * 残っていることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、あらかじめファイルサーバーに保存しておくファイルを取得する。
   * -# ファイルサーバーにテストケースのファイルを保存後、テスト対象メソッドを実行する。
   * -# ファイルサーバーにアクセスしてファイルを取り出し、削除対象のファイルが消えており、それ以外のファイルは
   * 残っていることを確認する。
   * -# ファイルサーバーに保存していないファイルを削除しようとした際に、エラーが発生することを確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバー内の通常データ削除機能の動作確認() throws IOException{

    File normal_file = Facility_Double_Component_TestUtils.
                       generateTmpFile(this.target_func, 
                                       this.target_dir, 
                                       this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data), 
                                       this.prefix, 
                                       this.ext, 
                                       true);

    String normal_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.ext, false, this.config);
    String normal_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.ext, false, this.config);

    Facility_Double_Component_TestUtils.preparaSambaData(normal_file, normal_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(normal_file, normal_path_2, this.config, false);


    this.faci_samba.fileDelete(1);

    fileDeleteCheck("1", "2", this.ext, false);

    this.softly.assertThatThrownBy(() -> this.faci_samba.fileDelete(3))
               .isInstanceOf(IOException.class);


    Facility_Double_Component_TestUtils.deleteSambaFileCheck("2", this.ext, false, this.config, this.softly);
    this.softly.assertAll();
  }













  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の履歴データの削除機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト前にファイルサーバー上にテストケースとなるファイルを保存しておき、テスト対象
   * クラスを実行してファイルを削除する。
   * - 削除実行後にファイルサーバーにアクセスし、目的のファイルが削除されていて、それ以外のファイルは削除されずに
   * 残っていることを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、あらかじめファイルサーバーに保存しておくファイルを取得する。
   * -# ファイルサーバーにテストケースのファイルを保存後、テスト対象メソッドを実行する。
   * -# ファイルサーバーにアクセスしてファイルを取り出し、削除対象のファイルが消えており、それ以外のファイルは
   * 残っていることを確認する。
   * -# ファイルサーバーに保存していないファイルを削除しようとした際に、エラーが発生することを確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Facility_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバー内の履歴データ削除機能の動作確認() throws IOException{

    File hist_file = this.common_samba.compress(
                        Facility_Double_Component_TestUtils.
                        generateTmpFile(this.target_func, 
                                        this.target_dir, 
                                        this.testcase.getNormalData().get(Facility_Photo_TestKeys.Photo_Data), 
                                        this.prefix, 
                                        this.ext, 
                                        false)
                     );

    String history_path_1 = Facility_Double_Component_TestUtils.sambaMakePath("1", this.hist_ext, true, this.config);
    String history_path_2 = Facility_Double_Component_TestUtils.sambaMakePath("2", this.hist_ext, true, this.config);

    Facility_Double_Component_TestUtils.preparaSambaData(hist_file, history_path_1, this.config, false);
    Facility_Double_Component_TestUtils.preparaSambaData(hist_file, history_path_2, this.config, false);


    this.faci_samba.historyFileDelete(1);

    fileDeleteCheck("1", "2", this.hist_ext, true);

    this.softly.assertThatThrownBy(() -> this.faci_samba.historyFileDelete(3))
               .isInstanceOf(IOException.class);


    Facility_Double_Component_TestUtils.deleteTmpFileCheck(hist_file, this.softly);
    Facility_Double_Component_TestUtils.deleteSambaFileCheck("2", this.hist_ext, true, this.config, this.softly);
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
    Facility_Double_Component_TestUtils.checkTmpDirClear();
    Facility_Double_Component_TestUtils.checkSambaClear(Facility_Photo_SambaImpl_FileDelete_Test.config_static);
  }
}
