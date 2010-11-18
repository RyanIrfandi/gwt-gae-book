package org.gwtgaebook.CultureShows.server;

import name.pehl.taoki.security.SecurityCookieFilter;

import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.DispatchServiceImpl;
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
	}

}
