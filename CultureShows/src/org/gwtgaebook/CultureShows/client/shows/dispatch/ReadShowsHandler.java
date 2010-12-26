package org.gwtgaebook.CultureShows.client.shows.dispatch;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.dispatch.AbstractRequestBuilderClientActionHandler;
import org.gwtgaebook.CultureShows.client.shows.model.Shows;
import org.gwtgaebook.CultureShows.client.util.UrlBuilder;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

public class ReadShowsHandler
		extends
		AbstractRequestBuilderClientActionHandler<ReadShowsAction, ReadShowsResult> {

	// @Inject
	protected ReadShowsHandler() {
		super(ReadShowsAction.class);
	}

	@Override
	protected ReadShowsResult extractResult(final Response response) {
		Main.logger.info(response.getText());
		Shows shows = Shows.JSON.read(response.getText());

		return new ReadShowsResult(shows);
	}

	@Override
	protected RequestBuilder getRequestBuilder(final ReadShowsAction action) {
		UrlBuilder urlBuilder = new UrlBuilder().setModule("api").setVersion(
				"v1");
		urlBuilder.addResourcePath("theaters", action.getTheaterKey());
		urlBuilder.addResourcePath("shows");

		Main.logger.info(urlBuilder.toUrl());
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
				urlBuilder.toUrl());
		return rb;
	}

}
