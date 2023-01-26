/** 
 **************************************************************************************
 * @file Facility_Service.java
 * @brief 主に[設備管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェースを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.ServiceInterface;

import com.springproject.dockerspring.Entity.HistoryEntity.Facility_History;
import com.springproject.dockerspring.Entity.NormalEntity.Facility;
import com.springproject.dockerspring.Form.CsvImplForm.Facility_Form;
import com.springproject.dockerspring.Service.NormalService;








/** 
 **************************************************************************************
 * @brief 主に[設備管理]機能で使用する、全体の処理を総合的に実施するサービスに実装する機能を
 * 提供するインターフェース
 * 
 * @details 
 * - このインターフェースは、実装クラスのDIの際にデータ型として登録できるように設ける
 * ものである。
 * - このインターフェース内に実装するメソッドは特にない。すべてスーパーインターフェースに
 * 実装されているものである。
 * 
 * @see NormalService
 * @see Facility
 * @see Facility_History
 * @see Facility_Form
 **************************************************************************************
 */ 
public interface Facility_Service 
    extends NormalService<Facility, Facility_History, Facility_Form>{}