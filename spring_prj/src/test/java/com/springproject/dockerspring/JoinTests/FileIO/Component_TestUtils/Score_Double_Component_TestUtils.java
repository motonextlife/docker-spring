/** 
 **************************************************************************************
 * @file Score_Double_Component_TestUtils.java
 * @brief 主に[楽譜管理]と[楽譜データ管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは依存する
 * クラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.JoinTests.FileIO.Component_TestUtils;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;

import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.SoftAssertions;
import org.springframework.core.io.ClassPathResource;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;











/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]と[楽譜データ管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 * 
 * @note ここと同名のパッケージが、パッケージ[SingleTests]に存在するが、こちらは
 * 依存するクラスを、モックを使わずそのまま使ってテストした、結合テストである。
 **************************************************************************************
 */ 
public class Score_Double_Component_TestUtils{

  public static FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                 .fromString("rw-r--r--"));

  private Score_Double_Component_TestUtils(){
    throw new AssertionError();
  }











  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Musical_Score
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Musical_Score MockToReal(Musical_Score mock){

    Musical_Score real_ent = new Musical_Score();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setScore_id(mock.getScore_id());
    real_ent.setBuy_date(mock.getBuy_date());
    real_ent.setSong_title(mock.getSong_title());
    real_ent.setComposer(mock.getComposer());
    real_ent.setArranger(mock.getArranger());
    real_ent.setPublisher(mock.getPublisher());
    real_ent.setStorage_loc(mock.getStorage_loc());
    real_ent.setDisp_date(mock.getDisp_date());
    real_ent.setOther_comment(mock.getOther_comment());

    real_ent.stringSetNull();
    
    return real_ent;
  }











  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検査内容としては、シリアルナンバーは不定値なので対象外としてし、他の値は比較用のエンティティの値と
   * 完全一致する事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Musical_Score compare, Musical_Score result, SoftAssertions softly){
    softly.assertThat(result.getScore_id()).isEqualTo(compare.getScore_id());
    softly.assertThat(result.getBuy_date()).isEqualTo(compare.getBuy_date());
    softly.assertThat(result.getSong_title()).isEqualTo(compare.getSong_title());
    softly.assertThat(result.getComposer()).isEqualTo(compare.getComposer());
    softly.assertThat(result.getArranger()).isEqualTo(compare.getArranger());
    softly.assertThat(result.getPublisher()).isEqualTo(compare.getPublisher());
    softly.assertThat(result.getStorage_loc()).isEqualTo(compare.getStorage_loc());
    softly.assertThat(result.getDisp_date()).isEqualTo(compare.getDisp_date());
    softly.assertThat(result.getOther_comment()).isEqualTo(compare.getOther_comment());
  }








  /** 
   **********************************************************************************************
   * @brief テスト時にテスト対象クラスに投入するファイルのコピーを作成する。
   * 
   * @details
   * - このメソッドを設けた理由としては、原本のファイルを用いてテストを行ってしまうと、テスト対象クラスには
   * 使用した一時ファイルの削除機能が備わっているため、原本のデータが消えてしまう為。
   * - それを防ぐために、テスト対象クラスにはコピーしたファイルを投入して検査することで、原本データの削除を
   * 発生させないようにしている。
   * - なお、引数のフラグを指定する事で、コピーファイルではなくそのまま原本ファイルを返すことも可能である。
   * 
   * @return 複製した一時ファイル、又は原本ファイル
   *
   * @param[in] target_func 原本ファイルを取り出す対象の機能名
   * @param[in] target_dir 原本ファイルの入っているディレクトリ名
   * @param[in] original_filename 原本ファイル名
   * @param[in] prefix 一時ファイルの作成時に付加する文字列
   * @param[in] extention 一時ファイル作成時の指定拡張子文字列
   * @param[in] source_file 「True」で原本ファイル指定、「False」でコピーファイル指定。
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static File generateTmpFile(String target_func, 
                                     String target_dir, 
                                     String original_filename, 
                                     String prefix, 
                                     String extention, 
                                     Boolean source_file) throws IOException{

    Path source = new ClassPathResource(String.format("TestCaseFile/%s/BlobTestCaseData/%s/%s", 
                                        target_func, 
                                        target_dir, 
                                        original_filename))
                                        .getFile()
                                        .toPath();

    if(source_file){
      return source.toFile();
    }

    Path copy = Files.createTempFile(Paths.get("src/main/resources/TempFileBuffer"), 
                                     prefix, 
                                     ".".concat(extention), 
                                     Score_Double_Component_TestUtils.perms);

    return Files.copy(source, copy, REPLACE_EXISTING).toFile();
  }








  /** 
   **********************************************************************************************
   * @brief 指定された二つのファイルのストリームでハッシュ計算を行い、その値を利用して二つのファイルの一致
   * 確認を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用ファイルストリーム
   * @param[in] result テスト出力結果ファイルストリーム
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void compareHash(InputStream compare, InputStream result, SoftAssertions softly) throws IOException{
    String compare_hash = DigestUtils.sha512_256Hex(compare);
    String result_hash = DigestUtils.sha512_256Hex(result);

    softly.assertThat(compare_hash).isEqualTo(result_hash);
  }




  




  /** 
   **********************************************************************************************
   * @brief テスト後に生成した一時ファイルを削除する。
   *
   * @details
   * - 指定された一時ファイルを削除した後、遅延アサーションを使って確実にファイルが消えていることを確認した
   * 上で処理を終了する。
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * 
   * @param[in] delete_target 削除対象の一時ファイル
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void deleteTmpFileCheck(File delete_target, SoftAssertions softly) throws IOException{
    Files.deleteIfExists(delete_target.toPath());
    softly.assertThat(Files.exists(delete_target.toPath())).isFalse();
  }









  /** 
   **********************************************************************************************
   * @brief テスト対象となるファイルサーバーの接続のセットアップを行う。
   *
   * @details
   * - 引数で渡された環境変数を用いて、ファイルサーバーへの接続情報（バージョン、ユーザー名、パスワード）を
   * 設定し、設定されたコンテキストを返す。
   * - 環境変数はこのクラスでDIはせず、このメソッドを用いるテストクラスからもらって設定する。
   * この理由としては、対象のテストをこのクラスに依存させないためである。
   * 
   * @return 作成した接続コンテキスト
   * 
   * @param[in] config 環境変数のインスタンス
   * 
   * @note ファイルサーバーへの通信のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static CIFSContext sambaTestSetup(Environment_Config config) throws IOException{
    
    Properties prop = new Properties();
    prop.setProperty("jcifs.smb.client.minVersion", config.getSamba_min_ver());
    prop.setProperty("jcifs.smb.client.maxVersion", config.getSamba_max_ver());

    BaseContext bc = new BaseContext(new PropertyConfiguration(prop));

    String username = config.getSamba_username();
    String password = config.getSamba_password();
    NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator(username, password);

    return bc.withCredentials(auth);
  }











  /** 
   **********************************************************************************************
   * @brief テスト出力結果として戻ってきたZIPファイルを解凍し、ファイル名とファイルのマップリストを作成
   * して返す。
   *
   * @details
   * - 解凍したデータは一時ファイルに書き込み、マップリストにエントリのファイル名をキーとして格納する。
   * 
   * @param[in] compress_file 解凍の対象となる圧縮ファイル
   * @param[in] prefix 一時ファイル作成の際にファイル名に付加する文字列
   * @param[in] extension 一時ファイルの作成の際に指定する拡張子文字列
   * 
   * @note 圧縮ファイルのテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static List<FileEntity> decompressTestZip(File compress_file, String prefix, String extension) throws IOException {

    try(BufferedInputStream buf = new BufferedInputStream(new FileInputStream(compress_file));
        ZipInputStream zipin = new ZipInputStream(buf, StandardCharsets.UTF_8)){
          
      List<FileEntity> file_list = new ArrayList<>();
      LocalFileHeader zipent = null;
      while((zipent = zipin.getNextEntry()) != null){

        File entry = Files.createTempFile(Paths.get("src/main/resources/TempFileBuffer"), 
                                          prefix, 
                                          ".".concat(extension), 
                                          Score_Double_Component_TestUtils.perms)
                                          .toFile();

        try(FileOutputStream fos = new FileOutputStream(entry)){

          int readsize = 0;
          byte[] buffer = new byte[1024];

          while((readsize = zipin.read(buffer)) != -1){
            fos.write(buffer, 0, readsize);
          }
        }

        file_list.add(new FileEntity(zipent.getFileName(), entry));
      }

      return file_list;
    }
  }
  










  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリが空っぽであることを確認する。
   *
   * @details
   * - 判定前に1秒間待つ。判定に早く取り掛かりすぎると、一時ファイルの削除処理の途中にもかかわらず判定が
   * 始まってしまい、一時ファイルが削除済みでもエラー扱いとなるため。
   * 
   * @throw InterruptedException
   **********************************************************************************************
   */
  public static void checkTmpDirClear() throws InterruptedException{
    Thread.sleep(1000);
    File tmp_dir = new File("src/main/resources/TempFileBuffer");
    assertTrue(tmp_dir.list().length == 0);
  }








  /** 
   **********************************************************************************************
   * @brief テスト終了後に、ファイルサーバーが空っぽであることを確認する。
   *
   * @details
   * - 判定前に1秒間待つ。判定に早く取り掛かりすぎると、ファイルサーバーの中身の削除処理の途中にもかかわらず
   * 判定が始まってしまい、一時ファイルが削除済みでもエラー扱いとなるため。
   * 
   * @throw IOException
   * @throw InterruptedException
   **********************************************************************************************
   */
  public static void checkSambaClear(Environment_Config config) throws IOException, InterruptedException{
    Thread.sleep(1000);

    String url = config.getSamba_url();
    String normal_dir = url + "/ScorePdf/history/";
    String hist_dir = url + "/ScorePdf/normal/";

    try(SmbFile smb = new SmbFile(normal_dir, Score_Double_Component_TestUtils.sambaTestSetup(config))) {
      assertTrue(smb.listFiles().length == 0);
    }

    try(SmbFile smb = new SmbFile(hist_dir, Score_Double_Component_TestUtils.sambaTestSetup(config))) {
      assertTrue(smb.listFiles().length == 0);
    }
  }
}
