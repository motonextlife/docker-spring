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
import com.springproject.dockerspring.entity.NormalEntity.Audio_Data;





/**************************************************************/
/*   [Audio_Data_Zip]
/*   テーブル「Audio_Data」へのZIPデータの入出力を行う。
/**************************************************************/

@Component
public class Audio_Data_Zip extends Common_Zip implements InOutZip<Audio_Data>{

  private final int READ_COUNT = 10;  //ZIPファイル内のファイルは、読み取り数を「10個」までとする。
  private final int WRITE_COUNT = 10;  //ZIPファイル出力は、書き込み数を「10個」までとする。


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
      throw new IOException("Error location [Audio_Data_Zip:extractZip]");
    }catch(IOException e){
      throw new ZipException("Error location [Audio_Data_Zip:extractZip]");
    }
  }






  /*************************************************************/
  /*   [zipToEntitys]
  /*   正常なデータが入っているマップリストのデータを、
  /*   対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Audio_Data> zipToEntitys(Map<String, byte[]> map, String id) {

    List<Audio_Data> entitys = new ArrayList<>();

    try{
      map.forEach((k, v) -> entitys.add(new Audio_Data(k, v, id)));

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Audio_Data_Zip:zipToEntitys(NullPointerException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Audio_Data_Zip:zipToEntitys(ClassCastException)]");
    }

    return entitys;
  }






  /*************************************************************/
  /*   [outputZip]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputZip(Iterable<Audio_Data> entitys) throws ZipException, IOException, InterruptedException {

    try{
      List<Audio_Data> list = (ArrayList<Audio_Data>)entitys;
      Map<String[], byte[]> map = new HashMap<>();
      
      //データベースからのファイル数は「10個」までしか受け付けない。
      int listsize = list.size();
      if(listsize > WRITE_COUNT){
        list.subList(WRITE_COUNT, listsize - 1).clear();
      }

      list.stream()
          .forEach(s -> map.put(new String[]{s.getSound_name(), DatatypeEnum.AUDIO.getExt()}, s.getAudio_data()));

      return outputZipCom(FilePathEnum.Audio_Data.getPath(), map);

    }catch(ZipException e){
      throw new ZipException("Error location [Audio_Data_Zip:outputZip(ZipException)]");
    }catch(IOException e){
      throw new IOException("Error location [Audio_Data_Zip:outputZip(IOException)]");
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Audio_Data_Zip:outputZip(InterruptedException)]");
    }catch(RuntimeException e){
      throw new RuntimeException("Error location [Audio_Data_Zip:outputZip(RuntimeException)]");
    }
  }
}
