/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.InjectValids
 * 
 * @brief バリデーション機能のうち、他のクラスのDIが必要でアノテーションベースのバリデーションが
 * できない項目のバリデーション機能を格納するパッケージ
 * 
 * @details
 * - リポジトリなどの他のクラスをDIするような項目は、独自アノテーション内ではDIが成功せず使用
 * できないことが判明した為、フォームクラスとは切り分けてコントローラ内で行えるようにしている。
 * - なお、テストのしやすさの観点からも、切り分けた方が良いと判断した。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.InjectValids;





/** 
 **************************************************************************************
 * @file InjectValidMethod.java
 * @brief DIが必要なフォームバリデーションの項目に関して、判定を行うフォームバリデーション
 * クラスを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.Form.ValidTable_Enum;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.InjectEnvValid;
import com.springproject.dockerspring.Form.InjectValids.AggregationValid.InjectRepoValid;

import lombok.RequiredArgsConstructor;











/** 
 **************************************************************************************
 * @brief DIが必要なフォームバリデーションの項目に関して判定を行うフォームバリデーションクラス
 * 
 * @details 
 * - このクラスのようにDIが必要なバリデーションの項目を分離した理由としては、他のアノテーション
 * ベースのバリデーションのようにアノテーション内でDIを行おうとすると、正常にDIが行われず
 * テストができない為、動作が保証できないからである。
 * - そのため、確実に動作し品質の保証が可能な方法として、通常のフォームクラスとは分離し、
 * コントローラー内で別途バリデーションを行う方式として、このクラスを設けた。
 * 
 * @note 実際の判定処理は、集約したクラス側に任せる。このクラスは、集約したクラスへ処理を中継
 * するためのメソッドのみ設けている。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * 
 * @see InjectEnvValid
 * @see InjectRepoValid
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InjectValidMethod{

  private InjectEnvValid env_valid;
  private InjectRepoValid repo_valid;






  



  /** @name [リポジトリを要するバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult historyIdExistCheck(Integer history_id, ValidTable_Enum table, BindingResult bind_data) {
    return repo_valid.historyIdExistCheck(history_id, table, bind_data);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult pairExistCheckValid(String id,  
                                           List<Integer> serial_num_list, 
                                           ValidTable_Enum table, 
                                           BindingResult bind_data, 
                                           String field_name) {

    return repo_valid.pairExistCheckValid(id, serial_num_list, table, bind_data, field_name);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult foreignKeyCheck(String id, ValidTable_Enum table, BindingResult bind_data, String field_name) {
    return repo_valid.foreignKeyCheck(id, table, bind_data, field_name);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult deleteForeignCheck(String id, ValidTable_Enum table, BindingResult bind_data) {
    return repo_valid.deleteForeignCheck(id, table, bind_data);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult idUniqueCheck(String id, 
                                     Integer serial_num, 
                                     ValidTable_Enum table, 
                                     BindingResult bind_data, 
                                     String field_name) {

    return repo_valid.idUniqueCheck(id, serial_num, table, bind_data, field_name);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectRepoValid
   **********************************************************************************************
   */ 
  public BindingResult usernameUniqueCheck(String username, Integer serial_num, BindingResult bind_data) {
    return repo_valid.usernameUniqueCheck(username, serial_num, bind_data);
  }

  /** @} */










  /** @name [環境変数を要するバリデーション]に関連するメソッド */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectEnvValid
   **********************************************************************************************
   */ 
  public <T> BindingResult dataListLimit(List<T> list, BindingResult bind_data, String field_name, Boolean single, Datatype_Enum type) {
    return env_valid.dataListLimit(list, bind_data, field_name, single, type);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectEnvValid
   **********************************************************************************************
   */ 
  public BindingResult matchFileCheck(MultipartFile file, 
                                      Datatype_Enum kind, 
                                      BindingResult bind_data, 
                                      String field_name) throws IOException {

    return env_valid.matchFileCheck(file, kind, bind_data, field_name);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectEnvValid
   **********************************************************************************************
   */ 
  public BindingResult pageNumAndSubjectCheck(Integer page_num, String subject, ValidTable_Enum table, BindingResult bind_data) {
    return env_valid.pageNumAndSubjectCheck(page_num, subject, table, bind_data);
  }


  /** 
   **********************************************************************************************
   * @brief インジェクションしたクラスのメソッドの中継メソッドである。詳しくは該当クラスを参照。
   * 
   * @see InjectEnvValid
   **********************************************************************************************
   */ 
  public BindingResult insertOrUpdateCheck(Integer serial_num, BindingResult bind_data, Boolean update) {
    return env_valid.insertOrUpdateCheck(serial_num, bind_data, update);
  }

  /** @} */
}
