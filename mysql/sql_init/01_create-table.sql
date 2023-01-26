-- -------------------------------------------------------
--  01_create-table
--  データベースの初回起動時に、システムの運用に必要な
--  テーブルの定義を行う。
-- -------------------------------------------------------

BEGIN;

CREATE DATABASE IF NOT EXISTS spring_schema CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE spring_schema;

-- -------------------------------------------------------
--  「団員情報」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Member_Info(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  member_id VARCHAR(50) NOT NULL UNIQUE,
  name VARCHAR(70) NOT NULL,
  name_pronu VARCHAR(120) NOT NULL,
  sex ENUM('male', 'female') NOT NULL,
  birthday DATE NOT NULL,
  face_photo MEDIUMBLOB,
  join_date DATE NOT NULL,
  ret_date DATE,
  email_1 VARCHAR(120),
  email_2 VARCHAR(120),
  tel_1 VARCHAR(40),
  tel_2 VARCHAR(40),
  addr_postcode VARCHAR(35),
  addr VARCHAR(220),
  position VARCHAR(70),
  position_arri_date DATE,
  job VARCHAR(70),
  assign_dept VARCHAR(70),
  assign_date DATE,
  inst_charge VARCHAR(70),
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「団員情報変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Member_Info_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  member_id VARCHAR(50) NOT NULL,
  name VARCHAR(70) NOT NULL,
  name_pronu VARCHAR(120) NOT NULL,
  sex ENUM('male', 'female') NOT NULL,
  birthday DATE NOT NULL,
  face_photo MEDIUMBLOB,
  join_date DATE NOT NULL,
  ret_date DATE,
  email_1 VARCHAR(120),
  email_2 VARCHAR(120),
  tel_1 VARCHAR(40),
  tel_2 VARCHAR(40),
  addr_postcode VARCHAR(35),
  addr VARCHAR(220),
  position VARCHAR(70),
  position_arri_date DATE,
  job VARCHAR(70),
  assign_dept VARCHAR(70),
  assign_date DATE,
  inst_charge VARCHAR(70),
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「設備情報」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Facility(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  faci_id VARCHAR(50) NOT NULL UNIQUE,
  faci_name VARCHAR(70) NOT NULL,
  buy_date DATE NOT NULL,
  producer VARCHAR(70) NOT NULL,
  storage_loc VARCHAR(70),
  disp_date DATE,
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「設備情報変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Facility_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  faci_id VARCHAR(50) NOT NULL,
  faci_name VARCHAR(70) NOT NULL,
  buy_date DATE NOT NULL,
  producer VARCHAR(70) NOT NULL,
  storage_loc VARCHAR(70),
  disp_date DATE,
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「設備写真データ」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Facility_Photo(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  faci_id VARCHAR(50) NOT NULL,
  photo_name VARCHAR(70) NOT NULL,
  photo_hash VARCHAR(300) NOT NULL,

  FOREIGN KEY (faci_id) REFERENCES spring_schema.Facility(faci_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「設備写真データ変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Facility_Photo_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  faci_id VARCHAR(50) NOT NULL,
  photo_name VARCHAR(70) NOT NULL,
  photo_hash VARCHAR(300) NOT NULL
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「楽譜情報」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Musical_Score(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  score_id VARCHAR(50) NOT NULL UNIQUE,
  buy_date DATE NOT NULL,
  song_title VARCHAR(70) NOT NULL,
  composer VARCHAR(70) NOT NULL,
  arranger VARCHAR(70),
  publisher VARCHAR(70),
  storage_loc VARCHAR(70),
  disp_date DATE,
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「楽譜情報変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Musical_Score_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  score_id VARCHAR(50) NOT NULL,
  buy_date DATE NOT NULL,
  song_title VARCHAR(70) NOT NULL,
  composer VARCHAR(70) NOT NULL,
  arranger VARCHAR(70),
  publisher VARCHAR(70),
  storage_loc VARCHAR(70),
  disp_date DATE,
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「楽譜データ」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Score_Pdf(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  score_id VARCHAR(50) NOT NULL,
  score_name VARCHAR(70) NOT NULL,
  pdf_hash VARCHAR(300) NOT NULL,

  FOREIGN KEY (score_id) REFERENCES spring_schema.Musical_Score(score_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「楽譜データ変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Score_Pdf_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  score_id VARCHAR(50) NOT NULL,
  score_name VARCHAR(70) NOT NULL,
  pdf_hash VARCHAR(300) NOT NULL
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「音源情報」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Sound_Source(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  sound_id VARCHAR(50) NOT NULL UNIQUE,
  upload_date DATE NOT NULL,
  song_title VARCHAR(70) NOT NULL,
  composer VARCHAR(70) NOT NULL,
  performer VARCHAR(70) NOT NULL,
  publisher VARCHAR(70),
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「音源情報変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Sound_Source_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  sound_id VARCHAR(50) NOT NULL,
  upload_date DATE NOT NULL,
  song_title VARCHAR(70) NOT NULL,
  composer VARCHAR(70) NOT NULL,
  performer VARCHAR(70) NOT NULL,
  publisher VARCHAR(70),
  other_comment VARCHAR(420)
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「音源データ」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Audio_Data(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  sound_id VARCHAR(50) NOT NULL,
  sound_name VARCHAR(70) NOT NULL,
  audio_hash VARCHAR(300) NOT NULL,

  FOREIGN KEY (sound_id) REFERENCES spring_schema.Sound_Source(sound_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「音源データ変更履歴」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Audio_Data_History(
  history_id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  change_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  change_kinds ENUM('insert', 'update', 'delete', 'rollback') NOT NULL,
  operation_user VARCHAR(40) NOT NULL,
  serial_num INT UNSIGNED NOT NULL,
  sound_id VARCHAR(50) NOT NULL,
  sound_name VARCHAR(70) NOT NULL,
  audio_hash VARCHAR(300) NOT NULL
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「システム利用権限」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.Usage_Authority(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  auth_id VARCHAR(50) NOT NULL UNIQUE,
  auth_name VARCHAR(70) NOT NULL,
  admin BOOLEAN NOT NULL,
  member_info ENUM('none', 'brows', 'change', 'delete', 'hist', 'rollback') NOT NULL,
  facility ENUM('none', 'brows', 'change', 'delete', 'hist', 'rollback') NOT NULL,
  musical_score ENUM('none', 'brows', 'change', 'delete', 'hist', 'rollback') NOT NULL,
  sound_source ENUM('none', 'brows', 'change', 'delete', 'hist', 'rollback') NOT NULL
) DEFAULT CHARSET=utf8mb4;

-- -------------------------------------------------------
--  「システムユーザー情報」の保存に使用する。
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS spring_schema.System_User(
  serial_num INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  member_id VARCHAR(50) NOT NULL UNIQUE,
  username VARCHAR(40) NOT NULL UNIQUE,
  password VARCHAR(300) NOT NULL,
  auth_id VARCHAR(50) NOT NULL,
  fail_count TINYINT UNSIGNED NOT NULL,
  locking BOOLEAN NOT NULL,

  FOREIGN KEY (member_id) REFERENCES spring_schema.Member_Info(member_id) ON UPDATE CASCADE,
  FOREIGN KEY (auth_id) REFERENCES spring_schema.Usage_Authority(auth_id) ON UPDATE CASCADE
) DEFAULT CHARSET=utf8mb4;

COMMIT;