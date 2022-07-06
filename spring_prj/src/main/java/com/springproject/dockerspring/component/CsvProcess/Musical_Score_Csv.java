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

import com.springproject.dockerspring.commonenum.Musical_Score_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Musical_Score;





/**************************************************************/
/*   [Musical_Score_Csv]
/*   テーブル「Musical_Score」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Musical_Score_Csv extends Common_Csv implements InOutCsv<Musical_Score>{


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」は除外する。
  /*************************************************************/
  private final Musical_Score_Enum[] enumlist = {
    Musical_Score_Enum.Score_Id,
    Musical_Score_Enum.Buy_Date,
    Musical_Score_Enum.Song_Title,
    Musical_Score_Enum.Composer,
    Musical_Score_Enum.Arranger,
    Musical_Score_Enum.Publisher,
    Musical_Score_Enum.Strage_Loc,
    Musical_Score_Enum.Disp_Date,
    Musical_Score_Enum.Other_Comment
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
      throw new IOException("Error location [Musical_Score_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Musical_Score> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Musical_Score> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Musical_Score(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Musical_Score_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Musical_Score_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Musical_Score_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Musical_Score> entitys) throws IOException {

    try{
      List<Musical_Score> tmp = (ArrayList<Musical_Score>)entitys;
      
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
      throw new ClassCastException("Error location [Musical_Score_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Musical_Score_Csv:outputCsv(IOException)]");
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
      throw new IOException("Error location [Musical_Score_Csv:outputTemplate]");
    }
  }
}
