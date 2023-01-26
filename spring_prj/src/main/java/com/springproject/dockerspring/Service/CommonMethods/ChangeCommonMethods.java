/** 
 **************************************************************************************
 * @package com.springproject.dockerspring.Service.CommonMethods
 * 
 * @brief サービスクラス内で共通で使用する処理を定義するクラスを格納するパッケージ
 * 
 * @details
 * - このパッケージでは、サービスクラスの処理を可能な限り共通化するため、汎用的に用いることができる
 * 処理を実装したクラスを格納する。
 **************************************************************************************
 */ 
package com.springproject.dockerspring.Service.CommonMethods;





/** 
 **************************************************************************************
 * @file ChangeCommonMethods.java
 * @brief サービスクラスでの共通処理において、データのCRUD処理に関する処理を実装したクラス
 * を格納したファイル。
 * 
 * @note
 * このファイルに、所属パッケージの説明を記載する。
 **************************************************************************************
 */ 
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.CommonEnum.UtilEnum.File_Path_Enum;
import com.springproject.dockerspring.FileIO.CompInterface.InOutSamba;
import com.springproject.dockerspring.Repository.FindAllCrudRepository;
import com.springproject.dockerspring.Service.OriginalException.DbActionException;

import lombok.RequiredArgsConstructor;









/** 
 **************************************************************************************
 * @brief サービスクラスでの共通処理において、データのCRUD処理に関する処理を実装したクラス
 * 
 * @details 
 * - この共通処理では、サービスクラスで用いる集約が可能な機能を集めることで、メンテナンス性を
 * 向上させるために実装。
 * - ただし、特定の機能に依存してしまう処理に関しては、ここでは実装せずに関数型インターフェースで
 * 渡すなどして分離を行う。
 * - このクラス内で作成する一時ファイルに関しては、すべて実行権限を剥奪しておく。
 * 
 * @par 使用アノテーション
 * - @Component
 * - @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 **************************************************************************************
 */ 
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeCommonMethods{

  private final Path specify_dir = Paths.get(File_Path_Enum.ResourceFilePath.getPath());

  private final FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions
                                                                .asFileAttribute(PosixFilePermissions
                                                                                .fromString(File_Path_Enum.Permission.getPath()));














  /** 
   **********************************************************************************************
   * @brief ユーザーから受け取ったマルチパートのバイナリデータを、このシステム上で扱えるように一時ファイルに
   * 移し替える。
   * 
   * @details
   * - このシステムの一部のバイナリデータ処理は、一時ファイルを受け取って行うものがあるため、それに対応する
   * 為のメソッドである。
   *
   * @return 生成した一時ファイル 
   *
   * @param[in] file 移し替え対象のマルチパートファイル
   * @param[in] prefix 一時ファイル生成時に付加する文字列
   * @param[in] extension 一時ファイル生成時に付加する拡張子文字列
   *
   * @throw IOException
   **********************************************************************************************
   */
  public File makeMultipartTmpFile(MultipartFile file, String prefix, String extension) throws IOException {

    File temp_file_path = null;

    try(BufferedInputStream bis = new BufferedInputStream(file.getInputStream())){

      temp_file_path = Files.createTempFile(this.specify_dir, 
                                            prefix, 
                                            ".".concat(extension), 
                                            this.perms)
                                            .toFile();

      try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp_file_path))){
        byte[] buf = new byte[1024];
        int read_size = 0;

        while((read_size = bis.read(buf)) != -1){
          bos.write(buf, 0, read_size);
        }
      }

    }catch(IOException e){
      if(temp_file_path != null){
        Files.deleteIfExists(temp_file_path.toPath());
      }
      throw new IOException("Error location [ChangeCommonMethods:makeTmpFile]" + "\n" + e);
    }

    return temp_file_path;
  }










  /** 
   **********************************************************************************************
   * @brief 渡された情報をデータベースに格納する。なお、新規追加と更新で共用である。
   * 
   * @details
   * - 処理を行うリポジトリクラスは、特定のクラスに依存するため、引数として渡されたものを使用する。
   * - 処理としては単純で、使用するリポジトリの保存メソッドを実行するだけである。
   *
   * @return 保存が成功したエンティティ（自動連番付き） 
   *
   * @param[in] repository 処理を行うリポジトリクラス
   * @param[in] entity 保存を指定するデータが入ったエンティティ
   * 
   * @see FindAllCrudRepository
   *
   * @throw DbActionException
   **********************************************************************************************
   */
  public <T> T changeDatabaseCom(FindAllCrudRepository<T, Integer> repository, T entity) throws DbActionException {
    try{
      return repository.save(entity);
    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ChangeCommonMethods:changeDatabaseCom]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 渡された情報をファイルサーバーに格納する。なお、新規追加と更新で共用である。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 処理としては単純で、使用するコンポーネントの保存メソッドを実行するだけである。
   *
   * @param[in] samba_comp 処理を行うコンポーネントクラス
   * @param[in] file 保存するデータを格納したファイル
   * @param[in] num_id 保存する際に付加するファイル名としての、シリアルナンバーや履歴番号
   * @param[in] hist 通常データ保存の際は「False」、履歴用データの保存の際は「True」である。
   * 
   * @see InOutSamba
   *
   * @throw IOException
   **********************************************************************************************
   */
  public void changeSambaCom(InOutSamba samba_comp, File file, Integer num_id, Boolean hist) throws IOException{
    try{
      if(hist){
        samba_comp.historyFileInsert(file, num_id);
      }else{
        samba_comp.fileInsertUpdate(file, num_id);
      }
    }catch(IOException e){
      throw new IOException("Error location [ChangeCommonMethods:changeSambaCom]" + "\n" + e);
    }
  }










  /** 
   **********************************************************************************************
   * @brief 指定されたデータベース内のデータを削除する。
   * 
   * @details
   * - 処理を行うリポジトリクラスは、特定のクラスに依存するため、引数として渡されたものを使用する。
   * - 処理としては単純で、使用するリポジトリの削除メソッドを実行するだけである。
   *
   * @param[in] repository 処理を行うリポジトリクラス
   * @param[in] entity 削除を指定するデータが入ったエンティティ
   * 
   * @see FindAllCrudRepository
   *
   * @throw DbActionException
   **********************************************************************************************
   */
  public <T> void deleteDatabaseCom(FindAllCrudRepository<T, Integer> repository, T entity) throws DbActionException {
    try{
      repository.delete(entity);
    }catch(DbActionExecutionException e){
      throw new DbActionException("Error location [ChangeCommonMethods:deleteDatabaseCom]" + "\n" + e);
    }
  }











  /** 
   **********************************************************************************************
   * @brief 指定されたファイルサーバー上のバイナリファイルを削除する。
   * 
   * @details
   * - 処理を行うコンポーネントクラスは特定のクラスに依存するため、使用するコンポーネントを引数で受け取る。
   * - 処理としては単純で、使用するコンポーネントの削除メソッドを実行するだけである。
   *
   * @param[in] samba_comp 処理を行うコンポーネントクラス
   * @param[in] num_id 削除指定するファイル名としての、シリアルナンバーや履歴番号
   * @param[in] hist 通常データ削除の際は「False」、履歴用データの削除の際は「True」である。
   * 
   * @see InOutSamba
   *
   * @throw IOException
   **********************************************************************************************
   */
  public void deleteSambaCom(InOutSamba samba_comp, Integer num_id, Boolean hist) throws IOException{
    try{
      if(hist){
        samba_comp.historyFileDelete(num_id);
      }else{
        samba_comp.fileDelete(num_id);
      }
    }catch(IOException e){
      throw new IOException("Error location [ChangeCommonMethods:deleteSambaCom]" + "\n" + e);
    }
  }
}