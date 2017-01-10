package org.aktin.dwh.db;

import java.sql.Connection;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ResourceAccessor;

/**
 * Wraps the liquibase API with operations
 * that are supported for the AKTIN database.
 * 
 * @author R.W.Majeed
 *
 */
public class LiquibaseWrapper implements AutoCloseable {
	private Database database;
	private Liquibase liquibase;
	static final String CHANGELOG_RESOURCE = "/database.xml";

	/**
	 * Construct a liquibase wrapper for sql connection.
	 * Closing the wrapper will not call {@link Connection#close()}.
	 * @param connection
	 * @throws LiquibaseException
	 */
	public LiquibaseWrapper(Connection connection) throws LiquibaseException{
		database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		ResourceAccessor ra = new ClassResourceAccessor(LiquibaseWrapper.class);
		liquibase = new Liquibase(CHANGELOG_RESOURCE, ra, database);
	}

	/**
	 * Perform liquibase update operation
	 * @see Liquibase#update(Contexts, LabelExpression)
	 * @throws LiquibaseException liquibase error
	 */
	public void update() throws LiquibaseException{
		liquibase.update(new Contexts(), new LabelExpression());
	}

	public Liquibase getLiquibase(){
		return liquibase;
	}

	@Override
	public void close() throws DatabaseException{
		database.close();
	}
}
