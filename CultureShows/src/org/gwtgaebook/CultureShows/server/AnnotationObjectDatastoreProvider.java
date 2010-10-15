package org.gwtgaebook.CultureShows.server;

import com.google.inject.*;
import com.google.code.twig.*;
import com.google.code.twig.annotation.*;

public class AnnotationObjectDatastoreProvider implements
		Provider<ObjectDatastore> {

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
