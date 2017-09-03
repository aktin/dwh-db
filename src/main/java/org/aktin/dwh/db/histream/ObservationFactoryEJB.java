package org.aktin.dwh.db.histream;

import java.io.Flushable;
import java.time.ZoneId;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.aktin.Preferences;

import de.sekmi.histream.ObservationFactory;
import de.sekmi.histream.i2b2.DataDialect;
import de.sekmi.histream.i2b2.PostgresPatientStore;
import de.sekmi.histream.i2b2.PostgresVisitStore;
import de.sekmi.histream.impl.ObservationFactoryImpl;

@Singleton
public class ObservationFactoryEJB extends ObservationFactoryImpl implements ObservationFactory, Flushable {

	private PostgresPatientStore patientStore;
	private PostgresVisitStore visitStore;

	@Inject
	public ObservationFactoryEJB(PostgresPatientStore patientStore, PostgresVisitStore visitStore){
		super(patientStore, visitStore);
		this.patientStore = patientStore;
		this.visitStore = visitStore;
	}

	@Override
	public void flush(){
		patientStore.flush();
		visitStore.flush();
	}

	public static DataDialect createDialect(Preferences prefs){
		DataDialect dd = new DataDialect();
		// set timezone if configuration available
		String tz = prefs.get("i2b2.db.tz"); // TODO use PreferenceKey enum
		if( tz != null ){
			dd.setTimeZone(ZoneId.of(tz));
		}
		return dd;
	}
}
