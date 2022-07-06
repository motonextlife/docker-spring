package com.springproject.dockerspring.component.ZipProcess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import org.springframework.web.multipart.MultipartFile;

import com.springproject.dockerspring.component.DatatypeEnum;
import com.springproject.dockerspring.component.FilePathEnum;
import com.springproject.dockerspring.component.OriginalException.InputFileDifferException;





/**************************************************************/
/*   [Common_Zip]
/*   全てのZIP処理の共通部分を行う。
/**************************************************************/
public abstract class Common_Zip{


  /*************************************************************/
  /*   [extractZipCom]
  /*   読み込んだZIPファイルからデータを抽出し、マップリストに
  /*   ファイル名とファイルデータを保存する。
  /*************************************************************/
  public Map<String, byte[]> extractZipCom(MultipartFile zipfile, long limit, int readcount)
      throws IOException, ZipException, InputFileDifferException {

    Map<String, byte[]> map = new HashMap<>();

    /*************************************************************/
    /*   ZIPファイルを受け付ける段階で、ファイルにパスワードが
    /*   かかっていた場合は、処理を中断する。
    /*************************************************************/
    try(BufferedInputStream buf = new BufferedInputStream(zipfile.getInputStream());
      ZipInputStream zipin = new ZipInputStream(buf, StandardCharsets.UTF_8)){  //UTF_8で統一。


      /*************************************************************/
      /*   入力されたファイルが、そもそもZIPファイルでない、または
      /*   データサイズが超過していたら。処理を中断する。
      /*************************************************************/
      if(!zipfile.getContentType().equals(DatatypeEnum.ZIP.getType())
          || zipfile.getSize() > DatatypeEnum.ZIP.getLimit()){
        throw new InputFileDifferException();
      }


      /*************************************************************/
      /*   ZIPファイルを解凍し、エントリからデータを抽出する。
      /*   但し、一つでもフォルダや、パスワード付きのファイルが
      /*   含まれていた場合は、処理を中断する。
      /*************************************************************/
      LocalFileHeader zipent = null;
      int count = 1;
      long totalsize = 0L;

      while((zipent = zipin.getNextEntry()) != null){

        if(zipent.isDirectory() || zipent.isEncrypted()){
          throw new InputFileDifferException();
        }


        /*************************************************************/
        /*   データはバイト配列で読み取るが、読み取り可能なデータ量の
        /*   制限を行う。データ総数のデータ量は、一つのファイルの
        /*   データ制限に、読み取り可能ファイル数を乗じたものである。
        /*************************************************************/
        try(ByteArrayOutputStream byteout = new ByteArrayOutputStream();
          BufferedOutputStream out = new BufferedOutputStream(byteout);){

          int readsize;
          int maxsize = 0;
          while((readsize = zipin.read(DatatypeEnum.ZIP.getBuf())) != -1){

            out.write(DatatypeEnum.ZIP.getBuf(), 0, readsize);

            maxsize += readsize;
            totalsize += readsize;

            if(maxsize > limit 
                || totalsize > limit * readcount
                || count > readcount){
              throw new InputFileDifferException();
            }
          }

          map.put(zipent.getFileName(), byteout.toByteArray());

        }catch(IOException e){
          throw new IOException("Error location [Common_Zip:extractZipCom(inner)]" + " Error Timing " + count);
        }

        count++;
      }

    }catch(ZipException e){

      if(e.getType().equals(ZipException.Type.WRONG_PASSWORD)){  //「パスワードの設定」によるエラーのみ、別として扱う。
        throw new InputFileDifferException();
      }else{
        throw new ZipException("Error location [Common_Zip:extractZipCom(outer)]");
      }
    }

    return map;
  }







  /*************************************************************/
  /*   [outputZipCom]
  /*   データベースに保存してあるデータを、ZIPファイルデータ
  /*   として出力する。
  /*************************************************************/
  public <T> String outputZipCom(String filepath, Map<String[], byte[]> map) throws ZipException, IOException, InterruptedException {
    
    String result = null;
    Path tmpdirpath;
    Path specifydir = Paths.get(FilePathEnum.ResourceFilePath.getPath());
    List<File> generatefile = Collections.synchronizedList(new ArrayList<>());

    //一時ディレクトリやファイルの実行権限は、全ての利用者において剥奪しておく。
    FileAttribute<Set<PosixFilePermission>> perms = PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-"));


    /*************************************************************/
    /*   ZIP出力ライブラリ「zip4j」においては、圧縮出力対象の
    /*   ファイルを一時的にローカルファイルに出力した上で、
    /*   再度取り込む必要があるので、ファイル出力を行う。
    /*************************************************************/
    try{
      //一時ファイル保存用のディレクトリを作成。
      tmpdirpath = Files.createTempDirectory(specifydir, filepath, perms);
      File tmpdir = tmpdirpath.toFile();
      tmpdir.deleteOnExit();  //プログラムが終了すると自動的に削除。


      //一時ファイル出力は並列処理とする。
      map.entrySet().parallelStream().forEachOrdered(s -> {

        /*************************************************************/
        /*   データベースから取得したファイル名による、ディレクトリ
        /*   トラバーサルを避けるため、ファイルパス文字を取り除く。
        /*************************************************************/
        String filename = s.getKey()[0];
        filename = filename.replace("/", "");
        filename = filename.replace(".", "");
        filename = filename.replace("\\", "");
        
        /*************************************************************/
        /*   ファイル名が被ることを避けるため、現スレッドIDを付与する。
        /*************************************************************/
        filename = filename.concat("_").concat(String.valueOf(Thread.currentThread().getId()));
        String suff = ".".concat(s.getKey()[1]);

        //一時ファイルを作成。
        Path tmpfilepath;
        File tmpfile;
        try {
          tmpfilepath = Files.createTempFile(tmpdirpath, filename, suff, perms);
          tmpfile = tmpfilepath.toFile();
          tmpfile.deleteOnExit();  //プログラムが終了すると自動的に削除。
        } catch (IOException e) {
          throw new RuntimeException("Error location [Common_Zip:outputZipCom(IOException){TmpFileMake}]");
        }
          
        try(FileOutputStream file = new FileOutputStream(tmpfile);
          BufferedOutputStream bos = new BufferedOutputStream(file);){
          
          bos.write(s.getValue());
          
        }catch(FileNotFoundException e){
          throw new RuntimeException("Error location [Common_Zip:outputZipCom(FileNotFoundException){TmpFileOut}]");
        }catch(IOException e){
          throw new RuntimeException("Error location [Common_Zip:outputZipCom(IOException){TmpFileOut}]");
        }

        generatefile.add(tmpfile);
      });

    }catch(ClassCastException e){
      throw new ClassCastException("Error location [Common_Zip:outputZipCom(ClassCastException)]");
    }catch(IOException e){
      throw new IOException("Error location [Common_Zip:outputZipCom(IOException){TmpFileOut}]");
    }



    /*************************************************************/
    /*   ローカルファイルに出力した各ファイルを再度取り込み、
    /*   圧縮処理をして、そのZIPファイルも再度ローカルファイルに
    /*   出力する。
    /*************************************************************/

    //パスワードは設定しない。
    ZipParameters param = new ZipParameters();
    param.setCompressionMethod(CompressionMethod.DEFLATE);
    param.setCompressionLevel(CompressionLevel.NORMAL);

    //一時ZIPファイル作成。
    Path tmpzippath = Files.createTempFile(tmpdirpath, filepath, ".".concat(DatatypeEnum.ZIP.getExt()), perms);
    File tmpzip = tmpzippath.toFile();
    tmpzip.deleteOnExit();  //プログラムが終了すると自動的に削除。

    try(ZipFile zip = new ZipFile(tmpzip)){

      for(File file: generatefile){
        zip.addFile(file, param);
      }
    }catch(ZipException e){
      throw new ZipException("Error location [Common_Zip:outputZipCom(ZipException)]");
    }

    generatefile.add(tmpzip);



    /*************************************************************/
    /*   出力したZIPファイルを再度取り込み、バイト配列に変換。
    /*************************************************************/
    try(FileInputStream file = new FileInputStream(tmpzip);
      BufferedInputStream bis = new BufferedInputStream(file);
      ByteArrayOutputStream byteout = new ByteArrayOutputStream();){

      byte[] readdata = bis.readAllBytes();
      byteout.write(readdata, 0, readdata.length);

      result = zipBase64Encode(byteout.toByteArray());

    }catch(FileNotFoundException e){
      throw new FileNotFoundException("Error location [Common_Zip:outputZipCom(FileNotFoundException)]");
    }catch(IOException e){
      throw new IOException("Error location [Common_Zip:outputZipCom(IOException){TmpZipInput}]");
    }

    return result;
  }





  /***************************************************************/
  /*   [zipBase64Encode]
  /*   ZIPデータの、Base64へのエンコードを行う。
  /*   なお、このメソッドは実装先での変更が生じないので
  /*   デフォルトメソッドでの定義とする。
  /***************************************************************/
  private String zipBase64Encode(byte[] bytedata){

    if(bytedata.length == 0){
      return "";
    }else{
      return Base64.getEncoder().encodeToString(bytedata);
    }
  }
}
