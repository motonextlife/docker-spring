package com.springproject.dockerspring.component.CsvProcess;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.commonenum.Sound_Source_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Sound_Source;





/**************************************************************/
/*   [Sound_Source_Csv]
/*   テーブル「Sound_Source」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Sound_Source_Csv extends Common_Csv implements InOutCsv<Sound_Source>{


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」は除外する。
  /*************************************************************/
  private final Sound_Source_Enum[] enumlist = {
    Sound_Source_Enum.Sound_Id,
    Sound_Source_Enum.Upload_Date,
    Sound_Source_Enum.Song_Title,
    Sound_Source_Enum.Composer,
    Sound_Source_Enum.Performer,
    Sound_Source_Enum.Publisher,
    Sound_Source_Enum.Other_Comment
  };



  /*************************************************************/
  /*   [extractCsv]
  /*   読み込んだCSVファイルからデータを抽出し、マップリストに
  /*   文字列形式で格納する。
  /*************************************************************/
  @Override
  public List<Map<String, String>> extractCsv(MultipartFile file) throws IOException, InputFileDifferException {

    try{
      String[] enumkeylist = Arrays.stream(this.enumlist)
                                   .map(s -> s.getKey())
                                   .toArray(String[]::new);

      //読み取り行数は100行までとする。
      return extractCsvCom(file, 100, enumkeylist);

    }catch(IOException e){
      throw new IOException("Error location [Sound_Source_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Sound_Source> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Sound_Source> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Sound_Source(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Sound_Source_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Sound_Source_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Sound_Source_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Sound_Source> entitys) throws IOException {

    try{
      List<Sound_Source> tmp = (ArrayList<Sound_Source>)entitys;
      
      List<Map<String, String>> list = tmp.stream()
                                          .map(s -> s.makeMap())
                                          .collect(Collectors.toList());

      String[] enumkeylist = Arrays.stream(this.enumlist)
                                   .map(s -> s.getKey())
                                   .toArray(String[]::new);

      String[] enumtextlist = Arrays.stream(this.enumlist)
                                   .map(s -> s.getText())
                                   .toArray(String[]::new);
      
      return outputCsvCom(list, enumkeylist, enumtextlist, null, null);

    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Sound_Source_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Sound_Source_Csv:outputCsv(IOException)]");
    }
  }





  /*************************************************************/
  /*   [outputTemplate]
  /*   入力に使用するテンプレートCSVファイルの出力を行う。
  /*************************************************************/
  @Override
  public String outputTemplate() throws IOException {

    try{
      String[] enumtextlist = Arrays.stream(this.enumlist)
                                    .map(s -> s.getText())
                                    .toArray(String[]::new);

      //注意書きは100行とばした行に記載する。
      return outputTemplateCom(enumtextlist, 100);

    }catch(IOException e){
      throw new IOException("Error location [Sound_Source_Csv:outputTemplate]");
    }
  }
}
