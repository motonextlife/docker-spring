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

import com.springproject.dockerspring.commonenum.Facility_Enum;
import com.springproject.dockerspring.form.CsvValidation;
import com.springproject.dockerspring.form.RegexEnum;
import com.springproject.dockerspring.form.OriginalAnno.ForeignKey;
import com.springproject.dockerspring.service.ServiceInterface.FacilityService;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;

import lombok.Data;




/********************************************************/
/*   [FacilityForm]
/*   「設備情報」に関するフォーム入力値やCSVファイルの
/*    入力値のチェックを行う。
/********************************************************/

@Data
public class FacilityForm implements Serializable, CsvValidation{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /****************************************************/
  /* {設備番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String faci_id;


  /**************************************************/
  /* {設備名}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String faci_name;


  /************************************************************/
  /* {管理責任者}
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Member_Info」内に存在する団員番号のみ設定可能。
  /************************************************************/
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  @ForeignKey(table="Member_Info")
  private String chief_admin;


  /************************************************************/
  /* {管理担当者}
  /* 「半角英数字」のみ受け付け、文字数は「90文字以内」まで可能。
  /* テーブル「Member_Info」内に存在する団員番号のみ設定可能。
  /************************************************************/
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  @ForeignKey(table="Member_Info")
  private String resp_person;


  /**************************************************/
  /* {購入日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date buy_date;


  /******************************************/
  /* {保管場所}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=190)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String storage_loc;


  /******************************************/
  /* {購入時金額}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer buy_price;


  /*************************************************/
  /* {廃棄日}
  /* 入力値は自動的に以下のフォーマットに変換される。
  /*************************************************/
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date disp_date;


  /******************************************/
  /* {その他コメント}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「400文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=400)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String other_comment;







  /***************************************************************/
  /*   [csvChecker]
  /*   受け取ったデータ内の文字列全てをバリデーションチェックし、
  /*   エラーがあったデータは、該当データのCSV内での行数と列数を
  /*   エラーリストとして出力する。
  /***************************************************************/

  @Autowired
  private FacilityService faciserv;

  @Autowired
  private MemberInfoService menbinfoserv;
  
  @Override
  public List<List<Integer>> csvChecker(List<Map<String, String>> inputlist) throws ExecutionException, InterruptedException {

    List<List<Integer>> errorlist = new ArrayList<>();
    List<String> facilist = new ArrayList<>();
    List<Future<List<Integer>>> futures = new ArrayList<>();
    ExecutorService exec = Executors.newFixedThreadPool(10);  //リソース枯渇を避けるため、最大スレッド数は10個とする。

    
    try {
      /***************************************************************************************/
      /* スレッドを登録すると同時に、CSVファイル内の設備番号の重複チェックを行う。
      /***************************************************************************************/
      for(Map<String, String> map: inputlist){
        Future<List<Integer>> future = exec.submit(new ParallelCheck(map));
        futures.add(future);
        facilist.add(map.get(Facility_Enum.Faci_Id.getKey()));
      }
      if(facilist.size() != facilist.stream().distinct().count()){
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
      throw new InterruptedException("Error location [FacilityForm:csvChecker(InterruptedException)]");
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [FacilityForm:csvChecker(ExecutionException)]", e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [FacilityForm:csvChecker(RejectedExecutionException)]");
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
        /******** 設備番号 **********************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* 既に存在する設備番号と重複がないかも判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Faci_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(1);
        }else{
          if(faciserv.existsData(map.get(tmpkey))){
            list.add(1);
          }
        }

        /******** 設備名 ************************************************************************/
        /* 入力必須で、「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Faci_Name.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NOTNULL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(2);
        }

        /******** 管理責任者 ********************************************************************/
        /* 「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* テーブル「Member_Info」内に存在する団員番号のみ設定可能。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Chief_Admin.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NULLOK.getRegex()) 
            || map.get(tmpkey).length() > 90 
            || !menbinfoserv.existsData(map.get(tmpkey))){
          list.add(3);
        }

        /******** 管理担当者 ********************************************************************/
        /* 「半角英数字」のみで構成され、文字数が「90文字以内」か判別。
        /* テーブル「Member_Info」内に存在する団員番号のみ設定可能。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Resp_Person.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NULLOK.getRegex()) 
            || map.get(tmpkey).length() > 90 
            || !menbinfoserv.existsData(map.get(tmpkey))){
          list.add(4);
        }

        /******** 購入日 ************************************************************************/
        /* 入力必須かつ、ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Buy_Date.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
          list.add(5);
        }

        /******** 保管場所 **********************************************************************/
        /* 「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Storage_Loc.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(6);
        }

        /******** 購入時金額 ********************************************************************/
        /* ハイフン無しで「半角数字」のみでかつ、「9文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Buy_Price.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DIGIT.getRegex()) || map.get(tmpkey).length() > 9){
          list.add(7);
        }

        /******** 廃棄日 ************************************************************************/
        /* ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Disp_Date.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NULLOK.getRegex())){
          list.add(8);
        }

        /******** その他コメント *****************************************************************/
        /* 「全角文字」のみで構成され、文字数が「400文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Facility_Enum.Other_Comment.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 400){
          list.add(9);
        }
      }catch(NullPointerException e){
        throw new NullPointerException("Error location [FacilityForm:csvChecker(NullPointerException)]");
      }

      return list;
    }
  }
}