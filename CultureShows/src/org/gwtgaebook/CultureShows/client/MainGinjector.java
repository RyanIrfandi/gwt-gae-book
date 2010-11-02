package org.gwtgaebook.CultureShows.client;

import com.google.inject.*;
import com.google.gwt.inject.client.*;
import com.gwtplatform.dispatch.client.gin.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.CultureShows.client.resources.*;
import org.gwtgaebook.CultureShows.client.widget.*;
import org.gwtgaebook.CultureShows.client.landing.*;

@GinModules({ DispatchAsyncModule.class, MainModule.class, WidgetModule.class,
		LandingModule.class })
public interface MainGinjector extends Ginjector {
	EventBus getEventBus();

	PlaceManager getPlaceManager();

	ProxyFailureHandler getProxyFailureHandler();

	Resources getResources();

	Translations getTranslations();

	Provider<MainPresenter> getMainPresenter();

	Provider<LandingPresenter> getLandingPresenter();

}