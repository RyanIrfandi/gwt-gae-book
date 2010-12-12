package org.gwtgaebook.CultureShows.client.locations;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.CultureShows.client.util.UrlBuilder;

import com.google.gwt.core.client.GWT;
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

		// test
		// LocationModel.Location location = LocationModel.makeLocation();
		// location.setName("Raps");
		// Main.logger.info(LocationModel.serializeToJson(location));
		// LocationModel.Location location2 = LocationModel
		// .deserializeFromJson("{\"locationKey\":\"agxjdWx0dXJlc2hvd3NyHQsSB1RoZWF0ZXIY9AIMCxIITG9jYXRpb24Y_gIM\",\"name\":\"l2\"}");
		// Main.logger.info(location2.getName());

		Main.logger.info(response.getText());
		MyAutoBeanUtils<LocationModel.Locations> beanUtils = new MyAutoBeanUtils<LocationModel.Locations>(
				LocationModel.factory, LocationModel.Locations.class);

		LocationModel.Locations locations = beanUtils
				.deserializeFromJson(response.getText());
		//

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
