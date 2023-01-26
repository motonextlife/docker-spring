/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Service.ServiceImpl
 * 
 * @brief 実際に処理をする各機能のサービスクラスを格納したパッケージ。
 * 
 * @details
 * - このパッケージは、コントローラからURLのリクエストに応じて呼び出される、システム内の全ての
 * 処理を集約して一連の処理を定義した機能を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceImpl;







/** 
 **************************************************************************************
 * @file Admin_ServiceImpl.java
 * @brief 主に[管理者]が使用するサービスの機能を実装するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Form.FormInterface.System_User_Form_Output_Validator;
import com.springproject.dockerspring.Form.FormInterface.Usage_Authority_Form_Output_Validator;
import com.springproject.dockerspring.Form.NormalForm.Com_Search_Form;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.System_User_Form;
import com.springproject.dockerspring.Form.NormalForm.Usage_Authority_Form;
import com.springproject.dockerspring.Repository.FindAllResult;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;
import com.springproject.dockerspring.Service.CommonMethods.CommonUtilsService;
import com.springproject.dockerspring.Service.OriginalException.DataEmptyException;
import com.springproject.dockerspring.Service.OriginalException.OutputDataFailedException;
import com.springproject.dockerspring.Service.ServiceInterface.Admin_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Audio_Data_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Facility_Photo_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Facility_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Member_Info_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Musical_Score_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Score_Pdf_Service;
import com.springproject.dockerspring.Service.ServiceInterface.Sound_Source_Service;

import lombok.RequiredArgsConstructor;











/** 
 **************************************************************************************
 * @brief 主に[管理者]が使用するサービスの機能を実装するクラス
 * 
 * @details 
 * - このクラスでは、コントローラーの直下で実行する処理をサービスクラスとして定義する。
 * - 内容としては、このシステム内に実装されている各機能を集約し、コントローラー側からすぐに
 * 使用できる為にする実装である。
 * - なお、このクラスは多くのクラスをDIしており、このクラス自体の責務が大きいことになっているが
 * 、サービスクラスの特性上、致し方ないものとする。ここである程度集約しておかないと、コントローラー
 * 側の処理が膨大になってしまうからである。
 * 
 * @see AdminService
 * @see CommonUtilsService
 * @see Audio_Data_Service
 * @see Sound_Source_Service
 * @see Facility_Service
 * @see Facility_Photo_Service
 * @see Member_Info_Service
 * @see Musical_Score_Service
 * @see Score_Pdf_Service
 * @see System_User_Repo
 * @see Usage_Authority_Repo
 * @see System_User_Form_Output_Validator
 * @see Usage_Authority_Form_Output_Validator
 * @see BCryptPasswordEncoder
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
public class Admin_ServiceImpl implements Admin_Service{

  private final CommonUtilsService common_utils;
  private final Audio_Data_Service audio_serv;
  private final Sound_Source_Service sound_serv;
  private final Facility_Service faci_serv;
  private final Facility_Photo_Service faciphoto_serv;
  private final Member_Info_Service membinfo_serv;
  private final Musical_Score_Service score_serv;
  private final Score_Pdf_Service pdf_serv;
  private final System_User_Repo sysuser_repo;
  private final Usage_Authority_Repo usaauth_repo;
  private final System_User_Form_Output_Validator sysuser_valid;
  private final Usage_Authority_Form_Output_Validator usaauth_valid;
  private final BCryptPasswordEncoder encoder;











  /** 
   **********************************************************************************************
   * @brief 指定されたテーブルの、指定された期間内の履歴データを削除する。
   * 
   * @details
   * - 複数のテーブルの履歴情報をまとめて削除する際に、並列処理ができるようにする。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# フォームから指定されたテーブルの名前を抜き出し、削除処理を行う機能を指定する。
   * -# 指定された期間の履歴情報を指定し、その期間の履歴情報を削除する。
   * -# 全機能を履歴の削除を指定された場合は、すべての削除処理を並列で実行する。
   * 
   * @see Del_Indivi_Hist_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  public void deleteHistory(Del_Indivi_Hist_Form form) throws Exception {

    String table_name = form.getTable_name();

    try{
      switch(table_name){
        case "All":
          CompletableFuture.allOf(this.audio_serv.deleteHistory(form), 
                                  this.faciphoto_serv.deleteHistory(form), 
                                  this.faci_serv.deleteHistory(form),
                                  this.membinfo_serv.deleteHistory(form), 
                                  this.score_serv.deleteHistory(form), 
                                  this.pdf_serv.deleteHistory(form), 
                                  this.sound_serv.deleteHistory(form)).join();
          break;

        case "Audio_Data":
          this.audio_serv.deleteHistory(form);
          break;

        case "Facility_Photo":
          this.faciphoto_serv.deleteHistory(form);
          break;

        case "Facility":
          this.faci_serv.deleteHistory(form);
          break;

        case "Member_Info":
          this.membinfo_serv.deleteHistory(form);
          break;

        case "Musical_Score":
          this.score_serv.deleteHistory(form);
          break;

        case "Score_Pdf":
          this.pdf_serv.deleteHistory(form);
          break;

        case "Sound_Source":
          this.sound_serv.deleteHistory(form);
          break;

        default:
          throw new IllegalArgumentException("Error location [Admin_ServiceImpl:deleteHistory(IllegalArgumentException)]");
      }

    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:deleteHistory(Exception)]" + "\n" + e);
    }
  }
















  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致したシステムユーザーデータを全て取得する。
   * 
   * @details
   * - 検索した結果、結果が存在しなかった場合と、検索結果を出力する際にデータに不正があって出力できない
   * 場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form 検索時の必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 引数で渡ってきたフォームクラスを、共通の検索メソッドにわたし、検索した結果のリストを取得する。
   * -# 取得した結果のリストを、出力時バリデーションに投入し検査を行う。もし不正があった場合は、直ちに
   * 処理を終了する。
   * -# 検査が正常に終了すれば、そのリストを戻り値とする。
   * 
   * @see Com_Search_Form
   * @see FindAllResult
   * @see System_User
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FindAllResult<System_User> accountList(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<System_User> result = this.common_utils.selectAllDataCom(this.sysuser_repo, form);

      for(System_User item: result.getResult_list()){
        Boolean judge = this.common_utils.outputValidCom(item, this.sysuser_valid, null, null);

        if(!judge){
          throw new OutputDataFailedException("output data failed");
        }
      }

      return result;

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("account data failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:accountList]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 指定された団員番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した団員番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] member_id 指定する団員番号
   *
   * @par 大まかな処理の流れ
   * -# 引数で渡ってきた団員番号を用いて個別情報の検索を行い、検索した結果のエンティティを取得する。
   * 検索した結果がなければ、専用の例外を投げる。
   * -# 取得したデータを出力時バリデーションに投入し検査を行う。不正があった場合は、直ちに処理を終了。
   * -# 検査が正常に終了すれば、そのエンティティを戻り値とする。
   *
   * @see System_User
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public System_User checkAccount(String member_id) throws Exception, DataEmptyException, OutputDataFailedException {

    try{
      System_User entity = this.sysuser_repo.findByMember_id(member_id)
                                            .orElseThrow(() -> new DataEmptyException());

      Boolean judge = this.common_utils.outputValidCom(entity, this.sysuser_valid, null, null);

      if(judge){
        return entity;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("account data failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:checkAccount]" + "\n" + e);
    }
  }














  /** 
   **********************************************************************************************
   * @brief フォームから渡されたシステムユーザー情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   *
   * @par 大まかな処理の流れ
   * -# 引数で渡ってきたフォームクラスの「パスワード文字列」を取り出し、ハッシュ化した文字列で上書きする。
   * -# データ保存の共通処理にフォームクラスをわたし、データの保存を行う。
   *
   * @see System_User_Form
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  public void changeAccount(System_User_Form form) throws Exception {

    try{
      form.setPassword(encoder.encode(form.getPassword()));
      this.common_utils.changeDatabaseCom(this.sysuser_repo, new System_User(form));

    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:changeAccount]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 指定されたシステムユーザーデータ内のシリアルナンバーのデータを削除する。
   * 
   * @details
   * - 削除しようとしたデータが見つからなかった場合は、特定の例外を投げ、別として対応する。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   *
   * @par 大まかな処理の流れ
   * -# 指定された団員番号のデータを取得する。データが存在しなければ専用の例外を投げる。
   * -# データ削除の共通処理に削除対象のデータのエンティティをわたし、データの削除を行う。
   *
   * @see Delete_Form
   * @see System_User
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Override
  public void deleteAccount(Delete_Form form) throws Exception, DataEmptyException {

    try{
      System_User entity = this.sysuser_repo.findByMember_id(form.getId())
                                            .orElseThrow(() -> new DataEmptyException());

      this.common_utils.deleteDatabaseCom(this.sysuser_repo, entity);

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:deleteAccount]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief 指定した検索ワードと検索ジャンルを用いて、条件に合致した権限情報データを全て取得する。
   * 
   * @details
   * - 検索した結果、結果が存在しなかった場合と、検索結果を出力する際にデータに不正があって出力できない
   * 場合は、特定の例外を投げ別として扱う。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 検索結果のリスト
   * 
   * @param[in] form 検索時の必要な情報を格納したフォームクラス
   * 
   * @par 大まかな処理の流れ
   * -# 引数で渡ってきたフォームクラスを、共通の検索メソッドにわたし、検索した結果のリストを取得する。
   * -# 取得した結果のリストを、出力時バリデーションに投入し検査を行う。もし不正があった場合は、直ちに
   * 処理を終了する。
   * -# 検査が正常に終了すれば、そのリストを戻り値とする。
   * 
   * @see Com_Search_Form
   * @see FindAllResult
   * @see Usage_Authority
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public FindAllResult<Usage_Authority> authList(Com_Search_Form form) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      FindAllResult<Usage_Authority> result = this.common_utils.selectAllDataCom(this.usaauth_repo, form);

      for(Usage_Authority item: result.getResult_list()){
        Boolean judge = this.common_utils.outputValidCom(item, usaauth_valid, null, null);

        if(!judge){
          throw new OutputDataFailedException("output data failed");
        }
      }

      return result;

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("authority data failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:authList]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定された権限番号に対応する情報を、個別にデータベースから取得する。
   * 
   * @details
   * - このメソッドは、単体情報の検索時に用いられる。
   * - 指定した権限番号の情報が見つからなかったり、取得したデータに不正があって出力できない場合は、特定の例外を
   * 出し、別として扱う。
   * - その他の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @return 取得したデータのエンティティ
   * 
   * @param[in] auth_id 指定する権限番号
   *
   * @par 大まかな処理の流れ
   * -# 引数で渡ってきた権限番号を用いて個別情報の検索を行い、検索した結果のエンティティを取得する。
   * 検索した結果がなければ、専用の例外を投げる。
   * -# 取得したデータを出力時バリデーションに投入し検査を行う。不正があった場合は、直ちに処理を終了。
   * -# 検査が正常に終了すれば、そのエンティティを戻り値とする。
   *
   * @see Usage_Authority
   *
   * @throw Exception
   * @throw DataEmptyException
   * @throw OutputDataFailedException
   **********************************************************************************************
   */
  @Override
  public Usage_Authority checkAuth(String auth_id) throws Exception, DataEmptyException, OutputDataFailedException{

    try{
      Usage_Authority entity = this.usaauth_repo.findByAuth_id(auth_id)
                                                .orElseThrow(() -> new DataEmptyException());

      Boolean judge = this.common_utils.outputValidCom(entity, this.usaauth_valid, null, null);

      if(judge){
        return entity;
      }else{
        throw new OutputDataFailedException("output data failed");
      }

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(OutputDataFailedException e){
      throw new OutputDataFailedException("authority data failed" + "\n" + e);
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:checkAuth]" + "\n" + e);
    }
  }













  /** 
   **********************************************************************************************
   * @brief フォームから渡された権限情報をデータベース内に格納する。
   * 
   * @details
   * - このメソッドは、新規追加と更新で共用できる。
   * - 例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form データベースに格納する情報を格納したフォームクラス
   *
   * @par 大まかな処理の流れ
   * -# データ保存の共通処理にフォームクラスをわたし、データの保存を行う。
   *
   * @see Usage_Authority_Form
   * @see Usage_Authority
   *
   * @throw Exception
   **********************************************************************************************
   */
  @Override
  public void changeAuth(Usage_Authority_Form form) throws Exception {

    try{
      this.common_utils.changeDatabaseCom(this.usaauth_repo, new Usage_Authority(form));
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:changeAuth]" + "\n" + e);
    }
  }












  /** 
   **********************************************************************************************
   * @brief 指定された権限データ内のシリアルナンバーのデータを削除する。
   * 
   * @details
   * - 削除しようとしたデータが見つからなかった場合は、特定の例外を投げ、別として対応する。
   * - それ以外の例外に関しては、予期せぬ動作が発生してしまうのを防ぐため、すべてまとめてキャッチする。
   * 
   * @param[in] form 削除に必要なデータが格納されたフォームクラス
   *
   * @par 大まかな処理の流れ
   * -# 指定された権限番号のデータを取得する。データが存在しなければ専用の例外を投げる。
   * -# データ削除の共通処理に削除対象のデータのエンティティをわたし、データの削除を行う。
   *
   * @see Delete_Form
   * @see Usage_Authority
   *
   * @throw Exception
   * @throw DataEmptyException
   **********************************************************************************************
   */
  @Override
  public void deleteAuth(Delete_Form form) throws Exception, DataEmptyException {

    try{
      Usage_Authority entity = this.usaauth_repo.findByAuth_id(form.getId())
                                                .orElseThrow(() -> new DataEmptyException());

      this.common_utils.deleteDatabaseCom(this.usaauth_repo, entity);

    }catch(DataEmptyException e){
      throw new DataEmptyException();
    }catch(Exception e){
      throw new Exception("Error location [Admin_ServiceImpl:deleteAuth]" + "\n" + e);
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
      throw new Exception("Error location [Admin_ServiceImpl:parseString]" + "\n" + e);
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
  public List<Map<String, String>> mapPacking(List<EntitySetUp> data_list) throws Exception{
    try {
      return this.common_utils.mapPackingCom(data_list);
    } catch (Exception e) {
      throw new Exception("Error location [Admin_ServiceImpl:mapPacking]" + "\n" + e);
    }
  }
}