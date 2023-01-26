/** 
 **************************************************************************************
 * @file Score_Pdf.java
 * @brief 主に[楽譜データ情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Score_Pdf_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[楽譜データ情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Score_Pdf]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Score_Pdf")
 * 
 * @see EntitySetUp
 * @see Score_Pdf_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Score_Pdf")
public class Score_Pdf implements EntitySetUp{

  @Id
  //! シリアルナンバー
  private Integer serial_num;

  //! 楽譜番号
  private String score_id;

  //! 楽譜名
  private String score_name;

  //! ＰＤＦハッシュ値
  private String pdf_hash;










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

    if(this.score_id == null || this.score_id.isEmpty() || this.score_id.isBlank()){
      this.score_id = null;
    }

    if(this.score_name == null || this.score_name.isEmpty() || this.score_name.isBlank()){
      this.score_name = null;
    }

    if(this.pdf_hash == null || this.pdf_hash.isEmpty() || this.pdf_hash.isBlank()){
      this.pdf_hash = null;
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
   * @see Score_Pdf_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();

    stringSetNull();

    convert_map.put(Score_Pdf_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
    convert_map.put(Score_Pdf_Enum.Score_Id.getEnName(), 
                    this.score_id == null ? "" : this.score_id);
              
    convert_map.put(Score_Pdf_Enum.Score_Name.getEnName(), 
                    this.score_name == null ? "" : this.score_name);

    return convert_map;
  }
}