/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.FileIO.SambaProcess
 * 
 * @brief ファイル入出力機能のうち、[ファイルサーバーへの入出力]に関係する処理を格納したパッケージ
 * 
 * @details
 * - このパッケージは、外部に接続しているファイルサーバーへのデータの格納や、取り出し、削除を
 * 行う機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.SambaProcess;






/** 
 **************************************************************************************
 * @file Audio_Data_SambaImpl.java
 * @brief 主に[音源データ管理]機能で使用する、ファイルサーバー通信に関する機能を実装するクラスを
 * 格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Audio_Data_Samba;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]機能で使用する、ファイルサーバー通信に関する機能を実装するクラス
 * 
 * @details 
 * - 通信対象のファイルサーバーの種類は、[samba]とする。
 * - 処理の大部分は、このクラスにDIする共通処理クラスに任せることで、こちらの特定クラスの
 * 実装内容を大幅に減らしている。
 * - このクラスで用いる環境変数のインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * - ファイルサーバーを操作するライブラリに関しては、[jcifs-ng]を用いる。これによって、
 * セキュリティを確保しつつファイルサーバーへの通信を行うことが可能になる。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Audio_Data_Samba
 * @see Common_Samba
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Audio_Data_SambaImpl implements Audio_Data_Samba{

  private final Common_Samba common_samba;









  /** 
   **********************************************************************************************
   * @brief 指定された通常データのファイルを、ファイルサーバーに書き込む。
   * 
   * @details
   * - 引数に保存対象のファイルと、ファイル名としてシリアルナンバーを指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[wav]のみとする。
   * - このメソッドで対応できるデータは通常データのみとして、履歴データへは別のメソッドで対応する。
   *
   * @param[in] file 保存対象のファイル
   * @param[in] serial_num 指定するファイルのシリアルナンバー
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[fileOutputCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void fileInsertUpdate(File file, Integer serial_num) throws IOException {
    
    String filename = String.valueOf(serial_num);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.AUDIO.getExtension();

    this.common_samba.fileOutputCom(dir, filename, ext, file, false);
  }









  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーのデータを削除する。
   * 
   * @details
   * - ファイル名としてシリアルナンバーを指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[wav]のみとする。
   * - このメソッドで対応できるデータは通常データのみとして、履歴データへは別のメソッドで対応する。
   *
   * @param[in] file 保存対象のファイル
   * @param[in] serial_num 指定するファイルのシリアルナンバー
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[fileDeleteCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void fileDelete(Integer serial_num) throws IOException {

    String filename = String.valueOf(serial_num);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.AUDIO.getExtension();
    
    this.common_samba.fileDeleteCom(dir, filename, ext, false);
  }










  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーのファイルを読み込み、出力する。
   * 
   * @details
   * - ファイル名としてシリアルナンバーを指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[wav]のみとする。
   * - このメソッドで対応できるデータは通常データのみとして、履歴データへは別のメソッドで対応する。
   * - 引数にあらかじめデータベースに保存されているデータのハッシュ値と、読み取ったファイルのハッシュ値を
   * 照合することで、ファイルの改ざんを検知することができる。
   * - なお、改ざんを検知したり、エラーが発生した場合は読取時に使用した一時ファイルを削除したうえで処理を
   * 終了する。
   *
   * @param[in] file 保存対象のファイル
   * @param[in] serial_num 指定するファイルのシリアルナンバー
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[fileInputCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public File fileOutput(Integer serial_num, String hash) throws IOException, InputFileDifferException {

    String filename = String.valueOf(serial_num);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.AUDIO.getExtension();

    File temp_file = this.common_samba.fileInputCom(dir, filename, ext, false);

    if(!this.common_samba.checkHash(temp_file, hash)){
      Files.deleteIfExists(temp_file.toPath());
      throw new InputFileDifferException("tampering occurred");
    }

    return temp_file;
  }











  /** 
   **********************************************************************************************
   * @brief 指定された履歴データのファイルを、ファイルサーバーに書き込む。
   * 
   * @details
   * - 引数に保存対象のファイルと、ファイル名として履歴番号を指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[gz]のみとする。
   * - このメソッドで対応できるデータは履歴データのみとして、通常データへは別のメソッドで対応する。
   * - なお、保存の際にはデータをGZIP圧縮したうえで格納する。
   *
   * @param[in] file 保存対象のファイル
   * @param[in] history_id 指定するファイルの履歴番号
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[fileOutputCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void historyFileInsert(File file, Integer history_id) throws IOException {

    String filename = String.valueOf(history_id);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.GZIP.getExtension();

    File compress_file = this.common_samba.compress(file);
    this.common_samba.fileOutputCom(dir, filename, ext, compress_file, true);
  }










  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号のデータを削除する。
   * 
   * @details
   * - ファイル名として履歴番号を指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[gz]のみとする。
   * - このメソッドで対応できるデータは履歴データのみとして、通常データへは別のメソッドで対応する。
   *
   * @param[in] history_id 指定するファイルの履歴番号
   * 
   * @note このメソッドで使用する共通クラスの共通メソッドは[fileDeleteCom]である。詳しい処理内容は
   * そちらを参照の事。
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public void historyFileDelete(Integer history_id) throws IOException {

    String filename = String.valueOf(history_id);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.GZIP.getExtension();

    this.common_samba.fileDeleteCom(dir, filename, ext, true);
  }










  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号のファイルを読み込み、出力する。
   * 
   * @details
   * - ファイル名として履歴番号を指定する。
   * - この機能においては、ファイルパスを組み立てる際に用いる拡張子は、[gz]のみとする。
   * - このメソッドで対応できるデータは履歴データのみとして、通常データへは別のメソッドで対応する。
   * - 引数にあらかじめデータベースに保存されているデータのハッシュ値と、読み取ったファイルのハッシュ値を
   * 照合することで、ファイルの改ざんを検知することができる。
   * - なお、改ざんを検知したり、エラーが発生した場合は読取時に使用した一時ファイルを削除したうえで処理を
   * 終了する。
   * - データを取り出す際は、GZIP解凍したうえで取り出す。
   *
   * @param[in] file 保存対象のファイル
   * @param[in] serial_num 指定するファイルのシリアルナンバー
   * 
   * @note 
   * - このメソッドで使用する共通クラスの共通メソッドは[fileInputCom]である。詳しい処理内容はそちらを参照の事。
   * - 照合するハッシュ値は、「圧縮前の値」となる。そのため、ハッシュ値照合は解凍後に行う。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public File historyFileOutput(Integer history_id, String hash) throws IOException, InputFileDifferException {

    String filename = String.valueOf(history_id);
    String dir = File_Path_Enum.Audio_Data.getPath();
    String ext = Datatype_Enum.AUDIO.getExtension();
    String zip_ext = Datatype_Enum.GZIP.getExtension();

    File data = this.common_samba.fileInputCom(dir, filename, zip_ext, true);
    File decompress = this.common_samba.decompress(data, ext);

    if(!this.common_samba.checkHash(decompress, hash)){
      Files.deleteIfExists(decompress.toPath());
      throw new InputFileDifferException("tampering occurred");
    }

    return decompress;
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
   * @note このメソッドを、共通クラスから呼び出してわざわざ公開する理由としては、ファイルサーバーへの保存前に
   * ハッシュ値を計算してデータベースに格納するので、サービス側でハッシュ計算を実行する必要があるからである。
   *
   * @throw IOException
   **********************************************************************************************
   */
  @Override
  public String makeHash(File target_data) throws IOException{
    return this.common_samba.makeHash(target_data);
  }
}
