package org.gwtgaebook.CultureShows.server;

import java.util.TimeZone;

import org.gwtgaebook.CultureShows.shared.Constants;

import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.shared.ActionImpl;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		// filter("*").through(SecurityCookieFilter.class);
		// filter("*").through(HttpSessionSecurityCookieFilter.class);
		// bindConstant().annotatedWith(SecurityCookie.class).to("MYAPPAUTH");
		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
				DispatchServiceImpl.class);
		serve("/api/v1/*").with(APIServlet.class);

		// store all dates in GMT

		// before store, convert from signed in client TZ to GMT
		// after reading, convert from GMT to signed in client TZ
		// for anonymous clients, return GMT + TZ info
		// OR always work with GMT server side, client should handle translation
		// before sending/after receiving
		TimeZone.setDefault(TimeZone.getTimeZone(Constants.serverTimeZone));
	}

}
