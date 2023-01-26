/** 
 **************************************************************************************
 * @file Musical_Score_History.java
 * @brief 主に[楽譜情報変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.HistoryEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.History_Common_Enum;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Musical_Score_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;









/** 
 **************************************************************************************
 * @brief 主に[楽譜情報変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Musical_Score_History]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @Table("Musical_Score_History")
 * 
 * @see EntitySetUp
 * @see Musical_Score
 * @see History_Kind_Enum
 * @see DateFormat_Enum
 * @see History_Common_Enum
 * @see Musical_Score_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Musical_Score_History")
public class Musical_Score_History implements EntitySetUp{

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
   * @brief 対になっている通常データ用のエンティティから、この履歴用エンティティにデータを移し替える。
   * 
   * @details　移し替える際には、以下の情報を一緒に保存する。
   * - データベースに保存する履歴の種別
   * - 変更操作を行ったユーザーの名称
   * - 変更が行われた日時
   * 
   * @param[in] normal_entity この履歴用エンティティと対となる、通常データ用エンティティ
   * @param[in] hist_kind 履歴保存時の変更種別（新規追加、更新、削除など）
   * @param[in] username 変更操作を行ったユーザーの名前
   * @param[in] datetime 変更が行われた日時
   * 
   * @par 処理の大まかな流れ
   * -# 通常データ用エンティティからデータを抽出し、この履歴用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see History_Kind_Enum
   * @see Musical_Score
   **********************************************************************************************
   */
  public Musical_Score_History(Musical_Score info, History_Kind_Enum hist_kind, String username, Date datetime){

    this.history_id = null;
    this.change_datetime = datetime;
    this.change_kinds = hist_kind == null ? null : hist_kind.getKinds();
    this.operation_user = username;

    this.serial_num = info.getSerial_num();
    this.score_id = info.getScore_id();
    this.buy_date = info.getBuy_date();
    this.song_title = info.getSong_title();
    this.composer = info.getComposer();
    this.arranger = info.getArranger();
    this.publisher = info.getPublisher();
    this.storage_loc = info.getStorage_loc();
    this.disp_date = info.getDisp_date();
    this.other_comment = info.getOther_comment();

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

    if(this.change_kinds == null || this.change_kinds.isEmpty() || this.change_kinds.isBlank()){
      this.change_kinds = null;
    }

    if(this.operation_user == null || this.operation_user.isEmpty() || this.operation_user.isBlank()){
      this.operation_user = null;
    }

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
   * @see History_Common_Enum
   * @see Musical_Score_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());
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