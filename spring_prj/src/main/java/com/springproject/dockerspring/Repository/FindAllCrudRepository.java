/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Repository
 * 
 * @brief このシステムで全般的に利用する、データベースとの接続を行うリポジトリクラスを格納する
 * パッケージ。
 * 
 * @details
 * - このパッケージでは、データベースからのデータの取得や更新を行うリポジトリに加え、多すぎる
 * 引数に対応するビルダークラスや、データ格納用の専用エンティティ、並列処理用の設定ファイルを
 * 格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository;





/** 
 **************************************************************************************
 * @file FindAllCrudRepository.java
 * @brief 検索リポジトリで使用する、ベースとなる既存のリポジトリインターフェースに
 * 追加して、使用する分岐メソッドを定義したインターフェースを格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;






/** 
 **************************************************************************************
 * @brief 検索リポジトリで使用する、ベースとなる既存のリポジトリインターフェースに
 * 追加して、使用する分岐メソッドを定義したインターフェース。
 * 
 * @details 
 * - このクラスを設けた理由としては、システムの上流のサービスクラスで、リポジトリ内に実装する
 * デフォルトメソッドを共通の型で扱えるようにするためである。
 * - このインターフェースでは、自動的にインスタンスが作成されないようになっている。
 * 
 * @par 使用アノテーション
 * - @NoRepositoryBean
 **************************************************************************************
 */ 
@NoRepositoryBean
public interface FindAllCrudRepository<T, ID> extends CrudRepository<T, ID>{


  /** 
   **********************************************************************************************
   * @brief フリーワードを指定したデータベースの検索時において、検索種別に応じて処理を分岐し、適切な
   * クエリ実装メソッドを非同期で実行する。
   * 
   * @details
   * - このインターフェース内に実装されているクエリ実装メソッドは、単体で使用されることはほとんどなく、
   * 全てこの分岐メソッドを経由する。
   * - 検索ワードは、要件上一つしか受け付けることができない。ただし、日付での期間検索の場合は例外的に
   * 二つの検索ワードを受け取ることができる。
   * - 履歴情報に関してはすべての検索で、変更履歴の期間を指定して検索を行う。
   * - データベース検索時の結果数を制限し、JVMのメモリ枯渇を防ぐため、オフセットを利用したページネーションを
   * 設ける。
   * - 検索種別に関しては列挙型を使用して厳密に受け入れる文字列を管理しており、違う文字列が引数で渡された場合は
   * エラーとなる。
   * - クエリ実装メソッドは基本的に非同期処理が可能となっており、ページネーション機能に必要な「全検索結果数」と
   * 「オフセットによる検索結果の一部分」を同時に取得する事が可能である。
   * - 引数がかなり多くなるため、ビルダークラスを用いてその中に引数を格納し渡すものとする。
   * 
   * @param[in] param 検索に必要な引数を格納したビルダーエンティティ
   * 
   * @return 「全検索結果数」と「オフセットによる検索結果の一部分」を格納した、このリポジトリで扱える専用の
   * エンティティクラス
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
	FindAllResult<T> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException, ParseException;
}