package com.springproject.dockerspring.form.CsvImplForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.springproject.dockerspring.commonenum.Inspect_Items_Enum;
import com.springproject.dockerspring.form.CsvValidation;
import com.springproject.dockerspring.form.RegexEnum;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;

import lombok.Data;





/**************************************************************/
/*   [InspectItemsForm]
/*   「点検シート情報」に関するフォーム入力値やCSVファイルの
/*    入力値のチェックを行う。
/**************************************************************/

@Data
public class InspectItemsForm implements Serializable, CsvValidation{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /****************************************************/
  /* {点検シート番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String inspsheet_id;
  

  /**************************************************/
  /* {点検シート名}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String inspsheet_name;


  /**************************************************/
  /* {シート作成日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date inspsheet_date;


  /******************************************/
  /* {点検項目内容1}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「400文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_1;


  /******************************************/
  /* {点検方法1}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_1;


  /******************************************/
  /* {点検単位1}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_1;


  /******************************************/
  /* 以下2～10まで同様のルールとする。
  /******************************************/ 
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_2;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_2;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_2;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_3;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_3;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_3;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_4;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_4;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_4;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_5;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_5;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_5;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_6;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_6;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_6;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_7;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_7;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_7;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_8;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_8;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_8;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_9;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_9;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_9;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_contents_10;

  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_kinds_10;
  
  @Length(min=0, max=90)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_item_unit_10;





  /***************************************************************/
  /*   [csvChecker]
  /*   受け取ったデータ内の文字列全てをバリデーションチェックし、
  /*   エラーがあったデータは、該当データのCSV内での行数と列数を
  /*   エラーリストとして出力する。
  /***************************************************************/

  @Autowired
  private InspectItemsService inspitemsserv;

  @Override
  public List<List<Integer>> csvChecker(List<Map<String, String>> inputlist) throws ExecutionException, InterruptedException {

    List<List<Integer>> errorlist = new ArrayList<>();
    List<String> inspsheetlist = new ArrayList<>();
    List<Future<List<Integer>>> futures = new ArrayList<>();
    ExecutorService exec = Executors.newFixedThreadPool(10);  //リソース枯渇を避けるため、最大スレッド数は10個とする。


    try {
      /***************************************************************************************/
      /* スレッドを登録すると同時に、CSVファイル内の点検シート番号の重複チェックを行う。
      /***************************************************************************************/
      for(Map<String, String> map: inputlist){
        Future<List<Integer>> future = exec.submit(new ParallelCheck(map));
        futures.add(future);
        inspsheetlist.add(map.get(Inspect_Items_Enum.Inspsheet_Id.getKey()));
      }
      if(inspsheetlist.size() != inspsheetlist.stream().distinct().count()){
        errorlist.add(0, new ArrayList<>());
      }


      /***************************************************************************************/
      /* CSVファイルのヘッダ部分を除いた行数と紐つけるため、「2」からカウントしてインデックスを
      /* 指定してリストに登録する。
      /***************************************************************************************/
      int i = 2;
      for(Future<List<Integer>> result: futures){
        List<Integer> list = result.get();
        if(list.size() != 0){
          errorlist.add(i, list);
        }
        i++;
      }

    }catch(InterruptedException e) {
      throw new InterruptedException("Error location [InspectItemsForm:csvChecker(InterruptedException)]");
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [InspectItemsForm:csvChecker(ExecutionException)]", e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [InspectItemsForm:csvChecker(RejectedExecutionException)]");
    }

    exec.shutdown();
    return errorlist;
  }





  /***************************************************************/
  /*   [ParallelCheck]
  /*   並列処理の際に個別に呼び出される処理。
  /*   内部クラスとして定義。
  /***************************************************************/

  private class ParallelCheck implements Callable<List<Integer>>{

    Map<String, String> map;

    ParallelCheck(Map<String, String> map){
      this.map = map;
    }

    @Override
    public List<Integer> call(){

      List<Integer> list = new ArrayList<>();
      String tmpkey;

      try{
        /******** 点検シート番号 *****************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* 既に存在する点検シート番号と重複がないかも判別。
        /***************************************************************************************/
        tmpkey = Inspect_Items_Enum.Inspsheet_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(1);
        }else{
          if(inspitemsserv.existsData(map.get(tmpkey))){
            list.add(1);
          }
        }

        /******** 点検シート名 *******************************************************************/
        /* 入力必須で、「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Inspect_Items_Enum.Inspsheet_Name.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NOTNULL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(2);
        }

        /******** シート作成日 *******************************************************************/
        /* 入力必須かつ、ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Inspect_Items_Enum.Inspsheet_Date.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
          list.add(3);
        }

        /**************************************************************/
        /* ここでは文字列を連結してマップキーを作るため、
        /* 「Inspect_Items」列挙型の使用を断念する。
        /**************************************************************/
        for(int j = 0; j < 10; j++){
          String key1 = "insp_item_contents_".concat(String.valueOf(j + 1));
          String key2 = "insp_item_kinds_".concat(String.valueOf(j + 1));
          String key3 = "insp_item_unit_".concat(String.valueOf(j + 1));

          /******** 点検項目内容1~10 ***************************************************************/
          /* 「全角文字」のみで構成され、文字数が「400文字以内」か判別。
          /***************************************************************************************/
          if(!map.get(key1).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(key1).length() > 400){
            list.add(4 + j * 3);
          }

          /******** 点検方法1~10 ***************************************************************/
          /* 「全角文字」のみで構成され、文字数が「190文字以内」か判別。
          /***************************************************************************************/
          if(!map.get(key2).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(key2).length() > 190){
            list.add(5 + j * 3);
          }

          /******** 点検単位1~10 ***************************************************************/
          /* 「全角文字」のみで構成され、文字数が「90文字以内」か判別。
          /***************************************************************************************/
          if(!map.get(key3).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(key3).length() > 90){
            list.add(6 + j * 3);
          }
        }
      }catch(NullPointerException e){
        throw new NullPointerException("Error location [InspectItemsForm:csvChecker(NullPointerException)]");
      }

      return list;
    }
  }
}