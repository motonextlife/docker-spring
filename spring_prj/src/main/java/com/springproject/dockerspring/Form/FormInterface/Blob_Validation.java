/** 
 **************************************************************************************
 * @file Blob_Validation.java
 * @brief 共通の入力時のバイナリデータのバリデーションの際に用いる機能を提供を行う
 * インターフェースを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.FormInterface;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;









/** 
 **************************************************************************************
 * @brief 共通の入力時のバイナリデータのバリデーションの際に用いる機能を提供を行う
 * インターフェース
 * 
 * @details 
 * - このインターフェースは、主にバイナリデータの正当性のチェックや、そのチェックをまとめて順次
 * 処理をするための機能を提供する。
 * - この機能では、主にZIPファイルから抽出したバイナリデータの検査や、出力時のバイナリデータの
 * バリデーションの際に用いる。
 * 
 * @see FileEntity
 **************************************************************************************
 */ 
public interface Blob_Validation {


  /** 
   **********************************************************************************************
   * @brief 受け取ったバイナリデータのファイルのリスト内の、データの正当性を確認する。
   * 
   * @details
   * - この機能で検査する項目としては、主にファイル名のXSSに対する安全性の確認や、拡張子＆MIMEタイプの
   * チェック、データサイズの確認などを行う。
   * - なお、このメソッドでは、検査対象のデータを取り出し検査用のメソッドにデータを渡すだけである。
   * 実際のチェックは[sequentialCheck]に、順次処理でゆだねる。
   * 
   * @param[in] input_list 検査対象のデータが入った専用エンティティのリスト
   * 
   * @see FileEntity
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  void blobChecker(List<FileEntity> input_list) throws InputFileDifferException, IOException;


  /** 
   **********************************************************************************************
   * @brief 実際にチェック処理を順次処理で行う。
   * 
   * @details
   * - この機能で検査する項目としては、主にファイル名のXSSに対する安全性の確認や、拡張子＆MIMEタイプの
   * チェック、データサイズの確認などを行う。
   * - 検査対象のファイル名と、ファイル本体を渡すことで、このシステムが受け入れ可能なデータかどうかを判断。
   * - なお、少しでもフロント側へのレスポンスを早くするため、一つでもエラーが発生した後続の検査処理は
   * 行わず、直ちに例外を発生させ処理を中断する。
   * 
   * @param[in] filename 検査対象のファイル名
   * @param[in] file 検査対象のファイルの本体
   * 
   * @note ファイル本体と、ファイル名が分離している理由としては、ファイルサーバーやローカルファイルに
   * 保存する際のディレクトリトラバーサル攻撃のリスクを下げるため、分離してデータベースとファイルサーバーに
   * データを保管しているためである。
   * 
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  void sequentialCheck(String filename, File file) throws InputFileDifferException, IOException;
}
