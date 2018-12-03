package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;


import liquibase.exception.LiquibaseException;


public class TestDataSource extends TestDataSourcePlain{
	private static final Logger log = Logger.getLogger(TestDataSource.class.getName());
	
	public TestDataSource(TestDatabasePlain db) throws SQLException {
		super(db);
	}

	public TestDataSource() throws SQLException {
		this(new TestDatabase());
	}

	@Override
	public Connection getConnection() throws SQLException {
		log.info("Opening connection..");
		Connection dbc = super.getConnection();
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
	public void dropAll() throws  SQLException{
		try( Connection dbc = getConnection() ){
			LiquibaseWrapper w = new LiquibaseWrapper(dbc);
			w.getLiquibase().dropAll();
			w.close();
		} catch (LiquibaseException e) {
			throw new SQLException(e);
		}
		
	}
}
