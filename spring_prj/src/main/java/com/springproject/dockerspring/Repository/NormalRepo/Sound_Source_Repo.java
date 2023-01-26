/** 
 **************************************************************************************
 * @file Sound_Source_Repo.java
 * @brief 主に[音源管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Sound_Source_Subject;

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
 * @brief 主に[音源管理]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Sound_Source]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Sound_Source
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Sound_Source_Repo extends FindAllCrudRepository<Sound_Source, Integer>{


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
  public default FindAllResult<Sound_Source> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException, ParseException{

    String main_word = param.getMain_word();
    String sub_word = param.getSub_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<Sound_Source_Subject> form_enum = Arrays.stream(Sound_Source_Subject.values())
                                                     .parallel()
                                                     .filter(s -> s.name().equals(subject))
                                                     .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Sound_Source_Repo:findAllBranch(IllegalArgumentException)]");
    }

    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());
    CompletableFuture<List<Sound_Source>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Sound_Source> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findAllOriginal(limit, offset) : 
                                 findAllOriginalDESC(limit, offset);
          async_count = findAllOriginalCount(max);
          break;

        case SoundId:
          async_result = order ? findAllBySound_id(main_word, limit, offset) : 
                                 findAllBySound_idDESC(main_word, limit, offset);
          async_count = findAllBySound_idCount(main_word, max);
          break;

        case UploadDate:
          Date start_date = main_word == null ? null : parse_date.parse(main_word);
          Date end_date = sub_word == null ? null : parse_date.parse(sub_word);
          async_result = order ? findAllByUpload_date(start_date, end_date, limit, offset) : 
                                 findAllByUpload_dateDESC(start_date, end_date, limit, offset);
          async_count = findAllByUpload_dateCount(start_date, end_date, max);
          break;

        case SongTitle:
          async_result = order ? findAllBySong_title(main_word, limit, offset) : 
                                 findAllBySong_titleDESC(main_word, limit, offset);
          async_count = findAllBySong_titleCount(main_word,  max);
          break;

        case Composer:
          async_result = order ? findAllByComposer(main_word, limit, offset) : 
                                 findAllByComposerDESC(main_word, limit, offset);
          async_count = findAllByComposerCount(main_word, max);
          break;

        case Performer:
          async_result = order ? findAllByPerformer(main_word, limit, offset) : 
                                 findAllByPerformerDESC(main_word, limit, offset);
          async_count = findAllByPerformerCount(main_word, max);
          break;

        case Publisher:
          async_result = order ? findAllByPublisher(main_word, limit, offset) : 
                                 findAllByPublisherDESC(main_word, limit, offset);
          async_count = findAllByPublisherCount(main_word, max);
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
      throw new ExecutionException("Error location [Sound_Source_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Sound_Source_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }catch(ParseException e) {
      throw new ParseException("Error location [Sound_Source_Repo:findAllBranch(ParseException)]" + "\n" + e, e.getErrorOffset());
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Sound_Source_Repo:findAllBranch(NullPointerException)]" + "\n" + e);
    }

    return new FindAllResult<Sound_Source>(result, count);
  }












  /** 
   **********************************************************************************************
   * @brief [音源番号]に完全一致するデータを一件取得する。
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
   * @param[in] sound_id 検索対象の[音源番号]
   * 
   * @return [音源番号]に合致したデータ一件
   * 
   * @par 処理の大まかな流れ
   * -# [音源番号]に完全一致するデータを一件まで取得し、出力する。
   **********************************************************************************************
   */
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE sound_id = :sound_id LIMIT 1")
  Optional<Sound_Source> findBySound_Id(@Param("sound_id") String sound_id);












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
        + " SELECT * FROM Sound_Source" 
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
   * - 並び替え対象のカラムは[音源番号]とする。
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
  @Query("SELECT * FROM Sound_Source" 
      + " ORDER BY sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllOriginal(@Param("limit") Integer limit, @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[全取得]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[音源番号]とする。
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
  @Query("SELECT * FROM Sound_Source" 
      + " ORDER BY sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllOriginalDESC(@Param("limit") Integer limit, @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[音源番号]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源番号]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[音源番号]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[音源番号]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE sound_id LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllBySound_idCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[音源番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[音源番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */    
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE sound_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllBySound_id(@Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは[音源番号]とする。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[音源番号]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源番号]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */    
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE sound_id LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllBySound_idDESC(@Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[登録日]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[登録日]に指定し、全検索結果数をカウントする。
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
   * @param[in] start_date 検索対象の[登録日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[登録日]の検索対象期間の終了日
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[登録日]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE upload_date BETWEEN :start_date AND :end_date" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByUpload_dateCount(@Param("start_date") Date start_date, 
                                                       @Param("end_date") Date end_date, 
                                                       @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[登録日]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[登録日]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[登録日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[登録日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[登録日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE upload_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY upload_date ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByUpload_date(@Param("start_date") Date start_date, 
                                                             @Param("end_date") Date end_date, 
                                                             @Param("limit") Integer limit, 
                                                             @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[登録日]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[登録日]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] start_date 検索対象の[登録日]の検索対象期間の開始日
   * @param[in] end_date 検索対象の[登録日]の検索対象期間の終了日
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[登録日]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE upload_date BETWEEN :start_date AND :end_date" 
      + " ORDER BY upload_date DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByUpload_dateDESC(@Param("start_date") Date start_date, 
                                                                 @Param("end_date") Date end_date, 
                                                                 @Param("limit") Integer limit, 
                                                                 @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[曲名]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[曲名]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[曲名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[曲名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE song_title LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllBySong_titleCount(@Param("word") String word, @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[曲名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[曲名]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[曲名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[曲名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE song_title LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY song_title ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllBySong_title(@Param("word") String word, 
                                                            @Param("limit") Integer limit, 
                                                            @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[曲名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[曲名]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[曲名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[曲名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE song_title LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY song_title DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllBySong_titleDESC(@Param("word") String word, 
                                                                @Param("limit") Integer limit, 
                                                                @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[作曲者]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[作曲者]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[作曲者]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[作曲者]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE composer LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByComposerCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[作曲者]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[作曲者]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[作曲者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[作曲者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE composer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY composer ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByComposer(@Param("word") String word, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[作曲者]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[作曲者]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[作曲者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[作曲者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE composer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY composer DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByComposerDESC(@Param("word") String word, 
                                                               @Param("limit") Integer limit, 
                                                               @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[演奏者]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[演奏者]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[演奏者]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[演奏者]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE performer LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByPerformerCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[演奏者]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[演奏者]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[演奏者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[演奏者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE performer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY performer ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByPerformer(@Param("word") String word, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[演奏者]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[演奏者]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[演奏者]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[演奏者]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE performer LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY performer DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByPerformerDESC(@Param("word") String word, 
                                                               @Param("limit") Integer limit, 
                                                               @Param("offset") Integer offset);

  /** @} */














  /** @name 検索種別[出版社]使用時のクエリ実行メソッド */
  /** @{ */
    
  /** 
   **********************************************************************************************
   * @brief 検索種別を[出版社]に指定し、全検索結果数をカウントする。
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
   * @param[in] word 検索対象の[出版社]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[出版社]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Sound_Source" 
        + " WHERE publisher LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllByPublisherCount(@Param("word") String word, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[出版社]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[出版社]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[出版社]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[出版社]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */  
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE publisher LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY publisher ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByPublisher(@Param("word") String word, 
                                                           @Param("limit") Integer limit, 
                                                           @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[出版社]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[出版社]で行い、その次に[音源番号]で行う。
   * - 検索方法としては、部分一致検索としている。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] word 検索対象の[出版社]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[出版社]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */ 
  @Async("Repo")
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE publisher LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY publisher DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByPublisherDESC(@Param("word") String word, 
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
        + " SELECT * FROM Sound_Source" 
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
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[音源番号]で行う。
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
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment ASC, sound_id ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByComment(@Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[その他コメント]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替え対象のカラムは、優先的に[その他コメント]で行い、その次に[音源番号]で行う。
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
  @Query("SELECT * FROM Sound_Source" 
      + " WHERE other_comment LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY other_comment DESC, sound_id DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Sound_Source>> findAllByCommentDESC(@Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */













  /** 
   **********************************************************************************************
   * @brief 指定した[音源番号][シリアルナンバー]で更新しようとした際に、データベース内で重複しないこと
   * を確認する。
   * 
   * @details
   * - 特に使用する機会がないので、非同期処理は実装しない。
   * - このメソッドは、主にフォームバリデーションでの[音源番号]の重複チェックに用いられる。
   * - [音源番号]とペアになっている[シリアルナンバー]を一緒に指定するのは、対象の[音源番号]がすでに
   * 保存されていてそれを更新したいといった場合に、その格納カラムまで判定してしまい重複と判定されてしまう
   * のを防ぐためである。
   * - 前項の対策によって「指定された[シリアルナンバー]を除外して[音源番号]を検索する」ことが可能になる。
   * - 検索方法としては、[音源番号]は完全一致、[シリアルナンバー]は否定完全一致とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] serial_num 指定の[音源番号]とペアになっている[シリアルナンバー]
   * @param[in] sound_id 指定の[シリアルナンバー]とペアになっている[音源番号]
   * 
   * @return 検索後の結果件数。重複ありの場合は「1」が返り、重複なしの場合は「0」が返る。
   * 
   * @par 処理の大まかな流れ
   * -# [音源番号]は完全一致、[シリアルナンバー]は否定完全一致として検索し、結果件数を取得する。
   **********************************************************************************************
   */ 
  @Query("SELECT COUNT(*) FROM Sound_Source" 
      + " WHERE serial_num != :serial_num " 
         + "AND sound_id = :sound_id " 
       + "LIMIT 1")
  Integer checkUnique(@Param("serial_num") Integer serial_num, @Param("sound_id") String sound_id);
}