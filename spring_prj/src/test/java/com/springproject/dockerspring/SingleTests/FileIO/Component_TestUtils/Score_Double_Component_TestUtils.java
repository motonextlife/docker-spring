/** 
 **************************************************************************************
 * @file Score_Double_Component_TestUtils.java
 * @brief 主に[楽譜管理]と[楽譜データ管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils;

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
import java.io.BufferedOutputStream;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.SoftAssertions;
import org.springframework.core.io.ClassPathResource;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;











/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]と[楽譜データ管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
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
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 検査内容としては、シリアルナンバーのみをNullとし、他の値は比較用のエンティティの値と完全一致する
   * 事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   * 
   * @see Musical_Score
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public static Musical_Score makeMockEntity(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyyMMdd");
    Musical_Score mock_ent = mock(Musical_Score.class);
    
    when(mock_ent.getSerial_num()).thenReturn(null);
    when(mock_ent.getScore_id()).thenReturn(csv_map.get("score_id"));
    when(mock_ent.getBuy_date()).thenReturn(parse_date.parse(csv_map.get("buy_date")));
    when(mock_ent.getSong_title()).thenReturn(csv_map.get("song_title"));
    when(mock_ent.getComposer()).thenReturn(csv_map.get("composer"));
    when(mock_ent.getArranger()).thenReturn(csv_map.get("arranger"));
    when(mock_ent.getPublisher()).thenReturn(csv_map.get("publisher"));
    when(mock_ent.getStorage_loc()).thenReturn(csv_map.get("storage_loc"));
    when(mock_ent.getDisp_date()).thenReturn(parse_date.parse(csv_map.get("disp_date")));
    when(mock_ent.getOther_comment()).thenReturn(csv_map.get("other_comment"));

    return mock_ent;
  }










  /** 
   **********************************************************************************************
   * @brief 各エンティティに備わっているデータ文字列変換メソッドのモックを作成する。
   * 
   * @details
   * - ここで言うデータ文字列変換メソッドとは、各エンティティの[makeMap]メソッドの事である。
   * - テストケースからモックとして作成されたエンティティ情報を使って、データを文字列に変換した値を返却
   * するモックを作成する。
   *
   * @param[in] mock_entity モックを作成する既存のモックエンティティ
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   * 
   * @see Musical_Score
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public static Musical_Score methodMockMakeMap(Musical_Score mock_entity) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, String> output_mockmap = new HashMap<>();

    Integer serial_num = mock_entity.getSerial_num();
    output_mockmap.put("serial_num", serial_num == null ? "" : String.valueOf(serial_num));
            
    String score_id = mock_entity.getScore_id();
    output_mockmap.put("score_id", score_id == null ? "" : score_id);
            
    Date buy_date = mock_entity.getBuy_date();
    output_mockmap.put("buy_date", buy_date == null ? "" : parse_date.format(buy_date));
            
    String song_title = mock_entity.getSong_title();
    output_mockmap.put("song_title", song_title == null ? "" : song_title);
            
    String composer = mock_entity.getComposer();
    output_mockmap.put("composer", composer == null ? "" : composer);
            
    String arranger = mock_entity.getArranger();
    output_mockmap.put("arranger", arranger == null ? "" : arranger);
            
    String publisher = mock_entity.getPublisher();
    output_mockmap.put("publisher", publisher == null ? "" : publisher);
            
    String storage_loc = mock_entity.getStorage_loc();
    output_mockmap.put("storage_loc", storage_loc == null ? "" : storage_loc);
            
    Date disp_date = mock_entity.getDisp_date();
    output_mockmap.put("disp_date", disp_date == null ? "" : parse_date.format(disp_date));
            
    String other_comment = mock_entity.getOther_comment();
    output_mockmap.put("other_comment", other_comment == null ? "" : other_comment);

    when(mock_entity.makeMap()).thenReturn(output_mockmap);

    return mock_entity;
  }











  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検査内容としては、シリアルナンバーのみをNullとし、他の値は比較用のエンティティの値と完全一致する
   * 事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @see Musical_Score
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Musical_Score compare, Musical_Score result, SoftAssertions softly){
    softly.assertThat(result.getSerial_num()).isNull();
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
   * @param[in] extension 一時ファイル作成時の指定拡張子文字列
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
                                     String extension, 
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
                                     ".".concat(extension), 
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
   * @brief ファイルサーバーへのアクセスの際のファイルパス文字列を作成する。
   *
   * @details
   * - 環境変数はこのクラスでDIはせず、このメソッドを用いるテストクラスからもらって設定する。
   * この理由としては、対象のテストをこのクラスに依存させないためである。
   * 
   * @return 作成したファイルパス文字列
   * 
   * @param[in] file_name 接続先のファイル名
   * @param[in] ext 接続先ファイル名に付加する拡張子文字列
   * @param[in] hist 「True」で履歴データ用ディレクトリ、「False」で通常データ用ディレクトリにアクセス。
   * @param[in] config 環境変数のインスタンス
   * 
   * @note ファイルサーバーへの通信のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static String sambaMakePath(String file_name, 
                                     String ext, 
                                     Boolean hist, 
                                     Environment_Config config) throws IOException {

    String url = config.getSamba_url();

    if(hist){
      return url + "/ScorePdf/history/" + file_name + "." + ext;
    }else{
      return url + "/ScorePdf/normal/" + file_name + "." + ext;
    }
  }








  /** 
   **********************************************************************************************
   * @brief ファイルサーバー内に、テストケースとなるデータを準備する。なお、フラグを立てれば、通常のファイル
   * としてではなく、空のディレクトリを作成する。
   *
   * @details
   * - 指定された原本データを、ファイルサーバー内の指定されたファイルパスに対して書き込む。
   * - ディレクトリの作成時は、既に同じファイルが無い事を確認したうえで行う。
   * 
   * @param[in] source 書き込みの対象となるデータファイル
   * @param[in] write_path 書き込み先になるファイルサーバー上のファイルパス
   * @param[in] config 環境変数のインスタンス
   * @param[in] dir_make 「True」で、通常ファイルではなく空のディレクトリを作成する。
   * 
   * @note ファイルサーバーへの通信のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void preparaSambaData(File source, 
                                      String write_path, 
                                      Environment_Config config, 
                                      Boolean dir_make) throws IOException {

    try(SmbFile smb = new SmbFile(write_path, Score_Double_Component_TestUtils.sambaTestSetup(config))){
      if(dir_make){
        if(!smb.exists()){
          smb.mkdir();
        }

      }else{
        try(BufferedOutputStream bos = new BufferedOutputStream(smb.openOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));){

          byte[] buf = new byte[1024];
          int readsize = 0;
          
          while((readsize = bis.read(buf)) != -1){
            bos.write(buf, 0, readsize);
          }  
        }
      }
    }
  }










  /** 
   **********************************************************************************************
   * @brief テスト後に生成したファイルサーバー内のファイルを削除する。
   *
   * @details
   * - 指定されたファイルを削除した後、遅延アサーションを使って確実にファイルが消えていることを確認した
   * 上で処理を終了する。
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * 
   * @param[in] delete_target 削除対象のファイル名
   * @param[in] ext 削除対象ファイル名につける拡張子文字列
   * @param[in] hist 「True」で履歴データ用ディレクトリ、「False」で通常データ用ディレクトリにアクセス。
   * @param[in] config 環境変数のインスタンス
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note ファイルサーバーへの通信のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void deleteSambaFileCheck(String delete_target, 
                                          String ext, 
                                          Boolean hist, 
                                          Environment_Config config, 
                                          SoftAssertions softly) throws IOException{

    String url = config.getSamba_url();
    String dir = "";

    if(hist){
      dir = url + "/ScorePdf/history/" + delete_target + "." + ext;
    }else{
      dir = url + "/ScorePdf/normal/" + delete_target + "." + ext;
    }

    try(SmbFile smb = new SmbFile(dir, Score_Double_Component_TestUtils.sambaTestSetup(config))) {
      smb.delete();
      softly.assertThat(smb.exists()).isFalse();
    }
  }










  /** 
   **********************************************************************************************
   * @brief テスト後にファイルサーバーと一時ファイルディレクトリをリセットする。
   * 
   * @details 
   * - このメソッドは、ファイルサーバーからのデータ取得テストの後に用いる。他の場所で用いると、プロダクト
   * コードの一時ファイル削除機能のバグに気づきにくくなる恐れがあるので、むやみに使わない事。
   * 
   * @param[in] config 環境変数のインスタンス
   * 
   * @note ファイルサーバーへの通信のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void resetDir(Environment_Config config) throws IOException{

    String url = config.getSamba_url();
    String normal_dir = url + "/ScorePdf/normal/";
    String history_dir = url + "/ScorePdf/history/";

    try(SmbFile smb_1 = new SmbFile(normal_dir, Score_Double_Component_TestUtils.sambaTestSetup(config));
        SmbFile smb_2 = new SmbFile(history_dir, Score_Double_Component_TestUtils.sambaTestSetup(config));) {

      SmbFile[] normal_list = smb_1.listFiles();
      for(SmbFile item: normal_list){
        item.delete();
      }

      SmbFile[] history_list = smb_2.listFiles();
      for(SmbFile item: history_list){
        item.delete();
      }
    }

    FileUtils.cleanDirectory(new File("src/main/resources/TempFileBuffer"));
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
