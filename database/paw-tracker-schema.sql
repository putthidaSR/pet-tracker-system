/*
 * Database schema for Pet Tracker System
 * Date: 12/5/2021
 */
 
DROP DATABASE IF EXISTS `paw_tracker`;
CREATE DATABASE IF NOT EXISTS `paw_tracker`;
USE `paw_tracker`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------------
-- Table structure for user
-- --------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` INT NOT NULL AUTO_INCREMENT, 
  `creation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modification_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,     
  `login_name` VARCHAR(100) NOT NULL,
  `login_password` VARCHAR(100) NOT NULL,
  
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

-- Password validation: Must be between 6 and 15 chars
ALTER TABLE `user` ADD CONSTRAINT check_password 
    CHECK ( 
                    LENGTH(login_password) >= 6
            AND     LENGTH(login_password) <= 15
    );

-- Login name must be unique
ALTER TABLE `user` ADD CONSTRAINT check_login_name_unique UNIQUE (`login_name`);

-- -----------------------------------
-- Table structure for account_detail
-- -----------------------------------
DROP TABLE IF EXISTS `account_detail`;
CREATE TABLE `account_detail`  (
  `id` INT NOT NULL AUTO_INCREMENT,  
  `role` VARCHAR(100) NOT NULL CHECK (role IN  ('Admin', 'PetOwner', 'Veterinarian')),
  `badge_number` VARCHAR(50) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,
  `phone_number` VARCHAR(50) DEFAULT NULL,
  `address` VARCHAR(100) DEFAULT NULL,
  `active` BOOLEAN NOT NULL DEFAULT 0,
  `confirmation_code` INT DEFAULT NULL,

  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

/* If email value is not null, it must satisfy the following validations:
 * - At least one char before @
 * - At least two chars between @ and .
 * - At least two chars between .and the end
 */
ALTER TABLE `account_detail` ADD CONSTRAINT check_email CHECK (email like '%_@__%.__%'  OR email IS NULL);
    
-- ----------------------------
-- Table structure for pet
-- ----------------------------
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet`  (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rfid_number` VARCHAR(64) NOT NULL,
  `registration_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modification_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,    
  `user_id` INT NOT NULL,
  
  PRIMARY KEY (`id`) USING BTREE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`)
) ENGINE = InnoDB;

-- RFID tag number must be unique
ALTER TABLE `pet` ADD CONSTRAINT check_rfid_unique UNIQUE (`rfid_number`);

-- -------------------------------------------
-- Table structure for pet_detail
-- -------------------------------------------
DROP TABLE IF EXISTS `pet_detail`;
CREATE TABLE `pet_detail`  (
  `pet_detail_id` INT NOT NULL AUTO_INCREMENT,
  `pet_id` INT NOT NULL,
  `species` VARCHAR(20) NOT NULL CHECK (species IN  ('Cat', 'Dog', 'Other')),
  `name` VARCHAR(32) DEFAULT NULL,
  `age` VARCHAR(50) DEFAULT NULL,
  `breed` VARCHAR(50) DEFAULT NULL,
  `color` VARCHAR(50) DEFAULT NULL,
  `gender` VARCHAR(20) NOT NULL CHECK (gender IN  ('Male', 'Female', 'Neutered')),
  `active` BOOLEAN NOT NULL DEFAULT 1,
  
  PRIMARY KEY (`pet_detail_id`) USING BTREE,
  FOREIGN KEY (`pet_id`) REFERENCES pet(`id`)
) ENGINE = InnoDB;

-- ------------------------------------
-- Table structure for pet_vaccination
-- ------------------------------------
DROP TABLE IF EXISTS `pet_vaccination`;
CREATE TABLE `pet_vaccination`  (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pet_id` INT NOT NULL,
  `creation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `modification_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,     
  `vaccination_name` VARCHAR(500) DEFAULT NULL,
  `immunization_date` DATETIME DEFAULT NULL,
  `veterinarian_name` VARCHAR(100) DEFAULT NULL,
  `veterinarian_contact` VARCHAR(100) DEFAULT NULL,
  
  PRIMARY KEY (`id`) USING BTREE,
  FOREIGN KEY (`pet_id`) REFERENCES pet(`id`)
) ENGINE = InnoDB;

-- ----------------------------
-- Table structure for pet_medical
-- ----------------------------
DROP TABLE IF EXISTS `pet_medical`;
CREATE TABLE `pet_medical`  (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pet_id` INT NOT NULL,
  `medical` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `medical_assign_date` DATETIME DEFAULT NULL,
  `creation_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci;

-- --------------------------------
-- Table structure for pet_location
-- --------------------------------
DROP TABLE IF EXISTS `pet_location`;
CREATE TABLE `pet_location`  (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pet_id` INT NOT NULL,
  `latitude` DOUBLE DEFAULT 0.0,
  `longitude` DOUBLE DEFAULT 0.0,
  `address` VARCHAR(100) DEFAULT NULL,
  `last_seen` DATETIME DEFAULT NULL,
  
  PRIMARY KEY (`id`) USING BTREE,
  FOREIGN KEY (`pet_id`) REFERENCES pet(`id`)
) ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS = 1;
