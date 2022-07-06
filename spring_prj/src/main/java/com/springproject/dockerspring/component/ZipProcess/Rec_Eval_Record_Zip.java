package com.springproject.dockerspring.component.ZipProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lingala.zip4j.exception.ZipException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.component.FilePathEnum;
import com.springproject.dockerspring.component.InOutZip;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Record;





/**************************************************************/
/*   [Rec_Eval_Record_Zip]
/*   テーブル「Rec_Eval_Record」へのZIPデータの入出力を行う。
/**************************************************************/

@Component
public class Rec_Eval_Record_Zip extends Common_Zip implements InOutZip<Rec_Eval_Record>{

  private final int READ_COUNT = 10;  //ZIPファイル内のファイルは、読み取り数を「10個」までとする。
  private final int WRITE_COUNT = 10;  //ZIPファイル出力は、書き込み数を「10個」までとする。

  /***********************************************/
  /* 保存データの種別を入れるための定数である。
  /***********************************************/
  private final String PHOTO_STR = "photo";
  private final String MOVIE_STR = "movie";
  private final String AUDIO_STR = "audio";



  /*************************************************************/
  /*   [extractZip]
  /*   読み込んだZIPファイルからデータを抽出し、マップリストに
  /*   ファイル名とファイルデータを保存する。
  /*************************************************************/
  @Override
  public Map<String, byte[]> extractZip(MultipartFile zipfile) throws IOException, ZipException, InputFileDifferException {

    try{
      return extractZipCom(zipfile, DatatypeEnum.AUDIO.getLimit(), READ_COUNT);
    }catch(ZipException e){
      throw new IOException("Error location [Rec_Eval_Record_Zip:extractZip]");
    }catch(IOException e){
      throw new ZipException("Error location [Rec_Eval_Record_Zip:extractZip]");
    }
  }






  /*************************************************************/
  /*   [zipToEntitys]
  /*   正常なデータが入っているマップリストのデータを、
  /*   対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Record> zipToEntitys(Map<String, byte[]> map, String id) {

    List<Rec_Eval_Record> entitys = new ArrayList<>();

    try{
      map.forEach((k, v) -> entitys.add(new Rec_Eval_Record(k, v, id)));

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Record_Zip:zipToEntitys(NullPointerException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Rec_Eval_Record_Zip:zipToEntitys(ClassCastException)]");
    }

    return entitys;
  }






  /*************************************************************/
  /*   [outputZip]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputZip(Iterable<Rec_Eval_Record> entitys) throws ZipException, IOException, InterruptedException {
    
    try{
      List<Rec_Eval_Record> list = (ArrayList<Rec_Eval_Record>)entitys;
      Map<String[], byte[]> map = new HashMap<>();
      
      //データベースからのファイル数は「10個」までしか受け付けない。
      int listsize = list.size();
      if(listsize > WRITE_COUNT){
        list.subList(WRITE_COUNT, listsize - 1).clear();
      }

      for(Rec_Eval_Record ent: list){
        String kind = ent.getData_kinds();
        String ext;

        switch(kind){
          case PHOTO_STR:
            ext = DatatypeEnum.PHOTO.getExt();
            break;
          case MOVIE_STR:
            ext = DatatypeEnum.MOVIE.getExt();
            break;
          case AUDIO_STR:
            ext = DatatypeEnum.AUDIO.getExt();
            break;
          default:
            throw new IllegalStateException();
        } 

        map.put(new String[]{ent.getRecord_name(), ext}, ent.getRecord_data());
      }

      return outputZipCom(FilePathEnum.Equip_Inspect_Photo.getPath(), map);

    }catch(ZipException e){
      throw new ZipException("Error location [Audio_Data_Zip:outputZip(ZipException)]");
    }catch(IOException e){
      throw new IOException("Error location [Audio_Data_Zip:outputZip(IOException)]");
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Audio_Data_Zip:outputZip(InterruptedException)]");
    }catch(IllegalStateException e){
      throw new IllegalStateException("Error location [Rec_Eval_Record_Zip:outputZip(IllegalStateException)]");
    }catch(RuntimeException e){
      throw new RuntimeException("Error location [Audio_Data_Zip:outputZip(RuntimeException)]");
    }
  }
}
