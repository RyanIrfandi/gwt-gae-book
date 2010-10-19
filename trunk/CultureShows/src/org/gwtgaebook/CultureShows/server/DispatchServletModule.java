package org.gwtgaebook.CultureShows.server;

import com.google.inject.servlet.*;
import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		// filter("*").through(HttpSessionSecurityCookieFilter.class);
		// bindConstant().annotatedWith(SecurityCookie.class).to("MYAPPAUTH");
		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
				DispatchServiceImpl.class);
	}

}
