/** 
 **************************************************************************************
 * @file InOutCsv.java
 * @brief CSVファイルの入出力の機能を提供したインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CompInterface;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;







/** 
 **************************************************************************************
 * @brief CSVファイルの入出力の機能を提供したインターフェース
 * 
 * @details 
 * - 入力されたCSVファイルからのデータの抽出のほか、保存されているデータをCSVファイルに
 * 入力し出力する機能を持つ。
 **************************************************************************************
 */ 
public interface InOutCsv<T> {


  /** 
   **********************************************************************************************
   * @brief 入力されたCSVファイルから、データを抽出する。
   * 
   * @details
   * - 受け付けるCSVファイルは厳密にバリデーションを行い、ヘッダー行が不正だったり、不正に長い文字列が
   * 格納されていればエラーを出す。
   * - 抽出が成功したら、次の工程のフォームバリデーションに渡すために、一時ファイルを作成して再度
   * 抽出済みのデータを書き込む。
   *
   * @return 抽出が成功し、再度CSVファイルとして一時生成したファイル 
   *
   * @param[in] csv_file 入力されたCSVファイル
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  File extractCsv(MultipartFile csv_file) throws IOException, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief バリデーションが終わり正常なCSVファイルを再度抽出し、データベースに情報を格納する。
   * 
   * @details
   * - データベースに格納する処理は、サービス側から関数型インターフェースで渡して、その中で実行する。
   * これによってCSV処理とデータベース処理を明確に分ける。
   *
   * @param[in] tmp_csv_file データベースに投入する一時CSVファイル
   * @param[in] goto_database 抽出したデータをデータベースに投入する処理の関数
   *
   * @throw IOException
   * @throw ParseException
   **********************************************************************************************
   */
  void csvToDatabase(File tmp_csv_file, Consumer<List<T>> goto_database) throws ParseException, IOException;


  /** 
   **********************************************************************************************
   * @brief データベースに保存しているデータを、CSVデータとして出力する。
   * 
   * @details
   * - データベースからデータを読み込む処理は、サービス側から関数型インターフェースで渡して、
   * その中で実行する。これによってCSV処理とデータベース処理を明確に分ける。
   * - 取得するデータは、出力時にもバリデーションを行い、通過したものを用いる。
   *
   * @return 生成されたCSVファイル 
   *
   * @param[in] database_generate データベースから出力対象のデータを取得する処理の関数
   *
   * @throw IOException
   **********************************************************************************************
   */
  File outputCsv(Function<Integer, List<Map<String, String>>> database_generate) throws IOException;


  /** 
   **********************************************************************************************
   * @brief このシステムへのCSVファイルの入力時に使用するCSVファイルのテンプレートを出力する。
   * このテンプレートに入力したいデータを書き込むことで、ユーザーが入力データを作成しやすくする。
   * 
   * @details
   * - 出力されるテンプレートの内容としては、ヘッダー行と書き込める限界行数を記述した空っぽの
   * CSVファイルである。
   *
   * @return 生成されたCSVファイル 
   *
   * @throw IOException
   **********************************************************************************************
   */
  File outputTemplate() throws IOException;
}