package com.springproject.dockerspring.commonenum;



/********************************************************************/
/*   [Rec_Eval_Enum]
/*   主に「Rec_Eval」関連の機能で共通に用いる、
/*   マップキー名などで全般的に用いる列挙型である。
/********************************************************************/
public enum Rec_Eval_Enum {
  Serial_Num("シリアルナンバー", "serial_num"),
  Eval_Id("採用考課番号", "eval_id"),
  Member_Id("団員番号", "member_id"),
  Eval_Name("採用考課名", "eval_name"),
  Eval_Date("採用考課日", "eval_date"),
  Evalsheet_Id("採用考課シート番号", "evalsheet_id"),
  Eval_Contents_1("採用考課内容１", "eval_contents_1"),
  Eval_Rank_1("評価ランク１", "eval_rank_1"),
  Eval_Contents_2("採用考課内容２", "eval_contents_2"),
  Eval_Rank_2("評価ランク２", "eval_rank_2"),
  Eval_Contents_3("採用考課内容３", "eval_contents_3"),
  Eval_Rank_3("評価ランク３", "eval_rank_3"),
  Eval_Contents_4("採用考課内容４", "eval_contents_4"),
  Eval_Rank_4("評価ランク４", "eval_rank_4"),
  Eval_Contents_5("採用考課内容５", "eval_contents_5"),
  Eval_Rank_5("評価ランク５", "eval_rank_5"),
  Eval_Contents_6("採用考課内容６", "eval_contents_6"),
  Eval_Rank_6("評価ランク６", "eval_rank_6"),
  Eval_Contents_7("採用考課内容７", "eval_contents_7"),
  Eval_Rank_7("評価ランク７", "eval_rank_7"),
  Eval_Contents_8("採用考課内容８", "eval_contents_8"),
  Eval_Rank_8("評価ランク８", "eval_rank_8"),
  Eval_Contents_9("採用考課内容９", "eval_contents_9"),
  Eval_Rank_9("評価ランク９", "eval_rank_9"),
  Eval_Contents_10("採用考課内容１０", "eval_contents_10"),
  Eval_Rank_10("評価ランク１０", "eval_rank_10");

  private final String text;
  private final String key;

  private Rec_Eval_Enum(String text, String key){
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
