/** 
 **************************************************************************************
 * @file TestSambaSetUp.java
 * @brief テストコード全体において、ファイルサーバーを使用可能なように接続情報などを定義して、
 * 初期化をするクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Configure;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;








/** 
 **************************************************************************************
 * @brief テストコード全体において、ファイルサーバーを使用可能なように接続情報などを定義して、
 * 初期化をするクラスを格納するファイル。
 * 
 * @details 
 * - ファイルサーバーへのアクセスのための接続情報を、インスタンスの作成の際に定義する。
 * - 前述の接続情報を元に、ファイルサーバーへの初期化データの挿入を行う。
 * - テストが終わった後は、使用したデータの削除を行い、ファイルサーバーをリセットする。
 * - なお、このクラスは通常データ用と履歴データ用で共用で用いる。
 * 
 * @par 使用アノテーション
 * - @Component
 **************************************************************************************
 */ 
@Component
public class TestSambaSetUp {

  private final CIFSContext connect_context;
  private final String base_file_path;







  /** 
   **********************************************************************************************
   * @brief ファイルサーバーへの接続情報の定義を行う。
   * 
   * @details
   * - 環境変数に指定されたURLやバージョン情報をもとに定義する。
   * - また、ファイルサーバー内の基本となるファイルパスを、環境変数から取得し定義する。
   * 
   * @param[in] data_source テスト用の環境変数設定ファイル。
   * 
   * @par 処理の大まかな流れ
   * -# 接続情報のバージョン情報に、ファイルサーバーの最小と最大のバージョン番号を設定。
   * -# 基本コンテキストを作成し、ファイルサーバーへのアクセスのための、ユーザー名とパスワードを登録する。
   * -# 作成した認証情報を元に接続コンテキストを作成し、フィールド変数に格納。
   * -# 環境変数からファイルサーバー上のファイルパスを取得し、フィールド変数に格納。
   * 
   * @throw CIFSException
   **********************************************************************************************
   */ 
  @Autowired
  public TestSambaSetUp(TestDataSource data_source) throws CIFSException{

    Properties prop = new Properties();
    prop.setProperty("jcifs.smb.client.minVersion", data_source.getSamba_min_ver());
    prop.setProperty("jcifs.smb.client.maxVersion", data_source.getSamba_max_ver());

    BaseContext bc = new BaseContext(new PropertyConfiguration(prop));

    String username = data_source.getSamba_username();
    String password = data_source.getSamba_password();
    NtlmPasswordAuthenticator auth = new NtlmPasswordAuthenticator(username, password);

    this.connect_context = bc.withCredentials(auth);
    this.base_file_path = data_source.getSamba_url() + "/";
  }




  




  /** 
   **********************************************************************************************
   * @brief 通常データのファイルサーバーへの初期値データの挿入を行う。
   * 
   * @details
   * - 指定されたテストケース内のファイルを読み取って、ファイルサーバーに格納する。
   * - 格納したデータはハッシュ値を計算し、戻り値とする。
   * 
   * @param[in] read_file_name テストケースから読み取るデータのファイル名
   * @param[in] output_file_path ファイルサーバー上の書き込み対象のファイル名。
   * 
   * @par 処理の大まかな流れ
   * -# テストケースから読み取るバイナリデータのファイル名を指定し、入力ストリームを開く。
   * -# あらかじめ作成された接続情報と、接続先のファイルサーバー上のファイル名を指定して、出力ストリームを開く。
   * -# データの格納の前に、読み取ったデータのハッシュ値を計算し、保存しておく。
   * -# 読み取ったテストケースのデータを、ファイルサーバーに順次書き込んでいく。
   * -# 保存しておいたハッシュ値を戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public String sambaOutput(String read_file_name, String output_file_path) throws IOException{

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(read_file_name));
        SmbFile smb = new SmbFile(this.base_file_path + output_file_path, this.connect_context);
        BufferedOutputStream bos = new BufferedOutputStream(smb.openOutputStream());){

      byte[] buf = new byte[1024];
      int readsize = 0;

      while((readsize = bis.read(buf)) != -1){
        bos.write(buf, 0, readsize);
      }

      return DigestUtils.sha512_256Hex(bis);
    }
  }








  /** 
   **********************************************************************************************
   * @brief 履歴データのファイルサーバーへの初期値データの挿入を行う。
   * 
   * @details
   * - 指定されたテストケース内のファイルを読み取って、ファイルサーバーに格納する。
   * - 格納するデータは、GZIP圧縮して保存する。
   * - 格納するデータは、圧縮前にハッシュ値を計算し、戻り値とする。
   * 
   * @param[in] read_file_name テストケースから読み取るデータのファイル名
   * @param[in] output_file_path ファイルサーバー上の書き込み対象のファイル名。
   * 
   * @par 処理の大まかな流れ
   * -# テストケースから読み取るバイナリデータのファイル名を指定し、入力ストリームを開く。
   * -# あらかじめ作成された接続情報と、接続先のファイルサーバー上のファイル名を指定して、出力ストリームを開く。
   * -# データの格納の前に、読み取ったデータのハッシュ値を計算し、保存しておく。
   * -# 読み取ったテストケースのデータを、ファイルサーバーに「GZIP圧縮しながら」順次書き込んでいく。
   * -# 保存しておいたハッシュ値を戻り値とする。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public String sambaHistoryOutput(String read_file_name, String output_file_path) throws IOException{

    try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(read_file_name));
        SmbFile smb = new SmbFile(this.base_file_path + output_file_path, this.connect_context);
        GZIPOutputStream gzip = new GZIPOutputStream(smb.openOutputStream());
        BufferedOutputStream bos = new BufferedOutputStream(gzip);){

      byte[] buf = new byte[1024];
      int readsize = 0;

      while((readsize = bis.read(buf)) != -1){
        bos.write(buf, 0, readsize);
      }

      return DigestUtils.sha512_256Hex(bis);
    }
  }









  /** 
   **********************************************************************************************
   * @brief ファイルサーバーに格納されているテスト用のデータを削除し、初期化する。
   * 
   * @details
   * - 指定されたファイルサーバー上のディレクトリ内を、全て削除する。
   * 
   * @param[in] delete_dir_path ファイルサーバー上の削除対象のディレクトリパス名。
   * 
   * @par 処理の大まかな流れ
   * -# 削除対象のディレクトリを指定して、あらかじめ作成した接続情報を用いて接続する。
   * -# 削除対象のディレクトリ内のファイルの一覧を取得する。
   * -# 一覧のファイルをすべて削除する。
   * 
   * @throw IOException
   **********************************************************************************************
   */ 
  public void sambaClear(String delete_dir_path) throws IOException{

    try (SmbFile smb = new SmbFile(this.base_file_path + delete_dir_path, this.connect_context)) {

      SmbFile[] list = smb.listFiles();

      for(SmbFile item: list){
        item.delete();
      }
    }
  }
}
