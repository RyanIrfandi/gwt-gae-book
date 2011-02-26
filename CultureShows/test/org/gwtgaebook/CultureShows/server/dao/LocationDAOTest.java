package org.gwtgaebook.CultureShows.server.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;
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
public class LocationDAOTest extends LocalDatastoreTestCase {

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
	}

	@After
	public void tearDown() {
		theaterDAO.delete(t1Key);

		super.tearDown();
	}

	@Test
	public final void testLocations() {

		l1 = new Location();
		l1.setName(UUID.randomUUID().toString());

		try {
			l1Key = locationDAO.create(l1);
			fail("location created without an associated theater");
		} catch (IllegalArgumentException expected) {
			assertTrue("Exception message doesn't mention " + l1.getName(),
					expected.getMessage().indexOf(l1.getName()) >= 0);
		}
		l1Key = locationDAO.create(t1, l1);

		l2 = locationDAO.read(l1Key);
		assertEquals("created location doesn't match", l1, l2);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		Performance p = new Performance();
		p.date = "2016-01-04";
		p.showKey = KeyFactory.keyToString(s1Key);
		p.locationKey = KeyFactory.keyToString(l1Key);
		performanceDAO.create(t1, p);

		l1.setName(l1.getName() + "updated");
		locationDAO.update(l1, l1Key);
		l2 = locationDAO.read(l1Key);
		assertEquals("updated location doesn't match", l1, l2);

		List<Performance> plist = performanceDAO.readByLocation(KeyFactory
				.keyToString(l1Key));
		assertEquals("performance location wasn't updated", l1.getName(),
				plist.get(0).locationName);

	}

	@Test
	public final void testReferentialIntegrity() {

		l1 = new Location();
		l1.setName(UUID.randomUUID().toString());
		l1Key = locationDAO.create(t1, l1);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		p1 = new Performance();
		p1.date = "2016-01-04";
		p1.showKey = KeyFactory.keyToString(s1Key);
		p1.locationKey = KeyFactory.keyToString(l1Key);

		p1Key = performanceDAO.create(t1, p1);

		try {
			locationDAO.delete(l1Key);
			fail("location with associated performances deleted");
		} catch (IllegalArgumentException expected) {
		}

		l2 = locationDAO.read(l1Key);
		assertEquals(
				"location was deleted although performances with refences to it exist",
				l1, l2);

		performanceDAO.delete(p1Key);

		locationDAO.delete(l1Key);
		l2 = locationDAO.read(l1Key);
		assertEquals("deleted location still exists", null, l2);

	}

}
