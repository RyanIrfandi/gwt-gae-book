package org.gwtgaebook.template.server.dao;

import org.gwtgaebook.template.server.model.Theater;

import com.google.appengine.api.datastore.Key;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;

public class TheaterDAO extends DAO<Theater> {

	@Inject
	public TheaterDAO(final ObjectDatastore datastore) {
		super(Theater.class, datastore);
	}

	@Override
	public void delete(Key key) {
		// TODO need to check associated entities, such as shows. Will do later
		// when needed
		super.delete(key);
	}

}
