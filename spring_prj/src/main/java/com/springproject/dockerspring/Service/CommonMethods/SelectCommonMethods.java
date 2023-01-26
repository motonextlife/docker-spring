/** 
 **************************************************************************************
 * @file SelectCommonMethods.java
 * @brief サービスクラスでの共通処理において、データの検索と取得関連の処理を
 * 実装したクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.CommonMethods;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import com.springproject.dockerspring.CommonEnum.Environment_Config;
import com.springproject.dockerspring.CommonEnum.NormalEnum.History_Common_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.InOutSamba;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Form.CommonOutputValid.History_Form;
import com.springproject.dockerspring.Form.FormInterface.History_Form_Validator;
import com.springproject.dockerspring.Form.NormalForm.Com_Hist_Get_Form;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Repository.FindAllParam;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Security.Service.GetLoginUser;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;

import lombok.RequiredArgsConstructor;











/** 
 **************************************************************************************
 * @brief サービスクラスでの共通処理において、データの検索と取得関連の処理を
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
 * 
 * @see History_Form_Validator
 * @see GetLoginUser
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SelectCommonMethods{

  private final Environment_Config config;
  private final History_Form_Validator hist_valid;
  private final GetLoginUser get_login;










  /** 
   **********************************************************************************************
   * @brief 保存されているデータを出力する際のバリデーションを行う。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 一つでも不正なデータがあれば、直ちに例外を発し、処理を中断する。
   * - なお、メインの出力バリデーションに追加で行いたい処理があれば、関数型インターフェースを渡して追加する
   * 事ができる。
   * 
   * @return 判定結果の真偽値
   *
   * @param[in] form 検査対象のデータが格納されたフォームクラス
   * @param[in] validator 対象の出力データを判定するバリデータクラス
   * @param[in] append_func 追加でバリデーションを実行したい際に指定する関数型インターフェース
   * @param[in] entity 追加でバリデーションを実行する際に指定する検査対象のデータが入ったエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# フォームクラスがNullで無ければ、エラー情報格納インスタンスを生成し、バリデータを実行する。
   * -# バリデーションの実行後、エラー格納インスタンス内に一つでもエラー情報があればエラーとする。
   * -# 追加で行う関数型インターフェースが指定されていれば実行し、一つでもエラー情報があればエラーとする。
   *
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  public <T, E> Boolean outputValidCom(T form, Validator validator, Predicate<E> append_func, E entity){

    Boolean judge = false;

    if(form != null){
      BeanPropertyBindingResult error = new BeanPropertyBindingResult(form, "outputValidCom");
      validator.validate(form, error);

      judge = !error.hasErrors();
    }

    if(append_func != null && !append_func.test(entity)){
      judge = false;
    }

    return judge;
  }











  /** 
   **********************************************************************************************
   * @brief 保存されているデータのうち、履歴情報に関する内容を出力する際のバリデーションを行う。
   * 
   * @details
   * - この処理は、全ての履歴機能で共通の為、履歴データ用エンティティから共通の履歴情報のみ抽出して
   * バリデーションを行う。
   * 
   * @return 判定結果の真偽値
   *
   * @param[in] history_id 履歴番号
   * @param[in] change_datetime 履歴日時
   * @param[in] change_kinds 履歴種別
   * @param[in] operation_user 操作ユーザー名
   **********************************************************************************************
   */
  public Boolean outputValidHistInfoCom(Integer history_id, 
                                        Date change_datetime, 
                                        String change_kinds, 
                                        String operation_user) {

    History_Form form = new History_Form(history_id, change_datetime, change_kinds, operation_user);
    BeanPropertyBindingResult error = new BeanPropertyBindingResult(form, "outputValidHistInfoCom");
    this.hist_valid.validate(form, error);

    return !error.hasErrors();
  }












  /** 
   **********************************************************************************************
   * @brief 指定された検索ワードと検索種別、並び順を指定して、条件に合致した情報をデータベースから
   * 検索する。
   * 
   * @details
   * - 処理を行うリポジトリクラスは、特定のクラスに依存するため、引数として渡されたものを使用する。
   *
   * @param[in] repository 検索処理を行うリポジトリクラス
   * @param[in] form 検索時に必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 環境変数に指定されている1ページ当たりの出力結果件数と、フォームで渡されたページ指定を用いて、
   * 検索時のオフセット値を計算する。
   * -# 検索メソッドを実行し、検索結果を専用のエンティティに格納する。
   * -# 検索結果が全くなければ、データが無い事を示す専用の例外を発する。結果があれば、そのまま戻り値とする。
   * 
   * @see FindAllCrudRepository
   * @see Com_Search_Form
   * @see FindAllParam
   * @see FindAllResult
   *
   * @throw ExecutionException
   * @throw DataEmptyException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  public <T> FindAllResult<T> selectAllDataCom(FindAllCrudRepository<T, Integer> repository, 
                                               Com_Search_Form form)
                                               throws ExecutionException, DataEmptyException, InterruptedException, ParseException{
    
    try{
      int offset = this.config.getOffset() * (form.getPage_num() - 1);

      FindAllParam param = FindAllParam.builder().main_word(form.getMain_word())
                                                 .sub_word(form.getSub_word())
                                                 .subject(form.getSubject())
                                                 .order(form.getOrder())
                                                 .max(this.config.getMax())
                                                 .limit(this.config.getOffset())
                                                 .offset(offset)
                                                 .build();

      FindAllResult<T> result = repository.findAllBranch(param);

      if(result.getResult_count() == 0 || result.getResult_list().size() == 0){
        throw new DataEmptyException();
      }else{
        return result;
      }

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [SelectCommonMethods:selectAllDataCom(ExecutionException)]" + "\n" + e, e);
    } catch (InterruptedException e) {
      throw new InterruptedException("Error location [SelectCommonMethods:selectAllDataCom(InterruptedException)]" + "\n" + e);
    } catch (ParseException e) {
      throw new ParseException("Error location [SelectCommonMethods:selectAllDataCom(ParseException)]" + "\n" + e, 0);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定された検索ワードと検索種別、並び順を指定して、条件に合致した履歴情報をデータベースから
   * 検索する。なお、履歴検索に関しては、同時に検索期間を指定する。
   * 
   * @details
   * - 処理を行うリポジトリクラスは、特定のクラスに依存するため、引数として渡されたものを使用する。
   *
   * @param[in] repository 検索処理を行うリポジトリクラス
   * @param[in] form 検索時に必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 環境変数に指定されている1ページ当たりの出力結果件数と、フォームで渡されたページ指定を用いて、
   * 検索時のオフセット値を計算する。
   * -# 検索メソッドを実行し、検索結果を専用のエンティティに格納する。
   * -# 検索結果が全くなければ、データが無い事を示す専用の例外を発する。結果があれば、そのまま戻り値とする。
   * 
   * @see FindAllCrudRepository
   * @see Com_Hist_Get_Form
   * @see FindAllParam
   * @see FindAllResult
   *
   * @throw ExecutionException
   * @throw DataEmptyException
   * @throw InterruptedException
   * @throw ParseException
   **********************************************************************************************
   */
  public <T> FindAllResult<T> selectHistCom(FindAllCrudRepository<T, Integer> repository, 
                                            Com_Hist_Get_Form form)
                                            throws ExecutionException, DataEmptyException, InterruptedException, ParseException{
    
    try{
      int offset = this.config.getOffset() * (form.getPage_num() - 1);

      FindAllParam param = FindAllParam.builder().main_word(form.getMain_word())
                                                 .start_datetime(form.getStart_datetime())
                                                 .end_datetime(form.getEnd_datetime())
                                                 .subject(form.getSubject())
                                                 .order(form.getOrder())
                                                 .max(this.config.getMax())
                                                 .limit(this.config.getOffset())
                                                 .offset(offset)
                                                 .build();

      FindAllResult<T> result = repository.findAllBranch(param);

      if(result.getResult_count() == 0 || result.getResult_list().size() == 0){
        throw new DataEmptyException();
      }else{
        return result;
      }

    }catch(ExecutionException e){
      throw new ExecutionException("Error location [SelectCommonMethods:selectHistCom(ExecutionException)]" + "\n" + e, e);
    } catch (InterruptedException e) {
      throw new InterruptedException("Error location [SelectCommonMethods:selectHistCom(InterruptedException)]" + "\n" + e);
    }catch(ParseException e){
      throw new ParseException("Error location [SelectCommonMethods:selectHistCom(ParseException)]" + "\n" + e, 0);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定された番号のバイナリデータを、ファイルサーバーから取得する。なお、このメソッドは、通常データと
   * 履歴用データで共用である。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   *
   * @param[in] samba_comp 取得処理を行うコンポーネントクラス
   * @param[in] num_id 指定するシリアルナンバーや履歴番号
   * @param[in] hist 通常データの指定なら「False」、履歴データの指定なら「True」と指定する。
   * @param[in] hash データ取得時に照合する、データベースに保存されているバイナリデータのハッシュ値
   * 
   * @par 大まかな処理の流れ
   * -# 取得対象の番号のバイナリデータを指定し、取得の際にハッシュ値の照合を行う。
   * -# 照合が合格すれば、取得したデータのファイルを戻り値とする。
   * 
   * @see InOutSamba
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public File selectBlobDataCom(InOutSamba samba_comp, 
                                Integer num_id, 
                                Boolean hist, 
                                String hash) throws IOException, InputFileDifferException{
    
    try{
      if(hist){
        return samba_comp.historyFileOutput(num_id, hash);
      }else{
        return samba_comp.fileOutput(num_id, hash);
      }
    }catch(IOException e){
      throw new IOException("Error location [SelectCommonMethods:selectBlobDataCom]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定されたエンティティ内のデータを文字列に変換し、マップデータとして返却する。
   * 
   * @details
   * - 指定されたエンティティが履歴情報の物の場合で、現在ログイン中のユーザーに管理者権限がない場合は、
   * 履歴情報の操作ユーザー名を削除したうえで出力する。削除する理由としては、個人ユーザー名は、他のユーザーに
   * 漏らしてしまうとプライバシーの侵害に当てはまるため、管理者権限を持つ人間のみ閲覧できるようにするため。
   *
   * @param[in] entity 変換対象のデータが入ったエンティティ
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたエンティティ内の文字列変換メソッドを実行し、マップリストを取得する。
   * -# 現在ログイン中のユーザーに管理者権限がない場合、履歴情報の「操作ユーザー名」を、空文字で上書きして
   * 削除する。
   * 
   * @see EntitySetUp
   *
   * @throw IOException
   **********************************************************************************************
   */
  public Map<String, String> parseStringCom(EntitySetUp entity) throws IOException {
    try {
      Map<String, String> map = entity.makeMap();

      if(!this.get_login.existAdminRole()){
        map.put(History_Common_Enum.Operation_User.getEnName(), "");
      }

      return map;

    } catch (IOException e) {
      throw new IOException("Error location [SelectCommonMethods:parseStringCom]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 指定されたエンティティリスト内のデータを文字列に変換し、マップデータのリストとして返却する。
   * 
   * @details
   * - 内部処理としては、このクラスの[parseStringCom]を繰り返し行っているだけである。
   *
   * @param[in] data_list 変換対象のデータが入ったエンティティリスト
   * 
   * @see EntitySetUp
   **********************************************************************************************
   */
  public List<Map<String, String>> mapPackingCom(List<EntitySetUp> data_list) {
    List<Map<String, String>> result = new ArrayList<>();

    data_list.stream()
             .parallel()
             .map(s -> {
               try {
                 return parseStringCom(s);
               } catch (IOException e) {
                 throw new IllegalStateException("Error location [SelectCommonMethods:mapPackingCom]" + "\n" + e);
               }
             })
             .forEachOrdered(s -> result.add(s));

    return result;
  }
}