package com.springproject.dockerspring.entity;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**************************************************************/
/*   [CompressBlob]
/*   履歴情報に保存するバイナリデータを圧縮や解凍する機能
/*   を提供する。
/**************************************************************/

public interface CompressBlob{

  /***************************************************************/
  /*   [getBlobDecompress]
  /*   取得したバイト配列を解凍した物を、呼び出し元に返す。
  /***************************************************************/
  byte[] getBlobDecompress() throws DataFormatException;



  /***************************************************************/
  /*   [compress]
  /*   渡されたバイトデータを圧縮して、その結果を返す。
  /***************************************************************/
  default public byte[] compress(byte[] beforebyte){

    Deflater compress = new Deflater(Deflater.BEST_COMPRESSION);
    compress.setInput(beforebyte);
    compress.finish();

    byte[] output = new byte[100];
    compress.deflate(output);
    compress.end();

    return output;
  }



  /***************************************************************/
  /*   [decompress]
  /*   渡されたバイトデータを解凍して、その結果を返す。
  /***************************************************************/
  default public byte[] decompress(byte[] beforebyte, int beforelength) throws DataFormatException{

    Inflater decompress = new Inflater();
    decompress.setInput(beforebyte, 0, beforebyte.length);

    byte[] output = new byte[beforelength];
    decompress.inflate(output);
    decompress.end();

    return output;
  }
}