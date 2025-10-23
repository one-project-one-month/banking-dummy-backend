SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Gender;
DROP TABLE IF EXISTS Nationality;
DROP TABLE IF EXISTS FAQ;
DROP TABLE IF EXISTS Audit_log;
DROP TABLE IF EXISTS Account_type;
DROP TABLE IF EXISTS Account_detail;
DROP TABLE IF EXISTS Nickname;
DROP TABLE IF EXISTS KYC;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Profile_detail;
DROP TABLE IF EXISTS Transaction;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Media;

SET FOREIGN_KEY_CHECKS = 1;

-- Table: Gender
CREATE TABLE Gender (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_by INT NULL,
                        updated_by INT NULL
);

-- Table: Nationality
CREATE TABLE Nationality (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(100),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             created_by INT NULL,
                             updated_by INT NULL
);

-- Table: FAQ
CREATE TABLE FAQ (
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     question TEXT,
                     answer TEXT,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     created_by INT NULL,
                     updated_by INT NULL
);

-- Table: audit_log
CREATE TABLE Audit_log (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           before_state JSON,
                           after_state JSON,
                           action VARCHAR(255),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           created_by INT NULL,
                           updated_by INT NULL
);

-- Table: AccountType
CREATE TABLE Account_type (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              code VARCHAR(50),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              created_by INT NULL,
                              updated_by INT NULL
);

-- Table: AccountDetail
CREATE TABLE Account_detail (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                account_number VARCHAR(100) UNIQUE NOT NULL,
                                user_id INT,
                                account_type_id INT,
                                group_id INT,
                                current_balance DECIMAL(19, 4) DEFAULT 0.00,
                                role_id INT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                created_by INT NULL,
                                updated_by INT NULL
);

-- Table: Nickname
CREATE TABLE Nickname (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          from_account INT,
                          to_account INT,
                          nickname VARCHAR(255),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          created_by INT NULL,
                          updated_by INT NULL
);

-- Table: KYC
CREATE TABLE KYC (
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     kyc_data VARCHAR(100),
                     id_type VARCHAR(100),
                     kyc_type VARCHAR(100),
                     profile_id INT,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     created_by INT NULL,
                     updated_by INT NULL
);

-- Table: Users
CREATE TABLE Users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       profile_id INT,
                       role_id INT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       created_by INT NULL,
                       updated_by INT NULL
);

-- Table: profile_detail (renamed from "Profile Detail")
CREATE TABLE Profile_detail (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                fullname VARCHAR(255),
                                date_of_birth DATE,
                                organization_id INT,
                                gender_id INT,
                                selected_account_id INT,
                                nationality_id INT,
                                pin VARCHAR(255),
                                is_policy_agreement BOOLEAN DEFAULT false,
                                is_auto_save_receipt BOOLEAN DEFAULT false,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                created_by INT NULL,
                                updated_by INT NULL
);

-- Table: Transaction
CREATE TABLE Transaction (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             credit_account_id INT,
                             debit_account_id INT,
                             amount DECIMAL(19, 4),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             created_by INT NULL,
                             updated_by INT NULL
);

-- Table: Role
CREATE TABLE Role (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      role_type VARCHAR(100),
                      name VARCHAR(100),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      created_by INT NULL,
                      updated_by INT NULL
);

-- Table: Media
CREATE TABLE Media (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       url VARCHAR(1024),
                       entity_id INT,
                       entity_name VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       created_by INT NULL,
                       updated_by INT NULL
);

INSERT INTO Gender (name, created_by, updated_by)
VALUES
    ('Male', 1, 1),
    ('Female', 1, 1),
    ('Other', 1, 1);

INSERT INTO Nationality (name, created_by, updated_by)
VALUES
    ('Myanmar', 1, 1),
    ('Thailand', 1, 1),
    ('Singapore', 1, 1),
    ('Malaysia', 1, 1),
    ('Indonesia', 1, 1),
    ('Philippines', 1, 1),
    ('Vietnam', 1, 1),
    ('Japan', 1, 1),
    ('South Korea', 1, 1),
    ('China', 1, 1);

INSERT INTO Role (role_type, name, created_by, updated_by)
VALUES
    ('CUSTOMER', 'Customer Role', 1, 1),
    ('ADMIN', 'Admin Role', 1, 1);
