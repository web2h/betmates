INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (1, 'test.user@email.com', 'password', 'Test', 'User');

INSERT INTO platforms (`id`, `name`) VALUES (1, 'Existing platform');

INSERT INTO platform_supervisors (`platform_id`, `app_user_id`, `administrator`, `function`, `nickname`) VALUES (1, 1, 'Y', 'TEACHER', 'Prof Test');

INSERT INTO log_events (`id`, `platform_id`, `app_user_id`, `event_ts`, `event_type`) VALUES (1, 1, 1, '2018-03-02 18:00:00', 'PLATFORM_CREATION');
INSERT INTO log_events (`id`, `platform_id`, `app_user_id`, `event_ts`, `event_type`) VALUES (2, 1, 1, '2018-03-02 18:00:01', 'SUPERVISOR_ADDITION');

INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `old_value`, `new_value`) VALUES (1, 1, 'name', null, 'Existing platform');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `old_value`, `new_value`) VALUES (2, 2, 'administrator', null, 'true');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `old_value`, `new_value`) VALUES (3, 2, 'function', null, 'TEACHER');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `old_value`, `new_value`) VALUES (4, 2, 'nickname', null, 'Prof Test');