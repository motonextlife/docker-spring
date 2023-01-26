/** 
 **************************************************************************************
 * @file Common_Zip.java
 * @brief すべての圧縮ファイル作成機能で使用する共通処理を定義するクラスを格納するファイル。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.FileIO.ZipProcess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.UtilEnum.Datatype_Enum;
import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.FileIO.OriginalException.InputFileDifferException;
import com.springproject.dockerspring.Form.BlobImplForm.FileEntity;









/** 
 **************************************************************************************
 * @brief すべての圧縮ファイル作成機能で使用する共通処理を定義するクラス
 * 
 * @details 
 * - この共通処理では、ファイルサーバー通信処理に関して、特定のクラスに依存しない共通の処理を
 * 定義する。作成対象の特定クラスに対してはDIでこのクラスを使えるようにする。
 * - このクラスで作成される一時ファイルに関しては、すべてローカルにある一時ファイル用の
 * ディレクトリに出力する。なお、その際のパーミッションは、実行権限をすべて剥奪しておく。
 * - ZIP圧縮を行うライブラリは[zip4j]を用いる。これによって、標準の圧縮機能では実現が難しい
 * 暗号化済みの圧縮ファイルへの対応が可能になる。
 * 
 * @par 使用アノテーション
 * - @Component
 * 
 * @see FileEntity
 **************************************************************************************
 */ 
@Component
public class Common_Zip{

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));











  /** @name このクラス内で用いるユーティリティクラス（外部へは一切出さない） */
  /** @{ */

  /** 
   **********************************************************************************************
   * @brief このクラス内で生成する一時ファイルを一元的に製作するメソッド。
   * 
   * @details
   * - このメソッドを使用する際に、任意の文字列を渡すことでファイル名にその文字列を付加する。
   * これによって一時ファイル生成を指示した呼び出し元メソッドが特定できる。
   * - このメソッドでは、モードを切り替えて動作させることができる。一つ目は、「ランダムな名前を付けた一時
   * ファイルの作成」、もう一つは「一時ファイルは作成せず、ランダムな名前を付けたFileインスタンスのみ作成」の
   * 2種類である。
   * - モードを分けた理由としては、ライブラリの仕様上、何もデータが定義されていない空のファイルに圧縮ファイル
   * データを書き込むことができない。書き込む際には、ファイルの名前だけ指定して、ファイル生成はライブラリに
   * ゆだねる必要があるためである。
   *
   * @return 生成した一時ファイル or ランダムな名前を付加したFileインスタンス
   *
   * @param[in] prefix ファイル生成時に、ファイル名に付加する名前
   * @param[in] ext ファイル生成時の拡張子の指定
   * @param[in] name_only モード切替の真偽値。「True」でインスタンスのみ、「False」で一時ファイル作成。
   * 
   * @par ランダムファイル名の生成方法
   * -# 20文字のランダムの半角数字の文字列を自動生成。
   * -# 現在のタイムスタンプを取得し、文字列に変換した物を作成。
   * -# 作成した二つの文字列を連結する。
   * -# なお、これはインスタンス生成時のみ使用する。通常の一時ファイル作成時には、Javaが自動的にスレッドを
   * 認識して一意な名前を付けるので、この機能は使わなくてもよい。
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   **********************************************************************************************
   */
  private File makeTmpFile(String prefix, String ext, Boolean name_only) throws IOException {

    if(name_only){
      String dir_path = this.specify_dir.toString().concat("/");
      String rand_str = RandomStringUtils.randomNumeric(20);
      String timestamp = String.valueOf(System.currentTimeMillis());

      return new File(dir_path + prefix + rand_str + timestamp + "." + ext);

    }else{
      try{
        Path tmp_zip_path = Files.createTempFile(this.specify_dir, 
                                                 prefix, 
                                                 ".".concat(ext), 
                                                 this.perms);
       return tmp_zip_path.toFile();

      }catch(IOException e){
        throw new IOException("Error location [Common_Zip:makeTmpFile]" + "\n" + e);
      }
    }
  }










  /** 
   **********************************************************************************************
   * @brief 圧縮ファイルからのエントリのファイル名から拡張子を抜き出し、抜き出した拡張子を返す。
   * 
   * @details
   * - このメソッドは主に、圧縮ファイルから抽出したファイルと同じ拡張子の一時ファイルを作成するために用いる。
   * - システムで許可されている拡張子は受付可能で、抜き出した拡張子文字列をそのまま返すが、少しでも違う拡張子
   * のファイルだった場合は、エラーを返す。
   *
   * @return 抜き出した拡張子文字列 
   *
   * @param[in] zip_ent 圧縮ファイルのエントリ
   * 
   * @note このメソッドは、おもに[extractZipCom]さらには[zipEntryRead]で使用する。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private String entryExtSet(LocalFileHeader zip_ent) throws IOException, InputFileDifferException {

    String name = zip_ent.getFileName();
    int idx = name.lastIndexOf(".");
    name = idx != -1 ? name.substring(idx + 1) : "";

    if(name.equals(Datatype_Enum.AUDIO.getExtension())){
      return Datatype_Enum.AUDIO.getExtension();
    }else if(name.equals(Datatype_Enum.PHOTO.getExtension())){
      return Datatype_Enum.PHOTO.getExtension();
    }else if(name.equals(Datatype_Enum.PDF.getExtension())){
      return Datatype_Enum.PDF.getExtension();
    }else{
      throw new InputFileDifferException("missing extension");
    }
  }










  /** 
   **********************************************************************************************
   * @brief 処理中にエラーになった際は、作成されている一時ファイルを削除し例外を投げる。
   * 同じ名前のメソッドが二つあるが、こちらは単体の一時ファイルの削除用となる。
   *
   * @param[in] delete_tmp_file 削除対象の一時ファイル
   * 
   * @note このメソッドは、おもに[extractZipCom]さらには[zipEntryRead]で使用する。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private void errorTmpFileReset(File delete_tmp_file) throws IOException, InputFileDifferException {
    Files.deleteIfExists(delete_tmp_file.toPath());
    throw new InputFileDifferException("missing zip file");
  }










  /** 
   **********************************************************************************************
   * @brief 処理中にエラーになった際は、作成されている一時ファイルを削除し例外を投げる。
   * 同じ名前のメソッドが二つあるが、こちらは既に複数生成されている一時ファイルの削除用となる。
   *
   * @param[in] delete_files 既にエンティティに格納済みの削除対象の複数の一時ファイル
   * 
   * @note このメソッドは、このクラスの全域で使用。
   *
   * @throw IOException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private void errorTmpFileReset(List<FileEntity> delete_files) throws IOException, InputFileDifferException {
    for(FileEntity item: delete_files){
      Files.deleteIfExists(item.getTmpfile().toPath());
    }
  }









  /** 
   **********************************************************************************************
   * @brief 読み込んだ個別の圧縮ファイルのエントリからファイルを読み込み、専用のエンティティに格納する。
   *
   * @param[in] zipin 圧縮ファイル入力のストリーム
   * @param[in] zip_ent 圧縮ファイルから取り出したエントリ
   * @param[in] size_limit 設定されている読み込み可能データ量の制限値
   * 
   * @par 処理の大まかな流れ
   * -# エントリのファイル名から拡張子を抜き出す。この時点で、拡張子がシステムで受け入れることができる物
   * 以外の場合は、エラーとなる。
   * -# 抜き出した拡張子を使って、読み込んだファイルのデータを格納するための一時ファイルを作成する。
   * -# ストリームを開き、バッファを用いて少しづつ読み取っていくが、以下の状態になった時は後続の処理を実行せず
   * すぐにエラーとする。
   *    - 読み取ったファイルのデータ量が制限値を少しでも超えた場合。
   *    - ファイルの名前の長さが余りにも長い（ここでは200文字）だった場合。
   * -# 異常が無ければ、専用の格納エンティティに作成したエントリのファイル名と、一時ファイルをセットで格納し
   * 戻り値とする。
   * -# エラーになった際は、作成しておいた一時ファイルを削除し、例外を投げる。
   * 
   * @note このメソッドは、おもに[extractZipCom]で使用する。なお、データ量の制限値は、このクラスを使用する
   * 特定クラスで設定している環境変数に依存する。
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  private FileEntity zipEntryRead(ZipInputStream zipin, 
                                  LocalFileHeader zip_ent, 
                                  int size_limit) throws IOException, ZipException, InputFileDifferException {

    FileEntity ent = new FileEntity();
    String ext = entryExtSet(zip_ent);
    File tmp_file = makeTmpFile("zipEntryRead", ext, false);

    try(FileOutputStream fos = new FileOutputStream(tmp_file);
        BufferedOutputStream out = new BufferedOutputStream(fos);){

      int read_size = 0;
      int max_size = 0;
      byte[] buffer = new byte[1024];
      
      while((read_size = zipin.read(buffer)) != -1){
        out.write(buffer, 0, read_size);
        max_size += read_size;

        //個別ファイルの読取量制限
        if(max_size > size_limit){
          errorTmpFileReset(tmp_file);
        }
      }
      
      //ファイル名長さ制限
      if(zip_ent.getFileNameLength() > 200){
        errorTmpFileReset(tmp_file);
      }

      ent.setFilename(zip_ent.getFileName());
      ent.setTmpfile(tmp_file);

    }catch(IOException e){
      Files.deleteIfExists(tmp_file.toPath());
      throw new IOException("Error location [Common_Zip:zipEntryRead]" + "\n" + e);
    }

    return ent;
  }









  /** 
   **********************************************************************************************
   * @brief ファイルの圧縮時に、同じ名称のファイルが複数あった場合に、ファイル名本体と拡張子の間に
   * 連番を付与することで、名前が被るのを防ぐ。
   *
   * @param[in] file 変換対象のファイルが格納された専用エンティティ
   * @param[in] param 圧縮ファイルの中身のファイル名をセットするパラメーター
   * 
   * @par 大まかな処理の流れ
   * -# ファイル名を抽出し、ファイル名専用のマップリストに登録。その際に現在の重複数をカウントして保存。
   * -# 次のファイル名の抽出の際に、同じファイル名がマップリストに登録されていないことを確認。
   * -# 同じ名前が保存されていれば、登録されている重複数を、抽出するファイル名に付加する。
   * 
   * @note このメソッドは、おもに[outputZipCom]で使用する。
   **********************************************************************************************
   */
  private void renameFilename(FileEntity file, ZipParameters param, Map<String, Integer> filename_list){

    String filename = file.getFilename();

    if(filename_list.containsKey(filename)){
      int distinct_count = filename_list.get(filename);

      int idx = filename.lastIndexOf(".");
      String dot_ext = idx != -1 ? filename.substring(idx) : "";
      String cut_filename = filename.replace(dot_ext, "");

      filename_list.put(filename, ++distinct_count);
      param.setFileNameInZip(cut_filename + "(" + distinct_count + ")" + dot_ext);

    }else{
      param.setFileNameInZip(filename);
      filename_list.put(filename, 0);
    }
  }

  /** @} */














  /** 
   **********************************************************************************************
   * @brief 読み込んだ圧縮ファイルからデータを抽出し、専用のエンティティに格納して返す。
   *
   * @param[in] zip_file 入力された圧縮ファイル
   * @param[in] count_limit 読み取り可能なファイル数の制限値
   * @param[in] size_limit 読み取り可能な、一つのファイルのデータ量制限値
   * 
   * @par 処理の大まかな流れ
   * -# 圧縮ファイルの入力ストリームを開く。この時点で、圧縮ファイルにパスワードがかかっていて開けない場合は
   * エラーとなる。
   * -# 圧縮ファイルから個別のファイルのエントリを読み込んでいくが、以下の状態になったら後続の処理を行わず
   * すぐにエラーを出す。
   *    - エントリのファイルにパスワードがかかっていて開けない場合。
   *    - エントリのファイルが、ディレクトリだった場合。
   *    - エントリの数が、制限値を超えた場合。
   * -# エントリからファイルのデータを抽出し、専用のエンティティに抽出したデータを全て格納する。
   * -# エラーになった際は、作成しておいた一時ファイルを全て削除（抽出が成功したものを含めて）し、例外を投げる。
   * 
   * @note データ量とファイル数の制限値は、このクラスを使用する特定クラスで設定している環境変数に依存する。
   * また、読取時の文字コードは[UTF-8]とする。
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public List<FileEntity> extractZipCom(MultipartFile zip_file, 
                                        int count_limit, 
                                        int size_limit) throws IOException, ZipException, InputFileDifferException {

    List<FileEntity> extract = new ArrayList<>();

    try(BufferedInputStream buf = new BufferedInputStream(zip_file.getInputStream());
        ZipInputStream zipin = new ZipInputStream(buf, StandardCharsets.UTF_8)){

      int file_count = 0;
      LocalFileHeader zip_ent = null;
      while((zip_ent = zipin.getNextEntry()) != null){

        if(++file_count > count_limit || zip_ent.isDirectory() || zip_ent.isEncrypted()){
          throw new InputFileDifferException("missing zip file");
        }

        extract.add(zipEntryRead(zipin, zip_ent, size_limit));
      }

    }catch(InputFileDifferException e){
      errorTmpFileReset(extract);
      throw new InputFileDifferException("missing zip file");

    }catch(ZipException e){
      errorTmpFileReset(extract);

      //「パスワードの設定」によるエラーのみ別として扱う。
      if(e.getType().equals(ZipException.Type.WRONG_PASSWORD)){  
        throw new InputFileDifferException("missing zip file");
      }else{
        throw new ZipException("Error location [Common_Zip:extractZipCom]" + "\n" + e);
      }

    }catch(IOException e){
      errorTmpFileReset(extract);
      throw new IOException("Error location [Common_Zip:extractZipCom]" + "\n" + e);
    }

    return extract;
  }














  /** 
   **********************************************************************************************
   * @brief ファイルサーバーから読み込んだデータを、圧縮ファイルとして出力する。
   * 
   * @details
   * - ファイルサーバーからのデータ取得の処理は、関数型インターフェースとして引数で渡し、その中で実行する。
   * これによって、ファイルサーバー通信処理とファイル圧縮処理を明確に分けることが可能になる。
   *
   * @param[in] samba_generate ファイルサーバーからのデータ取得の処理の関数
   * 
   * @par 処理の大まかな流れ
   * -# 圧縮ファイル出力用として、Fileインスタンスを生成する。
   * -# 圧縮ファイルの設定には、「標準の圧縮度」かつ「パスワード無し」で設定する。
   * -# ファイルサーバーからデータを取り出し、圧縮ファイルのエントリに追加していく。その際に、ファイル名が重複
   * すれば、重複防止として連番を付与する。
   * -# 生成した圧縮ファイルを戻り値とする。また、この処理の際に生成されている一時ファイルは、エラーの有無に
   * 関わらず全て削除する。
   * 
   * @note 出力する圧縮ファイルはパスワードを設定しない。
   *
   * @throw IOException
   * @throw ZipException
   * @throw InputFileDifferException
   **********************************************************************************************
   */
  public File outputZipCom(Supplier<List<FileEntity>> samba_generate) throws ZipException, IOException, InputFileDifferException{

    File result = makeTmpFile("outputZipCom", Datatype_Enum.ZIP.getExtension(), true);

    ZipParameters param = new ZipParameters();
    param.setCompressionMethod(CompressionMethod.DEFLATE);
    param.setCompressionLevel(CompressionLevel.NORMAL);

    List<FileEntity> file_list = new ArrayList<>();

    try(ZipFile zip = new ZipFile(result)){
      file_list = samba_generate.get();
      Map<String, Integer> filename_list = new HashMap<>();

      for(FileEntity file: file_list){
        renameFilename(file, param, filename_list);
        zip.addFile(file.getTmpfile(), param);
      }

    }catch(ZipException e){
      Files.deleteIfExists(result.toPath());
      throw new ZipException("Error location [Common_Zip:samba_generateTmpZip]" + "\n" + e);
    }finally{
      errorTmpFileReset(file_list);
    }

    return result;
  }
}
