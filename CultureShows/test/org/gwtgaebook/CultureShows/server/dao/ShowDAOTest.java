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
import com.google.inject.Provider;
import com.gwtplatform.tester.mockito.AutomockingModule;
import com.gwtplatform.tester.mockito.GuiceMockitoJUnitRunner;
import com.gwtplatform.tester.mockito.TestScope;

@RunWith(GuiceMockitoJUnitRunner.class)
public class ShowDAOTest extends LocalDatastoreTestCase {

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

		l1 = new Location();
		l1.setName(UUID.randomUUID().toString());
		l1Key = locationDAO.create(t1, l1);

	}

	@After
	public void tearDown() {
		theaterDAO.delete(t1Key);

		super.tearDown();
	}

	@Test
	public final void testShows() {

		s1 = new Show();
		s1.setName(UUID.randomUUID().toString());

		try {
			s1Key = showDAO.create(s1);
			fail("show created without an associated theater");
		} catch (IllegalArgumentException expected) {
			assertTrue("Exception message doesn't mention " + s1.getName(),
					expected.getMessage().indexOf(s1.getName()) >= 0);
		}
		s1Key = showDAO.create(t1, s1);

		s2 = showDAO.read(s1Key);
		assertEquals("created show doesn't match", s1, s2);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		Performance p = new Performance();
		p.date = date;
		p.showKey = KeyFactory.keyToString(s1Key);
		p.locationKey = KeyFactory.keyToString(l1Key);
		performanceDAO.create(t1, p);

		s1.setName(s1.getName() + "updated");
		showDAO.update(s1, s1Key);
		s2 = showDAO.read(s1Key);
		assertEquals("updated show doesn't match", s1, s2);

		List<Performance> plist = performanceDAO.readByShow(KeyFactory
				.keyToString(s1Key));
		assertEquals("performance show wasn't updated", s1.getName(),
				plist.get(0).showName);

	}

	@Test
	public final void testReferentialIntegrity() {

		s1 = new Show();
		s1.setName(UUID.randomUUID().toString());
		s1Key = showDAO.create(t1, s1);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		p1 = new Performance();
		p1.date = date;
		p1.showKey = KeyFactory.keyToString(s1Key);
		p1.locationKey = KeyFactory.keyToString(l1Key);

		p1Key = performanceDAO.create(t1, p1);

		try {
			showDAO.delete(s1Key);
			fail("show with associated performances deleted");
		} catch (IllegalArgumentException expected) {
		}

		s2 = showDAO.read(s1Key);
		assertEquals(
				"show was deleted although performances with refences to it exist",
				s1, s2);

		performanceDAO.delete(p1Key);

		showDAO.delete(s1Key);
		s2 = showDAO.read(s1Key);
		assertEquals("deleted show still exists", null, s2);

	}

}
