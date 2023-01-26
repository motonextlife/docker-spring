/** 
 **************************************************************************************
 * @file ManyParam.java
 * @brief 主に、ファイルサーバーへの保存時の個別トランザクションを実行する共通メソッドに値を渡す際の
 * 引数のエンティティのクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.InOutSamba;
import com.springproject.dockerspring.Form.BlobImplForm.Blob_Data_Form;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;
import com.springproject.dockerspring.Form.NormalForm.Del_Indivi_Hist_Form;
import com.springproject.dockerspring.Form.NormalForm.Delete_Form;
import com.springproject.dockerspring.Form.NormalForm.Rollback_Form;
import com.springproject.dockerspring.Form.NormalForm.Zip_Form;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Service.CommonMethods.CommonUtilsService.BlobEntityFunction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;










/** 
 **********************************************************************************************
 * @brief 主に、ファイルサーバーへの保存時の個別トランザクションを実行する共通メソッドに値を渡す際の
 * 引数のエンティティのクラス。
 * 
 * @details
 * - ビルダーパターンを採用し、柔軟に値をセットできることで、共通に使用できるようにしている。
 * - 一つの処理の引数に対して、全ての値を使用するわけではない。状況によってはNullのままもある。
 * - ビルダー経由のインスタンス生成のみ許可し、コンストラクタの引数からの生成は禁止する。
 * 
 * @par 使用アノテーション
 * - @Builder
 * - @Getter
 * - @AllArgsConstructor(access = AccessLevel.PRIVATE)
 **********************************************************************************************
 */
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ManyParam<EN, EH>{

  //! 保存対象のバイナリデータが格納されたフォームクラス
  private final Blob_Data_Form blob_data_form;
  
  //! ロールバック対象の履歴番号が格納されたフォームクラス
  private final Rollback_Form rollback_form;

  //! 削除対象のデータのシリアルナンバーが格納されたフォームクラス
  private final Delete_Form delete_form;

  //! 削除対象の履歴情報を検索するための情報が入ったフォームクラス
  private final Del_Indivi_Hist_Form del_indivi_hist_form;

  //! 圧縮対象のデータのシリアルナンバーと管理番号が格納されたフォームクラス
  private final Zip_Form zip_form;
  
  //! 保存するバイナリデータのデータ種別
  private final Datatype_Enum datatype_enum;
  
  //! 格納対象のファイルが格納されたリスト
  private final List<FileEntity> file_entity_list;
  
  //! ファイルサーバーへの保存を行うコンポーネントクラス
  private final InOutSamba samba_comp;
  
  //! 通常データの保存を行うリポジトリクラス
  private final FindAllCrudRepository<EN, Integer> normal_repo;
  
  //! 履歴データの保存を行うリポジトリクラス
  private final FindAllCrudRepository<EH, Integer> hist_repo;
  
  //! 保存完了後の通常エンティティから、自動連番でついた新規のシリアルナンバーを取り出す関数型インターフェース
  private final Function<EN, Integer> new_serial_out;
  
  //! 保存完了後の履歴エンティティから、自動連番でついた新規の履歴番号を取り出す関数型インターフェース
  private final Function<EH, Integer> new_hist_id_out;
  
  //! フォームクラスから抽出したデータを、対象の特定の通常エンティティクラスに格納する関数型インターフェース
  private final BlobEntityFunction<EN> normal_entity_make;
  
  //! フォームクラスから抽出したデータを、対象の特定の履歴エンティティクラスに格納する関数型インターフェース
  private final BlobEntityFunction<EH> hist_entity_make;
  
  //! 指定した履歴エンティティから、保存されているハッシュ値を取得する関数型インターフェース
  private final Function<EH, String> hash_out;
  
  //! ロールバックしようとしているデータを判定し、新規追加扱いか、更新扱いかを判定する関数型インターフェース
  private final Predicate<EH> new_append_check;

  //! ロールバックしようとしているデータを判定し、最新のシリアルアンバーのデータを上書きするか判定する関数型インターフェース
  private final Predicate<EH> latest_serial_check;
  
  //! ロールバック後に、バイナリデータの保存数の制限数を超過しないか判定する関数型インターフェース
  private final Predicate<EH> count_check;
  
  //! 指定された履歴エンティティのシリアルナンバーを、Nullに上書きして新規追加扱いのロールバックにする関数型インターフェース
  private final Consumer<EH> serial_null;

  //! 指定された履歴エンティティのシリアルナンバーを、最新の番号に上書きしたロールバックにする関数型インターフェース
  private final Consumer<EH> serial_latest;
  
  //! 指定した履歴エンティティを、対象の特定の通常エンティティクラスに格納する関数型インターフェース
  private final Function<EH, EN> normal_entity_refill;
  
  //! 指定した通常エンティティを、対象の特定の履歴エンティティクラスに格納する関数型インターフェース
  private final Function<EN, EH> hist_entity_refill;

  //! ロールバック時に取得してきたテキストデータの履歴情報を出力時バリデーションにかける関数型インターフェース
  private final Predicate<EH> rollback_output_valid_normal;

  //! ロールバック時に取得してきたバイナリデータの履歴情報を出力時バリデーションにかける関数型インターフェース
  private final BiPredicate<EH, File> rollback_output_valid_blob;

  //! 履歴の削除時に削除の対象となる期間のエンティティのリストを返す関数型インターフェース
  private final BiFunction<Date, Date, List<EH>> history_date_between;
}
