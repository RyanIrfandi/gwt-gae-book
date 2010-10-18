package org.gwtgaebook.CultureShows.client;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.gin.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.CultureShows.client.resources.*;
import org.gwtgaebook.CultureShows.client.landing.*;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

public class MainModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(Resources.class).in(Singleton.class);
		bind(Translations.class).in(Singleton.class);
		bind(UserInfo.class).in(Singleton.class);
		// bind(SignedInGatekeeper.class).in(Singleton.class);

		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(MainPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(
				Singleton.class);
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class)
				.in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();

	}
}