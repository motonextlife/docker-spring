package com.springproject.dockerspring.service.ServiceInterface;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;


/***************************************************************************/
/*   [CommonService]
/*   全てのサービスメソッドで実装するメソッドを提供する。
/***************************************************************************/
public interface CommonService<T, H> {

  /*************************************************************/
  /*   [selectData]
  /*   管理番号を元に、個別の情報を取得する。
  /*************************************************************/
  Optional<T> selectData(String id);


  /*************************************************************/
  /*   [duplicateData]
  /*   引数で渡されたシリアルナンバーと管理番号を用いて、
  /*   管理番号に重複がないか確認する。
  /*************************************************************/
  Boolean duplicateData(Integer serial_num, String id);


  /*************************************************************/
  /*   [existsData]
  /*   該当の管理番号が存在するか確認する。
  /*************************************************************/
  Boolean existsData(String id);


  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  T changeData(T entity) throws DbActionException;


  /*************************************************************/
  /*   [selectAllData]
  /*   指定された検索ワードと検索ジャンル、並び順を指定して
  /*   条件に合致した情報をデータベースから取り出す。
  /*************************************************************/
  Iterable<T> selectAllData(String word, String subject, Boolean order);


  /*************************************************************/
  /*   [selectHist]
  /*   管理番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  Iterable<H> selectHist(String id, Date start_datetime, Date end_datetime);


  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  void rollbackData(Integer history_id) throws DbActionException, ForeignMissException;


  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  Map<String, String> parseString(T entity);
  Map<String, String> parseStringH(H entity);


  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  List<Map<String, String>> mapPacking(Iterable<T> datalist);
  List<Map<String, String>> mapPackingH(Iterable<H> datalist);


  /*************************************************************/
  /*   [outputPdf]
  /*   指定されたシリアルナンバーのデータをPDFシートとして出力する。
  /*************************************************************/
  String outputPdf(Integer serial_num) throws IOException;


  /*************************************************************/
  /*   [inputCsv]
  /*   CSVシートに書かれたデータを取り込み、データベースに登録する。
  /*************************************************************/
  List<List<Integer>> inputCsv(MultipartFile file) 
    throws IOException, InputFileDifferException, ExecutionException, InterruptedException, ParseException, DbActionException;


  /*************************************************************/
  /*   [outputCsv]
  /*   渡されたエンティティリスト内のデータを、CSVシートとして
  /*   出力する。
  /*************************************************************/
  String outputCsv(Iterable<T> datalist) throws IOException;


  /*************************************************************/
  /*   [outputCsvTemplate]
  /*   CSVシートの作成に必要な項目を記述したテンプレートを
  /*   出力する。
  /*************************************************************/
  String outputCsvTemplate() throws IOException;


  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  Boolean deleteData(Integer serial_num) throws DbActionException;
}