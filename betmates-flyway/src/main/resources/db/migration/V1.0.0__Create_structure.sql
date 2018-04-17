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

CREATE TABLE `basic_references` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name_en` VARCHAR(64) NOT NULL,
	`name_fr` VARCHAR(64) NOT NULL,
	`discriminator` VARCHAR(16) NOT NULL,
	`country_id` INT DEFAULT NULL,
	`city_id` INT DEFAULT NULL,
	`sport` VARCHAR(16) DEFAULT NULL,
	`short_name_en` VARCHAR(16) DEFAULT NULL,
	`short_name_fr` VARCHAR(16) DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_basic_references_country_id` FOREIGN KEY (`country_id`) REFERENCES `basic_references` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT `fk_basic_references_city_id` FOREIGN KEY (`city_id`) REFERENCES `basic_references` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reference_log_events` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`event_ts` TIMESTAMP NOT NULL,
	`reference_id` INT NOT NULL,
	`app_user_id` INT NOT NULL,
	`event_type` VARCHAR(32) NOT NULL,
	`description` VARCHAR(512) DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_reference_log_events_basic_references_reference_id` FOREIGN KEY (`reference_id`) REFERENCES `basic_references` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT `fk_reference_log_events_app_users_app_user_id` FOREIGN KEY (`app_user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reference_log_event_changes` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`field` VARCHAR(16) NOT NULL,
	`old_value` VARCHAR(1024) DEFAULT NULL,
	`new_value` VARCHAR(1024) DEFAULT NULL,
	`log_event_id` INT NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_reference_log_event_changes_log_events_log_event_id` FOREIGN KEY (`log_event_id`) REFERENCES `reference_log_events` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `competitions` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name_en` VARCHAR(64) NOT NULL,
	`name_fr` VARCHAR(64) NOT NULL,
	`competition_type` VARCHAR(32) NOT NULL,
	`start_date` DATE NOT NULL,
	PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `competition_teams` (
	`competition_id` INT NOT NULL,
	`team_id` INT NOT NULL,
	`competition_group` VARCHAR(32) DEFAULT NULL,
	`position` TINYINT UNSIGNED DEFAULT 0,
	PRIMARY KEY (`competition_id`, `team_id`),
	CONSTRAINT `fk_competition_teams_competitions_competition_id` FOREIGN KEY (`competition_id`) REFERENCES `competitions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `fk_competition_teams_references_team_id` FOREIGN KEY (`team_id`) REFERENCES `basic_references` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `competition_venues` (
	`competition_id` INT NOT NULL,
	`venue_id` INT NOT NULL,
	PRIMARY KEY (`competition_id`, `venue_id`),
	CONSTRAINT `fk_competition_teams_competitions_competition_id` FOREIGN KEY (`competition_id`) REFERENCES `competitions` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `fk_competition_teams_references_venue_id` FOREIGN KEY (`venue_id`) REFERENCES `basic_references` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `game_groups` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`competition_id` INT NOT NULL,
	`group_type` VARCHAR(128) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_game_groups_competitions_competition_id` FOREIGN KEY (`competition_id`) REFERENCES `competitions` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `games` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`game_group_id` INT NOT NULL,
	`venue_id` INT NOT NULL,
	`team_home_id` INT DEFAULT NULL,
	`team_away_id` INT DEFAULT NULL,
	`start_date` DATE NOT NULL,
	`confirmed` VARCHAR(1) NOT NULL DEFAULT 'Y'
	`team_home_score` TINYINT UNSIGNED DEFAULT 0,
	`team_away_score` TINYINT UNSIGNED DEFAULT 0,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_games_game_groups_game_group_id` FOREIGN KEY (`game_group_id`) REFERENCES `game_groups` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT `fk_games_references_venue_id` FOREIGN KEY (`venue_id`) REFERENCES `basic_references` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `fk_games_references_team_home_id` FOREIGN KEY (`team_home_id`) REFERENCES `basic_references` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `fk_games_references_team_away_id` FOREIGN KEY (`team_away_id`) REFERENCES `basic_references` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;