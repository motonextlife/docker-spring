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

import com.springproject.dockerspring.commonenum.Equip_Inspect_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Equip_Inspect;





/**************************************************************/
/*   [Equip_Inspect_Csv]
/*   テーブル「Equip_Inspect」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Equip_Inspect_Csv extends Common_Csv implements InOutCsv<Equip_Inspect>{


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」は除外する。
  /*************************************************************/
  private final Equip_Inspect_Enum[] enumlist = {
    Equip_Inspect_Enum.Inspect_Id,
    Equip_Inspect_Enum.Faci_Id,
    Equip_Inspect_Enum.Inspect_Name,
    Equip_Inspect_Enum.Inspect_Date,
    Equip_Inspect_Enum.Inspsheet_Id,
    Equip_Inspect_Enum.Insp_Contents_1,
    Equip_Inspect_Enum.Insp_Rank_1,
    Equip_Inspect_Enum.Insp_Contents_2,
    Equip_Inspect_Enum.Insp_Rank_2,
    Equip_Inspect_Enum.Insp_Contents_3,
    Equip_Inspect_Enum.Insp_Rank_3,
    Equip_Inspect_Enum.Insp_Contents_4,
    Equip_Inspect_Enum.Insp_Rank_4,
    Equip_Inspect_Enum.Insp_Contents_5,
    Equip_Inspect_Enum.Insp_Rank_5,
    Equip_Inspect_Enum.Insp_Contents_6,
    Equip_Inspect_Enum.Insp_Rank_6,
    Equip_Inspect_Enum.Insp_Contents_7,
    Equip_Inspect_Enum.Insp_Rank_7,
    Equip_Inspect_Enum.Insp_Contents_8,
    Equip_Inspect_Enum.Insp_Rank_8,
    Equip_Inspect_Enum.Insp_Contents_9,
    Equip_Inspect_Enum.Insp_Rank_9,
    Equip_Inspect_Enum.Insp_Contents_10,
    Equip_Inspect_Enum.Insp_Rank_10
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
      throw new IOException("Error location [Equip_Inspect_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Equip_Inspect> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Equip_Inspect> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Equip_Inspect(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Equip_Inspect_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Equip_Inspect_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Equip_Inspect_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Equip_Inspect> entitys) throws IOException {

    try{
      List<Equip_Inspect> tmp = (ArrayList<Equip_Inspect>)entitys;

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
      throw new ClassCastException("Error location [Equip_Inspect_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Equip_Inspect_Csv:outputCsv(IOException)]");
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
      throw new IOException("Error location [Equip_Inspect_Csv:outputTemplate]");
    }
  }
}
