package com.springproject.dockerspring.component;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;

import net.lingala.zip4j.exception.ZipException;



/***************************************************************************/
/*   [InOutZip]
/*   バイナリデータのZIP形式による出力や入力を提供する。
/***************************************************************************/
public interface InOutZip<T> {

  /*************************************************************/
  /*   [extractZip]
  /*   読み込んだZIPファイルからデータを抽出し、マップリストに
  /*   ファイル名とファイルデータを保存する。
  /*************************************************************/
  Map<String, byte[]> extractZip(MultipartFile zipfile) throws IOException, ZipException, InputFileDifferException;


  /*************************************************************/
  /*   [zipToEntitys]
  /*   正常なデータが入っているマップリストのデータを、
  /*   対象のエンティティに移し替える。
  /*************************************************************/
  Iterable<T> zipToEntitys(Map<String, byte[]> map, String id);


  /*************************************************************/
  /*   [outputZip]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  String outputZip(Iterable<T> entitys) throws ZipException, IOException, InterruptedException;
}