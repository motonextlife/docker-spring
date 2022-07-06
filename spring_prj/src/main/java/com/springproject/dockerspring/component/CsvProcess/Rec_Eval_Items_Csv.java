package com.springproject.dockerspring.component.CsvProcess;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.commonenum.Rec_Eval_Items_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Rec_Eval_Items;





/**************************************************************/
/*   [Rec_Eval_Items_Csv]
/*   テーブル「Rec_Eval_Items」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Rec_Eval_Items_Csv extends Common_Csv implements InOutCsv<Rec_Eval_Items>{


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」は除外する。
  /*************************************************************/
  private final Rec_Eval_Items_Enum[] enumlist = {
    Rec_Eval_Items_Enum.Evalsheet_Id,
    Rec_Eval_Items_Enum.Evalsheet_Name,
    Rec_Eval_Items_Enum.Evalsheet_Date,
    Rec_Eval_Items_Enum.Evalsheet_Kinds,
    Rec_Eval_Items_Enum.Eval_Item_Contents_1,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_1,
    Rec_Eval_Items_Enum.Eval_Item_Contents_2,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_2,
    Rec_Eval_Items_Enum.Eval_Item_Contents_3,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_3,
    Rec_Eval_Items_Enum.Eval_Item_Contents_4,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_4,
    Rec_Eval_Items_Enum.Eval_Item_Contents_5,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_5,
    Rec_Eval_Items_Enum.Eval_Item_Contents_6,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_6,
    Rec_Eval_Items_Enum.Eval_Item_Contents_7,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_7,
    Rec_Eval_Items_Enum.Eval_Item_Contents_8,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_8,
    Rec_Eval_Items_Enum.Eval_Item_Contents_9,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_9,
    Rec_Eval_Items_Enum.Eval_Item_Contents_10,
    Rec_Eval_Items_Enum.Eval_Item_Kinds_10
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
      throw new IOException("Error location [Rec_Eval_Items_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Rec_Eval_Items> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Rec_Eval_Items> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Rec_Eval_Items(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Rec_Eval_Items_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Rec_Eval_Items_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Rec_Eval_Items_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Rec_Eval_Items> entitys) throws IOException {

    try{
      List<Rec_Eval_Items> tmp = (ArrayList<Rec_Eval_Items>)entitys;
      
      List<Map<String, String>> list = tmp.stream()
                                          .map(s -> s.makeMap())
                                          .collect(Collectors.toList());

      String[] enumkeylist = Arrays.stream(this.enumlist)
                                   .map(s -> s.getKey())
                                   .toArray(String[]::new);

      String[] enumtextlist = Arrays.stream(this.enumlist)
                                   .map(s -> s.getText())
                                   .toArray(String[]::new);

      Predicate<String> func1 = str -> {
        return str.equals(Rec_Eval_Items_Enum.Evalsheet_Kinds.getKey());
      };

      UnaryOperator<String> func2 = str -> {
        switch(str){
          case "recruit":
            return "採用";
          case "eval":
            return "考課";
          case "audition":
            return "実技";
          default:
            return "分類無し";
          }
      };
      
      return outputCsvCom(list, enumkeylist, enumtextlist, func1, func2);

    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Rec_Eval_Items_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Rec_Eval_Items_Csv:outputCsv(IOException)]");
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
      throw new IOException("Error location [Rec_Eval_Items_Csv:outputTemplate]");
    }
  }
}
