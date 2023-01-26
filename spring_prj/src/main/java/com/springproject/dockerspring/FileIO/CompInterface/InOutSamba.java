/** 
 **************************************************************************************
 * @file InOutSamba.java
 * @brief バイナリデータのファイルサーバーとの通信で使用する機能を提供したインターフェース
 * を格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CompInterface;

import java.io.File;
import java.io.IOException;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;








/** 
 **************************************************************************************
 * @brief バイナリデータのファイルサーバーとの通信で使用する機能を提供したインターフェース
 * 
 * @details 
 * - 通信対象のファイルサーバーは[samba]とし、それに準じた実装を行う。
 * - ファイルサーバーとの通信のほか、受け取ったバイナリデータのハッシュ値を計算する機能を
 * 設けている。
 **************************************************************************************
 */ 
public interface InOutSamba {


  /** 
   **********************************************************************************************
   * @brief 受け取ったファイルを、ファイルサーバー上にシリアルナンバーで名称をつけて保管する。
   * 
   * @details
   * - このメソッドは、新規追加と更新の両方で共用する。
   * - シリアルナンバーでファイル名をリネームする理由としては、ファイル名を一意にするためである。
   * 他のファイル名を用いるよりは、データベース上で自動的に付与された連番を用いたほうが、より一意にする
   * ことが可能だから。また、ディレクトリトラバーサルの対策にもなる。
   * 
   * @param[in] normal_file ファイルサーバに格納するファイル
   * @param[in] serial_num ファイルサーバーに格納する際にファイル名に付与するシリアルナンバー
   *
   * @throw IOException
   **********************************************************************************************
   */
  void fileInsertUpdate(File normal_file, Integer serial_num) throws IOException;


  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーの、ファイルサーバー上のファイルを削除する。
   * 
   * @details
   * - 削除の場合、削除対象のファイルが存在しない場合はエラーを発する。
   * - 厳密にシステムを管理していれば、本来保存されているはずのファイルが存在しないことはありえないので
   * 存在しないことが判明した時点で何らかの異常があると判断する。
   * 
   * @param[in] serial_num 削除対象のシリアルナンバーのファイル名
   *
   * @throw IOException
   **********************************************************************************************
   */
  void fileDelete(Integer serial_num) throws IOException;


  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーの、ファイルサーバー内のファイルを取得し、事前に取得しておいた
   * ファイルのハッシュ値を照合して、合致した場合のみ返す。
   * 
   * @details
   * - ファイルが存在しない場合や、指定したファイルがディレクトリの場合はエラーとする。
   * - 厳密にシステムを管理していれば、本来保存されているはずのファイルが存在しないことはありえないので
   * 存在しないことが判明した時点で異常と判断する。
   * - ハッシュ値が合致しない場合、ファイルが改ざんされたとみなされるため、エラーとなる。
   *
   * @return ファイルサーバーから読み取ったファイル 
   *
   * @param[in] serial_num 指定するファイルサーバー内のファイルのシリアルナンバー
   * @param[in] compare_hash 改ざん検知の為、照合するハッシュ値
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  File fileOutput(Integer serial_num, String compare_hash) throws IOException, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief 受け取ったファイルを、ファイルサーバー上に履歴番号で名称をつけて保管する。
   * 保存時には、GZIP形式にファイルを圧縮して保管する。
   * 
   * @details
   * - このメソッドは、新規追加と更新の両方で使用可能だが、そもそも履歴は新規追加しか行わないため
   * あまり関係ない。
   * - 履歴番号でファイル名をリネームする理由としては、ファイル名を一意にするためである。
   * 他のファイル名を用いるよりは、データベース上で自動的に付与された連番を用いたほうが、より一意にする
   * ことが可能だから。また、ディレクトリトラバーサルの対策にもなる。
   * 
   * @param[in] hist_file ファイルサーバに格納するファイル
   * @param[in] history_id ファイルサーバーに格納する際にファイル名に付与する履歴番号
   *
   * @throw IOException
   **********************************************************************************************
   */
  void historyFileInsert(File hist_file, Integer history_id) throws IOException;


  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号の、ファイルサーバー上のファイルを削除する。
   * 
   * @details
   * - 削除の場合、削除対象のファイルが存在しない場合はエラーを発する。
   * - 厳密にシステムを管理していれば、本来保存されているはずのファイルが存在しないことはありえないので
   * 存在しないことが判明した時点で何らかの異常があると判断する。
   * 
   * @param[in] serial_num 削除対象の履歴番号のファイル名
   *
   * @throw IOException
   **********************************************************************************************
   */
  void historyFileDelete(Integer history_id) throws IOException;


  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号の、ファイルサーバー内のファイルを取得し解凍後、事前に取得しておいた
   * ファイルのハッシュ値を照合して、合致した場合のみ返す。
   * 
   * @details
   * - ファイルが存在しない場合や、指定したファイルがディレクトリの場合はエラーとする。
   * - 厳密にシステムを管理していれば、本来保存されているはずのファイルが存在しないことはありえないので
   * 存在しないことが判明した時点で異常と判断する。
   * - ハッシュ値が合致しない場合、ファイルが改ざんされたとみなされるため、エラーとなる。
   *
   * @return ファイルサーバーから読み取ったファイル 
   *
   * @param[in] history_id 指定するファイルサーバー内のファイルの履歴番号
   * @param[in] compare_hash 改ざん検知の為、照合するハッシュ値
   *
   *@note ここで照合するハッシュ値は「解凍後」のものである。つまり照合は解凍後に行わないと意味がない。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  File historyFileOutput(Integer history_id, String compare_hash) throws IOException, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief 指定されたファイルのハッシュ値を計算し、ハッシュ文字を返す。
   * 
   * @details
   * - このメソッドは、ファイルサーバー機能以外にもサービスの各所で用いる。
   * - このメソッドをわざわざ公開している理由としては、ファイルサーバーへの保存の前にハッシュ値を計算
   * してデータベースにその値を格納する必要があるため、ここだけでは完結できないからである。
   *
   * @return 計算されたハッシュ値 
   *
   * @param[in] target_data ハッシュ計算対象のファイル
   *
   * @throw IOException
   **********************************************************************************************
   */
  String makeHash(File target_data) throws IOException;
}