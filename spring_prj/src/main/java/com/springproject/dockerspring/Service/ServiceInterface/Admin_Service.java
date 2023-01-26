/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Service.ServiceInterface
 * 
 * @brief サービスクラスに全般的に使用するインターフェースを格納したパッケージ
 * 
 * @details
 * - このパッケージに一緒くたに全てのインターフェースを格納した理由としては、SpringのDIに対応する
 * 為のメソッドの定義が全くないインターフェースがほとんどで、新たなパッケージを作る必要はないと
 * 判断したためである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;





/** 
 **************************************************************************************
 * @file AdminService.java
 * @brief 主に[管理者]が使用するサービスに機能を提供するインターフェースを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.List;
import java.util.Map;

import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.System_User_Form;
import com.springproject.dockerspring.Form.NormalForm.Usage_Authority_Form;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;









/** 
 **************************************************************************************
 * @brief 主に[管理者]が使用するサービスに機能を提供するインターフェース
 * 
 * @details 
 * - サービスからは、特定の例外以外は全ての例外をキャッチするように作成する。
 **************************************************************************************
 */ 
public interface Admin_Service{


  /** 
   **********************************************************************************************
   * @brief 指定されたテーブルの、指定された期間内の履歴データを削除する。
   * 
   * @details
   * - 複数のテーブルの履歴情報をまとめて削除する際に、並列処理ができるようにする。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @see Del_Indivi_Hist_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  void deleteHistory(Del_Indivi_Hist_Form form) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致したシステムユーザーデータを全て取得する。
   * 
   * @details
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
   * @see System_User
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FindAllResult<System_User> accountList(Com_Search_Form form) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定された団員番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した団員番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] member_id 指定する団員番号
   *
   * @see System_User
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  System_User checkAccount(String member_id) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief フォームから渡されたシステムユーザー情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   *
   * @see System_User_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  void changeAccount(System_User_Form form) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定されたシステムユーザーデータ内のシリアルナンバーのデータを削除する。
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
  void deleteAccount(Delete_Form form) throws Exception, DataEmptyException;


  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した権限情報データを全て取得する。
   * 
   * @details
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
   * @see Usage_Authority
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  FindAllResult<Usage_Authority> authList(Com_Search_Form form) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief 指定された権限番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した権限番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] auth_id 指定する権限番号
   *
   * @see Usage_Authority
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  Usage_Authority checkAuth(String auth_id) 
    throws Exception, DataEmptyException, OutputDataFailedException;


  /** 
   **********************************************************************************************
   * @brief フォームから渡された権限情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   *
   * @see Usage_Authority_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  void changeAuth(Usage_Authority_Form form) throws Exception;


  /** 
   **********************************************************************************************
   * @brief 指定された権限データ内のシリアルナンバーのデータを削除する。
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
  void deleteAuth(Delete_Form form) throws Exception, DataEmptyException;


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
}