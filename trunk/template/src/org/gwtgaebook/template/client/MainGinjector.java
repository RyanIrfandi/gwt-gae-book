package org.gwtgaebook.template.client;

import org.gwtgaebook.template.client.landing.LandingModule;
import org.gwtgaebook.template.client.landing.LandingPresenter;
import org.gwtgaebook.template.client.resources.Resources;
import org.gwtgaebook.template.client.resources.Translations;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({ DispatchAsyncModule.class, MainModule.class, LandingModule.class })
public interface MainGinjector extends Ginjector {
	EventBus getEventBus();

	PlaceManager getPlaceManager();

	ProxyFailureHandler getProxyFailureHandler();

	Resources getResources();

	Translations getTranslations();

	Provider<LandingPresenter> getLandingPresenter();

}