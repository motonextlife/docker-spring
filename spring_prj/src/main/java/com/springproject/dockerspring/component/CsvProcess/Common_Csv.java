package com.springproject.dockerspring.component.CsvProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;





/**************************************************************/
/*   [Common_Csv]
/*   全てのCSV処理の共通部分を行う。
/**************************************************************/
public abstract class Common_Csv{


  /*************************************************************/
  /*   [extractCsvCom]
  /*   読み込んだCSVファイルからデータを抽出し、マップリストに
  /*   文字列形式で格納する。
  /*************************************************************/
   public List<Map<String, String>> extractCsvCom(MultipartFile file, int readcol, String[] enumkeylist) 
      throws IOException, InputFileDifferException {

    List<Map<String, String>> list = new ArrayList<>();

    try(InputStream is = file.getInputStream();
      InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);  //UTF_8で統一。
      BufferedReader br = new BufferedReader(isr);){


      /*************************************************************/
      /*   入力されたファイルが、そもそもCSVファイルでない、または
      /*   データサイズが超過していたら。処理を中断する。
      /*************************************************************/
      if(!file.getContentType().equals(DatatypeEnum.CSV.getType())
          || file.getSize() > DatatypeEnum.CSV.getLimit()){
        throw new InputFileDifferException();
      }

      
      /*************************************************************/
      /*   読み込むCSVファイルの行数は、引数の指定行数までとする。
      /*   最初の題名行は必要ないのでスキップする。
      /*************************************************************/
      List<String> lines = br.lines().skip(1)
                                     .limit(readcol + 1)
                                     .collect(Collectors.toList());

      
      /********************************************************************************/
      /*   行を分割し、対応する列挙型項目名をキーとしながら、マップリストに格納。
      /*   なお、データが無い列に関しては、該当するマップキーに空文字を入れる。
      /*******************************************************************************/
      for(String line: lines){
        Map<String, String> map = new HashMap<>();
        List<String> split = Arrays.asList(line.split(","));

        if(split.size() < enumkeylist.length){
          IntStream.range(0, enumkeylist.length - split.size())
                   .forEach(s -> split.add(""));
        }

        IntStream.range(0, enumkeylist.length)
                 .forEach(s -> map.put(enumkeylist[s], split.get(s)));

        list.add(map);
      }

    }catch(IOException e){
      throw new IOException("Error location [Common_Csv:extractCsvCom]");
    }

    return list;
  }






  /*************************************************************/
  /*   [outputCsvCom]
  /*   データベースに保存してあるデータを、CSVファイルデータ
  /*   として出力する。
  /*************************************************************/
  public String outputCsvCom(List<Map<String, String>> list, 
                              String[] enumkeylist, 
                              String[] enumtextlist, 
                              Predicate<String> func1, 
                              UnaryOperator<String> func2) throws IOException {

    String result;

    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(byteout, StandardCharsets.UTF_8);  //UTF_8で統一。
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw);){
      
      //題名行を出力。
      for(int i = 0; i < enumkeylist.length; i++){
        pw.print(enumtextlist[i]);
        
        if(i != enumkeylist.length - 1){  //末尾のコンマは出力しない。
          pw.print(",");
        }
      }

      pw.println();

      //内容を全行出力。
      for(Map<String, String> map: list){

        for(int i = 0; i < enumkeylist.length; i++){

          if(func1 != null && func2 != null){  //呼び出し元から、指定のメソッドがあれば、そちらを実行。
            if(func1.test(enumkeylist[i])){
              pw.print(func2.apply(map.get(enumkeylist[i])));
            }else{
              pw.print(map.get(enumkeylist[i]));
            }
          }else{
            pw.print(map.get(enumkeylist[i]));
          }

          if(i != enumkeylist.length - 1){  //末尾のコンマは出力しない。
            pw.print(",");
          }
        }

        pw.println();
      }

      result = csvBase64Encode(byteout.toByteArray());

    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Common_Csv:outputCsvCom(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Common_Csv:outputCsvCom(IOException)]");
    }
    
    return result;
  }





  /*************************************************************/
  /*   [outputTemplateCom]
  /*   入力に使用するテンプレートCSVファイルの出力を行う。
  /*************************************************************/
  public String outputTemplateCom(String[] enumtextlist, int jumpcol) throws IOException {

    String result;

    try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(byteout, StandardCharsets.UTF_8);  //UTF_8で統一。
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw);){
      
      //題名行のみを出力。
      for(int i = 0; i < enumtextlist.length; i++){
        pw.print(enumtextlist[i]);

        if(i != enumtextlist.length - 1){  //末尾のコンマは出力しない。
          pw.print(",");
        }
      }

      IntStream.range(0, jumpcol).forEach(s -> pw.println());  //指定行とばした行に注意書きを記載。
      pw.print("この行以降のデータは読み込まれません。ご注意ください。");

      result = csvBase64Encode(byteout.toByteArray());

    }catch(IOException e){
      throw new IOException("Error location [Common_Csv:outputTemplateCom]");
    }
    
    return result;
  }





  /***************************************************************/
  /*   [csvBase64Encode]
  /*   CSVデータの、Base64へのエンコードを行う。
  /***************************************************************/
  private String csvBase64Encode(byte[] bytedata){

    if(bytedata.length == 0){
      return "";
    }else{
      return Base64.getEncoder().encodeToString(bytedata);
    }
  }
}
