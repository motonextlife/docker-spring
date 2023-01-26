/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Entity.NormalEntity
 * 
 * @brief このシステム上で扱うエンティティクラスのうち、[通常データ用のエンティティクラス]を
 * 格納したパッケージ
 * 
 * @details
 * - 通常データ用エンティティの中には、履歴データ用エンティティにはないシステムアカウント用や
 * 権限情報用のエンティティが存在する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;







/** 
 **************************************************************************************
 * @file Audio_Data.java
 * @brief 主に[音源データ情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Audio_Data_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Audio_Data]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Audio_Data")
 * 
 * @see EntitySetUp
 * @see Audio_Data_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Audio_Data")
public class Audio_Data implements EntitySetUp{

  @Id
  //! シリアルナンバー(プライマリ)
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
   * @see Audio_Data_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();

    stringSetNull();

    convert_map.put(Audio_Data_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));
            
    convert_map.put(Audio_Data_Enum.Sound_Id.getEnName(), 
                    this.sound_id == null ? "" : this.sound_id);
            
    convert_map.put(Audio_Data_Enum.Sound_Name.getEnName(), 
                    this.sound_name == null ? "" : this.sound_name);

    return convert_map;
  }
}