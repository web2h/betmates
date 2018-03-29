INSERT INTO app_users (`id`, `email`, `password`, `alias`, `role`, `status`) VALUES (1, 'admin@betmates.com', 'password', 'Admin', 'ROLE_ADMINISTRATOR', 'ACTIVE');

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (1, 'France', 'France', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (2, 'Paris', 'Paris', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (3, 'Lille', 'Lille', 'CITY', 1);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (4, 'Parc des Princes', 'Parc des Princes', 'VENUE', 2);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (5, 'Stade Charléty', 'Stade Charléty', 'VENUE', 2);

INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`) VALUES (6, 'Belgium', 'Belgique', 'COUNTRY');
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `country_id`) VALUES (7, 'Brussels', 'Bruxelles', 'CITY', 6);
INSERT INTO basic_references (`id`, `name_en`, `name_fr`, `discriminator`, `city_id`) VALUES (8, 'Stade du Roi Baudouin', 'Stade du Roi Baudoin', 'VENUE', 7);