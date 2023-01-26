/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Form.FormInterface
 * 
 * @brief バリデーションクラスに全般的に使用するインターフェースを格納したパッケージ
 * 
 * @details
 * - このパッケージに一緒くたに全てのインターフェースを格納した理由としては、SpringのDIに対応する
 * 為のメソッドの定義が全くないインターフェースがほとんどで、新たなパッケージを作る必要はないと
 * 判断したためである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Form.FormInterface;







/** 
 **************************************************************************************
 * @file Audio_Data_Zip_Form.java
 * @brief 主に[音源データ情報]機能で使用する、入力されたZIPファイルのバリデーションを行う
 * 機能を提供するインターフェースを格納するファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 







/** 
 **************************************************************************************
 * @brief 主に[音源データ情報]機能で使用する、入力されたZIPファイルのバリデーションを行う
 * 機能を提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、DIの際にデータ型を指定してインジェクションできるように使用する
 * 物であり、このインターフェース内に抽象メソッドは定義しない。
 * - 実装内容は、継承元のインターフェースの物を用いる。
 * 
 * @see Blob_Validation
 **************************************************************************************
 */ 
public interface Audio_Data_Zip_Form extends Blob_Validation{}