/** 
 **************************************************************************************
 * @file Musical_Score_Csv_Form.java
 * @brief 主に[楽譜管理]機能で使用する、入力されたCSvファイルのバリデーションを行う機能を
 * 提供するインターフェースを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.FormInterface;







/** 
 **************************************************************************************
 * @brief 主に[楽譜管理]機能で使用する、入力されたCSvファイルのバリデーションを行う機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、DIの際にデータ型を指定してインジェクションできるように使用する
 * 物であり、このインターフェース内に抽象メソッドは定義しない。
 * - 実装内容は、継承元のインターフェースの物を用いる。
 * 
 * @see Csv_Validation
 **************************************************************************************
 */ 
public interface Musical_Score_Csv_Form extends Csv_Validation{}