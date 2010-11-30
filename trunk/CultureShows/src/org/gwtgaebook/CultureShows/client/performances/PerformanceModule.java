package org.gwtgaebook.CultureShows.client.performances;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.gin.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.CultureShows.client.*;

public class PerformanceModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(PerformancePresenter.class, PerformancePresenter.MyView.class,
				PerformanceView.class, PerformancePresenter.MyProxy.class);

	}
}
