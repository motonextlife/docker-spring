/** 
 **************************************************************************************
 * @file Common_Samba.java
 * @brief すべてのファイルサーバー通信機能で使用する共通処理を定義するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.SambaProcess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Properties;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief すべてのファイルサーバー通信機能で使用する共通処理を定義するクラス。
 * 
 * @details 
 * - 通信対象のファイルサーバーの種類は、[samba]とする。
 * - この共通処理では、ファイルサーバー通信処理に関して、特定のクラスに依存しない共通の処理を
 * 定義する。作成対象の特定クラスに対してはDIでこのクラスを使えるようにする。
 * - このクラスで作成される一時ファイルに関しては、すべてローカルにある一時ファイル用の
 * ディレクトリに出力する。なお、その際のパーミッションは、実行権限をすべて剥奪しておく。
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - ファイルサーバーを操作するライブラリに関しては、[jcifs-ng]を用いる。これによって、
 * セキュリティを確保しつつファイルサーバーへの通信を行うことが可能になる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Common_Samba{

  private final Environment_Config config;

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));









  /** @name このクラス内で用いるユーティリティクラス（外部へは一切出さない） */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief ファイルサーバー内へのアクセスファイルパスの作成を行う。
   * 
   * @details
   * - アクセス対象の機能の名称と、ファイル名（シリアルナンバー又は履歴番号）を指定する事で、ファイルサーバー
   * へのアクセスに必要なファイルパスの組立を行う。
   *
   * @return 生成したファイルパス文字列 
   *
   * @param[in] dir アクセス対象の機能の名称
   * @param[in] file_name アクセス対象のファイル名（シリアルナンバー又は履歴番号）
   * @param[in] ext ファイル名に付加する拡張子
   * @param[in] hist 通常データ用のファイルサーバー上のディレクトリにアクセスする場合は「False」、履歴用の
   * ディレクトリにアクセスする場合は「True」とする。
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private String makePath(String dir, String file_name, String ext, Boolean hist) throws IOException {

    String url = this.config.getSamba_url();

    if(hist){
      return url + "/" + dir + "/history/" + file_name + "." + ext;
    }else{
      return url + "/" + dir + "/normal/" + file_name + "." + ext;
    }
  }








  /** 
   **********************************************************************************************
   * @brief このクラス内で生成する一時ファイルを一元的に製作するメソッド。
   * 
   * @details
   * - このメソッドを使用する際に、任意の文字列を渡すことでファイル名にその文字列を付加する。
   * これによって一時ファイル生成を指示した呼び出し元メソッドが特定できる。
   *
   * @return 生成した一時ファイル 
   *
   * @param[in] prefix ファイル生成時に、ファイル名に付加する名前
   * @param[in] ext ファイル生成時の拡張子の指定
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private File makeTmpFile(String prefix, String ext) throws IOException {

    try{
      Path tmp_path = Files.createTempFile(this.specify_dir, 
                                           prefix, 
                                           ".".concat(ext), 
                                           this.perms);
      return tmp_path.toFile();

    }catch(IOException e){
      throw new IOException("Error location [Common_Samba:makeTmpFile]" + "\n" + e);
    }
  }








  /** 
   **********************************************************************************************
   * @brief ファイルサーバーへの通信の際の、タイムアウトの時間の設定を行う。
   * 
   * @details
   * - 設定する内容としては、指定ファイルへの接続時のタイムアウト時間と、指定ファイルの読取時のタイムアウト
   * 時間を設定する。
   * - 設定時間は、環境変数から取得し、その値を設定する。
   * - 注意点として、二つの設定時間に同じ環境変数の時間を設定するため、厳密に環境変数の設定時間で必ず
   * タイムアウトとはならない。若干の誤差がある。
   *
   * @param[in] smb ファイルサーバーへの接続ストリーム
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private void setTimeout(SmbFile smb) throws IOException {
    smb.setConnectTimeout(this.config.getSamba_timeout());
    smb.setReadTimeout(this.config.getSamba_timeout());
  }



  




  /** 
   **********************************************************************************************
   * @brief ファイルサーバーへの通信の際の接続設定を作成する。
   * 
   * @details
   * - 設定する内容としては、接続先ファイルサーバーのバージョン設定や、認証情報を設定する。
   * - 接続先ファイルサーバーのユーザー名とパスワードに関しては、環境変数から指定する。
   *
   * @return 作成した接続コンテキスト
   * 
   * @par 大まかな処理の流れ
   * -# 接続先ファイルサーバーの対応最小バージョンと、対応最大バージョンをプロパティとして設定。
   * -# 環境変数から読み取った、ユーザー名とパスワードを用いて認証情報を作成。
   * -# 作成したプロパティと認証情報を用いて、接続コンテキストを作成して戻り値とする。
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw CIFSException
   **********************************************************************************************
   */
  private CIFSContext sambaSetup() throws CIFSException{

    Properties prop = new Properties();
    prop.setProperty("jcifs.smb.client.minVersion", this.config.getSamba_min_ver());
    prop.setProperty("jcifs.smb.client.maxVersion", this.config.getSamba_max_ver());

    BaseContext bc = null;

    try{
      bc = new BaseContext(new PropertyConfiguration(prop));
    }catch(CIFSException e){
      throw new CIFSException("Error location [Common_Samba:sambaSetup]" + "\n" + e);
    }

    String username = this.config.getSamba_username();
    String password = this.config.getSamba_password();
    NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator(username, password);

    return bc.withCredentials(auth);
  }

  /** @} */












  /** 
   **********************************************************************************************
   * @brief ファイルサーバーから指定したファイルを読み込む。
   * 
   * @details
   * - 引数として、指定する機能名と、ファイル名（シリアルナンバーや履歴番号）を指定する事で、ファイルを
   * 読み取ることができる。
   * - このメソッドは、通常データと履歴用データで共用で用いることができる。
   *
   * @return 読み取ったデータを格納した一時ファイル 
   *
   * @param[in] dir 読取を指定する機能名
   * @param[in] file_name 読取を指定するファイル名
   * @param[in] ext ファイル読取時のファイルの拡張子の指定
   * @param[in] hist 通常データ用のファイルサーバー上のディレクトリにアクセスする場合は「False」、履歴用の
   * ディレクトリにアクセスする場合は「True」とする。
   * 
   * @par 処理の大まかな流れ
   * -# 読み取ったファイルのデータを格納するための一時ファイルを作成。
   * -# 事前に作成したファイルサーバーへの接続コンテキストを用いて、入力ストリームを作成。
   * -# 読み取るファイルパスを指定して読み取る際に、その接続先のファイルが「ディレクトリではない」かつ
   * 「ファイルが存在する」ことを確認する。満たさなければ、エラーを出す。
   * -# データを読み取る際にメモリの枯渇を避けるため、バッファを設けながら少しづつデータを読み取って、作成
   * した一時ファイルに書き込む。
   * -# 書き込みが終わった一時ファイルを戻り値とする。
   * -# もし処理の過程でエラーが発生した場合は、作成した一時ファイルを削除し例を投げる。
   *
   * @throw IOException
   **********************************************************************************************
   */
  public File fileInputCom(String dir, String file_name, String ext, Boolean hist) throws IOException{

    File temp_file = makeTmpFile("fileInputCom", ext);
    String file_path = makePath(dir, file_name, ext, hist);

    try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp_file));
        SmbFile smb = new SmbFile(file_path, sambaSetup());
        BufferedInputStream bis = new BufferedInputStream(smb.openInputStream());) {

      setTimeout(smb);

      if(smb.isDirectory() || !smb.exists()){
        throw new IOException("file not exists or directory");
      }

      byte[] buf = new byte[1024];
      int read_size = 0;

      while((read_size = bis.read(buf)) != -1){
        bos.write(buf, 0, read_size);
      }

    }catch(IOException e){
      Files.deleteIfExists(temp_file.toPath());
      throw new IOException("Error location [Common_Samba:fileInputCom]" + "\n" + e);
    }

    return temp_file;
  }










  /** 
   **********************************************************************************************
   * @brief ファイルサーバーへ指定したファイルパスへデータを書き込む。
   * 
   * @details
   * - 引数として、指定する機能名と、ファイル名（シリアルナンバーや履歴番号）、書き込むデータが入った一時
   * ファイルを指定する事で、データを書き込むことができる。
   * - このメソッドは、通常データと履歴用データで共用で用いることができる。また、新規追加と更新で共用。
   *
   * @param[in] dir 書き込みを指定する機能名
   * @param[in] file_name 書き込み時に指定するファイル名
   * @param[in] ext 書き込む際に指定する拡張子
   * @param[in] temp_file 書き込むデータが格納された一時ファイル
   * @param[in] hist 通常データ用のファイルサーバー上のディレクトリに書き込む場合は「False」、履歴用の
   * ディレクトリに書き込む場合は「True」とする。
   * 
   * @par 処理の大まかな流れ
   * -# 書き込み対象のファイルパスを生成。
   * -# 事前に作成したファイルサーバーへの接続コンテキストを用いて、出力ストリームを作成。
   * -# 書き込み対象のデータを読み取る際にメモリの枯渇を避けるため、バッファを設けながら少しづつデータを
   * 読み取って、書き込み先のファイルサーバー上のディレクトリに書き込む。
   * -# エラーが出る出ないにかかわらず、引数で渡された一時ファイルは削除して、処理を終了する。
   *
   * @throw IOException
   **********************************************************************************************
   */
  public void fileOutputCom(String dir, String file_name, String ext, File temp_file, Boolean hist) throws IOException{

    String file_path = makePath(dir, file_name, ext, hist);

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(temp_file));
        SmbFile smb = new SmbFile(file_path, sambaSetup());
        BufferedOutputStream bos = new BufferedOutputStream(smb.openOutputStream());){

      setTimeout(smb);

      byte[] buf = new byte[1024];
      int read_size = 0;

      while((read_size = bis.read(buf)) != -1){
        bos.write(buf, 0, read_size);
      }

    }catch(IOException e){
      throw new IOException("Error location [Common_Samba:fileOutputCom]" + "\n" + e);
    }finally{
      Files.deleteIfExists(temp_file.toPath());
    }
  }









  /** 
   **********************************************************************************************
   * @brief 指定したファイルサーバー上のファイルを削除する。
   * 
   * @details
   * - 引数として、指定する機能名と、ファイル名（シリアルナンバーや履歴番号）を指定する事で、対象のファイルを
   * 削除することができる。
   * - このメソッドは、通常データと履歴用データで共用で用いることができる。
   *
   * @param[in] dir 削除を指定する機能名
   * @param[in] file_name 削除を指定するファイル名
   * @param[in] ext 削除時に指定する拡張子
   * @param[in] hist 通常データ用のファイルサーバー上のディレクトリ内のファイルを削除する場合は「False」、
   * 履歴用のディレクトリ内のファイルを削除する場合は「True」とする。
   * 
   * @par 処理の大まかな流れ
   * -# 削除対象のファイルパスを生成。
   * -# 事前に作成したファイルサーバーへの接続コンテキストを用いて、接続ストリームを作成。
   * -# 削除対象のファイルが存在するか確認を行い、存在しなければエラーを出す。
   * -# 対象ファイルを削除し、処理を終了する。
   *
   * @throw IOException
   **********************************************************************************************
   */
  public void fileDeleteCom(String dir, String file_name, String ext, Boolean hist) throws IOException{

    String file_path = makePath(dir, file_name, ext, hist);

    try (SmbFile smb = new SmbFile(file_path, sambaSetup())) {

      setTimeout(smb);

      if(!smb.exists()){
        throw new IOException("file not exists");
      }

      smb.delete();

    }catch(IOException e){
      throw new IOException("Error location [Common_Samba:fileDeleteCom]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 指定されたファイルのハッシュ値を計算する。
   * 
   * @details
   * - このメソッドで用いるハッシュ値の計算方法は[SHA512_256Hex]とする。
   * - このメソッドは、通常データと履歴用データで共用で用いることができる。
   * 
   * @return 計算で産出されたハッシュ値の文字列
   *
   * @param[in] data ハッシュ値計算対象のファイル
   *
   * @throw IOException
   **********************************************************************************************
   */
  public String makeHash(File data) throws IOException{

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(data))){
      return DigestUtils.sha512_256Hex(bis);
    }catch(IOException e){
      throw new IOException("Error location [Common_Samba:makeHash]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 指定されたファイルのハッシュ値と、あらかじめ用意されているハッシュ値が一致するか確認する。
   * 
   * @details
   * - 使い方としては、ファイルサーバーから取得したデータの改ざんチェックを行うためである。
   * - 以前にファイルサーバーに保管する際に計算してデータベースに保存しておいてハッシュ値を用いて、
   * 取り出そうとするファイルが改ざんされていないことを確認するためである。
   * 
   * @return 照合した結果の真偽値。一致していれば「True」である。
   *
   * @param[in] target_data ファイルサーバーから取り出してきた検査対象データ
   * @param[in] database_hash データベースにあらかじめ保存されていたハッシュ値
   *
   * @throw IOException
   **********************************************************************************************
   */  
  public Boolean checkHash(File target_data, String database_hash) throws IOException{
    return makeHash(target_data).equals(database_hash);
  }











  /** 
   **********************************************************************************************
   * @brief 指定されたファイルをGZIP圧縮する。
   * 
   * @details
   * - 使い方としては、履歴情報を保存する際にGZIP形式で圧縮して保管する際に用いる。
   * 
   * @return 圧縮後のデータが格納された一時ファイル
   *
   * @param[in] before_file 対象となる圧縮前のファイル
   * 
   * @par 処理の大まかな流れ
   * -# 拡張子を[gz]に指定したうえで、圧縮後ファイルの書き込み用の一時ファイルを作成。
   * -# メモリ枯渇を避けるため、バッファを設けて少しづつ圧縮前ファイルからデータを読み取って、圧縮後
   * ファイルに書き込んでいく。
   * -# エラーが発生すれば、圧縮後ファイルを書き込むための一時ファイルを削除して例外を出す。
   * -# エラーが出る出ないにかかわらず、圧縮前の一時ファイルは削除して、処理を終了する。
   *
   * @throw IOException
   **********************************************************************************************
   */
  public File compress(File before_file) throws IOException{

    String ext = Datatype_Enum.GZIP.getExtension();
    File after_file = makeTmpFile("compress", ext);

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(before_file));
        GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(after_file));
        BufferedOutputStream bos = new BufferedOutputStream(gzip);){

      byte[] buf = new byte[1024];
      int read_size = 0; 

      while((read_size = bis.read(buf)) != -1){
        bos.write(buf, 0, read_size);
      }

    }catch(IOException e){
      Files.deleteIfExists(after_file.toPath());
      throw new IOException("Error location [Common_Samba:compress]" + "\n" + e);
    }finally{
      Files.deleteIfExists(before_file.toPath());
    }
    
    return after_file;
  }









  /** 
   **********************************************************************************************
   * @brief 指定された、GZIP圧縮されたファイルを解凍する。
   * 
   * @details
   * - 使い方としては、ファイルサーバー上の履歴保存用のデータを取り出した後に、解凍して他の場所で用いれる
   * ようにすることである。
   * 
   * @return 解凍が終わったデータを格納した一時ファイル
   *
   * @param[in] before_file 対象となる圧縮後のファイル
   * @param[in] ext 解凍後の一時ファイルに付加する拡張子
   * 
   * @par 処理の大まかな流れ
   * -# 指定された拡張子で、解凍後のデータを書き込む一時ファイルを作成。
   * -# メモリ枯渇を避けるため、バッファを設けて少しづつ解凍前ファイルからデータを読み取って、解凍後
   * ファイルに書き込んでいく。
   * -# エラーが発生すれば、解凍後ファイルを書き込むための一時ファイルを削除して例外を出す。
   * -# エラーが出る出ないにかかわらず、解凍前の一時ファイルは削除して、処理を終了する。
   *
   * @throw IOException
   **********************************************************************************************
   */ 
  public File decompress(File before_file, String ext) throws IOException{

    File after_file = makeTmpFile("decompress", ext);

    try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(after_file));
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(before_file));
        BufferedInputStream bis = new BufferedInputStream(gzip);){

      byte[] buf = new byte[1024];
      int read_size = 0; 

      while((read_size = bis.read(buf)) != -1){
        bos.write(buf, 0, read_size);
      }

    }catch(IOException e){
      Files.deleteIfExists(after_file.toPath());
      throw new IOException("Error location [Common_Samba:decompress]" + "\n" + e);
    }finally{
      Files.deleteIfExists(before_file.toPath());
    }
    
    return after_file;
  }
}
