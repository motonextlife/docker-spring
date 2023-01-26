/** 
 **************************************************************************************
 * @file Facility.java
 * @brief 主に[設備情報]機能のデータの、データベース保存やデータのやり取りに使用する
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

import com.springproject.dockerspring.CommonEnum.NormalEnum.Facility_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Form.CsvImplForm.Facility_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/** 
 **************************************************************************************
 * @brief 主に[設備情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Facility]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Facility")
 * 
 * @see EntitySetUp
 * @see Facility_Form
 * @see Facility_History
 * @see Facility_Enum
 * @see DateFormat_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Facility")
public class Facility implements EntitySetUp{

  @Id
  //! シリアルナンバー(プライマリ)
  private Integer serial_num;
  
  //! 設備番号
  private String faci_id;
  
  //! 設備名
  private String faci_name;
  
  //! 購入日
  private Date buy_date;
  
  //! 製作者
  private String producer;
  
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
   * @see Facility_Form
   **********************************************************************************************
   */
  public Facility(Facility_Form form_clazz){

    this.serial_num = form_clazz.getSerial_num();
    this.faci_id = form_clazz.getFaci_id();
    this.faci_name = form_clazz.getFaci_name();
    this.buy_date = form_clazz.getBuy_date();
    this.producer = form_clazz.getProducer();
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
   * @see Facility_History
   **********************************************************************************************
   */
  public Facility(Facility_History hist_entity){

    this.serial_num = hist_entity.getSerial_num();
    this.faci_id = hist_entity.getFaci_id();
    this.faci_name = hist_entity.getFaci_name();
    this.buy_date = hist_entity.getBuy_date();
    this.producer = hist_entity.getProducer();
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
   * @see Facility_Enum
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public Facility(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getFormat());

    try{
      this.serial_num = null;
      this.faci_id = csv_map.get(Facility_Enum.Faci_Id.getEnName());
      this.faci_name = csv_map.get(Facility_Enum.Faci_Name.getEnName());

      String tmp = csv_map.get(Facility_Enum.Buy_Date.getEnName());
      this.buy_date = tmp == null ? null : parse_date.parse(tmp);

      this.producer = csv_map.get(Facility_Enum.Producer.getEnName());
      this.storage_loc = csv_map.get(Facility_Enum.Storage_Loc.getEnName());

      tmp = csv_map.get(Facility_Enum.Disp_Date.getEnName());
      this.disp_date = tmp == null ? null : parse_date.parse(tmp);

      this.other_comment = csv_map.get(Facility_Enum.Other_Comment.getEnName());

    }catch(ParseException e){
      throw new ParseException("Error location [Facility:constructor3]" + "\n" + e, e.getErrorOffset());
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

    if(this.faci_id == null || this.faci_id.isEmpty() || this.faci_id.isBlank()){
      this.faci_id = null;
    }

    if(this.faci_name == null || this.faci_name.isEmpty() || this.faci_name.isBlank()){
      this.faci_name = null;
    }

    if(this.producer == null || this.producer.isEmpty() || this.producer.isBlank()){
      this.producer = null;
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
   * @see Facility_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());

    stringSetNull();

    convert_map.put(Facility_Enum.Serial_Num.getEnName(), 
                    this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
    convert_map.put(Facility_Enum.Faci_Id.getEnName(), 
                    this.faci_id == null ? "" : this.faci_id);
              
    convert_map.put(Facility_Enum.Faci_Name.getEnName(), 
                    this.faci_name == null ? "" : this.faci_name);
              
    convert_map.put(Facility_Enum.Buy_Date.getEnName(), 
                    this.buy_date == null ? "" : parse_date.format(this.buy_date));
         
    convert_map.put(Facility_Enum.Producer.getEnName(), 
                    this.producer == null ? "" : this.producer);

    convert_map.put(Facility_Enum.Storage_Loc.getEnName(), 
                    this.storage_loc == null ? "" : this.storage_loc);
              
    convert_map.put(Facility_Enum.Disp_Date.getEnName(), 
                    this.disp_date == null ? "" : parse_date.format(this.disp_date));
              
    convert_map.put(Facility_Enum.Other_Comment.getEnName(), 
                    this.other_comment == null ? "" : this.other_comment);

    return convert_map;
  }
}