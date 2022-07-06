package com.springproject.dockerspring.entity.NormalEntity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.Musical_Score_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.form.CsvImplForm.MusicalScoreForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Musical_Score]
/*   テーブル「Musical_Score」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Musical_Score")
public class Musical_Score implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
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






  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Musical_Score(MusicalScoreForm form){

    this.serial_num = form.getSerial_num();
    this.score_id = form.getScore_id();
    this.buy_date = form.getBuy_date();
    this.song_title = form.getSong_title();
    this.composer = form.getComposer();
    this.arranger = form.getArranger();
    this.publisher = form.getPublisher();
    this.strage_loc = form.getStrage_loc();
    this.disp_date = form.getDisp_date();
    this.other_comment = form.getOther_comment();

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Musical_Score(Musical_Score_History hist){

    this.serial_num = hist.getSerial_num();
    this.score_id = hist.getScore_id();
    this.buy_date = hist.getBuy_date();
    this.song_title = hist.getSong_title();
    this.composer = hist.getComposer();
    this.arranger = hist.getArranger();
    this.publisher = hist.getPublisher();
    this.strage_loc = hist.getStrage_loc();
    this.disp_date = hist.getDisp_date();
    this.other_comment = hist.getOther_comment();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Musical_Score(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
      this.serial_num = null;
      this.score_id = map.get(Musical_Score_Enum.Score_Id.getKey());

      String tmp = map.get(Musical_Score_Enum.Buy_Date.getKey());
      this.buy_date = tmp == null ? null : date.parse(tmp);

      this.song_title = map.get(Musical_Score_Enum.Song_Title.getKey());
      this.composer = map.get(Musical_Score_Enum.Composer.getKey());
      this.arranger = map.get(Musical_Score_Enum.Arranger.getKey());
      this.publisher = map.get(Musical_Score_Enum.Publisher.getKey());
      this.strage_loc = map.get(Musical_Score_Enum.Strage_Loc.getKey());

      tmp = map.get(Musical_Score_Enum.Disp_Date.getKey());
      this.disp_date = tmp == null ? null : date.parse(tmp);
      
      this.other_comment = map.get(Musical_Score_Enum.Other_Comment.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Musical_Score:constructor3]", e.getErrorOffset());
    }

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
      throw new NullPointerException("Error location [Musical_Score:stringSetNull]");
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

    stringSetNull();

    try{
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
      throw new NullPointerException("Error location [Musical_Score:makeMap]");
    }

    return map;
  }
}