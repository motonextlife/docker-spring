/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.CommonTestCaseMaker.Common
 * 
 * @brief テストケース生成クラスのうち、[全システム共通のフォームクラス]に関するテストケースを
 * 生成する処理のパッケージ
 * 
 * @details
 * - このパッケージは、フォームバリデーションにおいて共通で使用するフォームクラスのテストに使用する
 * テストケースを生成する処理を格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonTestCaseMaker.Common;






/** 
 **************************************************************************************
 * @file Common_TestCase_Make.java
 * @brief 主に[共通のフォーム]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.dockerspring.CommonTestCaseMaker.Common.ComHistGet.Com_Hist_Get_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Common.ComSearch.Com_Search_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Common.CommonCsv.Common_Csv_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Common.CommonZip.Common_Zip_TestCase;
import com.springproject.dockerspring.CommonTestCaseMaker.Common.DelIndiviHist.Del_Indivi_Hist_TestCase;

import lombok.RequiredArgsConstructor;







/** 
 **************************************************************************************
 * @brief 主に[共通のフォーム]機能のテストにおいて、全てのテストに用いるテストケースを全般的に
 * 生成するクラス。
 * 
 * @details 
 * - 当該パッケージ内で作成された各テストケースクラスをインジェクションし、このクラスから共通して
 * 使えるようにする。
 * - 各テストケースを集約し、メソッドを中継することが目的のクラスである。
 * - 各テストケースクラスのインジェクションは、Lombokによりコンストラクタインジェクションを
 * 行うことで実現する。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @ComponentScan
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see Com_Hist_Get_TestCase
 * @see Common_Csv_TestCase
 * @see Common_Zip_TestCase
 * @see Com_Search_TestCase
 * @see Del_Indivi_Hist_TestCase
 **************************************************************************************
 */ 
@Component
@ComponentScan
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Common_TestCase_Make{

  private Com_Hist_Get_TestCase com_hist_get_clazz;
  private Common_Csv_TestCase com_csv_clazz;
  private Common_Zip_TestCase com_zip_clazz;
  private Com_Search_TestCase com_search_clazz;
  private Del_Indivi_Hist_TestCase del_indivi_clazz;







  /** 
   **********************************************************************************************
   * @brief 共通のフォーム項目を関連付けるためのマップキーの列挙型である。
   * 
   * @details
   * - システム全体のテストクラスで、全般的に用いる。
   **********************************************************************************************
   */ 
  public static enum Common_TestKeys {
    Word,
    Start_Datetime,
    End_Datetime,
    Table_Name,
    Csv_File, 
    Zip_File;
  }








  /** @name [共通の履歴検索]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Hist_Get_TestCase
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComHistGetOkData(){
    return this.com_hist_get_clazz.getComHistGetOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Hist_Get_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComHistGetWordFailData(){
    return this.com_hist_get_clazz.getComHistGetWordFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Hist_Get_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComHistGetStartDatetimeFailData(){
    return this.com_hist_get_clazz.getComHistGetStartDatetimeFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Hist_Get_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComHistGetEndDatetimeFailData(){
    return this.com_hist_get_clazz.getComHistGetEndDatetimeFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Hist_Get_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComHistGetSubjectFailData(){
    return this.com_hist_get_clazz.getComHistGetSubjectFailData();
  }

  /** @} */








  /** @name [共通の通常情報検索]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Search_TestCase
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComSearchOkData(){
    return this.com_search_clazz.getComSearchOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Search_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComSearchWordFailData(){
    return this.com_search_clazz.getComSearchWordFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Com_Search_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComSearchSubjectFailData(){
    return this.com_search_clazz.getComSearchSubjectFailData();
  }

  /** @} */







  /** @name [共通のCSVファイル]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Common_Csv_TestCase
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComCsvOkData(){
    return this.com_csv_clazz.getComCsvOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Common_Csv_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComCsvCsvFileFailData(){
    return this.com_csv_clazz.getComCsvCsvFileFailData();
  }

  /** @} */









  /** @name [共通のZIPファイル]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Common_Zip_TestCase
   **********************************************************************************************
   */ 
  public Map<Common_TestKeys, String> getComZipOkData(){
    return this.com_zip_clazz.getComZipOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Common_Zip_TestCase
   **********************************************************************************************
   */ 
  public List<String> getComZipZipFileFailData(){
    return this.com_zip_clazz.getComZipZipFileFailData();
  }

  /** @} */







  /** @name [共通の履歴削除]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Del_Indivi_Hist_TestCase
   **********************************************************************************************
   */ 
  public List<Map<Common_TestKeys, String>> getDelIndiviHistOkData(){
    return this.del_indivi_clazz.getDelIndiviHistOkData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Del_Indivi_Hist_TestCase
   **********************************************************************************************
   */ 
  public List<String> getDelIndiviHistTableNameFailData(){
    return this.del_indivi_clazz.getDelIndiviHistTableNameFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Del_Indivi_Hist_TestCase
   **********************************************************************************************
   */ 
  public List<String> getDelIndiviHistStartDatetimeFailData(){
    return this.del_indivi_clazz.getDelIndiviHistStartDatetimeFailData();
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see Del_Indivi_Hist_TestCase
   **********************************************************************************************
   */ 
  public List<String> getDelIndiviHistEndDatetimeFailData(){
    return this.del_indivi_clazz.getDelIndiviHistEndDatetimeFailData();
  }

  /** @} */
}
