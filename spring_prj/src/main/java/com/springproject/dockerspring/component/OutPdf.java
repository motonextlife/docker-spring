package com.springproject.dockerspring.component;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Base64;



/**************************************************************/
/*   [OutPdf]
/*   PDFの出力に関する機能の提供を行う。
/**************************************************************/
public interface OutPdf<T>{

  /***************************************************************/
  /*   [makePdf]
  /*   エンティティ内のデータを、PDFとして出力するために、
  /*   出力用ストリームを生成して、PDF生成用クラスにデータを渡し、
  /*   返ってきた作成済みデータを出力する。
  /***************************************************************/
  String makePdf(T entity) throws IOException;

  /********************************************************************/
  /*   [designPdf]
  /*   エンティティから受け取ったデータを用いてPDFデータをデザインする。
  /********************************************************************/
  void designPdf(BufferedOutputStream out) throws IOException;

  /***************************************************************/
  /*   [pdfBase64Encode]
  /*   PDFデータの、Base64へのエンコードを行う。
  /*   なお、このメソッドは実装先での変更が生じないので
  /*   デフォルトメソッドでの定義とする。
  /***************************************************************/
  default public String pdfBase64Encode(byte[] bytedata){

    if(bytedata.length == 0){
      return "";
    }else{
      return Base64.getEncoder().encodeToString(bytedata);
    }
  }
}