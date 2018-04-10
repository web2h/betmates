INSERT INTO app_users (`id`, `email`, `password`, `alias`, `role`, `status`) VALUES (1, 'admin@betmates.com', 'password', 'Admin', 'ROLE_ADMINISTRATOR', 'ACTIVE');

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (1, 'France', 'France', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (2, 'Paris', 'Paris', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (3, 'Lille', 'Lille', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (4, 'Parc des Princes', 'Parc des Princes', 'VENUE', 2);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (5, 'Stade Charléty', 'Stade Charléty', 'VENUE', 2);

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (6, 'Belgium', 'Belgique', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (7, 'Brussels', 'Bruxelles', 'CITY', 6);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (8, 'Stade du Roi Baudouin', 'Stade du Roi Baudoin', 'VENUE', 7);

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (9, 'Miami Heat', 'Miami Heat', 'TEAM', 'BASKET_BALL', 'Heat', 'Heat');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (10, 'Boston Celtics', 'Boston Celtics', 'TEAM', 'BASKET_BALL', 'Celtics', 'Celtics');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (11, 'Orlando Magic', 'Orlando Magic', 'TEAM', 'BASKET_BALL', 'Magic', 'Magic');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `sport`, `short_name_en`, `short_name_fr`) VALUES (12, 'Chicago Bulls', 'Chicago Bulls', 'TEAM', 'BASKET_BALL', 'Bulls', 'Bulls');

INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (1, 1, '2018-04-01 18:00:00', 'CREATION', 1);
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (1, 1, 'NAME_EN', 'France');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (2, 1, 'NAME_FR', 'France');

INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (2, 1, '2018-04-01 18:00:01', 'CREATION', 2);
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (3, 2, 'NAME_EN', 'Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (4, 2, 'NAME_FR', 'Paris');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (5, 2, 'COUNTRY', 'France / France');

INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (3, 1, '2018-04-01 18:00:02', 'CREATION', 3);
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (6, 3, 'NAME_EN', 'Lille');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (7, 3, 'NAME_FR', 'Lille');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (8, 3, 'COUNTRY', 'France / France');

INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (4, 1, '2018-04-01 18:00:03', 'CREATION', 4);
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (9, 4, 'NAME_EN', 'Parc des Princes');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (10, 4, 'NAME_FR', 'Parc des Princes');
INSERT INTO reference_log_event_changes (`id`, `log_event_id`, `field`, `new_value`) VALUES (11, 4, 'CITY', 'Paris / Paris');

INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (9, 1, '2018-04-01 18:00:04', 'CREATION', 9);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (10, 1, '2018-04-01 18:00:05', 'CREATION', 10);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (11, 1, '2018-04-01 18:00:06', 'CREATION', 11);
INSERT INTO reference_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `reference_id`) VALUES (12, 1, '2018-04-01 18:00:07', 'CREATION', 12);

INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (1, 'Russia 2018', 'Russie 2018', 'FIFA_WORLD_CUP', '2018-06-10 20:00:00');
INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (2, 'NBA Playoffs 2018', 'NBA Playoffs 2018', 'NBA_PLAYOFFS', '2018-04-15 20:00:00');

INSERT INTO `competition_teams` (`id`, `competition_id`, `team_id`) VALUES (1, 2, 9);
INSERT INTO `competition_teams` (`id`, `competition_id`, `team_id`) VALUES (1, 2, 10);

INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`) VALUES (1, 1, '2018-04-02 18:00:02', 'CREATION', 1);
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`) VALUES (2, 1, '2018-04-02 18:00:03', 'CREATION', 2);
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`, `Team addition`) VALUES (3, 1, '2018-04-02 18:00:04', 'TEAM_ADDITION', 2);
INSERT INTO competition_log_events (`id`, `app_user_id`, `event_ts`, `event_type`, `competition_id`, `Team addition`) VALUES (4, 1, '2018-04-02 18:00:04', 'TEAM_ADDITION', 2);