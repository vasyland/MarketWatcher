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


ALTER TABLE `watch_symbol` 
ADD COLUMN `exchange` VARCHAR(10) NULL COMMENT 'Stock symbol exchange ' AFTER `updated_on`;


CREATE TABLE `user_info_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_info` (
  `id` bigint NOT NULL,
  `email_id` varchar(255) NOT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `roles` varchar(255) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeo44j61iq2l3i834bgn193qxr` (`email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `refresh_tokens_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `refresh_tokens` (
  `revoked` bit(1) DEFAULT NULL,
  `id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `refresh_token` varchar(10000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnpiq3a870qyx0ilrx2gvfuiee` (`user_id`),
  CONSTRAINT `FKnpiq3a870qyx0ilrx2gvfuiee` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `volatility_date_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `volatility_date` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `action_description` varchar(450) DEFAULT NULL,
  `description` varchar(450) DEFAULT NULL,
  `active` int DEFAULT NULL,
  `active_from_date` datetime DEFAULT NULL,
  `active_to_date` datetime DEFAULT NULL,
  `created_on` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_on` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;





