USE `betmates`;

CREATE TABLE `app_users` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(128) NOT NULL,
	`pseudo` VARCHAR(64) NOT NULL,
	`password` VARCHAR(128) NOT NULL,
	`status` VARCHAR(16) NOT NULL DEFAULT 'NOT_CONFIRMED',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `idx_unique_app_users_email` (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `app_users_roles` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`app_user_id` INT NOT NULL,
	`role` VARCHAR(64) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;