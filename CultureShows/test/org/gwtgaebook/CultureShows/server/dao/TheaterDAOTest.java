package org.gwtgaebook.CultureShows.server.dao;

import static org.junit.Assert.*;

import java.util.UUID;

import org.gwtgaebook.CultureShows.server.AnnotationObjectDatastoreProvider;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.appengine.api.datastore.Key;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.tester.mockito.AutomockingModule;
import com.gwtplatform.tester.mockito.GuiceMockitoJUnitRunner;
import com.gwtplatform.tester.mockito.TestScope;

@RunWith(GuiceMockitoJUnitRunner.class)
public class TheaterDAOTest extends LocalDatastoreTestCase {

	public static class Module extends AutomockingModule {
		@Override
		protected void configureTest() {
			bind(ObjectDatastore.class).toProvider(
					AnnotationObjectDatastoreProvider.class).in(
					TestScope.SINGLETON);
			bind(TheaterDAO.class);
		}
	}

	// @Inject
	// Provider<TheaterDAO> theaterDAOProvider;

	@Inject
	TheaterDAO theaterDAO;

	Theater t1, t2;
	Key t1Key, t2Key;

	@Before
	public void setUp() {
		super.setUp();
		// theaterDAO = theaterDAOProvider.get();
	}

	@After
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public final void testTheaters() {
		t1 = new Theater();
		t1.name = UUID.randomUUID().toString();
		t1Key = theaterDAO.create(t1);
		t2 = theaterDAO.read(t1Key);
		assertEquals("created theater doesn't match", t1, t2);

		t1.name = t1.name + "updated";
		theaterDAO.update(t1, t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("updated theater doesn't match", t1, t2);

		theaterDAO.delete(t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("deleted theater still exists", null, t2);

		t1 = new Theater();
		t1Key = theaterDAO.create(t1);
		theaterDAO.delete(t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("deleted theater still exists", null, t2);

	}
}
