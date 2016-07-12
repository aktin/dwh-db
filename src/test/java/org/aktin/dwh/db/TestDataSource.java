package org.aktin.dwh.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;


public class TestDataSource implements DataSource{
	private static final Logger log = Logger.getLogger(TestDataSource.class.getName());
	private PrintWriter logWriter;
	private int version;
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}

	public void setVersion(int version){
		this.version = version;
		// TODO use this version for liquibase initialisation
	}
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
//		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		log.info("Opening connection..");
		Connection dbc = DriverManager.getConnection("jdbc:hsqldb:file:target/testdb;shutdown=true", "sa", "");
		// check if database exists
		// TODO why is this block not executed if the database was created.
		// XXX probably logging is switched to different logging provider
		try( Statement s = dbc.createStatement() ){
			log.info("Reading schemata");
			s.execute("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA");// WHERE SCHEMA_NAME = '<SCHEMA NAME>'
			try( ResultSet rs = s.getResultSet() ){
				int count = 0;
				while( rs.next() ){
					count ++;
					log.info("Schema: "+rs.getString(1));
				}
				if( count == 0 ){
					log.info("No existing schemata");
				}
			}
			s.close();
		}
		// TODO use version
		TestDatabase.initializeDatabase(dbc);
		log.info("done");
		return dbc;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

}
