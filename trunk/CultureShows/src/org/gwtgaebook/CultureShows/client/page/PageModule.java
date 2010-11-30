package org.gwtgaebook.CultureShows.client.page;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PageModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(PagePresenter.class, PagePresenter.MyView.class,
				PageView.class, PagePresenter.MyProxy.class);

	}
}
