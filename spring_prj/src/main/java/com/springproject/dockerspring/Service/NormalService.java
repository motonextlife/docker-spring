/** 
 **************************************************************************************
 * @file NormalService.java
 * @brief バイナリデータが関わらないデータを扱うサービスクラスにおいて、共通で使用する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;

import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.CsvImplForm.CsvCheckResult;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Common_Csv_Form;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;








/** 
 **************************************************************************************
 * @brief バイナリデータが関わらないデータを扱うサービスクラスにおいて、共通で使用する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - 全サービスで使用する機能を提供するインターフェースを継承するため、そちらの機能も実装可能。
 * - このインターフェースを直接実装クラスに実装することはなく、このインターフェースを他の
 * インターフェースが継承して使用する。
 * - サービスからは、特定の例外以外は全ての例外をキャッチするように作成する。
 * 
 * @see CommonService
 **************************************************************************************
 */ 
public interface NormalService<T, H, F> extends CommonService<T, H>{


  /** 
   **********************************************************************************************
   * @brief フォームから渡された情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   * @param[in] hist_kind 履歴情報を登録する際の指定する履歴種別
   *
   * @throw Exception
   **********************************************************************************************
   */
  void changeData(F form, History_Kind_Enum hist_kind) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定された管理番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した管理番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] id 指定する管理番号
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  T selectData(String id) throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定された管理番号に対応する情報を、PDFシートとして出力する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した管理番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したPDFシートファイル
   * 
   * @param[in] id 指定する管理番号
   * 
   * @see FileEntity
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FileEntity outputPdf(String id) throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 渡されたCSVファイルの情報を読み込み、読み込んだ内容をデータベースに保存する。
   * 
   * @details
   * - このメソッドは、登録したい情報をCSVに記述してシステムに投入することで、大量のデータをまとめて保存できる
   * 機能を提供する。
   * - 渡されたCSVファイルに不正があり、受付をすることができない場合は特定の例外を発生させ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return CSVを取り込んだ際の、エラーが発生した座標や管理番号の重複の有無の情報。なお、取り込みが成功
   * した場合は、「Null」を返却する。
   * 
   * @param[in] form CSVファイルが格納されたフォームクラス
   * 
   * @see CsvCheckResult
   *
   * @throw Exception
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  CsvCheckResult inputCsv(Common_Csv_Form form) throws Exception, InputFileDifferException;


  /** 
   **********************************************************************************************
   * @brief データベース内に保存されている指定されたデータを、一覧としてCSVファイルに書き込み出力する。
   * 
   * @details
   * - このメソッドは、現在保存中のデータを一括して取り出したい際に用いる。
   * - このメソッド内部でデータ検索を実行し、その結果をそのままCSVとして出力する。
   * - 検索した結果、結果が存在しない場合や、出力しようとしたデータに不正があり出力ができない場合は、特定の
   * 例外を投げ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したCSVファイルデータ
   * 
   * @param[in] form 出力対象となるデータを検索する際に必要な情報が入ったフォームクラス
   * 
   * @see FileEntity
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FileEntity outputCsv(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException;
       

  /** 
   **********************************************************************************************
   * @brief このシステム内に大量のデータを投入する際に用いるCSVを作成するうえで便利な、CSVファイルの
   * テンプレートを作成する。
   * 
   * @details
   * - CSVファイルをこのシステムに投入する際に、CSVファイルの体裁がわかってないとエラーが発生しやすくなるため、
   * ユーザー補助として、このテンプレートにデータを記述して投入すればエラーが発生しにくくなる。
   * - 出力しようとしたデータに不正があり出力ができない場合は、特定の例外を投げ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したCSVテンプレートデータ
   * 
   * @see FileEntity
   *
   * @throw Exception
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FileEntity outputCsvTemplate() throws Exception, OutputDataFailedException;
}