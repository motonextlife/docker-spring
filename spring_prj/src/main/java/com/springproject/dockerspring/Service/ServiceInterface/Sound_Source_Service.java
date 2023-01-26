/** 
 **************************************************************************************
 * @file Sound_Source_Service.java
 * @brief 主に[音源管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Sound_Source_History;
import com.springproject.dockerspring.Entity.NormalEntity.Sound_Source;
import com.springproject.dockerspring.Form.CsvImplForm.Sound_Source_Form;
import com.springproject.dockerspring.Service.NormalService;








/** 
 **************************************************************************************
 * @brief 主に[音源管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see NormalService
 * @see Sound_Source
 * @see Sound_Source_History
 * @see Sound_Source_Form
 **************************************************************************************
 */ 
public interface Sound_Source_Service 
    extends NormalService<Sound_Source, Sound_Source_History, Sound_Source_Form>{}