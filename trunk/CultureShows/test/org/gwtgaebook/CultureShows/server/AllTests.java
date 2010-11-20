package org.gwtgaebook.CultureShows.server;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gwtgaebook.CultureShows.server.dao.LocationDAOTest;
import org.gwtgaebook.CultureShows.server.dao.PerformanceDAOTest;
import org.gwtgaebook.CultureShows.server.dao.ShowDAOTest;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAOTest;
import org.gwtgaebook.CultureShows.server.util.ValidationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ValidationTest.class, TheaterDAOTest.class,
		ShowDAOTest.class, LocationDAOTest.class, PerformanceDAOTest.class })
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		// // datastore config
		// DefaultConfiguration.registerTypeName(Member.class, "Member");
		// DefaultConfiguration.registerTypeName(Theater.class, "Theater");
		// DefaultConfiguration.registerTypeName(TheaterMemberJoin.class,
		// "TheaterMemberJoin");
		// DefaultConfiguration.registerTypeName(Show.class, "Show");
		// DefaultConfiguration.registerTypeName(Location.class, "Location");
		// DefaultConfiguration.registerTypeName(Performance.class,
		// "Performance");

		// $JUnit-BEGIN$

		// $JUnit-END$
		return suite;
	}

}
