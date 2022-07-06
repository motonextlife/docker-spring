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

import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.component.InOutCsv;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.entity.NormalEntity.Member_Info;





/**************************************************************/
/*   [Member_Info_Csv]
/*   テーブル「Member_Info」へのCSVデータの入出力を行う。
/**************************************************************/

@Component
public class Member_Info_Csv extends Common_Csv implements InOutCsv<Member_Info>{


  /*************************************************************/
  /*   この列挙型配列で、CSVの行の並び順を定義する。
  /*   なお、「シリアルナンバー」「顔写真」は除外する。
  /*************************************************************/
  private final Member_Info_Enum[] enumlist = {
    Member_Info_Enum.Member_Id,
    Member_Info_Enum.Name,
    Member_Info_Enum.Name_Pronu,
    Member_Info_Enum.Sex,
    Member_Info_Enum.Birthday,
    Member_Info_Enum.Join_Date,
    Member_Info_Enum.Ret_Date,
    Member_Info_Enum.Email_1,
    Member_Info_Enum.Email_2,
    Member_Info_Enum.Tel_1,
    Member_Info_Enum.Tel_2,
    Member_Info_Enum.Addr_Postcode,
    Member_Info_Enum.Addr,
    Member_Info_Enum.Position,
    Member_Info_Enum.Position_Arri_Date,
    Member_Info_Enum.Job,
    Member_Info_Enum.Assign_Dept,
    Member_Info_Enum.Assign_Date,
    Member_Info_Enum.Inst_Charge,
    Member_Info_Enum.Other_Comment
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
      throw new IOException("Error location [Member_Info_Csv:extractCsv]");
    }
  }






  /*************************************************************/
  /*   [csvToEntitys]
  /*   バリデーションが終わり、正常なデータが入っているマップリスト
  /*   のデータを、対象のエンティティに移し替える。
  /*************************************************************/
  @Override
  public Iterable<Member_Info> csvToEntitys(List<Map<String, String>> maplist) throws ParseException {
    
    List<Member_Info> entitys = new ArrayList<>();

    try{
      for(Map<String, String> map: maplist){
        entitys.add(new Member_Info(map));
      }

    }catch(NullPointerException e){
      throw new NullPointerException("Error location [Member_Info_Csv:csvToEntitys(NullPointerException)]");
    } catch (ParseException e) {
      throw new ParseException("Error location [Member_Info_Csv:csvToEntitys(ParseException)]", e.getErrorOffset());
    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Member_Info_Csv:csvToEntitys(ClassCastException)]");
    }

    return entitys;
  }





  /*************************************************************/
  /*   [outputCsv]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  @Override
  public String outputCsv(Iterable<Member_Info> entitys) throws IOException {

    try{
      List<Member_Info> tmp = (ArrayList<Member_Info>)entitys;

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
        return str.equals(Member_Info_Enum.Sex.getKey());
      };

      UnaryOperator<String> func2 = str -> {
        if(str.equals("male")){
          return "男";
        }else if(str.equals("female")){
          return "女";
        }else{
          return "分類無し";
        }
      };
      
      return outputCsvCom(list, enumkeylist, enumtextlist, func1, func2);

    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Member_Info_Csv:outputCsv(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Member_Info_Csv:outputCsv(IOException)]");
    }catch(IllegalArgumentException e){
      throw new IllegalArgumentException("Error location [Member_Info_Csv:outputCsv(IllegalArgumentException)]");
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
      throw new IOException("Error location [Member_Info_Csv:outputTemplate]");
    }
  }
}
