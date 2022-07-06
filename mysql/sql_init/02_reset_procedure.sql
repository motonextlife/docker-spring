BEGIN;

DELIMITER //

CREATE PROCEDURE mysql.Reset_Procedure()
BEGIN

  DECLARE EXIT HANDLER FOR SQLEXCEPTION, SQLWARNING ROLLBACK;
  
  START TRANSACTION;
  
  TRUNCATE TABLE spring_schema.System_User;
  TRUNCATE TABLE spring_schema.Usage_Authority;
  TRUNCATE TABLE spring_schema.Audio_Data_History;
  TRUNCATE TABLE spring_schema.Audio_Data;
  TRUNCATE TABLE spring_schema.Sound_Source_History;
  TRUNCATE TABLE spring_schema.Sound_Source;
  TRUNCATE TABLE spring_schema.Score_Pdf_History;
  TRUNCATE TABLE spring_schema.Score_Pdf;
  TRUNCATE TABLE spring_schema.Musical_Score_History;
  TRUNCATE TABLE spring_schema.Musical_Score;
  TRUNCATE TABLE spring_schema.Equip_Inspect_Photo_History;
  TRUNCATE TABLE spring_schema.Equip_Inspect_Photo;
  TRUNCATE TABLE spring_schema.Equip_Inspect_History;
  TRUNCATE TABLE spring_schema.Equip_Inspect;
  TRUNCATE TABLE spring_schema.Inspect_Items_History;
  TRUNCATE TABLE spring_schema.Inspect_Items;
  TRUNCATE TABLE spring_schema.Facility_History;
  TRUNCATE TABLE spring_schema.Facility;
  TRUNCATE TABLE spring_schema.Rec_Eval_Record_History;
  TRUNCATE TABLE spring_schema.Rec_Eval_Record;
  TRUNCATE TABLE spring_schema.Rec_Eval_History;
  TRUNCATE TABLE spring_schema.Rec_Eval;
  TRUNCATE TABLE spring_schema.Rec_Eval_Items_History;
  TRUNCATE TABLE spring_schema.Rec_Eval_Items;
  TRUNCATE TABLE spring_schema.Member_Info_History;
  TRUNCATE TABLE spring_schema.Member_Info;

  COMMIT;

END;
//

DELIMITER ;

COMMIT;