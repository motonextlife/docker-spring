package com.springproject.dockerspring.component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;



/*************************************************************/
/*   [InOutCsv]
/*   CSVデータの書き出しや読込の機能を提供する。
/*************************************************************/
public interface InOutCsv<T> {

  /*************************************************************/
  /*   [extractCsv]
  /*   読み込んだCSVファイルからデータを抽出し、マップリストに
  /*   文字列形式で格納する。
  /*************************************************************/
  List<Map<String, String>> extractCsv(MultipartFile file) throws IOException, InputFileDifferException;


  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  Iterable<T> csvToEntitys(List<Map<String, String>> maplist) throws ParseException;


  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  String outputCsv(Iterable<T> entitys) throws IOException;


  /*************************************************************/
  /*   [outputTemplate]
  /*   入力に使用するテンプレートCSVファイルの出力を行う。
  /*************************************************************/
  String outputTemplate() throws IOException;
}