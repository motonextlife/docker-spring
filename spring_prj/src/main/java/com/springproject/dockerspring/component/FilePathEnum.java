package com.springproject.dockerspring.component;



/*************************************************************/
/*   [FilePathEnum]
/*   ZIPファイルの出力の際の、ローカルファイルパスを定義する。
/*************************************************************/
public enum FilePathEnum {
  ResourceFilePath("src/main/resources/ZipOutputBuffer"),
  Audio_Data("AudioData"),
  Equip_Inspect_Photo("EquipInspPhoto"),
  Rec_Eval_Record("RecEvalRecord"),
  Score_Pdf("ScorePdf");

  private final String path;

  private FilePathEnum(String path){
    this.path = path;
  }

  public String getPath(){
    return this.path;
  }
}