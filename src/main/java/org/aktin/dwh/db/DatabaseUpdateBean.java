package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import liquibase.exception.LiquibaseException;

/**
 * Perform liquibase update at EJB/CDI startup
 * 
 * TODO integration test to verify that this works in Wildfly
 * 
 * @author R.W.Majeed
 *
 */
@Startup
@Singleton
public class DatabaseUpdateBean {

	public DatabaseUpdateBean(){
		// TODO acquire DataSource/Connection via CDI/JNDI or directly with host/user/password 
	}
	
	private Connection openConnection() throws SQLException{
		// TODO get a connection for the DataSource
		throw new UnsupportedOperationException("TODO implement");
	}
	
	/**
	 * Checks the database and update if necessary
	 * 
	 * @throws SQLException connection errors
	 * @throws LiquibaseException errors during the liquibase update operation
	 */
	@PostConstruct
	public void checkAndUpdate() throws SQLException, LiquibaseException{
		
		try(	Connection connection = openConnection();
				LiquibaseWrapper lw = new LiquibaseWrapper(connection) )
		{
			lw.update();
		}
	}
	
}
