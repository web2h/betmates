USE `betmates`;

CREATE TABLE `app_users` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(128) NOT NULL,
	`alias` VARCHAR(64) NOT NULL,
	`password` VARCHAR(128) NOT NULL,
	`role` VARCHAR(32) NOT NULL DEFAULT 'PLAYER',
	`status` VARCHAR(16) NOT NULL DEFAULT 'NOT_CONFIRMED',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `idx_unique_app_users_email` (email),
	UNIQUE INDEX `idx_unique_app_users_alias` (alias)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `core_data` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name_en` VARCHAR(64) NOT NULL,
	`name_fr` VARCHAR(64) NOT NULL,
	`discriminator` VARCHAR(16) NOT NULL,
	`country_id` INT DEFAULT NULL,
	`city_id` INT DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_core_data_country_id` FOREIGN KEY (`country_id`) REFERENCES `core_data` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT `fk_core_data_references_city_id` FOREIGN KEY (`city_id`) REFERENCES `core_data` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `competitions` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name_en` VARCHAR(64) NOT NULL,
	`name_fr` VARCHAR(64) NOT NULL,
	`competition_type` VARCHAR(32) NOT NULL,
	`start_date` DATE NOT NULL,
	PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;