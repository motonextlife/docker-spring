/** 
 **************************************************************************************
 * @file Member_Info_Pdf.java
 * @brief 主に[団員管理]機能で使用する、指定された情報をPDFシートとして出力する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.CompInterface;

import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;






/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能で使用する、指定された情報をPDFシートとして出力する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see OutPdf<Member_Info>
 **************************************************************************************
 */
public interface Member_Info_Pdf extends OutPdf<Member_Info>{}