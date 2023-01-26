/** 
 **************************************************************************************
 * @file Musical_Score_ServiceImpl.java
 * @brief 主に[楽譜管理機能]が使用するサービスの機能を実装するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceImpl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Musical_Score_Csv;
import com.springproject.dockerspring.FileIO.CompInterface.Musical_Score_Pdf;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.CsvImplForm.CsvCheckResult;
import com.springproject.dockerspring.Form.CsvImplForm.Musical_Score_Form;
import com.springproject.dockerspring.Form.FormInterface.Musical_Score_Csv_Form;
import com.springproject.dockerspring.Form.FormInterface.Musical_Score_Form_Output_Validator;
import com.springproject.dockerspring.Form.NormalForm.Com_Hist_Get_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Common_Csv_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.Rollback_Form;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.HistoryRepo.Musical_Score_History_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Musical_Score_Repo;
import com.springproject.dockerspring.Service.FuncWrap;
import com.springproject.dockerspring.Service.ManyParam;
import com.springproject.dockerspring.Service.CommonMethods.CommonUtilsService;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.ServiceInterface.Musical_Score_Service;

import lombok.RequiredArgsConstructor;











/** 
 **************************************************************************************
 * @brief 主に[楽譜管理機能]が使用するサービスの機能を実装するクラス
 * 
 * @details 
 * - このクラスでは、コントローラーの直下で実行する処理をサービスクラスとして定義する。
 * - 内容としては、このシステム内に実装されている各機能を集約し、コントローラー側からすぐに
 * 使用できる為にする実装である。
 * - なお、このクラスは多くのクラスをDIしており、このクラス自体の責務が大きいことになっているが
 * 、サービスクラスの特性上、致し方ないものとする。ここである程度集約しておかないと、コントローラー
 * 側の処理が膨大になってしまうからである。
 * 
 * @see Musical_Score_Service
 * @see CommonUtilsService
 * @see Musical_Score_Repo
 * @see Musical_Score_History_Repo
 * @see Musical_Score_Pdf
 * @see Musical_Score_Csv
 * @see Musical_Score_Csv_Form
 * @see Environment_Config
 * @see Musical_Score_Form_Output_Validator
 * 
 * @par 使用アノテーション
 * - @Service
 * - @Transactional(rollbackFor = Exception.class)
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Musical_Score_ServiceImpl implements Musical_Score_Service{

  private final CommonUtilsService common_utils;
  private final Musical_Score_Repo normal_repo;
  private final Musical_Score_History_Repo hist_repo;
  private final Musical_Score_Pdf pdf_comp;
  private final Musical_Score_Csv csv_comp;
  private final Musical_Score_Csv_Form csv_form;
  private final Environment_Config config;
  private final Musical_Score_Form_Output_Validator validator;











  /** 
   **********************************************************************************************
   * @brief 指定された管理番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した管理番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] id 指定する管理番号
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号を元に検索を行い、該当するデータを個別に取得する。
   * -# 取得したデータを出力時バリデーションに投入し、一つでもエラーが発生した場合は処理を中断する。
   * -# 正常だった場合は、検索結果のエンティティを戻り値とする。
   * 
   * @see Musical_Score
   * 
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public Musical_Score selectData(String id) throws Exception, DataEmptyException, OutputDataFailedException {

    try{
      Musical_Score entity = this.normal_repo.findByScore_Id(id)
                                             .orElseThrow(() -> new DataEmptyException());
      Boolean judge = this.common_utils.outputValidCom(entity, this.validator, null, null);

      if(judge){
        return entity;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("musical score failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:selectData]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief フォームから渡された情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   * @param[in] hist_kind 履歴情報を登録する際の指定する履歴種別
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームクラス内の情報を使用して、データ格納用のエンティティを作成する。
   * -# 作成したエンティティをデータベースに保存し、その際に保存が成功した後のエンティティを取得する。
   * -# 保存後のエンティティを元に、履歴用のエンティティを作成し、引数で指定されて履歴種別で履歴情報を
   * 保存する。
   * 
   * @see Musical_Score
   * @see Musical_Score_History
   * @see Musical_Score_Form
   * 
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  public void changeData(Musical_Score_Form form, History_Kind_Enum hist_kind) throws Exception {

    try{
      Musical_Score entity = this.common_utils.changeDatabaseCom(this.normal_repo, new Musical_Score(form));
      this.common_utils.changeDatabaseCom(this.hist_repo, new Musical_Score_History(entity, 
                                                                                    hist_kind, 
                                                                                    this.common_utils.getLoginUser(), 
                                                                                    new Date()));
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:changeData]" + "\n" + e);
    }
  }












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
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの検索ワードを元に、データベースの検索を行い結果リストを取得する。
   * -# 取得した結果を、出力時の専用フォームクラスに、並列処理で移し替える。
   * -# 結果をすべて出力時バリデーションに順次投入する。この時点で、一つでもエラーが発生した場合は処理を
   * 中止する。
   * -# 正常だった場合は、結果リストを戻り値とする。
   * 
   * @see Com_Search_Form
   * @see FindAllResult
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FindAllResult<Musical_Score> selectAllData(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<Musical_Score> result = this.common_utils.selectAllDataCom(this.normal_repo, form);

      Boolean judge = result.getResult_list()
                            .parallelStream()
                            .allMatch(s -> this.common_utils.outputValidCom(s, this.validator, null, null));

      if(judge){
        return result;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(ValidationException e){
      throw new OutputDataFailedException("musical score failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:selectAllData]" + "\n" + e);
    }
  }












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
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの検索ワードを元に、データベースの検索を行い結果リストを取得する。
   * -# 取得した結果を、出力時の専用フォームクラスに、並列処理で移し替える。
   * -# 結果をすべて出力時バリデーションに順次投入する。この時点で、一つでもエラーが発生した場合は処理を
   * 中止する。
   * -# 正常だった場合は、結果リストを戻り値とする。
   * 
   * @see Com_Hist_Get_Form
   * @see FindAllResult
   * @see Musical_Score_History
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FindAllResult<Musical_Score_History> selectHist(Com_Hist_Get_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<Musical_Score_History> result = this.common_utils.selectHistCom(this.hist_repo, form);

      Predicate<Musical_Score_History> append_func = s -> {
        return this.common_utils.outputValidHistInfoCom(s.getHistory_id(), 
                                                        s.getChange_datetime(), 
                                                        s.getChange_kinds(), 
                                                        s.getOperation_user());
      };

      Boolean judge = result.getResult_list()
                            .parallelStream()
                            .allMatch(s -> this.common_utils.outputValidCom(s, this.validator, append_func, s));

      if(judge){
        return result;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(ValidationException e){
      throw new OutputDataFailedException("musical score history failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:selectHist]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号の履歴情報を、通常データにロールバックして復元する。
   * 
   * @details
   * - このメソッドは、誤操作時などに以前の情報を履歴から復元し、ユーザビリティを高めるためにある。
   * - ロールバック処理を行った結果、該当の履歴情報が存在しなかった場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form ロールバック時に必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通のロールバックメソッドに渡して処理を実行する。
   * 
   * @see Rollback_Form
   * @see Musical_Score
   * @see Musical_Score_History
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OverCountException
   **********************************************************************************************
   */
  @Override
  public void rollbackData(Rollback_Form form) throws Exception, DataEmptyException {

    try{
      Predicate<Musical_Score_History> new_append_check = s -> {
        return this.normal_repo.findByScore_Id(s.getScore_id()).isPresent();
      };

      Predicate<Musical_Score_History> latest_serial_check = s -> {
        Optional<Musical_Score> normal_ent = this.normal_repo.findById(s.getSerial_num());
    
        return normal_ent.isPresent() 
               && normal_ent.get().getScore_id().equals(s.getScore_id());
      };

      Consumer<Musical_Score_History> serial_latest = FuncWrap.throwConsumer(s -> {
        Musical_Score latest = this.normal_repo.findByScore_Id(s.getScore_id())
                                               .orElseThrow(() -> new DataEmptyException());

        s.setSerial_num(latest.getSerial_num());
      });

      Predicate<Musical_Score_History> rollback_output_valid_normal = o -> {
        Predicate<Musical_Score_History> append_func = s -> {
          return this.common_utils.outputValidHistInfoCom(s.getHistory_id(), 
                                                          s.getChange_datetime(), 
                                                          s.getChange_kinds(), 
                                                          s.getOperation_user());
        };

        return this.common_utils.outputValidCom(o, this.validator, append_func, o);
      };


      ManyParam<Musical_Score, Musical_Score_History> many_param = ManyParam.
        <Musical_Score, Musical_Score_History>builder()
        .rollback_form(form)
        .normal_repo(this.normal_repo)
        .hist_repo(this.hist_repo)
        .new_append_check(new_append_check)
        .latest_serial_check(latest_serial_check)
        .serial_null(s -> s.setSerial_num(null))
        .serial_latest(serial_latest)
        .new_serial_out(Musical_Score::getSerial_num)
        .new_hist_id_out(Musical_Score_History::getHistory_id)
        .normal_entity_refill(s -> new Musical_Score(s))
        .hist_entity_refill(s -> new Musical_Score_History(s, History_Kind_Enum.ROLLBACK, 
                                                           this.common_utils.getLoginUser(), 
                                                           new Date()))
        .rollback_output_valid_normal(rollback_output_valid_normal)
        .build();

      this.common_utils.normalRollbackDataCom(many_param);

    }catch(DataEmptyException | NoSuchElementException e) {
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("musical score history rollback failed" + "\n" + e);
    }catch (Exception e) {
      throw new Exception("Error location [Musical_Score_ServiceImpl:rollbackData]" + "\n" + e);
    }
  }












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
  @Override
  public Map<String, String> parseString(EntitySetUp entity) throws Exception {

    try {
      return this.common_utils.parseStringCom(entity);
    } catch (Exception e) {
      throw new Exception("Error location [Musical_Score_ServiceImpl:parseString]" + "\n" + e);
    }
  }











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
  @Override
  public List<Map<String, String>> mapPacking(List<EntitySetUp> data_list) throws Exception {

    try {
      return this.common_utils.mapPackingCom(data_list);
    } catch (Exception e) {
      throw new Exception("Error location [Musical_Score_ServiceImpl:mapPacking]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された管理番号に対応する情報を、PDFシートとして出力する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した管理番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したPDFシートファイル
   * 
   * @param[in] id 指定する管理番号
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号を元に検索を行い、該当するデータを個別に取得する。
   * -# 取得したデータを出力時バリデーションに投入し、一つでもエラーが発生した場合は処理を中断する。
   * -# 正常だった場合は、検索結果のエンティティを用いてPDFデータを作成する。
   * -# 作成したPDFファイルに環境変数で指定されているファイル名を付けて戻り値とする。
   * 
   * @see FileEntity
   * @see Musical_Score
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FileEntity outputPdf(String id) throws Exception, DataEmptyException, OutputDataFailedException {

    try{
      Musical_Score entity = this.normal_repo.findByScore_Id(id)
                                             .orElseThrow(() -> new DataEmptyException());

      Boolean judge = this.common_utils.outputValidCom(entity, this.validator, null, null);

      if(!judge){
        throw new OutputDataFailedException("output data failed");
      }

      File file = this.common_utils.outputPdfCom(this.pdf_comp, entity);

      return new FileEntity(this.config.getMusical_score_pdf() + "." + Datatype_Enum.PDF.getExtension(), file);

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("musical score failed");
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:outputPdf]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 渡されたCSVファイルの情報を読み込み、読み込んだ内容をデータベースに保存する。
   * 
   * @details
   * - このメソッドは、登録したい情報をCSVに記述してシステムに投入することで、大量のデータをまとめて保存できる
   * 機能を提供する。
   * - 渡されたCSVファイルに不正があり、受付をすることができない場合は特定の例外を発生させ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return CSVを取り込んだ際の、エラーが発生した座標や管理番号の重複の有無の情報。なお、取り込みが成功
   * した場合は、「Null」を返却する。
   * 
   * @param[in] form CSVファイルが格納されたフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# CSV入力の共通処理側で実行する関数型インターフェースを準備する。処理の内容としては、渡されたエンティティの
   * リストを通常データに新規保存した後、自動連番で番号が割り振られたエンティティを履歴として保存する。
   * -# 作成した関数型インターフェースと、フォームクラスに格納されているCSVファイルを共通処理に渡して処理を
   * 実行する。
   * -# CSV内のデータをバリデーションした際の、表内のエラーの座標を記したマップリストを戻り値とする。もし
   * エラーがなければ戻り値はNullとなる。
   * 
   * @note 
   * - ラムダ式内に例外キャッチ処理があり、検査例外を非検査例外に変換して外部に出しているが、このコーディングは
   * 余り推奨されるものではない。だが今回のケースは、並列処理ではなく順次処理としており、影響は少ないと判断
   * した為、この方法を採用する。
   * 
   * @see CsvCheckResult
   * @see Musical_Score
   * @see Musical_Score_History
   *
   * @throw Exception
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  @Override
  public CsvCheckResult inputCsv(Common_Csv_Form form) throws Exception, InputFileDifferException{

    try{
      Consumer<List<Musical_Score>> goto_database = FuncWrap.throwConsumer(list -> {
        List<Musical_Score> entities = (List<Musical_Score>)this.normal_repo.saveAll(list);

        List<Musical_Score_History> hist_list = 
          entities.parallelStream()
                  .map(s -> new Musical_Score_History(s, History_Kind_Enum.INSERT, 
                                                      this.common_utils.getLoginUser(), new Date()))
                  .toList();

        this.hist_repo.saveAll(hist_list);
      });

      return this.common_utils.inputCsvCom(goto_database, this.csv_comp, this.csv_form, form);

    }catch(InputFileDifferException e){
      throw new InputFileDifferException("musical score input csv failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:inputCsv]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief データベース内に保存されている指定されたデータを、一覧としてCSVファイルに書き込み出力する。
   * 
   * @details
   * - このメソッドは、現在保存中のデータを一括して取り出したい際に用いる。
   * - このメソッド内部でデータ検索を実行し、その結果をそのままCSVとして出力する。
   * - 検索した結果、結果が存在しない場合や、出力しようとしたデータに不正があり出力ができない場合は、特定の
   * 例外を投げ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したCSVファイルデータ
   * 
   * @param[in] form 出力対象となるデータを検索する際に必要な情報が入ったフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# CSV出力の共通処理側で実行する関数型インターフェースを準備する。処理内容としては、引数で渡された
   * ページ数を元にデータベース内のデータをフォームクラス内の検索ワードで検索し、取得したデータを出力時
   * バリデーションしたうえで文字列変換し、マップリストとして返却するものである。
   * -# 作成した関数型インターフェースを共通処理に渡し、CSV出力処理を実行する。
   * -# 作成したCSVファイルに環境変数で指定されているファイル名を付けて戻り値とする。
   * 
   * @note 
   * - ラムダ式内に例外キャッチ処理があり、検査例外を非検査例外に変換して外部に出しているが、このコーディングは
   * 余り推奨されるものではない。だが今回のケースは、並列処理ではなく順次処理としており、影響は少ないと判断
   * した為、この方法を採用する。
   * 
   * @see FileEntity
   * @see Musical_Score
   * @see FindAllResult
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FileEntity outputCsv(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException {

    try{
      Function<Integer, List<Map<String, String>>> generate = FuncWrap.throwFunction(i -> {
        Predicate<Musical_Score> valid_func = s -> {
          return this.common_utils.outputValidCom(s, this.validator, null, null);
        };

        FindAllResult<Musical_Score> result = this.common_utils.outputCsvCom(this.normal_repo, form, i, valid_func);
        return this.common_utils.mapPackingCom(result.getResult_list().stream().map(s -> (EntitySetUp)s).toList());
      });


      File file = this.common_utils.outputCsvCom(this.csv_comp, generate);

      return new FileEntity(this.config.getMusical_score_csv() + "." + Datatype_Enum.CSV.getExtension(), file);

    }catch(NoSuchElementException e){
      throw new DataEmptyException();
    }catch(SecurityException e){
      throw new OutputDataFailedException("musical score failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:outputCsv]" + "\n" + e);
    }
  }









  

  /** 
   **********************************************************************************************
   * @brief このシステム内に大量のデータを投入する際に用いるCSVを作成するうえで便利な、CSVファイルの
   * テンプレートを作成する。
   * 
   * @details
   * - CSVファイルをこのシステムに投入する際に、CSVファイルの体裁がわかってないとエラーが発生しやすくなるため、
   * ユーザー補助として、このテンプレートにデータを記述して投入すればエラーが発生しにくくなる。
   * - 出力しようとしたデータに不正があり出力ができない場合は、特定の例外を投げ、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成したCSVテンプレートデータ
   * 
   * @par 大まかな処理の流れ
   * -# CSVテンプレート出力の共通処理を実行し、ファイルを取得する。
   * -# 作成したCSVテンプレートファイルに環境変数で指定されているファイル名を付けて戻り値とする。
   * 
   * @see FileEntity
   *
   * @throw Exception
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FileEntity outputCsvTemplate() throws Exception, OutputDataFailedException {

    try{
      File file = this.common_utils.outputCsvCom(this.csv_comp, null);
      return new FileEntity(this.config.getMusical_score_csv() + "." + Datatype_Enum.CSV.getExtension(), file);

    }catch(InputFileDifferException e){
      throw new OutputDataFailedException("musical score failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:outputCsvTemplate]" + "\n" + e);
    }
  }











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
   * @par 大まかな処理の流れ
   * -# 渡されたフォームクラスから削除対象となるシリアルナンバーを取得する。取得する番号は、リストのうち最初の
   * 物とする。
   * -# 抽出したシリアルナンバーを使って検索を行い、削除対象のエンティティを取得して、そのエンティティで
   * データを削除する。
   * -# 削除したという履歴を残すために、取得したエンティティを履歴用エンティティに詰め替え、履歴情報を
   * 登録する。
   * 
   * @see Delete_Form
   * @see Musical_Score
   * @see Musical_Score_History
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Override
  public void deleteData(Delete_Form form) throws Exception, DataEmptyException {

    try{
    Musical_Score entity = this.normal_repo.findById(form.getSerial_num_list().get(0).getSerial_num())
                                           .orElseThrow(() -> new DataEmptyException());

    this.common_utils.deleteDatabaseCom(this.normal_repo, entity);
    this.common_utils.changeDatabaseCom(this.hist_repo, new Musical_Score_History(entity, History_Kind_Enum.DELETE, 
                                                                                  this.common_utils.getLoginUser(), new Date()));

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(Exception e){
      throw new Exception("Error location [Musical_Score_ServiceImpl:deleteData]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された期間内の履歴データを削除する。
   * 
   * @details
   * - このメソッドに関しては、原則管理者ページからのアクセスからしか実行しない。
   * - 他のテーブルの履歴情報をまとめて削除する場合があるので、並列処理ができるようにする。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 並列処理のスレッドが入ったインスタンス(戻り値無し)
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @par 使用アノテーション
   * - @Async("Service")
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームクラス内の、削除対象期間データを持ちいて、データベース上の該当期間の履歴情報を削除する。
   * 
   * @see Del_Indivi_Hist_Form
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Async("Service")
  @Override
  public CompletableFuture<Void> deleteHistory(Del_Indivi_Hist_Form form) throws Exception{

    try {
      this.hist_repo.deleteByDateBetween(form.getStart_datetime(), form.getEnd_datetime());
      return CompletableFuture.completedFuture(null);

    } catch (Exception e) {
      throw new Exception("Error location [Musical_Score_ServiceImpl:deleteHistory]" + "\n" + e);
    }
  }
}