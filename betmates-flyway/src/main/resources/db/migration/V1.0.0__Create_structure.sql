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