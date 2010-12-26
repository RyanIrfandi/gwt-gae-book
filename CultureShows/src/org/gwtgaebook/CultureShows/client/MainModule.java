package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.resources.Resources;
import org.gwtgaebook.CultureShows.client.resources.Translations;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
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

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(MainPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(
				Singleton.class);
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class)
				.in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		// TODO enhance gwtp to remove the need for manual handler registration
		install(new DispatchAsyncModule.Builder().clientActionHandlerRegistry(
				RESTHandlerRegistry.class).build());

	}
}