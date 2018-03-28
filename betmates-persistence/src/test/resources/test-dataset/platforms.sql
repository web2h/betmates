INSERT INTO platforms (`id`, `name`) VALUES (1, 'Test platform 1');
INSERT INTO platforms (`id`, `name`) VALUES (2, 'Test platform 2');
INSERT INTO platforms (`id`, `name`) VALUES (3, 'Test platform 3');

INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (1, 'test.user@email.com', 'password', 'Test', 'User');
INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (2, 'test.user2@email.com', 'password', 'Test', 'User2');
INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (3, 'test.user3@email.com', 'password', 'Test', 'User3');

INSERT INTO platform_supervisors (`app_user_id`, `platform_id`, `nickname`, `function`) VALUES (1, 1, 'testUser', 'TEACHER');
INSERT INTO platform_supervisors (`app_user_id`, `platform_id`, `nickname`, `function`) VALUES (1, 2, 'testUser', 'PARENT');
INSERT INTO platform_supervisors (`app_user_id`, `platform_id`, `nickname`, `function`) VALUES (2, 1, 'testUser2', 'SCHOOL_STAFF');
