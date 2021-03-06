CREATE TABLE USERS (
        USER_ID SERIAL, 
        CREATE_TIME timestamp NOT NULL DEFAULT now(), 
        UPDATE_TIME timestamp,
        USER_NAME VARCHAR(200) NOT NULL UNIQUE,
        EMAIL VARCHAR(255),
        gauth_token varchar(255), 
	locale varchar(100), 
	pic_link varchar(500), 
	given_name varchar(255), 
	family_name varchar(255), 
	full_name varchar(510)
        );        
ALTER TABLE USERS ADD PRIMARY KEY(USER_ID);
commit;


--Pi_Profile

CREATE TABLE pi_profile (
	PI_ID SERIAL, 
	CREATE_TIME timestamp DEFAULT now() NOT NULL, 
	UPDATE_TIME timestamp, 
	PI_SERIAL_ID varchar(100) NOT NULL, 
	NAME varchar(250), 
	IP_ADDRESS varchar(50), 
	SSH_PORT_NUMBER numeric(8,0), 
	USER_ID int4 DEFAULT 0 NOT NULL, 
	API_KEY uuid, 
PRIMARY KEY (pi_id), 
CONSTRAINT pi_profile_user_id_fkey 
FOREIGN KEY (user_id) 
	REFERENCES public.users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION
	);

CREATE UNIQUE INDEX pi_serial_idx ON PI_PROFILE (PI_SERIAL_ID);


CREATE TABLE MANAGED_APP (
	APP_ID SERIAL, 
	CREATE_TIME timestamp DEFAULT now() NOT NULL, 
	UPDATE_TIME timestamp, 
	VERSION_NUMBER numeric(12,0) DEFAULT 1 NOT NULL, 
	APP_NAME varchar(200), 
	FILE_NAME varchar(200) NOT NULL, 
	DEPLOYMENT_PATH varchar(2000) NOT NULL, 
	USER_ID int4 NOT NULL,
	WEB_NAME varchar(200),
PRIMARY KEY (app_id), 
CONSTRAINT managed_app_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT uniqueNameConst UNIQUE(APP_NAME,USER_ID)
);
CREATE UNIQUE INDEX MA_SEARCH_WEB_NAME ON MANAGED_APP (WEB_NAME);

--ALTER TABLE MANAGED_APP ADD CONSTRAINT uniqueNameConst UNIQUE(APP_NAME,USER_ID);
--ALTER TABLE MANAGED_APP ADD COLUMN WEB_NAME varchar(200);
	
	
CREATE TABLE PROFILE_MANAGED_APP (
	APP_ID int4 NOT NULL, 
	PI_ID int4 NOT NULL, 
CONSTRAINT profile_managed_app_app_id_fkey FOREIGN KEY (app_id) REFERENCES public.managed_app (app_id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
CONSTRAINT profile_managed_app_pi_id_fkey FOREIGN KEY (pi_id) REFERENCES public.pi_profile (pi_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE APP_CONFIG (
	CONFIG_ID SERIAL  NOT NULL, 
	CREATE_TIME timestamp DEFAULT now() NOT NULL, 
	UPDATE_TIME timestamp, 
	CONFIG_KEY varchar(255) NOT NULL, 
	CONFIG_VALUE varchar(255) NOT NULL,
PRIMARY KEY (CONFIG_ID)	
	);

--ALTER TABLE PROFILE_MANAGED_APP ADD FOREIGN KEY(APP_ID) REFERENCES MANAGED_APP(APP_ID);
--ALTER TABLE PROFILE_MANAGED_APP ADD FOREIGN KEY(PI_ID) REFERENCES PI_PROFILE(PI_ID);
ALTER TABLE PROFILE_MANAGED_APP ADD CONSTRAINT uniqueIdsConst UNIQUE(APP_ID,PI_ID);

--Logs
CREATE TABLE LOG_TYPE(
	LOG_TYPE_ID SERIAL  NOT NULL,
	LOG_TYPE_NAME varchar(255) NOT NULL,
PRIMARY KEY(LOG_TYPE_ID)
);

CREATE TABLE LOG_DATA (
	LOG_ID SERIAL  NOT NULL,
	PI_ID int4 NOT NULL,
	USER_ID int4 NOT NULL,
	APP_ID int4,
	LOG_TYPE_ID int4 DEFAULT 1 NOT NULL,
	CREATE_TIME timestamp DEFAULT now() NOT NULL,
	LOG_KEY varchar(512) NOT NULL,
	LOG_VALUE TEXT NOT NULL,
PRIMARY KEY (LOG_ID),	
CONSTRAINT log_Data_pi_profile_pi_id_fkey FOREIGN KEY (pi_id) REFERENCES public.pi_profile (pi_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT log_data_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT log_data_APP_id_fkey FOREIGN KEY (app_id) REFERENCES public.managed_app (app_id) ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT log_data_log_type_fkey FOREIGN KEY (log_type_id) REFERENCES public.log_type (log_type_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE INDEX LOG_SEARCH_LOG_KEY ON LOG_DATA (LOG_KEY);

INSERT INTO log_type (log_type_id, log_type_name) VALUES (1, 'SYSTEM');
INSERT INTO log_type (log_type_id, log_type_name) VALUES (2, 'APP_DATA_POINT');
INSERT INTO log_type (log_type_id, log_type_name) VALUES (3, 'SYSTEM_DEBUG');
INSERT INTO log_type (log_type_id, log_type_name) VALUES (4, 'APP_DEBUG');




--for Ubuntu:  sudo apt-get install postgresql-contrib-9.1   
--select * from pg_available_extensions where name like 'uuid%' order by name desc;
--CREATE EXTENSION "uuid-ossp";
--select * from pg_extension;
--select uuid_generate_v4() as one;

--INSERT INTO app_config (config_key, config_value) VALUES ('key_name', 'value');
