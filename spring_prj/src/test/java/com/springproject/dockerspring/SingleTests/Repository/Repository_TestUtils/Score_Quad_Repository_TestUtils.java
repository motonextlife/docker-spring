/** 
 **************************************************************************************
 * @file Score_Quad_Repository_TestUtils.java
 * @brief 主に[楽譜管理][楽譜情報変更履歴][楽譜データ管理][楽譜データ変更履歴]、機能のリポジトリの
 * テストで用いる、テスト全般に用いることができるユーティリティクラスを格納したファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.Repository.Repository_TestUtils;

import com.springproject.dockerspring.Entity.HistoryEntity.Musical_Score_History;
import com.springproject.dockerspring.Entity.HistoryEntity.Score_Pdf_History;
import com.springproject.dockerspring.Entity.NormalEntity.Musical_Score;
import com.springproject.dockerspring.Entity.NormalEntity.Score_Pdf;

import org.assertj.core.api.SoftAssertions;











/** 
 **************************************************************************************
 * @brief 主に[楽譜管理][楽譜情報変更履歴][楽譜データ管理][楽譜データ変更履歴]、機能のリポジトリの
 * テストで用いる、テスト全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 **************************************************************************************
 */ 
public class Score_Quad_Repository_TestUtils{

  private Score_Quad_Repository_TestUtils(){
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
   * @see Musical_Score
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Musical_Score compare, Musical_Score result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getScore_id()).isEqualTo(compare.getScore_id());
    softly.assertThat(result.getBuy_date()).isEqualTo(compare.getBuy_date());
    softly.assertThat(result.getSong_title()).isEqualTo(compare.getSong_title());
    softly.assertThat(result.getComposer()).isEqualTo(compare.getComposer());
    softly.assertThat(result.getArranger()).isEqualTo(compare.getArranger());
    softly.assertThat(result.getPublisher()).isEqualTo(compare.getPublisher());
    softly.assertThat(result.getStorage_loc()).isEqualTo(compare.getStorage_loc());
    softly.assertThat(result.getDisp_date()).isEqualTo(compare.getDisp_date());
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
   * @see Score_Pdf
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Score_Pdf compare, Score_Pdf result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    }
    
    softly.assertThat(result.getScore_id()).isEqualTo(compare.getScore_id());
    softly.assertThat(result.getScore_name()).isEqualTo(compare.getScore_name());
    softly.assertThat(result.getPdf_hash()).isEqualTo(compare.getPdf_hash());
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
   * @see Musical_Score_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Musical_Score_History compare, Musical_Score_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getScore_id()).isEqualTo(compare.getScore_id());
    softly.assertThat(result.getBuy_date()).isEqualTo(compare.getBuy_date());
    softly.assertThat(result.getSong_title()).isEqualTo(compare.getSong_title());
    softly.assertThat(result.getComposer()).isEqualTo(compare.getComposer());
    softly.assertThat(result.getArranger()).isEqualTo(compare.getArranger());
    softly.assertThat(result.getPublisher()).isEqualTo(compare.getPublisher());
    softly.assertThat(result.getStorage_loc()).isEqualTo(compare.getStorage_loc());
    softly.assertThat(result.getDisp_date()).isEqualTo(compare.getDisp_date());
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
   * @see Score_Pdf_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの比較の際に用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Score_Pdf_History compare, Score_Pdf_History result, SoftAssertions softly, Boolean fixed){

    if(fixed){
      softly.assertThat(result.getHistory_id()).isEqualTo(compare.getHistory_id());
    }
    
    softly.assertThat(result.getChange_datetime()).isEqualTo(compare.getChange_datetime());
    softly.assertThat(result.getChange_kinds()).isEqualTo(compare.getChange_kinds());
    softly.assertThat(result.getOperation_user()).isEqualTo(compare.getOperation_user());
    softly.assertThat(result.getSerial_num()).isEqualTo(compare.getSerial_num());
    softly.assertThat(result.getScore_id()).isEqualTo(compare.getScore_id());
    softly.assertThat(result.getScore_name()).isEqualTo(compare.getScore_name());
    softly.assertThat(result.getPdf_hash()).isEqualTo(compare.getPdf_hash());
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
   * @see Musical_Score
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Musical_Score MockToReal(Musical_Score mock){

    Musical_Score real_ent = new Musical_Score();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setScore_id(mock.getScore_id());
    real_ent.setBuy_date(mock.getBuy_date());
    real_ent.setSong_title(mock.getSong_title());
    real_ent.setComposer(mock.getComposer());
    real_ent.setArranger(mock.getArranger());
    real_ent.setPublisher(mock.getPublisher());
    real_ent.setStorage_loc(mock.getStorage_loc());
    real_ent.setDisp_date(mock.getDisp_date());
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
   * @see Score_Pdf
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの通常データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Score_Pdf MockToReal(Score_Pdf mock){

    Score_Pdf real_ent = new Score_Pdf();

    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setScore_id(mock.getScore_id());
    real_ent.setScore_name(mock.getScore_name());
    real_ent.setPdf_hash(mock.getPdf_hash());
    
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
   * @see Musical_Score_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にテキストデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Musical_Score_History MockToReal(Musical_Score_History mock){

    Musical_Score_History real_ent = new Musical_Score_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setScore_id(mock.getScore_id());
    real_ent.setBuy_date(mock.getBuy_date());
    real_ent.setSong_title(mock.getSong_title());
    real_ent.setComposer(mock.getComposer());
    real_ent.setArranger(mock.getArranger());
    real_ent.setPublisher(mock.getPublisher());
    real_ent.setStorage_loc(mock.getStorage_loc());
    real_ent.setDisp_date(mock.getDisp_date());
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
   * @see Score_Pdf_History
   * 
   * @note 同名のメソッドが複数があるが、こちらは主にバイナリデータの履歴データの移し替えの際に用いる。
   **********************************************************************************************
   */
  public static Score_Pdf_History MockToReal(Score_Pdf_History mock){

    Score_Pdf_History real_ent = new Score_Pdf_History();

    real_ent.setHistory_id(mock.getHistory_id());
    real_ent.setChange_datetime(mock.getChange_datetime());
    real_ent.setChange_kinds(mock.getChange_kinds());
    real_ent.setOperation_user(mock.getOperation_user());
    real_ent.setSerial_num(mock.getSerial_num());
    real_ent.setScore_id(mock.getScore_id());
    real_ent.setScore_name(mock.getScore_name());
    real_ent.setPdf_hash(mock.getPdf_hash());
    
    return real_ent;
  }
}
