package com.meadowhawk.homepi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ManageDB {
	@Autowired
    private DataSource localDataSource;

    public static void main(String[] args) throws Exception {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:beans.xml");
    }

    //Sample below.
    public void myRealMainMethod() throws SQLException {
        Statement stmt = localDataSource.getConnection().createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
    }
    
    @PostConstruct
    public void buildDatabase() throws SQLException{
    	Statement stmt = null;
    	try{
    	stmt = localDataSource.getConnection().createStatement();
//        stmt.executeUpdate("CREATE TABLE PI_PROFILE (PI_ID SERIAL, CREATE_TIME timestamp, UPDATE_TIME timestamp)");
//        stmt.executeUpdate("ALTER TABLE PI_PROFILE ADD COLUMN PI_SERIAL_ID varchar(100)");
//        stmt.executeUpdate("ALTER TABLE PI_PROFILE ADD COLUMN NAME varchar(250)");
//        stmt.executeUpdate("ALTER TABLE PI_PROFILE ADD COLUMN IP_ADDRESS varchar(50)");
//        stmt.executeUpdate("ALTER TABLE PI_PROFILE ADD COLUMN SSH_PORT_NUMBER numeric(8)");
//        stmt.executeUpdate("CREATE UNIQUE INDEX pi_serial_idx ON PI_PROFILE (PI_SERIAL_ID)");
//        stmt.executeUpdate("ALTER TABLE PI_PROFILE ADD CONSTRAINT piPropfileConst UNIQUE (PI_SERIAL_ID)");
//    	  stmt.executeUpdate("ALTER TABLE PI_PROFILE UPDATE COLUMN CREATE_TIME TIMESTAMP NOT NULL DEFAULT now()");
//    	  stmt.executeUpdate("ALTER TABLE PI_PROFILE ALTER COLUMN CREATE_TIME SET NOT NULL");
//    	  stmt.executeUpdate("ALTER TABLE PI_PROFILE ALTER COLUMN CREATE_TIME SET DEFAULT now()");
//    	  stmt.executeUpdate("ALTER TABLE PI_PROFILE ALTER COLUMN PI_SERIAL_ID SET NOT NULL");
    	
//	    	//ADD User Table
//	    	stmt.executeUpdate("CREATE TABLE USERS (USER_ID SERIAL, CREATE_TIME timestamp, UPDATE_TIME timestamp)");
//	    	stmt.executeUpdate("ALTER TABLE USERS ADD PRIMARY KEY(USER_ID)");
//	    	stmt.executeUpdate("ALTER TABLE USERS ADD COLUMN USER_NAME VARCHAR(200) NOT NULL");
//	    	stmt.executeUpdate("ALTER TABLE USERS ADD CONSTRAINT userNameConst UNIQUE (USER_NAME)");
//	    	stmt.executeUpdate("ALTER TABLE USERS UPDATE COLUMN CREATE_TIME TIMESTAMP NOT NULL DEFAULT now()");
//
//    	
//    		//ADD ManagedApps Tables
//    		stmt.executeUpdate("CREATE TABLE MANAGED_APP (APP_ID SERIAL, CREATE_TIME timestamp, UPDATE_TIME timestamp)");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD PRIMARY KEY(APP_ID)");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD COLUMN VERSION_NUMBER numeric(12) NOT NULL DEFAULT 1");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD COLUMN APP_NAME VARCHAR(200)");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD COLUMN FILE_NAME VARCHAR(200) NOT NULL");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD COLUMN DEPLOYMENT_PATH VARCHAR(2000) NOT NULL");
//    		stmt.executeUpdate("ALTER TABLE MANAGED_APP ADD FOREIGN KEY(USER_ID) REFERENCES USERS(USER_ID)");
//    		
//    		//ADD MapTable for ManagedApps - USER_ID, APP_ID, and PI_ID are unique in combination
//    		stmt.executeUpdate("CREATE TABLE USER_PI_MANAGED_APP (USER_ID INT)");
//    		stmt.executeUpdate("ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(USER_ID) REFERENCES USERS(USER_ID)");
//    		stmt.executeUpdate("ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(APP_ID) REFERENCES MANAGED_APP(APP_ID)");
//    		stmt.executeUpdate("ALTER TABLE USER_PI_MANAGED_APP ADD FOREIGN KEY(PI_ID) REFERENCES PI_PROFILE(PI_ID)");
//    		stmt.executeUpdate("ALTER TABLE USER_PI_MANAGED_APP ADD CONSTRAINT uniqueIdsConst UNIQUE(USER_ID,APP_ID,PI_ID)");
    		
    		
    		
    	} finally {
    		if (stmt != null) {
    			stmt.close();
			}
    	}
	}
    
}
