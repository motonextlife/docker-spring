/** 
 **************************************************************************************
 * @file Usage_Authority_Repo.java
 * @brief 主に[権限管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Usage_Authority_Subject;










/** 
 **************************************************************************************
 * @brief 主に[権限管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Usage_Authority]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Usage_Authority
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Usage_Authority_Repo extends FindAllCrudRepository<Usage_Authority, Integer>{


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
  public default FindAllResult<Usage_Authority> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException{

    String word = param.getMain_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<Usage_Authority_Subject> form_enum = Arrays.stream(Usage_Authority_Subject.values())
                                                        .parallel()
                                                        .filter(s -> s.name().equals(subject))
                                                        .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Usage_Authority_Repo:findAllBranch(IllegalArgumentException)]");
    }


    CompletableFuture<List<Usage_Authority>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Usage_Authority> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findAllOriginal(limit, offset) : 
                                 findAllOriginalDESC(limit, offset);
          async_count = findAllOriginalCount(max);
          break;

        case AuthId:
          async_result = order ? findAllByAuth_id(word, limit, offset) : 
                                 findAllByAuth_idDESC(word, limit, offset);
          async_count = findAllByAuth_idCount(word, max);
          break;

        case AuthName:
          async_result = order ? findAllByAuth_name(word, limit, offset) : 
                                 findAllByAuth_nameDESC(word, limit, offset);
          async_count = findAllByAuth_nameCount(word, max);
          break;

        case Admin:
          Boolean admin_bool = null;
          if(word != null && (word.equals("true") || word.equals("false"))){
            admin_bool = Boolean.parseBoolean(word);
          }
          async_result = order ? findAllByAdmin(admin_bool, limit, offset) : 
                                 findAllByAdminDESC(admin_bool, limit, offset);
          async_count = findAllByAdminCount(admin_bool, max);
          break;

        case MemberInfo:
          async_result = order ? findAllByMember_info(word, limit, offset) : 
                                 findAllByMember_infoDESC(word, limit, offset);
          async_count = findAllByMember_infoCount(word, max);
          break;

        case Facility:
          async_result = order ? findAllByFacility(word, limit, offset) : 
                                 findAllByFacilityDESC(word, limit, offset);
          async_count = findAllByFacilityCount(word, max);
          break;

        case MusicalScore:
          async_result = order ? findAllByMusical_score(word, limit, offset) : 
                                 findAllByMusical_scoreDESC(word, limit, offset);
          async_count = findAllByMusical_scoreCount(word, max);
          break;

        case SoundSource:
          async_result = order ? findAllBySound_source(word, limit, offset) : 
                                 findAllBySound_sourceDESC(word, limit, offset);
          async_count = findAllBySound_sourceCount(word, max);
          break;
      }

      CompletableFuture.allOf(async_result, async_count).join();
      result = async_result == null ? null : async_result.get();
      count = async_count == null ? null : async_count.get();

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [Usage_Authority_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Usage_Authority_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Usage_Authority_Repo:findAllBranch(NullPointerException)]" + "\n" + e);
    }

    return new FindAllResult<Usage_Authority>(result, count);
  }










  /** 
   **********************************************************************************************
   * @brief [権限番号]に完全一致するデータを一件取得する。
   * 
   * @details
   * - このメソッドに関しては機会がないので、非同期実行は可能になっていない。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * - この検索メソッドに関しては、メソッド[findAllBranch]を経由しない。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] auth_id 検索対象の[権限番号]
   * 
   * @return [権限番号]に合致したデータ一件
   * 
   * @par 処理の大まかな流れ
   * -# [権限番号]に完全一致するデータを一件まで取得し、出力する。
   **********************************************************************************************
   */
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE auth_id = :auth_id LIMIT 1")
  Optional<Usage_Authority> findByAuth_id(@Param("auth_id") String auth_id);













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
        + " SELECT * FROM Usage_Authority" 
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
   * - 並び替え対象のカラムは[権限番号]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
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
  @Query("SELECT * FROM Usage_Authority" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllOriginal(@Param("limit") Integer limit, @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[権限番号]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
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
  @Query("SELECT * FROM Usage_Authority" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllOriginalDESC(@Param("limit") Integer limit, @Param("offset") Integer offset);

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
        + " SELECT * FROM Usage_Authority" 
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
   * - 並び替え対象のカラムは[権限番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
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
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE auth_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAuth_id(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[権限番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
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
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE auth_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAuth_idDESC(@Param("word") String word, 
                                                                @Param("limit") Integer limit, 
                                                                @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[権限名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限名]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[権限名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[権限名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE auth_name LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAuth_nameCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[権限名]で行い、その次に[権限番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[権限名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[権限名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE auth_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_name ASC, auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAuth_name(@Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[権限名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[権限名]で行い、その次に[権限番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[権限名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[権限名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE auth_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY auth_name DESC, auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAuth_nameDESC(@Param("word") String word, 
                                                                  @Param("limit") Integer limit, 
                                                                  @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[管理者権限]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[管理者権限]に指定し、全検索結果数をカウントする。
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
   * @param[in] admin_bool 検索対象の[管理者権限]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[管理者権限]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE admin = :admin_bool" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAdminCount(@Param("admin_bool") Boolean admin_bool, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[管理者権限]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[権限番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] admin_bool 検索対象の[管理者権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[管理者権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE admin = :admin_bool" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAdmin(@Param("admin_bool") Boolean admin_bool, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[管理者権限]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[権限番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] admin_bool 検索対象の[管理者権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[管理者権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE admin = :admin_bool" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByAdminDESC(@Param("admin_bool") Boolean admin_bool, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[団員管理権限]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員管理権限]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[団員管理権限]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[団員管理権限]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE member_info = :word" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByMember_infoCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[団員管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[団員管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE member_info = :word" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByMember_info(@Param("word") String word, 
                                                                @Param("limit") Integer limit, 
                                                                @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[団員管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[団員管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[団員管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE member_info = :word" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByMember_infoDESC(@Param("word") String word, 
                                                                    @Param("limit") Integer limit, 
                                                                    @Param("offset") Integer offset);

  /** @} */

















  /** @name 検索種別[設備管理権限]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備管理権限]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[設備管理権限]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[設備管理権限]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE facility = :word" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByFacilityCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[設備管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE facility = :word" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByFacility(@Param("word") String word, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[設備管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE facility = :word" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByFacilityDESC(@Param("word") String word, 
                                                                 @Param("limit") Integer limit, 
                                                                 @Param("offset") Integer offset);

  /** @} */















  /** @name 検索種別[楽譜管理権限]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[楽譜管理権限]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[楽譜管理権限]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[楽譜管理権限]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE musical_score = :word" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByMusical_scoreCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[楽譜管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[楽譜管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[楽譜管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[楽譜管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE musical_score = :word" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByMusical_score(@Param("word") String word, 
                                                                  @Param("limit") Integer limit, 
                                                                  @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[楽譜管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[楽譜管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[楽譜管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[楽譜管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE musical_score = :word" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllByMusical_scoreDESC(@Param("word") String word, 
                                                                      @Param("limit") Integer limit, 
                                                                      @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[音源管理権限]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源管理権限]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[音源管理権限]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[音源管理権限]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Usage_Authority" 
        + " WHERE sound_source = :word" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllBySound_sourceCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[音源管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[音源管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE sound_source = :word" 
      + " ORDER BY auth_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllBySound_source(@Param("word") String word, 
                                                                  @Param("limit") Integer limit, 
                                                                  @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源管理権限]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[音源管理権限]とする。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[音源管理権限]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源管理権限]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Usage_Authority" 
      + " WHERE sound_source = :word" 
      + " ORDER BY auth_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Usage_Authority>> findAllBySound_sourceDESC(@Param("word") String word, 
                                                                      @Param("limit") Integer limit, 
                                                                      @Param("offset") Integer offset);

  /** @} */















  /** 
   **********************************************************************************************
   * @brief 指定した[権限番号][シリアルナンバー]で更新しようとした際に、データベース内で重複しないこと
   * を確認する。
   * 
   * @details
   * - 特に使用する機会がないので、非同期処理は実装しない。
   * - このメソッドは、主にフォームバリデーションでの[権限番号]の重複チェックに用いられる。
   * - [権限番号]とペアになっている[シリアルナンバー]を一緒に指定するのは、対象の[権限番号]がすでに
   * 保存されていてそれを更新したいといった場合に、その格納カラムまで判定してしまい重複と判定されてしまう
   * のを防ぐためである。
   * - 前項の対策によって「指定された[シリアルナンバー]を除外して[権限番号]を検索する」ことが可能になる。
   * - 検索方法としては、[権限番号]は完全一致、[シリアルナンバー]は否定完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] serial_num 指定の[権限番号]とペアになっている[シリアルナンバー]
   * @param[in] auth_id 指定の[シリアルナンバー]とペアになっている[権限番号]
   * 
   * @return 検索後の結果件数。重複ありの場合は「1」が返り、重複なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [権限番号]は完全一致、[シリアルナンバー]は否定完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT COUNT(*) FROM Usage_Authority" 
      + " WHERE serial_num != :serial_num " 
         + "AND auth_id = :auth_id " 
       + "LIMIT 1")
  Integer checkUnique(@Param("serial_num") Integer serial_num, @Param("auth_id") String auth_id);
}