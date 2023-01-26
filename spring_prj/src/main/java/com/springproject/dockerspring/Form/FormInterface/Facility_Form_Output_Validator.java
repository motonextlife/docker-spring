/** 
 **************************************************************************************
 * @file Facility_Form_Output_Validator.java
 * @brief 主に[設備管理]機能で使用する、出力時のバリデーションの機能を提供するインターフェースを
 * 格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.FormInterface;

import org.springframework.validation.Validator;







/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能で使用する、出力時のバリデーションの機能を提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、DIの際にデータ型を指定してインジェクションできるように使用する
 * 物であり、このインターフェース内に抽象メソッドは定義しない。
 * - 実装内容は、継承元のインターフェースの物を用いる。
 **************************************************************************************
 */ 
public interface Facility_Form_Output_Validator extends Validator {}
