/** 
 **************************************************************************************
 * @file InOutZip.java
 * @brief バイナリデータの圧縮ファイルを入出力する機能を提供するインターフェースを格納
 * したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CompInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;

import net.lingala.zip4j.exception.ZipException;








/** 
 **************************************************************************************
 * @brief バイナリデータの圧縮ファイルを入出力する機能を提供するインターフェース
 * 
 * @details 
 * - 保存済みのバイナリデータを複数格納した圧縮ファイルの作成や、入力された圧縮ファイルの
 * 解凍を行う。
 **************************************************************************************
 */ 
public interface InOutZip {


  /** 
   **********************************************************************************************
   * @brief 受け取った圧縮ファイルを、データの正当性検査を確認しながら解凍する。
   * 
   * @details
   * - 解凍時に、解凍したデータが規定値より多かったり、形式が違う場合はエラーとする。
   * - なお、要件とZIP爆弾の対策から、解凍は再帰的には行わないようにする。
   * - ディレクトリは読み取らない。
   * - なお、暗号化されたファイルは受付対象外である。
   * 
   * @return 圧縮ファイルから抽出したファイルを格納したエンティティ
   * 
   * @param[in] zip_file ファイルサーバに格納するファイル
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  List<FileEntity> extractZip(MultipartFile zip_file) throws IOException, ZipException, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief ファイルサーバーに保存しているファイルを取り出し、ZIPファイルに格納して出力する。
   * 
   * @details
   * - ファイルサーバーからファイルを取り出す処理は、サービス側から関数型インターフェースで処理を
   * 受け取り、その中で行う。これによって、ZIPの処理とファイルサーバーの処理を明確に分ける。
   * - ファイルサーバーからのファイル取得の際にも、データの正当性チェックを行い、不正であれば
   * エラーを出す。
   * 
   * @return 生成した圧縮ファイル
   * 
   * @param[in] samba_file_generate ファイルサーバーからファイルを取り出す処理の関数
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  File outputZip(Supplier<List<FileEntity>> samba_file_generate) throws ZipException, IOException, InputFileDifferException;
}