package org.aktin.dwh.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;


public class TestDataSourcePlain implements DataSource{
	private static final Logger log = Logger.getLogger(TestDataSourcePlain.class.getName());
	private PrintWriter logWriter;
	TestDatabasePlain db;
//	private int version;
	
	public TestDataSourcePlain(TestDatabasePlain db) throws SQLException {
		this.db = db;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		try( Connection dbc = getConnection() ){
			// TODO use version
			db.initializeDatabase(dbc);
		}
		// TODO Auto-generated constructor stub
	}

	public TestDataSourcePlain() throws SQLException {
		this(new TestDatabase());
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

	@Override
	public Connection getConnection() throws SQLException {
		log.info("Opening connection..");
		Connection dbc = DriverManager.getConnection("jdbc:hsqldb:file:target/"+db.getId()+";shutdown=true", "sa", "");		

		log.info("done");
		return dbc;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

	public void dropAll() throws SQLException{
	}
}
