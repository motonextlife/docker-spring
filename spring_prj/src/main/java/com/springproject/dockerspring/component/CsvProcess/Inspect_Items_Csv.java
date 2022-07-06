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

import com.springproject.dockerspring.commonenum.Inspect_Items_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Inspect_Items;





/**************************************************************/
/*   [Inspect_Items_Csv]
/*   テーブル「Inspect_Items」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Inspect_Items_Csv extends Common_Csv implements InOutCsv<Inspect_Items> {


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」は除外する。
  /*************************************************************/
  private final Inspect_Items_Enum[] enumlist = {
    Inspect_Items_Enum.Inspsheet_Id,
    Inspect_Items_Enum.Inspsheet_Name,
    Inspect_Items_Enum.Inspsheet_Date,
    Inspect_Items_Enum.Insp_Item_Contents_1,
    Inspect_Items_Enum.Insp_Item_Kinds_1,
    Inspect_Items_Enum.Insp_Item_Unit_1,
    Inspect_Items_Enum.Insp_Item_Contents_2,
    Inspect_Items_Enum.Insp_Item_Kinds_2,
    Inspect_Items_Enum.Insp_Item_Unit_2,
    Inspect_Items_Enum.Insp_Item_Contents_3,
    Inspect_Items_Enum.Insp_Item_Kinds_3,
    Inspect_Items_Enum.Insp_Item_Unit_3,
    Inspect_Items_Enum.Insp_Item_Contents_4,
    Inspect_Items_Enum.Insp_Item_Kinds_4,
    Inspect_Items_Enum.Insp_Item_Unit_4,
    Inspect_Items_Enum.Insp_Item_Contents_5,
    Inspect_Items_Enum.Insp_Item_Kinds_5,
    Inspect_Items_Enum.Insp_Item_Unit_5,
    Inspect_Items_Enum.Insp_Item_Contents_6,
    Inspect_Items_Enum.Insp_Item_Kinds_6,
    Inspect_Items_Enum.Insp_Item_Unit_6,
    Inspect_Items_Enum.Insp_Item_Contents_7,
    Inspect_Items_Enum.Insp_Item_Kinds_7,
    Inspect_Items_Enum.Insp_Item_Unit_7,
    Inspect_Items_Enum.Insp_Item_Contents_8,
    Inspect_Items_Enum.Insp_Item_Kinds_8,
    Inspect_Items_Enum.Insp_Item_Unit_8,
    Inspect_Items_Enum.Insp_Item_Contents_9,
    Inspect_Items_Enum.Insp_Item_Kinds_9,
    Inspect_Items_Enum.Insp_Item_Unit_9,
    Inspect_Items_Enum.Insp_Item_Contents_10,
    Inspect_Items_Enum.Insp_Item_Kinds_10,
    Inspect_Items_Enum.Insp_Item_Unit_10
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
      throw new IOException("Error location [Inspect_Items_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Inspect_Items> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Inspect_Items> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Inspect_Items(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Inspect_Items_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Inspect_Items_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Inspect_Items_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Inspect_Items> entitys) throws IOException {

    try{
      List<Inspect_Items> tmp = (ArrayList<Inspect_Items>)entitys;
      
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
      throw new ClassCastException("Error location [Inspect_Items_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Inspect_Items_Csv:outputCsv(IOException)]");
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
      throw new IOException("Error location [Inspect_Items_Csv:outputTemplate]");
    }
  }
}
