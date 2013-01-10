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
    	
    	} finally {
    		if (stmt != null) {
    			stmt.close();
			}
    	}
	}
    
}
