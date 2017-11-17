package org.aktin.dwh.db.histream;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.aktin.Preferences;
import org.aktin.dwh.PreferenceKey;

import de.sekmi.histream.i2b2.DataDialect;
import de.sekmi.histream.i2b2.PostgresPatientStore;


@Singleton
public class PatientStoreEJB extends PostgresPatientStore{
	private static final Logger log = Logger.getLogger(PatientStoreEJB.class.getName());

	@Inject
	public PatientStoreEJB(Preferences prefs) throws SQLException, NamingException {
		String i2b2Project = prefs.get(PreferenceKey.i2b2Project);
		log.info("Using i2b2 project: "+i2b2Project);

		String jndiDS = prefs.get(PreferenceKey.i2b2DatasourceCRC);
		log.info("Using datasource: "+jndiDS);
		Objects.requireNonNull(jndiDS);
		
		// locate data source
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup(jndiDS);
		Objects.requireNonNull(ds, "datasource not found: "+jndiDS);
		Objects.requireNonNull(i2b2Project, "non-null i2b2 project id required");
		DataDialect dd = ObservationFactoryEJB.createDialect(prefs);
		this.open(ds.getConnection(), i2b2Project, dd);
	}

	public PatientStoreEJB(DataSource ds, String projectId) throws SQLException{
		open(ds.getConnection(), projectId, new DataDialect());
	}
	
	@PreDestroy
	@Override
	public void close(){
		try {
			super.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
