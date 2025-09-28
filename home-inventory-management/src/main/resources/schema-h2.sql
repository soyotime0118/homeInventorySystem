-- 사용자 관리
-- 사용자 관리
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id    bigint       not null AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'user',
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN users.user_id IS '사용자 고유 ID';
COMMENT ON COLUMN users.username IS '사용자 이름';
COMMENT ON COLUMN users.email IS '사용자 이메일';
COMMENT ON COLUMN users.password IS '사용자 비밀번호 (암호화 저장)';
COMMENT ON COLUMN users.role IS '사용자 권한 (admin, user 등)';
COMMENT ON COLUMN users.created_at IS '사용자 생성 날짜';

DROP TABLE IF EXISTS family_members;
CREATE TABLE family_members
(
    family_member_id   bigint      not null AUTO_INCREMENT,
    user_id            INT,
    family_member_name VARCHAR(50) NOT NULL,
    role               VARCHAR(20) NOT NULL DEFAULT 'member'
);

COMMENT ON COLUMN family_members.family_member_id IS '가족 구성원 고유 ID';
COMMENT ON COLUMN family_members.user_id IS '가족 구성원이 속한 사용자 ID';
COMMENT ON COLUMN family_members.family_member_name IS '가족 구성원의 이름';
COMMENT ON COLUMN family_members.role IS '가족 구성원의 역할 (member 등)';

-- 물품 관리
DROP TABLE IF EXISTS categories;
CREATE TABLE categories
(
    category_id   bigint      not null AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON COLUMN categories.category_id IS '카테고리 고유 ID';
COMMENT ON COLUMN categories.category_name IS '카테고리 이름';

DROP TABLE IF EXISTS storage_locations;
CREATE TABLE storage_locations
(
    location_id   bigint      not null AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(50) NOT NULL UNIQUE
);

COMMENT ON COLUMN storage_locations.location_id IS '보관 위치 고유 ID';
COMMENT ON COLUMN storage_locations.location_name IS '보관 위치 이름 (예: 주방, 냉장고)';

DROP TABLE IF EXISTS items;
CREATE TABLE items
(
    item_id           bigint       not null AUTO_INCREMENT PRIMARY KEY,
    item_name         VARCHAR(100) NOT NULL,
    category_id       INT,
    brand             VARCHAR(50),
    purchase_location VARCHAR(100),
    purchase_date     DATE,
    last_used_date    DATE,
    expiry_date       DATE,
    quantity          INT DEFAULT 0,
    location_id       INT
);

COMMENT ON COLUMN items.item_id IS '물품 고유 ID';
COMMENT ON COLUMN items.item_name IS '물품 이름';
COMMENT ON COLUMN items.category_id IS '물품 카테고리 ID';
COMMENT ON COLUMN items.brand IS '물품 브랜드 이름';
COMMENT ON COLUMN items.purchase_location IS '물품 구매 장소';
COMMENT ON COLUMN items.purchase_date IS '물품 구매 날짜';
COMMENT ON COLUMN items.expiry_date IS '물품 유통기한';
COMMENT ON COLUMN items.last_used_date IS '최종사용일';
COMMENT ON COLUMN items.quantity IS '물품 수량';
COMMENT ON COLUMN items.location_id IS '물품 보관 위치 ID';

-- 재고 관리
DROP TABLE IF EXISTS stock_alerts;
CREATE TABLE stock_alerts
(
    alert_id        bigint not null AUTO_INCREMENT PRIMARY KEY,
    item_id         INT,
    alert_threshold INT    NOT NULL,
    alert_active    BOOLEAN DEFAULT TRUE
);

COMMENT ON COLUMN stock_alerts.alert_id IS '재고 알림 고유 ID';
COMMENT ON COLUMN stock_alerts.item_id IS '재고 알림이 설정된 물품 ID';
COMMENT ON COLUMN stock_alerts.alert_threshold IS '재고 알림 임계값';
COMMENT ON COLUMN stock_alerts.alert_active IS '재고 알림 활성화 여부';

DROP TABLE IF EXISTS usage_logs;
CREATE TABLE usage_logs
(
    usage_log_id  bigint not null AUTO_INCREMENT PRIMARY KEY,
    item_id       INT,
    used_quantity INT    NOT NULL,
    usage_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN usage_logs.usage_log_id IS '물품 사용 기록 고유 ID';
COMMENT ON COLUMN usage_logs.item_id IS '사용된 물품 ID';
COMMENT ON COLUMN usage_logs.used_quantity IS '사용된 물품 수량';
COMMENT ON COLUMN usage_logs.usage_date IS '물품 사용 날짜';

-- 구매 관리
DROP TABLE IF EXISTS purchase_lists;
CREATE TABLE purchase_lists
(
    purchase_list_id bigint not null AUTO_INCREMENT PRIMARY KEY,
    user_id          INT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN purchase_lists.purchase_list_id IS '구매 목록 고유 ID';
COMMENT ON COLUMN purchase_lists.user_id IS '구매 목록을 생성한 사용자 ID';
COMMENT ON COLUMN purchase_lists.created_at IS '구매 목록 생성 날짜';

DROP TABLE IF EXISTS purchase_list_items;
CREATE TABLE purchase_list_items
(
    purchase_list_item_id bigint not null AUTO_INCREMENT PRIMARY KEY,
    purchase_list_id      INT,
    item_id               INT,
    quantity              INT    NOT NULL,
    purchased             BOOLEAN DEFAULT FALSE
);

COMMENT ON COLUMN purchase_list_items.purchase_list_item_id IS '구매 목록 항목 고유 ID';
COMMENT ON COLUMN purchase_list_items.purchase_list_id IS '구매 목록 ID';
COMMENT ON COLUMN purchase_list_items.item_id IS '구매할 물품 ID';
COMMENT ON COLUMN purchase_list_items.quantity IS '구매할 물품 수량';
COMMENT ON COLUMN purchase_list_items.purchased IS '구매 완료 여부';

DROP TABLE IF EXISTS purchase_logs;
CREATE TABLE purchase_logs
(
    purchase_log_id bigint not null AUTO_INCREMENT PRIMARY KEY,
    item_id         INT,
    quantity        INT    NOT NULL,
    purchase_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cost            NUMERIC(10, 2)
);

COMMENT ON COLUMN purchase_logs.purchase_log_id IS '구매 기록 고유 ID';
COMMENT ON COLUMN purchase_logs.item_id IS '구매한 물품 ID';
COMMENT ON COLUMN purchase_logs.quantity IS '구매한 물품 수량';
COMMENT ON COLUMN purchase_logs.purchase_date IS '구매 날짜';
COMMENT ON COLUMN purchase_logs.cost IS '구매 비용';

-- 시스템 설정
DROP TABLE IF EXISTS settings;
CREATE TABLE settings
(
    setting_id bigint not null AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    theme      VARCHAR(50) DEFAULT 'light',
    language   VARCHAR(10) DEFAULT 'en'
);

COMMENT ON COLUMN settings.setting_id IS '설정 고유 ID';
COMMENT ON COLUMN settings.user_id IS '설정이 적용된 사용자 ID';
COMMENT ON COLUMN settings.theme IS '사용자 테마 설정';
COMMENT ON COLUMN settings.language IS '사용자 언어 설정';

-- 다국어 지원
DROP TABLE IF EXISTS translations;
CREATE TABLE translations
(
    translation_id bigint not null AUTO_INCREMENT PRIMARY KEY,
    translation_key            VARCHAR(100) NOT NULL,
    translation_language       VARCHAR(10)  NOT NULL,
    translation_value          TEXT         NOT NULL
);

COMMENT ON COLUMN translations.translation_id IS '번역 고유 ID';
COMMENT ON COLUMN translations.translation_key IS '번역 키';
COMMENT ON COLUMN translations.translation_language IS '언어 코드 (예: en, ko)';
COMMENT ON COLUMN translations.translation_value IS '번역된 텍스트';