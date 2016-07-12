package org.aktin.dwh.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Manager {

	public static final Connection openConnection() throws SQLException, NamingException{
		InitialContext ctx = new InitialContext();
		//log.info("Connecting to i2b2 database via "+dsName);
		DataSource ds = (DataSource)ctx.lookup(dataSourceName());
		// open database connection
		return ds.getConnection();		
	}
	
	public static final String dataSourceName(){
		String dsName = "java:/AktinDS";
		try {
			InitialContext ctx = new InitialContext();
			ctx.lookup(dsName);
		} catch (NamingException e) {
			// TODO log info that the name was not found
			dsName = "java:comp/env/jdbc/AktinDS";
		}
		// if running under jetty, use other name: "java:comp/env/jdbc/DSTest"
		return dsName;
	}
}
