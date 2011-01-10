package org.gwtgaebook.template.server;

import name.pehl.taoki.GuiceRouter;
import name.pehl.taoki.RestletServlet;

import org.restlet.Context;

import com.google.inject.Injector;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class APIServlet extends RestletServlet {
	@Override
	protected GuiceRouter createRouter(Injector injector, Context context) {
		return new APIRouter(injector, context);
	}
}
