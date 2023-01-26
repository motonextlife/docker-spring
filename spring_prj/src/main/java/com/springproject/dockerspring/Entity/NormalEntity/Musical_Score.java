/** 
 **************************************************************************************
 * @file Musical_Score.java
 * @brief 主に[楽譜情報]機能のデータの、データベース保存やデータのやり取りに使用する
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

import com.springproject.dockerspring.CommonEnum.NormalEnum.Musical_Score_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Form.CsvImplForm.Musical_Score_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[楽譜情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Musical_Score]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Musical_Score")
 * 
 * @see EntitySetUp
 * @see Musical_Score_Form
 * @see Musical_Score_History
 * @see DateFormat_Enum
 * @see Musical_Score_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Musical_Score")
public class Musical_Score implements EntitySetUp{

  @Id
  //! シリアルナンバー
  private Integer serial_num;

  //! 楽譜番号
  private String score_id;

  //! 購入日
  private Date buy_date;

  //! 曲名
  private String song_title;

  //! 作曲者
  private String composer;

  //! 編曲者
  private String arranger;

  //! 出版社
  private String publisher;

  //! 保管場所
  private String storage_loc;

  //! 廃棄日
  private Date disp_date;

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
   * @see Musical_Score_Form
   **********************************************************************************************
   */
  public Musical_Score(Musical_Score_Form form_clazz){

    this.serial_num = form_clazz.getSerial_num();
    this.score_id = form_clazz.getScore_id();
    this.buy_date = form_clazz.getBuy_date();
    this.song_title = form_clazz.getSong_title();
    this.composer = form_clazz.getComposer();
    this.arranger = form_clazz.getArranger();
    this.publisher = form_clazz.getPublisher();
    this.storage_loc = form_clazz.getStorage_loc();
    this.disp_date = form_clazz.getDisp_date();
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
   * @see Musical_Score_History
   **********************************************************************************************
   */
  public Musical_Score(Musical_Score_History hist_entity){

    this.serial_num = hist_entity.getSerial_num();
    this.score_id = hist_entity.getScore_id();
    this.buy_date = hist_entity.getBuy_date();
    this.song_title = hist_entity.getSong_title();
    this.composer = hist_entity.getComposer();
    this.arranger = hist_entity.getArranger();
    this.publisher = hist_entity.getPublisher();
    this.storage_loc = hist_entity.getStorage_loc();
    this.disp_date = hist_entity.getDisp_date();
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
   * @see Musical_Score_Enum
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public Musical_Score(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getFormat());

    try{
      this.serial_num = null;
      this.score_id = csv_map.get(Musical_Score_Enum.Score_Id.getEnName());

      String tmp = csv_map.get(Musical_Score_Enum.Buy_Date.getEnName());
      this.buy_date = tmp == null ? null : parse_date.parse(tmp);

      this.song_title = csv_map.get(Musical_Score_Enum.Song_Title.getEnName());
      this.composer = csv_map.get(Musical_Score_Enum.Composer.getEnName());
      this.arranger = csv_map.get(Musical_Score_Enum.Arranger.getEnName());
      this.publisher = csv_map.get(Musical_Score_Enum.Publisher.getEnName());
      this.storage_loc = csv_map.get(Musical_Score_Enum.Storage_Loc.getEnName());

      tmp = csv_map.get(Musical_Score_Enum.Disp_Date.getEnName());
      this.disp_date = tmp == null ? null : parse_date.parse(tmp);
      
      this.other_comment = csv_map.get(Musical_Score_Enum.Other_Comment.getEnName());

    }catch(ParseException e){
      throw new ParseException("Error location [Musical_Score:constructor3]" + "\n" + e, e.getErrorOffset());
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

    if(this.score_id == null || this.score_id.isEmpty() || this.score_id.isBlank()){
      this.score_id = null;
    }

    if(this.song_title == null || this.song_title.isEmpty() || this.song_title.isBlank()){
      this.song_title = null;
    }

    if(this.composer == null || this.composer.isEmpty() || this.composer.isBlank()){
      this.composer = null;
    }

    if(this.arranger == null || this.arranger.isEmpty() || this.arranger.isBlank()){
      this.arranger = null;
    }

    if(this.publisher == null || this.publisher.isEmpty() || this.publisher.isBlank()){
      this.publisher = null;
    }

    if(this.storage_loc == null || this.storage_loc.isEmpty() || this.storage_loc.isBlank()){
      this.storage_loc = null;
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
   * @see Musical_Score_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());

    stringSetNull();

    convert_map.put(Musical_Score_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));
            
    convert_map.put(Musical_Score_Enum.Score_Id.getEnName(), 
                    this.score_id == null ? "" : this.score_id);
            
    convert_map.put(Musical_Score_Enum.Buy_Date.getEnName(), 
                    this.buy_date == null ? "" : parse_date.format(this.buy_date));
            
    convert_map.put(Musical_Score_Enum.Song_Title.getEnName(), 
                    this.song_title == null ? "" : this.song_title);
            
    convert_map.put(Musical_Score_Enum.Composer.getEnName(), 
                    this.composer == null ? "" : this.composer);
            
    convert_map.put(Musical_Score_Enum.Arranger.getEnName(), 
                    this.arranger == null ? "" : this.arranger);
            
    convert_map.put(Musical_Score_Enum.Publisher.getEnName(), 
                    this.publisher == null ? "" : this.publisher);
            
    convert_map.put(Musical_Score_Enum.Storage_Loc.getEnName(), 
                    this.storage_loc == null ? "" : this.storage_loc);
            
    convert_map.put(Musical_Score_Enum.Disp_Date.getEnName(), 
                    this.disp_date == null ? "" : parse_date.format(this.disp_date));
            
    convert_map.put(Musical_Score_Enum.Other_Comment.getEnName(), 
                    this.other_comment == null ? "" : this.other_comment);

    return convert_map;
  }
}