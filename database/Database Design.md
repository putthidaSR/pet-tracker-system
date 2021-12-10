# Database Design

## user

| Name              | Type     | Length | Primary Key | Description             |
| ----------------- | -------- | ------ | ----------- | ----------------------- |
| id                | INT      | 0      | Yes         | primary key             |
| login_name        | VARCHAR  | 100    |             | user name for login     |
| login_password    | VARCHAR  | [6,15] |             | user password for login |
| creation_time     | DATETIME | 0      |             | record create time      |
| modification_time | DATETIME | 0      |             | record modify time      |

## account_detail

| Name              | Type    | Length | Primary Key | Description                   |
| ----------------- | ------- | ------ | ----------- | ----------------------------- |
| id                | INT     | 0      | Yes         | primary key                   |
| role              | VARCHAR | 100    |             | Admin, PetOwner, Veterinarian |
| badge_number      | VARCHAR | 50     |             | user badge number             |
| email             | VARCHAR | 255    |             | user email                    |
| phone_number      | VARCHAR | 50     |             | user phone number             |
| address           | VARCHAR | 100    |             | user address                  |
| active            | BOOLEAN | 0      |             | record status                 |
| confirmation_code | INT     | 0      |             | confirmation code             |

## pet

| Name              | Type     | Length | Primary Key | Description               |
| ----------------- | -------- | ------ | ----------- | ------------------------- |
| id                | INT      | 32     | Yes         | primary key               |
| user_id           | INT      | 32     |             | primary key in user table |
| rfid_number       | VARCHAR  | 64     |             | RFID                      |
| registration_time | DATETIME | 0      |             | record create time        |
| modification_time | DATETIME | 0      |             | record modify time        |

## pet_detail

| Name          | Type    | Length | Primary Key | Description              |
| ------------- | ------- | ------ | ----------- | ------------------------ |
| pet_detail_id | INT     | 32     | Yes         | primary key              |
| pet_id        | INT     | 32     |             | primary key in pet table |
| species       | VARCHAR | 20     |             | Cat, Dog, Other          |
| name          | VARCHAR | 32     |             | pet name                 |
| age           | VARCHAR | 50     |             | pet age                  |
| breed         | VARCHAR | 50     |             | pet breed                |
| color         | VARCHAR | 50     |             | pet color                |
| gender        | VARCHAR | 20     |             | Male, Female, Neutered   |
| active        | BOOLEAN | 0      |             | record status            |


## pet_medical

| Name        | Type     | Length | Primary Key | Description              |
| ----------- | -------- | ------ | ----------- | ------------------------ |
| id          | INT   | 0     | Yes         | primary key              |
| pet_id      | INT   | 0     |             | primary key in pet table |
| medical        | text     | 0      |             | medical information  |
| medical_assign_date | DATETIME | 0      |             | medical assign time |
| creation_time | DATETIME | 0     |             | record create time |

## pet_vaccination

| Name        | Type     | Length | Primary Key | Description              |
| ----------- | -------- | ------ | ----------- | ------------------------ |
| id          | INT   | 32     | Yes         | primary key              |
| pet_id      | INT   | 32     |             | primary key in pet table |
| creation_time | DATETIME | 0      |             | record create time       |
| modification_time | DATETIME | 0 | | record modify time |
| vaccination_name | VARCHAR | 500   |             | vaccination name |
| immunization_date | DATETIME | 0 | | immunization date |
| veterinarian_name | VARCHAR | 100 | | veterinarian name |
| veterinarian_contact | VARCHAR | 100 | | veterinarian contact |

## pet_location

| Name        | Type     | Length | Primary Key | Description              |
| ----------- | -------- | ------ | ----------- | ------------------------ |
| id          | int      | 0     | Yes         | primary key              |
| pet_id | varchar | 0    |             | primary key in pet table |
| latitude | double | 0      |             | latitude for the record |
| longitude | double | 0 | | longitude for the record |
| address | VARCHAR | 100    |             | location address |
| last_seen | DATETIME | 0     |             | last seen time |

