/** 
 **************************************************************************************
 * @file Score_Pdf_ZipImpl_OutputZip_Test.java
 * @brief 主に[楽譜データ管理]の圧縮ファイルの出力機能のテストを行うクラスを格納した
 * ファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.Score_Pdf_ZipImpl_Test;

import com.springproject.dockerspring.CommonTestCaseMaker.MusicalScore.Musical_Score_TestCase_Make;
import com.springproject.dockerspring.CommonTestCaseMaker.ScorePdf.Score_Pdf_TestCase_Make;
import com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils.Score_Double_Component_TestUtils;
import com.springproject.dockerspring.JoinTests.FileIO.ZipProcess_Test.TestInterface.ZipProcess_OutputZip_Test;
import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Samba;
import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Zip;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.FileIO.SambaProcess.Common_Samba;
import com.springproject.dockerspring.FileIO.SambaProcess.Score_Pdf_SambaImpl;
import com.springproject.dockerspring.FileIO.ZipProcess.Common_Zip;
import com.springproject.dockerspring.FileIO.ZipProcess.Score_Pdf_ZipImpl;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;

import static org.mockito.Mockito.mock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;










/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理]の圧縮ファイルの出力機能のテストを行うクラス
 * 
 * @details 
 * - このテストで対象となるクラスは、[Score_Pdf_ZipImpl]である。
 * - 主に、集約クラス側の圧縮ファイルの出力機能をテストする。
 * 
 * @see Score_Double_Component_TestUtils
 * @see ZipProcess_OutputZip_Test
 * @see Score_Pdf_ZipImpl
 * @see Score_Pdf_Zip
 * @see Common_Zip
 * @see Score_Pdf_SambaImpl
 * @see Score_Pdf_Samba
 * @see Common_Samba
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
@SpringBootTest(classes = {Score_Pdf_TestCase_Make.class, 
                           Musical_Score_TestCase_Make.class, 
                           Common_Zip.class, 
                           Score_Pdf_SambaImpl.class, 
                           Common_Samba.class, 
                           Environment_Config.class})
public class Score_Pdf_ZipImpl_OutputZip_Test implements ZipProcess_OutputZip_Test{

  private final Score_Pdf_TestCase_Make testcase;
  private final Musical_Score_TestCase_Make foreign_case;
  private static Score_Pdf_TestCase_Make testcase_static;
  private static Musical_Score_TestCase_Make foreign_case_static;
  private final Score_Pdf_Zip score_zip;
  private final Score_Pdf_Samba score_samba;

  private final SoftAssertions softly = new SoftAssertions();
  private final Environment_Config samba_config;
  private static Environment_Config samba_config_static;

  private final String target_func = "ScorePdf";
  private final String target_dir = "setup";
  private final String prefix = "ScorePdfZipImplOutputZipTest";
  private final String extension = "pdf";


  //! 圧縮前の総合データサイズ                                                 
  private int all_datasize = 0;

  //! 生成予定のファイル名
  private List<String> generate_name = new ArrayList<>();  











  /** 
   **********************************************************************************************
   * @brief コンストラクタインジェクションで、テストケースと検査対象クラスをDIする。
   * 
   * @details
   * - 環境変数に関しては、不定値の為、モックとしてインジェクションを行う。
   * - 環境変数は使用しないが、モックとしてインジェクションする必要がある。
   *
   * @param[in] testcase テストケースクラス
   * @param[in] foreign_case 参照するテストケースクラス
   * @param[in] common_zip 共通クラス側
   * 
   * @see Common_Zip
   * @see Score_Pdf_ZipImpl
   * @see Score_Pdf_Samba
   * 
   * @throw IOException
   **********************************************************************************************
   */
  @Autowired
  public Score_Pdf_ZipImpl_OutputZip_Test(Score_Pdf_TestCase_Make testcase, 
                                          Musical_Score_TestCase_Make foreign_case, 
                                          Common_Zip common_zip, 
                                          Score_Pdf_Samba score_samba, 
                                          Environment_Config samba_config) throws IOException{

    Environment_Config config = mock(Environment_Config.class);

    this.score_zip = new Score_Pdf_ZipImpl(config, common_zip);
    this.testcase = testcase;
    this.foreign_case = foreign_case;
    this.score_samba = score_samba;
    this.samba_config = samba_config;

    this.foreign_case.databaseSetup();
    this.testcase.databaseSetup(true);
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
    Score_Pdf_ZipImpl_OutputZip_Test.testcase_static = this.testcase;
    Score_Pdf_ZipImpl_OutputZip_Test.foreign_case_static = this.foreign_case;
    Score_Pdf_ZipImpl_OutputZip_Test.samba_config_static = this.samba_config;
  }











  /** 
   **********************************************************************************************
   * @brief 圧縮対象となるテストケースのファイルを複数作成する。
   * 
   * @details
   * - 作成の際には、圧縮後とのデータ量の比較の為、全てのファイルデータ量を合算した数値を保存しておく。
   * - 圧縮ファイルに格納するファイルの名前が被った場合の比較リストとして、オリジナルの名前の名前本体と
   * ドット付き拡張子文字列の間に、括弧を付けて連番をふったものを作成する。
   * 
   * @par 大まかな処理の流れ
   * -# ファイルサーバーから、シリアルナンバーを指定して順番にバイナリデータを取得する。取得の際には、
   * テストケースから対応するハッシュ値を用いて、照合を行ってから取得する。
   * -# データを取得したら、任意をファイル名をつけて専用のエンティティに格納する。名前は、被った際の
   * 挙動を確認したいので、すべてのファイルで同一にしておく。
   * -# 圧縮処理に投入するデータの全てのデータ量の合計を記憶しておく。圧縮後に、圧縮後のデータ量と比較して
   * 圧縮がされていることを確認するためである。
   * -# 想定される圧縮後の新しいファイル名を、リストに格納して記憶しておく。想定されるファイル名としては、
   * 名称が被った際にファイル名と拡張子の間に連番がふられたファイル名である。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private List<FileEntity> makeCommpressFiles() throws IOException, InputFileDifferException{

    List<FileEntity> tmp_file_list = new ArrayList<>();

    for(int i = 0; i < 20; i++){
      String compare_hash = this.testcase.compareEntityMake(i + 1, true).getPdf_hash();
      File before_file = this.score_samba.fileOutput(i + 1, compare_hash);

      String file_name = "test-file.wav";
      tmp_file_list.add(new FileEntity(file_name, before_file));
      this.all_datasize += before_file.length();

      if(i == 0){
        this.generate_name.add(file_name);
      }else{
        int idx = file_name.lastIndexOf(".");
        String dot_ext = idx != -1 ? file_name.substring(idx) : "";
        String cut_filename = file_name.replace(dot_ext, "");

        this.generate_name.add(cut_filename + "(" + (i) + ")" + dot_ext);
      }
    }

    return tmp_file_list;
  }












  /** 
   **********************************************************************************************
   * @brief テスト対象メソッドから生成された圧縮ファイルを解凍し、中に入っていたデータの同一性を検証する。
   * 
   * @details
   * - 中身のデータがテストケースの圧縮前のファイルと同一のものに復元できているか確認する。
   * - また、事前に作成しておいた予想される連番付きのファイル名と比較して、圧縮ファイル内のファイルの名称が
   * 、自動連番がついて生成されていることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# 引数で渡された圧縮ファイルと、圧縮前の複数ファイルの総合データ量を比較し、明らかに圧縮前のデータの
   * 量よりも小さくなっている事を確認する。
   * -# 圧縮ファイルを解凍し、事前に用意したテストケースのファイルと、ハッシュ化して比較を行う。
   * なお、ファイルの順番は順不同の為、同じデータが存在すれば良いものとする。確認が終われば削除する。
   * -# 抽出したファイルの名称が被っていた場合、自動連番がついて自動生成されたファイル名がついていることを
   * 確認する。事前に作成した予想される名前のリストと比較する。一致確認が取れたら、そのファイルを削除する。
   * -# 最終的に抽出したすべてのファイルが、確認が終わって消えており、確認漏れで残ったファイルが存在しない事を
   * 確認する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   **********************************************************************************************
   */
  private void checkCompressData(File compress_file) throws IOException{

    this.softly.assertThat(compress_file.length() < this.all_datasize).isTrue();

    List<FileEntity> thawing = Score_Double_Component_TestUtils
                               .decompressTestZip(compress_file, this.prefix, this.extension);

    Files.deleteIfExists(compress_file.toPath());


    for(int i = 1; i <= 20; i++){
      File compare_file = Score_Double_Component_TestUtils.
                          generateTmpFile(this.target_func, 
                                          this.target_dir, 
                                          this.testcase.compareSambaFileMake(i), 
                                          this.prefix, 
                                          this.extension, 
                                          true);                        
      Boolean diff = true;
  
      for(FileEntity item: thawing){
        try(BufferedInputStream bis_1 = new BufferedInputStream(new FileInputStream(compare_file));
            BufferedInputStream bis_2 = new BufferedInputStream(new FileInputStream(item.getTmpfile()));){
  
          String compare_hash = DigestUtils.sha512_256Hex(bis_1);
          String result_hash = DigestUtils.sha512_256Hex(bis_2);
  
          if(!compare_hash.equals(result_hash)){
            continue;
          }
  
          diff = false;
          Score_Double_Component_TestUtils.deleteTmpFileCheck(item.getTmpfile(), this.softly);
  
          this.softly.assertThat(this.generate_name).contains(item.getFilename());
          this.generate_name.remove(item.getFilename());
          thawing.remove(item);
        }
  
        break;
      }
  
      if(diff){
        this.softly.fail("ここに到達したのであれば、一致するファイルが存在しなかったことになります。");
      }
    }


    this.softly.assertThat(this.generate_name).isEmpty();
    this.softly.assertThat(thawing).isEmpty();
  }









  /** 
   **********************************************************************************************
   * @brief テスト対象メソッドに圧縮する複数のファイルを渡し、正常に圧縮された状態で返ってくることを確認する。
   * 
   * @details
   * - 内容としては、圧縮後のファイルを解析し、圧縮前のファイルと同一のものが入っている事、ファイル名が
   * 想定された通りの名称になっていることを確認する。
   * 
   * @par 大まかな処理の流れ
   * -# モックの関数型インターフェースとして、圧縮対象となる複数のバイナリファイルを生成するモックの関数を
   * 作成する。
   * -# テスト対象メソッドに、その関数型インターフェースを渡し、圧縮処理を実施する。
   * -# 返ってきた圧縮ファイルを解凍して解析し、圧縮前のファイルと同一のものがすべて入っている事、ファイル名が
   * 被っていた場合に、自動連番がついた自動生成のファイル名がついていることを確認する。
   * 
   * @see Score_Double_Component_TestUtils
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Test
  @Override
  public void 圧縮ファイル出力機能の動作確認() throws IOException, InputFileDifferException{

    Supplier<List<FileEntity>> generate_func = () -> {
      try {
        return makeCommpressFiles();
      } catch (IOException | InputFileDifferException e) {
        throw new IllegalCallerException(e);
      }
    };

    checkCompressData(this.score_zip.outputZip(generate_func));
    this.softly.assertAll();
  }









  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリとファイルサーバーが空っぽであることを確認する。
   * 
   * @throw IOException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @AfterAll
  public static void 一時ファイル用のディレクトリとファイルサーバーの初期化確認() throws InterruptedException, IOException{
    Score_Pdf_ZipImpl_OutputZip_Test.testcase_static.databaseReset(true);
    Score_Pdf_ZipImpl_OutputZip_Test.foreign_case_static.databaseReset();
    Score_Double_Component_TestUtils.checkTmpDirClear();
    Score_Double_Component_TestUtils.checkSambaClear(Score_Pdf_ZipImpl_OutputZip_Test.samba_config_static);
  }
}
