package org.gwtgaebook.CultureShows.server.dao;

import java.util.List;

import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Theater;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;

public class MemberDAO extends DAO<Member> {

	@Inject
	public MemberDAO(final ObjectDatastore datastore) {
		super(Member.class, datastore);
	}

	public Member readByUserId(String userId) {
		List<Member> members = datastore.find().type(Member.class)
				.addFilter("userId", FilterOperator.EQUAL, userId).returnAll()
				.now();
		if (0 == members.size()) {
			return null;
		}

		if (1 < members.size()) {
			logger.severe("There is more than one entity for Member with id="
					+ userId);
		}

		return members.get(0);
	}

}
