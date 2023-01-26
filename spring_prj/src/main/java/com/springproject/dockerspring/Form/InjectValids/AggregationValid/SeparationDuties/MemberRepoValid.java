/** 
 **************************************************************************************
 * @file MemberRepoValid.java
 * @brief DIが必要なフォームバリデーションの項目に関して、[団員関連のリポジトリからの値が必要な項目]の
 * 判定を行うフォームバリデーションクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;
import com.springproject.dockerspring.Form.ValidTable_Enum;
import com.springproject.dockerspring.Repository.HistoryRepo.Member_Info_History_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Member_Info_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.System_User_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Usage_Authority_Repo;

import lombok.RequiredArgsConstructor;








/** 
 **************************************************************************************
 * @brief DIが必要なフォームバリデーションの項目に関して、[団員関連のリポジトリからの値が必要な項目]
 * の判定を行うフォームバリデーションクラス
 * 
 * @details 
 * - このクラスのようにDIが必要なバリデーションの項目を分離した理由としては、他のアノテーション
 * ベースのバリデーションのようにアノテーション内でDIを行おうとすると、正常にDIが行われず
 * テストができない為、動作が保証できないからである。
 * - そのため、確実に動作し品質の保証が可能な方法として、通常のフォームクラスとは分離し、
 * コントローラー内で別途バリデーションを行う方式として、このクラスを設けた。
 * 
 * @note このクラスを設けた理由としては、一つのクラスにDIするクラスが集中しないように、クラスの
 * 責務を分離させるため。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Member_Info_Repo
 * @see Member_Info_History_Repo
 * @see System_User_Repo
 * @see Usage_Authority_Repo
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberRepoValid{
  
  private final Member_Info_Repo memb_info_repo;
  private final Member_Info_History_Repo memb_info_hist;
  private final System_User_Repo system_user_repo;
  private final Usage_Authority_Repo usage_auth_repo;









  /** 
   *******************************************************************************
   * @brief 検査対象の履歴番号がテーブル内に存在するか確認する。
   * 
   * @details　
   * - 使用の用途としては、ロールバック時の対象のデータの指定の際の履歴番号が、テーブル内に
   * 存在し、処理が可能かどうかを判定する。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] history_id 検査対象の履歴番号
   * @param[in] table 検査対象のテーブル名
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたテーブル内に、指定した履歴番号が存在するか確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public Boolean historyIdExistCheck(Integer history_id, ValidTable_Enum table) {

    Boolean judge = false;

    switch(table){
      case Member_Info_History:
        judge = memb_info_hist.existsById(history_id);
        break;

      default:
        throw new IllegalArgumentException("Error location [MemberRepoValid:historyIdExistCheck]");
    }

    return judge;
  }










  /** 
   *******************************************************************************
   * @brief 指定された管理番号とシリアルナンバーのペアが存在することを確認する。
   * 
   * @details　
   * - 使用の用途としては、データ削除の際などに管理番号と複数のシリアルナンバーを指定して
   * 削除対象のデータを決定する際に、該当データが存在して削除が可能であることを確認する
   * 為である。
   * - 注意点として、バイナリデータではない通常のデータに関しては、一度に一つのデータしか
   * 削除できない仕様なので、複数のシリアルナンバーをリストとして渡したとしても、リストの最初の
   * 値しか判定しない。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] id 指定の管理番号
   * @param[in] serial_num_list 指定の複数のシリアルナンバーを格納したリスト
   * @param[in] table 検査対象のテーブル
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
  public Boolean pairExistCheckValid(String id, List<Integer> serial_num_list, ValidTable_Enum table) {

    Boolean judge = false;

    switch(table){
      case Member_Info:
        Optional<Member_Info> memb_info_opt = memb_info_repo.findById(serial_num_list.get(0));
        judge = memb_info_opt.isPresent() && memb_info_opt.get().getMember_id().equals(id);
        break;

      case System_User:
        Optional<System_User> system_user_opt = system_user_repo.findById(serial_num_list.get(0));
        judge = system_user_opt.isPresent() && system_user_opt.get().getMember_id().equals(id);
        break;

      case Usage_Authority:
        Optional<Usage_Authority> usage_auth_opt = usage_auth_repo.findById(serial_num_list.get(0));
        judge = usage_auth_opt.isPresent() && usage_auth_opt.get().getAuth_id().equals(id);
        break;

      default:
        throw new IllegalArgumentException("Error location [MemberRepoValid:pairExistCheckValid]");
    }

    return judge;
  }












  /** 
   *******************************************************************************
   * @brief 指定されたテーブルに、指定した管理番号があることを確認する。
   * 
   * @details　
   * - 使用の用途としては、バイナリデータの追加や更新を行う際に、追加しようとしている
   * データに紐付いている管理番号が、参照元のテーブルに存在しているかを確認する為。
   * 参照できない管理番号だと、データベース追加の際にエラーが発生してしまうためである。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] id 指定の管理番号
   * @param[in] table 検査対象のテーブル
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号で、指定されたテーブルを検索する。
   * -# 検索結果が存在し、指定の管理番号が参照できることを確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public Boolean foreignKeyCheck(String id, ValidTable_Enum table) {

    Boolean judge = false;

    switch(table){
      case Member_Info:
        judge = memb_info_repo.findByMember_id(id).isPresent();
        break;

      case Usage_Authority:
        judge = usage_auth_repo.findByAuth_id(id).isPresent();
        break;

      default:
        throw new IllegalArgumentException("Error location [MemberRepoValid:foreignKeyCheck]");
    }

    return judge;
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
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] id 指定の管理番号
   * @param[in] table 検査対象のテーブル
   * 
   * @par 大まかな処理の流れ
   * -# 指定された管理番号で、参照先のテーブルを検索し、結果がなくて参照されていないことを
   * 確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public Boolean deleteForeignCheck(String id, ValidTable_Enum table) {
    
    Boolean judge = false;

    switch(table){
      case Member_Info:
        try {
          judge = system_user_repo.nonExistRemainMember_id(id).get().equals(0);
        } catch (InterruptedException | ExecutionException e) {
          throw new IllegalStateException("Error location [MemberRepoValid:deleteForeignCheck]" + "\n" + e);
        }
        break;

      case Usage_Authority:
        try {
          judge = system_user_repo.nonExistRemainAuth_id(id).get().equals(0);
        } catch (InterruptedException | ExecutionException e) {
          throw new IllegalStateException("Error location [MemberRepoValid:deleteForeignCheck]" + "\n" + e);
        }
        break;

      default:
        throw new IllegalArgumentException("Error location [MemberRepoValid:deleteForeignCheck]");
    }

    return judge;
  }











  /** 
   *******************************************************************************
   * @brief 指定された管理番号を指定のテーブルに追加した際に、重複しないかを判定する。
   * 
   * @details　
   * - 使用の用途としては、データの新規追加や更新の際に管理番号を指定するが、同じデータが
   * 既に対象テーブル内にあった場合、一意制約エラーとなる。それを防ぐために用いる。
   * - 新規追加の時と、更新の時で判定処理の挙動が異なる。
   * - 新規追加の際は、単純に対象テーブルを指定した管理番号で検索して、検索結果が返ってこない
   * 事を確認する。
   * - 更新の際は、新規追加の時みたいに同じ存在チェックをしてしまうと、既にある指定の管理番号
   * 自身のせいで一意制約エラーとバリデーションエラーと認識されてしまうので、管理番号と
   * 同時に紐付いているシリアルナンバーを指定して「そのシリアルナンバー以外のデータだけ」を
   * 対象にして管理番号の検索を行う。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] id 指定の管理番号
   * @param[in] serial_num 指定のシリアルナンバー
   * @param[in] table 検査対象のテーブル
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
  public Boolean idUniqueCheck(String id, Integer serial_num, ValidTable_Enum table) {

    Boolean judge = false;

    switch(table){
      case Member_Info:
        if(serial_num == null){
          judge = memb_info_repo.findByMember_id(id).isEmpty();
        }else{
          if(memb_info_repo.findById(serial_num).isPresent()){
            judge = memb_info_repo.checkUnique(serial_num, id).equals(0);
          }
        }
        break;

      case System_User:
        if(serial_num == null){
          judge = system_user_repo.findByMember_id(id).isEmpty();
        }else{
          if(system_user_repo.findById(serial_num).isPresent()){
            judge = system_user_repo.checkUnique(serial_num, id).equals(0);
          }
        }
        break;

      case Usage_Authority:
        if(serial_num == null){
          judge = usage_auth_repo.findByAuth_id(id).isEmpty();
        }else{
          if(usage_auth_repo.findById(serial_num).isPresent()){
            judge = usage_auth_repo.checkUnique(serial_num, id).equals(0);
          }
        }
        break;

      default:
        throw new IllegalArgumentException("Error location [MemberRepoValid:idUniqueCheck]");
    }

    return judge;
  }









  /** 
   *******************************************************************************
   * @brief 指定されたユーザー名を、アカウント情報に登録しようとした際に重複しないことを
   * 確認する。
   * 
   * @details　
   * - 使用の用途としては、データの新規追加や更新の際にユーザー名を指定するが、同じデータが
   * 既に対象テーブル内にあった場合、一意制約エラーとなる。それを防ぐために用いる。
   * - 新規追加の時と、更新の時で判定処理の挙動が異なる。
   * - 新規追加の際は、単純に対象テーブルを指定したユーザー名で検索して、検索結果が返ってこない
   * 事を確認する。
   * - 更新の際は、新規追加の時みたいに同じ存在チェックをしてしまうと、既にある指定のユーザー名
   * 自身のせいで一意制約エラーとバリデーションエラーと認識されてしまうので、ユーザー名と
   * 同時に紐付いているシリアルナンバーを指定して「そのシリアルナンバー以外のデータだけ」を
   * 対象にしてユーザー名の検索を行う。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] username 指定のユーザー名
   * @param[in] serial_num 指定のシリアルナンバー
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
  public Boolean usernameUniqueCheck(String username, Integer serial_num) {

    Boolean judge = false;

    if(serial_num == null){
      judge = system_user_repo.sysSecurity(username).isEmpty();
    }else{
      if(system_user_repo.findById(serial_num).isPresent()){
        judge = system_user_repo.checkUsernameUnique(serial_num, username).equals(0);
      }
    }

    return judge;
  }
}
