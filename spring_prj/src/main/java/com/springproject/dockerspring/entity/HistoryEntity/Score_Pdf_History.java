package com.springproject.dockerspring.entity.HistoryEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.History_Common;
import com.springproject.dockerspring.commonenum.Score_Pdf_Enum;
import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.entity.CompressBlob;
import com.springproject.dockerspring.entity.EntitySetUp;
import com.springproject.dockerspring.entity.HistoryKindEnum;
import com.springproject.dockerspring.entity.NormalEntity.Score_Pdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;






/**************************************************************/
/*   [Score_Pdf_History]
/*   テーブル「Score_Pdf_History」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/

@AllArgsConstructor
@Data
@NoArgsConstructor
@Table("Score_Pdf_History")
public class Score_Pdf_History implements Serializable, EntitySetUp, CompressBlob{


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
  private String score_name;
  private byte[] pdf_data;
  private Integer before_length;








  /******************************************************************************************/
  /*   [コンストラクタ]
  /*   データが格納済みの対応する履歴用エンティティを受け取り、エンティティ内にデータを詰め替える。
  /*   その際に、空白や空文字しか入っていない文字列型データは「Null」に変換する。
  /******************************************************************************************/
  
  public Score_Pdf_History(Score_Pdf info, String kind, String username){

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
      throw new NullPointerException("Error location [Score_Pdf_History:constructor]");
    }

    this.operation_user = username;
    this.serial_num = info.getSerial_num();
    this.score_id = info.getScore_id();
    this.score_name = info.getScore_name();

    this.before_length = info.getPdf_data().length;   //圧縮前に、元のデータの長さを記録。
    this.pdf_data = compress(info.getPdf_data());   //圧縮して保存

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

      if(this.score_name == null || this.score_name.isEmpty() || this.score_name.isBlank()){
        this.score_name = null;
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Score_Pdf_History:stringSetNull]");
    }
  }






  /**
   * @throws DataFormatException*************************************************************/
  /*   [makeMap]
  /*   エンティティ内のデータを、ビューへの出力用に文字列に変換し
  /*   マップリストへ格納して返却する。
  /***************************************************************/

  @Override
  public Map<String, String> makeMap() throws DataFormatException {
    Map<String, String> map = new HashMap<>();
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
              
      map.put(Score_Pdf_Enum.Serial_Num.getKey(), 
              this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
      map.put(Score_Pdf_Enum.Score_Id.getKey(), 
              this.score_id == null ? "" : this.score_id);
              
      map.put(Score_Pdf_Enum.Score_Name.getKey(), 
              this.score_name == null ? "" : this.score_name);
              
      byte[] decompress = decompress(this.pdf_data, this.before_length);  //出力の際には解凍して出力する。
      map.put(Score_Pdf_Enum.Pdf_Data.getKey(), 
              this.pdf_data == null ? "" : base64encode(decompress, DatatypeEnum.PDF));

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Score_Pdf_History:makeMap(NullPointerException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Score_Pdf_History:makeMap(IllegalStateException)]");
    }catch(DataFormatException e){
      throw new DataFormatException("Error location [Score_Pdf_History:makeMap(DataFormatException)]");
    }

    return map;
  }





  /***************************************************************/
  /*   [getBlobDecompress]
  /*   取得したバイト配列を解凍した物を、呼び出し元に返す。
  /***************************************************************/
  @Override
  public byte[] getBlobDecompress() throws DataFormatException {
    return decompress(this.pdf_data, this.before_length);
  }
}