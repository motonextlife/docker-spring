/** 
 **************************************************************************************
 * @file SoundRepoValid.java
 * @brief DIが必要なフォームバリデーションの項目に関して、[音源関連のリポジトリからの値が必要な項目]の
 * 判定を行うフォームバリデーションクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.InjectValids.AggregationValid.SeparationDuties;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springproject.dockerspring.Entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.Entity.NormalEntity.Audio_Data;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.Form.ValidTable_Enum;
import com.springproject.dockerspring.Repository.HistoryRepo.Audio_Data_History_Repo;
import com.springproject.dockerspring.Repository.HistoryRepo.Sound_Source_History_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Audio_Data_Repo;
import com.springproject.dockerspring.Repository.NormalRepo.Sound_Source_Repo;

import lombok.RequiredArgsConstructor;







/** 
 **************************************************************************************
 * @brief DIが必要なフォームバリデーションの項目に関して、[音源関連のリポジトリからの値が必要な項目]
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
 * @see Sound_Source_Repo
 * @see Sound_Source_History_Repo
 * @see Audio_Data_Repo
 * @see Audio_Data_History_Repo
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SoundRepoValid{
  
  private final Sound_Source_Repo sound_source_repo;
  private final Sound_Source_History_Repo sound_source_hist;
  private final Audio_Data_Repo audio_data_repo;
  private final Audio_Data_History_Repo audio_data_hist;









  /** 
   *******************************************************************************
   * @brief 検査対象の履歴番号がテーブル内に存在するか確認する。
   * 
   * @details　
   * - 使用の用途としては、ロールバック時の対象のデータの指定の際の履歴番号が、テーブル内に
   * 存在し、処理が可能かどうかを判定する。
   * - バイナリデータ用の履歴用テーブルに関しては、履歴番号の有無の判定と同時に、その履歴
   * 番号のデータがあった場合、それに紐付いている管理番号が参照元のテーブルに存在しており、
   * ロールバックしてもエラーが出ないかを判定する。
   * 
   * @return 判定結果真偽値
   * 
   * @param[in] history_id 検査対象の履歴番号
   * @param[in] table 検査対象のテーブル名
   * 
   * @par 大まかな処理の流れ
   * -# 指定されたテーブル内に、指定した履歴番号が存在するか確認する。
   * -# 対象のテーブルがバイナリデータ用だった場合、指定の履歴番号に紐付いている管理番号で
   * 参照元のテーブルを検索する。
   * -# 管理番号で参照元を検索した際に、同じ管理番号が存在することを確認する。
   * -# 条件に当てはまらなければ、指定されたフィールドにエラーインスタンスにエラーを
   * 登録して戻り値とする。条件に合致していれば、空のエラーインスタンスを返す。
   *******************************************************************************
   */
  public Boolean historyIdExistCheck(Integer history_id, ValidTable_Enum table) {

    Boolean judge = false;

    switch(table){
      case Sound_Source_History:
        judge = sound_source_hist.existsById(history_id);
        break;

      case Audio_Data_History:
        Optional<Audio_Data_History> audio_data_opt = audio_data_hist.findById(history_id);
        judge = audio_data_opt.isPresent() 
             && sound_source_repo.findBySound_Id(audio_data_opt.get().getSound_id()).isPresent();
      break;

      default:
        throw new IllegalArgumentException("Error location [SoundRepoValid:historyIdExistCheck]");
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
   * - バイナリデータの削除に関しては、複数一度に削除することがあるので、シリアルナンバーの
   * リストを全てチェックする。
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
      case Sound_Source:
        Optional<Sound_Source> sound_source_opt = sound_source_repo.findById(serial_num_list.get(0));
        judge = sound_source_opt.isPresent() && sound_source_opt.get().getSound_id().equals(id);
        break;

      case Audio_Data:
        List<Optional<Audio_Data>> audio_data_opt = serial_num_list.stream()
                                                                   .parallel()
                                                                   .map(s -> audio_data_repo.findById(s))
                                                                   .toList();
        judge = audio_data_opt.stream()
                              .parallel()
                              .allMatch(s -> s.isPresent() && s.get().getSound_id().equals(id));
        break;

      default:
        throw new IllegalArgumentException("Error location [SoundRepoValid:pairExistCheckValid]");
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
      case Sound_Source:
        judge = sound_source_repo.findBySound_Id(id).isPresent();
       break;

      default:
        throw new IllegalArgumentException("Error location [SoundRepoValid:foreignKeyCheck]");
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
      case Sound_Source:
        if(serial_num == null){
          judge = sound_source_repo.findBySound_Id(id).isEmpty();
        }else{
          if(sound_source_repo.findById(serial_num).isPresent()){
            judge = sound_source_repo.checkUnique(serial_num, id).equals(0);
          }
        }
        break;

      default:
        throw new IllegalArgumentException("Error location [SoundRepoValid:idUniqueCheck]");
    }
    
    return judge;
  }
}
