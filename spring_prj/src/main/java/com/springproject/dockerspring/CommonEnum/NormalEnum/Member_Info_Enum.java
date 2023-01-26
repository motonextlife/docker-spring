/** 
 **************************************************************************************
 * @file Member_Info_Enum.java
 * @brief 主に[団員情報管理]機能において使用する列挙型を格納したファイルである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.CommonEnum.NormalEnum;




/** 
 **************************************************************************************
 * @brief 主に[団員情報管理]機能において使用する列挙型である。
 * @details 
 * - 列挙型に定義する内容としては、「日本語でのデータ項目名」「英語でのデータ項目名」を定義する。
 * - 日本語項目名はCSVファイルの出力の際のヘッダー名に使用する。
 * - 英語項目名に関してはマップリストのキー名の定義に用いる。
 **************************************************************************************
 */ 
public enum Member_Info_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Member_Id("団員番号", "member_id"),
  Name("名前", "name"),
  Name_Pronu("ふりがな", "name_pronu"),
  Sex("性別", "sex"),
  Birthday("誕生日", "birthday"),
  Face_Photo("顔写真", "face_photo"),
  Join_Date("入団日", "join_date"),
  Ret_Date("退団日", "ret_date"),
  Email_1("メールアドレス１", "email_1"),
  Email_2("メールアドレス２", "email_2"),
  Tel_1("電話番号１", "tel_1"),
  Tel_2("電話番号２", "tel_2"),
  Addr_Postcode("現住所郵便番号", "addr_postcode"),
  Addr("現住所", "addr"),
  Position("役職名", "position"),
  Position_Arri_Date("現役職着任日", "position_arri_date"),
  Job("職種名", "job"),
  Assign_Dept("配属部署", "assign_dept"),
  Assign_Date("配属日", "assign_date"),
  Inst_Charge("担当楽器", "inst_charge"),
  Other_Comment("その他コメント", "other_comment");


  //! 日本語項目名の格納変数
  private final String ja_name;

  //! 英語項目名の格納変数
  private final String en_name;


  /** 
   **********************************************************
   * @brief 列挙型の定義値をセットする。
   * @param[in] ja_name 項目の日本語名をセットする。
   * @param[in] en_name 項目の英語名をセットする。
   **********************************************************
   */ 
  private Member_Info_Enum(String ja_name, String en_name){
    this.ja_name = ja_name;
    this.en_name = en_name;
  }


  /** 
   **********************************************************
   * @brief 各列挙型項目の日本語項目名を取得する。
   **********************************************************
   */ 
  public String getJaName() {
    return this.ja_name;
  }


  /** 
   **********************************************************
   * @brief 各列挙型項目の英語項目名を取得する。
   **********************************************************
   */ 
  public String getEnName() {
    return this.en_name;
  }
}
