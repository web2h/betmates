-- APP_USERS
INSERT INTO app_users (`id`, `email`, `password`, `alias`, `role`, `status`) VALUES (1, 'admin@betmates.com', 'password', 'Admin', 'ROLE_ADMINISTRATOR', 'ACTIVE');

-- COUNTRIES
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (1, 'France', 'France', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (2, 'Belgium', 'Belgique', 'COUNTRY');

-- CITIES
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (11, 'Paris', 'Paris', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (12, 'Lille', 'Lille', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (13, 'Brussels', 'Bruxelles', 'CITY', 2);

-- VENUES
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (21, 'Parc des Princes', 'Parc des Princes', 'VENUE', 11);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (22, 'Stade Charléty', 'Stade Charléty', 'VENUE', 11);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (23, 'Stade du Roi Baudouin', 'Stade du Roi Baudoin', 'VENUE', 13);

-- TEAMS
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (31, 'Miami Heat', 'Miami Heat', 'TEAM', 'BASKET_BALL', 'Heat', 'Heat');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (32, 'Boston Celtics', 'Boston Celtics', 'TEAM', 'BASKET_BALL', 'Celtics', 'Celtics');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (33, 'Orlando Magic', 'Orlando Magic', 'TEAM', 'BASKET_BALL', 'Magic', 'Magic');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (34, 'Chicago Bulls', 'Chicago Bulls', 'TEAM', 'BASKET_BALL', 'Bulls', 'Bulls');

-- COMPETITIONS
INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (1, 'Russia 2018', 'Russie 2018', 'FIFA_WORLD_CUP', '2018-06-10 20:00:00');
INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (2, 'NBA Playoffs 2018', 'NBA Playoffs 2018', 'NBA_PLAYOFFS', '2018-04-15 20:00:00');

-- COMPETITION_TEAMS
INSERT INTO `competition_teams` (`competition_id`, `team_id`) VALUES (2, 31);
INSERT INTO `competition_teams` (`competition_id`, `team_id`) VALUES (2, 32);

-- REFERENCE_LOG_EVENTS
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (1, 1, '2018-04-01 18:00:00', 'CREATION', 1);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (2, 1, '2018-04-01 18:00:01', 'CREATION', 2);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (3, 1, '2018-04-01 18:00:02', 'CREATION', 11);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (4, 1, '2018-04-01 18:00:03', 'CREATION', 12);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (5, 1, '2018-04-01 18:00:04', 'CREATION', 13);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (6, 1, '2018-04-01 18:00:05', 'CREATION', 21);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (7, 1, '2018-04-01 18:00:06', 'CREATION', 22);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (8, 1, '2018-04-01 18:00:07', 'CREATION', 23);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (9, 1, '2018-04-01 18:00:07', 'CREATION', 31);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (10, 1, '2018-04-01 18:00:07', 'CREATION', 32);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (11, 1, '2018-04-01 18:00:07', 'CREATION', 33);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (12, 1, '2018-04-01 18:00:07', 'CREATION', 34);

-- REFERENCE_LOG_EVENT_CHANGES
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (1, 1, 'NAME_EN', 'France');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (2, 1, 'NAME_FR', 'France');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (3, 2, 'NAME_EN', 'Belgium');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (4, 2, 'NAME_FR', 'Belgique');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (5, 3, 'NAME_EN', 'Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (6, 3, 'NAME_FR', 'Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (7, 3, 'COUNTRY', '(1)France');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (8, 4, 'NAME_EN', 'Lille');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (9, 4, 'NAME_FR', 'Lille');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (10, 4, 'COUNTRY', '(1)France');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (11, 5, 'NAME_EN', 'Brussels');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (12, 5, 'NAME_FR', 'Bruxelles');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (13, 5, 'COUNTRY', '(1)Belgium');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (14, 6, 'NAME_EN', 'Parc des Princes');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (15, 6, 'NAME_FR', 'Parc des Princes');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (16, 6, 'CITY', '(11)Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (17, 7, 'NAME_EN', 'Stade Charléty');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (18, 7, 'NAME_FR', 'Stade Charléty');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (19, 7, 'CITY', '(11)Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (20, 8, 'NAME_EN', 'Stade du Roi Baudouin');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (21, 8, 'NAME_FR', 'Stade du Roi Baudouin');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (22, 8, 'CITY', '(13)Brussels');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (23, 9, 'NAME_EN', 'Miami Heat');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (24, 9, 'NAME_FR', 'Miami Heat');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (25, 9, 'SPORT', 'Basket-Ball');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (26, 9, 'SHORT_NAME_EN', 'Heat');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (27, 9, 'SHORT_NAME_FR', 'Heat');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (28, 10, 'NAME_EN', 'Boston Celtics');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (29, 10, 'NAME_FR', 'Boston Celtics');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (30, 10, 'SPORT', 'Basket-Ball');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (31, 10, 'SHORT_NAME_EN', 'Celtics');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (32, 10, 'SHORT_NAME_FR', 'Celtics');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (33, 11, 'NAME_EN', 'Orlando Magic');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (34, 11, 'NAME_FR', 'Orlando Magic');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (35, 11, 'SPORT', 'Basket-Ball');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (36, 11, 'SHORT_NAME_EN', 'Magic');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (37, 11, 'SHORT_NAME_FR', 'Magic');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (38, 12, 'NAME_EN', 'Chicago Bulls');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (39, 12, 'NAME_FR', 'Chicago Bulls');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (40, 12, 'SPORT', 'Basket-Ball');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (41, 12, 'SHORT_NAME_EN', 'Bulls');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (42, 12, 'SHORT_NAME_FR', 'Bulls');

-- COMPETITION_LOG_EVENTS
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`) VALUES (1, 1, '2018-04-02 18:00:02', 'CREATION', 1);
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`) VALUES (2, 1, '2018-04-02 18:00:03', 'CREATION', 2);
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`, `description`) VALUES (3, 1, '2018-04-02 18:00:04', 'TEAM_ADDITION', 2, 'Team (31)Miami Heat has been added');
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`, `description`) VALUES (4, 1, '2018-04-02 18:00:04', 'TEAM_ADDITION', 2, 'Team (32)Boston Celtics been added');

-- COMPETITION_LOG_EVENT_CHANGES
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (1, 1, 'NAME_EN', 'Russia 2018');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (2, 1, 'NAME_FR', 'Russie 2018');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (3, 1, 'TYPE', 'FIFA World Cup');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (4, 1, 'START_DATE', '2018-06-10 20:00:00');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (5, 2, 'NAME_EN', 'NBA Playoffs 2018');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (6, 2, 'NAME_FR', 'NBA Playoffs 2018');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (7, 2, 'TYPE', 'NBA Playoffs');
INSERT INTO competition_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (8, 2, 'START_DATE', '2018-04-15 20:00:00');