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

import com.springproject.dockerspring.commonenum.Score_Pdf_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.form.BlobImplForm.ScorePdfForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Score_Pdf]
/*   テーブル「Score_Pdf」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Score_Pdf")
public class Score_Pdf implements Serializable, EntitySetUp{


  /***********************************************/
  /* フィールド変数は全て、対象テーブルのカラム名に
  /* 準じている。それぞれの変数の説明は
  /* 対応するフォームクラスのコメントを参照の事。
  /* （全く同じ変数名で定義してある。）
  /***********************************************/
  @Id
  private Integer serial_num;
  
  private String score_id;
  private String score_name;
  private byte[] pdf_data;







  /*************************************************************************************/
  /*   [コンストラクタ1]
  /*   データが格納済みの対応するフォームクラスを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/
  
  public Score_Pdf(ScorePdfForm form, int num) throws IOException{

    String datatype;
    String name;
    this.serial_num = form.getSerial_num();
    this.score_id = form.getScore_id();

    try{

      /********************************************************************/
      /*   受け取ったデータのうち、何番目のデータをエンティティに格納するか
      /*   判別し、対応するデータ名とデータを格納。
      /********************************************************************/
      switch(num){
        case 1:
          datatype = form.getPdf_data_1().getContentType();
          name = form.getPdf_data_1().getOriginalFilename();
          this.pdf_data = form.getPdf_data_1().getBytes();
          break;
        case 2:
          datatype = form.getPdf_data_2().getContentType();
          name = form.getPdf_data_2().getOriginalFilename();
          this.pdf_data = form.getPdf_data_2().getBytes();
          break;
        case 3:
          datatype = form.getPdf_data_3().getContentType();
          name = form.getPdf_data_3().getOriginalFilename();
          this.pdf_data = form.getPdf_data_3().getBytes();
          break;
        case 4:
          datatype = form.getPdf_data_4().getContentType();
          name = form.getPdf_data_4().getOriginalFilename();
          this.pdf_data = form.getPdf_data_4().getBytes();
          break;
        case 5:
          datatype = form.getPdf_data_5().getContentType();
          name = form.getPdf_data_5().getOriginalFilename();
          this.pdf_data = form.getPdf_data_5().getBytes();
          break;
        default:
          throw new IllegalArgumentException();
      }

      //データ名は、アップロードファイルに付与されているものをサニタイズして用いる。
      this.score_name = HtmlUtils.htmlEscape(name);

      /********************************************************************/
      /*   データのコンテンツタイプがPDFか判別。
      /********************************************************************/
      if(!datatype.equals(DatatypeEnum.PDF.getType())){
        throw new IllegalStateException();
      }

    }catch(IOException e){
      throw new IOException("Error location [Score_Pdf:constructor3(IOException)]");
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Score_Pdf:constructor3(IllegalArgumentException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Score_Pdf:constructor3(IllegalStateException)]");
    }

    stringSetNull();
  }







  /******************************************************************************************/
  /*   [コンストラクタ2]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Score_Pdf(Score_Pdf_History hist) throws DataFormatException{

    this.serial_num = hist.getSerial_num();
    this.score_id = hist.getScore_id();
    this.score_name = hist.getScore_name();

    try{
      this.pdf_data = hist.getBlobDecompress();  //データは解凍済みの物を受け取る。
    }catch(DataFormatException e){
      throw new DataFormatException("Error location [Score_Pdf:constructor2]");
    }
    
    stringSetNull();
  }





  
  /*************************************************************************************/
  /*   [コンストラクタ3]
  /*   ZIPファイルから展開済みのファイルデータと、保存先の楽譜番号を受け取り、
  /*   新規のエンティティを作成する。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /*************************************************************************************/

  public Score_Pdf(String filename, byte[] data, String score_id){

    this.serial_num = null;
    this.score_id = score_id;

    
    /**********************************************************/
    /*   ファイルの拡張子とMIMEタイプを判別。
    /**********************************************************/
    int idx = filename.lastIndexOf(".");
    String ext = idx != -1 ? filename.substring(idx + 1) : "";

    Tika tika = new Tika();
    String mime = tika.detect(data);

    try{
      if(!mime.equals(DatatypeEnum.PDF.getType()) || !ext.equals(DatatypeEnum.PDF.getExt())){
        throw new IllegalStateException();
      }
      
      this.pdf_data = data;

    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Score_Pdf:constructor3]");
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

    this.score_name = HtmlUtils.htmlEscape(filename);

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

      if(this.score_name == null || this.score_name.isEmpty() || this.score_name.isBlank()){
        this.score_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Score_Pdf:stringSetNull]");
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
      map.put(Score_Pdf_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Score_Pdf_Enum.Score_Id.getKey(), 
              this.score_id == null ? "" : this.score_id);
              
      map.put(Score_Pdf_Enum.Score_Name.getKey(), 
              this.score_name == null ? "" : this.score_name);
              
      map.put(Score_Pdf_Enum.Pdf_Data.getKey(), 
              this.pdf_data == null ? "" : base64encode(this.pdf_data, DatatypeEnum.PDF));
              
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Score_Pdf:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Score_Pdf:makeMap(IllegalStateException)]");
    }

    return map;
  }
}