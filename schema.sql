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
                       status INT DEFAULT 1,
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

-- Tables for Bank Admin
DROP TABLE IF EXISTS Faq;
DROP TABLE IF EXISTS Faq_category;

CREATE TABLE IF NOT EXISTS Faq_category (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NULL
    );

CREATE TABLE IF NOT EXISTS Faq (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   question TEXT NOT NULL,
                                   answer TEXT NOT NULL,
                                   faq_category_id INT,
                                   created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (faq_category_id) REFERENCES Faq_category(id) ON DELETE SET NULL
    );

INSERT INTO Faq_category (name) VALUES
                                    ('Account Management'),
                                    ('Security & Privacy'),
                                    ('Transfers & Payments'),
                                    ('App Usage'),
                                    ('Technical Support');


INSERT INTO Faq (question, answer, faq_category_id) VALUES

-- Account Management
('How do I open a mobile banking account?',
 'You can open an account by downloading our mobile banking app and completing the registration form using your phone number, national ID, and email address.',
 1),

('Can I have multiple accounts linked to the same app?',
 'Yes, you can link multiple accounts under the same profile by adding them in the “Linked Accounts” section from Settings.',
 1),

('How do I reset my password?',
 'On the login screen, tap “Forgot Password”, then follow the instructions sent to your registered phone number or email to reset it.',
 1),

-- Security & Privacy
('Is mobile banking safe to use?',
 'Yes. Our app uses end-to-end encryption and multi-factor authentication to protect your data and transactions.',
 2),

('What should I do if I suspect fraudulent activity?',
 'Immediately lock your account using the “Freeze Account” option in the app and contact our support center for assistance.',
 2),

('Does the app store my card details?',
 'No. Your card details are encrypted and securely stored in compliance with banking security standards.',
 2),

-- Transfers & Payments
('How do I transfer money to another bank account?',
 'Go to “Transfers” → “External Transfer”, enter the recipient’s bank details, and confirm with your PIN or biometric authentication.',
 3),

('Is there a transfer limit?',
 'Yes, daily transfer limits depend on your account type. You can view or request an increase in the “Limits” section under Settings.',
 3),

('Can I schedule recurring payments?',
 'Yes, you can set up recurring payments from the “Scheduled Transfers” option for bills, rent, or subscriptions.',
 3),

-- App Usage
('Does the app work without internet?',
 'No, you need an active internet connection to use mobile banking services.',
 4),

('How do I update the app?',
 'Visit the Google Play Store or Apple App Store, search for our app, and tap “Update”.',
 4),

('Can I use the app on multiple devices?',
 'Yes, but you must verify each device using OTP authentication during login.',
 4),

-- Technical Support
('What should I do if the app crashes?',
 'Try restarting your device and updating the app to the latest version. If the problem persists, contact customer support.',
 5),

('Why am I not receiving OTPs?',
 'Ensure your phone number is active and not blocking SMS messages from short codes. You can also resend OTP after 30 seconds.',
 5),

('How can I contact customer support?',
 'You can reach us via in-app chat, call our hotline, or email support@mobilebank.com for assistance.',
 5);

INSERT INTO Faq (question, answer, faq_category_id) VALUES

-- Account Management
('မိုဘိုင်းဘဏ်အကောင့်ကို ဘယ်လိုဖွင့်ရမလဲ။',
 'မိုဘိုင်းဘဏ်အက်ပ်ကို ဒေါင်းလုဒ်လုပ်ပြီး မိမိဖုန်းနံပါတ်၊ အမျိုးသားမှတ်ပုံတင်နံပါတ်နဲ့ အီးမေးလ်ဖြင့် မှတ်ပုံတင်ဖောင်ဖြည့်ခြင်းဖြင့် အကောင့်ဖွင့်နိုင်ပါတယ်။',
 1),

-- Security & Privacy
('လိမ်လည်မှုရှိတယ်လို့ သံသယရှိရင် ဘာလုပ်သင့်လဲ။',
 'အက်ပ်ထဲရှိ “Freeze Account” ဆိုတဲ့ရွေးချယ်မှုကို အသုံးပြုပြီး မိမိအကောင့်ကို ချက်ချင်းပိတ်ပါ။ ပြီးရင် ဝန်ဆောင်မှုအထောက်အပံ့ဌာနကို ဆက်သွယ်ပါ။',
 2),

-- Transfers & Payments
('အခြားဘဏ်အကောင့်သို့ ငွေလွှဲချင်ရင် ဘယ်လိုလုပ်ရမလဲ။',
 '“ငွေလွှဲ” → “အပြင်ဘဏ်လွှဲမှု” ကိုနှိပ်ပြီး လက်ခံသူ၏ ဘဏ်အချက်အလက်များထည့်ပါ။ ထို့နောက် မိမိ၏ PIN သို့မဟုတ် လက်ဗွေဖြင့် အတည်ပြုပါ။',
 3),

-- App Usage
('အက်ပ်ကို အင်တာနက်မရှိပဲ အသုံးပြုနိုင်မလား။',
 'မရပါ။ မိုဘိုင်းဘဏ်ဝန်ဆောင်မှုများအသုံးပြုရန် အင်တာနက်ချိတ်ဆက်မှု လိုအပ်ပါသည်။',
 4),

-- Technical Support
('OTP မရဘူးဆိုရင် ဘယ်လိုလုပ်ရမလဲ။',
 'မိမိဖုန်းနံပါတ်အသက်ဝင်နေကြောင်း၊ short code မှ SMS မတားထားကြောင်း စစ်ဆေးပါ။ မရပါက စက္ကန့် ၃၀ ကြာပြီးနောက် OTP ကို ပြန်တောင်းနိုင်ပါတယ်။',
 5);

DROP TABLE IF EXISTS Organization;

CREATE TABLE Organization (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              shortcode VARCHAR(50) UNIQUE,
                              address VARCHAR(255) NOT NULL,
                              country VARCHAR(255) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                              created_by BIGINT NULL,
                              updated_by BIGINT NULL
);

SET @hashed_password = '$2a$10$T.SC.OMcLRkPB8vM.A/3He.XW6A/T/rTpvY5A5Lz9.Qo8J7j68f.O';

SET @customer_role_id = (SELECT id FROM Role WHERE role_type = 'CUSTOMER');

INSERT INTO Profile_detail (id, fullname, date_of_birth, gender_id, nationality_id, is_policy_agreement, pin)
VALUES (6, 'John Doe', '1990-01-15', 1, 1, TRUE, @hashed_password);

INSERT INTO Users (id, username, email, password, profile_id, role_id, status)
VALUES (6, 'john.doe', 'john.doe@example.com', @hashed_password, 6, @customer_role_id, 2); -- Status 2 = ACTIVE

INSERT INTO Account_detail (id, account_number, user_id, account_type_id, current_balance, role_id)
VALUES (2, '1000000001', 6, 1, 50000.00, @customer_role_id);

UPDATE Profile_detail SET selected_account_id = 1 WHERE id = 6;

INSERT INTO Profile_detail (id, fullname, date_of_birth, gender_id, nationality_id, is_policy_agreement, pin)
VALUES (2, 'Jane Smith', '1992-05-20', 2, 2, TRUE, @hashed_password);

INSERT INTO Users (id, username, email, password, profile_id, role_id, status)
VALUES (7, 'jane.smith', 'jane.smith@example.com', @hashed_password, 7, @customer_role_id, 2); -- Status 2 = ACTIVE

INSERT INTO Account_detail (id, account_number, user_id, account_type_id, current_balance, role_id)
VALUES (3, '1000000002', 7, 1, 75000.00, @customer_role_id);

UPDATE Profile_detail SET selected_account_id = 2 WHERE id = 7;

SET @admin_role_id = (SELECT id FROM Role WHERE role_type = 'ADMIN');

INSERT INTO Organization (name, shortcode, address, country)
VALUES ('Head Office', 'HQ', 'Yangon', 'Myanmar');

-- 3️⃣ Create a Profile for the admin
INSERT INTO Profile_detail (
    fullname, date_of_birth, organization_id, gender_id, nationality_id,
    is_policy_agreement, is_auto_save_receipt, pin
)
VALUES (
           'System Admin',
           '1985-01-01',
           1,
           1,
           1,
           TRUE,
           TRUE,
           '1234'
       );

SET @hashed_password = '$2y$10$hDqjFQfP9Xn6x7yP6YyNEOkUBN6j8OiWJmznFX/hHTDhKfTG6Lkni';

INSERT INTO Users (
    username, email, password, profile_id, role_id, status, created_by, updated_by
)
VALUES (
           'admin',
           'admin@bank.com',
           @hashed_password,
           LAST_INSERT_ID(),
           @admin_role_id,
           1,   -- active
           1,
           1
       );

INSERT INTO Account_type (code, created_by, updated_by)
VALUES ('ADMIN_MAIN', 1, 1);

SET @admin_account_type_id = LAST_INSERT_ID();
SET @admin_user_id = (SELECT id FROM Users WHERE username = 'admin');

INSERT INTO Account_detail (
    account_number, user_id, account_type_id, current_balance, role_id, created_by, updated_by
)
VALUES (
           'ADM-0001',
           @admin_user_id,
           @admin_account_type_id,
           0.00,
           @admin_role_id,
           1,
           1
       );

UPDATE Profile_detail
SET selected_account_id = (SELECT id FROM Account_detail WHERE user_id = @admin_user_id)
WHERE id = (SELECT profile_id FROM Users WHERE id = @admin_user_id);
