package org.gwtgaebook.CultureShows.server;

import com.google.inject.servlet.*;
import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		// filter("*").through(HttpSessionSecurityCookieFilter.class);
		// bindConstant().annotatedWith(SecurityCookie.class).to("MYAPPAUTH");
		// TODO If you use GWT 2.1M3, they changed a little bit the path of RPC
		// request, now myApp must be removed from the serve path
		serve("/main/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
				DispatchServiceImpl.class);
	}

}
