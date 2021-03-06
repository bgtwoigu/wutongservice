CREATE TABLE IF NOT EXISTS `page` (
  page_id BIGINT NOT NULL AUTO_INCREMENT,
  created_time BIGINT NOT NULL,
  updated_time BIGINT NOT NULL,
  destroyed_time BIGINT NOT NULL DEFAULT 0,
  type VARCHAR(128) NOT NULL DEFAULT '',
  flags BIGINT NOT NULL DEFAULT 0,
  email_domain1 VARCHAR(64) NOT NULL DEFAULT '',
  email_domain2 VARCHAR(64) NOT NULL DEFAULT '',
  email_domain3 VARCHAR(64) NOT NULL DEFAULT '',
  email_domain4 VARCHAR(64) NOT NULL DEFAULT '',
  `name` VARCHAR(255) NOT NULL DEFAULT '',
  `name_en` VARCHAR(255) NOT NULL DEFAULT '',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `address_en` VARCHAR(255) NOT NULL DEFAULT '',
  `email` VARCHAR(255) NOT NULL DEFAULT '',
  `website` VARCHAR(255) NOT NULL DEFAULT '',
  `tel` VARCHAR(32) NOT NULL DEFAULT '',
  `fax` VARCHAR(32) NOT NULL DEFAULT '',
  `zip_code` VARCHAR(32) NOT NULL DEFAULT '',
  `small_logo_url` VARCHAR(255) NOT NULL DEFAULT '',
  `logo_url` VARCHAR(255) NOT NULL DEFAULT '',
  `large_logo_url` VARCHAR(255) NOT NULL DEFAULT '',
  `cover_url` VARCHAR(255) NOT NULL DEFAULT '',
  `small_cover_url` VARCHAR(255) NOT NULL DEFAULT '',
  `large_cover_url` VARCHAR(255) NOT NULL DEFAULT '',
  `description` MEDIUMTEXT NOT NULL,
  `description_en` MEDIUMTEXT NOT NULL,
  `sk` VARCHAR(1024) NOT NULL DEFAULT '',

  `creator` BIGINT NOT NULL,
  `associated_id` BIGINT NOT NULL,
  `free_circle_ids` MEDIUMTEXT NOT NULL,

  PRIMARY KEY (`page_id`),
  INDEX (email_domain1),
  INDEX (email_domain2),
  INDEX (email_domain3),
  INDEX (email_domain4),
  INDEX (name),
  INDEX (creator),
  INDEX (associated_id)
) ENGINE InnoDB DEFAULT CHARSET utf8;
ALTER TABLE `page` AUTO_INCREMENT=20000000001;