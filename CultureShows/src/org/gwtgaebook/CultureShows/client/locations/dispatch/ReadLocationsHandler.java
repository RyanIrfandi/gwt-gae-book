package org.gwtgaebook.CultureShows.client.locations.dispatch;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.CultureShows.client.locations.model.Locations;
import org.gwtgaebook.CultureShows.client.util.UrlBuilder;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

public class ReadLocationsHandler
		extends
		AbstractRequestBuilderClientActionHandler<ReadLocationsAction, ReadLocationsResult> {

	// @Inject
	protected ReadLocationsHandler() {
		super(ReadLocationsAction.class);
	}

	@Override
	protected ReadLocationsResult extractResult(final Response response) {
		Main.logger.info(response.getText());
		Locations locations = Locations.JSON.read(response.getText());

		return new ReadLocationsResult(locations);
	}

	@Override
	protected RequestBuilder getRequestBuilder(final ReadLocationsAction action) {
		UrlBuilder urlBuilder = new UrlBuilder().setModule("api").setVersion(
				"v1");
		urlBuilder.addResourcePath("theaters", action.getTheaterKey());
		urlBuilder.addResourcePath("locations");

		Main.logger.info(urlBuilder.toUrl());
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
				urlBuilder.toUrl());
		return rb;
	}

}
