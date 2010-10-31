package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.resources.Resources;
import org.gwtgaebook.CultureShows.client.resources.Translations;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultEventBus;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class MainModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(Resources.class).in(Singleton.class);
		bind(Translations.class).in(Singleton.class);
		bind(ClientState.class).in(Singleton.class);
		// bind(SignedInGatekeeper.class).in(Singleton.class);

		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(MainPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(
				Singleton.class);
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class)
				.in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

	}
}