/** 
 **************************************************************************************
 * @file Member_Info.java
 * @brief 主に[団員情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティを格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Entity.NormalEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.NormalEnum.Member_Info_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.DateFormat_Enum;
import com.springproject.dockerspring.Entity.EntitySetUp;
import com.springproject.dockerspring.Entity.PhotoBase64Encode;
import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Form.CsvImplForm.Member_Info_Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;








/** 
 **************************************************************************************
 * @brief 主に[団員情報]機能のデータの、データベース保存やデータのやり取りに使用する
 * エンティティである。
 * 
 * @details 
 * - このエンティティが対応するデータベース内のテーブルの名称は[Member_Info]である。
 * - 定義しているフィールド変数に関しては、対応するデータベース内のテーブルに対応した名称である。
 * 
 * @par 使用アノテーション
 * - @AllArgsConstructor
 * - @Data
 * - @NoArgsConstructor
 * - @@EqualsAndHashCode(callSuper = false)
 * - @Table("Member_Info")
 * 
 * @see PhotoBase64Encode
 * @see EntitySetUp
 * @see Member_Info_Form
 * @see Member_Info_History
 * @see Member_Info_Enum
 * @see DateFormat_Enum
 **************************************************************************************
 */ 
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table("Member_Info")
public class Member_Info implements EntitySetUp{

  @Id
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
   * @brief 対になっているフォームクラスから、この通常データ用エンティティにデータを移し替える。
   * 
   * @param[in] form_clazz この通常データ用エンティティと対となる、フォームクラス
   * 
   * @par 処理の大まかな流れ
   * -# フォームクラスからデータを抽出し、この通常データ用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see Member_Info_Form
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public Member_Info(Member_Info_Form form_clazz) throws IOException{

    this.serial_num = form_clazz.getSerial_num();
    this.member_id = form_clazz.getMember_id();
    this.name = form_clazz.getName();
    this.name_pronu = form_clazz.getName_pronu();
    this.sex = form_clazz.getSex();
    this.birthday = form_clazz.getBirthday();

    MultipartFile tmp = form_clazz.getFace_photo();
    try{
      this.face_photo = tmp == null ? null : tmp.getBytes();
    }catch(IOException e){
      throw new IOException("Error location [Member_Info:constructor1]" + "\n" + e);
    }
    
    this.join_date = form_clazz.getJoin_date();
    this.ret_date = form_clazz.getRet_date();
    this.email_1 = form_clazz.getEmail_1();
    this.email_2 = form_clazz.getEmail_2();
    this.tel_1 = form_clazz.getTel_1();
    this.tel_2 = form_clazz.getTel_2();
    this.addr_postcode = form_clazz.getAddr_postcode();
    this.addr = form_clazz.getAddr();
    this.position = form_clazz.getPosition();
    this.position_arri_date = form_clazz.getPosition_arri_date();
    this.job = form_clazz.getJob();
    this.assign_dept = form_clazz.getAssign_dept();
    this.assign_date = form_clazz.getAssign_date();
    this.inst_charge = form_clazz.getInst_charge();
    this.other_comment = form_clazz.getOther_comment();

    stringSetNull();
  }








  /** 
   **********************************************************************************************
   * @brief 対になっている履歴用エンティティから、この通常データ用エンティティにデータを移し替える。
   * 
   * @param[in] hist_entity この通常データ用エンティティと対となる、履歴用エンティティ
   * 
   * @par 処理の大まかな流れ
   * -# 履歴用エンティティからデータを抽出し、この通常データ用エンティティの同名のフィールド変数に格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @see Member_Info_History
   **********************************************************************************************
   */
  public Member_Info(Member_Info_History hist_entity){

    this.serial_num = hist_entity.getSerial_num();
    this.member_id = hist_entity.getMember_id();
    this.name = hist_entity.getName();
    this.name_pronu = hist_entity.getName_pronu();
    this.sex = hist_entity.getSex();
    this.birthday = hist_entity.getBirthday();
    this.face_photo = hist_entity.getFace_photo();
    this.join_date = hist_entity.getJoin_date();
    this.ret_date = hist_entity.getRet_date();
    this.email_1 = hist_entity.getEmail_1();
    this.email_2 = hist_entity.getEmail_2();
    this.tel_1 = hist_entity.getTel_1();
    this.tel_2 = hist_entity.getTel_2();
    this.addr_postcode = hist_entity.getAddr_postcode();
    this.addr = hist_entity.getAddr();
    this.position = hist_entity.getPosition();
    this.position_arri_date = hist_entity.getPosition_arri_date();
    this.job = hist_entity.getJob();
    this.assign_dept = hist_entity.getAssign_dept();
    this.assign_date = hist_entity.getAssign_date();
    this.inst_charge = hist_entity.getInst_charge();
    this.other_comment = hist_entity.getOther_comment();

    stringSetNull();
  }








  /** 
   **********************************************************************************************
   * @brief CSVリストから抽出したデータをマップリストにしたものを、このエンティティに移し替える。
   * 
   * @param[in] csv_map CSVリストから抽出したデータをマップリストにしたもの
   * 
   * @par 処理の大まかな流れ
   * -# CSVマップリストからデータを抽出し、それぞれ対応するデータ型に変換する。
   * -# 変換したデータを、この通常データ用エンティティに格納する。
   * -# 移し替えた後、Null初期化を実行する。
   * 
   * @note
   * - CSVリストから抽出したデータはすべて新規追加扱いになるため、SpringDataJDBCの仕様に伴い
   * シリアルナンバーは「Null」となる。
   * - CSVリストからは画像のバイナリデータは取得できないので、「Null」扱いとなる。
   * - 項目「性別」に関しては、CSVリストから抽出した段階では漢字表記の為、データベースに格納できるように
   * 英語表記に変換している。
   * 
   * @see DateFormat_Enum
   * @see Member_Info_Enum
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public Member_Info(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE_NO_HYPHEN.getFormat());

    try{
      this.serial_num = null;
      this.member_id = csv_map.get(Member_Info_Enum.Member_Id.getEnName());
      this.name = csv_map.get(Member_Info_Enum.Name.getEnName());
      this.name_pronu = csv_map.get(Member_Info_Enum.Name_Pronu.getEnName());

      String tmp = csv_map.get(Member_Info_Enum.Sex.getEnName());
      if(tmp != null && tmp.equals("男")){
        this.sex = "male";
      }else if(tmp != null && tmp.equals("女")){
        this.sex = "female";
      }else{
        this.sex = null;
      }

      tmp = csv_map.get(Member_Info_Enum.Birthday.getEnName());
      this.birthday = tmp == null ? null : parse_date.parse(tmp);

      this.face_photo = null;

      tmp = csv_map.get(Member_Info_Enum.Join_Date.getEnName());
      this.join_date = tmp == null ? null : parse_date.parse(tmp);

      tmp = csv_map.get(Member_Info_Enum.Ret_Date.getEnName());
      this.ret_date = tmp == null ? null : parse_date.parse(tmp);

      this.email_1 = csv_map.get(Member_Info_Enum.Email_1.getEnName());
      this.email_2 = csv_map.get(Member_Info_Enum.Email_2.getEnName());
      this.tel_1 = csv_map.get(Member_Info_Enum.Tel_1.getEnName());
      this.tel_2 = csv_map.get(Member_Info_Enum.Tel_2.getEnName());
      this.addr_postcode = csv_map.get(Member_Info_Enum.Addr_Postcode.getEnName());
      this.addr = csv_map.get(Member_Info_Enum.Addr.getEnName());
      this.position = csv_map.get(Member_Info_Enum.Position.getEnName());

      tmp = csv_map.get(Member_Info_Enum.Position_Arri_Date.getEnName());
      this.position_arri_date = tmp == null ? null : parse_date.parse(tmp);

      this.job = csv_map.get(Member_Info_Enum.Job.getEnName());
      this.assign_dept = csv_map.get(Member_Info_Enum.Assign_Dept.getEnName());

      tmp = csv_map.get(Member_Info_Enum.Assign_Date.getEnName());
      this.assign_date = tmp == null ? null : parse_date.parse(tmp);
      
      this.inst_charge = csv_map.get(Member_Info_Enum.Inst_Charge.getEnName());
      this.other_comment = csv_map.get(Member_Info_Enum.Other_Comment.getEnName());

    }catch(ParseException e){
      throw new ParseException("Error location [Member_Info:constructor3]" + "\n" + e, e.getErrorOffset());
    }

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
   * @see Member_Info_Enum
   **********************************************************************************************
   */
  @Override
  public Map<String, String> makeMap() {
    Map<String, String> convert_map = new HashMap<>();
    SimpleDateFormat parse_date = new SimpleDateFormat(DateFormat_Enum.DATE.getFormat());    

    stringSetNull();

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