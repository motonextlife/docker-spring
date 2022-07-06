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
import com.springproject.dockerspring.commonenum.Sound_Source_Enum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.form.CsvImplForm.SoundSourceForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Sound_Source]
/*   テーブル「Sound_Source」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Sound_Source")
public class Sound_Source implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/

  @Id
  private Integer serial_num;
  
  private String sound_id;
  private Date upload_date;
  private String song_title;
  private String composer;
  private String performer;
  private String publisher;
  private String other_comment;






  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Sound_Source(SoundSourceForm form){

    this.serial_num = form.getSerial_num();
    this.sound_id = form.getSound_id();
    this.upload_date = form.getUpload_date();
    this.song_title = form.getSong_title();
    this.composer = form.getComposer();
    this.performer = form.getPerformer();
    this.publisher = form.getPublisher();
    this.other_comment = form.getOther_comment();

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Sound_Source(Sound_Source_History hist){

    this.serial_num = hist.getSerial_num();
    this.sound_id = hist.getSound_id();
    this.upload_date = hist.getUpload_date();
    this.song_title = hist.getSong_title();
    this.composer = hist.getComposer();
    this.performer = hist.getPerformer();
    this.publisher = hist.getPublisher();
    this.other_comment = hist.getOther_comment();

    stringSetNull();
  }






  /******************************************************************************************/
  /*   [コンストラクタ3]
  /*   CSVデータから抽出し、バリデーションが終わったデータをエンティティに格納する。
  /*   その際、全てデータベースへの新規追加扱いとするので、「serial_num」は「Null」となる
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/

  public Sound_Source(Map<String, String> map) throws ParseException{

    SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());

    try{
      this.serial_num = null;
      this.sound_id = map.get(Sound_Source_Enum.Sound_Id.getKey());

      String tmp = map.get(Sound_Source_Enum.Upload_Date.getKey());
      this.upload_date = tmp == null ? null : date.parse(tmp);
      
      this.song_title = map.get(Sound_Source_Enum.Song_Title.getKey());
      this.composer = map.get(Sound_Source_Enum.Composer.getKey());
      this.performer = map.get(Sound_Source_Enum.Performer.getKey());
      this.publisher = map.get(Sound_Source_Enum.Publisher.getKey());
      this.other_comment = map.get(Sound_Source_Enum.Other_Comment.getKey());
    }catch(ParseException e){
      throw new ParseException("Error location [Equip_Inspect:constructor3]", e.getErrorOffset());
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
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Sound_Source:stringSetNull]");
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
      map.put(Sound_Source_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Sound_Source_Enum.Sound_Id.getKey(), 
              this.sound_id == null ? "" : this.sound_id);
              
      map.put(Sound_Source_Enum.Upload_Date.getKey(), 
              this.upload_date == null ? "" : date.format(this.upload_date));
              
      map.put(Sound_Source_Enum.Song_Title.getKey(), 
              this.song_title == null ? "" : this.song_title);
              
      map.put(Sound_Source_Enum.Composer.getKey(), 
              this.composer == null ? "" : this.composer);
              
      map.put(Sound_Source_Enum.Performer.getKey(), 
              this.performer == null ? "" : this.performer);
              
      map.put(Sound_Source_Enum.Publisher.getKey(), 
              this.publisher == null ? "" : this.publisher);
              
      map.put(Sound_Source_Enum.Other_Comment.getKey(), 
              this.other_comment == null ? "" : this.other_comment);
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Sound_Source:makeMap]");
    }

    return map;
  }
}