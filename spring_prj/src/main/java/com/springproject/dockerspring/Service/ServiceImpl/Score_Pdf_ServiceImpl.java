/** 
 **************************************************************************************
 * @file Score_Pdf_ServiceImpl.java
 * @brief 主に[楽譜データ管理機能]が使用するサービスの機能を実装するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Samba;
import com.springproject.dockerspring.FileIO.CompInterface.Score_Pdf_Zip;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;
import com.springproject.dockerspring.Form.BlobImplForm.Blob_Data_Form;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.CommonOutputValid.Blob_Name_Form;
import com.springproject.dockerspring.Form.FormInterface.Blob_Name_Form_Validator;
import com.springproject.dockerspring.Form.FormInterface.Score_Pdf_Zip_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Hist_Get_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form_Child;
import com.springproject.dockerspring.Form.NormalForm.Rollback_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Output_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Output_Form_Child;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.HistoryRepo.Score_Pdf_History_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Score_Pdf_Repo;
import com.springproject.dockerspring.Repository.SubjectEnum.Score_Pdf_Subject;
import com.springproject.dockerspring.Service.FuncWrap;
import com.springproject.dockerspring.Service.ManyParam;
import com.springproject.dockerspring.Service.CommonMethods.CommonUtilsService;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.OriginalException.OverCountException;
import com.springproject.dockerspring.Service.ServiceInterface.Score_Pdf_Service;

import lombok.RequiredArgsConstructor;











/** 
 **************************************************************************************
 * @brief 主に[楽譜データ管理機能]が使用するサービスの機能を実装するクラス
 * 
 * @details 
 * - このクラスでは、コントローラーの直下で実行する処理をサービスクラスとして定義する。
 * - 内容としては、このシステム内に実装されている各機能を集約し、コントローラー側からすぐに
 * 使用できる為にする実装である。
 * - なお、このクラスは多くのクラスをDIしており、このクラス自体の責務が大きいことになっているが
 * 、サービスクラスの特性上、致し方ないものとする。ここである程度集約しておかないと、コントローラー
 * 側の処理が膨大になってしまうからである。
 * 
 * @see Score_Pdf_Service
 * @see CommonUtilsService
 * @see Score_Pdf_Repo
 * @see Score_Pdf_History_Repo
 * @see Score_Pdf_Samba
 * @see Score_Pdf_Zip
 * @see Score_Pdf_Zip_Form
 * @see Environment_Config
 * @see Blob_Name_Form_Validator
 * 
 * @par 使用アノテーション
 * - @Service
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Score_Pdf_ServiceImpl implements Score_Pdf_Service{

  private final CommonUtilsService common_utils;
  private final Score_Pdf_Repo normal_repo;
  private final Score_Pdf_History_Repo hist_repo;
  private final Score_Pdf_Samba samba_comp;
  private final Score_Pdf_Zip zip_comp;
  private final Score_Pdf_Zip_Form zip_form;
  private final Environment_Config config;
  private final Blob_Name_Form_Validator validator;











  /** 
   **********************************************************************************************
   * @brief 指定の楽譜番号で、指定した個数のバイナリデータを追加保存しようとした際に、環境変数で
   * 定められている一つの楽譜番号当たりの保存制限数を超えないか判定する。
   * 
   * @details
   * - このメソッドは、データの新規追加や、ロールバックの際にバイナリデータの保存制限数を超えてしまわないかを
   * 判定する。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 判定結果の真偽値（合格なら「True」である）
   * 
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @par 大まかな処理の流れ
   * -# 渡された楽譜番号で現状のバイナリデータの検索結果件数を取得する。
   * -# 以下の計算で判定を行い、環境変数の設定個数を超過していないことを確認する。
   *    - 環境変数設定値 >= (現状の個数 + 追加しようとしている個数)
   * 
   * @param[in] score_id 指定する楽譜番号
   * @param[in] input_count 追加するファイルの個数
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean checkMaxLimitData(String score_id, int input_count) throws Exception {

    try {
      int current_count = this.normal_repo.findByScore_idCount(score_id, Integer.MAX_VALUE).get();
      return this.config.getPdf_save_limit() >= (current_count + input_count);

    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:checkMaxLimitData]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief フォームから渡された情報をデータベースとファイルサーバーでペアにして格納する。なお、この
   * メソッドには、複数のバイナリデータをまとめて保存することが可能である。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - 戻り値であるマップリストは、バイナリデータのファイルサーバーへの保存に失敗した際に、保存に失敗した
   * バイナリデータのファイル名を返却し、ユーザーに知らせるためにある。
   * - ファイルサーバーとデータベースとの整合性を極力保つため、トランザクションを一つのファイルごとに実施して、
   * コミットしている。
   * - ファイルサーバーとデータベースの整合性は、エラーが発生した場合に取ることが難しくなるため、多少の不整合は
   * 要件上許容をしている。
   * - 許容するパターンとしては、「ファイルサーバー上にはデータが存在しているが、データベース側にはペアとなる
   * データが存在しない」である。
   * - 逆に、「データベース上にはデータが存在するが、ファイルサーバー上にはペアが存在しない」のは、許容しない。
   * - これは、ファイルサーバー上のデータはデータベース上のデータを元に検索するためであり、データベース上に
   * データが無ければ、そもそもファイルサーバー上を検索すら行わない為、ファイルサーバー上にデータが残ってしまう
   * 分には問題ないからである。
   * - 逆のパターンは、ファイルサーバー上にないデータを検索してしまうことになり、エラーが頻発する恐れがあるため
   * 防ぐようにしている。
   * - ただ、ファイルサーバー上に余計なデータが残ってしまうのはよろしくないので、定期的なバッチ処理で削除する。
   * 
   * @param[in] form データベースとファイルサーバーに格納するデータを格納したフォームクラス
   * @param[in] hist_kind 履歴情報を登録する際の指定する履歴種別
   * 
   * @return 保存に失敗したファイル名
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通の保存メソッドに渡して処理を実行する。
   * -# 戻ってきたエラーリストを、戻り値として返す。ただし、正常に終了した際は、Nullとなる。
   * 
   * @note 注意点として、このメソッドは例外を内部で処理してしまい、外部に例外を出さないのでエラーハンドラで
   * キャッチできない。コントローラー側で、戻り値の値に従い、適切に処理する必要がある。
   * 
   * @see Blob_Data_Form
   * @see ManyParam
   * @see Score_Pdf
   * @see Score_Pdf_History
   **********************************************************************************************
   */
  @Override
  public Map<Integer, String> changeData(Blob_Data_Form form, History_Kind_Enum hist_kind) throws Exception{

    try{
      ManyParam<Score_Pdf, Score_Pdf_History> many_param = ManyParam
        .<Score_Pdf, Score_Pdf_History>builder()
        .blob_data_form(form)
        .datatype_enum(Datatype_Enum.AUDIO)
        .samba_comp(this.samba_comp)
        .normal_repo(this.normal_repo)
        .hist_repo(this.hist_repo)
        .new_serial_out(Score_Pdf::getSerial_num)
        .new_hist_id_out(Score_Pdf_History::getHistory_id)
        .normal_entity_make((num, id, name, hash) -> new Score_Pdf(num, id, name, hash))
        .hist_entity_make((num, id, name, hash) -> new Score_Pdf_History(null, new Date(), hist_kind.getKinds(), 
                                                                         this.common_utils.getLoginUser(), 
                                                                         num, id, name, hash))
        .build();

      return this.common_utils.blobChangeDataCom(many_param);
    }catch(Exception e){
      throw new Exception("Error location [Score_Pdf_ServiceImpl:changeData]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した通常データを全て取得する。
   * ただし、この段階ではファイルサーバーからはデータを取り出さない。
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
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
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
  @Transactional(rollbackFor = Exception.class)
  public FindAllResult<Score_Pdf> selectAllData(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<Score_Pdf> result = this.common_utils.selectAllDataCom(this.normal_repo, form);

      Boolean judge = result.getResult_list()
                            .parallelStream()
                            .allMatch(s -> {
                              Blob_Name_Form blob_name_form = new Blob_Name_Form(s.getSerial_num(), 
                                                                                 s.getScore_id(), 
                                                                                 s.getScore_name(), 
                                                                                 Datatype_Enum.PDF);
                              return this.common_utils.outputValidCom(blob_name_form, this.validator, null, null);                                   
                            });

      if(judge){
        return result;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch (DataEmptyException e) {
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("score pdf failed" + "\n" + e);
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:selectAllData]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した履歴データを全て取得する。
   * ただし、この段階ではファイルサーバーからはデータを取り出さない。
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
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
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
   * @see Score_Pdf_History
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public FindAllResult<Score_Pdf_History> selectHist(Com_Hist_Get_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<Score_Pdf_History> result = this.common_utils.selectHistCom(this.hist_repo, form);

      Predicate<Score_Pdf_History> append_func = s -> {
        return this.common_utils.outputValidHistInfoCom(s.getHistory_id(), 
                                                        s.getChange_datetime(), 
                                                        s.getChange_kinds(), 
                                                        s.getOperation_user());
      };

      Boolean judge = result.getResult_list()
                            .parallelStream()
                            .allMatch(s -> {
                              Blob_Name_Form blob_name_form = new Blob_Name_Form(s.getSerial_num(), 
                                                                                 s.getScore_id(), 
                                                                                 s.getScore_name(), 
                                                                                 Datatype_Enum.PDF);
                              return this.common_utils.outputValidCom(blob_name_form, this.validator, append_func, s);
                            });

      if(judge){
        return result;
      }else{
        throw new OutputDataFailedException("output data failed");
      }
      
    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("score pdf history failed" + "\n" + e);
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:selectHist]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーや履歴番号のバイナリデータを個別に取得する。
   * 
   * @details
   * - このメソッドは、通常データと履歴データで共用する。
   * - バイナリデータに関してはデータが大きすぎるため、データ一覧の検索時にまとめて取得してフロントに送信と
   * いった処理は行わない。フロント側から個別のデータを指定されることで、その都度バイナリデータを取得する。
   * - 指定した番号のバイナリデータが存在しなかったり、取得したデータに不正があり出力できない場合は、特定の
   * 例外を出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得してきたバイナリデータファイル
   * 
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @param[in] id 指定するシリアルナンバーや履歴番号
   * @param[in] hist 「True」で履歴用の圧縮済みのデータ解凍して取得、「False」で通常データを取得
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたシリアルナンバーや履歴番号に従い、該当するバイナリデータをファイルサーバーから取得する。
   * -# 取得した結果を、出力時のバリデーション（ZIP抽出ファイルのバリデーションと共用）を利用し、出力時
   * バリデーションを実行する。この時点で、一つでもエラーが発生した場合は処理を中止する。
   * -# 正常だった場合は、専用のエンティティに格納し、戻り値とする。
   * 
   * @see FileEntity
   * @see Score_Pdf_History
   * @see Score_Pdf
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public FileEntity selectBlobData(Integer num_id, Boolean hist) throws DataEmptyException, OutputDataFailedException, Exception {

    try{
      File file = null;
      String filename = null;

      if(hist){
        Score_Pdf_History hist_ent = this.hist_repo.findById(num_id)
                                                   .orElseThrow(() -> new DataEmptyException());

        file = this.common_utils.selectBlobDataCom(this.samba_comp, hist_ent.getHistory_id(), true, hist_ent.getPdf_hash());
        filename = hist_ent.getScore_name();
      }else{
        Score_Pdf entity = this.normal_repo.findById(num_id)
                                           .orElseThrow(() -> new DataEmptyException());

        file = this.common_utils.selectBlobDataCom(this.samba_comp, entity.getSerial_num(), false, entity.getPdf_hash());
        filename = entity.getScore_name();
      }

      this.zip_form.sequentialCheck(filename, file);

      return new FileEntity(filename, file);

    } catch (DataEmptyException e) {
      throw new DataEmptyException();
    }catch(InputFileDifferException e){
      throw new OutputDataFailedException("score pdf blob failed" + "\n" + e);
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:selectBlobData]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号の履歴情報を、通常データにロールバックして復元する。
   * 
   * @details
   * - このメソッドは、誤操作時などに以前の情報を履歴から復元し、ユーザビリティを高めるためにある。
   * - ロールバック処理を行った結果、該当の履歴情報が存在しなかった場合や、ロールバックした後に一つの管理番号
   * につき保持できる情報を制限数を超えてしまう場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同クラス内のメソッド[changeData]の説明を参照する事。
   * 
   * @return 検索結果のリスト
   * 
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @param[in] form ロールバック時に必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通のロールバックメソッドに渡して処理を実行する。
   * 
   * @see Rollback_Form
   * @see Score_Pdf
   * @see Score_Pdf_History
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OverCountException
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void rollbackData(Rollback_Form form) throws Exception, DataEmptyException, OverCountException{

    try{
      Predicate<Score_Pdf_History> new_append_check = FuncWrap.throwPredicate(s -> {
        Boolean exists_data = this.normal_repo.findByScore_idCount(s.getScore_id(), Integer.MAX_VALUE).get() > 0;
        
        Optional<Score_Pdf> normal_ent = this.normal_repo.findById(s.getSerial_num());
        Boolean exists_serial = normal_ent.isPresent() 
                             && normal_ent.get().getScore_id().equals(s.getScore_id());
             
        return exists_data && exists_serial;
      });


      BiPredicate<Score_Pdf_History, File> rollback_output_valid_blob = FuncWrap.throwBiPredicate((ent, file) -> {
        Predicate<Score_Pdf_History> append_func = s -> {
          return this.common_utils.outputValidHistInfoCom(ent.getHistory_id(), 
                                                          ent.getChange_datetime(), 
                                                          ent.getChange_kinds(), 
                                                          ent.getOperation_user());
        };

        Blob_Name_Form blob_name_form = new Blob_Name_Form(ent.getSerial_num(), 
                                                           ent.getScore_id(), 
                                                           ent.getScore_name(), 
                                                           Datatype_Enum.PDF);

        this.zip_form.sequentialCheck(ent.getScore_name(), file);
        return this.common_utils.outputValidCom(blob_name_form, this.validator, append_func, ent);
      });


      ManyParam<Score_Pdf, Score_Pdf_History> many_param = ManyParam.
        <Score_Pdf, Score_Pdf_History>builder()
        .rollback_form(form)
        .samba_comp(this.samba_comp)
        .normal_repo(this.normal_repo)
        .hist_repo(this.hist_repo)
        .hash_out(Score_Pdf_History::getPdf_hash)
        .new_append_check(new_append_check)
        .serial_null(s -> s.setSerial_num(null))
        .count_check(FuncWrap.throwPredicate(s -> checkMaxLimitData(s.getScore_id(), 1)))
        .new_serial_out(Score_Pdf::getSerial_num)
        .new_hist_id_out(Score_Pdf_History::getHistory_id)
        .normal_entity_refill(s -> new Score_Pdf(s.getSerial_num(), 
                                                 s.getScore_id(), 
                                                 s.getScore_name(), 
                                                 s.getPdf_hash()))
        .hist_entity_refill(s -> new Score_Pdf_History(null, new Date(), 
                                                       History_Kind_Enum.ROLLBACK.getKinds(), 
                                                       this.common_utils.getLoginUser(), 
                                                       s.getSerial_num(), 
                                                       s.getScore_id(), 
                                                       s.getScore_name(), 
                                                       s.getPdf_hash()))
        .rollback_output_valid_blob(rollback_output_valid_blob)
        .build();

      this.common_utils.blobRollbackCom(many_param);

    }catch(DataEmptyException e) {
      throw new DataEmptyException();
    }catch(OverCountException e) {
      throw new OverCountException();
    }catch(OutputDataFailedException | IllegalArgumentException e){
      throw new OutputDataFailedException("audio data blob rollback failed" + "\n" + e);
    }catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:rollbackData]" + "\n" + e);
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
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @see EntitySetUp
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Map<String, String> parseString(EntitySetUp entity) throws Exception {
    try {
      return this.common_utils.parseStringCom(entity);
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:parseString]" + "\n" + e);
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
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @see EntitySetUp
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<Map<String, String>> mapPacking(List<EntitySetUp> data_list) throws Exception {
    try {
      return this.common_utils.mapPackingCom(data_list);
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:mapPacking]" + "\n" + e);
    }
  }









  


  /** 
   **********************************************************************************************
   * @brief 入力された圧縮ファイルを解凍し、中に入っているバイナリデータを、データベースとファイルサーバーに
   * 新規追加する。
   * 
   * @details
   * - 要件上、一つの管理番号に複数のバイナリデータ一括で格納する事が多々あるため、圧縮ファイルで送信する
   * 事でまとめて保存ができるようになっている。
   * - 戻り値となっているマップリストには、保存に失敗したバイナリファイルの名称と、発生した例外の名称が
   * 格納されている。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同クラス内のメソッド[changeData]の説明を参照する事。
   * 
   * @return 保存に失敗したファイル名
   * 
   * @param[in] form 渡された圧縮ファイルが格納されたフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# まず、渡された圧縮ファイルを解凍し、中のデータを抽出する。この段階で不正なファイルと判断されたものは
   * エラーとする。
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通の保存メソッドに渡して処理を実行する。
   * -# 戻ってきたエラーリストを、戻り値として返す。ただし、正常に終了した際は、Nullとなる。
   * -# エラーの有無に関係なく、生成した一時ファイルはすべて削除する。
   * 
   * @note 
   * - 注意点として、共通メソッドは例外を内部で処理してしまい、外部に例外を出さないのでエラーハンドラで
   * キャッチできない。コントローラー側で、戻り値の値に従い、適切に処理する必要がある。
   * - ラムダ式内に例外キャッチ処理があり、検査例外を非検査例外に変換して外部に出しているが、このコーディングは
   * 余り推奨されるものではない。だが今回のケースは、並列処理ではなく順次処理としており、影響は少ないと判断
   * した為、この方法を採用する。
   * 
   * @see Zip_Form
   * @see FileEntity
   * @see Score_Pdf
   * @see Score_Pdf_History
   * 
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  public Map<String, String> inputZip(Zip_Form form) throws Exception{

    List<FileEntity> files = null;
    
    try{
      files = this.common_utils.inputZipCom(zip_comp, zip_form, form, 
                                            FuncWrap.throwBiPredicate((s, i) -> checkMaxLimitData(s, i)));

      ManyParam<Score_Pdf, Score_Pdf_History> many_param = ManyParam
        .<Score_Pdf, Score_Pdf_History>builder()
        .file_entity_list(files)
        .samba_comp(this.samba_comp)
        .normal_repo(this.normal_repo)
        .hist_repo(this.hist_repo)
        .new_serial_out(Score_Pdf::getSerial_num)
        .new_hist_id_out(Score_Pdf_History::getHistory_id)
        .normal_entity_make((num, id, name, hash) -> new Score_Pdf(num, id, name, hash))
        .hist_entity_make((num, id, name, hash) -> new Score_Pdf_History(null, new Date(), 
                                                                         History_Kind_Enum.INSERT.getKinds(), 
                                                                         this.common_utils.getLoginUser(), 
                                                                         num, id, name, hash))
        .build();

      return this.common_utils.zipChangeDataCom(many_param);

    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:inputZip]" + "\n" + e);
    }finally{
      if(files != null){ 
        for(FileEntity item: files){
          Files.deleteIfExists(item.getTmpfile().toPath()); 
        }
      }
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定された複数のバイナリデータを、圧縮ファイルに格納してまとめて返却する。
   * 
   * @details
   * - 要件上、保存しているバイナリデータをまとめてほしいといった事があるので、保存されているバイナリデータを
   * まとめて圧縮ファイルに格納し、返却することができる。
   * - 取得しようとしたバイナリデータが存在しなかったり、取得してきたデータに不正があり出力ができない場合は、
   * 特定の例外を投げ、別として扱う。
   * - その他例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 生成した圧縮ファイル
   * 
   * @param[in] form 指定したバイナリデータのシリアルナンバーが複数格納されたフォームクラス
   * 
   * @par 使用アノテーション
   * - @Transactional(rollbackFor = Exception.class)
   * 
   * @par 大まかな処理の流れ
   * -# まず、ファイルサーバーからのデータの取得処理に関しては、圧縮処理を行う共通クラスで行う。そのため、
   * データベースアクセスとファイルサーバーのアクセスの処理を関数型インターフェースで定義する。
   * -# フォームで渡された指定のファイルのシリアルナンバーを元に、データベースを検索した後ファイルサーバーを
   * 検索し、バイナリデータを取得する。該当するデータが無かった場合は、特定の例外を出す。
   * -# 取得したバイナリデータの正当性を、出力時バリデーションで確認する。一つでも不正があれば、その時点で
   * エラーとする。
   * -# 以上の処理の関数型インターフェースを、圧縮ファイル生成の共通クラスに渡し、処理を実行する。
   * 実行後に生成された圧縮ファイルに、環境変数で指定された生成時のファイル名を付けて戻り値とする。
   * 
   * @note 
   * - ラムダ式内に例外キャッチ処理があり、検査例外を非検査例外に変換して外部に出しているが、このコーディングは
   * 余り推奨されるものではない。だが今回のケースは、並列処理ではなく順次処理としており、影響は少ないと判断
   * した為、この方法を採用する。
   * 
   * @see Zip_Output_Form
   * @see Zip_Output_Form_Child
   * @see FileEntity
   * @see Score_Pdf
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public FileEntity outputZip(Zip_Output_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      Supplier<List<FileEntity>> generate = FuncWrap.throwSupplier(() -> {
        List<FileEntity> ent_list = new ArrayList<>();

        for(Zip_Output_Form_Child item: form.getSerial_num_list()){
          Score_Pdf entity = this.normal_repo.findById(item.getSerial_num())
                                             .orElseThrow(() -> new DataEmptyException());

          File file = this.common_utils.selectBlobDataCom(this.samba_comp, entity.getSerial_num(), 
                                                          false, entity.getPdf_hash());
          ent_list.add(new FileEntity(entity.getScore_name(), file));
        }

        zip_form.blobChecker(ent_list);
        return ent_list;
      });

      
      File zip_file = this.common_utils.outputZipCom(generate, this.zip_comp);
      return new FileEntity(this.config.getMusical_score_zip() + "." + Datatype_Enum.ZIP.getExtension(), zip_file);

    }catch(NoSuchElementException e){
      throw new DataEmptyException();
    }catch(InputFileDifferException | IllegalArgumentException e){
      throw new OutputDataFailedException("score pdf zip failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Score_Pdf_ServiceImpl:outputZip]" + "\n" + e);
    }
  }














  /** 
   **********************************************************************************************
   * @brief 指定された通常データ内のシリアルナンバーのデータを削除する。
   * 
   * @details
   * - 削除しようとしたデータが見つからなかった場合は、特定の例外を投げ、別として対応する。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同クラス内のメソッド[changeData]の説明を参照する事。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通の削除メソッドに渡して処理を実行する。
   * 
   * @see Delete_Form
   * @see Score_Pdf
   * @see Score_Pdf_History
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Override
  public void deleteData(Delete_Form form) throws Exception, DataEmptyException {

    try{
      ManyParam<Score_Pdf, Score_Pdf_History> many_param = ManyParam.
        <Score_Pdf, Score_Pdf_History>builder()
        .delete_form(form)
        .samba_comp(this.samba_comp)
        .normal_repo(this.normal_repo)
        .hist_repo(this.hist_repo)
        .hash_out(Score_Pdf_History::getPdf_hash)
        .new_serial_out(Score_Pdf::getSerial_num)
        .new_hist_id_out(Score_Pdf_History::getHistory_id)
        .hist_entity_refill(s -> new Score_Pdf_History(null, new Date(), 
                                                       History_Kind_Enum.DELETE.getKinds(), 
                                                       this.common_utils.getLoginUser(), 
                                                       s.getSerial_num(), 
                                                       s.getScore_id(), 
                                                       s.getScore_name(), 
                                                       s.getPdf_hash()))
        .build();

      this.common_utils.blobDeleteDataCom(many_param);

    }catch (InputFileDifferException | DataEmptyException e) {
      throw new DataEmptyException();
    }catch(Exception e){
      throw new Exception("Error location [Score_Pdf_ServiceImpl:deleteData]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 指定の管理番号で保存されているバイナリデータをすべて削除する。
   * 
   * @details
   * - 参照元の管理番号のデータそのものが消えた場合は、連動して保管されているバイナリデータもすべて削除
   * しなければならない。
   * - 削除しようとしたバイナリデータが存在しなかったり場合は、特定の例外を投げ、別として扱う。
   * - その他例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同インターフェース内のメソッド[changeData]の説明を参照する事。
   * - このメソッドで削除処理を行うわけではなく、同クラス内にある[deleteData]を繰り返し実行しているだけである。
   * 
   * @param[in] id 削除を指定する管理番号
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号で現在データベースに保存中のデータを検索し、リストとして取得する。
   * -# 取得したデータを元に、同クラスにある[deleteData]を実行するために引数として渡すフォームクラスを作成
   * する。フォームクラスには、リストとして取得したデータのシリアルナンバーを格納する。
   * -# 組み立てたフォームクラスを削除実行メソッドに渡し、バイナリデータ一つずつにトランザクションを実行
   * しながら削除していく。
   * -# この一連の処理を、指定した管理番号で検索した際の検索結果がすべてなくなるまで実行する。
   * 
   * @see Com_Search_Form
   * @see Delete_Form
   * @see Delete_Form_Child
   * @see FindAllResult
   * @see Score_Pdf
   * @see Score_Pdf_History
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Override
  public void deleteAllData(String id) throws Exception, DataEmptyException{
    
    try{
      for(;;){
        Com_Search_Form form = new Com_Search_Form();
        form.setMain_word(id);
        form.setSubject(Score_Pdf_Subject.All.name());
        form.setOrder(true);
        form.setPage_num(1);

        FindAllResult<Score_Pdf> result = this.common_utils.selectAllDataCom(this.normal_repo, form);

        if(result.getResult_count() == 0 || result.getResult_list().isEmpty()){
          break;
        }

        List<Delete_Form_Child> delete_list = result.getResult_list()
                                                    .parallelStream()
                                                    .map(s -> {
                                                       Delete_Form_Child child = new Delete_Form_Child();
                                                       child.setSerial_num(s.getSerial_num());
                                                       return child;
                                                     })
                                                    .toList();

        Delete_Form del_form = new Delete_Form();
        del_form.setSerial_num_list(delete_list);
        deleteData(del_form);
      }      
      
    }catch (DataEmptyException e) {
      throw new DataEmptyException();
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:deleteAllData]" + "\n" + e);
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
   * - このメソッドに関しても、データベースとファイルサーバーの整合性を保つための配慮を行っている。詳しくは、
   * 同クラス内のメソッド[changeData]の説明を参照する事。
   * 
   * @return 並列処理のスレッドが入ったインスタンス(戻り値無し)
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @par 使用アノテーション
   * - @Async("Service")
   * 
   * @par 大まかな処理の流れ
   * -# 渡されたフォームの情報や、このクラスにDIされているクラス、使用する関数型インターフェースを定義した
   * パラメータ用のビルダークラスを作成する。
   * -# 作成したビルダークラスを、共通の削除メソッドに渡して処理を実行する。
   * 
   * @see Del_Indivi_Hist_Form
   * @see Score_Pdf
   * @see Score_Pdf_History
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Async("Service")
  @Override
  public CompletableFuture<Void> deleteHistory(Del_Indivi_Hist_Form form) throws DataEmptyException, Exception {

    try{
      ManyParam<Score_Pdf, Score_Pdf_History> many_param = ManyParam
        .<Score_Pdf, Score_Pdf_History>builder()
        .del_indivi_hist_form(form)
        .samba_comp(this.samba_comp)
        .hist_repo(this.hist_repo)
        .new_hist_id_out(Score_Pdf_History::getHistory_id)
        .history_date_between(FuncWrap.throwBiFunction((start, end) -> this.hist_repo
                                                                           .findAllByDateBetween(start, end, 
                                                                                                 this.config.getOffset(), 
                                                                                                 0).get()))
        .build();

      this.common_utils.blobDeleteHistoryCom(many_param);    
      return CompletableFuture.completedFuture(null);
      
    }catch (DataEmptyException e) {
      throw new DataEmptyException();
    } catch (Exception e) {
      throw new Exception("Error location [Score_Pdf_ServiceImpl:deleteHistory]" + "\n" + e);
    }
  }
}