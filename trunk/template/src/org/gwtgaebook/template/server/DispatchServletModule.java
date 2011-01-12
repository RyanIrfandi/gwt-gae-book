package org.gwtgaebook.template.server;

import java.util.TimeZone;

import com.google.inject.servlet.ServletModule;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		// serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
		// DispatchServiceImpl.class);
		serve("/api/v1/*").with(APIServlet.class);

		TimeZone.setDefault(TimeZone.getTimeZone(Constants.serverTimeZone));
	}

}
