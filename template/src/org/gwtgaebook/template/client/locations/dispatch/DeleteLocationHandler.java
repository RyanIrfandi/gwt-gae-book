package org.gwtgaebook.template.client.locations.dispatch;

import org.gwtgaebook.template.client.Main;
import org.gwtgaebook.template.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.template.client.util.UrlBuilder;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

//TODO how to have by default CRUD actions & handlers for all entities?
//just overwrite behavior if needed, like DAO
public class DeleteLocationHandler
		extends
		AbstractRequestBuilderClientActionHandler<DeleteLocationAction, DeleteLocationResult> {

	// @Inject
	protected DeleteLocationHandler() {
		super(DeleteLocationAction.class);
	}

	@Override
	protected DeleteLocationResult extractResult(final Response response) {
		return new DeleteLocationResult();
	}

	@Override
	protected RequestBuilder getRequestBuilder(final DeleteLocationAction action) {
		UrlBuilder urlBuilder = new UrlBuilder().setModule("api").setVersion(
				"v1");
		urlBuilder.addResourcePath("theaters", action.getTheaterKey());
		urlBuilder.addResourcePath("locations", action.getLocationKey());

		Main.logger.info(urlBuilder.toUrl());
		RequestBuilder rb = new RequestBuilder(RequestBuilder.DELETE,
				urlBuilder.toUrl());

		return rb;
	}
}
