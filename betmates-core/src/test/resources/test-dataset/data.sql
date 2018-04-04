INSERT INTO app_users (`id`, `email`, `password`, `alias`, `role`, `status`) VALUES (1, 'admin@betmates.com', 'password', 'Admin', 'ROLE_ADMINISTRATOR', 'ACTIVE');

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (1, 'France', 'France', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (2, 'Paris', 'Paris', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (3, 'Lille', 'Lille', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (4, 'Parc des Princes', 'Parc des Princes', 'VENUE', 2);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (5, 'Stade Charléty', 'Stade Charléty', 'VENUE', 2);

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (6, 'Belgium', 'Belgique', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (7, 'Brussels', 'Bruxelles', 'CITY', 6);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (8, 'Stade du Roi Baudouin', 'Stade du Roi Baudoin', 'VENUE', 7);

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

INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (1, 'Russia 2018', 'Russie 2018', 'FIFA_WORLD_CUP', '2018-06-10 20:00:00');
INSERT INTO competitions (`id`, `name_en`, `name_fr`, `competition_type`, `start_date`) VALUES (2, 'NBA Playoffs 2018', 'NBA Playoffs 2018', 'NBA_PLAYOFFS', '2018-04-15 20:00:00');