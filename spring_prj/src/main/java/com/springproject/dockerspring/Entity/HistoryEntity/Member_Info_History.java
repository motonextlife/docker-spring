/** 
 **************************************************************************************
 * @file Member_Info_History.java
 * @brief 主に[団員情報変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.HistoryEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.springproject.dockerspring.CommonEnum.NormalEnum.History_Common_Enum;
import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.History_Kind_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.PhotoBase64Encode;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[団員情報変更履歴]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Member_Info_History]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @EqualsAndHashCode(callSuper = false)
 * - @Table("Member_Info_History")
 * 
 * @see PhotoBase64Encode
 * @see EntitySetUp
 * @see Member_Info
 * @see History_Kind_Enum
 * @see DateFormat_Enum
 * @see History_Common_Enum
 * @see Member_Info_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table("Member_Info_History")
public class Member_Info_History implements EntitySetUp{

  //! 履歴番号(プライマリ)
  @Id
  private Integer history_id;  

  //! 履歴記録日時
  private Date change_datetime;

  //! 履歴種別
  private String change_kinds;

  //! 操作ユーザー名
  private String operation_user;

  //! シリアルナンバー
  private Integer serial_num;
  
  //! 団員番号
  private String member_id;
  
  //! 名前
  private String name;
  
  //! ふりがな
  private String name_pronu;
  
  //! 性別
  private String sex;
  
  //! 誕生日
  private Date birthday;
  
  //! 顔写真
  private byte[] face_photo;
  
  //! 入団日
  private Date join_date;
  
  //! 退団日
  private Date ret_date;
  
  //! メールアドレス１
  private String email_1;
  
  //! メールアドレス２
  private String email_2;
  
  //! 電話番号１
  private String tel_1;
  
  //! 電話番号２
  private String tel_2;
  
  //! 現住所郵便番号
  private String addr_postcode;
  
  //! 現住所
  private String addr;
  
  //! 役職名
  private String position;
  
  //! 現役職着任日
  private Date position_arri_date;
  
  //! 職種名
  private String job;
  
  //! 配属部署
  private String assign_dept;
  
  //! 配属日
  private Date assign_date;
  
  //! 担当楽器
  private String inst_charge;
  
  //! その他コメント
  private String other_comment;
  





  



  /** 
   **********************************************************************************************
   * @brief 対になっている通常データ用のエンティティから、この履歴用エンティティにデータを移し替える。
   * 
   * @details　移し替える際には、以下の情報を一緒に保存する。
   * - データベースに保存する履歴の種別
   * - 変更操作を行ったユーザーの名称
   * - 変更が行われた日時
   * 
   * @param[in] normal_entity この履歴用エンティティと対となる、通常データ用エンティティ
   * @param[in] hist_kind 履歴保存時の変更種別（新規追加、更新、削除など）
   * @param[in] username 変更操作を行ったユーザーの名前
   * @param[in] datetime 変更が行われた日時
   * 
   * @par 処理の大まかな流れ
   * -# 通常データ用エンティティからデータを抽出し、この履歴用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see History_Kind_Enum
   * @see Member_Info
   **********************************************************************************************
   */
  public Member_Info_History(Member_Info info, History_Kind_Enum hist_kind, String username, Date datetime){

    this.history_id = null;
    this.change_datetime = datetime;
    this.change_kinds = hist_kind == null ? null : hist_kind.getKinds();
    this.operation_user = username;

    this.serial_num = info.getSerial_num();
    this.member_id = info.getMember_id();
    this.name = info.getName();
    this.name_pronu = info.getName_pronu();
    this.sex = info.getSex();
    this.birthday = info.getBirthday();
    this.face_photo = info.getFace_photo();
    this.join_date = info.getJoin_date();
    this.ret_date = info.getRet_date();
    this.email_1 = info.getEmail_1();
    this.email_2 = info.getEmail_2();
    this.tel_1 = info.getTel_1();
    this.tel_2 = info.getTel_2();
    this.addr_postcode = info.getAddr_postcode();
    this.addr = info.getAddr();
    this.position = info.getPosition();
    this.position_arri_date = info.getPosition_arri_date();
    this.job = info.getJob();
    this.assign_dept = info.getAssign_dept();
    this.assign_date = info.getAssign_date();
    this.inst_charge = info.getInst_charge();
    this.other_comment = info.getOther_comment();

    stringSetNull();
  }




  



  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータのうち、文字列型かつ空文字や空白のデータをNullに初期化する。
   * 
   * @details このメソッドは以下の理由で設けた。
   * - 空文字や空白のままデータベースに保存されると、他のテーブルとの参照制約に悪影響を及ぼすため。
   * 
   * @par 処理の大まかな流れ
   * -# 各フィールド変数のうち、文字列型の物のみを対象とする。
   * -# フィールド内に格納されている文字列が「空白」又は「空文字」か判別する。
   * -# 該当すれば、内容を「Null」に置き換える。
   **********************************************************************************************
   */
  @Override
  public void stringSetNull(){

    if(this.change_kinds == null || this.change_kinds.isEmpty() || this.change_kinds.isBlank()){
      this.change_kinds = null;
    }

    if(this.operation_user == null || this.operation_user.isEmpty() || this.operation_user.isBlank()){
      this.operation_user = null;
    }

    if(this.member_id == null || this.member_id.isEmpty() || this.member_id.isBlank()){
      this.member_id = null;
    }

    if(this.name == null || this.name.isEmpty() || this.name.isBlank()){
      this.name = null;
    }

    if(this.name_pronu == null || this.name_pronu.isEmpty() || this.name_pronu.isBlank()){
      this.name_pronu = null;
    }

    if(this.sex == null || this.sex.isEmpty() || this.sex.isBlank()){
      this.sex = null;
    }

    if(this.email_1 == null || this.email_1.isEmpty() || this.email_1.isBlank()){
      this.email_1 = null;
    }

    if(this.email_2 == null || this.email_2.isEmpty() || this.email_2.isBlank()){
      this.email_2 = null;
    }

    if(this.tel_1 == null || this.tel_1.isEmpty() || this.tel_1.isBlank()){
      this.tel_1 = null;
    }

    if(this.tel_2 == null || this.tel_2.isEmpty() || this.tel_2.isBlank()){
      this.tel_2 = null;
    }

    if(this.addr_postcode == null || this.addr_postcode.isEmpty() || this.addr_postcode.isBlank()){
      this.addr_postcode = null;
    }

    if(this.addr == null || this.addr.isEmpty() || this.addr.isBlank()){
      this.addr = null;
    }

    if(this.position == null || this.position.isEmpty() || this.position.isBlank()){
      this.position = null;
    }

    if(this.job == null || this.job.isEmpty() || this.job.isBlank()){
      this.job = null;
    }

    if(this.assign_dept == null || this.assign_dept.isEmpty() || this.assign_dept.isBlank()){
      this.assign_dept = null;
    }

    if(this.inst_charge == null || this.inst_charge.isEmpty() || this.inst_charge.isBlank()){
      this.inst_charge = null;
    }

    if(this.other_comment == null || this.other_comment.isEmpty() || this.other_comment.isBlank()){
      this.other_comment = null;
    }
  }







  /** 
   **********************************************************************************************
   * @brief エンティティ内に格納されているデータを全て文字列型に変換し、マップリストへ格納して返却する。
   * 
   * @details
   * - 主に、フロント側の画面への出力の際に用いるデータを作成する目的として、このメソッドを使用する。
   * - 「Null」が入っているデータは、全て「空文字」に変換される。
   * 
   * @return 文字列に変換が終わり、キー名を項目の英語名としたマップリスト。
   * 
   * @par 処理の大まかな流れ
   * -# 格納済みのデータを全てNull初期化する。
   * -# フィールドに格納されているデータが「Null」であれば、例外なく「空文字」をマップリストに登録。
   * -# 文字列型データはそのままマップリストに登録し、それ以外のデータ型は文字列型に変換して登録。
   * -# 画像データに関しては、継承元クラスにあるBase64変換処理を用いてBase64文字列に変換して登録。
   * 
   * @see DateFormat_Enum
   * @see History_Common_Enum
   * @see Member_Info_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());
    SimpleDateFormat parse_datetime = new SimpleDateFormat(DateFormat_Enum.DATETIME.getFormat());

    stringSetNull();

    convert_map.put(History_Common_Enum.History_Id.getEnName(),
              			this.history_id == null ? "" : String.valueOf(this.history_id));
              
    convert_map.put(History_Common_Enum.Change_Datetime.getEnName(),
              			this.change_datetime == null ? "" : parse_datetime.format(this.change_datetime));
              
    convert_map.put(History_Common_Enum.Change_Kinds.getEnName(),
              			this.change_kinds == null ? "" : this.change_kinds);

    convert_map.put(History_Common_Enum.Operation_User.getEnName(),
              			this.operation_user == null ? "" : this.operation_user);
              
    convert_map.put(Member_Info_Enum.Serial_Num.getEnName(), 
              			this.serial_num == null ? "" : String.valueOf(this.serial_num));
              
    convert_map.put(Member_Info_Enum.Member_Id.getEnName(), 
              			this.member_id == null ? "" : this.member_id);
              
    convert_map.put(Member_Info_Enum.Name.getEnName(), 
              			this.name == null ? "" : this.name);
              
    convert_map.put(Member_Info_Enum.Name_Pronu.getEnName(), 
              			this.name_pronu == null ? "" : this.name_pronu);
              
    convert_map.put(Member_Info_Enum.Sex.getEnName(), 
              			this.sex == null ? "" : this.sex);
              
    convert_map.put(Member_Info_Enum.Birthday.getEnName(), 
              			this.birthday == null ? "" : parse_date.format(this.birthday));
              
    convert_map.put(Member_Info_Enum.Face_Photo.getEnName(), 
              			this.face_photo == null ? "" : PhotoBase64Encode.encode(this.face_photo));
              
    convert_map.put(Member_Info_Enum.Join_Date.getEnName(), 
              			this.join_date == null ? "" : parse_date.format(this.join_date));
              
    convert_map.put(Member_Info_Enum.Ret_Date.getEnName(), 
              			this.ret_date == null ? "" : parse_date.format(this.ret_date));
              
    convert_map.put(Member_Info_Enum.Email_1.getEnName(), 
              			this.email_1 == null ? "" : this.email_1);
              
    convert_map.put(Member_Info_Enum.Email_2.getEnName(), 
              			this.email_2 == null ? "" : this.email_2);
              
    convert_map.put(Member_Info_Enum.Tel_1.getEnName(), 
              			this.tel_1 == null ? "" : this.tel_1);
              
    convert_map.put(Member_Info_Enum.Tel_2.getEnName(), 
              			this.tel_2 == null ? "" : this.tel_2);
              
    convert_map.put(Member_Info_Enum.Addr_Postcode.getEnName(), 
              			this.addr_postcode == null ? "" : this.addr_postcode);
              
    convert_map.put(Member_Info_Enum.Addr.getEnName(), 
              			this.addr == null ? "" : this.addr);
              
    convert_map.put(Member_Info_Enum.Position.getEnName(), 
              			this.position == null ? "" : this.position);
              
    convert_map.put(Member_Info_Enum.Position_Arri_Date.getEnName(), 
              			this.position_arri_date == null ? "" : parse_date.format(this.position_arri_date));
              
    convert_map.put(Member_Info_Enum.Job.getEnName(), 
              			this.job == null ? "" : this.job);
              
    convert_map.put(Member_Info_Enum.Assign_Dept.getEnName(), 
              			this.assign_dept == null ? "" : this.assign_dept);
              
    convert_map.put(Member_Info_Enum.Assign_Date.getEnName(), 
              			this.assign_date == null ? "" : parse_date.format(this.assign_date));
              
    convert_map.put(Member_Info_Enum.Inst_Charge.getEnName(), 
              			this.inst_charge == null ? "" : this.inst_charge);
              
    convert_map.put(Member_Info_Enum.Other_Comment.getEnName(), 
              			this.other_comment == null ? "" : this.other_comment);

    return convert_map;
  }
}