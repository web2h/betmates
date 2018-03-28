INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (1, 'test.user@email.com', 'password', 'Test', 'User');
INSERT INTO app_users (`id`, `email`, `password`, `first_name`, `last_name`) VALUES (2, 'test.user2@email.com', 'password', 'Test', 'User2');

INSERT INTO platforms (`id`, `name`) VALUES (1, 'Test platform 1');
INSERT INTO log_events (`id`, `event_ts`, `platform_id`, `app_user_id`, `event_type`) 
	VALUES (1, CURRENT_TIMESTAMP(), 1, 1, 'PLATFORM_CREATION');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `new_value`) 
	VALUES (1, 1, 'NAME', 'Test platform 1');
		
INSERT INTO platforms (`id`, `name`) VALUES (2, 'Test platform 2');
INSERT INTO log_events (`id`, `event_ts`, `platform_id`, `app_user_id`, `event_type`) 
	VALUES (2, CURRENT_TIMESTAMP(), 2, 1, 'PLATFORM_CREATION');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `new_value`) 
	VALUES (2, 2, 'NAME', 'Test platform 2');
	
UPDATE platforms SET `name` = 'Test platform 2 updated' WHERE `id` = 2;
INSERT INTO log_events (`id`, `event_ts`, `platform_id`, `app_user_id`, `event_type`) 
	VALUES (3, CURRENT_TIMESTAMP(), 2, 2, 'PLATFORM_EDITION');
INSERT INTO log_event_changes (`id`, `log_event_id`, `field`, `old_value`, `new_value`) 
	VALUES (3, 3, 'NAME', 'Test platform 2', 'Test platform 2 updated');