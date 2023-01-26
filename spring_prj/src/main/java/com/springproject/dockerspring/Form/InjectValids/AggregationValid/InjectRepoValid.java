/** 
 **************************************************************************************
 * @file InjectRepoValid.java
 * @brief DIが必要なフォームバリデーションの項目に関して、[リポジトリからの値が必要な項目]の
 * 判定を行うフォームバリデーションクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.InjectValids.AggregationValid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.springproject.dockerspring.Form.ValidTable_Enum;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties.FacilityRepoValid;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties.MemberRepoValid;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties.ScoreRepoValid;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties.SoundRepoValid;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief DIが必要なフォームバリデーションの項目に関して、[リポジトリからの値が必要な項目]の
 * 判定を行うフォームバリデーションクラス
 * 
 * @details 
 * - このクラスのようにDIが必要なバリデーションの項目を分離した理由としては、他のアノテーション
 * ベースのバリデーションのようにアノテーション内でDIを行おうとすると、正常にDIが行われず
 * テストができない為、動作が保証できないからである。
 * - そのため、確実に動作し品質の保証が可能な方法として、通常のフォームクラスとは分離し、
 * コントローラー内で別途バリデーションを行う方式として、このクラスを設けた。
 * 
 * @note 実際の判定処理は、集約したクラス側に任せる。このクラスは、集約したクラスへ処理を中継
 * するためのメソッドのみ設けている。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see FacilityRepoValid
 * @see MemberRepoValid
 * @see ScoreRepoValid
 * @see SoundRepoValid
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InjectRepoValid{
  
  private final FacilityRepoValid faci_repo_valid;
  private final MemberRepoValid memb_repo_valid;
  private final ScoreRepoValid score_repo_valid;
  private final SoundRepoValid sound_repo_valid;










  /** 
   *******************************************************************************
   * @brief 検査対象の履歴番号がテーブル内に存在するか確認する。
   * 
   * @details　
   * - 使用の用途としては、ロールバック時の対象のデータの指定の際の履歴番号が、テーブル内に
   * 存在し、処理が可能かどうかを判定する。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] history_id 検査対象の履歴番号
   * @param[in] table 検査対象のテーブル名
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたテーブル内に、指定した履歴番号が存在するか確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult historyIdExistCheck(Integer history_id, ValidTable_Enum table, BindingResult bind_data) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = false;

    switch(table){
      case Member_Info_History:
        judge = memb_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Facility_History:
        judge = faci_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Musical_Score_History:
        judge = score_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Sound_Source_History:
        judge = sound_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Facility_Photo_History:
        judge = faci_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Score_Pdf_History:
        judge = score_repo_valid.historyIdExistCheck(history_id, table);
        break;

      case Audio_Data_History:
        judge = sound_repo_valid.historyIdExistCheck(history_id, table);
      break;

      default:
        throw new IllegalArgumentException("Error location [InjectRepoValid:historyIdExistCheck]");
    }

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), "history_id", "historyIdExistCheck"));
    }

    return bind_data;
  }








  /** 
   *******************************************************************************
   * @brief 指定された管理番号とシリアルナンバーのペアが存在することを確認する。
   * 
   * @details　
   * - 使用の用途としては、データ削除の際などに管理番号と複数のシリアルナンバーを指定して
   * 削除対象のデータを決定する際に、該当データが存在して削除が可能であることを確認する
   * 為である。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * - 注意点として、バイナリデータではない通常のデータに関しては、一度に一つのデータしか
   * 削除できない仕様なので、複数のシリアルナンバーをリストとして渡したとしても、リストの最初の
   * 値しか判定しない。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] id 指定の管理番号
   * @param[in] serial_num_list 指定の複数のシリアルナンバーを格納したリスト
   * @param[in] table 検査対象のテーブル
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] field_name エラー情報を登録する対象のフィールド変数名
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたシリアルナンバーで、指定テーブルの検索を行う。
   * -# 検索によって取得したデータに紐付いている管理番号が、引数で指定された管理番号と
   * 同一であることを確認する。
   * -# なお、検索の際に結果が返ってこなかったとしてもエラーとして扱われる。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult pairExistCheckValid(String id, 
                                           List<Integer> serial_num_list, 
                                           ValidTable_Enum table, 
                                           BindingResult bind_data, 
                                           String field_name) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = false;

    switch(table){
      case Member_Info:
        judge = memb_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Facility:
        judge = faci_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Facility_Photo:
        judge = faci_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Musical_Score:
        judge = score_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Score_Pdf:
        judge = score_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Sound_Source:
        judge = sound_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Audio_Data:
        judge = sound_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case System_User:
        judge = memb_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      case Usage_Authority:
        judge = memb_repo_valid.pairExistCheckValid(id, serial_num_list, table);
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectRepoValid:pairExistCheckValid]");
    }

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), field_name, "pairExistCheckValid"));
    }

    return bind_data;
  }












  /** 
   *******************************************************************************
   * @brief 指定されたテーブルに、指定した管理番号があることを確認する。
   * 
   * @details　
   * - 使用の用途としては、バイナリデータの追加や更新を行う際に、追加しようとしている
   * データに紐付いている管理番号が、参照元のテーブルに存在しているかを確認する為。
   * 参照できない管理番号だと、データベース追加の際にエラーが発生してしまうためである。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] id 指定の管理番号
   * @param[in] table 検査対象のテーブル
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] field_name エラー情報を登録する対象のフィールド変数名
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号で、指定されたテーブルを検索する。
   * -# 検索結果が存在し、指定の管理番号が参照できることを確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult foreignKeyCheck(String id, ValidTable_Enum table, BindingResult bind_data, String field_name) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = false;

    switch(table){
      case Member_Info:
        judge = memb_repo_valid.foreignKeyCheck(id, table);
        break;

      case Facility:
        judge = faci_repo_valid.foreignKeyCheck(id, table);
        break;

      case Musical_Score:
        judge = score_repo_valid.foreignKeyCheck(id, table);
        break;

      case Sound_Source:
        judge = sound_repo_valid.foreignKeyCheck(id, table);
       break;

      case Usage_Authority:
        judge = memb_repo_valid.foreignKeyCheck(id, table);
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectRepoValid:foreignKeyCheck]");
    }

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), field_name, "foreignKeyCheck"));
    }

    return bind_data;
  }










  /** 
   *******************************************************************************
   * @brief 指定テーブルの指定された管理番号が、他のテーブルから参照されていないことを確認する。
   * 
   * @details　
   * - 使用の用途としては、他のテーブルからデータを参照されているテーブルのデータを削除したい
   * 場合、参照されているままにもかかわらず削除するとエラーが発生する。
   * そのため、削除する前に削除対象の管理番号が、参照先のテーブルに存在しないか確認して
   * 参照されていないことを確認するためである。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] id 指定の管理番号
   * @param[in] table 検査対象のテーブル
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号で、参照先のテーブルを検索し、結果がなくて参照されていないことを
   * 確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult deleteForeignCheck(String id, ValidTable_Enum table, BindingResult bind_data) {

    if(bind_data.hasErrors()){
      return bind_data;
    }
    
    Boolean judge = false;

    switch(table){
      case Member_Info:
        judge = memb_repo_valid.deleteForeignCheck(id, table);
        break;

      case Usage_Authority:
        judge = memb_repo_valid.deleteForeignCheck(id, table);
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectRepoValid:deleteForeignCheck]");
    }

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), "id", "deleteForeignCheck"));
    }

    return bind_data;
  }











  /** 
   *******************************************************************************
   * @brief 指定された管理番号を指定のテーブルに追加した際に、重複しないかを判定する。
   * 
   * @details　
   * - 使用の用途としては、データの新規追加や更新の際に管理番号を指定するが、同じデータが
   * 既に対象テーブル内にあった場合、一意制約エラーとなる。それを防ぐために用いる。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * - 新規追加の時と、更新の時で判定処理の挙動が異なる。
   * - 新規追加の際は、単純に対象テーブルを指定した管理番号で検索して、検索結果が返ってこない
   * 事を確認する。
   * - 更新の際は、新規追加の時みたいに同じ存在チェックをしてしまうと、既にある指定の管理番号
   * 自身のせいで一意制約エラーとバリデーションエラーと認識されてしまうので、管理番号と
   * 同時に紐付いているシリアルナンバーを指定して「そのシリアルナンバー以外のデータだけ」を
   * 対象にして管理番号の検索を行う。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] id 指定の管理番号
   * @param[in] serial_num 指定のシリアルナンバー
   * @param[in] table 検査対象のテーブル
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * @param[in] field_name エラー情報を登録する対象のフィールド変数名
   * 
   * @par 大まかな処理の流れ
   * -# シリアルナンバーがNullで新規追加の際は、指定した管理番号で対象テーブルを
   * 検索し、データが存在しないことを確認する。
   * -# シリアルナンバーが存在する更新処理の場合は、まずそのシリアルナンバーで検索を
   * 行い、データが存在することを確認する
   * -#　データの存在確認が取れたら、専用のクエリメソッドを用いて、指定シリアルナンバー以外の
   * データを対象に管理番号の検索を行う。
   * -# 検索の結果がなくて、データが存在しないことを確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult idUniqueCheck(String id, 
                                     Integer serial_num, 
                                     ValidTable_Enum table, 
                                     BindingResult bind_data, 
                                     String field_name) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = false;

    switch(table){
      case Member_Info:
        judge = memb_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      case Facility:
        judge = faci_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      case Musical_Score:
        judge = score_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      case Sound_Source:
        judge = sound_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      case System_User:
        judge = memb_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      case Usage_Authority:
        judge = memb_repo_valid.idUniqueCheck(id, serial_num, table);
        break;

      default:
        throw new IllegalArgumentException("Error location [InjectRepoValid:idUniqueCheck]");
    }

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), field_name, "idUniqueCheck"));
    }

    return bind_data;
  }








  /** 
   *******************************************************************************
   * @brief 指定されたユーザー名を、アカウント情報に登録しようとした際に重複しないことを
   * 確認する。
   * 
   * @details　
   * - 使用の用途としては、データの新規追加や更新の際にユーザー名を指定するが、同じデータが
   * 既に対象テーブル内にあった場合、一意制約エラーとなる。それを防ぐために用いる。
   * - なお、レスポンスを早くするため、この処理を実行する前にすでに他の箇所でバリデーション
   * エラーが発生しておりエラーが登録されている場合は、処理を行わずスルーする。
   * - 新規追加の時と、更新の時で判定処理の挙動が異なる。
   * - 新規追加の際は、単純に対象テーブルを指定したユーザー名で検索して、検索結果が返ってこない
   * 事を確認する。
   * - 更新の際は、新規追加の時みたいに同じ存在チェックをしてしまうと、既にある指定のユーザー名
   * 自身のせいで一意制約エラーとバリデーションエラーと認識されてしまうので、ユーザー名と
   * 同時に紐付いているシリアルナンバーを指定して「そのシリアルナンバー以外のデータだけ」を
   * 対象にしてユーザー名の検索を行う。
   * 
   * @return 登録したエラーの情報
   * 
   * @param[in] username 指定のユーザー名
   * @param[in] serial_num 指定のシリアルナンバー
   * @param[in] bind_data エラー情報を登録するためのインスタンス
   * 
   * @par 大まかな処理の流れ
   * -# シリアルナンバーがNullで新規追加の際は、指定したユーザー名で対象テーブルを
   * 検索し、データが存在しないことを確認する。
   * -# シリアルナンバーが存在する更新処理の場合は、まずそのシリアルナンバーで検索を
   * 行い、データが存在することを確認する
   * -#　データの存在確認が取れたら、専用のクエリメソッドを用いて、指定シリアルナンバー以外の
   * データを対象にユーザー名の検索を行う。
   * -# 検索の結果がなくて、データが存在しないことを確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public BindingResult usernameUniqueCheck(String username, Integer serial_num, BindingResult bind_data) {

    if(bind_data.hasErrors()){
      return bind_data;
    }

    Boolean judge = memb_repo_valid.usernameUniqueCheck(username, serial_num);

    if(!judge){
      bind_data.addError(new FieldError(bind_data.getObjectName(), "username", "usernameUniqueCheck"));
    }

    return bind_data;
  }
}
