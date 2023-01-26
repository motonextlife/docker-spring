/** 
 **************************************************************************************
 * @file CommonService.java
 * @brief 全てのサービスクラスで共通の機能を提供するインターフェースを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Form.NormalForm.Com_Hist_Get_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.Rollback_Form;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.OriginalException.OverCountException;









/** 
 **************************************************************************************
 * @brief 全てのサービスクラスで共通の機能を提供するインターフェース
 * 
 * @details 
 * - このインターフェースを直接実装クラスに実装することはなく、このインターフェースを他の
 * インターフェースが継承して使用する。
 * - サービスからは、特定の例外以外は全ての例外をキャッチするように作成する。
 **************************************************************************************
 */ 
public interface CommonService<T, H> {


  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した通常データを全て取得する。
   * 
   * @details
   * - このメソッドは、通常データの検索時に使用する。履歴用データに関しては、また別のメソッドが存在する。
   * - 検索した結果、結果が存在しなかった場合と、検索結果を出力する際にデータに不正があって出力できない
   * 場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form 検索時の必要な情報を格納したフォームクラス
   * 
   * @see Com_Search_Form
   * @see FindAllResult
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FindAllResult<T> selectAllData(Com_Search_Form form)
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した履歴データを全て取得する。
   * 
   * @details
   * - このメソッドは、履歴データの検索時に使用する。通常データに関しては、また別のメソッドが存在する。
   * - 検索した結果、結果が存在しなかった場合と、検索結果を出力する際にデータに不正があって出力できない
   * 場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form 検索時の必要な情報を格納したフォームクラス
   * 
   * @see Com_Hist_Get_Form
   * @see FindAllResult
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FindAllResult<H> selectHist(Com_Hist_Get_Form form) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号の履歴情報を、通常データにロールバックして復元する。
   * 
   * @details
   * - このメソッドは、誤操作時などに以前の情報を履歴から復元し、ユーザビリティを高めるためにある。
   * - ロールバック処理を行った結果、該当の履歴情報が存在しなかった場合や、ロールバックした後に一つの管理番号
   * につき保持できる情報を制限数を超えてしまう場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form ロールバック時に必要な情報を格納したフォームクラス
   * 
   * @see Rollback_Form
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OverCountException
   **********************************************************************************************
   */
  void rollbackData(Rollback_Form form) 
    throws Exception, DataEmptyException, OverCountException;


  /** 
   **********************************************************************************************
   * @brief 引数として渡されたデータが格納されているエンティティの情報を、文字列に変換してマップリストとして
   * 返却する。こちらは単体のエンティティの変換である。
   * 
   * @details
   * - このメソッドは、データの画面出力時は文字列である必要があることから、フロント側にデータを渡す直前に
   * 行う。
   * - 処理内容としては、ただ単にエンティティ内に備わっている文字列変換出力メソッドを実行するだけである。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 文字列変換したデータのマップリスト
   * 
   * @param[in] entity 変換対象となるデータが入ったエンティティ
   * 
   * @see EntitySetUp
   *
   * @throw Exception
   **********************************************************************************************
   */
  Map<String, String> parseString(EntitySetUp entity) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 引数として渡されたデータが格納されているエンティティの情報を、文字列に変換してマップリストとして
   * 返却する。こちらはリストで渡された複数のエンティティをまとめて変換する。
   * 
   * @details
   * - このメソッドは、データの画面出力時は文字列である必要があることから、フロント側にデータを渡す直前に
   * 行う。
   * - 処理内容としては、ただ単にエンティティ内に備わっている文字列変換出力メソッドを実行するだけである。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 文字列変換したデータのマップリストを格納したリスト
   * 
   * @param[in] data_list 変換対象となるデータが入ったエンティティのリスト
   * 
   * @see EntitySetUp
   *
   * @throw Exception
   **********************************************************************************************
   */
  List<Map<String, String>> mapPacking(List<EntitySetUp> data_list) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定された通常データ内のシリアルナンバーのデータを削除する。
   * 
   * @details
   * - 削除しようとしたデータが見つからなかった場合は、特定の例外を投げ、別として対応する。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @see Delete_Form
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  void deleteData(Delete_Form form) throws Exception, DataEmptyException;


  /** 
   **********************************************************************************************
   * @brief 指定された期間内の履歴データを削除する。
   * 
   * @details
   * - このメソッドに関しては、原則管理者ページからのアクセスからしか実行しない。
   * - 他のテーブルの履歴情報をまとめて削除する場合があるので、並列処理ができるようにする。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 並列処理のスレッドが入ったインスタンス
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @see Del_Indivi_Hist_Form
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  CompletableFuture<Void> deleteHistory(Del_Indivi_Hist_Form form) throws DataEmptyException, Exception;
}