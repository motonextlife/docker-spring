/** 
 **************************************************************************************
 * @file Member_Info_Component_TestUtils.java
 * @brief 主に[団員管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラスを格納したファイル。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、こちらは依存する
 * クラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.SingleTests.FileIO.Component_TestUtils;

import com.springproject.dockerspring.Entity.NormalEntity.Member_Info;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.SoftAssertions;
import org.springframework.core.io.ClassPathResource;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;











/** 
 **************************************************************************************
 * @brief 主に[団員管理]機能の入出力コンポーネントのテストで用いる、テスト
 * 全般に用いることができるユーティリティクラス
 * 
 * @details 
 * - 必要性がないため、インスタンス化を禁止する。
 * - 生成する全ての一時ファイルは、実行権限を剥奪しておく。
 * 
 * @note ここと同名のパッケージが、パッケージ[JoinTests]に存在するが、
 * こちらは依存するクラスをモック化してテストする、単体テストである。
 **************************************************************************************
 */ 
public class Member_Info_Component_TestUtils{

  public static FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                 .fromString("rw-r--r--"));

  private Member_Info_Component_TestUtils(){
    throw new AssertionError();
  }









  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 検査内容としては、シリアルナンバーのみをNullとし、他の値は比較用のエンティティの値と完全一致する
   * 事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   * 
   * @see Member_Info
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public static Member_Info makeMockEntity(Map<String, String> csv_map) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyyMMdd");
    Member_Info mock_ent = mock(Member_Info.class);

    when(mock_ent.getSerial_num()).thenReturn(null);
    when(mock_ent.getMember_id()).thenReturn(csv_map.get("member_id"));
    when(mock_ent.getName()).thenReturn(csv_map.get("name"));
    when(mock_ent.getName_pronu()).thenReturn(csv_map.get("name_pronu"));

    String sex = csv_map.get("sex");
    if(sex.equals("男")){
      when(mock_ent.getSex()).thenReturn("male");
    }else if(sex.equals("女")){
      when(mock_ent.getSex()).thenReturn("female");
    }

    when(mock_ent.getBirthday()).thenReturn(parse_date.parse(csv_map.get("birthday")));
    when(mock_ent.getJoin_date()).thenReturn(parse_date.parse(csv_map.get("join_date")));
    when(mock_ent.getRet_date()).thenReturn(parse_date.parse(csv_map.get("ret_date")));
    when(mock_ent.getEmail_1()).thenReturn(csv_map.get("email_1"));
    when(mock_ent.getEmail_2()).thenReturn(csv_map.get("email_2"));
    when(mock_ent.getTel_1()).thenReturn(csv_map.get("tel_1"));
    when(mock_ent.getTel_2()).thenReturn(csv_map.get("tel_2"));
    when(mock_ent.getAddr_postcode()).thenReturn(csv_map.get("addr_postcode"));
    when(mock_ent.getAddr()).thenReturn(csv_map.get("addr"));
    when(mock_ent.getPosition()).thenReturn(csv_map.get("position"));
    when(mock_ent.getPosition_arri_date()).thenReturn(parse_date.parse(csv_map.get("position_arri_date")));
    when(mock_ent.getJob()).thenReturn(csv_map.get("job"));
    when(mock_ent.getAssign_dept()).thenReturn(csv_map.get("assign_dept"));
    when(mock_ent.getAssign_date()).thenReturn(parse_date.parse(csv_map.get("assign_date")));
    when(mock_ent.getInst_charge()).thenReturn(csv_map.get("inst_charge"));
    when(mock_ent.getOther_comment()).thenReturn(csv_map.get("other_comment"));

    return mock_ent;
  }










  /** 
   **********************************************************************************************
   * @brief 各エンティティに備わっているデータ文字列変換メソッドのモックを作成する。
   * 
   * @details
   * - ここで言うデータ文字列変換メソッドとは、各エンティティの[makeMap]メソッドの事である。
   * - テストケースからモックとして作成されたエンティティ情報を使って、データを文字列に変換した値を返却
   * するモックを作成する。
   *
   * @param[in] mock_entity モックを作成する既存のモックエンティティ
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   * 
   * @see Member_Info
   * 
   * @throw ParseException
   **********************************************************************************************
   */
  public static Member_Info methodMockMakeMap(Member_Info mock_entity) throws ParseException{

    SimpleDateFormat parse_date = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, String> output_mockmap = new HashMap<>();

    Integer serial_num = mock_entity.getSerial_num();
    output_mockmap.put("serial_num", serial_num == null ? "" : String.valueOf(serial_num));
              
    String member_id = mock_entity.getMember_id();
    output_mockmap.put("member_id", member_id == null ? "" : member_id);
              
    String name = mock_entity.getName();
    output_mockmap.put("name", name == null ? "" : name);
              
    String name_pronu = mock_entity.getName_pronu();
    output_mockmap.put("name_pronu", name_pronu == null ? "" : name_pronu);
              
    String sex = mock_entity.getSex();
    output_mockmap.put("sex", sex == null ? "" : sex);
              
    Date birthday = mock_entity.getBirthday();
    output_mockmap.put("birthday", birthday == null ? "" : parse_date.format(birthday));
              
    String base64_str = Base64.getEncoder().encodeToString(mock_entity.getFace_photo());
    output_mockmap.put("face_photo", base64_str == null ? "" : "data:image/png;base64," + base64_str);
              
    Date join_date = mock_entity.getJoin_date();
    output_mockmap.put("join_date", join_date == null ? "" : parse_date.format(join_date));
              
    Date ret_date = mock_entity.getRet_date();
    output_mockmap.put("ret_date", ret_date == null ? "" : parse_date.format(ret_date));
              
    String email_1 = mock_entity.getEmail_1();
    output_mockmap.put("email_1", email_1 == null ? "" : email_1);
              
    String email_2 = mock_entity.getEmail_2();
    output_mockmap.put("email_2", email_2 == null ? "" : email_2);
              
    String tel_1 = mock_entity.getTel_1();
    output_mockmap.put("tel_1", tel_1 == null ? "" : tel_1);
              
    String tel_2 = mock_entity.getTel_2();
    output_mockmap.put("tel_2", tel_2 == null ? "" : tel_2);
              
    String addr_postcode = mock_entity.getAddr_postcode();
    output_mockmap.put("addr_postcode", addr_postcode == null ? "" : addr_postcode);
              
    String addr = mock_entity.getAddr();
    output_mockmap.put("addr", addr == null ? "" : addr);
              
    String position = mock_entity.getPosition();
    output_mockmap.put("position", position == null ? "" : position);
              
    Date position_arri_date = mock_entity.getPosition_arri_date();
    output_mockmap.put("position_arri_date", position_arri_date == null ? "" : parse_date.format(position_arri_date));
              
    String job = mock_entity.getJob();
    output_mockmap.put("job", job == null ? "" : job);
              
    String assign_dept = mock_entity.getAssign_dept();
    output_mockmap.put("assign_dept", assign_dept == null ? "" : assign_dept);
              
    Date assign_date = mock_entity.getAssign_date();
    output_mockmap.put("assign_date", assign_date == null ? "" : parse_date.format(assign_date));
              
    String inst_charge = mock_entity.getInst_charge();
    output_mockmap.put("inst_charge", inst_charge == null ? "" : inst_charge);
              
    String other_comment = mock_entity.getOther_comment();
    output_mockmap.put("other_comment", other_comment == null ? "" : other_comment);

    when(mock_entity.makeMap()).thenReturn(output_mockmap);

    return mock_entity;
  }






  



  /** 
   **********************************************************************************************
   * @brief 比較用の基準エンティティと、テスト結果として出力されたエンティティを比較する。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * - 検査内容としては、シリアルナンバーのみをNullとし、他の値は比較用のエンティティの値と完全一致する
   * 事を確認する。
   *
   * @param[in] compare 比較用エンティティ
   * @param[in] result テスト出力結果エンティティ
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @see Member_Info
   * 
   * @note 主にCSV入出力のテストでこのメソッドを用いる。
   **********************************************************************************************
   */
  public static void assertAllEquals(Member_Info compare, Member_Info result, SoftAssertions softly){
    softly.assertThat(result.getSerial_num()).isNull();
    softly.assertThat(result.getMember_id()).isEqualTo(compare.getMember_id());
    softly.assertThat(result.getName()).isEqualTo(compare.getName());
    softly.assertThat(result.getName_pronu()).isEqualTo(compare.getName_pronu());
    softly.assertThat(result.getSex()).isEqualTo(compare.getSex());
    softly.assertThat(result.getBirthday()).isEqualTo(compare.getBirthday());
    softly.assertThat(result.getFace_photo()).isNull();
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
    softly.assertThat(result.getJob()).isEqualTo(compare.getJob());
  }








  /** 
   **********************************************************************************************
   * @brief テスト時にテスト対象クラスに投入するファイルのコピーを作成する。
   * 
   * @details
   * - このメソッドを設けた理由としては、原本のファイルを用いてテストを行ってしまうと、テスト対象クラスには
   * 使用した一時ファイルの削除機能が備わっているため、原本のデータが消えてしまう為。
   * - それを防ぐために、テスト対象クラスにはコピーしたファイルを投入して検査することで、原本データの削除を
   * 発生させないようにしている。
   * - なお、引数のフラグを指定する事で、コピーファイルではなくそのまま原本ファイルを返すことも可能である。
   * 
   * @return 複製した一時ファイル、又は原本ファイル
   *
   * @param[in] target_func 原本ファイルを取り出す対象の機能名
   * @param[in] target_dir 原本ファイルの入っているディレクトリ名
   * @param[in] original_filename 原本ファイル名
   * @param[in] prefix 一時ファイルの作成時に付加する文字列
   * @param[in] extention 一時ファイル作成時の指定拡張子文字列
   * @param[in] source_file 「True」で原本ファイル指定、「False」でコピーファイル指定。
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static File generateTmpFile(String target_func, 
                                     String target_dir, 
                                     String original_filename, 
                                     String prefix, 
                                     String extention, 
                                     Boolean source_file) throws IOException{

    Path source = new ClassPathResource(String.format("TestCaseFile/%s/BlobTestCaseData/%s/%s", 
                                        target_func, 
                                        target_dir, 
                                        original_filename))
                                        .getFile()
                                        .toPath();

    if(source_file){
      return source.toFile();
    }

    Path copy = Files.createTempFile(Paths.get("src/main/resources/TempFileBuffer"), 
                                     prefix, 
                                     ".".concat(extention), 
                                     Member_Info_Component_TestUtils.perms);

    return Files.copy(source, copy, REPLACE_EXISTING).toFile();
  }








  /** 
   **********************************************************************************************
   * @brief 指定された二つのファイルのストリームでハッシュ計算を行い、その値を利用して二つのファイルの一致
   * 確認を行う。
   * 
   * @details
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   *
   * @param[in] compare 比較用ファイルストリーム
   * @param[in] result テスト出力結果ファイルストリーム
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void compareHash(InputStream compare, InputStream result, SoftAssertions softly) throws IOException{
    String compare_hash = DigestUtils.sha512_256Hex(compare);
    String result_hash = DigestUtils.sha512_256Hex(result);

    softly.assertThat(compare_hash).isEqualTo(result_hash);
  }




  




  /** 
   **********************************************************************************************
   * @brief テスト後に生成した一時ファイルを削除する。
   *
   * @details
   * - 指定された一時ファイルを削除した後、遅延アサーションを使って確実にファイルが消えていることを確認した
   * 上で処理を終了する。
   * - 遅延アサーションのインスタンスは、このメソッドを呼び出したクラス側の物を使用する。また、アサーションの
   * 最終実行も呼び出し側のクラスで行う。
   * 
   * @param[in] delete_target 削除対象の一時ファイル
   * @param[out] softly 遅延アサーション用のインスタンス
   * 
   * @note 入出力コンポーネントの全体のテストでこのメソッドを用いる。
   * 
   * @throw IOException
   **********************************************************************************************
   */
  public static void deleteTmpFileCheck(File delete_target, SoftAssertions softly) throws IOException{
    Files.deleteIfExists(delete_target.toPath());
    softly.assertThat(Files.exists(delete_target.toPath())).isFalse();
  }










  /** 
   **********************************************************************************************
   * @brief テスト終了後に、一時ファイル用のディレクトリが空っぽであることを確認する。
   *
   * @details
   * - 判定前に1秒間待つ。判定に早く取り掛かりすぎると、一時ファイルの削除処理の途中にもかかわらず判定が
   * 始まってしまい、一時ファイルが削除済みでもエラー扱いとなるため。
   * 
   * @throw InterruptedException
   **********************************************************************************************
   */
  public static void checkTmpDirClear() throws InterruptedException{
    Thread.sleep(1000);
    File tmp_dir = new File("src/main/resources/TempFileBuffer");
    assertTrue(tmp_dir.list().length == 0);
  }
}
