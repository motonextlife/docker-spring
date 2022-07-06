package com.springproject.dockerspring.entity.NormalEntity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.apache.tika.Tika;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.util.HtmlUtils;

import com.springproject.dockerspring.commonenum.Rec_Eval_Record_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Rec_Eval_Record_History;
import com.springproject.dockerspring.form.BlobImplForm.RecEvalRecordForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Rec_Eval_Record]
/*   テーブル「Rec_Eval_Record」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Rec_Eval_Record")
public class Rec_Eval_Record implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/
  @Id
  private Integer serial_num;
  
  private String eval_id;
  private String data_kinds;
  private String record_name;
  private byte[] record_data;



  /***********************************************/
  /* 保存データの種別を入れるための定数である。
  /***********************************************/
  private final String PHOTO_STR = "photo";
  private final String MOVIE_STR = "movie";
  private final String AUDIO_STR = "audio";



  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/
  
  public Rec_Eval_Record(RecEvalRecordForm form, int num) throws IOException{

    String datatype;
    String name;
    this.serial_num = form.getSerial_num();
    this.eval_id = form.getEval_id();

    try{

      /********************************************************************/
      /*   受け取ったデータのうち、何番目のデータをエンティティに格納するか
      /*   判別し、対応するデータ名とデータを格納。
      /********************************************************************/
      switch(num){
        case 1:
          datatype = form.getRecord_data_1().getContentType();
          name = form.getRecord_data_1().getOriginalFilename();
          this.record_data = form.getRecord_data_1().getBytes();
          break;
        case 2:
          datatype = form.getRecord_data_2().getContentType();
          name = form.getRecord_data_2().getOriginalFilename();
          this.record_data = form.getRecord_data_2().getBytes();
          break;
        case 3:
          datatype = form.getRecord_data_3().getContentType();
          name = form.getRecord_data_3().getOriginalFilename();
          this.record_data = form.getRecord_data_3().getBytes();
          break;
        case 4:
          datatype = form.getRecord_data_4().getContentType();
          name = form.getRecord_data_4().getOriginalFilename();
          this.record_data = form.getRecord_data_4().getBytes();
          break;
        case 5:
          datatype = form.getRecord_data_5().getContentType();
          name = form.getRecord_data_5().getOriginalFilename();
          this.record_data = form.getRecord_data_5().getBytes();
          break;
        default:
          throw new IllegalArgumentException();
      }


      /********************************************************************/
      /*   ファイル名は、ファイルパス構成文字を削除した上で、
      /*   サニタイズしたうえで使用する。
      /*   ファイル名は、拡張子を除いた名前となる。
      /********************************************************************/
      int idx = name.lastIndexOf(".");
      String ext = idx != -1 ? name.substring(idx + 1) : "";
      name = name.replace(ext, "");
      name = name.replace("/", "");
      name = name.replace(".", "");
      name = name.replace("\\", "");

      this.record_name = HtmlUtils.htmlEscape(name);

      
      /********************************************************************/
      /*   受け取ったデータのコンテンツタイプを判断し、対応する文字列を
      /*   エンティティ内に格納する。
      /********************************************************************/
      if(datatype.equals(DatatypeEnum.PHOTO.getType())){
        this.data_kinds = PHOTO_STR;
      }else if(datatype.equals(DatatypeEnum.MOVIE.getType())){
        this.data_kinds = MOVIE_STR;
      }else if(datatype.equals(DatatypeEnum.AUDIO.getType())){
        this.data_kinds = AUDIO_STR;
      }else{
        throw new IllegalStateException();
      }

    }catch(IOException e){
      throw new IOException("Error location [Rec_Eval_Record:constructor1(IOException)]");
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Rec_Eval_Record:constructor1(IllegalArgumentException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Rec_Eval_Record:constructor1(IllegalStateException)]");
    }

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Rec_Eval_Record(Rec_Eval_Record_History hist) throws DataFormatException{

    this.serial_num = hist.getSerial_num();
    this.eval_id = hist.getEval_id();
    this.data_kinds = hist.getData_kinds();
    this.record_name = hist.getRecord_name();

    try{
      this.record_data = hist.getBlobDecompress();  //データは解凍済みの物を受け取る。
    }catch(DataFormatException e){
      throw new DataFormatException("Error location [Rec_Eval_Record:constructor2]");
    }

    stringSetNull();
  }






  /*************************************************************************************/
  /*   [コンストラクタ3]
  /*   ZIPファイルから展開済みのファイルデータと、保存先の採用考課番号を受け取り、
  /*   新規のエンティティを作成する。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Rec_Eval_Record(String filename, byte[] data, String eval_id){

    this.serial_num = null;
    this.eval_id = eval_id;

    /**********************************************************/
    /*   ファイルの拡張子とMIMEタイプを判別。
    /**********************************************************/
    int idx = filename.lastIndexOf(".");
    String ext = idx != -1 ? filename.substring(idx + 1) : "";

    Tika tika = new Tika();
    String mime = tika.detect(data);

    try{
      if(mime.equals(DatatypeEnum.PHOTO.getType()) && ext.equals(DatatypeEnum.PHOTO.getExt())){
        this.data_kinds = PHOTO_STR;
      }else if(mime.equals(DatatypeEnum.AUDIO.getType()) && ext.equals(DatatypeEnum.AUDIO.getExt())){
        this.data_kinds = AUDIO_STR;
      }else if(mime.equals(DatatypeEnum.MOVIE.getType()) && ext.equals(DatatypeEnum.MOVIE.getExt())){
        this.data_kinds = MOVIE_STR;
      }else{
        throw new IllegalStateException();
      }
      
      this.record_data = data;

    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Rec_Eval_Record:constructor3]");
    }


    /********************************************************************/
    /*   ファイル名は、ファイルパス構成文字を削除した上で、
    /*   サニタイズしたうえで使用する。
    /*   ファイル名は、拡張子を除いた名前となる。
    /********************************************************************/
    filename = filename.replace(ext, "");
    filename = filename.replace("/", "");
    filename = filename.replace(".", "");
    filename = filename.replace("\\", "");

    this.record_name = HtmlUtils.htmlEscape(filename);

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
      if(this.eval_id == null || this.eval_id.isEmpty() || this.eval_id.isBlank()){
        this.eval_id = null;
      }

      if(this.data_kinds == null || this.data_kinds.isEmpty() || this.data_kinds.isBlank()){
        this.data_kinds = null;
      }

      if(this.record_name == null || this.record_name.isEmpty() || this.record_name.isBlank()){
        this.record_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Record:stringSetNull]");
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
    DatatypeEnum type;

    stringSetNull();

    try{
      switch(this.data_kinds){
        case PHOTO_STR:
          type = DatatypeEnum.PHOTO;
          break;
        case MOVIE_STR:
          type = DatatypeEnum.MOVIE;
          break;
        case AUDIO_STR:
          type = DatatypeEnum.AUDIO;
          break;
        default:
          throw new IllegalStateException();
      } 

      map.put(Rec_Eval_Record_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Rec_Eval_Record_Enum.Eval_Id.getKey(), 
              this.eval_id == null ? "" : this.eval_id);
              
      map.put(Rec_Eval_Record_Enum.Data_Kinds.getKey(), 
              this.data_kinds == null ? "" : this.data_kinds);
              
      map.put(Rec_Eval_Record_Enum.Record_Name.getKey(), 
              this.record_name == null ? "" : this.record_name);
              
      map.put(Rec_Eval_Record_Enum.Record_Data.getKey(), 
              this.record_data == null ? "" : base64encode(this.record_data, type));
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Record:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Rec_Eval_Record:makeMap(IllegalStateException)]");
    }

    return map;
  }
}