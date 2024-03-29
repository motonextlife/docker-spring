/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface
 * 
 * @brief ファイルサーバーへの入出力単体テストで用いる、インターフェースを格納する。
 * 
 * @details
 * - このパッケージは、テストメソッドの名称を統一する目的で設けたインターフェースを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.SambaProcess_Test.TestInterface;





/** 
 **************************************************************************************
 * @file SambaProcess_Compress_Test.java
 * 
 * @brief ファイルサーバーへの履歴情報の保存時に行うデータ圧縮＆解凍機能のテストにおいて、共通で
 * 使用するメソッドの提供を行うインターフェースを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;








/** 
 **************************************************************************************
 * @brief ファイルサーバーへの履歴情報の保存時に行うデータ圧縮＆解凍機能のテストにおいて、共通で
 * 使用するメソッドの提供を行うインターフェース
 **************************************************************************************
 */ 
public interface SambaProcess_Compress_Test{


  /** 
   ****************************************************************************************
   * @brief バイナリデータの圧縮＆解凍処理において、正常に動作することを確認する。圧縮確認に関しては、
   * 圧縮前と圧縮後でデータ量が明らかに減っていることを確認し、解凍確認に関しては、圧縮前のデータと
   * 解凍後のデータをハッシュ化して比較し、同一に復元できている事を確認する。
   * 
   * @throw IOException
   ****************************************************************************************
   */ 
  void バイナリデータの圧縮と解凍機能の動作確認() throws IOException;
}
