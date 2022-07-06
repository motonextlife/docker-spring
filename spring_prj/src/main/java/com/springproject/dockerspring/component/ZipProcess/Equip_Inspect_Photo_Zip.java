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
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect_Photo;





/**************************************************************/
/*   [Equip_Inspect_Photo_Zip]
/*   テーブル「Equip_Inspect_Photo」へのZIPデータの入出力を行う。
/**************************************************************/

@Component
public class Equip_Inspect_Photo_Zip extends Common_Zip implements InOutZip<Equip_Inspect_Photo>{

  private final int READ_COUNT = 50;  //ZIPファイル内のファイルは、読み取り数を「50個」までとする。
  private final int WRITE_COUNT = 50;  //ZIPファイル出力は、書き込み数を「50個」までとする。


  /*************************************************************/
  /*   [extractZip]
  /*   読み込んだZIPファイルからデータを抽出し、マップリストに
  /*   ファイル名とファイルデータを保存する。
  /*************************************************************/
  @Override
  public Map<String, byte[]> extractZip(MultipartFile zipfile) throws IOException, ZipException, InputFileDifferException {

    try{
      return extractZipCom(zipfile, DatatypeEnum.PHOTO.getLimit(), READ_COUNT);
    }catch(ZipException e){
      throw new IOException("Error location [Equip_Inspect_Photo_Zip:extractZip]");
    }catch(IOException e){
      throw new ZipException("Error location [Equip_Inspect_Photo_Zip:extractZip]");
    }
  }






  /*************************************************************/
  /*   [zipToEntitys]
  /*   正常なデータが入っているマップリストのデータを、
  /*   対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect_Photo> zipToEntitys(Map<String, byte[]> map, String id) {

    List<Equip_Inspect_Photo> entitys = new ArrayList<>();

    try{
      map.forEach((k, v) -> entitys.add(new Equip_Inspect_Photo(k, v, id)));

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_Photo_Zip:zipToEntitys(NullPointerException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Equip_Inspect_Photo_Zip:zipToEntitys(ClassCastException)]");
    }

    return entitys;
  }






  /*************************************************************/
  /*   [outputZip]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputZip(Iterable<Equip_Inspect_Photo> entitys) throws ZipException, IOException, InterruptedException {
    
    try{
      List<Equip_Inspect_Photo> list = (ArrayList<Equip_Inspect_Photo>)entitys;
      Map<String[], byte[]> map = new HashMap<>();
      
      //データベースからのファイル数は「10個」までしか受け付けない。
      int listsize = list.size();
      if(listsize > WRITE_COUNT){
        list.subList(WRITE_COUNT, listsize - 1).clear();
      }

      list.stream()
          .forEach(s -> map.put(new String[]{s.getPhoto_name(), DatatypeEnum.AUDIO.getExt()}, s.getPhoto_data()));

      return outputZipCom(FilePathEnum.Equip_Inspect_Photo.getPath(), map);

    }catch(ZipException e){
      throw new ZipException("Error location [Equip_Inspect_Photo_Zip:outputZip(ZipException)]");
    }catch(IOException e){
      throw new IOException("Error location [Equip_Inspect_Photo_Zip:outputZip(IOException)]");
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Equip_Inspect_Photo_Zip:outputZip(InterruptedException)]");
    }catch(RuntimeException e){
      throw new RuntimeException("Error location [Equip_Inspect_Photo_Zip:outputZip(RuntimeException)]");
    }
  }
}
