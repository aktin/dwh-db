package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Manager {

	public static final Connection openConnection() throws SQLException, NamingException{
		InitialContext ctx = new InitialContext();
		String dsName = "java:/AktinDS";
		//log.info("Connecting to i2b2 database via "+dsName);
		DataSource ds = (DataSource)ctx.lookup(dsName);
		// TODO open database connection
		return ds.getConnection();		
	}
}
