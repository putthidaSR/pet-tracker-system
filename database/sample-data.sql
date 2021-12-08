-- ------------------------------------------
-- Records of user and its associated details
-- ------------------------------------------
-- Insert admin record
BEGIN;
INSERT INTO user (login_name, login_password) VALUES ("admin", "admin21!");
INSERT INTO account_detail (role) VALUES ("Admin");
COMMIT;

-- Insert veterinarians
BEGIN;
INSERT INTO user (login_name, login_password) VALUES ("vet1", "123456!");
INSERT INTO account_detail (role, badge_number) VALUES ("Veterinarian", "123456AB");

INSERT INTO user (login_name, login_password) VALUES ("vet2", "123456!");
INSERT INTO account_detail (role, badge_number) VALUES ("Veterinarian", "123456AC");

INSERT INTO user (login_name, login_password) VALUES ("vet3", "123456!");
INSERT INTO account_detail (role, badge_number) VALUES ("Veterinarian", "123456ABD");
COMMIT;

-- Insert pet owner
BEGIN;        
INSERT INTO user (login_name, login_password) VALUES ("thida", "123456!");
INSERT INTO account_detail (role, email, phone_number, address) VALUES ("PetOwner", "psamrith@uw.edu", "2065966256", "5568 E Pacific, Tacoma, WA, 98403");

INSERT INTO user (login_name, login_password) VALUES ("user1", "123456!");
INSERT INTO account_detail (role, email, phone_number, address) VALUES ("PetOwner", "test1@gmail.com", "2064563455", "3421 E Pacific, Tacoma, WA, 98402");

INSERT INTO user (login_name, login_password) VALUES ("user2", "123456!");
INSERT INTO account_detail (role, email, phone_number, address) VALUES ("PetOwner", "test2@gmail.com", "2064563455", "4436 S Pacific, Tacoma, WA, 98405");

INSERT INTO user (login_name, login_password) VALUES ("user3", "123456!");
INSERT INTO account_detail (role, email, phone_number, address) VALUES ("PetOwner", "test3@gmail.com", "2064563455", "4490 E Pacific, Tacoma, WA, 98403");
COMMIT;

-- ------------------------------
-- Records of pet and pet_detail
-- ------------------------------
BEGIN;
INSERT INTO pet (rfid_number, user_id) VALUES ("123456", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (1, "Cat", "Meow", "5 months", "Ragdoll", "white", "Female");

INSERT INTO pet (rfid_number, user_id) VALUES ("187654", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (2, "Dog", "Chance", "7 months", "Pitbull", "black", "Male");

INSERT INTO pet (rfid_number, user_id) VALUES ("0023654", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (3, "Cat", "Tiger", "2 years", "Savannah cat", "yellow/gray", "Male");

INSERT INTO pet (rfid_number, user_id) VALUES ("055654", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (4, "Dog", "Fluffy", "4 years", "Pug", "brown", "Neutered");

INSERT INTO pet (rfid_number, user_id) VALUES ("2047654", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (5, "Cat", "Prada", "9 months", "Maine Coon", "gray", "Male");

INSERT INTO pet (rfid_number, user_id) VALUES ("003654", 5);
INSERT INTO pet_detail (pet_id, species, name, age, breed, color, gender) VALUES (6, "Dog", "Bella", "9 months", "Golden Retriever", "brown", "Neutered");
COMMIT;

-- ----------------------------
-- Records of pet_vaccination
-- ----------------------------
BEGIN;
INSERT INTO pet_vaccination (pet_id, vaccination_name, immunization_date, veterinarian_name, veterinarian_contact) VALUES (1, "Canine Parvovirus", '2021-01-01 12:30:00', "Lakewood Animal Shelter", "(206) 591-2543");
INSERT INTO pet_vaccination (pet_id, vaccination_name, immunization_date, veterinarian_name, veterinarian_contact) VALUES (1, "Canine Distemper", '2021-12-01 15:30:00', "Lakewood Animal Shelter", "(206) 591-2543");
INSERT INTO pet_vaccination (pet_id, vaccination_name, immunization_date, veterinarian_name, veterinarian_contact) VALUES (1, "Rabies", '2021-11-01 16:30:00', "Lakewood Animal Shelter", "(206) 591-2543");
INSERT INTO pet_vaccination (pet_id, vaccination_name, immunization_date, veterinarian_name, veterinarian_contact) VALUES (1, "Hepatitis", '2021-08-01 13:30:00', "Lakewood Animal Shelter", "(206) 591-2543");
INSERT INTO pet_vaccination (pet_id, vaccination_name, immunization_date, veterinarian_name, veterinarian_contact) VALUES (1, "Leptospirosis", '2021-01-01 18:30:00', "Lakewood Animal Shelter", "(206) 591-2543");
COMMIT;
-- ----------------------------------
-- Records of pet_medical
-- ----------------------------------
BEGIN;
INSERT INTO pet_medical (pet_id, medical, medical_assign_date) VALUES (1, "Allergies: None, Diet Restriction: None", '2021-01-01 18:30:00');
INSERT INTO pet_medical (pet_id, medical, medical_assign_date) VALUES (1, "Allergies with peanut and sweet potato", '2021-02-01 13:30:00');
INSERT INTO pet_medical (pet_id, medical, medical_assign_date) VALUES (1, "Lack of protein, need more excise", '2021-03-01 14:30:00');
INSERT INTO pet_medical (pet_id, medical, medical_assign_date) VALUES (2, "Overweight", '2021-04-01 15:30:00');
INSERT INTO pet_medical (pet_id, medical, medical_assign_date) VALUES (2, "Allergies: None, Diet Restriction: None", '2021-05-01 16:30:00');
COMMIT;

-- ----------------------------
-- Records of pet_location
-- ----------------------------
BEGIN;
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60553394673865, -122.34003846401347, '2021-11-05 00:30:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60563144542295, -122.33873676485442, '2021-12-05 00:40:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60536891181118, -122.33928192877308, '2021-12-05 01:40:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60530140713765, -122.33980483146667, '2021-12-05 02:30:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.6062090459284, -122.33913170502638, '2021-12-05 03:20:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.606017777364215, -122.34049460912584, '2021-12-05 04:20:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60645283347124, -122.33898706097206, '2021-12-06 05:40:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.60660659163385, -122.338102549044, '2021-12-07 06:40:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.606925398747826, -122.33830279794368, '2021-12-08 07:30:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (1, 47.607082928270785, -122.33846967913865, '2021-12-08 08:10:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (2, 47.60699662000861, -122.33678409477271, '2021-12-08 09:10:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (3, 47.605631408631446, -122.33727373862575, '2021-12-08 10:20:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (4, 47.61296955232555, -122.34169759879386, '2021-12-08 11:30:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (5, 47.542256690051, -121.83598752068544, '2021-12-08 12:40:00');
INSERT INTO pet_location (pet_id, latitude, longitude, last_seen) VALUES (6, 47.59582803709632, -120.66202089730126, '2021-12-08 23:30:00');
COMMIT;