package org.gwtgaebook.CultureShows.client.widget;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.gin.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

public class WidgetModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.MyView.class, HeaderView.class);

	}
}
