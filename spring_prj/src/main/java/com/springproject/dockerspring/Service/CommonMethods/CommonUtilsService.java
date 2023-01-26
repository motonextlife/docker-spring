/** 
 **************************************************************************************
 * @file CommonUtilsService.java
 * @brief サービスクラスでの共通処理において、各集約メソッドに中継を行うクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.CommonMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.InOutCsv;
import com.springproject.dockerspring.FileIO.CompInterface.InOutSamba;
import com.springproject.dockerspring.FileIO.CompInterface.InOutZip;
import com.springproject.dockerspring.FileIO.CompInterface.OutPdf;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Form.BlobImplForm.Blob_Data_Form;
import com.springproject.dockerspring.Form.BlobImplForm.Blob_Data_Form_Child;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.CsvImplForm.CsvCheckResult;
import com.springproject.dockerspring.Form.FormInterface.Blob_Validation;
import com.springproject.dockerspring.Form.FormInterface.Csv_Validation;
import com.springproject.dockerspring.Form.NormalForm.Com_Hist_Get_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Common_Csv_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.Rollback_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Form;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Security.Service.GetLoginUser;
import com.springproject.dockerspring.Service.ManyParam;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.DbActionException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.OriginalException.OverCountException;

import lombok.RequiredArgsConstructor;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;











/** 
 **************************************************************************************
 * @brief サービスクラスでの共通処理において、各集約メソッドに中継を行うクラス
 * 
 * @details 
 * - この共通処理では、サービスクラスで用いる集約が可能な機能を集めることで、メンテナンス性を
 * 向上させるために実装。
 * - ただし、特定の機能に依存してしまう処理に関しては、ここでは実装せずに関数型インターフェースで
 * 渡すなどして分離を行う。
 * - このクラスで実装は行わない。他の集約クラスへの中継を行うだけである。ただし、ファイルサーバーとの
 * 整合性の為に個別トランザクションを実施している処理の共通メソッドや、他の共通処理を用いている
 * ロールバックの共通処理に関しては、このクラスに実装する。
 * 
 * @see ChangeCommonMethods
 * @see CsvZipPdfCommonMethods
 * @see SelectCommonMethods
 * @see GetLoginUser
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonUtilsService{

  private final ChangeCommonMethods change;
  private final CsvZipPdfCommonMethods csv_zip_pdf;
  private final SelectCommonMethods select;
  private final GetLoginUser get_login;

  private final PlatformTransactionManager tra_manager;
  private final TransactionDefinition def = new DefaultTransactionDefinition();









  /** 
   **********************************************************************************************
   * @brief 独自作成した、バイナリデータに関するデータベースのデータを受け取り、エンティティを生成する事が
   * 可能になる関数型インターフェース。
   * 
   * @details
   * - 既存の関数型インターフェースのみでは、受付可能な引数の数が足りなかったため、独自に作成する事で、
   * エンティティの生成処理のみ分離し、バイナリデータが関わるトランザクション処理やロールバック処理、
   * 削除処理の共通化を図る。
   * 
   * @par 使用アノテーション
   * - @FunctionalInterface
   **********************************************************************************************
   */
  @FunctionalInterface
  public interface BlobEntityFunction<E> {
    E apply(Integer serial_num, String id, String file_name, String hash);
  }











  /** @name [ログイン中ユーザーの取得]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see GetLoginUser
   **********************************************************************************************
   */ 
  public String getLoginUser(){
    return this.get_login.getLoginUser();
  }

  /** @} */



  





  /** @name [データのCRUD処理]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see ChangeCommonMethods
   **********************************************************************************************
   */ 
  public File makeMultipartTmpFile(MultipartFile file, 
                                   String prefix, 
                                   String extension) throws IOException {

    return this.change.makeMultipartTmpFile(file, prefix, extension);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see ChangeCommonMethods
   **********************************************************************************************
   */ 
  public <T> T changeDatabaseCom(FindAllCrudRepository<T, Integer> repository, T entity) throws DbActionException {
    return this.change.changeDatabaseCom(repository, entity);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see ChangeCommonMethods
   **********************************************************************************************
   */ 
  public void changeSambaCom(InOutSamba samba_comp, 
                             File file, 
                             Integer num_id, 
                             Boolean hist) throws IOException{

    this.change.changeSambaCom(samba_comp, file, num_id, hist);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see ChangeCommonMethods
   **********************************************************************************************
   */ 
  public <T> void deleteDatabaseCom(FindAllCrudRepository<T, Integer> repository, T entity) throws DbActionException {
    this.change.deleteDatabaseCom(repository, entity);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see ChangeCommonMethods
   **********************************************************************************************
   */ 
  public void deleteSambaCom(InOutSamba samba_comp, Integer num_id, Boolean hist) throws IOException{
    this.change.deleteSambaCom(samba_comp, num_id, hist);
  }

  /** @} */











  /** @name [データの取得処理]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public <T, E> Boolean outputValidCom(T form, Validator validator, Predicate<E> append_func, E entity){

    return this.select.outputValidCom(form, validator, append_func, entity);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public Boolean outputValidHistInfoCom(Integer history_id, 
                                        Date change_datetime, 
                                        String change_kinds, 
                                        String operation_user) {

    return this.select.outputValidHistInfoCom(history_id, change_datetime, change_kinds, operation_user);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public <T> FindAllResult<T> selectAllDataCom(FindAllCrudRepository<T, Integer> repo, Com_Search_Form form)
      throws ExecutionException, DataEmptyException, InterruptedException, ParseException{

    return this.select.selectAllDataCom(repo, form);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public <T> FindAllResult<T> selectHistCom(FindAllCrudRepository<T, Integer> hist_repo, Com_Hist_Get_Form form)
      throws ExecutionException, DataEmptyException, InterruptedException, ParseException{

    return this.select.selectHistCom(hist_repo, form);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public File selectBlobDataCom(InOutSamba samba_comp, 
                                Integer num_id, 
                                Boolean hist, 
                                String hash) throws IOException, InputFileDifferException{

    return this.select.selectBlobDataCom(samba_comp, num_id, hist, hash);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public Map<String, String> parseStringCom(EntitySetUp entity) throws IOException {
    return this.select.parseStringCom(entity);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see SelectCommonMethods
   **********************************************************************************************
   */ 
  public List<Map<String, String>> mapPackingCom(List<EntitySetUp> data_list) {
    return this.select.mapPackingCom(data_list);
  }

  /** @} */











  /** @name [CSV,ZIP,PDF関係の処理]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public <T> File outputPdfCom(OutPdf<T> pdf_comp, T entity) throws IOException{
    return this.csv_zip_pdf.outputPdfCom(pdf_comp, entity);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public <T> CsvCheckResult inputCsvCom(Consumer<List<T>> goto_database, 
                                        InOutCsv<T> csv_comp, 
                                        Csv_Validation csv_form, 
                                        Common_Csv_Form form)
                                        throws IOException, ExecutionException, InterruptedException, ParseException, InputFileDifferException{

    return this.csv_zip_pdf.inputCsvCom(goto_database, csv_comp, csv_form, form);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public List<FileEntity> inputZipCom(InOutZip zip_comp, 
                                      Blob_Validation blob_form, 
                                      Zip_Form form, 
                                      BiPredicate<String, Integer> count_limit) throws IOException, OverCountException, InputFileDifferException{

    return this.csv_zip_pdf.inputZipCom(zip_comp, blob_form, form, count_limit);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public <T> FindAllResult<T> outputCsvCom(FindAllCrudRepository<T, Integer> repository, 
                                           Com_Search_Form form, 
                                           int page, 
                                           Predicate<T> valid_func) 
                                           throws ExecutionException, DataEmptyException, InterruptedException, ParseException, OutputDataFailedException{

    return this.csv_zip_pdf.outputCsvCom(repository, form, page, valid_func);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public <T> File outputCsvCom(InOutCsv<T> csv_comp, Function<Integer, List<Map<String, String>>> generate) 
      throws IOException, InputFileDifferException{

    return this.csv_zip_pdf.outputCsvCom(csv_comp, generate);
  }

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see CsvZipPdfCommonMethods
   **********************************************************************************************
   */ 
  public <T> File outputZipCom(Supplier<List<FileEntity>> generate, InOutZip zip_comp) 
      throws IOException, InputFileDifferException{

    return this.csv_zip_pdf.outputZipCom(generate, zip_comp);
  }

  /** @} */
























  /** @name [個別トランザクションや共通ロールバック処理]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号のテキストデータを、通常データにロールバックする。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - ロールバックの処理が、新規追加または更新のどちらになるかは、通常データ側の対象データの状態で判別する。
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定された履歴番号で該当データを検索し、ロールバック対象のデータを取得する。もしデータが無ければ例外
   * を投げる。
   * -# 取得してきたデータを出力時バリデーションにかけて、不正な値がないかを確認する。
   * -# ロールバック処理を、新規追加扱いか更新扱いか判定するが、以下の場合は新規追加扱いとして、ロールバック対象の
   * データのシリアルナンバーをNullとする。
   *    - 通常データ側に指定履歴の管理番号が存在しない場合。
   * -# ただし、以下の場合は最新のシリアルナンバーに更新する扱いとする。
   *    - 通常データ側に該当の管理番号は存在するが、指定のシリアルナンバーが存在しない。
   *    - 通常データ側に該当の管理番号は存在するが、管理番号とシリアルナンバーのペアが異なる。
   * -# ロールバック対象の履歴情報のデータを用いて通常エンティティを作成し、データベースに保存する。
   * -# 新しいシリアルナンバーが付けられた通常エンティティを用いて、新たに履歴エンティティを作成し、ロールバックを
   * 行ったという履歴保存の為に、データベースに保存する。
   * 
   * @see ManyParam
   *
   * @throw DbActionException
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  public <EN, EH> void normalRollbackDataCom(ManyParam<EN, EH> many_param) 
      throws DbActionException, DataEmptyException, OutputDataFailedException {

    Rollback_Form form = many_param.getRollback_form();
    Predicate<EH> new_append_check = many_param.getNew_append_check();
    Predicate<EH> latest_serial_check = many_param.getLatest_serial_check();
    Consumer<EH> serial_null = many_param.getSerial_null();
    Consumer<EH> serial_latest = many_param.getSerial_latest();
    Function<EH, EN> normal_entity_refill = many_param.getNormal_entity_refill();
    Function<EN, EH> hist_entity_refill = many_param.getHist_entity_refill();
    Predicate<EH> rollback_output_valid_normal = many_param.getRollback_output_valid_normal();

    try{
      EH hist_ent = many_param.getHist_repo()
                              .findById(form.getHistory_id())
                              .orElseThrow(() -> new DataEmptyException());

      if(!rollback_output_valid_normal.test(hist_ent)){
        throw new OutputDataFailedException("output data failed");
      }

      if(!new_append_check.test(hist_ent)){
        serial_null.accept(hist_ent);
        
      }else if(!latest_serial_check.test(hist_ent)){
        serial_latest.accept(hist_ent);
      }

      EN normal_ent = changeDatabaseCom(many_param.getNormal_repo(), normal_entity_refill.apply(hist_ent));
      changeDatabaseCom(many_param.getHist_repo(), hist_entity_refill.apply(normal_ent));

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("rollback data failed");
    }catch(DbActionException e){
      throw new DbActionException("Error location [ChangeCommonMethods:rollbackData]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定された履歴番号のバイナリデータを、通常データにロールバックする。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - ロールバックの処理が、新規追加または更新のどちらになるかは、通常データ側の対象データの状態で判別する。
   * - 新規追加の場合に関して、処理途中で例外が発生した場合、データベースのデータはリセットされるが
   * ファイルサーバーのデータは残ったままになる。だが、残ったデータに関しては今後上書きすることで対応可能で、
   * データベースに対応するデータがそもそも無ければ、ファイルサーバーの検索すら行わないので、この段階での
   * ファイルサーバー上のデータの残りは許容する。
   * - 更新に関しては、意図せぬ形でファイルサーバー側のデータが更新されてしまうことを避けるため、通常データの
   * 更新を処理の最後にすることで、通常データが不本意な形で更新されることを防ぐ。
   * ただし、履歴情報は意図せぬ情報が保存されてしまうが、通常データと比べれば重要度が低い事や、意図せぬ保存が
   * 発生したという事実を履歴として残したいという観点から許容する。
   * - ファイルサーバー側に残った不整合のファイルに関しては、定期的にバッチ処理で削除する。
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定された履歴番号で該当データを検索し、ロールバック対象のデータを取得する。もしデータが無ければ例外
   * を投げる。
   * -# 取得してきたデータを出力時バリデーションにかけて、不正な値がないか確認する。
   * -# ファイルサーバーから取得してきたファイルをコピーし、同じデータの一時ファイルを2つ作成する。これによって、
   * 通常データと履歴データに同じデータが格納されるようになる。
   * -# ロールバック処理を、新規追加扱いか更新扱いか判定するが、以下の場合は新規追加扱いとして、ロールバック対象の
   * データのシリアルナンバーをNullとする。
   *    - 通常データ側に指定履歴の管理番号が存在しない場合。
   *    - 通常データ側に指定履歴のシリアルナンバーが存在しない場合。
   *    - 通常データ側が履歴のシリアルナンバーと管理番号のペアと一致しない場合。
   * -# ロールバックした後に、環境変数で指定されている一つの管理番号当たりの保管可能なバイナリデータの制限数を
   * 超過しないか確認する。超過する場合は、その時点で例外を出す。
   * -# ロールバック対象の履歴情報のデータを用いて通常エンティティを作成し、データベースに保存する。
   * -# 新しいシリアルナンバーが付けられた通常エンティティを用いて、新たに履歴エンティティを作成し、ロールバックを
   * 行ったという履歴保存の為に、データベースに保存する。
   * -# 自動連番で付いた履歴番号を使用して、履歴データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「履歴データを先に」保存する。
   * -# 自動連番で付いたシリアルナンバーを使用して、通常データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「通常データを最後に」保存する。
   * -# エラーの有無に関わらず、生成した一時ファイルはすべて削除する。
   * 
   * @see ManyParam
   *
   * @throw IOException
   * @throw DbActionException
   * @throw DataEmptyException
   * @throw OverCountException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  public <EN, EH> void blobRollbackCom(ManyParam<EN, EH> many_param) 
      throws IOException, DbActionException, DataEmptyException, OverCountException, OutputDataFailedException{

    File file_normal = null;
    File file_hist = null;

    Rollback_Form form = many_param.getRollback_form();
    InOutSamba samba_comp = many_param.getSamba_comp();
    Function<EH, String> hash_out = many_param.getHash_out();
    Predicate<EH> new_append_check = many_param.getNew_append_check();
    Predicate<EH> count_check = many_param.getCount_check();
    Consumer<EH> serial_null = many_param.getSerial_null();
    Function<EN, Integer> new_serial_out = many_param.getNew_serial_out();
    Function<EH, Integer> new_hist_id_out = many_param.getNew_hist_id_out();
    Function<EH, EN> normal_entity_refill = many_param.getNormal_entity_refill();
    Function<EN, EH> hist_entity_refill = many_param.getHist_entity_refill();
    BiPredicate<EH, File> rollback_output_valid_blob = many_param.getRollback_output_valid_blob();

    try{
      Integer history_id = form.getHistory_id();
      EH hist_ent = many_param.getHist_repo()
                              .findById(history_id)
                              .orElseThrow(() -> new DataEmptyException());

      file_normal = selectBlobDataCom(samba_comp, history_id, true, hash_out.apply(hist_ent));

      if(!rollback_output_valid_blob.test(hist_ent, file_normal)){
        throw new OutputDataFailedException("output data failed");
      }


      Path copy_target = Paths.get(String.format("%s/%s%s", File_Path_Enum.ResourceFilePath.getPath(), 
                                                 "rollbackDataCopy", 
                                                 file_normal.getName()));

      file_hist = Files.copy(file_normal.toPath(), copy_target, REPLACE_EXISTING).toFile();

      if(!new_append_check.test(hist_ent)){
        serial_null.accept(hist_ent);
      }

      if(!count_check.test(hist_ent)){
        throw new OverCountException();
      }


      EN rollback_ent = normal_entity_refill.apply(hist_ent);
      rollback_ent = changeDatabaseCom(many_param.getNormal_repo(), rollback_ent);
      Integer new_serial = new_serial_out.apply(rollback_ent);

      EH rollback_hist_ent = hist_entity_refill.apply(rollback_ent);
      rollback_hist_ent = changeDatabaseCom(many_param.getHist_repo(), rollback_hist_ent);
      Integer new_hist_id = new_hist_id_out.apply(rollback_hist_ent);


      changeSambaCom(samba_comp, file_hist, new_hist_id, true);
      changeSambaCom(samba_comp, file_normal, new_serial, false);

    }catch(DataEmptyException e) {
      throw new DataEmptyException();
    }catch(OverCountException e) {
      throw new OverCountException();
    }catch(OutputDataFailedException | InputFileDifferException e){
      throw new OutputDataFailedException("rollback data failed");
    }catch(IOException e){
      throw new IOException("Error location [ChangeCommonMethods:blobRollbackCom]" + "\n" + e);
    }catch(DbActionException e){
      throw new DbActionException("Error location [ChangeCommonMethods:blobRollbackCom]" + "\n" + e);
    }finally{
      if(file_normal != null){ 
        Files.deleteIfExists(file_normal.toPath()); 
      }
      if(file_hist != null){ 
        Files.deleteIfExists(file_hist.toPath()); 
      }
    }
  }











  

  /** 
   **********************************************************************************************
   * @brief 指定されたバイナリデータを、データベースとファイルサーバーで紐付けて保存する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 新規追加の場合に関して、処理途中で例外が発生した場合、データベースのデータはリセットされるが
   * ファイルサーバーのデータは残ったままになる。だが、残ったデータに関しては今後上書きすることで対応可能で、
   * データベースに対応するデータがそもそも無ければ、ファイルサーバーの検索すら行わないので、この段階での
   * ファイルサーバー上のデータの残りは許容する。
   * - 更新に関しては、意図せぬ形でファイルサーバー側のデータが更新されてしまうことを避けるため、通常データの
   * 更新を処理の最後にすることで、通常データが不本意な形で更新されることを防ぐ。
   * ただし、履歴情報は意図せぬ情報が保存されてしまうが、通常データと比べれば重要度が低い事や、意図せぬ保存が
   * 発生したという事実を履歴として残したいという観点から許容する。
   * - ファイルサーバー側に残った不整合のファイルに関しては、定期的にバッチ処理で削除する。
   * 
   * @return 保存に失敗したファイル名のリスト
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 保存処理の実行前に、保存対象のファイルのファイル名をリストとして控えておく。処理後、このファイルの中に
   * 残ったファイル名は、保存が失敗したファイル名となる。
   * -# 入力されたバイナリデータから、通常データ保存用と履歴用データ保存用の一時ファイルを作成する。
   * -# バイナリデータのハッシュ値を計算し、ハッシュ値を取得する。
   * -# トランザクションを、「バイナリデータ一つごとに」実行する。これによって、最小限データベースとファイル
   * サーバーの辻褄が合うようになる。
   * -# トランザクションを開始し、通常データをデータベースに保存する。保存後は、自動連番でついたシリアルナンバーを
   * 取得しておく。
   * -# 自動連番で付いたシリアルナンバーを使用して、履歴データをデータベースに保存する。保存後は、自動連番でついた
   * 履歴番号を取得しておく。
   * -# 自動連番で付いた履歴番号を使用して、履歴データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「履歴データを先に」保存する。
   * -# 自動連番で付いたシリアルナンバーを使用して、通常データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「通常データを最後に」保存する。
   * -# トランザクションをコミットし、控えておいたファイル名のリストから、保存が成功したファイル名を削除する。
   * -# 全てのデータの保存が成功すれば、戻り値としてNullを返却する。
   * -# 例外が発生した場合は、トランザクションのロールバックを行い、戻り値として控えておいたファイル名の
   * リストを返却する。
   * -# エラーの有無に関わらず、生成した一時ファイルはすべて削除する。
   * 
   * @see ManyParam
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public <EN, EH> Map<Integer, String> blobChangeDataCom(ManyParam<EN, EH> many_param) throws IOException{

    TransactionStatus status = null;
    File file_normal = null;
    File file_hist = null;

    Blob_Data_Form form = many_param.getBlob_data_form();
    InOutSamba samba_comp = many_param.getSamba_comp();
    Function<EN, Integer> new_serial_out = many_param.getNew_serial_out();
    Function<EH, Integer> new_hist_id_out = many_param.getNew_hist_id_out();
    BlobEntityFunction<EN> normal_entity_make = many_param.getNormal_entity_make();
    BlobEntityFunction<EH> hist_entity_make = many_param.getHist_entity_make();

    Map<Integer, String> save_fail_filename = form.getData_list().stream()
                                                                 .collect(Collectors.toMap(k -> k.getSerial_num(), 
                                                                                           v -> v.getBlob_data().getOriginalFilename()));

    try{
      String id = form.getId();

      for(Blob_Data_Form_Child item: form.getData_list()){

        MultipartFile multipart = item.getBlob_data();
        Integer serial_num = item.getSerial_num();
        String file_name = multipart.getOriginalFilename();

        file_normal = makeMultipartTmpFile(multipart, "blobChangeDataCom", many_param.getDatatype_enum().getExtension());
        file_hist = makeMultipartTmpFile(multipart, "blobChangeDataCom", many_param.getDatatype_enum().getExtension());

        String hash = samba_comp.makeHash(file_normal);

        
        //通常データと履歴データ保存開始-------------------------------------------------------------------------------
        status = this.tra_manager.getTransaction(def);

        EN entity = normal_entity_make.apply(serial_num, id, file_name, hash);
        Integer new_serial = new_serial_out.apply(changeDatabaseCom(many_param.getNormal_repo(), entity));

        EH hist_ent = hist_entity_make.apply(new_serial, id, file_name, hash);
        Integer new_hist_id = new_hist_id_out.apply(changeDatabaseCom(many_param.getHist_repo(), hist_ent));

        changeSambaCom(samba_comp, file_hist, new_hist_id, true);
        changeSambaCom(samba_comp, file_normal, new_serial, false);

        this.tra_manager.commit(status);
        //トランザクション終了----------------------------------------------------------------------------------------

        save_fail_filename.remove(serial_num);
      }

      return null;

    }catch(Exception e){
      this.tra_manager.rollback(status);
      return save_fail_filename;

    }finally{
      if(file_normal != null){ 
        Files.deleteIfExists(file_normal.toPath()); 
      }
      if(file_hist != null){ 
        Files.deleteIfExists(file_hist.toPath()); 
      }
    }
  }












  /** 
   **********************************************************************************************
   * @brief 圧縮ファイルから抽出した複数のバイナリデータを、データベースとファイルサーバーで紐付けて保存する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 新規追加の場合に関して、処理途中で例外が発生した場合、データベースのデータはリセットされるが
   * ファイルサーバーのデータは残ったままになる。だが、残ったデータに関しては今後上書きすることで対応可能で、
   * データベースに対応するデータがそもそも無ければ、ファイルサーバーの検索すら行わないので、この段階での
   * ファイルサーバー上のデータの残りは許容する。
   * - 更新に関しては、意図せぬ形でファイルサーバー側のデータが更新されてしまうことを避けるため、通常データの
   * 更新を処理の最後にすることで、通常データが不本意な形で更新されることを防ぐ。
   * ただし、履歴情報は意図せぬ情報が保存されてしまうが、通常データと比べれば重要度が低い事や、意図せぬ保存が
   * 発生したという事実を履歴として残したいという観点から許容する。
   * - ファイルサーバー側に残った不整合のファイルに関しては、定期的にバッチ処理で削除する。
   * 
   * @return 保存に失敗したファイル名のリスト
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 保存処理の実行前に、保存対象のファイルのファイル名をリストとして控えておく。処理後、このファイルの中に
   * 残ったファイル名は、保存が失敗したファイル名となる。
   * -# 入力されたバイナリデータから、通常データ保存用と履歴用データ保存用の一時ファイルを作成する。
   * -# バイナリデータのハッシュ値を計算し、ハッシュ値を取得する。
   * -# トランザクションを、「バイナリデータ一つごとに」実行する。これによって、最小限データベースとファイル
   * サーバーの辻褄が合うようになる。
   * -# トランザクションを開始し、通常データをデータベースに保存する。保存後は、自動連番でついたシリアルナンバーを
   * 取得しておく。
   * -# 自動連番で付いたシリアルナンバーを使用して、履歴データをデータベースに保存する。保存後は、自動連番でついた
   * 履歴番号を取得しておく。
   * -# 自動連番で付いた履歴番号を使用して、履歴データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「履歴データを先に」保存する。
   * -# 自動連番で付いたシリアルナンバーを使用して、通常データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「通常データを最後に」保存する。
   * -# トランザクションをコミットし、控えておいたファイル名のリストから、保存が成功したファイル名を削除する。
   * -# 全てのデータの保存が成功すれば、戻り値としてNullを返却する。
   * -# 例外が発生した場合は、トランザクションのロールバックを行い、戻り値として控えておいたファイル名の
   * リストを返却する。
   * -# エラーの有無に関わらず、生成した一時ファイルはすべて削除する。
   * 
   * @see ManyParam
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public <EN, EH> Map<String, String> zipChangeDataCom(ManyParam<EN, EH> many_param) throws IOException{
    
    TransactionStatus status = null;
    File file_normal = null;
    File file_hist = null;

    Zip_Form zip_form = many_param.getZip_form();
    List<FileEntity> file_entity_list = many_param.getFile_entity_list();
    InOutSamba samba_comp = many_param.getSamba_comp();
    Function<EN, Integer> new_serial_out = many_param.getNew_serial_out();
    Function<EH, Integer> new_hist_id_out = many_param.getNew_hist_id_out();
    BlobEntityFunction<EN> normal_entity_make = many_param.getNormal_entity_make();
    BlobEntityFunction<EH> hist_entity_make = many_param.getHist_entity_make();

    Map<String, String> save_fail_filename = IntStream.range(0, file_entity_list.size())
                                                      .mapToObj(s -> s)
                                                      .collect(Collectors.toMap(k -> String.valueOf(k), 
                                                                                v -> file_entity_list.get(v).getFilename()));

    try{
      String id = zip_form.getId();

      int i = 0;
      for(FileEntity item: many_param.getFile_entity_list()){

        String file_name = item.getFilename(); 
        file_normal = item.getTmpfile();

        Path copy_target = Paths.get(String.format("%s/%s%s", File_Path_Enum.ResourceFilePath.getPath(), 
                                                   "inputZipCopy", 
                                                   file_normal.getName()));

        file_hist = Files.copy(file_normal.toPath(), copy_target, REPLACE_EXISTING).toFile();

        String hash = many_param.getSamba_comp()
                                .makeHash(file_normal);


        //通常データと履歴データ保存開始-------------------------------------------------------------------------------
        status = this.tra_manager.getTransaction(def);

        EN entity = normal_entity_make.apply(null, id, file_name, hash);
        Integer new_serial = new_serial_out.apply(changeDatabaseCom(many_param.getNormal_repo(), entity));

        EH hist_ent = hist_entity_make.apply(new_serial, id, file_name, hash);
        Integer new_hist_id = new_hist_id_out.apply(changeDatabaseCom(many_param.getHist_repo(), hist_ent));

        changeSambaCom(samba_comp, file_hist, new_hist_id, true);
        changeSambaCom(samba_comp, file_normal, new_serial, false);

        this.tra_manager.commit(status);
        //トランザクション終了----------------------------------------------------------------------------------------

        save_fail_filename.remove(String.valueOf(i));
        i++;
      }

      return null;

    }catch (Exception e) {
      this.tra_manager.rollback(status);
      return save_fail_filename;

    }finally{
      if(file_normal != null){ 
        Files.deleteIfExists(file_normal.toPath()); 
      }
      if(file_hist != null){ 
        Files.deleteIfExists(file_hist.toPath()); 
      }
    }
  }














  /** 
   **********************************************************************************************
   * @brief 指定されたシリアルナンバーのバイナリデータを削除する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 削除に関しては、意図せぬ形でファイルサーバー側のデータが削除されてしまうことを避けるため、通常データの
   * 削除を処理の最後にすることで、通常データが不本意な形で削除されることを防ぐ。
   * ただし、履歴情報は意図せぬ情報が保存されてしまうが、通常データと比べれば重要度が低い事や、意図せぬ削除が
   * 発生したという事実を履歴として残したいという観点から許容する。
   * - ファイルサーバー側に残った不整合のファイルに関しては、定期的にバッチ処理で削除する。
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたシリアルナンバーで該当データを検索し、削除対象のデータを取得する。もしデータが無ければ例外
   * を投げる。
   * -# 削除を行う前に、削除したという履歴を残すために、削除予定のデータで履歴エンティティを作成する。
   * -# 対象データをデータベースから削除したのち、履歴情報をデータベースに保存する。
   * -# 自動連番で付いた履歴番号を使用して、履歴データのバイナリデータをファイルサーバーに保存する。
   * 必ず、「履歴データを先に」保存する。
   * -# 削除対象シリアルナンバーを使用して、通常データのバイナリデータを削除する。必ず、「通常データを最後に」
   * 削除する。
   * -# エラーの有無に関わらず、生成した一時ファイルはすべて削除する。
   * 
   * @see ManyParam
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public <EN, EH> void blobDeleteDataCom(ManyParam<EN, EH> many_param) 
      throws Exception, DataEmptyException, InputFileDifferException {

    TransactionStatus status = null;
    File file = null;

    Delete_Form form = many_param.getDelete_form();
    InOutSamba samba_comp = many_param.getSamba_comp();
    FindAllCrudRepository<EN, Integer> normal_repo = many_param.getNormal_repo();
    FindAllCrudRepository<EH, Integer> hist_repo = many_param.getHist_repo();
    Function<EN, EH> hist_entity_refill = many_param.getHist_entity_refill();
    Function<EH, String> hash_out = many_param.getHash_out();
    Function<EN, Integer> new_serial_out = many_param.getNew_serial_out();
    Function<EH, Integer> new_hist_id_out = many_param.getNew_hist_id_out();

    try{
      List<Integer> delete_list = form.getSerial_num_list().stream()
                                                           .map(s -> s.getSerial_num())
                                                           .toList();

      for(Integer item: delete_list){
        EN del_ent = normal_repo.findById(item)
                                .orElseThrow(() -> new DataEmptyException());

        EH hist_ent = hist_entity_refill.apply(del_ent);
        Integer new_serial = new_serial_out.apply(del_ent);

        file = selectBlobDataCom(samba_comp, 
                                 new_serial, 
                                 false, 
                                 hash_out.apply(hist_ent));

        //通常データ削除と履歴データ保存開始---------------------------------------------------------
        status = tra_manager.getTransaction(def);

        deleteDatabaseCom(normal_repo, del_ent);
        Integer new_hist_id = new_hist_id_out.apply(changeDatabaseCom(hist_repo, hist_ent));

        changeSambaCom(samba_comp, file, new_hist_id, true);
        deleteSambaCom(samba_comp, new_serial, false);

        tra_manager.commit(status);
        //トランザクション終了---------------------------------------------------------------------
      }

    }catch (DataEmptyException e) {
      this.tra_manager.rollback(status);  
      throw new DataEmptyException();
      
    }catch (InputFileDifferException e) {
      this.tra_manager.rollback(status);  
      throw new InputFileDifferException("delete file missing" + "\n" + e);
      
    }catch(Exception e){
      this.tra_manager.rollback(status);  
      throw new Exception("Error location [ChangeCommonMethods:blobDeleteDataCom]" + "\n" + e);
      
    }finally{
      if(file != null){ 
        Files.deleteIfExists(file.toPath()); 
      }
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された検索期間で取得した履歴情報をすべて削除する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - データベースとファイルサーバーの不整合を避けるため、データベース側の履歴情報を最初に削除し、ファイルサーバー
   * 側を最後に削除する。これによって、ファイルサーバー上の存在しないデータを検索してしまうことによるエラーを
   * 防ぐことができる。
   * - ファイルサーバー側に残った不整合のファイルに関しては、定期的にバッチ処理で削除する。
   *
   * @param[in] many_param 処理に必要な引数を格納したビルダーエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定された期間で履歴情報を検索し、該当するデータを取得する。
   * -# 取得したデータから、履歴番号を抽出する。
   * -# 抽出した履歴情報で、個別の履歴情報を検索して、該当するエンティティを取得する。
   * -# トランザクションを開始し、必ず「先にデータベース上の該当データ」を削除する。
   * -# 次に、必ず「最後にファイルサーバー上の該当データ」を削除し、トランザクションを終了する。
   * -# この一連の流れを、データ一つ一つでトランザクションを発行しながら、検索結果がすべてなくなるまで
   * 繰り返す。
   * 
   * @see ManyParam
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  public <EN, EH> void blobDeleteHistoryCom(ManyParam<EN, EH> many_param) 
      throws Exception, DataEmptyException{

    TransactionStatus status = null;

    Del_Indivi_Hist_Form form = many_param.getDel_indivi_hist_form();
    InOutSamba samba_comp = many_param.getSamba_comp();
    BiFunction<Date, Date, List<EH>> history_date_between = many_param.getHistory_date_between();
    FindAllCrudRepository<EH, Integer> hist_repo = many_param.getHist_repo();
    Function<EH, Integer> new_hist_id_out = many_param.getNew_hist_id_out();

    try{
      for(;;){
        Date start_datetime = form.getStart_datetime();
        Date end_datetime = form.getEnd_datetime();
        List<EH> result = history_date_between.apply(start_datetime, end_datetime);

        if(result.size() == 0){
          break;
        }

        List<Integer> delete_list = result.stream()
                                          .map(s -> new_hist_id_out.apply(s))
                                          .toList();

        for(Integer item: delete_list){
          EH del_ent = hist_repo.findById(item)
                                .orElseThrow(() -> new DataEmptyException());

          //履歴データ削除開始---------------------------------------------------------
          status = tra_manager.getTransaction(def);
  
          deleteDatabaseCom(hist_repo, del_ent);
          deleteSambaCom(samba_comp, new_hist_id_out.apply(del_ent), true);
  
          tra_manager.commit(status);
          //トランザクション終了-------------------------------------------------------
        }
      }      
      
    }catch (DataEmptyException e) {
      this.tra_manager.rollback(status);  
      throw new DataEmptyException();

    } catch (Exception e) {
      this.tra_manager.rollback(status);  
      throw new Exception("Error location [ChangeCommonMethods:blobDeleteHistoryCom]" + "\n" + e);
    }
  }

  /** @} */
}