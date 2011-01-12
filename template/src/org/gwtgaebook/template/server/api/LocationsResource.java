package org.gwtgaebook.template.server.api;

import java.io.IOException;
import java.util.List;

import org.gwtgaebook.template.server.dao.LocationDAO;
import org.gwtgaebook.template.server.dao.MemberDAO;
import org.gwtgaebook.template.server.dao.TheaterDAO;
import org.gwtgaebook.template.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.template.server.model.Location;
import org.gwtgaebook.template.server.model.Member;
import org.gwtgaebook.template.server.model.Theater;
import org.gwtgaebook.template.server.model.UserInfo;
import org.gwtgaebook.template.server.util.Validation;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
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

public class LocationsResource extends ServerResource {

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

	public class Locations {
		@Expose
		List<Location> locations;
	}

	@Get("json")
	public Representation get() {
		// TODO check if user has right to...
		Locations get = new Locations();
		get.locations = locationDAO
				.readByTheater((String) getRequestAttributes()
						.get("theaterKey"));

		return new JsonRepresentation(gson.toJson(get));
	}

	@Post
	public Representation post(Representation entity) {
		// TODO have a kind of ActionValidator/Gatekeeper instead of manual
		// checks
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

		Location location = null;

		try {
			location = gson.fromJson(entity.getText(), Location.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Theater theater = theaterDAO.read(theaterKey);
		Key locationKey = locationDAO.create(theater, location);

		// TODO return location

		return null;
	}

}
