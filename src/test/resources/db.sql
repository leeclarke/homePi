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
PRIMARY KEY (app_id), 
CONSTRAINT managed_app_user_id_fkey FOREIGN KEY (user_id) 
	REFERENCES public.users (user_id) ON UPDATE NO ACTION ON DELETE NO ACTION);
ALTER TABLE MANAGED_APP ADD CONSTRAINT uniqueNameConst UNIQUE(APP_NAME,USER_ID);
	
	
CREATE TABLE PROFILE_MANAGED_APP (
	APP_ID int4 NOT NULL, 
	PI_ID int4 NOT NULL, 
CONSTRAINT profile_managed_app_app_id_fkey FOREIGN KEY (app_id) REFERENCES public.managed_app (app_id) ON UPDATE NO ACTION ON DELETE NO ACTION, 
CONSTRAINT profile_managed_app_pi_id_fkey FOREIGN KEY (pi_id) REFERENCES public.pi_profile (pi_id) ON UPDATE NO ACTION ON DELETE NO ACTION);


CREATE TABLE APP_CONFIG (
	CONFIG_ID SERIAL  NOT NULL, 
	CREATE_TIME timestamp DEFAULT now() NOT NULL, 
	UPDATE_TIME timestamp, 
	CONFIG_KEY varchar(255) NOT NULL, 
	CONFIG_VALUE varchar(255) NOT NULL
	);


ALTER TABLE PROFILE_MANAGED_APP ADD FOREIGN KEY(APP_ID) REFERENCES MANAGED_APP(APP_ID)
ALTER TABLE PROFILE_MANAGED_APP ADD FOREIGN KEY(PI_ID) REFERENCES PI_PROFILE(PI_ID)
ALTER TABLE PROFILE_MANAGED_APP ADD CONSTRAINT uniqueIdsConst UNIQUE(APP_ID,PI_ID)
ALTER TABLE PROFILE_MANAGED_APP DROP COLUMN CREATE_TIME


--select * from pg_available_extensions where name like 'uuid%' order by name desc;
--CREATE EXTENSION "uuid-ossp";
--select * from pg_extension;
--select uuid_generate_v4() as one;

--ALTER TABLE PI_PROFILE UPDATE COLUMN api_key DEFAULT




    	



//    		//ADD ManagedApps Tables
CREATE TABLE MANAGED_APP (APP_ID SERIAL, CREATE_TIME timestamp, UPDATE_TIME timestamp);
ALTER TABLE MANAGED_APP ADD PRIMARY KEY(APP_ID);
ALTER TABLE MANAGED_APP ADD COLUMN VERSION_NUMBER numeric(12) NOT NULL DEFAULT 1;
ALTER TABLE MANAGED_APP ADD COLUMN APP_NAME VARCHAR(200);
ALTER TABLE MANAGED_APP ADD COLUMN FILE_NAME VARCHAR(200) NOT NULL;
ALTER TABLE MANAGED_APP ADD COLUMN DEPLOYMENT_PATH VARCHAR(2000) NOT NULL;
ALTER TABLE MANAGED_APP ADD FOREIGN KEY(USER_ID) REFERENCES USERS(USER_ID);
//    		
//    		//ADD MapTable for ManagedApps - USER_ID, APP_ID, and PI_ID are unique in combination
CREATE TABLE USER_PI_MANAGED_APP (USER_ID INT);
ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(USER_ID) REFERENCES USERS(USER_ID);
ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(APP_ID) REFERENCES MANAGED_APP(APP_ID);
ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(PI_ID) REFERENCES PI_PROFILE(PI_ID);
ALTER TABLE USER_PI_MANAGED_APP ADD CONSTRAINT uniqueIdsConst UNIQUE(USER_ID,APP_ID,PI_ID);


CREATE TABLE MANAGED_APP (
        APP_ID SERIAL, 
        CREATE_TIME timestamp NOT NULL DEFAULT now(), 
        UPDATE_TIME timestamp,
        VERSION_NUMBER numeric(12) NOT NULL DEFAULT 1,
        APP_NAME VARCHAR(200),
        FILE_NAME VARCHAR(200) NOT NULL,
        DEPLOYMENT_PATH VARCHAR(2000) NOT NULL,
        COLUMN USER_ID INT NOT NULL      
);

*/


--ALTER TABLE MANAGED_APP ALTER COLUMN USER_ID SET NOT NULL;
--ALTER TABLE MANAGED_APP ALTER COLUMN CREATE_TIME SET  DEFAULT now()
--ALTER TABLE MANAGED_APP ADD FOREIGN KEY(USER_ID) REFERENCES USERS(USER_ID)
--INSERT INTO users ( user_name) VALUES ('test_user');

/*
CREATE TABLE PROFILE_MANAGED_APP (
        APP_ID INT NOT NULL,
        PI_ID INT NOT NULL
)
*
--update PI_PROFILE set api_key = uuid_generate_v4() where pi_id = 18
/*
CREATE TABLE APP_CONFIG (
        CONFIG_ID SERIAL, 
        CREATE_TIME timestamp NOT NULL DEFAULT now(), 
        UPDATE_TIME timestamp,
        CONFIG_KEY VARCHAR(255) NOT NULL,
        CONFIG_VALUE VARCHAR(255) NOT NULL
)
*/
--INSERT INTO app_config (config_key, config_value) VALUES ('key_name', 'value');
