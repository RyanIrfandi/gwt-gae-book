package org.gwtgaebook.CultureShows.server.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;

import org.gwtgaebook.CultureShows.server.AnnotationObjectDatastoreProvider;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.gwtplatform.tester.mockito.AutomockingModule;
import com.gwtplatform.tester.mockito.GuiceMockitoJUnitRunner;
import com.gwtplatform.tester.mockito.TestScope;

@RunWith(GuiceMockitoJUnitRunner.class)
public class PerformanceDAOTest extends LocalDatastoreTestCase {

	public static class Module extends AutomockingModule {
		@Override
		protected void configureTest() {
			bind(ObjectDatastore.class).toProvider(
					AnnotationObjectDatastoreProvider.class).in(
					TestScope.SINGLETON);
			bind(TheaterDAO.class);
			bind(ShowDAO.class);
			bind(LocationDAO.class);
			bind(PerformanceDAO.class);
		}
	}

	@Inject
	TheaterDAO theaterDAO;
	@Inject
	ShowDAO showDAO;
	@Inject
	LocationDAO locationDAO;
	@Inject
	PerformanceDAO performanceDAO;

	Theater t1, t2;
	Key t1Key, t2Key;
	Show s1, s2;
	Key s1Key, s2Key;
	Location l1, l2;
	Key l1Key, l2Key;
	Performance p1, p2;
	Key p1Key, p2Key;

	@Before
	public void setUp() {
		super.setUp();

		t1 = new Theater();
		t1.name = UUID.randomUUID().toString();
		t1Key = theaterDAO.create(t1);

		s1 = new Show();
		s1.setName(UUID.randomUUID().toString());
		s1Key = showDAO.create(t1, s1);

		s2 = new Show();
		s2.setName(UUID.randomUUID().toString());
		s2Key = showDAO.create(t1, s2);

		l1 = new Location();
		l1.setName(UUID.randomUUID().toString());
		l1Key = locationDAO.create(t1, l1);

	}

	@After
	public void tearDown() {
		theaterDAO.delete(t1Key);
		showDAO.delete(s1Key);
		locationDAO.delete(l1Key);

		super.tearDown();
	}

	@Test
	public final void testCreateUpdate() {

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		p1 = new Performance();
		p1.date = date;
		p1.showKey = KeyFactory.keyToString(s1Key);
		p1.locationKey = KeyFactory.keyToString(l1Key);

		p1Key = performanceDAO.create(t1, p1);

		p2 = performanceDAO.read(p1Key);
		assertEquals("created performance date doesn't match", p1.date, p2.date);
		assertEquals("created performance show key doesn't match", p1.showKey,
				p2.showKey);
		assertEquals("created performance show name doesn't match",
				s1.getName(), p2.showName);
		assertEquals("created performance location key doesn't match",
				p1.locationKey, p2.locationKey);
		assertEquals("created performance location name doesn't match",
				l1.getName(), p2.locationName);

		p1.showKey = KeyFactory.keyToString(s2Key);
		performanceDAO.update(p1, p1Key);
		p2 = performanceDAO.read(p1Key);

		assertEquals("updated performance show key doesn't match", p1.showKey,
				p2.showKey);
		assertEquals("updated performance show name doesn't match",
				s2.getName(), p2.showName);

		performanceDAO.delete(p1Key);
	}

}
