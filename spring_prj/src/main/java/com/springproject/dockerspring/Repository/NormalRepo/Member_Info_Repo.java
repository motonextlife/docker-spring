/** 
 **************************************************************************************
 * @file Member_Info_Repo.java
 * @brief 主に[団員管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Member_Info_Subject;

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
 * @brief 主に[団員管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Member_Info]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Member_Info
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Member_Info_Repo extends FindAllCrudRepository<Member_Info, Integer>{


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
  public default FindAllResult<Member_Info> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException, ParseException{

    String main_word = param.getMain_word();
    String sub_word = param.getSub_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<Member_Info_Subject> form_enum = Arrays.stream(Member_Info_Subject.values())
                                                    .parallel()
                                                    .filter(s -> s.name().equals(subject))
                                                    .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Member_Info_Repo:findAllBranch(IllegalArgumentException)]");
    }


    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());
    CompletableFuture<List<Member_Info>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Member_Info> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findAllOriginal(limit, offset) : 
                                 findAllOriginalDESC(limit, offset);
          async_count = findAllOriginalCount(max);
          break;

        case Memberid:
          async_result = order ? findAllByMember_id(main_word, limit, offset) : 
                                 findAllByMember_idDESC(main_word, limit, offset);
          async_count = findAllByMember_idCount(main_word, max);
          break;

        case Name:
          async_result = order ? findAllByName(main_word, limit, offset) : 
                                 findAllByNameDESC(main_word, limit, offset);
          async_count = findAllByNameCount(main_word, max);
          break;

        case NamePronu:
          async_result = order ? findAllByName_pronu(main_word, limit, offset) : 
                                 findAllByName_pronuDESC(main_word, limit, offset);
          async_count = findAllByName_pronuCount(main_word,  max);
          break;

        case Sex:
          async_result = order ? findAllBySex(main_word, limit, offset) : 
                                 findAllBySexDESC(main_word, limit, offset);
          async_count = findAllBySexCount(main_word, max);
          break;

        case Birthday:
          Date start_date = main_word == null ? null : parse_date.parse(main_word);
          Date end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByBirthday(start_date, end_date, limit, offset) : 
                                 findAllByBirthdayDESC(start_date, end_date, limit, offset);
          async_count = findAllByBirthdayCount(start_date, end_date, max);
          break;

        case JoinDate:
          start_date = main_word == null ? null : parse_date.parse(main_word);
          end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByJoin_date(start_date, end_date, limit, offset) : 
                                 findAllByJoin_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByJoin_dateCount(start_date, end_date, max);
          break;

        case RetDate:
          start_date = main_word == null ? null : parse_date.parse(main_word);
          end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByRet_date(start_date, end_date, limit, offset) : 
                                 findAllByRet_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByRet_dateCount(start_date, end_date, max);
          break;

        case Email:
          async_result = order ? findAllByEmail(main_word, limit, offset) : 
                                 findAllByEmailDESC(main_word, limit, offset);
          async_count = findAllByEmailCount(main_word, max);
          break;

        case Tel:
          async_result = order ? findAllByTel(main_word, limit, offset) : 
                                 findAllByTelDESC(main_word, limit, offset);
          async_count = findAllByTelCount(main_word, max);
          break;

        case Postcode:
          async_result = order ? findAllByPostcode(main_word, limit, offset) : 
                                 findAllByPostcodeDESC(main_word, limit, offset);
          async_count = findAllByPostcodeCount(main_word, max);
          break;

        case Addr:
          async_result = order ? findAllByAddr(main_word, limit, offset) : 
                                 findAllByAddrDESC(main_word, limit, offset);
          async_count = findAllByAddrCount(main_word, max);
          break;

        case Position:
          async_result = order ? findAllByPosition(main_word, limit, offset) : 
                                 findAllByPositionDESC(main_word, limit, offset);
          async_count = findAllByPositionCount(main_word, max);
          break;

        case ArriDate:
          start_date = main_word == null ? null : parse_date.parse(main_word);
          end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByArri_date(start_date, end_date, limit, offset) : 
                                 findAllByArri_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByArri_dateCount(start_date, end_date, max);
          break;

        case Job:
          async_result = order ? findAllByJob(main_word, limit, offset) : 
                                 findAllByJobDESC(main_word, limit, offset);
          async_count = findAllByJobCount(main_word, max);
          break;

        case AssignDept:
          async_result = order ? findAllByAssign_dept(main_word, limit, offset) : 
                                 findAllByAssign_deptDESC(main_word, limit, offset);
          async_count = findAllByAssign_deptCount(main_word, max);
          break;

        case AssignDate:
          start_date = main_word == null ? null : parse_date.parse(main_word);
          end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByAssign_date(start_date, end_date, limit, offset) : 
                                 findAllByAssign_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByAssign_dateCount(start_date, end_date, max);
          break;

        case InstCharge:
          async_result = order ? findAllByInst_charge(main_word, limit, offset) : 
                                 findAllByInst_chargeDESC(main_word, limit, offset);
          async_count = findAllByInst_chargeCount(main_word, max);
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
      throw new ExecutionException("Error location [Member_Info_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Member_Info_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }catch(ParseException e) {
      throw new ParseException("Error location [Member_Info_Repo:findAllBranch(ParseException)]" + "\n" + e, e.getErrorOffset());
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info_Repo:findAllBranch(NullPointerException)]" + "\n" + e);
    }

    return new FindAllResult<Member_Info>(result, count);
  }










  /** 
   **********************************************************************************************
   * @brief [団員番号]に完全一致するデータを一件取得する。
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
   * @param[in] member_id 検索対象の[団員番号]
   * 
   * @return [団員番号]に合致したデータ一件
   * 
   * @par 処理の大まかな流れ
   * -# [団員番号]に完全一致するデータを一件まで取得し、出力する。
   **********************************************************************************************
   */
  @Query("SELECT * FROM Member_Info" 
      + " WHERE member_id = :member_id LIMIT 1")
  Optional<Member_Info> findByMember_id(@Param("member_id") String member_id);













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
        + " SELECT * FROM Member_Info" 
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
  @Query("SELECT * FROM Member_Info" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllOriginal(@Param("limit") Integer limit, @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[団員番号]とする。
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
  @Query("SELECT * FROM Member_Info" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllOriginalDESC(@Param("limit") Integer limit, @Param("offset") Integer offset);

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
        + " SELECT * FROM Member_Info" 
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
  @Query("SELECT * FROM Member_Info" 
      + " WHERE member_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByMember_id(@Param("word") String word, 
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
  @Query("SELECT * FROM Member_Info" 
      + " WHERE member_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByMember_idDESC(@Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[名前]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[名前]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[名前]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[名前]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE name LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByNameCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[名前]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[名前]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[名前]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[名前]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY name ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByName(@Param("word") String word, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[名前]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[名前]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[名前]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[名前]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY name DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByNameDESC(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[ふりがな]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[ふりがな]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[ふりがな]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[ふりがな]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE name_pronu LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByName_pronuCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ふりがな]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[ふりがな]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[ふりがな]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ふりがな]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE name_pronu LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY name_pronu ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByName_pronu(@Param("word") String word, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[ふりがな]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[ふりがな]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[ふりがな]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[ふりがな]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE name_pronu LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY name_pronu DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByName_pronuDESC(@Param("word") String word, 
                                                               @Param("limit") Integer limit, 
                                                               @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[性別]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[性別]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[性別]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[性別]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE sex = :word" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllBySexCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[性別]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[団員番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[性別]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[性別]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE sex = :word" 
      + " ORDER BY member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllBySex(@Param("word") String word, 
                                                    @Param("limit") Integer limit, 
                                                    @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[性別]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、[団員番号]で行う。
   * - 検索方法としては、完全一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[性別]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[性別]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE sex = :word" 
      + " ORDER BY member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllBySexDESC(@Param("word") String word, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[誕生日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[誕生日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[誕生日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[誕生日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[誕生日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE birthday BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByBirthdayCount(@Param("start_date") Date start_date, 
                                                    @Param("end_date") Date end_date, 
                                                    @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[誕生日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[誕生日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[誕生日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[誕生日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[誕生日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE birthday BETWEEN :start_date AND :end_date" 
      + " ORDER BY birthday ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByBirthday(@Param("start_date") Date start_date, 
                                                         @Param("end_date") Date end_date, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[誕生日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[誕生日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[誕生日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[誕生日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[誕生日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE birthday BETWEEN :start_date AND :end_date" 
      + " ORDER BY birthday DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByBirthdayDESC(@Param("start_date") Date start_date, 
                                                             @Param("end_date") Date end_date, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */












  /** @name 検索種別[入団日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[入団日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[入団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[入団日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[入団日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE join_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByJoin_dateCount(@Param("start_date") Date start_date, 
                                                     @Param("end_date") Date end_date, 
                                                     @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[入団日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[入団日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[入団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[入団日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[入団日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE join_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY join_date ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByJoin_date(@Param("start_date") Date start_date, 
                                                          @Param("end_date") Date end_date, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[入団日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[入団日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[入団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[入団日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[入団日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE join_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY join_date DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByJoin_dateDESC(@Param("start_date") Date start_date, 
                                                              @Param("end_date") Date end_date, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[退団日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[退団日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[退団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[退団日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[退団日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE ret_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByRet_dateCount(@Param("start_date") Date start_date, 
                                                    @Param("end_date") Date end_date, 
                                                    @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[退団日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[退団日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[退団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[退団日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[退団日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE ret_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY ret_date ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByRet_date(@Param("start_date") Date start_date, 
                                                         @Param("end_date") Date end_date, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[退団日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[退団日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[退団日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[退団日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[退団日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE ret_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY ret_date DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByRet_dateDESC(@Param("start_date") Date start_date, 
                                                             @Param("end_date") Date end_date, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[メールアドレス１][メールアドレス２]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[メールアドレス１][メールアドレス２]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[メールアドレス１][メールアドレス２]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[メールアドレス１][メールアドレス２]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE email_1 LIKE CONCAT('%', :word, '%')" 
           + " OR email_2 LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByEmailCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[メールアドレス１][メールアドレス２]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[メールアドレス１][メールアドレス２]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[メールアドレス１][メールアドレス２]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[メールアドレス１][メールアドレス２]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE email_1 LIKE CONCAT('%', :word, '%')" 
         + " OR email_2 LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY email_1 ASC, email_2 ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByEmail(@Param("word") String word, 
                                                      @Param("limit") Integer limit, 
                                                      @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[メールアドレス１][メールアドレス２]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[メールアドレス１][メールアドレス２]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[メールアドレス１][メールアドレス２]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[メールアドレス１][メールアドレス２]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE email_1 LIKE CONCAT('%', :word, '%')" 
         + " OR email_2 LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY email_1 DESC, email_2 DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByEmailDESC(@Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[電話番号１][電話番号２]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[電話番号１][電話番号２]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[電話番号１][電話番号２]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[電話番号１][電話番号２]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE tel_1 LIKE CONCAT('%', :word, '%')" 
           + " OR tel_2 LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByTelCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[電話番号１][電話番号２]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[電話番号１][電話番号２]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[電話番号１][電話番号２]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[電話番号１][電話番号２]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE tel_1 LIKE CONCAT('%', :word, '%')" 
         + " OR tel_2 LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY tel_1 ASC, tel_2 ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByTel(@Param("word") String word, 
                                                    @Param("limit") Integer limit, 
                                                    @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[電話番号１][電話番号２]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[電話番号１][電話番号２]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[電話番号１][電話番号２]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[電話番号１][電話番号２]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE tel_1 LIKE CONCAT('%', :word, '%')" 
         + " OR tel_2 LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY tel_1 DESC, tel_2 DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByTelDESC(@Param("word") String word, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("offset") Integer offset);

  /** @} */













  /** @name 検索種別[現住所郵便番号]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所郵便番号]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[現住所郵便番号]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[現住所郵便番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE addr_postcode LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByPostcodeCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所郵便番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現住所郵便番号]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[現住所郵便番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現住所郵便番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE addr_postcode LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY addr_postcode ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByPostcode(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所郵便番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現住所郵便番号]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[現住所郵便番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現住所郵便番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE addr_postcode LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY addr_postcode DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByPostcodeDESC(@Param("word") String word, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[現住所]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[現住所]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[現住所]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE addr LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAddrCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現住所]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[現住所]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現住所]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE addr LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY addr ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAddr(@Param("word") String word, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[現住所]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現住所]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[現住所]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現住所]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE addr LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY addr DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAddrDESC(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[役職名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[役職名]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[役職名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[役職名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE position LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByPositionCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[役職名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[役職名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[役職名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[役職名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE position LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY position ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByPosition(@Param("word") String word, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[役職名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[役職名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[役職名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[役職名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE position LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY position DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByPositionDESC(@Param("word") String word, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);

  /** @} */
















  /** @name 検索種別[現役職着任日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[現役職着任日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[現役職着任日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[現役職着任日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[現役職着任日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE position_arri_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByArri_dateCount(@Param("start_date") Date start_date, 
                                                     @Param("end_date") Date end_date, 
                                                     @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[現役職着任日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現役職着任日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[現役職着任日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[現役職着任日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現役職着任日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */   
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE position_arri_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY position_arri_date ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByArri_date(@Param("start_date") Date start_date, 
                                                          @Param("end_date") Date end_date, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[現役職着任日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[現役職着任日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[現役職着任日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[現役職着任日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[現役職着任日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE position_arri_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY position_arri_date DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByArri_dateDESC(@Param("start_date") Date start_date, 
                                                              @Param("end_date") Date end_date, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */















  /** @name 検索種別[職種名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[職種名]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[職種名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[職種名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE job LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByJobCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[職種名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[職種名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[職種名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[職種名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE job LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY job ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByJob(@Param("word") String word, 
                                                    @Param("limit") Integer limit, 
                                                    @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[職種名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[職種名]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[職種名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[職種名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE job LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY job DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByJobDESC(@Param("word") String word, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("offset") Integer offset);

  /** @} */















  /** @name 検索種別[配属部署]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属部署]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[配属部署]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[配属部署]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE assign_dept LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAssign_deptCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属部署]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[配属部署]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[配属部署]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[配属部署]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE assign_dept LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY assign_dept ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAssign_dept(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属部署]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[配属部署]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[配属部署]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[配属部署]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE assign_dept LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY assign_dept DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAssign_deptDESC(@Param("word") String word, 
                                                                @Param("limit") Integer limit, 
                                                                @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[配属日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[配属日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[配属日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[配属日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE assign_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByAssign_dateCount(@Param("start_date") Date start_date, 
                                                       @Param("end_date") Date end_date, 
                                                       @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[配属日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[配属日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[配属日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[配属日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */   
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE assign_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY assign_date ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAssign_date(@Param("start_date") Date start_date, 
                                                            @Param("end_date") Date end_date, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[配属日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[配属日]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[配属日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[配属日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[配属日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE assign_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY assign_date DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByAssign_dateDESC(@Param("start_date") Date start_date, 
                                                                @Param("end_date") Date end_date, 
                                                                @Param("limit") Integer limit, 
                                                                @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[担当楽器]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[担当楽器]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[担当楽器]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[担当楽器]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Member_Info" 
        + " WHERE inst_charge LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByInst_chargeCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[担当楽器]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[担当楽器]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[担当楽器]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[担当楽器]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE inst_charge LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY inst_charge ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByInst_charge(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[担当楽器]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[担当楽器]で行い、その次に[団員番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[担当楽器]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[担当楽器]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Member_Info" 
      + " WHERE inst_charge LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY inst_charge DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByInst_chargeDESC(@Param("word") String word, 
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
        + " SELECT * FROM Member_Info" 
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
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[団員番号]で行う。
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
  @Query("SELECT * FROM Member_Info" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment ASC, member_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByComment(@Param("word") String word, 
                                                        @Param("limit") Integer limit, 
                                                        @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[その他コメント]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[団員番号]で行う。
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
  @Query("SELECT * FROM Member_Info" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment DESC, member_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Member_Info>> findAllByCommentDESC(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);

  /** @} */














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
  @Query("SELECT COUNT(*) FROM Member_Info" 
      + " WHERE serial_num != :serial_num " 
         + "AND member_id = :member_id " 
       + "LIMIT 1")
  Integer checkUnique(@Param("serial_num") Integer serial_num, @Param("member_id") String member_id);
}