package org.gwtgaebook.template.client.locations.dispatch;

import org.gwtgaebook.template.client.Main;
import org.gwtgaebook.template.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.template.client.locations.model.Location;
import org.gwtgaebook.template.client.util.UrlBuilder;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

//TODO how to have by default CRUD actions & handlers for all entities?
//just overwrite behavior if needed, like DAO
public class CreateLocationHandler
		extends
		AbstractRequestBuilderClientActionHandler<CreateLocationAction, CreateLocationResult> {

	// @Inject
	protected CreateLocationHandler() {
		super(CreateLocationAction.class);
	}

	@Override
	protected CreateLocationResult extractResult(final Response response) {
		Main.logger.info(response.getText());
		// TODO parse error handling
		Location location = Location.jsonr.read(response.getText());

		return new CreateLocationResult(location);
	}

	@Override
	protected RequestBuilder getRequestBuilder(final CreateLocationAction action) {
		UrlBuilder urlBuilder = new UrlBuilder().setModule("api").setVersion(
				"v1");
		urlBuilder.addResourcePath("theaters", action.getTheaterKey());
		urlBuilder.addResourcePath("locations");

		Main.logger.info(urlBuilder.toUrl());
		RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,
				urlBuilder.toUrl());

		rb.setRequestData(Location.jsonw.toJson(action.locationIn));

		return rb;
	}
}
