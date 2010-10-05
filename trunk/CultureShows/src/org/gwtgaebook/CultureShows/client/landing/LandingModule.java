package org.gwtgaebook.CultureShows.client.landing;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.gin.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.CultureShows.client.*;

public class LandingModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(LandingPresenter.class, LandingPresenter.MyView.class,
				LandingView.class, LandingPresenter.MyProxy.class);

	}
}
