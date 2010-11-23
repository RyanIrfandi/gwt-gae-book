package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.guide.GuideModule;
import org.gwtgaebook.CultureShows.client.guide.GuidePresenter;
import org.gwtgaebook.CultureShows.client.landing.LandingModule;
import org.gwtgaebook.CultureShows.client.landing.LandingPresenter;
import org.gwtgaebook.CultureShows.client.resources.Resources;
import org.gwtgaebook.CultureShows.client.resources.Translations;
import org.gwtgaebook.CultureShows.client.shows.ShowModule;
import org.gwtgaebook.CultureShows.client.shows.ShowPresenter;
import org.gwtgaebook.CultureShows.client.widget.WidgetModule;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({ DispatchAsyncModule.class, MainModule.class, WidgetModule.class,
		LandingModule.class, ShowModule.class, GuideModule.class })
public interface MainGinjector extends Ginjector {
	EventBus getEventBus();

	PlaceManager getPlaceManager();

	ProxyFailureHandler getProxyFailureHandler();

	Resources getResources();

	Translations getTranslations();

	SignedInGatekeeper getSignedInGatekeeper();

	Provider<MainPresenter> getMainPresenter();

	Provider<LandingPresenter> getLandingPresenter();

	AsyncProvider<GuidePresenter> getGuidePresenter();

	AsyncProvider<ShowPresenter> getShowPresenter();

}