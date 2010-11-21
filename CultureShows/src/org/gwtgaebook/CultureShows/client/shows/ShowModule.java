package org.gwtgaebook.CultureShows.client.shows;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ShowModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(ShowPresenter.class, ShowPresenter.MyView.class,
				ShowView.class, ShowPresenter.MyProxy.class);

	}
}
