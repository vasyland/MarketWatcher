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
  `allowed_buy_price` decimal(38,2) DEFAULT NULL,
  `allowed_buy_yield` decimal(38,2) DEFAULT NULL,
  `best_buy_price` decimal(38,2) DEFAULT NULL,
  `current_price` decimal(38,2) DEFAULT NULL,
  `current_yield` decimal(38,2) DEFAULT NULL,
  `lower_yield` decimal(38,2) DEFAULT NULL,
  `quoterly_dividend_amount` decimal(38,2) DEFAULT NULL,
  `sell_point_yield` decimal(38,2) DEFAULT NULL,
  `upper_yield` decimal(38,2) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `recommended_action` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) NOT NULL,
  PRIMARY KEY (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
