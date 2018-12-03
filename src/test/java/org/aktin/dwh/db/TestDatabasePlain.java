/**
 * 
 */
package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Verifies that the liquibase changelog file
 * can be loaded.
 * <p>
 * For external tests against the database,
 * a temporary database connection can be obtained
 * via {@link #createTestConnection()}
 *  
 * @author R.W.Majeed
 *
 */
public class TestDatabasePlain {
	public static final String DEFAULT_ID = "aktin_test";
	private String id;

	public TestDatabasePlain() {
		this(DEFAULT_ID);
	}
	public TestDatabasePlain(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * Create in-memory database containing the AKTIN tables
	 * and default data.
	 * <p>
	 * The database will be closed when {@link Connection#close()} 
	 * is called.
	 * 
	 * @return connection to in-memory database 
	 * @throws SQLException 
	 */
	public Connection createTestConnection() throws SQLException{
		Connection dbc = createDatabase();
		initializeDatabase(dbc);
		return dbc;
	}

	public void initializeDatabase(Connection dbc) throws SQLException{
	}
	
	protected Connection createDatabase() throws SQLException{
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		return DriverManager.getConnection("jdbc:hsqldb:mem:"+id+";shutdown=true", "sa", "");
	}
		

}
