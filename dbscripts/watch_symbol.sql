-- -----------------------------------------------------
-- Table `watch_symbol`
-- We need to add exchange field: NASDAQ, NYSE, AMEX
-- -----------------------------------------------------
CREATE TABLE `watch_symbol` (
  `symbol` VARCHAR(10) NOT NULL COMMENT 'Stock symbol TSX with .TO',
  `exchange` VARCHAR(10) NOT NULL COMMENT 'Stock symbol Exchange',
  `quoterly_dividend_amount` DECIMAL(10,4) NULL COMMENT 'Majority ov comapnies pay on quaterly basis',
  `upper_yield` DECIMAL(6,4) NULL COMMENT 'Upper yeild where price is at lowest point',
  `lower_yield` DECIMAL(6,4) NULL COMMENT 'Lowe yield when price is at highest point',
  `updated_on` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT 'Date when record was created or updated',
  PRIMARY KEY (`symbol`))
ENGINE = InnoDB


ALTER TABLE `us_watch_symbol` 
ADD COLUMN `exchange` VARCHAR(10) NULL COMMENT 'Stock symbol exchange ' AFTER `updated_on`;