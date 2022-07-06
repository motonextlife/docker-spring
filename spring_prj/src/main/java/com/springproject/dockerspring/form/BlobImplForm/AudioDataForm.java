package com.springproject.dockerspring.form.BlobImplForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.tika.Tika;
import org.hibernate.validator.constraints.Length;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.form.BlobValidation;
import com.springproject.dockerspring.form.OriginalAnno.DataSizeLimit;
import com.springproject.dockerspring.form.OriginalAnno.ForeignKey;
import com.springproject.dockerspring.form.OriginalAnno.NotImage;
import com.springproject.dockerspring.form.OriginalAnno.NotMovie;
import com.springproject.dockerspring.form.OriginalAnno.NotPdf;

import lombok.Data;





/********************************************************/
/*   [AudioDataForm]
/*   「参考音源データ」に関するフォーム入力値の
/*    入力値のチェックを行う。
/********************************************************/

@Data
public class AudioDataForm implements Serializable, BlobValidation{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /*****************************************************************/
  /* {関連音源番号}
  /* 空白や空文字は許されない。
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Sound_Source」内に存在する音源番号のみ設定可能。
  /*****************************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  @ForeignKey(table="Sound_Source")
  private String sound_id;


  /************************************************************/
  /* {保存音声データ1~5}
  /* 最低一つは必須である。
  /* 特定の拡張子のファイルかつ、指定されたデータ量以下のみ
  /* 受付可能。（※詳細は、アノテーションファイルを参照）
  /************************************************************/
  @NotNull
  @NotImage
  @NotPdf
  @NotMovie
  @DataSizeLimit
  private MultipartFile audio_data_1;


  /******************************************/
  /* 以下2～5まで同様のルールとする。
  /******************************************/ 
  @NotImage
  @NotPdf
  @NotMovie
  @DataSizeLimit
  private MultipartFile audio_data_2;

  @NotImage
  @NotPdf
  @NotMovie
  @DataSizeLimit
  private MultipartFile audio_data_3;

  @NotImage
  @NotPdf
  @NotMovie
  @DataSizeLimit
  private MultipartFile audio_data_4;

  @NotImage
  @NotPdf
  @NotMovie
  @DataSizeLimit
  private MultipartFile audio_data_5;





  /***************************************************************/
  /*   [blobChecker]
  /*   受け取ったデータの、拡張子やMIMEタイプ、データサイズを
  /*   検証する。
  /***************************************************************/
  @Override
  public Boolean blobChecker(Map<String, byte[]> inputblob) throws InterruptedException, ExecutionException {
    
    List<CompletableFuture<Boolean>> exec = new ArrayList<>();
    List<Boolean> result = new ArrayList<>();

    inputblob.forEach((k, v) -> exec.add(parallelCheck(k, v)));

    CompletableFuture.allOf(exec.toArray(new CompletableFuture[exec.size()])).join();

    try{
      for(CompletableFuture<Boolean> comp: exec){
        result.add(comp.get());
      }
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [AudioDataForm:blobChecker(InterruptedException)]");
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [AudioDataForm:blobChecker(ExecutionException)]", e.getCause());
    }

    //全てのファイルチェックがOKだった場合のみ、合格とする。
    return result.stream().allMatch(s -> s == true);
  }






  /***************************************************************/
  /*   [parallelCheck]
  /*   実際のチェック処理を並列に実行するための
  /*   メソッドである。
  /***************************************************************/
  @Override
  @Async("Form")  //スレッド設定は「DockerspringApplication」を参照。
  public CompletableFuture<Boolean> parallelCheck(String filename, byte[] data) {
    
    List<Boolean> result = new ArrayList<>();

    try{
      /****************************************************************************/
      /* 渡されたバイト配列を分析し、MIMEタイプが所定の物か判別。
      /****************************************************************************/
      Tika tika = new Tika();
      String mime = tika.detect(data);

      if(mime.equals(DatatypeEnum.AUDIO.getType())){
        result.add(true);
      }else{
        result.add(false);
      }


      /****************************************************************************/
      /* 渡されたファイル名を分析し、拡張子が所定の物か確認。
      /****************************************************************************/
      int idx = filename.lastIndexOf(".");
      String ext = idx != -1 ? filename.substring(idx + 1) : "";

      if(ext.equals(DatatypeEnum.AUDIO.getExt())){
        result.add(true);
      }else{
        result.add(false);
      }


      /****************************************************************************/
      /* 渡されたファイル名の中に、ファイルパス構成文字が含まれていないか確認。
      /* ※拡張子の「.」はあらかじめ取り除く。
      /****************************************************************************/
      filename = filename.replace("." + ext, "");

      if(filename.contains("/") || filename.contains(".") || filename.contains("\\")){
        result.add(false);
      }else{
        result.add(true);
      }
    }catch(NullPointerException e){
      throw new NullPointerException("Error location [AudioDataForm:blobChecker(NullPointerException)]");
    }


    //全てのチェックがOKだった場合のみ、「true」を返す。
    return CompletableFuture.completedFuture(result.stream().allMatch(s -> s == true));
  }
}