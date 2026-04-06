
CREATE DATABASE IF NOT EXISTS fuel_calculator_localization

CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 1. Create a new user with a password
CREATE USER 'fuel_user'@'localhost' IDENTIFIED BY 'password123';

-- 2. Give that user access to your database
GRANT ALL PRIVILEGES ON fuel_calculator_localization.* TO 'fuel_user'@'localhost';

-- 3. Apply the changes
FLUSH PRIVILEGES;

USE fuel_calculator_localization;

CREATE TABLE IF NOT EXISTS calculation_records (
                                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                                   distance DOUBLE NOT NULL,
                                                   consumption DOUBLE NOT NULL,
                                                   price DOUBLE NOT NULL,
                                                   total_fuel DOUBLE NOT NULL,
                                                   total_cost DOUBLE NOT NULL,
                                                   language VARCHAR(10),
                                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
DROP TABLE IF EXISTS localization_strings;
CREATE TABLE IF NOT EXISTS localization_strings (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    translation_key VARCHAR(100) NOT NULL,
                                                    value VARCHAR(255) NOT NULL,
                                                    language VARCHAR(10) NOT NULL,
                                                    UNIQUE KEY unique_key_lang (translation_key, language)
);

INSERT INTO localization_strings (translation_key, value, language) VALUES
-- Titles/Headers
('title', 'Fuel Consumption Calculator', 'EN'),
('title', 'Calculateur de consommation', 'FR'),
('title', '燃料消費量計算機', 'JP'),
('title', 'ماشین حساب مصرف سوخت', 'IR'),

-- Input Labels
('lbl_distance', 'Distance (km)', 'EN'),
('lbl_distance', 'Distance (km)', 'FR'),
('lbl_distance', '距離 (km)', 'JP'),
('lbl_distance', 'مسافت (کیلومتر)', 'IR'),

('lbl_consumption', 'Fuel Consumption (L/100km)', 'EN'),
('lbl_consumption', 'Consommation (L/100km)', 'FR'),
('lbl_consumption', '燃料消費率 (L/100km)', 'JP'),
('lbl_consumption', 'مصرف سوخت (لیتر در ۱۰۰ کیلومتر)', 'IR'),

('lbl_price', 'Fuel Price (per Liter)', 'EN'),
('lbl_price', 'Prix du carburant (par litre)', 'FR'),
('lbl_price', '燃料価格 (1リットルあたり)', 'JP'),
('lbl_price', 'قیمت سوخت (هر لیتر)', 'IR'),

-- Buttons
('btn_calculate', 'Calculate', 'EN'),
('btn_calculate', 'Calculer', 'FR'),
('btn_calculate', '計算する', 'JP'),
('btn_calculate', 'محاسبه', 'IR'),

-- Results
('res_total_fuel', 'Total Fuel Needed:', 'EN'),
('res_total_fuel', 'Carburant total nécessaire:', 'FR'),
('res_total_fuel', '必要な燃料合計:', 'JP'),
('res_total_fuel', 'کل سوخت مورد نیاز:', 'IR'),

('res_total_cost', 'Total Trip Cost:', 'EN'),
('res_total_cost', 'Coût total du trajet:', 'FR'),
('res_total_cost', '旅行の合計費用:', 'JP'),
('res_total_cost', 'کل هزینه سفر:', 'IR'),

-- Error/Success Messages
('msg_success', 'Record saved to database!', 'EN'),
('msg_success', 'Enregistrement sauvegardé!', 'FR'),
('msg_success', '記録が保存されました！', 'JP'),
('msg_success', 'رکورد در پایگاه داده ذخیره شد!', 'IR'),

('msg_error_input', 'Please enter valid positive numbers.', 'EN'),
('msg_error_input', 'Veuillez entrer des nombres positifs.', 'FR'),
('msg_error_input', '有効な正の数を入力してください。', 'JP'),
('msg_error_input', 'لطفا اعداد مثبت معتبر وارد کنید.', 'IR');