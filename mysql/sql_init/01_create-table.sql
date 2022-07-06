BEGIN;

CREATE DATABASE IF NOT EXISTS spring_schema CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE spring_schema;

CREATE TABLE IF NOT EXISTS spring_schema.Member_Info(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  member_id VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(200) NOT NULL,
  name_pronu VARCHAR(200) NOT NULL,
  sex ENUM('male', 'female') NOT NULL,
  birthday DATE NOT NULL,
  face_photo MEDIUMBLOB,
  join_date DATE NOT NULL,
  ret_date DATE,
  email_1 VARCHAR(200),
  email_2 VARCHAR(200),
  tel_1 VARCHAR(20),
  tel_2 VARCHAR(20),
  addr_postcode VARCHAR(20),
  addr VARCHAR(800),
  position VARCHAR(200),
  position_arri_date DATE,
  job VARCHAR(200),
  assign_dept VARCHAR(200),
  assign_date DATE,
  inst_charge VARCHAR(200),
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Member_Info_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  member_id VARCHAR(100) NOT NULL,
  name VARCHAR(200) NOT NULL,
  name_pronu VARCHAR(200) NOT NULL,
  sex ENUM('male', 'female') NOT NULL,
  birthday DATE NOT NULL,
  face_photo MEDIUMBLOB,
  join_date DATE NOT NULL,
  ret_date DATE,
  email_1 VARCHAR(200),
  email_2 VARCHAR(200),
  tel_1 VARCHAR(20),
  tel_2 VARCHAR(20),
  addr_postcode VARCHAR(20),
  addr VARCHAR(800),
  position VARCHAR(200),
  position_arri_date DATE,
  job VARCHAR(200),
  assign_dept VARCHAR(200),
  assign_date DATE,
  inst_charge VARCHAR(200),
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval_Items(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  evalsheet_id VARCHAR(100) NOT NULL UNIQUE,
  evalsheet_name VARCHAR(200) NOT NULL,
  evalsheet_date DATE NOT NULL,
  evalsheet_kinds ENUM('recruit', 'eval', 'audition') NOT NULL,
  eval_item_contents_1 VARCHAR(500),
  eval_item_kinds_1 VARCHAR(200),
  eval_item_contents_2 VARCHAR(500),
  eval_item_kinds_2 VARCHAR(200),
  eval_item_contents_3 VARCHAR(500),
  eval_item_kinds_3 VARCHAR(200),
  eval_item_contents_4 VARCHAR(500),
  eval_item_kinds_4 VARCHAR(200),
  eval_item_contents_5 VARCHAR(500),
  eval_item_kinds_5 VARCHAR(200),
  eval_item_contents_6 VARCHAR(500),
  eval_item_kinds_6 VARCHAR(200),
  eval_item_contents_7 VARCHAR(500),
  eval_item_kinds_7 VARCHAR(200),
  eval_item_contents_8 VARCHAR(500),
  eval_item_kinds_8 VARCHAR(200),
  eval_item_contents_9 VARCHAR(500),
  eval_item_kinds_9 VARCHAR(200),
  eval_item_contents_10 VARCHAR(500),
  eval_item_kinds_10 VARCHAR(200)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval_Items_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  evalsheet_id VARCHAR(100) NOT NULL,
  evalsheet_name VARCHAR(200) NOT NULL,
  evalsheet_date DATE NOT NULL,
  evalsheet_kinds ENUM('recruit', 'eval', 'audition') NOT NULL,
  eval_item_contents_1 VARCHAR(500),
  eval_item_kinds_1 VARCHAR(200),
  eval_item_contents_2 VARCHAR(500),
  eval_item_kinds_2 VARCHAR(200),
  eval_item_contents_3 VARCHAR(500),
  eval_item_kinds_3 VARCHAR(200),
  eval_item_contents_4 VARCHAR(500),
  eval_item_kinds_4 VARCHAR(200),
  eval_item_contents_5 VARCHAR(500),
  eval_item_kinds_5 VARCHAR(200),
  eval_item_contents_6 VARCHAR(500),
  eval_item_kinds_6 VARCHAR(200),
  eval_item_contents_7 VARCHAR(500),
  eval_item_kinds_7 VARCHAR(200),
  eval_item_contents_8 VARCHAR(500),
  eval_item_kinds_8 VARCHAR(200),
  eval_item_contents_9 VARCHAR(500),
  eval_item_kinds_9 VARCHAR(200),
  eval_item_contents_10 VARCHAR(500),
  eval_item_kinds_10 VARCHAR(200)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  eval_id VARCHAR(100) NOT NULL UNIQUE,
  member_id VARCHAR(100) NOT NULL,
  eval_name VARCHAR(200) NOT NULL,
  eval_date DATE NOT NULL,
  evalsheet_id VARCHAR(100) NOT NULL,
  eval_contents_1 VARCHAR(500),
  eval_rank_1 VARCHAR(500),
  eval_contents_2 VARCHAR(500),
  eval_rank_2 VARCHAR(500),
  eval_contents_3 VARCHAR(500),
  eval_rank_3 VARCHAR(500),
  eval_contents_4 VARCHAR(500),
  eval_rank_4 VARCHAR(500),
  eval_contents_5 VARCHAR(500),
  eval_rank_5 VARCHAR(500),
  eval_contents_6 VARCHAR(500),
  eval_rank_6 VARCHAR(500),
  eval_contents_7 VARCHAR(500),
  eval_rank_7 VARCHAR(500),
  eval_contents_8 VARCHAR(500),
  eval_rank_8 VARCHAR(500),
  eval_contents_9 VARCHAR(500),
  eval_rank_9 VARCHAR(500),
  eval_contents_10 VARCHAR(500),
  eval_rank_10 VARCHAR(500),

  FOREIGN KEY (member_id) REFERENCES spring_schema.Member_Info(member_id) ON UPDATE CASCADE,
  FOREIGN KEY (evalsheet_id) REFERENCES spring_schema.Rec_Eval_Items(evalsheet_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  eval_id VARCHAR(100) NOT NULL,
  member_id VARCHAR(100) NOT NULL,
  eval_name VARCHAR(200) NOT NULL,
  eval_date DATE NOT NULL,
  evalsheet_id VARCHAR(100) NOT NULL,
  eval_contents_1 VARCHAR(500),
  eval_rank_1 VARCHAR(500),
  eval_contents_2 VARCHAR(500),
  eval_rank_2 VARCHAR(500),
  eval_contents_3 VARCHAR(500),
  eval_rank_3 VARCHAR(500),
  eval_contents_4 VARCHAR(500),
  eval_rank_4 VARCHAR(500),
  eval_contents_5 VARCHAR(500),
  eval_rank_5 VARCHAR(500),
  eval_contents_6 VARCHAR(500),
  eval_rank_6 VARCHAR(500),
  eval_contents_7 VARCHAR(500),
  eval_rank_7 VARCHAR(500),
  eval_contents_8 VARCHAR(500),
  eval_rank_8 VARCHAR(500),
  eval_contents_9 VARCHAR(500),
  eval_rank_9 VARCHAR(500),
  eval_contents_10 VARCHAR(500),
  eval_rank_10 VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval_Record(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  eval_id VARCHAR(100) NOT NULL,
  data_kinds ENUM('audio', 'photo', 'movie') NOT NULL,
  record_name VARCHAR(200) NOT NULL,
  record_data LONGBLOB NOT NULL,

  FOREIGN KEY (eval_id) REFERENCES spring_schema.Rec_Eval(eval_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Rec_Eval_Record_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  eval_id VARCHAR(100) NOT NULL,
  data_kinds ENUM('audio', 'photo', 'movie') NOT NULL,
  record_name VARCHAR(200) NOT NULL,
  record_data LONGBLOB NOT NULL,
  before_length INT UNSIGNED NOT NULL
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Facility(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  faci_id VARCHAR(100) NOT NULL UNIQUE,
  faci_name VARCHAR(200) NOT NULL,
  chief_admin VARCHAR(100),
  resp_person VARCHAR(100),
  buy_date DATE NOT NULL,
  storage_loc VARCHAR(200),
  buy_price INT(10),
  disp_date DATE,
  other_comment VARCHAR(500),

  FOREIGN KEY (chief_admin) REFERENCES spring_schema.Member_Info(member_id) ON UPDATE CASCADE,
  FOREIGN KEY (resp_person) REFERENCES spring_schema.Member_Info(member_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Facility_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  faci_id VARCHAR(100) NOT NULL,
  faci_name VARCHAR(200) NOT NULL,
  chief_admin VARCHAR(100),
  resp_person VARCHAR(100),
  buy_date DATE NOT NULL,
  storage_loc VARCHAR(200),
  buy_price INT(10),
  disp_date DATE,
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Inspect_Items(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  inspsheet_id VARCHAR(100) NOT NULL UNIQUE,
  inspsheet_name VARCHAR(200) NOT NULL,
  inspsheet_date DATE NOT NULL,
  insp_item_contents_1 VARCHAR(500),
  insp_item_kinds_1 VARCHAR(200),
  insp_item_unit_1 VARCHAR(100),
  insp_item_contents_2 VARCHAR(500),
  insp_item_kinds_2 VARCHAR(200),
  insp_item_unit_2 VARCHAR(100),
  insp_item_contents_3 VARCHAR(500),
  insp_item_kinds_3 VARCHAR(200),
  insp_item_unit_3 VARCHAR(100),
  insp_item_contents_4 VARCHAR(500),
  insp_item_kinds_4 VARCHAR(200),
  insp_item_unit_4 VARCHAR(100),
  insp_item_contents_5 VARCHAR(500),
  insp_item_kinds_5 VARCHAR(200),
  insp_item_unit_5 VARCHAR(100),
  insp_item_contents_6 VARCHAR(500),
  insp_item_kinds_6 VARCHAR(200),
  insp_item_unit_6 VARCHAR(100),
  insp_item_contents_7 VARCHAR(500),
  insp_item_kinds_7 VARCHAR(200),
  insp_item_unit_7 VARCHAR(100),
  insp_item_contents_8 VARCHAR(500),
  insp_item_kinds_8 VARCHAR(200),
  insp_item_unit_8 VARCHAR(100),
  insp_item_contents_9 VARCHAR(500),
  insp_item_kinds_9 VARCHAR(200),
  insp_item_unit_9 VARCHAR(100),
  insp_item_contents_10 VARCHAR(500),
  insp_item_kinds_10 VARCHAR(200),
  insp_item_unit_10 VARCHAR(100)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Inspect_Items_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  inspsheet_id VARCHAR(100) NOT NULL,
  inspsheet_name VARCHAR(200) NOT NULL,
  inspsheet_date DATE NOT NULL,
  insp_item_contents_1 VARCHAR(500),
  insp_item_kinds_1 VARCHAR(200),
  insp_item_unit_1 VARCHAR(100),
  insp_item_contents_2 VARCHAR(500),
  insp_item_kinds_2 VARCHAR(200),
  insp_item_unit_2 VARCHAR(100),
  insp_item_contents_3 VARCHAR(500),
  insp_item_kinds_3 VARCHAR(200),
  insp_item_unit_3 VARCHAR(100),
  insp_item_contents_4 VARCHAR(500),
  insp_item_kinds_4 VARCHAR(200),
  insp_item_unit_4 VARCHAR(100),
  insp_item_contents_5 VARCHAR(500),
  insp_item_kinds_5 VARCHAR(200),
  insp_item_unit_5 VARCHAR(100),
  insp_item_contents_6 VARCHAR(500),
  insp_item_kinds_6 VARCHAR(200),
  insp_item_unit_6 VARCHAR(100),
  insp_item_contents_7 VARCHAR(500),
  insp_item_kinds_7 VARCHAR(200),
  insp_item_unit_7 VARCHAR(100),
  insp_item_contents_8 VARCHAR(500),
  insp_item_kinds_8 VARCHAR(200),
  insp_item_unit_8 VARCHAR(100),
  insp_item_contents_9 VARCHAR(500),
  insp_item_kinds_9 VARCHAR(200),
  insp_item_unit_9 VARCHAR(100),
  insp_item_contents_10 VARCHAR(500),
  insp_item_kinds_10 VARCHAR(200),
  insp_item_unit_10 VARCHAR(100)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Equip_Inspect(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  inspect_id VARCHAR(100) NOT NULL UNIQUE,
  faci_id VARCHAR(100) NOT NULL,
  inspect_name VARCHAR(200) NOT NULL,
  inspect_date DATE NOT NULL,
  inspsheet_id VARCHAR(100) NOT NULL,
  insp_contents_1 VARCHAR(500),
  insp_rank_1 VARCHAR(500),
  insp_contents_2 VARCHAR(500),
  insp_rank_2 VARCHAR(500),
  insp_contents_3 VARCHAR(500),
  insp_rank_3 VARCHAR(500),
  insp_contents_4 VARCHAR(500),
  insp_rank_4 VARCHAR(500),
  insp_contents_5 VARCHAR(500),
  insp_rank_5 VARCHAR(500),
  insp_contents_6 VARCHAR(500),
  insp_rank_6 VARCHAR(500),
  insp_contents_7 VARCHAR(500),
  insp_rank_7 VARCHAR(500),
  insp_contents_8 VARCHAR(500),
  insp_rank_8 VARCHAR(500),
  insp_contents_9 VARCHAR(500),
  insp_rank_9 VARCHAR(500),
  insp_contents_10 VARCHAR(500),
  insp_rank_10 VARCHAR(500),

  FOREIGN KEY (faci_id) REFERENCES spring_schema.Facility(faci_id) ON UPDATE CASCADE,
  FOREIGN KEY (inspsheet_id) REFERENCES spring_schema.Inspect_Items(inspsheet_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Equip_Inspect_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  inspect_id VARCHAR(100) NOT NULL,
  faci_id VARCHAR(100) NOT NULL,
  inspect_name VARCHAR(200) NOT NULL,
  inspect_date DATE NOT NULL,
  inspsheet_id VARCHAR(100) NOT NULL,
  insp_contents_1 VARCHAR(500),
  insp_rank_1 VARCHAR(500),
  insp_contents_2 VARCHAR(500),
  insp_rank_2 VARCHAR(500),
  insp_contents_3 VARCHAR(500),
  insp_rank_3 VARCHAR(500),
  insp_contents_4 VARCHAR(500),
  insp_rank_4 VARCHAR(500),
  insp_contents_5 VARCHAR(500),
  insp_rank_5 VARCHAR(500),
  insp_contents_6 VARCHAR(500),
  insp_rank_6 VARCHAR(500),
  insp_contents_7 VARCHAR(500),
  insp_rank_7 VARCHAR(500),
  insp_contents_8 VARCHAR(500),
  insp_rank_8 VARCHAR(500),
  insp_contents_9 VARCHAR(500),
  insp_rank_9 VARCHAR(500),
  insp_contents_10 VARCHAR(500),
  insp_rank_10 VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Equip_Inspect_Photo(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  inspect_id VARCHAR(100) NOT NULL,
  photo_name VARCHAR(200) NOT NULL,
  photo_data MEDIUMBLOB NOT NULL,

  FOREIGN KEY (inspect_id) REFERENCES spring_schema.Equip_Inspect(inspect_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Equip_Inspect_Photo_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  inspect_id VARCHAR(100) NOT NULL,
  photo_name VARCHAR(200) NOT NULL,
  photo_data MEDIUMBLOB NOT NULL,
  before_length INT UNSIGNED NOT NULL
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Usage_Authority(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  auth_id VARCHAR(100) NOT NULL UNIQUE,
  auth_name VARCHAR(200) NOT NULL,
  admin BOOLEAN NOT NULL DEFAULT false,
  m_i_brows BOOLEAN NOT NULL DEFAULT false,
  r_e_brows BOOLEAN NOT NULL DEFAULT false,
  f_brows BOOLEAN NOT NULL DEFAULT false,
  e_i_brows BOOLEAN NOT NULL DEFAULT false,
  m_s_brows BOOLEAN NOT NULL DEFAULT false,
  s_s_brows BOOLEAN NOT NULL DEFAULT false,
  m_i_change BOOLEAN NOT NULL DEFAULT false,
  r_e_change BOOLEAN NOT NULL DEFAULT false,
  f_change BOOLEAN NOT NULL DEFAULT false,
  e_i_change BOOLEAN NOT NULL DEFAULT false,
  m_s_change BOOLEAN NOT NULL DEFAULT false,
  s_s_change BOOLEAN NOT NULL DEFAULT false,
  m_i_delete BOOLEAN NOT NULL DEFAULT false,
  r_e_delete BOOLEAN NOT NULL DEFAULT false,
  f_delete BOOLEAN NOT NULL DEFAULT false,
  e_i_delete BOOLEAN NOT NULL DEFAULT false,
  m_s_delete BOOLEAN NOT NULL DEFAULT false,
  s_s_delete BOOLEAN NOT NULL DEFAULT false,
  m_i_hist_brows BOOLEAN NOT NULL DEFAULT false,
  r_e_hist_brows BOOLEAN NOT NULL DEFAULT false,
  f_hist_brows BOOLEAN NOT NULL DEFAULT false,
  e_i_hist_brows BOOLEAN NOT NULL DEFAULT false,
  m_s_hist_brows BOOLEAN NOT NULL DEFAULT false,
  s_s_hist_brows BOOLEAN NOT NULL DEFAULT false,
  m_i_hist_rollback BOOLEAN NOT NULL DEFAULT false,
  r_e_hist_rollback BOOLEAN NOT NULL DEFAULT false,
  f_hist_rollback BOOLEAN NOT NULL DEFAULT false,
  e_i_hist_rollback BOOLEAN NOT NULL DEFAULT false,
  m_s_hist_rollback BOOLEAN NOT NULL DEFAULT false,
  s_s_hist_rollback BOOLEAN NOT NULL DEFAULT false
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.System_User(
  member_id VARCHAR(100) PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(600) NOT NULL,
  auth_id VARCHAR(100) NOT NULL,
  fail_count INT(2) NOT NULL DEFAULT 0,
  locking BOOLEAN NOT NULL DEFAULT false,

  FOREIGN KEY (member_id) REFERENCES spring_schema.Member_Info(member_id) ON UPDATE CASCADE,
  FOREIGN KEY (auth_id) REFERENCES spring_schema.Usage_Authority(auth_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Musical_Score(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  score_id VARCHAR(100) NOT NULL UNIQUE,
  buy_date DATE NOT NULL,
  song_title VARCHAR(200) NOT NULL,
  composer VARCHAR(200) NOT NULL,
  arranger VARCHAR(200),
  publisher VARCHAR(200),
  strage_loc VARCHAR(200),
  disp_date DATE,
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Musical_Score_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  score_id VARCHAR(100) NOT NULL,
  buy_date DATE NOT NULL,
  song_title VARCHAR(200) NOT NULL,
  composer VARCHAR(200) NOT NULL,
  arranger VARCHAR(200),
  publisher VARCHAR(200),
  strage_loc VARCHAR(200),
  disp_date DATE,
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Score_Pdf(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  score_id VARCHAR(100) NOT NULL,
  score_name VARCHAR(200) NOT NULL,
  pdf_data LONGBLOB NOT NULL,

  FOREIGN KEY (score_id) REFERENCES spring_schema.Musical_Score(score_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Score_Pdf_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  score_id VARCHAR(100) NOT NULL,
  score_name VARCHAR(200) NOT NULL,
  pdf_data LONGBLOB NOT NULL,
  before_length INT UNSIGNED NOT NULL
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Sound_Source(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  sound_id VARCHAR(100) NOT NULL UNIQUE,
  upload_date DATE NOT NULL,
  song_title VARCHAR(200) NOT NULL,
  composer VARCHAR(200) NOT NULL,
  performer VARCHAR(200) NOT NULL,
  publisher VARCHAR(200),
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Sound_Source_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  sound_id VARCHAR(100) NOT NULL,
  upload_date DATE NOT NULL,
  song_title VARCHAR(200) NOT NULL,
  composer VARCHAR(200) NOT NULL,
  performer VARCHAR(200) NOT NULL,
  publisher VARCHAR(200),
  other_comment VARCHAR(500)
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Audio_Data(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  sound_id VARCHAR(100) NOT NULL,
  sound_name VARCHAR(200) NOT NULL,
  audio_data LONGBLOB NOT NULL,

  FOREIGN KEY (sound_id) REFERENCES spring_schema.Sound_Source(sound_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS spring_schema.Audio_Data_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(100) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  sound_id VARCHAR(100) NOT NULL,
  sound_name VARCHAR(200) NOT NULL,
  audio_data LONGBLOB NOT NULL,
  before_length INT UNSIGNED NOT NULL
) DEFAULT CHARSET=utf8mb4;

COMMIT;