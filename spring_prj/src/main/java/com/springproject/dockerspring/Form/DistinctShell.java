/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form
 * 
 * @brief このシステムにユーザーから渡されてくるデータをバリデーションする機能を格納したパッケージ
 * 
 * @details
 * - このパッケージでは、ユーザーからのデータ入力時のデータのバリデーション機能や、ユーザー当てに
 * データを出力する際のバリデーションの機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form;






/** 
 **************************************************************************************
 * @file DistinctShell.java
 * @brief CSVファイルの管理番号の重複チェックの際に使用するシェルスクリプトを作成し実行する
 * クラスを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;








/** 
 **************************************************************************************
 * @brief CSVファイルの管理番号の重複チェックの際に使用するシェルスクリプトを作成し実行する
 * クラス
 * 
 * @details 
 * - このクラスを設ける理由としては、重複チェックの際にメモリ枯渇が起きるのを防ぐため、重複チェックの
 * 処理を、Linux側に委託するためである。
 * - 通常の重複判定の場合、良く用いられる手法としてはコレクションを用いた手法だが、その手法は
 * データをすべてメモリにため込む必要があり、何万件もの処理を想定しているCSVの管理番号重複チェックでは
 * メモリリークが発生する恐れがあるため。
 * - かといってプログラマ自身がローカルファイルなどに管理番号のリストをテキストファイルなどに書きだして
 * 重複チェックのアルゴリズムを実装し行う方法は、全探索が必須となるため処理時間が膨大になる恐れがある。
 * - シェルスクリプトの[uniq]コマンドで行うことで、メモリ制限は実質無制限になり、プログラマが
 * 全探索アルゴリズムを実装する必要もないので、処理時間も少なくできる。
 * 
 * @note ここで作成するシェルスクリプトは[Linux]上でしか動作しない、動作環境依存の物である。
 * だが、現状の要件ではwindows等の他の環境では動かす予定はないので、それで良しとする。
 * 
 * @par 使用アノテーション
 * - @Component
 **************************************************************************************
 */ 
@Component
public class DistinctShell {

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());







  /** 
   **********************************************************************************************
   * @brief 重複チェック対象となる管理番号を書きだす先の一時テキストファイルを作成する。
   * 
   * @details
   * - テキストファイルに書き出し、そのファイルをシェルスクリプトの処理対象ファイルとすることで、重複チェック
   * 処理を可能にする。
   * - このファイルに関しては、実行権限を剥奪しておく。
   *
   * @return 生成した一時ファイル 
   *
   * @param[in] prefix ファイル生成時に、ファイル名に付加する名前
   *
   * @throw IOException
   **********************************************************************************************
   */
  public File makeNumberTmpFile(String prefix) throws IOException {

    FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                    .asFileAttribute(PosixFilePermissions
                                                    .fromString(File_Path_Enum.Permission.getPath()));

    try{
      Path tmp_txt_path = Files.createTempFile(this.specify_dir, prefix, ".txt", perms);

      return tmp_txt_path.toFile();

    }catch(IOException e){
      throw new IOException("Error location [DistinctShell:makeNumberTmpFile]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 処理対象の重複チェック処理を行うシェルスクリプトファイルを作成し、判定を行う。
   * 
   * @details
   * - シェルスクリプトは作成した後放置は危ないので、その都度スクリプトを生成して一時ファイルとして作成。
   * - このファイルに関しては、実行権限は付与しておく。
   *
   * @return 判定結果 
   *
   * @param[in] prefix ファイル生成時に、ファイル名に付加する名前
   * @param[in] txt_file 検査対象のテキストファイル
   * 
   * @par 大まかな処理の流れ
   * -# まず実行前に、現在このシステムが稼働しているOSの種類を確認する。[Linux]でない場合、エラーとする。
   * -# シェルスクリプト用の一時ファイルを、実行権限を付与した状態で作成する。
   * -# 作成した一時ファイルにスクリプトを書き込む。スクリプトの内容としては以下のようになる。
   *    - シェルを実行するディレクトリを、現在スクリプトファイルが存在するカレントディレクトリに設定。
   *    - 処理するテキストファイルを指定して、中身をソートしたうえで、重複した値を出力する。
   * -# 作成したシェルスクリプトを実行し、実行結果の入力ストリームを開く。
   * -# 実行結果を読み取って、結果が全くないことを確認する。一つでも存在すれば、重複が存在することになるので
   * 判定結果を不合格にする。
   * -# 使用したテキストファイルとシェルスクリプトファイルを削除し、判定結果を返す。
   *
   * @throw IOException
   **********************************************************************************************
   */
  public Boolean execShell(String prefix, File txt_file) throws IOException {

    Boolean judge = true;
    File tmp_sh = null;

    FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                    .asFileAttribute(PosixFilePermissions
                                                    .fromString("rwxrwxr--"));

    try{
      String os_name = System.getProperty("os.name").toLowerCase();
      if(!os_name.startsWith("linux")){
        throw new IOException("non executable OS");
      }

      tmp_sh = Files.createTempFile(this.specify_dir, prefix, ".sh", perms).toFile();
      String file_name = tmp_sh.getName();

      try(BufferedWriter bw = new BufferedWriter(new FileWriter(tmp_sh));
          PrintWriter pw = new PrintWriter(bw);){

        pw.println("#!/bin/sh");
        pw.println("cd `dirname $0`");
        pw.println(String.format("sort %s | uniq -d", file_name));
      }

      ProcessBuilder process_build = new ProcessBuilder().command(tmp_sh.getAbsolutePath());
      Process process = process_build.start();

      try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))){

        while(br.readLine() != null){
          judge = false;
          break;
        }
      }
      
      return judge;

    }catch(IOException e){
      throw new IOException("Error location [DistinctShell:execShell]" + "\n" + e);
    }finally{
      Files.deleteIfExists(txt_file.toPath());

      if(tmp_sh != null){
        Files.deleteIfExists(tmp_sh.toPath());
      }
    }
  }
}