/** 
 **************************************************************************************
 * @file Score_Pdf_SambaImpl_FileInput_Test.java
 * @brief 主に[楽譜データ管理]のファイルサーバーのデータ入力機能のテストを行うクラスを格納した
 * ファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.Score_Pdf_SambaImpl_Test;

import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Samba;
import com.springproject.dockerspring.FileIO.SambaProcess.Score_Pdf_SambaImpl;
import com.springproject.dockerspring.FileIO.SambaProcess.Common_Samba;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make.Score_Pdf_TestKeys;
import com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils.Score_Double_Component_TestUtils;
import com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface.SambaProcess_FileInput_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;

import jcifs.CIFSException;
import jcifs.smb.SmbFile;
import lombok.RequiredArgsConstructor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;









/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]のファイルサーバーのデータ入力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Common_Samba]と[Score_Pdf_SambaImpl]である。
 * - 主に、共通クラス側のファイルサーバー上のデータ入力機能と、集約クラス側のファイルサーバー上
 * のデータ入力をテストする。
 * 
 * @see Score_Double_Component_TestUtils
 * @see SambaProcess_FileInput_Test
 * @see Score_Pdf_SambaImpl
 * @see Score_Pdf_Samba
 * @see Common_Samba
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Score_Pdf_TestCase_Make.class, 
                           Score_Pdf_SambaImpl.class, 
                           Common_Samba.class, 
                           Environment_Config.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Score_Pdf_SambaImpl_FileInput_Test implements SambaProcess_FileInput_Test{

  private final Score_Pdf_TestCase_Make testcase;
  private final Score_Pdf_Samba score_samba;
  private final Common_Samba common_samba;
  private final Environment_Config config;

  private final SoftAssertions softly = new SoftAssertions();

  private static Environment_Config config_static;

  private final String target_func = "ScorePdf";
  private final String target_dir = "form";
  private final String prefix = "ScorePdfSambaImplFileInputTest";
  private final String ext = "pdf";
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
    Score_Pdf_SambaImpl_FileInput_Test.config_static = this.config;
  }
  








  /** 
   **********************************************************************************************
   * @brief 保存したファイルの同一性確認を行った後、ファイルサーバー内のファイルを削除する。
   *
   * @details
   * - ファイルサーバーに再度アクセスして取得したファイルが、保存したファイルと同一であることを確認する。
   * - 確認した後は、ファイルサーバー内のファイルを削除し、ファイルサーバーをリセットする。
   * 
   * @param[in] compare_file 比較対象となる保存したファイル
   * @param[in] file_name ファイルサーバーから取り出すファイル名
   * @param[in] ext 検索指定するファイルの拡張子文字列
   * @param[in] hist 「True」で履歴用データのディレクトリ検索、「False」で通常用データのディレクトリ検索
   * 
   * @note このメソッドは、アサーションのための非公開メソッドであり、外部に出さない。
   * 
   * @throw CIFSException
   * @throw IOException
   **********************************************************************************************
   */
  private void fileCheckAndReset(File compare_file, String file_name, String ext, Boolean hist) 
                                 throws CIFSException, IOException{

    String file_path = Score_Double_Component_TestUtils.sambaMakePath(file_name, ext, hist, this.config);

    try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(compare_file));
        SmbFile smb = new SmbFile(file_path, Score_Double_Component_TestUtils.sambaTestSetup(this.config));
        BufferedInputStream bis_2 = new BufferedInputStream(smb.openInputStream());) {

      Score_Double_Component_TestUtils.compareHash(bis_1, bis_2, this.softly);

    }finally{
      Score_Double_Component_TestUtils.deleteTmpFileCheck(compare_file, this.softly);
      Score_Double_Component_TestUtils.deleteSambaFileCheck(file_name, ext, hist, this.config, this.softly);
    }
  }










  /** 
   **********************************************************************************************
   * @brief テストで用いる、比較用ファイルやテストケースとして使用するファイルを準備する。
   *
   * @details
   * - 指定された種別に応じて、ファイルを作成し返却する。
   * 
   * @param[in] file_kind 指定ファイルの種別
   * 
   * @note このメソッドは、アサーションのための非公開メソッドであり、外部に出さない。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private File preparaSourceFile(String file_kind) throws IOException{

    String normal_data = this.testcase.getNormalData().get(Score_Pdf_TestKeys.Pdf_Data);
    String update_data = this.testcase.getPdfDataFailedData().get(0);
    String other_data = this.testcase.getPdfDataFailedData().get(1);
    String draft_data = this.testcase.getPdfDataFailedData().get(2);

    switch(file_kind){
      case "source":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, normal_data, 
                               this.prefix, this.ext, false);

      case "source_compress":
        return this.common_samba.compress(
                  Score_Double_Component_TestUtils.
                  generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                  this.prefix, this.ext, false)
               );

      case "normal_file":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, normal_data, 
                               this.prefix, this.ext, false);

      case "hist_file":
        return this.common_samba.compress(
                  Score_Double_Component_TestUtils.
                  generateTmpFile(this.target_func, this.target_dir, normal_data, 
                                  this.prefix, this.ext, false)
               );

      case "update_source":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, update_data, 
                               this.prefix, this.ext, false);

      case "update":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, update_data, 
                               this.prefix, this.ext, false);

      case "draft_source":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, draft_data, 
                               this.prefix, this.ext, false);

      case "other_source":
        return Score_Double_Component_TestUtils.
               generateTmpFile(this.target_func, this.target_dir, other_data, 
                               this.prefix, this.ext, false);

      default:
        throw new IllegalArgumentException();
    }
  }

  









  /** 
   **********************************************************************************************
   * @brief 共通クラス側のファイルサーバー内のファイルの出力保存機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用して、テストケースのファイルを保存する。その後に、ファイル
   * サーバーにアクセスし、テストケースと同一のファイルが存在することを確認する。
   * - 更新処理に関しては、テスト前に空のファイルをファイルサーバーに保存しておく。その後、テスト対象メソッドを
   * 用いてそのファイルをテストケースのファイルデータに更新する。その更新後のファイルがテストケースと同一であり、
   * 他の更新対象外のファイルに関しては変化がないことを確認する。
   * - なお、このテストは通常用データと履歴用データの両方を行う。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、テストケースのファイルを取得する。
   * -# 取得したファイルをテスト対象メソッドを利用してファイルサーバーに保管する。
   * -# 再度ファイルサーバーにアクセスして、保存したファイルを取り出す。
   * -# 取り出したファイルと、比較用のテストケースファイルをハッシュ化して比較し、同一の物であることを
   * 確認する。
   * -# 更新処理に関して、まずファイルサーバー内にあらかじめ更新対象となる下書きファイルを保存しておく。
   * -# テスト対象メソッドを利用して、更新対象のファイルをテストケースファイルで上書きする。
   * また、同時に違うファイルを新規追加しておく。
   * -# 再度ファイルサーバーにアクセスし、更新したファイルを取り出しテストケースと同一であることを確認。
   * -# 更新対象外のファイルに関しては、変化がなく更新がされていない事を確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 共通クラス側のファイルサーバーへのデータ入力機能の動作確認() throws IOException{

    //新規追加テスト
    File source = preparaSourceFile("source");
    File source_compress =  preparaSourceFile("source_compress");
    File normal_file =  preparaSourceFile("normal_file");
    File hist_file =  preparaSourceFile("hist_file");

    this.common_samba.fileOutputCom(this.target_func, "1", this.ext, normal_file, false);
    this.common_samba.fileOutputCom(this.target_func, "1", this.hist_ext, hist_file, true);

    fileCheckAndReset(source, "1", this.ext, false);
    fileCheckAndReset(source_compress, "1", this.hist_ext, true);



    //更新テスト
    File update_source =  preparaSourceFile("update_source");
    File update =  preparaSourceFile("update");
    File draft_source =  preparaSourceFile("draft_source");
    File other_source =  preparaSourceFile("other_source");

    String draft_path = Score_Double_Component_TestUtils.sambaMakePath("1", this.ext, false, this.config);
    String other_path = Score_Double_Component_TestUtils.sambaMakePath("2", this.ext, false, this.config);
    Score_Double_Component_TestUtils.preparaSambaData(draft_source, draft_path, this.config, false);
    Score_Double_Component_TestUtils.preparaSambaData(other_source, other_path, this.config, false);

    this.common_samba.fileOutputCom(this.target_func, "1", this.ext, update, false);

    fileCheckAndReset(update_source, "1", this.ext, false);
    fileCheckAndReset(other_source, "2", this.ext, false);
    
    Files.deleteIfExists(draft_source.toPath());
    this.softly.assertAll();
  }












  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の通常データファイルの出力保存機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用して、テストケースのファイルを保存する。その後に、ファイル
   * サーバーにアクセスし、テストケースと同一のファイルが存在することを確認する。
   * - 更新処理に関しては、テスト前に空のファイルをファイルサーバーに保存しておく。その後、テスト対象メソッドを
   * 用いてそのファイルをテストケースのファイルデータに更新する。その更新後のファイルがテストケースと同一であり、
   * 他の更新対象外のファイルに関しては変化がないことを確認する。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、テストケースのファイルを取得する。
   * -# 取得したファイルをテスト対象メソッドを利用してファイルサーバーに保管する。
   * -# 再度ファイルサーバーにアクセスして、保存したファイルを取り出す。
   * -# 取り出したファイルと、比較用のテストケースファイルをハッシュ化して比較し、同一の物であることを
   * 確認する。
   * -# 更新処理に関して、まずファイルサーバー内にあらかじめ更新対象となる下書きファイルを保存しておく。
   * -# テスト対象メソッドを利用して、更新対象のファイルをテストケースファイルで上書きする。
   * また、同時に違うファイルを新規追加しておく。
   * -# 再度ファイルサーバーにアクセスし、更新したファイルを取り出しテストケースと同一であることを確認。
   * -# 更新対象外のファイルに関しては、変化がなく更新がされていない事を確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバーへの通常データ入力機能の動作確認() throws IOException{

    //新規追加テスト
    File source = preparaSourceFile("source");
    File normal_file = preparaSourceFile("normal_file");

    this.score_samba.fileInsertUpdate(normal_file, 1);
    fileCheckAndReset(source, "1", this.ext, false);


    //更新テスト
    File update_source = preparaSourceFile("update_source");
    File update = preparaSourceFile("update");
    File draft_source = preparaSourceFile("draft_source");
    File other_source = preparaSourceFile("other_source");

    String draft_path = Score_Double_Component_TestUtils.sambaMakePath("1", this.ext, false, this.config);
    String other_path = Score_Double_Component_TestUtils.sambaMakePath("2", this.ext, false, this.config);
    Score_Double_Component_TestUtils.preparaSambaData(draft_source, draft_path, this.config, false);
    Score_Double_Component_TestUtils.preparaSambaData(other_source, other_path, this.config, false);

    this.score_samba.fileInsertUpdate(update, 1);

    fileCheckAndReset(update_source, "1", this.ext, false);
    fileCheckAndReset(other_source, "2", this.ext, false);

    Files.deleteIfExists(draft_source.toPath());
    this.softly.assertAll();
  }











  /** 
   **********************************************************************************************
   * @brief 集約クラス側のファイルサーバー内の履歴データファイルの出力保存機能のメソッドの検査を行う。
   * 
   * @details
   * - 検査方法としては、テスト対象メソッドを利用して、テストケースのファイルを保存する。その後に、ファイル
   * サーバーにアクセスし、テストケースと同一のファイルが存在することを確認する。
   * - 更新処理に関しては、テスト前に空のファイルをファイルサーバーに保存しておく。その後、テスト対象メソッドを
   * 用いてそのファイルをテストケースのファイルデータに更新する。その更新後のファイルがテストケースと同一であり、
   * 他の更新対象外のファイルに関しては変化がないことを確認する。
   * - なお、履歴情報に関しては要件上、更新処理は行わない為、更新のテストは行わない。
   *
   * @par 大まかな処理の流れ
   * -# テストケースクラスから、テストケースのファイルを取得する。
   * -# 取得したファイルをテスト対象メソッドを利用してファイルサーバーに保管する。
   * -# 再度ファイルサーバーにアクセスして、保存したファイルを取り出す。
   * -# 取り出したファイルと、比較用のテストケースファイルをハッシュ化して比較し、同一の物であることを
   * 確認する。
   * -# 更新処理に関して、まずファイルサーバー内にあらかじめ更新対象となる下書きファイルを保存しておく。
   * -# テスト対象メソッドを利用して、更新対象のファイルをテストケースファイルで上書きする。
   * また、同時に違うファイルを新規追加しておく。
   * -# 再度ファイルサーバーにアクセスし、更新したファイルを取り出しテストケースと同一であることを確認。
   * -# 更新対象外のファイルに関しては、変化がなく更新がされていない事を確認する。
   * -# テスト後は、生成した一時ファイルを削除する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 集約クラス側のファイルサーバーへの履歴データ入力機能の動作確認() throws IOException{

    //新規追加テスト
    File source_compress = preparaSourceFile("source_compress");
    File normal_file = preparaSourceFile("normal_file");

    this.score_samba.historyFileInsert(normal_file, 1);

    fileCheckAndReset(source_compress, "1", this.hist_ext, true);

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
    Score_Double_Component_TestUtils.checkTmpDirClear();
    Score_Double_Component_TestUtils.checkSambaClear(Score_Pdf_SambaImpl_FileInput_Test.config_static);
  }
}
