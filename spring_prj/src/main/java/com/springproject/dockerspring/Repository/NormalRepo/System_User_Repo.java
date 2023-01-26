/** 
 **************************************************************************************
 * @file System_User_Repo.java
 * @brief 主に[システムユーザー管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.System_User_Subject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;









/** 
 **************************************************************************************
 * @brief 主に[システムユーザー管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[System_User]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see System_User
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface System_User_Repo extends FindAllCrudRepository<System_User, Integer>{


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
   * @see Usage_Authority_Subject
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @Override
  public default FindAllResult<System_User> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException{

    String word = param.getMain_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<System_User_Subject> form_enum = Arrays.stream(System_User_Subject.values())
                                                    .parallel()
                                                    .filter(s -> s.name().equals(subject))
                                                    .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [System_User_Repo:findAllBranch(IllegalArgumentException)]");
    }


    CompletableFuture<List<System_User>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<System_User> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findAllOriginal(limit, offset) : 
                                 findAllOriginalDESC(limit, offset);
          async_count = findAllOriginalCount(max);
          break;

        case MemberId:
          async_result = order ? findAllByMember_id(word, limit, offset) : 
                                 findAllByMember_idDESC(word, limit, offset);
          async_count = findAllByMember_idCount(word, max);
          break;

        case Username:
          async_result = order ? findAllByUsername(word, limit, offset) : 
                                 findAllByUsernameDESC(word, limit, offset);
          async_count = findAllByUsernameCount(word, max);
          break;

        case AuthId:
          async_result = order ? findAllByAuth_id(word, limit, offset) : 
                                 findAllByAuth_idDESC(word, limit, offset);
          async_count = findAllByAuth_idCount(word,  max);
          break;

        case Locking:
          Boolean word_bool = null;
          if(word != null && (word.equals("true") || word.equals("false"))){
            word_bool = Boolean.parseBoolean(word);
          }

          async_result = order ? findAllByLocking(word_bool, limit, offset) : 
                                 findAllByLockingDESC(word_bool, limit, offset);
          async_count = findAllByLockingCount(word_bool, max);
          break;
      }

      CompletableFuture.allOf(async_result, async_count).join();
      result = async_result == null ? null : async_result.get();
      count = async_count == null ? null : async_count.get();

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [System_User_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [System_User_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [System_User_Repo:findAllBranch(NullPointerException)]" + "\n" + e);
    }

    return new FindAllResult<System_User>(result, count);
  }









  /** 
   **********************************************************************************************
   * @brief [団員番号]に完全一致するデータを一件取得する。
   * 
   * @details
   * - このメソッドに関しては機会がないので、非同期実行は可能になっていない。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * - この検索メソッドに関しては、メソッド[findAllBranch]を経由しない。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] member_id 検索対象の[団員番号]
   * 
   * @return [団員番号]に合致したデータ一件
   * 
   * @par 処理の大まかな流れ
   * -# [団員番号]に完全一致するデータを一件まで取得し、出力する。
   **********************************************************************************************
   */
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE member_id = :member_id LIMIT 1")
  Optional<System_User> findByMember_id(@Param("member_id") String member_id);













  /** @name 検索種別[全取得]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、保存データすべてを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM System_User" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllOriginalCount(@Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員番号]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 全データを対象に、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllOriginal(@Param("limit") Integer limit, @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員番号]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 全データを対象に、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */    
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllOriginalDESC(@Param("limit") Integer limit, @Param("offset") Integer offset);

  /** @} */











  


  /** @name 検索種別[団員番号]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員番号]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[団員番号]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[団員番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM System_User" 
        + " WHERE member_id LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByMember_idCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[団員番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[団員番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */    
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE member_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByMember_id(@Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[団員番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[団員番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */    
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE member_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByMember_idDESC(@Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */















  /** @name 検索種別[ユーザー名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[ユーザー名]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[ユーザー名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[ユーザー名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM System_User" 
        + " WHERE username LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByUsernameCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ユーザー名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[ユーザー名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[ユーザー名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ユーザー名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE username LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY username ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByUsername(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ユーザー名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[ユーザー名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[ユーザー名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ユーザー名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE username LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY username DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByUsernameDESC(@Param("word") String word, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */








  





  /** @name 検索種別[権限番号]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限番号]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[権限番号]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[権限番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM System_User" 
        + " WHERE auth_id LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAuth_idCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[権限番号]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[権限番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[権限番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE auth_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_id ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByAuth_id(@Param("word") String word, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[権限番号]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[権限番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[権限番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE auth_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_id DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByAuth_idDESC(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);

  /** @} */
















  /** @name 検索種別[ロック有無]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[ロック有無]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] admin_bool 検索対象の[ロック有無]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[ロック有無]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM System_User" 
        + " WHERE locking = :bool" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByLockingCount(@Param("bool") Boolean bool, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ロック有無]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[団員番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] admin_bool 検索対象の[ロック有無]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ロック有無]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE locking = :bool" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByLocking(@Param("bool") Boolean bool, 
																												@Param("limit") Integer limit, 
                                                    		@Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ロック有無]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[団員番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - 「パスワード」と「ログイン失敗回数」は、セキュリティの関係上出力の対象とはなっていない。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] admin_bool 検索対象の[ロック有無]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ロック有無]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT serial_num, member_id, username, auth_id, locking FROM System_User" 
      + " WHERE locking = :bool" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<System_User>> findAllByLockingDESC(@Param("bool") Boolean bool, 
																														@Param("limit") Integer limit, 
                                                        		@Param("offset") Integer offset);

  /** @} */












  /** 
   **********************************************************************************************
   * @brief 指定した[団員番号]のデータが、テーブル内に「存在しないことを」確認する。
   * 
   * @details
   * - 非同期処理を可能にする。
   * - このメソッドは、参照元である「団員情報テーブル」からデータを削除する際に、このテーブルに参照されて
   * いるデータだった場合にエラーが発生するため、そのエラーを未然に防ぐために用いられる。
   * - 検索方法としては、完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] member_id 検索対象の[団員番号]
   * 
   * @return 検索後の結果件数。結果ありの場合は「1」が返り、結果なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [団員番号]を完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM System_User" 
      + " WHERE member_id = :member_id")
  CompletableFuture<Integer> nonExistRemainMember_id(@Param("member_id") String member_id);










  /** 
   **********************************************************************************************
   * @brief 指定した[権限番号]のデータが、テーブル内に「存在しないことを」確認する。
   * 
   * @details
   * - 非同期処理を可能にする。
   * - このメソッドは、参照元である「権限情報テーブル」からデータを削除する際に、このテーブルに参照されて
   * いるデータだった場合にエラーが発生するため、そのエラーを未然に防ぐために用いられる。
   * - 検索方法としては、完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] member_id 検索対象の[権限番号]
   * 
   * @return 検索後の結果件数。結果ありの場合は「1」が返り、結果なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [権限番号]を完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM System_User" 
      + " WHERE auth_id = :auth_id")
  CompletableFuture<Integer> nonExistRemainAuth_id(@Param("auth_id") String auth_id);












  /** 
   **********************************************************************************************
   * @brief ログイン時に失敗した場合、該当[団員番号]のデータベース上に[ログイン失敗回数]と
   * [ロックの有無]を記録する。
   * 
   * @details
   * - 特に必要ないため、非同期処理は実装しない。
   * - このメソッドは主に、SpringSecurity上の認証処理で用いられ、ログイン失敗を検出した場合に用いる。
   * - 検索方法としては、完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Modifying
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] member_id 検索対象の[団員番号]
   * @param[in] fail_count 更新する[ログイン失敗回数]
   * @param[in] locking 更新する[ロックの有無]
   * 
   * @par 処理の大まかな流れ
   * -# [団員番号]を完全一致として検索し、[ログイン失敗回数][ロックの有無]を更新する。
   **********************************************************************************************
   */ 
  @Modifying
  @Query("UPDATE System_User" 
			+ " SET fail_count = :fail_count, locking = :locking" 
			+ " WHERE member_id = :member_id")
  void updateLock(@Param("member_id") String member_id, 
									@Param("fail_count") Integer fail_count, 
									@Param("locking") Boolean locking);











  /** 
   **********************************************************************************************
   * @brief ログインを行う際に、[ユーザー名]で検索してユーザー情報をすべて取得する。
   * 
   * @details
   * - 特に必要ないため、非同期処理は実装しない。
   * - このメソッドは主に、SpringSecurity上の認証処理で用いられ、認証処理をする際に用いられる。
   * - [パスワード]と[ログイン失敗回数]を取得することが出来る、唯一のメソッドである。
   * - 検索方法としては、完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] username 検索対象の[ユーザー名]
   * 
   * @par 処理の大まかな流れ
   * -# [ユーザー名]を完全一致として検索し、検索結果を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT * FROM System_User" 
			+ " WHERE username = :username LIMIT 1")
  Optional<System_User> sysSecurity(@Param("username") String username);










  /** 
   **********************************************************************************************
   * @brief 指定した[団員番号][シリアルナンバー]で更新しようとした際に、データベース内で重複しないこと
   * を確認する。
   * 
   * @details
   * - 特に使用する機会がないので、非同期処理は実装しない。
   * - このメソッドは、主にフォームバリデーションでの[団員番号]の重複チェックに用いられる。
   * - [団員番号]とペアになっている[シリアルナンバー]を一緒に指定するのは、対象の[団員番号]がすでに
   * 保存されていてそれを更新したいといった場合に、その格納カラムまで判定してしまい重複と判定されてしまう
   * のを防ぐためである。
   * - 前項の対策によって「指定された[シリアルナンバー]を除外して[団員番号]を検索する」ことが可能になる。
   * - 検索方法としては、[団員番号]は完全一致、[シリアルナンバー]は否定完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] serial_num 指定の[団員番号]とペアになっている[シリアルナンバー]
   * @param[in] member_id 指定の[シリアルナンバー]とペアになっている[団員番号]
   * 
   * @return 検索後の結果件数。重複ありの場合は「1」が返り、重複なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [団員番号]は完全一致、[シリアルナンバー]は否定完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT COUNT(*) FROM System_User" 
      + " WHERE serial_num != :serial_num " 
         + "AND member_id = :member_id " 
       + "LIMIT 1")
  Integer checkUnique(@Param("serial_num") Integer serial_num, @Param("member_id") String member_id);











  /** 
   **********************************************************************************************
   * @brief 指定した[ユーザー名][シリアルナンバー]で更新しようとした際に、データベース内で重複しないこと
   * を確認する。
   * 
   * @details
   * - 特に使用する機会がないので、非同期処理は実装しない。
   * - このメソッドは、主にフォームバリデーションでの[ユーザー名]の重複チェックに用いられる。
   * - [ユーザー名]とペアになっている[シリアルナンバー]を一緒に指定するのは、対象の[ユーザー名]がすでに
   * 保存されていてそれを更新したいといった場合に、その格納カラムまで判定してしまい重複と判定されてしまう
   * のを防ぐためである。
   * - 前項の対策によって「指定された[シリアルナンバー]を除外して[ユーザー名]を検索する」ことが可能になる。
   * - 検索方法としては、[ユーザー名]は完全一致、[シリアルナンバー]は否定完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] serial_num 指定の[ユーザー名]とペアになっている[シリアルナンバー]
   * @param[in] username 指定の[シリアルナンバー]とペアになっている[ユーザー名]
   * 
   * @return 検索後の結果件数。重複ありの場合は「1」が返り、重複なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [ユーザー名]は完全一致、[シリアルナンバー]は否定完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT COUNT(*) FROM System_User" 
      + " WHERE serial_num != :serial_num " 
         + "AND username = :username " 
       + "LIMIT 1")
  Integer checkUsernameUnique(@Param("serial_num") Integer serial_num, @Param("username") String username);
}