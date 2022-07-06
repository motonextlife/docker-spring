package com.springproject.dockerspring.service.BlobServiceInterface;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.service.OriginalException.DbActionException;
import com.springproject.dockerspring.service.OriginalException.ForeignMissException;
import com.springproject.dockerspring.service.OriginalException.OverCountException;


/***************************************************************************/
/*   [CommonBlobService]
/*   全てのバイナリデータ取り扱いサービスメソッドで実装するメソッドを提供する。
/***************************************************************************/
public interface CommonBlobService<T, H, F> {

  /*************************************************************/
  /*   [selectData]
  /*   管理番号を元に、該当するデータを取得する。
  /*************************************************************/
  Iterable<T> selectData(String id);


  /*************************************************************/
  /*   [existsData]
  /*   該当の管理番号が存在するか確認する。
  /*************************************************************/
  Boolean existsData(String id);
  
  
  /*************************************************************/
  /*   [countData]
  /*   該当の管理番号のデータの個数を数え、カウント数を返す。
  /*************************************************************/
  Integer countData(String id);
  
  
  /*************************************************************/
  /*   [checkMaxLimitData]
  /*   該当の管理番号のデータの個数を数え、規定数未満に
  /*   収まっているか確認する。
  /*************************************************************/
  Boolean checkMaxLimitData(String id, int inputcount);
  
  
  /*************************************************************/
  /*   [changeData]
  /*   渡されたエンティティをデータベースに登録する。
  /*   なお、新規登録と更新で共用である。
  /*************************************************************/
  Iterable<T> changeData(Iterable<T> entity) throws DbActionException;
  
  
  /*************************************************************/
  /*   [selectHist]
  /*   管理番号と検索期間を指定して、条件に合致する履歴情報を
  /*   データベースから取り出す。
  /*************************************************************/
  Iterable<H> selectHist(String id, Date start_datetime, Date end_datetime);
  
  
  /*************************************************************/
  /*   [selectHistBlobData]
  /*   指定された履歴番号のバイナリデータを取得する。
  /*************************************************************/
  Optional<H> selectHistBlobData(Integer history_id);
  
  
  /*************************************************************/
  /*   [rollbackData]
  /*   指定された履歴番号のデータを、本データに
  /*   ロールバックする。
  /*************************************************************/
  void rollbackData(Integer history_id) 
    throws DbActionException, ForeignMissException, DataFormatException, OverCountException;


  /*************************************************************/
  /*   [existsPair]
  /*   引数で渡されたシリアルナンバーと管理番号を用いて、
  /*   シリアルナンバーと管理番号のペアが存在するか確認。
  /*************************************************************/
  Boolean existsPair(Integer serial_num, String id);


  /*************************************************************/
  /*   [foreignAndMaxcount]
  /*   データベースカラムに参照性制約があるデータにおいて、
  /*   該当するデータの整合性を確認する。
  /*   またロールバック時に、ファイルの最大数を超えないかも
  /*   チェックする。
  /*************************************************************/
  void foreignAndMaxcount(H entity) throws ForeignMissException, OverCountException;


  /*************************************************************/
  /*   [parseString]
  /*   渡されたエンティティ内のデータを文字列に変換し、
  /*   マップデータに変換して返す。
  /*************************************************************/
  Map<String, String> parseString(T entity);
  Map<String, String> parseStringH(H entity) throws DataFormatException;


  /*************************************************************/
  /*   [mapPacking]
  /*   渡されたエンティティリストのデータを全て文字列化して
  /*   リストとして返す。
  /*************************************************************/
  List<Map<String, String>> mapPacking(Iterable<T> datalist);
  List<Map<String, String>> mapPackingH(Iterable<H> datalist) throws DataFormatException;

  
  /*************************************************************/
  /*   [inputZip]
  /*   指定された管理番号の為に入力されたデータの
  /*   ZIPファイルを解凍し、データベースに取り込む。
  /*************************************************************/
  void inputZip(String id, MultipartFile file) 
    throws ZipException, IOException, InputFileDifferException, InterruptedException, ExecutionException, DbActionException, OverCountException;


  /*************************************************************/
  /*   [outputZip]
  /*   渡されたエンティティリスト内のデータをZIPファイルに
  /*   詰めて、出力する。
  /*************************************************************/
  String outputZip(Iterable<T> datalist) throws ZipException, IOException, InterruptedException;


  /*************************************************************/
  /*   [deleteData]
  /*   指定のシリアルナンバーのデータを削除する。
  /*************************************************************/
  void deleteData(Integer serial_num) throws DbActionException;


  /*************************************************************/
  /*   [deleteAllData]
  /*   指定の管理番号のデータをすべて削除する。
  /*************************************************************/
  void deleteAllData(String id) throws DbActionException;


  /*************************************************************/
  /*   [selectData]
  /*   フォームクラス内のバイナリデータを取得し、それぞれの
  /*   エンティティを作成する。
  /*************************************************************/
  List<T> blobPacking(F form) throws IOException;
}