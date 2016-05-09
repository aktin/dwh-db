package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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

	private DataSource ds;
	
	public DatabaseUpdateBean() throws NamingException{
		InitialContext ctx = new InitialContext();
		// also lookup SessionContext via (SessionContext)ctx.lookup("java:comp/EJBContext")

		// TODO where to store/get the configuration for this EJB???
		String dsName = "java:/AktinDS";
		//log.info("Connecting to i2b2 database via "+dsName);
		ds = (DataSource)ctx.lookup(dsName);

		
		// TODO acquire DataSource/Connection via CDI/JNDI or directly with host/user/password 
	}
	
	private Connection openConnection() throws SQLException{
		// TODO get a connection for the DataSource
		return ds.getConnection();
		// throw new UnsupportedOperationException("TODO implement");
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
