package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Member_Info_Enum]
/*   主に「Member_Info」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
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

  private final String text;
  private final String key;

  private Member_Info_Enum(String text, String key){
    this.text = text;
    this.key = key;
  }

  public String getText() {
    return this.text;
  }

  public String getKey() {
    return this.key;
  }
}
