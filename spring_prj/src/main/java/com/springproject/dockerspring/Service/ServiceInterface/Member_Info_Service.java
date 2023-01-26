/** 
 **************************************************************************************
 * @file Member_Info_Service.java
 * @brief 主に[団員管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Form.CsvImplForm.Member_Info_Form;
import com.springproject.dockerspring.Service.NormalService;







/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see NormalService
 * @see Member_Info
 * @see Member_Info_History
 * @see Member_Info_Form
 **************************************************************************************
 */ 
public interface Member_Info_Service 
    extends NormalService<Member_Info, Member_Info_History, Member_Info_Form>{}