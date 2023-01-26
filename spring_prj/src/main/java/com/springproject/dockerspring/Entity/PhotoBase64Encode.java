/** 
 **************************************************************************************
 * @file PhotoBase64Encode.java
 * @brief 画像データを取り扱う機能において、画像データを出力データに変換する際に
 * Base64形式として変換するメソッドを定義するクラスを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity;

import java.util.Base64;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;







/** 
 **************************************************************************************
 * @brief 画像データを取り扱う機能において、画像データを出力データに変換する際に
 * Base64形式として変換するメソッドを定義するユーティリティクラスである。
 * 
 * @note この機能に関しては、エンティティのパッケージのみならず、他のパッケージでも使用する。
 * なお、このクラスはインスタンスの作成を禁止する。
 **************************************************************************************
 */ 
public class PhotoBase64Encode{

  private PhotoBase64Encode(){
    throw new AssertionError();
  }

  



  
  /** 
   **********************************************************************************************
   * @brief 引数として渡された画像データのバイト配列を、Base64形式文字列に変換する。
   * 
   * @details
   * - 変換後のデータ形式としては、「PNG」を定義する。
   * - バイト配列が長さが0の場合は、空文字が返される。
   * 
   * @param[in] byte_data 変換対象となるバイト配列
   * 
   * @return Base64形式へ変換された文字列
   * 
   * @par 処理の大まかな流れ
   * -# バイト配列をBase64形式文字列に変換。
   * -# 変換後、ブラウザで表示できるように宣言文字列（data:image/png;base64,)を付加する。
   **********************************************************************************************
   */
  public static String encode(byte[] byte_data){

    if(byte_data.length == 0){
      return "";
    }else{
      String base64 = Base64.getEncoder().encodeToString(byte_data);
      StringBuilder sb = new StringBuilder();

      sb.append("data:" + Datatype_Enum.PHOTO.getMimetype() + ";base64,");
      sb.append(base64);

      return sb.toString();
    }
  }
}