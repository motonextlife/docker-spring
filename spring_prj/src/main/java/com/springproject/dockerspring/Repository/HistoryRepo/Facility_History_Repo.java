/** 
 **************************************************************************************
 * @file Facility_History_Repo.java
 * @brief 主に[設備管理変更履歴]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.HistoryRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Common_History_Subject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;







/** 
 **************************************************************************************
 * @brief 主に[設備管理変更履歴]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Facility_History]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Facility_History
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Facility_History_Repo extends FindAllCrudRepository<Facility_History, Integer>{


  /** 
   **********************************************************************************************
   * @brief フリーワードを指定したデータベースの検索時において、検索種別に応じて処理を分岐し、適切な
   * クエリ実装メソッドを非同期で実行する。
   * 
   * @details
   * - このインターフェース内に実装されているクエリ実装メソッドは、単体で使用されることはほとんどなく、
   * 全てこの分岐メソッドを経由する。
   * - 検索ワードは、要件上一つしか受け付けることができない。
   * - すべての検索で、変更履歴の期間を指定して検索を行う。
   * - データベース検索時の結果数を制限し、JVMのメモリ枯渇を防ぐため、オフセットを利用したページネーションを
   * 設ける。
   * - 検索種別に関しては列挙型を使用して厳密に受け入れる文字列を管理しており、違う文字列が引数で渡された場合は
   * エラーとなる。
   * - クエリ実装メソッドは基本的に非同期処理が可能となっており、ページネーション機能に必要な「全検索結果数」と
   * 「オフセットによる検索結果の一部分」を同時に取得する事が可能である。
   * 
   * @param[in] param 検索に必要な引数を格納したビルダーエンティティ
   * 
   * @return 「全検索結果数」と「オフセットによる検索結果の一部分」を格納した、このリポジトリで扱える専用の
   * エンティティクラス
   * 
   * @par 処理の大まかな流れ
   * -# 引数で渡された検索種別の文字列を検証し、所定の列挙型に対応している物か確認する。
   * -# オフセット値や並び順指定など、クエリ実行に影響を及ぼす項目が「Null」で無い事を確認する。
   * -# 検索種別と、引数で渡された「検索結果の並び順指定」に応じて、クエリ実装メソッドを選定する。
   * -# 「全検索結果数取得」と「オフセットによる検索結果の一部分取得」を、非同期で同時実行する。
   * -# 前述の二つの結果を専用のエンティティクラスに格納し、戻り値とする。
   * 
   * @note 他のメソッドは抽象メソッドとなるが、このメソッドのみデフォルトメソッドでの実装となる。
   *
   * @see FindAllResult
   * @see FindAllParam
   * @see Common_History_Subject
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @Override
  public default FindAllResult<Facility_History> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException{

    String word = param.getMain_word();
    Date start_datetime = param.getStart_datetime();
    Date end_datetime = param.getEnd_datetime();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();
    
    Optional<Common_History_Subject> form_enum = Arrays.stream(Common_History_Subject.values())
                                                       .parallel()
                                                       .filter(s -> s.name().equals(subject))
                                                       .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Facility_History_Repo:findAllBranch(IllegalArgumentException)]");
    }


    CompletableFuture<List<Facility_History>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Facility_History> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case ChangeDatetime:
          async_result = order ? findAllByDateBetween(start_datetime, end_datetime, limit, offset) : 
                                 findAllByDateBetweenDESC(start_datetime, end_datetime, limit, offset);
          async_count = findAllByDateBetweenCount(start_datetime, end_datetime, max);
          break;

        case Id:
          async_result = order ? findByFaci_id(word, start_datetime, end_datetime, limit, offset) : 
                                 findByFaci_idDESC(word, start_datetime, end_datetime, limit, offset);
          async_count = findByFaci_idCount(word, start_datetime, end_datetime, max);
          break;

        case ChangeKinds:
          async_result = order ? findByChange_kinds(word, start_datetime, end_datetime, limit, offset) : 
                                 findByChange_kindsDESC(word, start_datetime, end_datetime, limit, offset);
          async_count = findByChange_kindsCount(word, start_datetime, end_datetime, max);
          break;

        case OperationUser:
          async_result = order ? findByOperation_user(word, start_datetime, end_datetime, limit, offset) : 
                                 findByOperation_userDESC(word, start_datetime, end_datetime, limit, offset);
          async_count = findByOperation_userCount(word, start_datetime, end_datetime, max);
          break;
      }

      CompletableFuture.allOf(async_result, async_count).join();
      result = async_result == null ? null : async_result.get();
      count = async_count == null ? null : async_count.get();

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [Facility_History_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Facility_History_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }

    return new FindAllResult<Facility_History>(result, count);
  }












  /** @name 検索種別[履歴日時]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴日時]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、範囲一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日時
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日時
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[履歴日時]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility_History" 
        + " WHERE change_datetime BETWEEN :start_datetime AND :end_datetime" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByDateBetweenCount(@Param("start_datetime") Date start_datetime, 
                                                       @Param("end_datetime") Date end_datetime, 
                                                       @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴日時]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[履歴日時]で行う。
   * - 検索方法としては、範囲一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY change_datetime ASC, history_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findAllByDateBetween(@Param("start_datetime") Date start_datetime, 
                                                                 @Param("end_datetime") Date end_datetime, 
                                                                 @Param("limit") Integer limit, 
                                                                 @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴日時]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[履歴日時]で行う。
   * - 検索方法としては、範囲一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY change_datetime DESC, history_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findAllByDateBetweenDESC(@Param("start_datetime") Date start_datetime, 
                                                                     @Param("end_datetime") Date end_datetime, 
                                                                     @Param("limit") Integer limit, 
                                                                     @Param("offset") Integer offset); 

  /** @} */













  /** @name 検索種別[設備番号]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備番号]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、[設備番号]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日時
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日時
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[履歴日時][設備番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility_History" 
        + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
          + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findByFaci_idCount(@Param("word") String word, 
                                                @Param("start_datetime") Date start_datetime, 
                                                @Param("end_datetime") Date end_datetime, 
                                                @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[設備番号]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[設備番号]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][設備番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY faci_id ASC, change_datetime ASC, history_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByFaci_id(@Param("word") String word, 
                                                          @Param("start_datetime") Date start_datetime, 
                                                          @Param("end_datetime") Date end_datetime, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[設備番号]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[設備番号]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][設備番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY faci_id DESC, change_datetime DESC, history_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByFaci_idDESC(@Param("word") String word, 
                                                              @Param("start_datetime") Date start_datetime, 
                                                              @Param("end_datetime") Date end_datetime, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset); 

  /** @} */













  /** @name 検索種別[履歴種別]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴種別]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、[履歴種別]は完全一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日時
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日時
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[履歴日時][履歴種別]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility_History" 
        + " WHERE change_kinds = :word" 
          + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findByChange_kindsCount(@Param("word") String word, 
                                                     @Param("start_datetime") Date start_datetime, 
                                                     @Param("end_datetime") Date end_datetime, 
                                                     @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴種別]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[履歴種別]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[履歴種別]は完全一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][履歴種別]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE change_kinds = :word" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY change_datetime ASC, history_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByChange_kinds(@Param("word") String word, 
                                                               @Param("start_datetime") Date start_datetime, 
                                                               @Param("end_datetime") Date end_datetime, 
                                                               @Param("limit") Integer limit, 
                                                               @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[履歴種別]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[履歴種別]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[履歴種別]は完全一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][履歴種別]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */   
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE change_kinds = :word" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY change_datetime DESC, history_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByChange_kindsDESC(@Param("word") String word, 
                                                                   @Param("start_datetime") Date start_datetime, 
                                                                   @Param("end_datetime") Date end_datetime, 
                                                                   @Param("limit") Integer limit, 
                                                                   @Param("offset") Integer offset); 

  /** @} */















  /** @name 検索種別[操作ユーザー名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[操作ユーザー名]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、[操作ユーザー名]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日時
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日時
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[履歴日時][操作ユーザー名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility_History" 
        + " WHERE operation_user LIKE CONCAT('%', :word, '%')" 
          + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findByOperation_userCount(@Param("word") String word, 
                                                       @Param("start_datetime") Date start_datetime, 
                                                       @Param("end_datetime") Date end_datetime, 
                                                       @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[操作ユーザー名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[操作ユーザー名]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[操作ユーザー名]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][操作ユーザー名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */     
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE operation_user LIKE CONCAT('%', :word, '%')" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY operation_user ASC, change_datetime ASC, history_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByOperation_user(@Param("word") String word, 
                                                                   @Param("start_datetime") Date start_datetime, 
                                                                   @Param("end_datetime") Date end_datetime, 
                                                                   @Param("limit") Integer limit, 
                                                                   @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[操作ユーザー名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[操作ユーザー名]で行い、その次に[履歴日時]で行う。
   * - 検索方法としては、[操作ユーザー名]は部分一致検索、[履歴日時]は範囲一致検索で行っている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象のフリーワード
   * @param[in] start_datetime 検索対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 検索対象の[履歴日時]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[履歴日時][操作ユーザー名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */      
  @Async("Repo")
  @Query("SELECT * FROM Facility_History" 
      + " WHERE operation_user LIKE CONCAT('%', :word, '%')" 
        + " AND change_datetime BETWEEN :start_datetime AND :end_datetime" 
      + " ORDER BY operation_user DESC, change_datetime DESC, history_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility_History>> findByOperation_userDESC(@Param("word") String word, 
                                                                       @Param("start_datetime") Date start_datetime, 
                                                                       @Param("end_datetime") Date end_datetime, 
                                                                       @Param("limit") Integer limit, 
                                                                       @Param("offset") Integer offset); 

  /** @} */





  
  






  /** 
   **********************************************************************************************
   * @brief 削除期間を指定して、保存されている履歴情報を削除する。
   * 
   * @details
   * - 特に使用しないため、非同期実行は設けない。
   * - 削除指定期間の開始日時と終了日時を指定して、その間の情報はすべて削除される。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Modifying
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載

   * @param[in] start_datetime 削除対象の[履歴日時]の検索対象期間の開始日
   * @param[in] end_datetime 削除対象の[履歴日時]の検索対象期間の終了日
   * 
   * @par 処理の大まかな流れ
   * -# 指定の期間で、該当するデータをすべて削除する。
   **********************************************************************************************
   */  
  @Modifying
  @Query("DELETE FROM Facility_History "
       + "WHERE change_datetime BETWEEN :start_datetime AND :end_datetime")
  void deleteByDateBetween(@Param("start_datetime") Date start_datetime, 
                           @Param("end_datetime") Date end_datetime);
}