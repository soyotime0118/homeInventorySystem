-- 사용자 관리
CREATE TABLE users
(
    user_id    SERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'user',
    created_at TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE family_members
(
    family_member_id   SERIAL PRIMARY KEY,
    user_id            INT,
    family_member_name VARCHAR(50) NOT NULL,
    role               VARCHAR(20) NOT NULL DEFAULT 'member'
);

-- 물품 관리
CREATE TABLE categories
(
    category_id   SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE storage_locations
(
    location_id   SERIAL PRIMARY KEY,
    location_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE items
(
    item_id           SERIAL PRIMARY KEY,
    item_name         VARCHAR(100) NOT NULL,
    category_id       INT,
    brand             VARCHAR(50),
    purchase_location VARCHAR(100),
    purchase_date     DATE,
    expiry_date       DATE,
    quantity          INT DEFAULT 0,
    location_id       INT
);

-- 재고 관리
CREATE TABLE stock_alerts
(
    alert_id        SERIAL PRIMARY KEY,
    item_id         INT,
    alert_threshold INT NOT NULL,
    alert_active    BOOLEAN DEFAULT TRUE
);

CREATE TABLE usage_logs
(
    usage_log_id  SERIAL PRIMARY KEY,
    item_id       INT,
    used_quantity INT NOT NULL,
    usage_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 구매 관리
CREATE TABLE purchase_lists
(
    purchase_list_id SERIAL PRIMARY KEY,
    user_id          INT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_list_items
(
    purchase_list_item_id SERIAL PRIMARY KEY,
    purchase_list_id      INT,
    item_id               INT,
    quantity              INT NOT NULL,
    purchased             BOOLEAN DEFAULT FALSE
);

CREATE TABLE purchase_logs
(
    purchase_log_id SERIAL PRIMARY KEY,
    item_id         INT,
    quantity        INT NOT NULL,
    purchase_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cost            NUMERIC(10, 2)
);

-- 보관 위치 관리
-- 보관 위치는 'storage_locations' 테이블에서 관리됨

-- 시스템 설정
CREATE TABLE settings
(
    setting_id SERIAL PRIMARY KEY,
    user_id    INT,
    theme      VARCHAR(50) DEFAULT 'light',
    language   VARCHAR(10) DEFAULT 'en'
);

-- 다국어 지원을 위한 예제 (다국어 테이블)
CREATE TABLE translations
(
    translation_id SERIAL PRIMARY KEY,
    key            VARCHAR(100) NOT NULL,
    language       VARCHAR(10)  NOT NULL,
    value          TEXT         NOT NULL
);