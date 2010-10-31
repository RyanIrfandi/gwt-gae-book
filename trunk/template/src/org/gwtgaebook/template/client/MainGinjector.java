package org.gwtgaebook.template.client;

import com.google.inject.*;
import com.google.gwt.inject.client.*;
import com.gwtplatform.dispatch.client.gin.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.template.client.resources.*;
import org.gwtgaebook.template.client.landing.*;

@GinModules( { DispatchAsyncModule.class, MainModule.class, LandingModule.class })
public interface MainGinjector extends Ginjector {
	EventBus getEventBus();

	PlaceManager getPlaceManager();

	ProxyFailureHandler getProxyFailureHandler();

	Resources getResources();

	Translations getTranslations();

	Provider<LandingPresenter> getLandingPresenter();

}