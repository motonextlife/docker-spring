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

import com.springproject.dockerspring.commonenum.Equip_Inspect_Enum;
import com.springproject.dockerspring.form.CsvValidation;
import com.springproject.dockerspring.form.RegexEnum;
import com.springproject.dockerspring.form.OriginalAnno.ForeignKey;
import com.springproject.dockerspring.service.ServiceInterface.EquipInspectService;
import com.springproject.dockerspring.service.ServiceInterface.FacilityService;
import com.springproject.dockerspring.service.ServiceInterface.InspectItemsService;

import lombok.Data;





/**************************************************************/
/*   [EquipInspectForm]
/*   「点検情報」に関するフォーム入力値やCSVファイルの
/*    入力値のチェックを行う。
/**************************************************************/

@Data
public class EquipInspectForm implements Serializable, CsvValidation{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /****************************************************/
  /* {点検情報番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String inspect_id;
  

  /************************************************************/
  /* {対象設備番号}
  /* 未入力は許さない。
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Facility」内に存在する設備番号のみ設定可能。
  /************************************************************/
  @NotNull
  @Length(min=0, max=90)
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @ForeignKey(table="Facility")
  private String faci_id;


  /**************************************************/
  /* {点検情報名}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String inspect_name;


  /**************************************************/
  /* {点検日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date inspect_date;


  /***********************************************************************/
  /* {関連点検シート番号}
  /* 未入力は許さない。
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Inspect_Items」内に存在する採用考課シート番号のみ設定可能。
  /***********************************************************************/
  @NotBlank
  @Length(min=0, max=90)
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @ForeignKey(table="Inspect_Items")
  private String inspsheet_id;


  /******************************************/
  /* {点検項目内容1}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「400文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_1;


  /******************************************/
  /* {点検結果1}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「400文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_1;


  /******************************************/
  /* 以下2～10まで同様のルールとする。
  /******************************************/ 
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_2;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_2;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_3;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_3;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_4;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_4;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_5;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_5;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_6;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_6;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_7;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_7;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_8;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_8;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_9;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_9;
  
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_contents_10;

  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String insp_rank_10;






  /***************************************************************/
  /*   [csvChecker]
  /*   受け取ったデータ内の文字列全てをバリデーションチェックし、
  /*   エラーがあったデータは、該当データのCSV内での行数と列数を
  /*   エラーリストとして出力する。
  /***************************************************************/

  @Autowired
  private FacilityService faciserv;

  @Autowired
  private InspectItemsService inspitemsserv;

  @Autowired
  private EquipInspectService equipinspserv;
  
  @Override
  public List<List<Integer>> csvChecker(List<Map<String, String>> inputlist) throws ExecutionException, InterruptedException {

    List<List<Integer>> errorlist = new ArrayList<>();
    List<String> equiplist = new ArrayList<>();
    List<Future<List<Integer>>> futures = new ArrayList<>();
    ExecutorService exec = Executors.newFixedThreadPool(10);  //リソース枯渇を避けるため、最大スレッド数は10個とする。


    try {
      /***************************************************************************************/
      /* スレッドを登録すると同時に、CSVファイル内の点検情報番号の重複チェックを行う。
      /***************************************************************************************/
      for(Map<String, String> map: inputlist){
        Future<List<Integer>> future = exec.submit(new ParallelCheck(map));
        futures.add(future);
        equiplist.add(map.get(Equip_Inspect_Enum.Inspect_Id.getKey()));
      }
      if(equiplist.size() != equiplist.stream().distinct().count()){
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
      throw new InterruptedException("Error location [EquipInspectForm:csvChecker(InterruptedException)]");
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [EquipInspectForm:csvChecker(ExecutionException)]", e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [EquipInspectForm:csvChecker(RejectedExecutionException)]");
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
        /******** 点検情報番号 *******************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* 既に存在する点検情報番号と重複がないかも判別。
        /***************************************************************************************/
        tmpkey = Equip_Inspect_Enum.Inspect_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(1);
        }else{
          if(equipinspserv.existsData(map.get(tmpkey))){
            list.add(1);
          }
        }

        /******** 対象設備番号 ******************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* テーブル「Facility」内に存在する設備番号のみ設定可能。
        /***************************************************************************************/
        tmpkey = Equip_Inspect_Enum.Faci_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(2);
        }else{
          if(!faciserv.existsData(map.get(tmpkey))){
            list.add(2);
          }
        }

        /******** 点検情報名 ********************************************************************/
        /* 入力必須で、「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Equip_Inspect_Enum.Inspect_Name.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NOTNULL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(3);
        }

        /******** 点検日 ************************************************************************/
        /* 入力必須かつ、ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Equip_Inspect_Enum.Inspect_Date.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
          list.add(4);
        }

        /******** 関連点検シート番号 *************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* テーブル「Inspect_Items」内に存在する関連採用考課シート番号のみ設定可能。
        /***************************************************************************************/
        tmpkey = Equip_Inspect_Enum.Inspsheet_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(5);
        }else{
          if(!inspitemsserv.existsData(map.get(tmpkey))){
            list.add(5);
          }
        }

        /**************************************************************/
        /* ここでは文字列を連結してマップキーを作るため、
        /* 「Equip_Inspect」列挙型の使用を断念する。
        /**************************************************************/
        for(int j = 0; j < 10; j++){
          String key1 = "insp_contents_".concat(String.valueOf(j + 1));
          String key2 = "insp_rank_".concat(String.valueOf(j + 1));

          /******** 点検項目内容1~10 ***************************************************************/
          /* 「全角文字」のみで構成され、文字数が「400文字以内」か判別。
          /***************************************************************************************/
          if(!map.get(key1).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(key1).length() > 400){
            list.add(6 + j * 2);
          }

          /******** 点検結果1~10 ******************************************************************/
          /* 「全角文字」のみで構成され、文字数が「190文字以内」か判別。
          /***************************************************************************************/
          if(!map.get(key2).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(key2).length() > 400){
            list.add(7 + j * 2);
          }
        }
      }catch(NullPointerException e){
        throw new NullPointerException("Error location [EquipInspectForm:csvChecker(NullPointerException)]");
      }

      return list;
    }
  }
}