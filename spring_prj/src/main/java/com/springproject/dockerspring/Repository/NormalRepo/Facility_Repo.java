/** 
 **************************************************************************************
 * @file Facility_Repo.java
 * @brief 主に[設備管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Facility_Subject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;









/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Facility]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Facility
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Facility_Repo extends FindAllCrudRepository<Facility, Integer>{


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
   * @see Audio_Data_Subject
   * 
   * @throw ExecutionException
   * @throw InterruptedException
   **********************************************************************************************
   */
  @Override
  public default FindAllResult<Facility> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException, ParseException{

    String main_word = param.getMain_word();
    String sub_word = param.getSub_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<Facility_Subject> form_enum = Arrays.stream(Facility_Subject.values())
                                                 .parallel()
                                                 .filter(s -> s.name().equals(subject))
                                                 .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Facility_Repo:findAllBranch(IllegalArgumentException)]");
    }


    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());
    CompletableFuture<List<Facility>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Facility> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findAllOriginal(limit, offset) : 
                                 findAllOriginalDESC(limit, offset);
          async_count = findAllOriginalCount(max);
          break;

        case FaciId:
          async_result = order ? findAllByFaci_id(main_word, limit, offset) : 
                                 findAllByFaci_idDESC(main_word, limit, offset);
          async_count = findAllByFaci_idCount(main_word, max);
          break;

        case FaciName:
          async_result = order ? findAllByFaci_name(main_word, limit, offset) : 
                                 findAllByFaci_nameDESC(main_word, limit, offset);
          async_count = findAllByFaci_nameCount(main_word, max);
          break;

        case BuyDate:
          Date start_date = main_word == null ? null : parse_date.parse(main_word);
          Date end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByBuy_date(start_date, end_date, limit, offset) : 
                                 findAllByBuy_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByBuy_dateCount(start_date, end_date, max);
          break;

        case Producer:
          async_result = order ? findAllByProducer(main_word, limit, offset) : 
                                 findAllByProducerDESC(main_word, limit, offset);
          async_count = findAllByProducerCount(main_word, max);
          break;

        case StorageLoc:
          async_result = order ? findAllByStorage_loc(main_word, limit, offset) : 
                                 findAllByStorage_locDESC(main_word, limit, offset);
          async_count = findAllByStorage_locCount(main_word, max);
          break;

        case DispDate:
          start_date = main_word == null ? null : parse_date.parse(main_word);
          end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByDisp_date(start_date, end_date, limit, offset) : 
                                 findAllByDisp_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByDisp_dateCount(start_date, end_date, max);
          break;

        case Comment:
          async_result = order ? findAllByComment(main_word, limit, offset) : 
                                 findAllByCommentDESC(main_word, limit, offset);
          async_count = findAllByCommentCount(main_word, max);
          break;
      }

      CompletableFuture.allOf(async_result, async_count).join();
      result = async_result == null ? null : async_result.get();
      count = async_count == null ? null : async_count.get();

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [Facility_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Facility_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }catch(ParseException e) {
      throw new ParseException("Error location [Facility_Repo:findAllBranch(ParseException)]" + "\n" + e, e.getErrorOffset());
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Facility_Repo:findAllBranch(NullPointerException)]" + "\n" + e);
    }

    return new FindAllResult<Facility>(result, count);
  }










  /** 
   **********************************************************************************************
   * @brief [設備番号]に完全一致するデータを一件取得する。
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
   * @param[in] faci_id 検索対象の[設備番号]
   * 
   * @return [設備番号]に合致したデータ一件
   * 
   * @par 処理の大まかな流れ
   * -# [設備番号]に完全一致するデータを一件まで取得し、出力する。
   **********************************************************************************************
   */
  @Query("SELECT * FROM Facility" 
      + " WHERE faci_id = :faci_id LIMIT 1")
  Optional<Facility> findByFaci_Id(@Param("faci_id") String faci_id);













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
        + " SELECT * FROM Facility" 
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
   * - 並び替え対象のカラムは[設備番号]とする。
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
  @Query("SELECT * FROM Facility" 
      + " ORDER BY faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllOriginal(@Param("limit") Integer limit, @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[設備番号]とする。
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
  @Query("SELECT * FROM Facility" 
      + " ORDER BY faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllOriginalDESC(@Param("limit") Integer limit, @Param("offset") Integer offset);

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
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備番号]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[設備番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByFaci_idCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[設備番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByFaci_id(@Param("word") String word, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[設備番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE faci_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByFaci_idDESC(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[設備名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備名]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[設備名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[設備名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE faci_name LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByFaci_nameCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[設備名]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE faci_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY faci_name ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByFaci_name(@Param("word") String word, 
                                                       @Param("limit") Integer limit, 
                                                       @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[設備名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[設備名]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[設備名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[設備名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE faci_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY faci_name DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByFaci_nameDESC(@Param("word") String word, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);

  /** @} */













                                                             



  /** @name 検索種別[購入日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[購入日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[購入日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[購入日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[購入日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE buy_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByBuy_dateCount(@Param("start_date") Date start_date, 
                                                    @Param("end_date") Date end_date, 
                                                    @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[購入日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[購入日]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[購入日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[購入日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[購入日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE buy_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY buy_date ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByBuy_date(@Param("start_date") Date start_date, 
                                                      @Param("end_date") Date end_date, 
                                                      @Param("limit") Integer limit, 
                                                      @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[購入日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[購入日]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[購入日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[購入日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[購入日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE buy_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY buy_date DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByBuy_dateDESC(@Param("start_date") Date start_date, 
                                                          @Param("end_date") Date end_date, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[製作者]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[製作者]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[製作者]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[製作者]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE producer LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByProducerCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[製作者]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[製作者]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[製作者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[製作者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE producer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY producer ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByProducer(@Param("word") String word, 
                                                      @Param("limit") Integer limit, 
                                                      @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[製作者]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[製作者]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[製作者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[製作者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE producer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY producer DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByProducerDESC(@Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[保管場所]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[保管場所]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[保管場所]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[保管場所]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE storage_loc LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByStorage_locCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[保管場所]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[保管場所]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[保管場所]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[保管場所]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE storage_loc LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY storage_loc ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByStorage_loc(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[保管場所]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[保管場所]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[保管場所]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[保管場所]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE storage_loc LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY storage_loc DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByStorage_locDESC(@Param("word") String word, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */















  /** @name 検索種別[廃棄日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[廃棄日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[廃棄日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[廃棄日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[廃棄日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE disp_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByDisp_dateCount(@Param("start_date") Date start_date, 
                                                     @Param("end_date") Date end_date, 
                                                     @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[廃棄日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[廃棄日]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[廃棄日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[廃棄日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[廃棄日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE disp_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY disp_date ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByDisp_date(@Param("start_date") Date start_date, 
                                                       @Param("end_date") Date end_date, 
                                                       @Param("limit") Integer limit, 
                                                       @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[廃棄日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[廃棄日]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[廃棄日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[廃棄日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[廃棄日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE disp_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY disp_date DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByDisp_dateDESC(@Param("start_date") Date start_date, 
                                                           @Param("end_date") Date end_date, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[その他コメント]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[その他コメント]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[その他コメント]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[その他コメント]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Facility" 
        + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByCommentCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[その他コメント]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[その他コメント]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[その他コメント]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment ASC, faci_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByComment(@Param("word") String word, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[その他コメント]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[設備番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[その他コメント]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[その他コメント]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Facility" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment DESC, faci_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Facility>> findAllByCommentDESC(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);

  /** @} */














  /** 
   **********************************************************************************************
   * @brief 指定した[設備番号][シリアルナンバー]で更新しようとした際に、データベース内で重複しないこと
   * を確認する。
   * 
   * @details
   * - 特に使用する機会がないので、非同期処理は実装しない。
   * - このメソッドは、主にフォームバリデーションでの[設備番号]の重複チェックに用いられる。
   * - [設備番号]とペアになっている[シリアルナンバー]を一緒に指定するのは、対象の[設備番号]がすでに
   * 保存されていてそれを更新したいといった場合に、その格納カラムまで判定してしまい重複と判定されてしまう
   * のを防ぐためである。
   * - 前項の対策によって「指定された[シリアルナンバー]を除外して[設備番号]を検索する」ことが可能になる。
   * - 検索方法としては、[設備番号]は完全一致、[シリアルナンバー]は否定完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] serial_num 指定の[設備番号]とペアになっている[シリアルナンバー]
   * @param[in] faci_id 指定の[シリアルナンバー]とペアになっている[設備番号]
   * 
   * @return 検索後の結果件数。重複ありの場合は「1」が返り、重複なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [設備番号]は完全一致、[シリアルナンバー]は否定完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT COUNT(*) FROM Facility" 
      + " WHERE serial_num != :serial_num " 
         + "AND faci_id = :faci_id " 
       + "LIMIT 1")
  Integer checkUnique(@Param("serial_num") Integer serial_num, @Param("faci_id") String faci_id);
}