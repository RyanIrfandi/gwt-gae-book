package org.gwtgaebook.CultureShows.server;

import com.google.inject.*;
import com.google.inject.servlet.*;

public class MainGuiceServletContextListener extends
		GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new MainHandlerModule(),
				new DispatchServletModule());
	}
}
