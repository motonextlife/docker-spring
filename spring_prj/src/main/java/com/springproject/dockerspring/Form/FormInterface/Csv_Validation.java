/** 
 **************************************************************************************
 * @file Csv_Validation.java
 * @brief 共通の入力時のCSVファイルデータのバリデーションの際に用いる機能を提供を行う
 * インターフェースを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.FormInterface;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.CsvImplForm.CsvCheckResult;








/** 
 **************************************************************************************
 * @brief 共通の入力時のCSVファイルデータのバリデーションの際に用いる機能を提供を行う
 * インターフェース
 * 
 * @details 
 * - このインターフェースは、主にCSVファイルから抽出したデータのバリデーションや、その処理を
 * 並列に実行するための機能を提供する。
 * 
 * @see CsvCheckResult
 **************************************************************************************
 */ 
public interface Csv_Validation {


  /** 
   **********************************************************************************************
   * @brief 受け取ったCSVデータが格納された一時ファイルのバリデーションを行う。
   * 
   * @details
   * - 受け取った一時ファイルから、CSVデータとしてデータを抽出し、並列実行のためのスレッドを作成する。
   * - 作成したスレッド上で、検査用のメソッドを動作させ、効率よく並列にバリデーションを実行できるように
   * している。
   * - 実際のチェックは[parallelCheck]に、並列処理でゆだねる。
   * - 検査後は、エラーが発生したCSV上の列番号と行番号を出力することで、CSVファイル修正時の補助となる。
   * 
   * @param[in] tmp_file 検査対象のデータが入った一時ファイル
   * 
   * @see CsvCheckResult
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  CsvCheckResult csvChecker(File tmp_file) 
                 throws ExecutionException, InterruptedException, ParseException, IOException, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief 実際にチェック処理を並列処理で行う。
   * 
   * @details
   * - この機能で検査する項目としては、通常のフォーム入力時の検査項目に準ずる。ただし、既に作成してある
   * フォームクラスの機能が使用できないので、正規表現でバリデーションを実行している。
   * - なお、少しでもフロント側へのレスポンスを早くするため、一つでもエラーが発生した後続の検査処理は
   * 行わず、スルーする。
   * 
   * @return 処理の並列実行の為の格納オブジェクト
   * 
   * @param[in] csv_map 検査対象のデータが格納されたマップリスト
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  CompletableFuture<List<Integer>> parallelCheck(Map<String, String> csv_map) throws ParseException;
}
