-- -----------------------------------------------------
-- Table `watch_symbol`
-- We need to add exchange field: NASDAQ, NYSE, AMEX
-- -----------------------------------------------------
CREATE TABLE `watch_symbol` (
  `symbol` varchar(10) NOT NULL COMMENT 'Stock symbol TSX with .TO',
  `exchange` varchar(10) DEFAULT NULL COMMENT 'Stock symbol exchange ',
  `quoterly_dividend_amount` decimal(10,4) DEFAULT NULL COMMENT 'Majority ov comapnies pay on quaterly basis',
  `upper_yield` decimal(6,4) DEFAULT NULL COMMENT 'Upper yeild where price is at lowest point',
  `lower_yield` decimal(6,4) DEFAULT NULL COMMENT 'Lowe yield when price is at highest point',
  `updated_on` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Date when record was created or updated',
  PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `symbol_status` (
  `symbol` varchar(10) NOT NULL,  
  `current_price` decimal(10,4) DEFAULT NULL,
  `quoterly_dividend_amount` decimal(10,4) DEFAULT NULL,
  `current_yield` decimal(8,4) DEFAULT NULL,
  `lower_yield` decimal(8,4) DEFAULT NULL,
  `upper_yield` decimal(8,4) DEFAULT NULL,
  `allowed_buy_yield` decimal(8,4) DEFAULT NULL,
  `allowed_buy_price` decimal(10,2) DEFAULT NULL,
  `best_buy_price` decimal(10,2) DEFAULT NULL,
  `sell_point_yield` decimal(8,4) DEFAULT NULL,
  `sell_price` decimal(10,2) DEFAULT NULL,
  `overpriced_amount` decimal(10,2) DEFAULT NULL,
  `overpriced_percentage` decimal(8,4) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `recommended_action` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`symbol`)
);


use golem;
drop table symbol_history_price_seq;
drop table symbol_history_price;
CREATE TABLE `symbol_history_price` (
  `id` int NOT NULL AUTO_INCREMENT,
  `symbol` varchar(10) DEFAULT NULL,
  `price` decimal(3,1) DEFAULT NULL,
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'creation time',
  `updated_on` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `symbol_history_price_seq` (
  `next_val` BIGINT NOT NULL
);
INSERT INTO symbol_history_price_seq (next_val) VALUES (1);
commit;

-- https://fmpcloud.io/api/v3/symbol/NASDAQ?apikey=ATt4kh10v7qTrdhbmSvWWOJmpYLgMIy5
use golem;
CREATE TABLE fmp_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(50) NOT NULL,
    name VARCHAR(255),
    price DECIMAL(10, 4),
    changesPercentage DECIMAL(10, 5),
    day_change DECIMAL(10, 4),
    dayLow DECIMAL(10, 4),
    dayHigh DECIMAL(10, 4),
    yearHigh DECIMAL(10, 4),
    yearLow DECIMAL(10, 4),
    marketCap BIGINT,
    priceAvg50 DECIMAL(10, 5),
    priceAvg200 DECIMAL(10, 5),
    exchange VARCHAR(50),
    volume BIGINT,
    avgVolume BIGINT,
    open DECIMAL(10, 4),
    previousClose DECIMAL(10, 4),
    eps DECIMAL(10, 4),
    pe DECIMAL(10, 4),
    earningsAnnouncement DATETIME,
    sharesOutstanding BIGINT,
    timestamp BIGINT,
    
    -- Optional: Add index on symbol for faster lookups
    INDEX idx_fmp_symbol (symbol)
);


use golem;
drop table listed_companies;
CREATE TABLE listed_companies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(11) NOT NULL UNIQUE, -- review size later
    name VARCHAR(100),  -- review size later
    marketCap BIGINT,
    exchange VARCHAR(6),
     -- Optional: Add index on symbol for faster lookups
    INDEX idx_listed_symbol (symbol)
);



use golem;
drop table current_price;
CREATE TABLE current_price (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(11) NOT NULL UNIQUE,
    name VARCHAR(100),
	price DECIMAL(10, 4),
	price_change DECIMAL(8, 4),
	created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     -- Optional: Add index on symbol for faster lookups
    INDEX idx_price_symbol (symbol)
);
