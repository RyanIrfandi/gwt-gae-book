package org.gwtgaebook.CultureShows.server.api;

import java.io.IOException;
import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.server.util.Validation;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowResult;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class LocationResource extends ServerResource {

	@Inject
	Gson gson;
	@Inject
	LocationDAO locationDAO;
	@Inject
	MemberDAO memberDAO;
	@Inject
	TheaterMemberJoinDAO tmjDAO;
	@Inject
	TheaterDAO theaterDAO;
	@Inject
	Provider<UserInfo> userInfoProvider;

	public class LocationGET {
		@Expose
		Location location;
	}

	@Get("json")
	public Representation get() {
		LocationGET get = new LocationGET();
		// TODO check if user has right to...
		get.location = locationDAO.read((String) getRequestAttributes().get(
				"locationId"));
		JsonRepresentation representation = new JsonRepresentation(
				gson.toJson(get));

		return representation;
	}

	@Delete
	public Representation delete() {
		// TODO have a kind of ActionValidator/Gatekeeper instead of manual
		// checks
		// http://wiki.restlet.org/docs_2.1/13-restlet/27-restlet/46-restlet.html
		UserInfo userInfo = userInfoProvider.get();
		if (!userInfo.isSignedIn) {
			// TODO 401
			return null;
		}
		Member member = memberDAO.readByUserId(userInfo.userId);
		Key theaterKey = Validation
				.getValidDSKey((String) getRequestAttributes()
						.get("theaterKey"));

		if (!tmjDAO.memberHasAccessToTheater(
				KeyFactory.keyToString(memberDAO.getKey(member)),
				KeyFactory.keyToString(theaterKey))) {
			// TODO 401
			return null;
		}

		Key locationKey = Validation
				.getValidDSKey((String) getRequestAttributes().get(
						"locationKey"));

		// check location belongs to given theaterKey
		if (!KeyFactory.keyToString(locationKey.getParent()).equals(
				KeyFactory.keyToString(theaterKey))) {
			// TODO 401
			return null;
		}
		locationDAO.delete(locationKey);

		return null;
	}

}
