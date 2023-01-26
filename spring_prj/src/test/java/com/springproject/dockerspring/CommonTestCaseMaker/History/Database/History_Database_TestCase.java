/** 
 **************************************************************************************
 * @file History_Database_TestCase.java
 * @brief 主に[共通の履歴機能]機能のテストにおいて、テストケースのファイルから値を読み取る
 * クラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.History.Database;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;











/** 
 **************************************************************************************
 * @brief 主に[共通の履歴機能]機能のテストにおいて、テストケースのファイルから値を読み取る
 * クラス。
 * 
 * @details 
 * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
 * - マッピングしたデータは、外部から取得して、他のテストケースクラスで使用可能。
 * - 現段階ではデータベース等へのデータの格納は行わない。実際の格納は、このクラスからデータを
 * 取得した他のテストケースクラスが行う。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see Common_Hist_Database_Yaml
 **************************************************************************************
 */ 
@Component
public class History_Database_TestCase{
  
  private final Common_Hist_Database_Yaml database_yml;





  



  /** 
   **********************************************************************************************
   * @brief YMLファイルからデータを読み取って専用エンティティへのマッピングを行う。
   * 
   * @details
   * - テストケースが書かれたYMLファイルを読み込んで、専用のエンティティにマッピングする。
   * 
   * @par 処理の大まかな流れ
   * -# テストケースのYMLファイルを読み込む。
   * -# 読み込んだファイルを、専用のエンティティにマッピングする。
   * -# マッピングしたデータは、フィールド変数に格納する。
   * 
   * @throw IOException
   * 
   * @see Common_Hist_Database_Yaml
   **********************************************************************************************
   */ 
  @Autowired
  public History_Database_TestCase() throws IOException{

    try(InputStream in_database_yml = new ClassPathResource("TestCaseFile/History/common-hist-database.yml").getInputStream()){
      Yaml yaml = new Yaml();
      this.database_yml = yaml.loadAs(in_database_yml, Common_Hist_Database_Yaml.class);
    }
  }











  /** 
   **********************************************************************************************
   * @brief データベースに正常に保存される、[管理番号]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[管理番号]を取り出しリストに格納する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   **********************************************************************************************
   */ 
  public String getCompareId(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.database_yml.getId().get("case_" + testcase_num);
    }
  }










  /** 
   **********************************************************************************************
   * @brief データベースに正常に保存される、[履歴日時]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[履歴日時]を取り出しリストに格納する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   **********************************************************************************************
   */ 
  public Date getCompareChange_datetime(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.database_yml.getChange_datetime().get("case_" + testcase_num);
    }
  }










  /** 
   **********************************************************************************************
   * @brief データベースに正常に保存される、[履歴種別]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[履歴種別]を取り出しリストに格納する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号
   * 
   * @return 番号に対応するデータ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   **********************************************************************************************
   */ 
  public String getCompareChange_kinds(Integer testcase_num){

    if(testcase_num > 20 || testcase_num < 1){
      throw new IllegalArgumentException();
    }else{
      return this.database_yml.getChange_kinds().get("case_" + testcase_num);
    }
  }










  /** 
   **********************************************************************************************
   * @brief データベースに正常に保存される、[操作ユーザー名]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[操作ユーザー名]を取り出しリストに格納する。
   * - 範囲外の数字が来たときは処理を続行できないのでエラーを投げる。
   * 
   * @param[in] testcase_num 指定するテストケースの番号。「Null」の場合は、カラムに許容される文字数ギリギリ
   * の文字数のテストケースのみ出力される。
   * 
   * @return 番号に対応するデータ
   * 
   * @par 処理の大まかな流れ
   * -# 指定した番号のテストケースデータを、マッピングエンティティから取り出す。
   * ただし、フラグが立っている場合は、該当テストケースデータのみとなり、必ずしもデータ数は20個にならない。
   **********************************************************************************************
   */ 
  public String getCompareOperation_user(Integer testcase_num){

    if(testcase_num == null){
      return this.database_yml.getOperation_user().get("ok_length");

    }else{
      if(testcase_num > 20 || testcase_num < 1){
        throw new IllegalArgumentException();
      }else{
        return this.database_yml.getOperation_user().get("case_" + testcase_num);
      }
    }
  }









  /** 
   **********************************************************************************************
   * @brief データベースに保存する際にエラーになる、[履歴日時]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[履歴日時]を取り出しリストに格納する。
   * 
   * @return エラーテストケースが格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、該当のデータのみ取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 格納が終わったリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public List<Date> getFailChange_datetime(){

    List<Date> compare_list = new ArrayList<>();

    compare_list.add(this.database_yml.getChange_datetime().get("ng_null"));

    return compare_list;
  }










  /** 
   **********************************************************************************************
   * @brief データベースに保存する際にエラーになる、[履歴種別]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[履歴種別]を取り出しリストに格納する。
   * 
   * @return エラーテストケースが格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、該当のデータのみ取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 格納が終わったリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getFailChange_kinds(){

    List<String> compare_list = new ArrayList<>();

    compare_list.add(this.database_yml.getChange_kinds().get("ng_enum"));
    compare_list.add(this.database_yml.getChange_kinds().get("ng_null"));

    return compare_list;
  }










  /** 
   **********************************************************************************************
   * @brief データベースに保存する際にエラーになる、[操作ユーザー名]をリストに格納したものである。
   * 
   * @details
   * - 既にマッピングされている専用エンティティから、[操作ユーザー名]を取り出しリストに格納する。
   * 
   * @return エラーテストケースが格納されたリスト
   * 
   * @par 処理の大まかな流れ
   * -# マッピングしたエンティティから、該当のデータのみ取り出す。
   * -# 取り出したデータは、順番にリストに格納する。
   * -# 格納が終わったリストを、戻り値とする。
   **********************************************************************************************
   */ 
  public List<String> getFailOperation_user(){

    List<String> compare_list = new ArrayList<>();

    compare_list.add(this.database_yml.getOperation_user().get("ng_overflow"));
    compare_list.add(this.database_yml.getOperation_user().get("ng_null"));

    return compare_list;
  }
}
