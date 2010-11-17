package org.gwtgaebook.CultureShows.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;

public class TheaterMemberJoinDAO extends DAO<TheaterMemberJoin> {

	@Inject
	public TheaterMemberJoinDAO(final ObjectDatastore datastore) {
		super(TheaterMemberJoin.class, datastore);
	}

	public List<Theater> readbyMember(String memberKey) {
		List<Theater> theaters = new ArrayList<Theater>();

		// get theaters member has access to
		List<TheaterMemberJoin> tmjs = datastore.find()
				.type(TheaterMemberJoin.class)
				.addFilter("memberKey", FilterOperator.EQUAL, memberKey)
				.returnAll().now();
		for (int i = 0; i < tmjs.size(); i++) {
			Theater t = new Theater();
			t.theaterKey = tmjs.get(i).theaterKey;
			t.name = tmjs.get(i).theaterName;
			theaters.add(t);
		}

		return theaters;
	}

	public Boolean memberHasAccessToTheater(String memberKey, String theaterKey) {
		List<TheaterMemberJoin> tmjs = datastore.find()
				.type(TheaterMemberJoin.class)
				.addFilter("memberKey", FilterOperator.EQUAL, memberKey)
				.addFilter("theaterKey", FilterOperator.EQUAL, theaterKey)
				.returnAll().now();

		return (0 < tmjs.size());
	}

}
