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

import com.springproject.dockerspring.commonenum.Audio_Data_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Audio_Data_History;
import com.springproject.dockerspring.form.BlobImplForm.AudioDataForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Audio_Data]
/*   テーブル「Audio_Data」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Audio_Data")
public class Audio_Data implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/
  @Id
  private Integer serial_num;
  
  private String sound_id;
  private String sound_name;
  private byte[] audio_data;







  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/
  
  public Audio_Data(AudioDataForm form, int num) throws IOException{

    String datatype;
    String name;
    this.serial_num = form.getSerial_num();
    this.sound_id = form.getSound_id();

    try{

      /********************************************************************/
      /*   受け取ったデータのうち、何番目のデータをエンティティに格納するか
      /*   判別し、対応するデータ名とデータを格納。
      /********************************************************************/
      switch(num){
        case 1:
          datatype = form.getAudio_data_1().getContentType();
          name = form.getAudio_data_1().getOriginalFilename();
          this.audio_data = form.getAudio_data_1().getBytes();
          break;
        case 2:
          datatype = form.getAudio_data_2().getContentType();
          name = form.getAudio_data_2().getOriginalFilename();
          this.audio_data = form.getAudio_data_2().getBytes();
          break;
        case 3:
          datatype = form.getAudio_data_3().getContentType();
          name = form.getAudio_data_3().getOriginalFilename();
          this.audio_data = form.getAudio_data_3().getBytes();
          break;
        case 4:
          datatype = form.getAudio_data_4().getContentType();
          name = form.getAudio_data_4().getOriginalFilename();
          this.audio_data = form.getAudio_data_4().getBytes();
          break;
        case 5:
          datatype = form.getAudio_data_5().getContentType();
          name = form.getAudio_data_5().getOriginalFilename();
          this.audio_data = form.getAudio_data_5().getBytes();
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

      this.sound_name = HtmlUtils.htmlEscape(name);

      
      /********************************************************************/
      /*   データのコンテンツタイプがMP3か判別。
      /********************************************************************/
      if(!datatype.equals(DatatypeEnum.AUDIO.getType())){
        throw new IllegalStateException();
      }

    }catch(IOException e){
      throw new IOException("Error location [Audio_Data:constructor1(IOException)]");
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Audio_Data:constructor1(IllegalArgumentException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Audio_Data:constructor1(IllegalStateException)]");
    }

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Audio_Data(Audio_Data_History hist) throws DataFormatException{

    this.serial_num = hist.getSerial_num();
    this.sound_id = hist.getSound_id();
    this.sound_name = hist.getSound_name();

    try{
      this.audio_data = hist.getBlobDecompress();  //データは解凍済みの物を受け取る。
    }catch(DataFormatException e){
      throw new DataFormatException("Error location [Audio_Data:constructor2]");
    }

    stringSetNull();
  }





  /*************************************************************************************/
  /*   [コンストラクタ3]
  /*   ZIPファイルから展開済みのファイルデータと、保存先の音源番号を受け取り、
  /*   新規のエンティティを作成する。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Audio_Data(String filename, byte[] data, String sound_id){

    this.serial_num = null;
    this.sound_id = sound_id;


    /**********************************************************/
    /*   ファイルの拡張子とMIMEタイプを判別。
    /**********************************************************/
    int idx = filename.lastIndexOf(".");
    String ext = idx != -1 ? filename.substring(idx + 1) : "";

    Tika tika = new Tika();
    String mime = tika.detect(data);

    try{
      if(!mime.equals(DatatypeEnum.AUDIO.getType()) || !ext.equals(DatatypeEnum.AUDIO.getExt())){
        throw new IllegalStateException();
      }
      
      this.audio_data = data;

    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Audio_Data:constructor3]");
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

    this.sound_name = HtmlUtils.htmlEscape(filename);

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

      if(this.sound_name == null || this.sound_name.isEmpty() || this.sound_name.isBlank()){
        this.sound_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Audio_Data:stringSetNull]");
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

    stringSetNull();

    try{
      map.put(Audio_Data_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Audio_Data_Enum.Sound_Id.getKey(), 
              this.sound_id == null ? "" : this.sound_id);
              
      map.put(Audio_Data_Enum.Sound_Name.getKey(), 
              this.sound_name == null ? "" : this.sound_name);
              
      map.put(Audio_Data_Enum.Audio_Data.getKey(), 
              this.audio_data == null ? "" : base64encode(this.audio_data, DatatypeEnum.AUDIO));
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Audio_Data:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Audio_Data:makeMap(IllegalStateException)]");
    }

    return map;
  }
}