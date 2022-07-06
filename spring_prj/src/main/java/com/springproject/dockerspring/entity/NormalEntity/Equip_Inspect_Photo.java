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

import com.springproject.dockerspring.commonenum.Equip_Inspect_Photo_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Equip_Inspect_Photo_History;
import com.springproject.dockerspring.form.BlobImplForm.EquipInspPhotoForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Equip_Inspect_Photo]
/*   テーブル「Equip_Inspect_Photo」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Equip_Inspect_Photo")
public class Equip_Inspect_Photo implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/
  @Id
  private Integer serial_num;
  
  private String inspect_id;
  private String photo_name;
  private byte[] photo_data;







  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/
  
  public Equip_Inspect_Photo(EquipInspPhotoForm form, int num) throws IOException{

    String datatype;
    String name;
    this.serial_num = form.getSerial_num();
    this.inspect_id = form.getInspect_id();

    try{

      /********************************************************************/
      /*   受け取ったデータのうち、何番目のデータをエンティティに格納するか
      /*   判別し、対応するデータ名とデータを格納。
      /********************************************************************/
      switch(num){
        case 1:
          datatype = form.getPhoto_data_1().getContentType();
          name = form.getPhoto_data_1().getOriginalFilename();
          this.photo_data = form.getPhoto_data_1().getBytes();
          break;
        case 2:
          datatype = form.getPhoto_data_2().getContentType();
          name = form.getPhoto_data_2().getOriginalFilename();
          this.photo_data = form.getPhoto_data_2().getBytes();
          break;
        case 3:
          datatype = form.getPhoto_data_3().getContentType();
          name = form.getPhoto_data_3().getOriginalFilename();
          this.photo_data = form.getPhoto_data_3().getBytes();
          break;
        case 4:
          datatype = form.getPhoto_data_4().getContentType();
          name = form.getPhoto_data_4().getOriginalFilename();
          this.photo_data = form.getPhoto_data_4().getBytes();
          break;
        case 5:
          datatype = form.getPhoto_data_5().getContentType();
          name = form.getPhoto_data_5().getOriginalFilename();
          this.photo_data = form.getPhoto_data_5().getBytes();
          break;
        case 6:
          datatype = form.getPhoto_data_6().getContentType();
          name = form.getPhoto_data_6().getOriginalFilename();
          this.photo_data = form.getPhoto_data_6().getBytes();
          break;
        case 7:
          datatype = form.getPhoto_data_7().getContentType();
          name = form.getPhoto_data_7().getOriginalFilename();
          this.photo_data = form.getPhoto_data_7().getBytes();
          break;
        case 8:
          datatype = form.getPhoto_data_8().getContentType();
          name = form.getPhoto_data_8().getOriginalFilename();
          this.photo_data = form.getPhoto_data_8().getBytes();
          break;
        case 9:
          datatype = form.getPhoto_data_9().getContentType();
          name = form.getPhoto_data_9().getOriginalFilename();
          this.photo_data = form.getPhoto_data_9().getBytes();
          break;
        case 10:
          datatype = form.getPhoto_data_10().getContentType();
          name = form.getPhoto_data_10().getOriginalFilename();
          this.photo_data = form.getPhoto_data_10().getBytes();
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

      this.photo_name = HtmlUtils.htmlEscape(name);

      
      /********************************************************************/
      /*   データのコンテンツタイプがPNGか判別。
      /********************************************************************/
      if(!datatype.equals(DatatypeEnum.PHOTO.getType())){
        throw new IllegalStateException();
      }

    }catch(IOException e){
      throw new IOException("Error location [Equip_Inspect_Photo:constructor1(IOException)]");
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Equip_Inspect_Photo:constructor1(IllegalArgumentException)]");
    }

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Equip_Inspect_Photo(Equip_Inspect_Photo_History hist) throws DataFormatException{

    this.serial_num = hist.getSerial_num();
    this.inspect_id = hist.getInspect_id();
    this.photo_name = hist.getPhoto_name();

    try{
      this.photo_data = hist.getBlobDecompress();  //データは解凍済みの物を受け取る。
    }catch(DataFormatException e){
      throw new DataFormatException("Error location [Equip_Inspect_Photo:constructor2]");
    }

    stringSetNull();
  }





  /*************************************************************************************/
  /*   [コンストラクタ3]
  /*   ZIPファイルから展開済みのファイルデータと、保存先の点検番号を受け取り、
  /*   新規のエンティティを作成する。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Equip_Inspect_Photo(String filename, byte[] data, String inspect_id){

    this.serial_num = null;
    this.inspect_id = inspect_id;


    /**********************************************************/
    /*   ファイルの拡張子とMIMEタイプを判別。
    /**********************************************************/
    int idx = filename.lastIndexOf(".");
    String ext = idx != -1 ? filename.substring(idx + 1) : "";

    Tika tika = new Tika();
    String mime = tika.detect(data);

    try{
      if(!mime.equals(DatatypeEnum.PHOTO.getType()) || !ext.equals(DatatypeEnum.PHOTO.getExt())){
        throw new IllegalStateException();
      }
      
      this.photo_data = data;

    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Equip_Inspect_Photo:constructor3]");
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

    this.photo_name = HtmlUtils.htmlEscape(filename);

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
      if(this.inspect_id == null || this.inspect_id.isEmpty() || this.inspect_id.isBlank()){
        this.inspect_id = null;
      }

      if(this.photo_name == null || this.photo_name.isEmpty() || this.photo_name.isBlank()){
        this.photo_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_Photo:stringSetNull]");
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
      map.put(Equip_Inspect_Photo_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Equip_Inspect_Photo_Enum.Inspect_Id.getKey(), 
              this.inspect_id == null ? "" : this.inspect_id);
              
      map.put(Equip_Inspect_Photo_Enum.Photo_Name.getKey(), 
              this.photo_name == null ? "" : this.photo_name);
              
      map.put(Equip_Inspect_Photo_Enum.Photo_Data.getKey(), 
              this.photo_data == null ? "" : base64encode(this.photo_data, DatatypeEnum.PHOTO));
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_Photo:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Equip_Inspect_Photo:makeMap(IllegalStateException)]");
    }

    return map;
  }
}