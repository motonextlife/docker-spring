/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Repository.SubjectEnum
 * 
 * @brief リポジトリでのデータ検索時に検索対象のカラムを指定するための種別を定義した列挙型を
 * 格納する。
 * 
 * @details
 * - このパッケージでは、検索時の検索種別を格納する。ユーザーから渡ってきた指定の種別の文字列が
 * このパッケージ内に格納されている列挙型と一致しないと検索ができないようになっている。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Repository.SubjectEnum;





/** 
 **************************************************************************************
 * @file Audio_Data_Subject.java
 * 
 * @brief フォームから来た検索ジャンルを判別するための列挙型を格納するファイルである。
 * 主に[音源データ管理]機能のリポジトリで使用する。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 




/** 
 **************************************************************************************
 * @brief 主に[音源データ管理]機能のリポジトリで、フォームから来た検索ジャンルを判別するための
 * 列挙型である。
 * 
 * @details 列挙型に定義する内容としては、「検索ジャンル名」を定義する。
 **************************************************************************************
 */ 
public enum Audio_Data_Subject {
  All,
  SoundName;
}