package com.springproject.dockerspring.entity.HistoryEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.History_Common;
import com.springproject.dockerspring.commonenum.Musical_Score_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Musical_Score_History]
/*   テーブル「Musical_Score_History」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Musical_Score_History")
public class Musical_Score_History implements Serializable, EntitySetUp{


  /*****************************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /* 
  /* なお、履歴エンティティだけにある以下の3個の意味を
  /* 記述しておく。
  /*****************************************************/

  @Id
  private Integer history_id;  //履歴番号（履歴情報を一意に判別）

  private Date change_datetime;  //履歴記録日時
  private String change_kinds;  //履歴種別（「更新」「削除」「復元」がある）
  private String operation_user;  //操作したユーザー名

  private Integer serial_num;
  private String score_id;
  private Date buy_date;
  private String song_title;
  private String composer;
  private String arranger;
  private String publisher;
  private String strage_loc;
  private Date disp_date;
  private String other_comment;







  /******************************************************************************************/
  /*   [コンストラクタ]
  /*   データが格納済みの対応する通常エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Musical_Score_History(Musical_Score info, String kind, String username){

    this.history_id = null;
    this.change_datetime = new Date();

    try{
      if(kind.equals(HistoryKindEnum.UPDATE.getKind())
         || kind.equals(HistoryKindEnum.DELETE.getKind())
         || kind.equals(HistoryKindEnum.ROLLBACK.getKind())){
        this.change_kinds = kind;
      }else{
        throw new IllegalArgumentException();
      }
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Audio_Data_History:constructor(IllegalArgumentException)]");
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Musical_Score_History:constructor]");
    }

    this.operation_user = username;
    this.serial_num = info.getSerial_num();
    this.score_id = info.getScore_id();
    this.buy_date = info.getBuy_date();
    this.song_title = info.getSong_title();
    this.composer = info.getComposer();
    this.arranger = info.getArranger();
    this.publisher = info.getPublisher();
    this.strage_loc = info.getStrage_loc();
    this.disp_date = info.getDisp_date();
    this.other_comment = info.getOther_comment();

    stringSetNull();
  }






  /***************************************************************/
  /*   [stringSetNull]
  /*   呼び出された時点で、変数内に格納されている「文字列型」の
  /*   データが「空文字又は空白のみ」の場合、「Null」に置き換える。
  /*   これは、空白や空文字がデータベース内に入り込み、参照性制約違反
  /*   などの不具合が発生しないよう、確実に空データに「Null」を
  /*   入れるための処置である。
  /***************************************************************/

  public void stringSetNull(){

    try{
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

      if(this.strage_loc == null || this.strage_loc.isEmpty() || this.strage_loc.isBlank()){
        this.strage_loc = null;
      }

      if(this.other_comment == null || this.other_comment.isEmpty() || this.other_comment.isBlank()){
        this.other_comment = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Musical_Score_History:stringSetNull]");
    }
  }






  /***************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/

  @Override
  public Map<String, String> makeMap() {
    Map<String, String> map = new HashMap<>();
    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE.getStr());
    SimpleDateFormat datetime = new SimpleDateFormat(DateFormat_Enum.DATETIME.getStr());

    stringSetNull();

    try{
      map.put(History_Common.History_Id.getKey(),
              this.history_id == null ? "" : String.valueOf(this.history_id));
              
      map.put(History_Common.Change_Datetime.getKey(),
              this.change_datetime == null ? "" : datetime.format(this.change_datetime));
              
      map.put(History_Common.Change_Kinds.getKey(),
              this.change_kinds == null ? "" : this.change_kinds);

      map.put(History_Common.Operation_User.getKey(),
              this.operation_user == null ? "" : this.operation_user);
              
      map.put(Musical_Score_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Musical_Score_Enum.Score_Id.getKey(), 
              this.score_id == null ? "" : this.score_id);
              
      map.put(Musical_Score_Enum.Buy_Date.getKey(), 
              this.buy_date == null ? "" : date.format(this.buy_date));
              
      map.put(Musical_Score_Enum.Song_Title.getKey(), 
              this.song_title == null ? "" : this.song_title);
              
      map.put(Musical_Score_Enum.Composer.getKey(), 
              this.composer == null ? "" : this.composer);
              
      map.put(Musical_Score_Enum.Arranger.getKey(), 
              this.arranger == null ? "" : this.arranger);
              
      map.put(Musical_Score_Enum.Publisher.getKey(), 
              this.publisher == null ? "" : this.publisher);
              
      map.put(Musical_Score_Enum.Strage_Loc.getKey(), 
              this.strage_loc == null ? "" : this.strage_loc);
              
      map.put(Musical_Score_Enum.Disp_Date.getKey(), 
              this.disp_date == null ? "" : date.format(this.disp_date));
              
      map.put(Musical_Score_Enum.Other_Comment.getKey(), 
              this.other_comment == null ? "" : this.other_comment);
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Musical_Score_History:makeMap]");
    }

    return map;
  }
}