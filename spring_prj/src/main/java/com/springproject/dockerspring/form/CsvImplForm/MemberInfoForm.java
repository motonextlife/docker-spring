package com.springproject.dockerspring.form.CsvImplForm;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.commonenum.DateFormat_Enum;
import com.springproject.dockerspring.commonenum.Member_Info_Enum;
import com.springproject.dockerspring.form.CsvValidation;
import com.springproject.dockerspring.form.RegexEnum;
import com.springproject.dockerspring.form.OriginalAnno.DataSizeLimit;
import com.springproject.dockerspring.form.OriginalAnno.DateCorrCheck;
import com.springproject.dockerspring.form.OriginalAnno.NotAudio;
import com.springproject.dockerspring.form.OriginalAnno.NotMovie;
import com.springproject.dockerspring.form.OriginalAnno.NotPdf;
import com.springproject.dockerspring.service.ServiceInterface.MemberInfoService;

import lombok.Data;



/********************************************************/
/*   [MemberInfoForm]
/*   「団員情報」に関するフォーム入力値やCSVファイルの
/*    入力値のチェックを行う。
/********************************************************/
@Component
@Data
@DateCorrCheck(start={"join_date", "join_date", "join_date", "birthday"}, 
               end={"ret_date", "position_arri_date", "assign_date", "join_date"})
public class MemberInfoForm implements Serializable, CsvValidation{


  /******************************************/
  /* {シリアルナンバー}
  /* 数字を符号なしで「9桁」まで受け付ける。
  /* 浮動小数点は受け付けない。
  /******************************************/
  @Digits(integer=9, fraction=0)
  private Integer serial_num;


  /****************************************************/
  /* {団員番号}
  /* 空白や空文字は許さず「半角英数字」のみ受け付ける。
  /* 文字数は「90文字以内」まで受付可能。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="^[a-zA-Z0-9]*$")
  @Length(min=0, max=90)
  private String member_id;


  /**************************************************/
  /* {名前}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String name;


  /**************************************************/
  /* {名前ふりがな}
  /* 空白や空文字は許さず「全角文字」のみ受け付ける。
  /* 文字数は「190文字以内」まで受付可能。
  /**************************************************/
  @NotBlank
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  @Length(min=0, max=190)
  private String name_pronu;


  /****************************************************/
  /* {性別}
  /* 空白や空文字は許さず「指定文字列」のみ受け付ける。
  /****************************************************/
  @NotBlank
  @Pattern(regexp="male|female")
  private String sex;


  /**************************************************/
  /* {誕生日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date birthday;


  /*********************************************************/
  /* {顔写真}
  /* 画像ファイルは「1MB以下」かつ「PNG形式」のみ受付可能。
  /*********************************************************/
  @NotPdf
  @NotAudio
  @NotMovie
  @DataSizeLimit
  private MultipartFile face_photo;


  /**************************************************/
  /* {入団日}
  /* 未入力は許さない。
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* 「誕生日」以前の日付は受け付けない。
  /**************************************************/
  @NotNull
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date join_date;


  /*************************************************/
  /* {退団日}
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* 「入団日」以前の日付は受け付けない。
  /*************************************************/
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date ret_date;


  /*******************************************************/
  /* {メールアドレス1}
  /* 「Eメール形式」として成立する文字列しか受け付けない。
  /* 文字数は「190文字以内」まで受付可能。
  /*******************************************************/
  @Length(min=0, max=190)
  @Email
  private String email_1;


  /*******************************************************/
  /* {メールアドレス2} 
  /* 「Eメール形式」として成立する文字列しか受け付けない。
  /* 文字数は「190文字以内」まで受付可能。
  /*******************************************************/
  @Length(min=0, max=190)
  @Email
  private String email_2;


  /*********************************************************************/
  /* {電話番号1}
  /* 「半角数字」のみ受け付ける。
  /* 文字数は「15文字以内」まで受付可能。
  /* なお、頭文字が「0」の場合に対応するため、「文字列型」での受付となる。
  /*********************************************************************/
  @Length(min=0, max=15)
  @Pattern(regexp="^[0-9]*$")
  private String tel_1;


  /*********************************************************************/
  /* {電話番号2}
  /* 「半角数字」のみ受け付ける。
  /* 文字数は「15文字以内」まで受付可能。
  /* なお、頭文字が「0」の場合に対応するため、「文字列型」での受付となる。
  /*********************************************************************/
  @Length(min=0, max=15)
  @Pattern(regexp="^[0-9]*$")
  private String tel_2;


  /********************************************************************/
  /* {郵便番号}
  /* 「半角数字」のみ受け付ける。
  /* 文字数は「10文字以内」まで受付可能。
  /* なお、頭文字が「0」の場合に対応するため、「文字列型」での受付となる。
  /********************************************************************/
  @Length(min=0, max=10)
  @Pattern(regexp="^[0-9]*$")
  private String addr_postcode;


  /******************************************/
  /* {住所}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「700文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=700)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String addr;


  /******************************************/
  /* {役職}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「100文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=100)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String position;


  /*************************************************/
  /* {現役職の着任日}
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* 「入団日」以前の日付は受け付けない。
  /*************************************************/
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date position_arri_date;


  /******************************************/
  /* {職種}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「100文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=100)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String job;


  /******************************************/
  /* {配属係}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「100文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=100)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String assign_dept;


  /*************************************************/
  /* {配属日}
  /* 入力値は自動的に以下のフォーマットに変換される。
  /* 「入団日」以前の日付は受け付けない。
  /*************************************************/
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date assign_date;


  /******************************************/
  /* {担当楽器}
  /* 「全角文字」のみ受け付ける。
  /* 文字数は「100文字以内」まで受付可能。
  /******************************************/
  @Length(min=0, max=100)
  @Pattern(regexp="^[^ -~｡-ﾟ]*$")
  private String inst_charge;


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
  private MemberInfoService membinfoserv;

  @Override
  public List<List<Integer>> csvChecker(List<Map<String, String>> inputlist) throws ExecutionException, InterruptedException {

    List<List<Integer>> errorlist = new ArrayList<>();
    List<String> memblist = new ArrayList<>();
    List<Future<List<Integer>>> futures = new ArrayList<>();
    ExecutorService exec = Executors.newFixedThreadPool(10);  //リソース枯渇を避けるため、最大スレッド数は10個とする。


    try {
      /***************************************************************************************/
      /* スレッドを登録すると同時に、CSVファイル内の団員番号の重複チェックを行う。
      /***************************************************************************************/
      for(Map<String, String> map: inputlist){
        Future<List<Integer>> future = exec.submit(new ParallelCheck(map));
        futures.add(future);
        memblist.add(map.get(Member_Info_Enum.Member_Id.getKey()));
      }
      if(memblist.size() != memblist.stream().distinct().count()){
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
      throw new InterruptedException("Error location [MemberInfoForm:csvChecker(InterruptedException)]");
    }catch(ExecutionException e){
      throw new ExecutionException("Error location [MemberInfoForm:csvChecker(ExecutionException)]", e.getCause());
    }catch(RejectedExecutionException e){
      throw new RejectedExecutionException("Error location [MemberInfoForm:csvChecker(RejectedExecutionException)]");
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
    public List<Integer> call() throws ParseException{

      List<Integer> list = new ArrayList<>();
      SimpleDateFormat date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getStr());
      String tmpkey;

      try{
        /******** 団員番号 **********************************************************************/
        /* 入力必須で、「半角英数字」のみで構成され、文字数が「90文字以内」か判別。。
        /* 既に存在する団員番号と重複がないかも判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Member_Id.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ALNUM_NOTNULL.getRegex()) || map.get(tmpkey).length() > 90){
          list.add(1);
        }else{
          if(membinfoserv.existsData(map.get(tmpkey))){
            list.add(1);
          }
        }

        /******** 名前 **************************************************************************/
        /* 入力必須で、「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Name.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NOTNULL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(2);
        }

        /******** 名前ふりがな *******************************************************************/
        /* 入力必須で、「全角文字」のみで構成され、文字数が「190文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Name_Pronu.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NOTNULL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(3);
        }

        /******** 性別 **************************************************************************/
        /* 入力必須かつ、「男」または「女」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Sex.getKey();
        if(!map.get(tmpkey).matches("男|女")){
          list.add(4);
        }

        /******** 誕生日 ************************************************************************/
        /* 入力必須かつ、ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Birthday.getKey();
        Date birthday = null;
        Boolean flag1 = false;
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
          list.add(5);
        }else{
          birthday = date.parse(map.get(tmpkey));
          flag1 = true;
        }

        /******** 入団日 ************************************************************************/
        /* 入力必須かつ、ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Join_Date.getKey();
        Date join_date = null;
        Boolean flag2 = false;
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NOTNULL.getRegex())){
          list.add(6);
        }else{
          join_date = date.parse(map.get(tmpkey));
          flag2 = true;
        }

        /******** 退団日 ************************************************************************/
        /* ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Ret_Date.getKey();
        Date ret_date = null;
        Boolean flag3 = false;
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NULLOK.getRegex())){
          list.add(7);
        }else{
          ret_date = date.parse(map.get(tmpkey));
          flag3 = true;
        }

        /******** メールアドレス1 ****************************************************************/
        /* メールアドレス形式の文字列（例：example@co.jp）かつ、「190文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Email_1.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.E_MAILL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(8);
        }

        /******** メールアドレス2 ****************************************************************/
        /* メールアドレス形式の文字列（例：example@co.jp）かつ、「190文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Email_2.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.E_MAILL.getRegex()) || map.get(tmpkey).length() > 190){
          list.add(9);
        }

        /******** 電話番号1 *********************************************************************/
        /* ハイフン無しで「半角数字」のみでかつ、「15文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Tel_1.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DIGIT.getRegex()) || map.get(tmpkey).length() > 15){
          list.add(10);
        }

        /******** 電話番号2 *********************************************************************/
        /* ハイフン無しで「半角数字」のみでかつ、「15文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Tel_2.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DIGIT.getRegex()) || map.get(tmpkey).length() > 15){
          list.add(11);
        }

        /******** 郵便番号 **********************************************************************/
        /* ハイフン無しで「半角数字」のみでかつ、「15文字以内」で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Addr_Postcode.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.DIGIT.getRegex()) || map.get(tmpkey).length() > 15){
          list.add(12);
        }

        /******** 住所 *************************************************************************/
        /* 「全角文字」のみで構成され、文字数が「700文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Addr.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 700){
          list.add(13);
        }

        /******** 役職 *************************************************************************/
        /* 「全角文字」のみで構成され、文字数が「100文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Position.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 100){
          list.add(15);
        }

        /******** 現役職の着任日 *****************************************************************/
        /* ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Position_Arri_Date.getKey();
        Date position_arri_date = null;
        Boolean flag4 = false;
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NULLOK.getRegex())){
          list.add(16);
        }else{
          position_arri_date = date.parse(map.get(tmpkey));
          flag4 = true;
        }


        /******** 職種 *************************************************************************/
        /* 「全角文字」のみで構成され、文字数が「100文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Job.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 100){
          list.add(17);
        }

        /******** 配属部 ************************************************************************/
        /* 「全角文字」のみで構成され、文字数が「100文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Assign_Dept.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 100){
          list.add(18);
        }

        /******** 配属日 ************************************************************************/
        /* ハイフン無しのゼロ埋め年月日（例：19960219）で構成されているか判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Assign_Date.getKey();
        Date assign_date = null;
        Boolean flag5 = false;
        if(!map.get(tmpkey).matches(RegexEnum.DATE_NO_HYPHEN_NULLOK.getRegex())){
          list.add(19);
        }else{
          assign_date = date.parse(map.get(tmpkey));
          flag5 = true;
        }

        /******** 担当楽器 **********************************************************************/
        /* 「全角文字」のみで構成され、文字数が「100文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Inst_Charge.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 100){
          list.add(20);
        }

        /******** その他コメント *****************************************************************/
        /* 「全角文字」のみで構成され、文字数が「400文字以内」か判別。
        /***************************************************************************************/
        tmpkey = Member_Info_Enum.Other_Comment.getKey();
        if(!map.get(tmpkey).matches(RegexEnum.ZENKAKU_ONLY_NULLOK.getRegex()) || map.get(tmpkey).length() > 400){
          list.add(21);
        }

        /******** 「誕生日」と「入団日」の論理チェック **********************************************/
        /* 「誕生日」が「入団日」以前に設定されている事を判別。
        /***************************************************************************************/
        if(flag1 && flag2 && birthday.after(join_date)){
          list.add(6);
        }

        /******** 「入団日」と「退団日」の論理チェック **********************************************/
        /* 「入団日」が「退団日」以前に設定されている事を判別。
        /***************************************************************************************/
        if(flag2 && flag3 && join_date.after(ret_date)){
          list.add(7);
        }

        /******** 「入団日」と「現役職の着任日」の論理チェック ***************************************/
        /* 「入団日」が「現役職の着任日」以前に設定されている事を判別。
        /***************************************************************************************/
        if(flag2 && flag4 && join_date.after(position_arri_date)){
          list.add(16);
        }

        /******** 「入団日」と「配属日」の論理チェック **********************************************/
        /* 「入団日」が「配属日」以前に設定されている事を判別。
        /***************************************************************************************/
        if(flag2 && flag5 && join_date.after(assign_date)){
          list.add(19);
        }

      }catch(ParseException e){
        throw new ParseException("Error location [MemberInfoForm:csvChecker(ParseException)]", e.getErrorOffset());
      }catch(NullPointerException e){
        throw new NullPointerException("Error location [MemberInfoForm:csvChecker(NullPointerException)]");
      }

      return list;
    }
  }
}