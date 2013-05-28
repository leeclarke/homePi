--Create Test Users
INSERT INTO users ( create_time, update_time, user_name, email, gauth_token, locale, pic_link, given_name, family_name, full_name ) VALUES ( '2013-04-16 16:05:02', NULL, 'test_user', 'tester@homepi.com', 'XD123-YT53', 'en', NULL, 'Test', 'User', 'Test User' );
INSERT INTO users ( create_time, update_time, user_name, email, gauth_token, locale, pic_link, given_name, family_name, full_name ) VALUES ( '2013-05-09 17:27:41', '2013-05-16 13:03:11', 'test-user42', 'test-user2@homepi.org', 'XYZ-123', 'en', 'http://homepi.org/mypic.jpg', 'Testaz', 'SuperUser', NULL );
INSERT INTO users ( create_time, update_time, user_name, email, gauth_token, locale, pic_link, given_name, family_name, full_name ) VALUES ( '2013-05-02 18:10:10', '2013-05-07 18:50:49', 'Lee Clarke', 'lee.k.clarke@gmail.com', 'ya29.AHES6ZSt_pU25hL9z5nZmKDfUuHbe0NT8qTaUEonfj6dIya3J6ljZxM', 'en', 'https://lh4.googleusercontent.com/-5KIKQF7_2og/AAAAAAAAAAI/AAAAAAAACR8/5SSgJOcVEWA/photo.jpg' , 'Lee', 'Clarke', 'Lee Clarke' );


--Create Pi_profiles
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-02-02 03:34:01', '2013-02-02 03:34:01', '12345', NULL, '127.0.0.1', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-01-10 03:28:49', '2013-01-10 03:28:49', 'rig5qk12t7', 'Test Pi', '129.168.1.52', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-02-09 03:18:25', '2013-02-09 03:18:25', '8lhdfenm1x', 'Test Pi', '129.168.1.52', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-02-09 03:51:47', '2013-02-09 04:34:33', 'ERROR000000000', 'LocalDevDevice', '127.0.0.1', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-02-18 19:47:05', '2013-02-18 19:51:04', '0000000040ee4dd0', 'WeatherPi_Seffner', '10.125.117.219', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-04-19 15:46:22', '2013-04-19 15:46:22', '', NULL, '', NULL, 1, NULL );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-04-18 20:36:52', NULL, 'hls1zeugsi', 'Test Pi', '129.168.1.52', NULL, 1, '94281717-62f2-4f8f-820d-0d709581521f' );
INSERT INTO pi_profile ( create_time, update_time, pi_serial_id, name, ip_address, ssh_port_number, user_id, api_key ) VALUES ( '2013-01-10 03:14:32', '2013-05-17 01:30:50', '2e848bg934', 'New Name', '129.168.1.6', 9090, 1, '7dbd8467-1f3d-4b2f-b7b2-0b66e59a55c6' );
    
 --Set up managed apps
INSERT INTO managed_app ( create_time, update_time, version_number, app_name, file_name, deployment_path, user_id, web_name ) VALUES ( '2013-04-19 19:27:40', NULL, 1, 'TestApp2', 'TestFile.jar', '/usr/home/pi/test', 1 ,'TestApp2');
 
UPDATE managed_app SET update_time = now(), web_name = 'TestApp2' WHERE app_id = 6;
UPDATE managed_app SET update_time = now(), web_name = 'Shiny_Test_App' WHERE app_id = 9;
UPDATE managed_app SET version_number = 1, web_name = 'Shiny_Test_App' WHERE app_id = 7;
 
 --Updates for TEst support
 UPDATE pi_profile SET update_time = now(), pi_serial_id = 'pi456y765', name = 'Another Test Pi', ip_address = '192.168.1.3', ssh_port_number = 22, user_id = 2, api_key = uuid_generate_v4() WHERE pi_id = 6;
 
 --associate a profile and app
 INSERT INTO profile_managed_app (app_id, pi_id) VALUES (6, 1);
 
 --create some log data
 --INSERT INTO log_type (log_type_name) VALUES ('SYSTEM');
 INSERT INTO log_type (log_type_name) VALUES ('APP_DATA_POINT');
 INSERT INTO log_type (log_type_name) VALUES ('SYSTEM_DEBUG');
 INSERT INTO log_type (log_type_name) VALUES ('APP_DEBUG');
 
 INSERT INTO log_data (pi_id, user_id, app_id, log_type_id, log_key, log_value) VALUES (1,1, 6, 2, 'Device Temp', '69.8');
 INSERT INTO log_data (pi_id, user_id, app_id, log_type_id, log_key, log_value) VALUES (1,1, 6, 2, 'Device Temp', '69.5');
 INSERT INTO log_data (pi_id, user_id, app_id, log_type_id, log_key, log_value) VALUES (1,1, 6, 2, 'Device Temp', '67.5');
 INSERT INTO log_data (pi_id, user_id, app_id, log_type_id, log_key, log_value) VALUES (1,1, 6, 4, 'APP_ERROR', 'Sensor not responding');
 INSERT INTO log_data (pi_id, user_id, log_type_id, log_key, log_value) VALUES (1,1, 1, 'ERROR', 'Could Not Download Updates');
 