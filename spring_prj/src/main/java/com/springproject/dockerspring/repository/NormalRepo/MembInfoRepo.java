package com.springproject.dockerspring.repository.NormalRepo;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springproject.dockerspring.entity.NormalEntity.Member_Info;
import com.springproject.dockerspring.repository.NormalRepo.FormEnum.FormMembInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;






/**************************************************************/
/*   [MembInfoRepo]
/*   テーブル「Member_Info」へのデータをやり取りや、
/*   エンティティ内のデータの加工を行う。
/**************************************************************/
public interface MembInfoRepo extends CrudRepository<Member_Info, Integer>{


  /***************************************************************/
  /*   [findAllJudge]
  /*   引数で渡された検索ジャンルと検索ワードに従い、検索処理に
  /*   使用するクエリメソッドを選定する。
  /*   また選定の際に、検索結果の並び順（昇順or降順）も指定する。
  /***************************************************************/
  public default Iterable<Member_Info> findAllJudge(String word, String subject, Boolean order){


    /**********************************************************************/
    /* 列挙型「FormMembInfo」を用いて、引数の検索ジャンルが列挙型内に
    /* 定義されているものか判定。
    /* 無ければ、不正な値が渡されたと判断し、空の検索結果を返す。
    /**********************************************************************/
    Optional<FormMembInfo> name = Arrays.stream(FormMembInfo.values())
                                             .parallel()
                                             .filter(s -> s.name().equals(subject))
                                             .findFirst();

    if(name.isEmpty()){
      return new ArrayList<Member_Info>();
    }


    /**********************************************************************/
    /* 検索ジャンルを元に分岐を行い、該当する検索メソッドを実行する。
    /* なお、分岐先では並び順指定に伴ってさらに分岐を行う。
    /**********************************************************************/
    Iterable<Member_Info> list = null;
    switch(name.get()){
      case All:
        list = order ? findAllOriginal() : findAllOriginalDESC();
        break;
      case Member_id:
        list = order ? findAllByMember_id(word) : findAllByMember_idDESC(word);
        break;
      case Name:
        list = order ? findAllByName(word) : findAllByNameDESC(word);
        break;
      case Name_Pronu:
        list = order ? findAllByName_pronu(word) : findAllByName_pronuDESC(word);
        break;
      case Sex:
        list = order ? findAllBySex(word) : findAllBySexDESC(word);
        break;
      case Birthday:
        list = order ? findAllByBirthday(word) : findAllByBirthdayDESC(word);
        break;
      case Join_Date:
        list = order ? findAllByJoin_date(word) : findAllByJoin_dateDESC(word);
        break;
      case Ret_Date:
        list = order ? findAllByRet_date(word) : findAllByRet_dateDESC(word);
        break;
      case Email:
        list = order ? findAllByEmail(word) : findAllByEmailDESC(word);
        break;
      case Tel:
        list = order ? findAllByTel(word) : findAllByTelDESC(word);
        break;
      case Postcode:
        list = order ? findAllByPostcode(word) : findAllByPostcodeDESC(word);
        break;
      case Addr:
        list = order ? findAllByAddr(word) : findAllByAddrDESC(word);
        break;
      case Position:
        list = order ? findAllByPosition(word) : findAllByPositionDESC(word);
        break;
      case Arri_Date:
        list = order ? findAllByArri_date(word) : findAllByArri_dateDESC(word);
        break;
      case Job:
        list = order ? findAllByJob(word) : findAllByJobDESC(word);
        break;
      case Assign_Dept:
        list = order ? findAllByAssign_dept(word) : findAllByAssign_deptDESC(word);
        break;
      case Assign_Date:
        list = order ? findAllByAssign_date(word) : findAllByAssign_dateDESC(word);
        break;
      case Inst_Charge:
        list = order ? findAllByInst_charge(word) : findAllByInst_chargeDESC(word);
        break;
      case Comment:
        list = order ? findAllByComment(word) : findAllByCommentDESC(word);
        break;
    }
    
    return list;
  }


  /************************************************************/
  /*   [findByMember_id]
  /*   指定された団員番号に該当する情報を個別で取得。
  /************************************************************/
  @Query("SELECT * FROM Member_Info WHERE member_id = :member_id LIMIT 1")
  Optional<Member_Info> findByMember_id(@Param("member_id") String member_id);


  /**************************************************************/
  /*   [findAllOriginal]
  /*   [findAllOriginalDESC]
  /*   全データを団員番号順ですべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /**************************************************************/
  @Query("SELECT * FROM Member_Info ORDER BY member_id ASC")
  Iterable<Member_Info> findAllOriginal();

  @Query("SELECT * FROM Member_Info ORDER BY member_id DESC")
  Iterable<Member_Info> findAllOriginalDESC();


  /*************************************************************************/
  /*   [findAllByMember_id]
  /*   [findAllByMember_idDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE member_id LIKE CONCAT('%', :word, '%') ORDER BY member_id ASC")
  Iterable<Member_Info> findAllByMember_id(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE member_id LIKE CONCAT('%', :word, '%') ORDER BY member_id DESC")
  Iterable<Member_Info> findAllByMember_idDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByName]
  /*   [findAllByNameDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE name LIKE CONCAT('%', :word, '%') ORDER BY name ASC")
  Iterable<Member_Info> findAllByName(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE name LIKE CONCAT('%', :word, '%') ORDER BY name DESC")
  Iterable<Member_Info> findAllByNameDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByName_pronu]
  /*   [findAllByName_pronuDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員名読み仮名五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE name_pronu LIKE CONCAT('%', :word, '%') ORDER BY name_pronu ASC")
  Iterable<Member_Info> findAllByName_pronu(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE name_pronu LIKE CONCAT('%', :word, '%') ORDER BY name_pronu DESC")
  Iterable<Member_Info> findAllByName_pronuDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllBySex]
  /*   [findAllBySexDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE sex = :word ORDER BY member_id ASC")
  Iterable<Member_Info> findAllBySex(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE sex = :word ORDER BY member_id DESC")
  Iterable<Member_Info> findAllBySexDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByBirthday]
  /*   [findAllByBirthdayDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「誕生日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE birthday = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY birthday ASC")
  Iterable<Member_Info> findAllByBirthday(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE birthday = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY birthday DESC")
  Iterable<Member_Info> findAllByBirthdayDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByJoin_date]
  /*   [findAllByJoin_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「入団日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE join_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY join_date ASC")
  Iterable<Member_Info> findAllByJoin_date(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE join_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY join_date DESC")
  Iterable<Member_Info> findAllByJoin_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByRet_date]
  /*   [findAllByRet_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「退団日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE ret_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY ret_date ASC")
  Iterable<Member_Info> findAllByRet_date(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE ret_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY ret_date DESC")
  Iterable<Member_Info> findAllByRet_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByEmail]
  /*   [findAllByEmailDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「メールアドレス五十音順」である。
  /*   ※先に「email_1」で並べ替え、同一の物は「email_2」で並べ替える。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE CONCAT(email_1 ,email_2) LIKE CONCAT('%', :word, '%') ORDER BY email_1 ASC, email_2 ASC")
  Iterable<Member_Info> findAllByEmail(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE CONCAT(email_1 ,email_2) LIKE CONCAT('%', :word, '%') ORDER BY email_1 DESC, email_2 DESC")
  Iterable<Member_Info> findAllByEmailDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByTel]
  /*   [findAllByTelDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「電話番号五十音順」である。
  /*   ※先に「tel_1」で並べ替え、同一の物は「tel_2」で並べ替える。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE CONCAT(tel_1 ,tel_2) LIKE CONCAT('%', :word, '%') ORDER BY tel_1 ASC,  tel_2 ASC")
  Iterable<Member_Info> findAllByTel(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE CONCAT(tel_1 ,tel_2) LIKE CONCAT('%', :word, '%') ORDER BY tel_1 DESC, tel_2 DESC")
  Iterable<Member_Info> findAllByTelDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByPostcode]
  /*   [findAllByPostcodeDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「郵便番号五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE addr_postcode LIKE CONCAT('%', :word, '%') ORDER BY addr_postcode ASC")
  Iterable<Member_Info> findAllByPostcode(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE addr_postcode LIKE CONCAT('%', :word, '%') ORDER BY addr_postcode DESC")
  Iterable<Member_Info> findAllByPostcodeDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByAddr]
  /*   [findAllByAddrDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「現住所五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE addr LIKE CONCAT('%', :word, '%') ORDER BY addr ASC")
  Iterable<Member_Info> findAllByAddr(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE addr LIKE CONCAT('%', :word, '%') ORDER BY addr DESC")
  Iterable<Member_Info> findAllByAddrDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByPosition]
  /*   [findAllByPositionDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「役職五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE position LIKE CONCAT('%', :word, '%') ORDER BY position ASC")
  Iterable<Member_Info> findAllByPosition(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE position LIKE CONCAT('%', :word, '%') ORDER BY position DESC")
  Iterable<Member_Info> findAllByPositionDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByArri_date]
  /*   [findAllByArri_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「役職着任日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE position_arri_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY position_arri_date ASC")
  Iterable<Member_Info> findAllByArri_date(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE position_arri_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY position_arri_date DESC")
  Iterable<Member_Info> findAllByArri_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByJob]
  /*   [findAllByJobDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「職種五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE job LIKE CONCAT('%', :word, '%') ORDER BY job ASC")
  Iterable<Member_Info> findAllByJob(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE job LIKE CONCAT('%', :word, '%') ORDER BY job DESC")
  Iterable<Member_Info> findAllByJobDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByAssign_dept]
  /*   [findAllByAssign_deptDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「配属部五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE assign_dept LIKE CONCAT('%', :word, '%') ORDER BY assign_dept ASC")
  Iterable<Member_Info> findAllByAssign_dept(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE assign_dept LIKE CONCAT('%', :word, '%') ORDER BY assign_dept DESC")
  Iterable<Member_Info> findAllByAssign_deptDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByAssign_date]
  /*   [findAllByAssign_dateDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「配属日順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE assign_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY assign_date ASC")
  Iterable<Member_Info> findAllByAssign_date(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE assign_date = STR_TO_DATE(:word, '%Y-%m-%d') ORDER BY assign_date DESC")
  Iterable<Member_Info> findAllByAssign_dateDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByInst_charge]
  /*   [findAllByInst_chargeDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「担当楽器五十音順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE inst_charge LIKE CONCAT('%', :word, '%') ORDER BY inst_charge ASC")
  Iterable<Member_Info> findAllByInst_charge(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE inst_charge LIKE CONCAT('%', :word, '%') ORDER BY inst_charge DESC")
  Iterable<Member_Info> findAllByInst_chargeDESC(@Param("word") String word);


  /*************************************************************************/
  /*   [findAllByComment]
  /*   [findAllByCommentDESC]
  /*   引数で受け取った検索ワードで、検索対象カラムで一致したデータすべて取得。
  /*   なお、それぞれのメソッドで、並び順（昇順or降順）を指定。
  /*   このメソッドでの並び順は「団員番号順」である。
  /*************************************************************************/
  @Query("SELECT * FROM Member_Info WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY member_id ASC")
  Iterable<Member_Info> findAllByComment(@Param("word") String word);

  @Query("SELECT * FROM Member_Info WHERE other_comment LIKE CONCAT('%', :word, '%') ORDER BY member_id DESC")
  Iterable<Member_Info> findAllByCommentDESC(@Param("word") String word);
}