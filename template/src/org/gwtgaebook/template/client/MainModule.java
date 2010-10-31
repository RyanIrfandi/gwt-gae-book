package org.gwtgaebook.template.client;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.gin.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.template.client.resources.*;
import org.gwtgaebook.template.client.landing.*;

public class MainModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(Resources.class).in(Singleton.class);
		bind(Translations.class).in(Singleton.class);

		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(MainPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(
				Singleton.class);
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class)
				.in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();
		// bind(LoggedInGatekeeper.class).in(Singleton.class);

	}
}