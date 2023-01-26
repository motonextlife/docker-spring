/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Service
 * 
 * @brief このシステム上の全ての処理を集約し、コントローラーからの指示を処理する機能を格納する。
 * 
 * @details
 * - このパッケージでは、システム全体の処理を集約し一連の流れの処理を行うサービスクラスを格納
 * する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;





/** 
 **************************************************************************************
 * @file BlobService.java
 * @brief バイナリデータが関わり、ファイルサーバーへの通信が必要なサービスクラスへの機能を提供する
 * インターフェースを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.Map;

import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.Form.BlobImplForm.Blob_Data_Form;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.NormalForm.Zip_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Output_Form;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;








/** 
 **************************************************************************************
 * @brief バイナリデータが関わり、ファイルサーバーへの通信が必要なサービスクラスへの機能を提供する
 * インターフェース
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
public interface BlobService<T, H> extends CommonService<T, H>{


  /** 
   **********************************************************************************************
   * @brief フォームから渡された情報をデータベースとファイルサーバーでペアにして格納する。なお、この
   * メソッドには、複数のバイナリデータをまとめて保存することが可能である。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - 戻り値であるマップリストは、バイナリデータのファイルサーバーへの保存に失敗した際に、保存に失敗した
   * バイナリデータのファイル名を返却し、ユーザーに知らせるためにある。
   * - ファイルサーバーとデータベースとの整合性を極力保つため、トランザクションを一つのファイルごとに実施して、
   * コミットしている。
   * - ファイルサーバーとデータベースの整合性は、エラーが発生した場合に取ることが難しくなるため、多少の不整合は
   * 要件上許容をしている。
   * - 許容するパターンとしては、「ファイルサーバー上にはデータが存在しているが、データベース側にはペアとなる
   * データが存在しない」である。
   * - 逆に、「データベース上にはデータが存在するが、ファイルサーバー上にはペアが存在しない」のは、許容しない。
   * - これは、ファイルサーバー上のデータはデータベース上のデータを元に検索するためであり、データベース上に
   * データが無ければ、そもそもファイルサーバー上を検索すら行わない為、ファイルサーバー上にデータが残ってしまう
   * 分には問題ないからである。
   * - 逆のパターンは、ファイルサーバー上にないデータを検索してしまうことになり、エラーが頻発する恐れがあるため
   * 防ぐようにしている。
   * - ただ、ファイルサーバー上に余計なデータが残ってしまうのはよろしくないので、定期的なバッチ処理で削除する。
   * 
   * @param[in] form データベースとファイルサーバーに格納するデータを格納したフォームクラス
   * @param[in] hist_kind 履歴情報を登録する際の指定する履歴種別
   * 
   * @return 保存に失敗したファイル名
   * 
   * @see Blob_Data_Form
   **********************************************************************************************
   */
  Map<Integer, String> changeData(Blob_Data_Form form, History_Kind_Enum hist_kind) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定の管理番号で、指定した個数のバイナリデータを追加保存しようとした際に、環境変数で定められている
   * 一つの管理番号当たりの保存制限数を超えないか判定する。
   * 
   * @details
   * - このメソッドは、データの新規追加や、ロールバックの際にバイナリデータの保存制限数を超えてしまわないかを
   * 判定する。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 判定結果の真偽値
   * 
   * @param[in] id 指定する管理番号
   * @param[in] input_count 追加するファイルの個数
   *
   * @throw Exception
   **********************************************************************************************
   */
  Boolean checkMaxLimitData(String id, int input_count) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーや履歴番号のバイナリデータを個別に取得する。
   * 
   * @details
   * - このメソッドは、通常データと履歴データで共用する。
   * - バイナリデータに関してはデータが大きすぎるため、データ一覧の検索時にまとめて取得してフロントに送信と
   * いった処理は行わない。フロント側から個別のデータを指定されることで、その都度バイナリデータを取得する。
   * - 指定した番号のバイナリデータが存在しなかったり、取得したデータに不正があり出力できない場合は、特定の
   * 例外を出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得してきたバイナリデータファイル
   * 
   * @param[in] id 指定するシリアルナンバーや履歴番号
   * @param[in] hist 「True」で履歴用の圧縮済みのデータ解凍して取得、「False」で通常データを取得
   * 
   * @see FileEntity
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FileEntity selectBlobData(Integer num_id, Boolean hist) 
    throws DataEmptyException, OutputDataFailedException, Exception;


  /** 
   **********************************************************************************************
   * @brief 入力された圧縮ファイルを解凍し、中に入っているバイナリデータを、データベースとファイルサーバーに
   * 新規追加する。
   * 
   * @details
   * - 要件上、一つの管理番号に複数のバイナリデータ一括で格納する事が多々あるため、圧縮ファイルで送信する
   * 事でまとめて保存ができるようになっている。
   * - 戻り値となっているマップリストには、保存に失敗したバイナリファイルの名称と、発生した例外の名称が
   * 格納されている。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同インターフェース内のメソッド[changeData]の説明を参照する事。
   * 
   * @return 保存に失敗したファイル名
   * 
   * @param[in] form 渡された圧縮ファイルが格納されたフォームクラス
   * 
   * @see Zip_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  Map<String, String> inputZip(Zip_Form form) throws Exception;

    
  /** 
   **********************************************************************************************
   * @brief 指定された複数のバイナリデータを、圧縮ファイルに格納してまとめて返却する。
   * 
   * @details
   * - 要件上、保存しているバイナリデータをまとめてほしいといった事があるので、保存されているバイナリデータを
   * まとめて圧縮ファイルに格納し、返却することができる。
   * - 取得しようとしたバイナリデータが存在しなかったり、取得してきたデータに不正があり出力ができない場合は、
   * 特定の例外を投げ、別として扱う。
   * - その他例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成した圧縮ファイル
   * 
   * @param[in] form 指定したバイナリデータのシリアルナンバーが複数格納されたフォームクラス
   * 
   * @see Zip_Output_Form
   * @see FileEntity
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FileEntity outputZip(Zip_Output_Form form) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定の管理番号で保存されているバイナリデータをすべて削除する。
   * 
   * @details
   * - 参照元の管理番号のデータそのものが消えた場合は、連動して保管されているバイナリデータもすべて削除
   * しなければならない。
   * - なお、バイナリデータを個別で削除するのは、このインターフェースの継承元の削除メソッドが担うので
   * 問題ない。
   * - 削除しようとしたバイナリデータが存在しなかったり場合は、特定の例外を投げ、別として扱う。
   * - その他例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同インターフェース内のメソッド[changeData]の説明を参照する事。
   * 
   * @param[in] id 削除を指定する管理番号
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  void deleteAllData(String id) throws Exception, DataEmptyException;
}