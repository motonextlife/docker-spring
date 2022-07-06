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
import com.springproject.dockerspring.entity.NormalEntity.Score_Pdf;





/**************************************************************/
/*   [Score_Pdf_Zip]
/*   テーブル「Score_Pdf」へのZIPデータの入出力を行う。
/**************************************************************/

@Component
public class Score_Pdf_Zip extends Common_Zip implements InOutZip<Score_Pdf>{

  private final int READ_COUNT = 20;  //ZIPファイル内のファイルは、読み取り数を「20個」までとする。
  private final int WRITE_COUNT = 20;  //ZIPファイル出力は、書き込み数を「20個」までとする。


  /*************************************************************/
  /*   [extractZip]
  /*   読み込んだZIPファイルからデータを抽出し、マップリストに
  /*   ファイル名とファイルデータを保存する。
  /*************************************************************/
  @Override
  public Map<String, byte[]> extractZip(MultipartFile zipfile) throws IOException, ZipException, InputFileDifferException {

    try{
      return extractZipCom(zipfile, DatatypeEnum.PDF.getLimit(), READ_COUNT);
    }catch(ZipException e){
      throw new IOException("Error location [Score_Pdf_Zip:extractZip]");
    }catch(IOException e){
      throw new ZipException("Error location [Score_Pdf_Zip:extractZip]");
    }
  }






  /*************************************************************/
  /*   [zipToEntitys]
  /*   正常なデータが入っているマップリストのデータを、
  /*   対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Score_Pdf> zipToEntitys(Map<String, byte[]> map, String id) {

    List<Score_Pdf> entitys = new ArrayList<>();

    try{
      map.forEach((k, v) -> entitys.add(new Score_Pdf(k, v, id)));

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Score_Pdf_Zip:zipToEntitys(NullPointerException)]");
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Score_Pdf_Zip:zipToEntitys(ClassCastException)]");
    }

    return entitys;
  }






  /*************************************************************/
  /*   [outputZip]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputZip(Iterable<Score_Pdf> entitys) throws ZipException, IOException, InterruptedException {
    
    try{
      List<Score_Pdf> list = (ArrayList<Score_Pdf>)entitys;
      Map<String[], byte[]> map = new HashMap<>();
      
      //データベースからのファイル数は「10個」までしか受け付けない。
      int listsize = list.size();
      if(listsize > WRITE_COUNT){
        list.subList(WRITE_COUNT, listsize - 1).clear();
      }

      list.stream()
          .forEach(s -> map.put(new String[]{s.getScore_name(), DatatypeEnum.PDF.getExt()}, s.getPdf_data()));

      return outputZipCom(FilePathEnum.Score_Pdf.getPath(), map);

    }catch(ZipException e){
      throw new ZipException("Error location [Score_Pdf_Zip:outputZip(ZipException)]");
    }catch(IOException e){
      throw new IOException("Error location [Score_Pdf_Zip:outputZip(IOException)]");
    }catch(InterruptedException e){
      throw new InterruptedException("Error location [Score_Pdf_Zip:outputZip(InterruptedException)]");
    }catch(RuntimeException e){
      throw new RuntimeException("Error location [Score_Pdf_Zip:outputZip(RuntimeException)]");
    }
  }
}
