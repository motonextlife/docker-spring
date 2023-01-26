/** 
 **************************************************************************************
 * @file Sound_Source.java
 * @brief 主に[音源情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Sound_Source_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Form.CsvImplForm.Sound_Source_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[音源情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Sound_Source]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Sound_Source")
 * 
 * @see EntitySetUp
 * @see Sound_Source_Form
 * @see Sound_Source_History
 * @see Sound_Source_Enum
 * @see DateFormat_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Sound_Source")
public class Sound_Source implements EntitySetUp{

  @Id
  //! シリアルナンバー
  private Integer serial_num;

  //! 音源番号
  private String sound_id;

  //! 登録日
  private Date upload_date;

  //! 曲名
  private String song_title;

  //! 作曲者
  private String composer;

  //! 演奏者
  private String performer;

  //! 出版社
  private String publisher;

  //! その他コメント
  private String other_comment;









  /** 
   **********************************************************************************************
   * @brief 対になっているフォームクラスから、この通常データ用エンティティにデータを移し替える。
   * 
   * @param[in] form_clazz この通常データ用エンティティと対となる、フォームクラス
   * 
   * @par 処理の大まかな流れ
   * -# フォームクラスからデータを抽出し、この通常データ用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see Sound_Source_Form
   **********************************************************************************************
   */
  public Sound_Source(Sound_Source_Form form_clazz){

    this.serial_num = form_clazz.getSerial_num();
    this.sound_id = form_clazz.getSound_id();
    this.upload_date = form_clazz.getUpload_date();
    this.song_title = form_clazz.getSong_title();
    this.composer = form_clazz.getComposer();
    this.performer = form_clazz.getPerformer();
    this.publisher = form_clazz.getPublisher();
    this.other_comment = form_clazz.getOther_comment();

    stringSetNull();
  }









  /** 
   **********************************************************************************************
   * @brief 対になっている履歴用エンティティから、この通常データ用エンティティにデータを移し替える。
   * 
   * @param[in] hist_entity この通常データ用エンティティと対となる、履歴用エンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 履歴用エンティティからデータを抽出し、この通常データ用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see Sound_Source_History
   **********************************************************************************************
   */
  public Sound_Source(Sound_Source_History hist_entity){

    this.serial_num = hist_entity.getSerial_num();
    this.sound_id = hist_entity.getSound_id();
    this.upload_date = hist_entity.getUpload_date();
    this.song_title = hist_entity.getSong_title();
    this.composer = hist_entity.getComposer();
    this.performer = hist_entity.getPerformer();
    this.publisher = hist_entity.getPublisher();
    this.other_comment = hist_entity.getOther_comment();

    stringSetNull();
  }








  /** 
   **********************************************************************************************
   * @brief CSVリストから抽出したデータをマップリストにしたものを、このエンティティに移し替える。
   * 
   * @param[in] csv_map CSVリストから抽出したデータをマップリストにしたもの
   * 
   * @par 処理の大まかな流れ
   * -# CSVマップリストからデータを抽出し、それぞれ対応するデータ型に変換する。
   * -# 変換したデータを、この通常データ用エンティティに格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @note
   * CSVリストから抽出したデータはすべて新規追加扱いになるため、SpringDataJDBCの仕様に伴い
   * シリアルナンバーは「Null」となる。
   * 
   * @see DateFormat_Enum
   * @see Sound_Source_Enum
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public Sound_Source(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getFormat());

    try{
      this.serial_num = null;
      this.sound_id = csv_map.get(Sound_Source_Enum.Sound_Id.getEnName());

      String tmp = csv_map.get(Sound_Source_Enum.Upload_Date.getEnName());
      this.upload_date = tmp == null ? null : date.parse(tmp);
      
      this.song_title = csv_map.get(Sound_Source_Enum.Song_Title.getEnName());
      this.composer = csv_map.get(Sound_Source_Enum.Composer.getEnName());
      this.performer = csv_map.get(Sound_Source_Enum.Performer.getEnName());
      this.publisher = csv_map.get(Sound_Source_Enum.Publisher.getEnName());
      this.other_comment = csv_map.get(Sound_Source_Enum.Other_Comment.getEnName());

    }catch(ParseException e){
      throw new ParseException("Error location [Equip_Inspect:constructor3]" + "\n" + e, e.getErrorOffset());
    }

    stringSetNull();
  }






  



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

    if(this.song_title == null || this.song_title.isEmpty() || this.song_title.isBlank()){
      this.song_title = null;
    }

    if(this.composer == null || this.composer.isEmpty() || this.composer.isBlank()){
      this.composer = null;
    }

    if(this.performer == null || this.performer.isEmpty() || this.performer.isBlank()){
      this.performer = null;
    }

    if(this.publisher == null || this.publisher.isEmpty() || this.publisher.isBlank()){
      this.publisher = null;
    }

    if(this.other_comment == null || this.other_comment.isEmpty() || this.other_comment.isBlank()){
      this.other_comment = null;
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
   * @see Sound_Source_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());

    stringSetNull();

    convert_map.put(Sound_Source_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
    convert_map.put(Sound_Source_Enum.Sound_Id.getEnName(), 
                    this.sound_id == null ? "" : this.sound_id);
              
    convert_map.put(Sound_Source_Enum.Upload_Date.getEnName(), 
                    this.upload_date == null ? "" : parse_date.format(this.upload_date));
              
    convert_map.put(Sound_Source_Enum.Song_Title.getEnName(), 
                    this.song_title == null ? "" : this.song_title);
              
    convert_map.put(Sound_Source_Enum.Composer.getEnName(), 
                    this.composer == null ? "" : this.composer);
              
    convert_map.put(Sound_Source_Enum.Performer.getEnName(), 
                    this.performer == null ? "" : this.performer);
              
    convert_map.put(Sound_Source_Enum.Publisher.getEnName(), 
                    this.publisher == null ? "" : this.publisher);
              
    convert_map.put(Sound_Source_Enum.Other_Comment.getEnName(), 
                    this.other_comment == null ? "" : this.other_comment);

    return convert_map;
  }
}