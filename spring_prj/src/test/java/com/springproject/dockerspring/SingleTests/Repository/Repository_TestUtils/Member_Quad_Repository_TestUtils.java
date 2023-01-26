/** 
 **************************************************************************************
 * @file Member_Quad_Repository_TestUtils.java
 * @brief 主に[団員管理][団員情報変更履歴][システムユーザー管理][権限管理]、機能のリポジトリのテスト
 * で用いる、テスト全般に用いることができるユーティリティクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import com.springproject.dockerspring.Entity.HistoryEntity.Member_Info_History;
import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.Entity.NormalEntity.System_User;
import com.springproject.dockerspring.Entity.NormalEntity.Usage_Authority;

import org.assertj.core.api.SoftAssertions;












/** 
 **************************************************************************************
 * @brief 主に[団員管理][団員情報変更履歴][システムユーザー管理][権限管理]、機能のリポジトリのテスト
 * で用いる、テスト全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 **************************************************************************************
 */ 
public class Member_Quad_Repository_TestUtils{

  private Member_Quad_Repository_TestUtils(){
    throw new AssertionError();
  }










  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see Member_Info
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に団員管理に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Member_Info compare, Member_Info result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getMember_id()).isEqualTo(compare.getMember_id());
    softly.assertThat(result.getName()).isEqualTo(compare.getName());
    softly.assertThat(result.getName_pronu()).isEqualTo(compare.getName_pronu());
    softly.assertThat(result.getSex()).isEqualTo(compare.getSex());
    softly.assertThat(result.getBirthday()).isEqualTo(compare.getBirthday());
    softly.assertThat(result.getFace_photo()).isEqualTo(compare.getFace_photo());
    softly.assertThat(result.getJoin_date()).isEqualTo(compare.getJoin_date());
    softly.assertThat(result.getRet_date()).isEqualTo(compare.getRet_date());
    softly.assertThat(result.getEmail_1()).isEqualTo(compare.getEmail_1());
    softly.assertThat(result.getEmail_2()).isEqualTo(compare.getEmail_2());
    softly.assertThat(result.getTel_1()).isEqualTo(compare.getTel_1());
    softly.assertThat(result.getTel_2()).isEqualTo(compare.getTel_2());
    softly.assertThat(result.getAddr_postcode()).isEqualTo(compare.getAddr_postcode());
    softly.assertThat(result.getAddr()).isEqualTo(compare.getAddr());
    softly.assertThat(result.getPosition()).isEqualTo(compare.getPosition());
    softly.assertThat(result.getPosition_arri_date()).isEqualTo(compare.getPosition_arri_date());
    softly.assertThat(result.getJob()).isEqualTo(compare.getJob());
    softly.assertThat(result.getAssign_dept()).isEqualTo(compare.getAssign_dept());
    softly.assertThat(result.getAssign_date()).isEqualTo(compare.getAssign_date());
    softly.assertThat(result.getInst_charge()).isEqualTo(compare.getInst_charge());
    softly.assertThat(result.getOther_comment()).isEqualTo(compare.getOther_comment());
  }











  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed 履歴番号が定まっている場合は「True」、新規追加などで番号が定まってない場合は「False」
   * とする。
   * 
   * @see Member_Info_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に団員情報変更履歴に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Member_Info_History compare, Member_Info_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getMember_id()).isEqualTo(compare.getMember_id());
    softly.assertThat(result.getName()).isEqualTo(compare.getName());
    softly.assertThat(result.getName_pronu()).isEqualTo(compare.getName_pronu());
    softly.assertThat(result.getSex()).isEqualTo(compare.getSex());
    softly.assertThat(result.getBirthday()).isEqualTo(compare.getBirthday());
    softly.assertThat(result.getFace_photo()).isEqualTo(compare.getFace_photo());
    softly.assertThat(result.getJoin_date()).isEqualTo(compare.getJoin_date());
    softly.assertThat(result.getRet_date()).isEqualTo(compare.getRet_date());
    softly.assertThat(result.getEmail_1()).isEqualTo(compare.getEmail_1());
    softly.assertThat(result.getEmail_2()).isEqualTo(compare.getEmail_2());
    softly.assertThat(result.getTel_1()).isEqualTo(compare.getTel_1());
    softly.assertThat(result.getTel_2()).isEqualTo(compare.getTel_2());
    softly.assertThat(result.getAddr_postcode()).isEqualTo(compare.getAddr_postcode());
    softly.assertThat(result.getAddr()).isEqualTo(compare.getAddr());
    softly.assertThat(result.getPosition()).isEqualTo(compare.getPosition());
    softly.assertThat(result.getPosition_arri_date()).isEqualTo(compare.getPosition_arri_date());
    softly.assertThat(result.getJob()).isEqualTo(compare.getJob());
    softly.assertThat(result.getAssign_dept()).isEqualTo(compare.getAssign_dept());
    softly.assertThat(result.getAssign_date()).isEqualTo(compare.getAssign_date());
    softly.assertThat(result.getInst_charge()).isEqualTo(compare.getInst_charge());
    softly.assertThat(result.getOther_comment()).isEqualTo(compare.getOther_comment());
  }










  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see System_User
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にシステムユーザー管理に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(System_User compare, System_User result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getMember_id()).isEqualTo(compare.getMember_id());
    softly.assertThat(result.getUsername()).isEqualTo(compare.getUsername());
    softly.assertThat(result.getPassword()).isEqualTo(compare.getPassword());
    softly.assertThat(result.getAuth_id()).isEqualTo(compare.getAuth_id());
    softly.assertThat(result.getFail_count()).isEqualTo(compare.getFail_count());
    softly.assertThat(result.getLocking()).isEqualTo(compare.getLocking());
  }










  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * @param[in] fixed シリアルナンバーが定まっている場合は「True」、新規追加などで番号が定まってない場合は
   * 「False」とする。
   * 
   * @see Usage_Authority
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に権限管理に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Usage_Authority compare, Usage_Authority result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getAuth_id()).isEqualTo(compare.getAuth_id());
    softly.assertThat(result.getAuth_name()).isEqualTo(compare.getAuth_name());
    softly.assertThat(result.getAdmin()).isEqualTo(compare.getAdmin());
    softly.assertThat(result.getMember_info()).isEqualTo(compare.getMember_info());
    softly.assertThat(result.getFacility()).isEqualTo(compare.getFacility());
    softly.assertThat(result.getMusical_score()).isEqualTo(compare.getMusical_score());
    softly.assertThat(result.getSound_source()).isEqualTo(compare.getSound_source());
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Member_Info
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に団員管理に用いる。
   **********************************************************************************************
   */
  public static Member_Info MockToReal(Member_Info mock){

    Member_Info real_ent = new Member_Info();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setMember_id(mock.getMember_id());
    real_ent.setName(mock.getName());
    real_ent.setName_pronu(mock.getName_pronu());
    real_ent.setSex(mock.getSex());
    real_ent.setBirthday(mock.getBirthday());
    real_ent.setFace_photo(mock.getFace_photo());
    real_ent.setJoin_date(mock.getJoin_date());
    real_ent.setRet_date(mock.getRet_date());
    real_ent.setEmail_1(mock.getEmail_1());
    real_ent.setEmail_2(mock.getEmail_2());
    real_ent.setTel_1(mock.getTel_1());
    real_ent.setTel_2(mock.getTel_2());
    real_ent.setAddr_postcode(mock.getAddr_postcode());
    real_ent.setAddr(mock.getAddr());
    real_ent.setPosition(mock.getPosition());
    real_ent.setPosition_arri_date(mock.getPosition_arri_date());
    real_ent.setJob(mock.getJob());
    real_ent.setAssign_dept(mock.getAssign_dept());
    real_ent.setAssign_date(mock.getAssign_date());
    real_ent.setInst_charge(mock.getInst_charge());
    real_ent.setOther_comment(mock.getOther_comment());
    
    return real_ent;
  }









  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Member_Info_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に団員情報変更履歴に用いる。
   **********************************************************************************************
   */
  public static Member_Info_History MockToReal(Member_Info_History mock){

    Member_Info_History real_ent = new Member_Info_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setMember_id(mock.getMember_id());
    real_ent.setName(mock.getName());
    real_ent.setName_pronu(mock.getName_pronu());
    real_ent.setSex(mock.getSex());
    real_ent.setBirthday(mock.getBirthday());
    real_ent.setFace_photo(mock.getFace_photo());
    real_ent.setJoin_date(mock.getJoin_date());
    real_ent.setRet_date(mock.getRet_date());
    real_ent.setEmail_1(mock.getEmail_1());
    real_ent.setEmail_2(mock.getEmail_2());
    real_ent.setTel_1(mock.getTel_1());
    real_ent.setTel_2(mock.getTel_2());
    real_ent.setAddr_postcode(mock.getAddr_postcode());
    real_ent.setAddr(mock.getAddr());
    real_ent.setPosition(mock.getPosition());
    real_ent.setPosition_arri_date(mock.getPosition_arri_date());
    real_ent.setJob(mock.getJob());
    real_ent.setAssign_dept(mock.getAssign_dept());
    real_ent.setAssign_date(mock.getAssign_date());
    real_ent.setInst_charge(mock.getInst_charge());
    real_ent.setOther_comment(mock.getOther_comment());
    
    return real_ent;
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see System_User
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にシステムユーザー管理に用いる。
   **********************************************************************************************
   */
  public static System_User MockToReal(System_User mock){

    System_User real_ent = new System_User();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setMember_id(mock.getMember_id());
    real_ent.setUsername(mock.getUsername());
    real_ent.setPassword(mock.getPassword());
    real_ent.setAuth_id(mock.getAuth_id());
    real_ent.setFail_count(mock.getFail_count());
    real_ent.setLocking(mock.getLocking());
    
    return real_ent;
  }










  /** 
   **********************************************************************************************
   * @brief テストケースから取得してきたモックエンティティのデータを、リポジトリで使用する本物のエンティティに
   * 格納しなおす。
   * 
   * @return 移し替えた本物のエンティティ
   *
   * @param[in] mock 移し替えの対象になるモック
   * 
   * @see Usage_Authority
   * 
   * @note 同名のメソッドが複数があるが、こちらは主に権限管理に用いる。
   **********************************************************************************************
   */
  public static Usage_Authority MockToReal(Usage_Authority mock){

    Usage_Authority real_ent = new Usage_Authority();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setAuth_id(mock.getAuth_id());
    real_ent.setAuth_name(mock.getAuth_name());
    real_ent.setAdmin(mock.getAdmin());
    real_ent.setMember_info(mock.getMember_info());
    real_ent.setFacility(mock.getFacility());
    real_ent.setMusical_score(mock.getMusical_score());
    real_ent.setSound_source(mock.getSound_source());
    
    return real_ent;
  }
}
