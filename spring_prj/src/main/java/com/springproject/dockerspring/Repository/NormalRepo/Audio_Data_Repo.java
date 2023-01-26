/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Repository.NormalRepo
 * 
 * @brief データベースにアクセスするリポジトリのうち、[通常情報の操作]に用いるリポジトリを格納した
 * パッケージ
 * 
 * @details
 * - このパッケージでは、データベースに保存されている通常データのCRUD処理を行う機能を格納する。
 * また、システムアカウントの管理や、権限情報の管理を行う。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.NormalRepo;





/** 
 **************************************************************************************
 * @file Audio_Data_Repo.java
 * @brief 主に[音源データ情報]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.SubjectEnum.Audio_Data_Subject;






/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能で使用する、データベースとの通信を行うリポジトリの
 * インターフェースである。
 * 
 * @details 
 * - このリポジトリが対応するデータベース内のテーブルの名称は[Audio_Data]である。
 * - SpringDataJDBCを採用し、ネイティブなSQLの作成を行っている。
 * - なお、このインターフェースの実装クラスは使用時にSpringFrameworkによって自動的に作成される。
 * そのため、明示的に実装しているクラスはこのシステム上に存在しない。
 * 
 * @see Audio_Data
 * @see FindAllCrudRepository
 **************************************************************************************
 */ 
public interface Audio_Data_Repo extends FindAllCrudRepository<Audio_Data, Integer>{


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
  public default FindAllResult<Audio_Data> findAllBranch(FindAllParam param) throws ExecutionException, InterruptedException{

    String main_word = param.getMain_word();
    String sub_word = param.getSub_word();
    String subject = param.getSubject();
    Boolean order = param.getOrder();
    Integer max = param.getMax();
    Integer limit = param.getLimit();
    Integer offset = param.getOffset();

    Optional<Audio_Data_Subject> form_enum = Arrays.stream(Audio_Data_Subject.values())
                                                   .parallel()
                                                   .filter(s -> s.name().equals(subject))
                                                   .findFirst();

    if(form_enum.isEmpty() || order == null || max == null || limit == null || offset == null){
      throw new IllegalArgumentException("Error location [Audio_Data_Repo:findAllBranch(IllegalArgumentException)]");
    }


    CompletableFuture<List<Audio_Data>> async_result = null;
    CompletableFuture<Integer> async_count = null;
    List<Audio_Data> result = null;
    Integer count = null;

    try{
      switch(form_enum.get()){
        case All:
          async_result = order ? findBySound_id(main_word, limit, offset) : 
                                 findBySound_idDESC(main_word, limit, offset);
          async_count = findBySound_idCount(main_word, max);
          break;

        case SoundName:
          async_result = order ? findAllBySound_name(main_word, sub_word, limit, offset) : 
                                 findAllBySound_nameDESC(main_word, sub_word, limit, offset);
          async_count = findAllBySound_nameCount(main_word, sub_word, max);
          break;
      }

      CompletableFuture.allOf(async_result, async_count).join();
      result = async_result == null ? null : async_result.get();
      count = async_count == null ? null : async_count.get();

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [Audio_Data_Repo:findAllBranch(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Audio_Data_Repo:findAllBranch](InterruptedException)" + "\n" + e);
    }

    return new FindAllResult<Audio_Data>(result, count);
  }







  

  /** @name 検索種別[完全一致の音源番号]使用時のクエリ実行メソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 検索種別を[完全一致の音源番号]に指定し、全検索結果数をカウントする。
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
   * @param[in] sound_id 検索対象の[音源番号]
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
        + " SELECT * FROM Audio_Data" 
        + " WHERE sound_id = :sound_id" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findBySound_idCount(@Param("sound_id") String sound_id, @Param("max") Integer max);
  

  /** 
   **********************************************************************************************
   * @brief 検索種別を[完全一致の音源番号]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替えの対象のカラムは[音源データ名]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] sound_id 検索対象の[音源番号]
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
  @Query("SELECT * FROM Audio_Data" 
      + " WHERE sound_id = :sound_id" 
      + " ORDER BY sound_name ASC, serial_num ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Audio_Data>> findBySound_id(@Param("sound_id") String sound_id, 
                                                     @Param("limit") Integer limit, 
                                                     @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[完全一致の音源番号]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替えの対象のカラムは[音源データ名]とする。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] sound_id 検索対象の[音源番号]
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
  @Query("SELECT * FROM Audio_Data" 
      + " WHERE sound_id = :sound_id" 
      + " ORDER BY sound_name DESC, serial_num DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Audio_Data>> findBySound_idDESC(@Param("sound_id") String sound_id, 
                                                         @Param("limit") Integer limit, 
                                                         @Param("offset") Integer offset);

  /** @} */











  /** @name 検索種別[音源データ名]使用時のクエリ実行メソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源データ名]に指定し、全検索結果数をカウントする。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡された検索結果最大値以上はカウントできないようになっている。
   * - [完全一致の音源番号]かつ、指定の[音源データ名]を部分一致で取得する。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] sound_id 検索対象の[音源番号]
   * @param[in] word 検索対象の[音源データ名]
   * @param[in] max 検索結果をカウント可能な最大数
   * 
   * @return 全検索結果カウント処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 副問問い合わせにより、指定の[音源番号][音源データ名]のデータを検索結果最大数まで選択。
   * -# 副問問い合わせの取得結果をカウントしたものを最終結果として出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT COUNT(*) FROM" 
      + " (" 
        + " SELECT * FROM Audio_Data" 
        + " WHERE sound_id = :sound_id" 
          + " AND sound_name LIKE CONCAT('%', :word, '%')" 
        + " LIMIT :max" 
      + " )" 
      + " AS sub")
  CompletableFuture<Integer> findAllBySound_nameCount(@Param("sound_id") String sound_id, 
                                                      @Param("word") String word, 
                                                      @Param("max") Integer max);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源データ名]に指定し、オフセット制限値の範囲内で検索結果データを[昇順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替えの対象のカラムは[音源データ名]とする。
   * - [完全一致の音源番号]かつ、指定の[音源データ名]を部分一致で取得する。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] sound_id 検索対象の[音源番号]
   * @param[in] word 検索対象の[音源データ名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源番号][音源データ名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT * FROM Audio_Data" 
      + " WHERE sound_id = :sound_id" 
        + " AND sound_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY sound_name ASC, serial_num ASC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Audio_Data>> findAllBySound_name(@Param("sound_id") String sound_id,  
                                                          @Param("word") String word, 
                                                          @Param("limit") Integer limit, 
                                                          @Param("offset") Integer offset);


  /** 
   **********************************************************************************************
   * @brief 検索種別を[音源データ名]に指定し、オフセット制限値の範囲内で検索結果データを[降順]に取得。
   * 
   * @details
   * - 非同期実行を可能とする。
   * - 引数として渡されたオフセット値の位置からデータ取得を行い、制限数に至ったら取得を中断する。
   * - 並び替えの対象のカラムは[音源データ名]とする。
   * - [完全一致の音源番号]かつ、指定の[音源データ名]を部分一致で取得する。
   * - 実際の処理は、アノテーションに記載したクエリ文字列によってデータベースに問い合わせを行うことで実現する。
   * 
   * @par 使用アノテーション
   * - @Async("Repo")
   * - @Query(...) ※...内にクエリ文字列を記載
   * - @Param(...) ※...内にプリペアードステートメント名を記載
   * 
   * @param[in] sound_id 検索対象の[音源番号]
   * @param[in] word 検索対象の[音源データ名]
   * @param[in] limit 一回の検索で取得可能な検索結果数
   * @param[in] offset 結果取得を開始するデータの位置
   * 
   * @return 検索結果取得処理を、非同期実行クラスに格納した物。
   * 
   * @par 処理の大まかな流れ
   * -# 指定の[音源番号][音源データ名]で、オフセットの範囲内のデータを出力する。
   **********************************************************************************************
   */
  @Async("Repo")
  @Query("SELECT * FROM Audio_Data" 
      + " WHERE sound_id = :sound_id" 
        + " AND sound_name LIKE CONCAT('%', :word, '%')" 
      + " ORDER BY sound_name DESC, serial_num DESC" 
      + " LIMIT :limit" 
      + " OFFSET :offset")
  CompletableFuture<List<Audio_Data>> findAllBySound_nameDESC(@Param("sound_id") String sound_id, 
                                                              @Param("word") String word, 
                                                              @Param("limit") Integer limit, 
                                                              @Param("offset") Integer offset);

  /** @} */
}