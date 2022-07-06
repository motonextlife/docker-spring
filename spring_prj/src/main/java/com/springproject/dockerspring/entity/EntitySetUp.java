package com.springproject.dockerspring.entity;

import java.util.Base64;
import java.util.Map;
import java.util.zip.DataFormatException;

import com.springproject.dockerspring.component.DatatypeEnum;




/**************************************************************/
/*   [EntitySetUp]
/*   エンティティ内のデータに対する初期化や変換などの
/*   他の機能でデータを使用するうえでの下処理を提供する。
/**************************************************************/

public interface EntitySetUp{

  /***************************************************************/
  /*   [stringSetNull]
  /*   呼び出された時点で、変数内に格納されている「文字列型」の
  /*   データが「空文字又は空白のみ」の場合、「Null」に置き換える。
  /*   これは、空白や空文字がデータベース内に入り込み、参照性制約違反
  /*   などの不具合が発生しないよう、確実に空データに「Null」を
  /*   入れるための処置である。
  /***************************************************************/
  void stringSetNull();


  /***************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/
  Map<String, String> makeMap() throws DataFormatException;

  
  /***************************************************************/
  /*   [base64encode]
  /*   指示されたデータに、Base64形式への変換を行う。
  /*   なお、このメソッドに関してのみ、実装先での変更が生じないので
  /*   デフォルトメソッドでの定義とする。
  /***************************************************************/
  default public String base64encode(byte[] bytedata, DatatypeEnum content){

    if(bytedata.length == 0){
      return "";
    }else{
      String base64 = Base64.getEncoder().encodeToString(bytedata);
      StringBuilder sb = new StringBuilder();

      try{
        switch(content){
          case PHOTO:
            sb.append("data:image/png;base64,");  //変換先は「PNG」とする。
            break;
          case MOVIE:
            sb.append("data:video/mpeg;base64,");  //変換先は「MP4」とする。
            break;
          case AUDIO:
            sb.append("data:audio/wav;base64,");  //変換先は「WAVE」とする。
            break;
          case PDF:
            sb.append("data:application/pdf;base64,");  //変換先は「PDF」とする。
            break;
          default:
            throw new IllegalStateException();
        }
      }catch(IllegalStateException e){
        throw new IllegalStateException("Error location [EntitySetUp:base64encode]");
      }

      sb.append(base64);
      return sb.toString();
    }
  }
}