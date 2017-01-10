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

import liquibase.exception.LiquibaseException;


public class TestDataSource implements DataSource{
	private static final Logger log = Logger.getLogger(TestDataSource.class.getName());
	private PrintWriter logWriter;
//	private int version;
	
	public TestDataSource() throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		try( Connection dbc = getVanillaConnection() ){
			// TODO use version
			TestDatabase.initializeDatabase(dbc);
		}
		// TODO Auto-generated constructor stub
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
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

	private static Connection getVanillaConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:hsqldb:file:target/testdb;shutdown=true", "sa", "");		
	}
	@Override
	public Connection getConnection() throws SQLException {
		log.info("Opening connection..");
		Connection dbc = getVanillaConnection();
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
		log.info("done");
		return dbc;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

	public void dropAll() throws LiquibaseException, SQLException{
		try( Connection dbc = getVanillaConnection() ){
			LiquibaseWrapper w = new LiquibaseWrapper(dbc);
			w.getLiquibase().dropAll();
			w.close();
		}
		
	}
}
