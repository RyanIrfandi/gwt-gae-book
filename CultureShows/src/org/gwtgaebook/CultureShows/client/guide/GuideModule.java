package org.gwtgaebook.CultureShows.client.guide;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class GuideModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(GuidePresenter.class, GuidePresenter.MyView.class,
				GuideView.class, GuidePresenter.MyProxy.class);

	}
}
