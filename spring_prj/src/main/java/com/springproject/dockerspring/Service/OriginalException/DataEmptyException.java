/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Service.OriginalException
 * 
 * @brief サービスクラスの中で用いる、独自実装の例外クラスを格納するパッケージ
 * 
 * @details
 * - このパッケージは、検索して取得しようとしたデータが存在しなかったり、データの操作処理に
 * エラーがあった場合などに投げる例外を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.OriginalException;






/** 
 **************************************************************************************
 * @file DataEmptyException.java
 * @brief データを取得しようとした際に、該当のデータを得られなかった場合に発する例外を
 * 格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 








/** 
 **************************************************************************************
 * @brief データを取得しようとした際に、該当のデータを得られなかった場合に発する例外
 * 
 * @details 
 * - この例外を発する状況としては、データの検索時に取得できたデータ数が0件だった場合である。
 * - この例外は、主にサービスクラス以上の上流のクラスで用いる。
 **************************************************************************************
 */ 
public class DataEmptyException extends Exception{
  private static final long serialVersionUID = 1L;
}