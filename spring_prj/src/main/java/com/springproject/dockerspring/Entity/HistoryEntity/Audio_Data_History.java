/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Entity.HistoryEntity
 * 
 * @brief このシステム上で扱うエンティティクラスのうち、[履歴データ用のエンティティクラス]を
 * 格納したパッケージ
 * 
 * @details
 * - このパッケージでは、通常データ用のエンティティの項目に、履歴付随情報を追加したエンティティを
 * 格納する。
 * - ここで言う履歴付随情報とは、履歴情報の日時や操作ユーザー名などの情報である。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.HistoryEntity;






/** 
 **************************************************************************************
 * @file Audio_Data_History.java
 * @brief 主に[音源データ変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Audio_Data_Enum;
import com.springproject.dockerspring.CommonEnum.NormalEnum.History_Common_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[音源データ変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Audio_Data_History]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Audio_Data_History")
 * 
 * @see EntitySetUp
 * @see DateFormat_Enum
 * @see History_Common_Enum
 * @see Audio_Data_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Audio_Data_History")
public class Audio_Data_History implements EntitySetUp{

  //! 履歴番号(プライマリ)
  @Id
  private Integer history_id;  

  //! 履歴記録日時
  private Date change_datetime;

  //! 履歴種別
  private String change_kinds;

  //! 操作ユーザー名
  private String operation_user;

  //! シリアルナンバー
  private Integer serial_num;

  //! 音源番号
  private String sound_id;

  //! 音源名
  private String sound_name;

  //! 音源ハッシュ値
  private String audio_hash;










  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータのうち、文字列型かつ空文字や空白のデータをNullに初期化する。
   * 
   * @details このメソッドは以下の理由で設けた。
   * - 空文字や空白のままデータベースに保存されると、他のテーブルとの参照制約に悪影響を及ぼすため。
   * 
   * @par 処理の大まかな流れ
   * -# 各フィールド変数のうち、文字列型の物のみを対象とする。
   * -# フィールド内に格納されている文字列が「空白」又は「空文字」か判別する。
   * -# 該当すれば、内容を「Null」に置き換える。
   **********************************************************************************************
   */
  @Override
  public void stringSetNull(){

    if(this.change_kinds == null || this.change_kinds.isEmpty() || this.change_kinds.isBlank()){
      this.change_kinds = null;
    }

    if(this.operation_user == null || this.operation_user.isEmpty() || this.operation_user.isBlank()){
      this.operation_user = null;
    }

    if(this.sound_id == null || this.sound_id.isEmpty() || this.sound_id.isBlank()){
      this.sound_id = null;
    }

    if(this.sound_name == null || this.sound_name.isEmpty() || this.sound_name.isBlank()){
      this.sound_name = null;
    }

    if(this.audio_hash == null || this.audio_hash.isEmpty() || this.audio_hash.isBlank()){
      this.audio_hash = null;
    }
  }







  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータを全て文字列型に変換し、マップリストへ格納して返却する。
   * 
   * @details
   * - 主に、フロント側の画面への出力の際に用いるデータを作成する目的として、このメソッドを使用する。
   * - 「Null」が入っているデータは、全て「空文字」に変換される。
   * 
   * @return 文字列に変換が終わり、キー名を項目の英語名としたマップリスト。
   * 
   * @par 処理の大まかな流れ
   * -# 格納済みのデータを全てNull初期化する。
   * -# フィールドに格納されているデータが「Null」であれば、例外なく「空文字」をマップリストに登録。
   * -# 文字列型データはそのままマップリストに登録し、それ以外のデータ型は文字列型に変換して登録。
   * 
   * @see DateFormat_Enum
   * @see History_Common_Enum
   * @see Audio_Data_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap(){
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_datetime = new SimpleDateFormat(DateFormat_Enum.DATETIME.getFormat());

    stringSetNull();

    convert_map.put(History_Common_Enum.History_Id.getEnName(),
                    this.history_id == null ? "" : String.valueOf(this.history_id));
            
    convert_map.put(History_Common_Enum.Change_Datetime.getEnName(),
                    this.change_datetime == null ? "" : parse_datetime.format(this.change_datetime));

    convert_map.put(History_Common_Enum.Change_Kinds.getEnName(),
                    this.change_kinds == null ? "" : this.change_kinds);

    convert_map.put(History_Common_Enum.Operation_User.getEnName(),
                    this.operation_user == null ? "" : this.operation_user);

    convert_map.put(Audio_Data_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));

    convert_map.put(Audio_Data_Enum.Sound_Id.getEnName(), 
                    this.sound_id == null ? "" : this.sound_id);

    convert_map.put(Audio_Data_Enum.Sound_Name.getEnName(), 
                    this.sound_name == null ? "" : this.sound_name);

    return convert_map;
  }
}