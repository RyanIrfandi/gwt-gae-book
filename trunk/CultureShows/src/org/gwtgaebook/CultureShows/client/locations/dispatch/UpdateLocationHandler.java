package org.gwtgaebook.CultureShows.client.locations.dispatch;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.CultureShows.client.locations.model.Location;
import org.gwtgaebook.CultureShows.client.util.UrlBuilder;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

//TODO how to have by default CRUD actions & handlers for all entities?
//just overwrite behavior if needed, like DAO
public class UpdateLocationHandler
		extends
		AbstractRequestBuilderClientActionHandler<UpdateLocationAction, UpdateLocationResult> {

	// @Inject
	protected UpdateLocationHandler() {
		super(UpdateLocationAction.class);
	}

	@Override
	protected UpdateLocationResult extractResult(final Response response) {
		Main.logger.info(response.getText());
		// TODO parse error handling
		Location location = Location.jsonr.read(response.getText());

		return new UpdateLocationResult(location);
	}

	@Override
	protected RequestBuilder getRequestBuilder(final UpdateLocationAction action) {
		UrlBuilder urlBuilder = new UrlBuilder().setModule("api").setVersion(
				"v1");
		urlBuilder.addResourcePath("theaters", action.getTheaterKey());
		urlBuilder.addResourcePath("locations",
				action.getLocationIn().locationKey);

		Main.logger.info(urlBuilder.toUrl());
		RequestBuilder rb = new RequestBuilder(RequestBuilder.PUT,
				urlBuilder.toUrl());

		rb.setRequestData(Location.jsonw.toJson(action.locationIn));

		return rb;
	}
}
