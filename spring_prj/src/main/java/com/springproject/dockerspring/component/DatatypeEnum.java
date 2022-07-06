package com.springproject.dockerspring.component;


/**************************************************************/
/*   [DatatypeEnum]
/*   データタイプを定義する列挙型である。
/**************************************************************/
public enum DatatypeEnum{
  //バッファは10KBずつ。
  PHOTO("image/png", "png", 1048576, 10240),  //1MB
  MOVIE("video/mp4", "mp4", 209715200, 10240),  //200MB
  AUDIO("audio/wav", "wav", 734003200, 10240),  //700MB
  PDF("application/pdf", "pdf", 20971520, 10240),  //20MB
  CSV("text/csv", "csv", 10485760, 10240),  //10MB
  ZIP("application/zip", "zip", 1073741824, 10240);  //1GB

  private final String type;
  private final String ext;
  private final int limit;
  private final byte[] buf;

  private DatatypeEnum(String type, String ext, int limit, int buf){
    this.type = type;
    this.ext = ext;
    this.limit = limit;
    this.buf = new byte[buf];
  }

  public String getType() {
    return this.type;
  }

  public String getExt() {
    return this.ext;
  }

  public long getLimit() {
    return this.limit;
  }

  public byte[] getBuf() {
    return this.buf;
  }
}