/** 
 **************************************************************************************
 * @file CsvZipPdfCommonMethods.java
 * @brief サービスクラスでの共通処理において、主にCSVやPDF、ZIPのファイルを取り扱う処理を
 * 実装したクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.CommonMethods;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.FileIO.CompInterface.InOutCsv;
import com.springproject.dockerspring.FileIO.CompInterface.InOutZip;
import com.springproject.dockerspring.FileIO.CompInterface.OutPdf;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.CsvImplForm.CsvCheckResult;
import com.springproject.dockerspring.Form.FormInterface.Blob_Validation;
import com.springproject.dockerspring.Form.FormInterface.Csv_Validation;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Common_Csv_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Form;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.OriginalException.OverCountException;

import lombok.RequiredArgsConstructor;










/** 
 **************************************************************************************
 * @brief サービスクラスでの共通処理において、主にCSVやPDF、ZIPのファイルを取り扱う処理を
 * 実装したクラス
 * 
 * @details 
 * - この共通処理では、サービスクラスで用いる集約が可能な機能を集めることで、メンテナンス性を
 * 向上させるために実装。
 * - ただし、特定の機能に依存してしまう処理に関しては、ここでは実装せずに関数型インターフェースで
 * 渡すなどして分離を行う。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CsvZipPdfCommonMethods{

  private final Environment_Config config;








  /** 
   **********************************************************************************************
   * @brief 指定されたデータが格納されたエンティティと、PDF出力処理用のコンポーネントのインスタンスを
   * 受け取り、出力処理を行う。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 処理としては単純で、使用するコンポーネントのPDF生成メソッドを実行するだけである。
   *
   * @return 生成したPDFファイル 
   *
   * @param[in] pdf_comp 生成処理を行うコンポーネントクラス
   * @param[in] entity 出力を指定するデータが入ったエンティティ
   * 
   * @see OutPdf
   *
   * @throw IOException
   **********************************************************************************************
   */
  public <T> File outputPdfCom(OutPdf<T> pdf_comp, T entity) throws IOException{
    try {
      return pdf_comp.makePdf(entity);
    } catch (IOException e) {
      throw new IOException("Error location [CsvZipPdfCommonMethods:outputPdfCom]" + "\n" + e);
    }
  }









  /** 
   **********************************************************************************************
   * @brief CSVファイルから抽出したデータをバリデーションし、合格ならばデータベースにデータを格納する。
   * 不合格ならエラーが発生した座標のエラーリストを返す。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - データベースへの格納処理に関しては、特定のクラスに依存する処理の為、関数型インターフェースとして
   * 処理を受け取ることで、処理を分離する。
   * - 不合格の場合はエラーリストを返すが、合格の場合はNullを返却する。
   *
   * @return 生成したエラーリスト又はNull 
   *
   * @param[in] goto_database データベースへの格納処理を行う処理の関数
   * @param[in] csv_comp CSVファイルの抽出処理とデータベース格納処理を行うコンポーネントクラス
   * @param[in] csv_form CSVから抽出したデータを判定するバリデーションクラス
   * @param[in] form 処理の対象となるCSVファイルが格納されたフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# CSVファイルからデータを抽出する。この段階でファイル内に不正なデータがあればエラーを出す。
   * -# 抽出したデータを、バリデーションクラスに渡し判定を行う。エラーがあれば中身が入ったエラーリストが
   * 返ってくる。例外が出るわけではない。
   * -# エラーリストを判定し、管理番号の重複がないかつ、エラーリストの長さが0でエラーが存在しないことを
   * 確認する。
   * -# エラーがあればエラーリストをそのまま戻り値とする。
   * -# エラーが無ければ、抽出データをデータベースに格納する処理を実行する。
   * -# 正常に処理が終われば、戻り値としてNullを返す。
   * 
   * @see InOutCsv
   * @see Csv_Validation
   * @see Common_Csv_Form
   *
   * @throw IOException
   * @throw ExecutionException
   * @throw InterruptedException
   * @throw ParseException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public <T> CsvCheckResult inputCsvCom(Consumer<List<T>> goto_database, 
                                        InOutCsv<T> csv_comp, 
                                        Csv_Validation csv_form, 
                                        Common_Csv_Form form)
                                        throws IOException, ExecutionException, InterruptedException, ParseException, InputFileDifferException{

    try{
      File csv_file = csv_comp.extractCsv(form.getCsv_file());
      CsvCheckResult check_result = csv_form.csvChecker(csv_file);

      if(check_result.getUnique() && check_result.getError_list().size() == 0){
        csv_comp.csvToDatabase(csv_file, goto_database);
        return null;
      }else{
        return check_result;
      }

    }catch(IOException e){
      throw new IOException("Error location [CsvZipPdfCommonMethods:inputCsvCom(IOException)]" + "\n" + e);
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [CsvZipPdfCommonMethods:inputCsvCom(ExecutionException)]" + "\n" + e, e);
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [CsvZipPdfCommonMethods:inputCsvCom(InterruptedException)]" + "\n" + e);
    }catch(ParseException e){
      throw new ParseException("Error location [CsvZipPdfCommonMethods:inputCsvCom(ParseException)]" + "\n" + e, 0);
    }
  }










  /** 
   **********************************************************************************************
   * @brief ZIPファイルに格納されているデータを解凍して抽出し、バリデーションを行った後、抽出したファイルの
   * リストを返す。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - バイナリデータの数量制限判定処理に関しては、特定のクラスに依存する処理の為、関数型インターフェースとして
   * 処理を受け取ることで、処理を分離する。
   *
   * @return 抽出したファイルのリスト 
   *
   * @param[in] zip_comp ZIPファイルの解凍抽出処理を行うコンポーネントクラス
   * @param[in] blob_form ZIPから抽出したデータを判定するバリデーションクラス
   * @param[in] form 処理の対象となるZIPファイルが格納されたフォームクラス
   * @param[in] count_limit バイナリデータの数量制限判定を行う処理の関数
   * 
   * @par 大まかな処理の流れ
   * -# ZIPファイルを解凍する。この段階でデータに不正があればエラーを出す。
   * -# 現状システムに保存されている、該当する管理番号の保存バイナリデータ数と、抽出して保存しようとしている
   * バイナリデータの数量を合算した場合、環境変数に定められている保存可能バイナリデータ制限数を超過しないか
   * 判定する。
   * -# 数量が超過するのであれば、専用の例外を投げ、良ければバイナリデータのバリデーションを実行する。
   * -# バリデーションの結果が合格ならば抽出したファイルを返却し、不合格ならば例外を出す。
   *
   * @see InOutZip
   * @see Blob_Validation
   * @see Zip_Form
   * 
   * @throw IOException
   * @throw OverCountException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public List<FileEntity> inputZipCom(InOutZip zip_comp, 
                                      Blob_Validation blob_form, 
                                      Zip_Form form, 
                                      BiPredicate<String, Integer> count_limit)
                                      throws IOException, OverCountException, InputFileDifferException{

    try{
      List<FileEntity> files = zip_comp.extractZip(form.getZip_file());

      if(!count_limit.test(form.getId(), files.size())){
        throw new OverCountException();
      }

      blob_form.blobChecker(files);
      return files;

    }catch(IOException e){
      throw new IOException("Error location [CsvZipPdfCommonMethods:inputZipCom]" + "\n" + e);
    }
  }









  /** 
   **********************************************************************************************
   * @brief 指定されたデータベース内のデータをCSVファイルに書き込むために、データ検索結果に応じた検索結果
   * リストを取り出す。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 取得したデータの出力バリデーション処理に関しては、特定のクラスに依存する処理の為、関数型インターフェース
   * として処理を受け取ることで、処理を分離する。
   * - 検索結果の全てのデータを取得するためには、このメソッドを複数回実行する必要がある。引数でページ数を指定し、
   * それに応じて少しずつ出力することでメモリリークを避けている。
   *
   * @return 検索結果のデータリスト 
   *
   * @param[in] repository データベースからのデータ取得を行うリポジトリクラス
   * @param[in] form 検索に必要な情報を格納したフォームクラス
   * @param[in] page 出力を指定するページ番号
   * @param[in] valid_func データ出力時のバリデーションを行う処理の関数
   * 
   * @par 大まかな処理の流れ
   * -# 指定された検索ワードをもちいてデータベースを検索し、検索結果データを出力する。
   * -# ページ番号が[0]、つまり検索処理の一番最初の段階で検索結果が0の場合は、該当するデータが全くないことに
   * なるので、専用の例外を投げて処理を終了する。
   * -# 取得したデータを出力時バリデーションに投入する。不正があればその時点で例外を出し、処理を終了する。
   * -# 合格すれば、検索結果のリストを戻り値とする。
   * 
   * @see FindAllCrudRepository
   * @see Com_Search_Form
   * 
   * @note このメソッドと同名の物がもう一つある。同じクラスで使用するので、使いやすさの為オーバーロードしている。
   *
   * @throw ExecutionException
   * @throw DataEmptyException
   * @throw InterruptedException
   * @throw ParseException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  public <T> FindAllResult<T> outputCsvCom(FindAllCrudRepository<T, Integer> repository, 
                                           Com_Search_Form form, 
                                           int page, 
                                           Predicate<T> valid_func) 
                                           throws ExecutionException, DataEmptyException, InterruptedException,  
                                                  ParseException, OutputDataFailedException{

    try{
      FindAllParam param = FindAllParam.builder().main_word(form.getMain_word())
                                                 .sub_word(form.getSub_word())
                                                 .subject(form.getSubject())
                                                 .order(form.getOrder())
                                                 .max(this.config.getMax())
                                                 .limit(this.config.getOffset())
                                                 .offset(this.config.getOffset() * page)
                                                 .build();

      FindAllResult<T> result = repository.findAllBranch(param);

      if(page == 0 && (result.getResult_count() == 0 || 
                       result.getResult_list().size() == 0)){
        throw new DataEmptyException();
      }

      Boolean judge = result.getResult_list()
                            .parallelStream()
                            .allMatch(s -> valid_func.test(s));

      if(judge){
        return result;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [CsvZipPdfCommonMethods:outputCsvCom(1)]" + "\n" + e, e);
    } catch (InterruptedException e) {
      throw new InterruptedException("Error location [CsvZipPdfCommonMethods:outputCsvCom(1)]" + "\n" + e);
    } catch (ParseException e) {
      throw new ParseException("Error location [CsvZipPdfCommonMethods:outputCsvCom(1)]" + "\n" + e, 0);
    }
  }









  /** 
   **********************************************************************************************
   * @brief 出力を指定されたデータを、CSVファイルに書き込み出力する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 出力するデータの生成処理に関しては、特定のクラスに依存する処理の為、関数型インターフェースとして処理を
   * 受け取ることで、処理を分離する。
   * - このメソッドは、通常のデータが入ったファイルと、テンプレートファイルの生成を兼ねている。
   * 出力データ生成用の関数型インターフェースが指定されていなければ、テンプレートを生成するものとみなす。
   *
   * @return 生成したCSVファイル 
   *
   * @param[in] csv_comp CSVファイルの出力処理を行うコンポーネントクラス
   * @param[in] generate 出力するデータを生成する処理の関数。Nullであれば、テンプレートの出力になる。
   * 
   * @par 大まかな処理の流れ
   * -# まず、データ出力用の関数型インターフェースが引数に指定されているか確認する。
   * -# 指定されていない場合、テンプレート用のCSVファイルの出力となるので、データは出力せず空となる。
   * -# 指定されている場合は、その関数を実行し出力するデータを取得して、CSVファイルへの書き込みを行う。
   * -# 生成したCSVファイルが、環境変数で定められているCSVファイルのデータ量の制限値を超過しないか確認する。
   * 超過した場合は出力できないので、エラーを発する。問題なければ戻り値とする。
   * 
   * @see InOutCsv
   * 
   * @note このメソッドと同名の物がもう一つある。同じクラスで使用するので、使いやすさの為オーバーロードしている。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public <T> File outputCsvCom(InOutCsv<T> csv_comp, 
                               Function<Integer, List<Map<String, String>>> generate) 
                               throws IOException, InputFileDifferException{

    try {
      if(generate != null){
        File output = csv_comp.outputCsv(generate);
        
        if(output.length() > this.config.getCsv_limit()){
          throw new InputFileDifferException("csv size over");
        }

        return output;
      }else{
        return csv_comp.outputTemplate();
      }
    } catch (IOException e) {
      throw new IOException("Error location [CsvZipPdfCommonMethods:outputCsvCom(2)]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 出力を指定されたバイナリデータを、ZIPファイルに書き込み圧縮して出力する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 出力するバイナリデータの生成処理に関しては、特定のクラスに依存する処理の為、関数型インターフェース
   * として処理を受け取ることで、処理を分離する。
   * - このメソッドは、通常のデータが入ったファイルと、テンプレートファイルの生成を兼ねている。
   *
   * @return 生成したZIPファイル 
   *
   * @param[in] generate 出力するデータを生成する処理の関数。Nullであれば、テンプレートの出力になる。
   * @param[in] zip_comp ZIPファイルの出力処理を行うコンポーネントクラス
   * 
   * @par 大まかな処理の流れ
   * -# ファイルサーバーから指定されたバイナリデータを取得し、その複数のデータを圧縮メソッドに投入する。
   * なお、バイナリデータは出力時バリデーションを実行し、不正があればエラーとする。
   * -# 完成した圧縮ファイルが、環境変数で指定されるデータ量制限値を超過しないか確認する。超過した場合は、
   * エラーを投げる。問題なければ戻り値とする。
   * 
   * @see InOutZip
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public <T> File outputZipCom(Supplier<List<FileEntity>> generate, 
                               InOutZip zip_comp) 
                               throws IOException, InputFileDifferException{

    try {
      File output = zip_comp.outputZip(generate);

      if(output.length() > config.getZip_limit()){
        throw new InputFileDifferException("zip size over");
      }

      return output;
    }catch (IOException e) {
      throw new IOException("Error location [CsvZipPdfCommonMethods:outputZipCom]" + "\n" + e);
    }
  }
}