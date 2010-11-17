package org.gwtgaebook.CultureShows.server;

import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin;

import com.google.inject.*;
import com.google.code.twig.*;
import com.google.code.twig.annotation.*;
import com.google.code.twig.configuration.DefaultConfiguration;

public class AnnotationObjectDatastoreProvider implements
		Provider<ObjectDatastore> {

	static {
		// datastore config
		DefaultConfiguration.registerTypeName(Member.class, "Member");
		DefaultConfiguration.registerTypeName(Theater.class, "Theater");
		DefaultConfiguration.registerTypeName(TheaterMemberJoin.class,
				"TheaterMemberJoin");
		DefaultConfiguration.registerTypeName(Show.class, "Show");
		DefaultConfiguration.registerTypeName(Location.class, "Location");
		DefaultConfiguration.registerTypeName(Performance.class, "Performance");
	}

	// don't index fields by default
	private Boolean indexed = false;

	// load just the first instances and their fields
	private int activationDepth = 2;

	public ObjectDatastore get() {
		ObjectDatastore datastore = new AnnotationObjectDatastore(indexed);

		datastore.setActivationDepth(activationDepth);

		return datastore;
	}
}
