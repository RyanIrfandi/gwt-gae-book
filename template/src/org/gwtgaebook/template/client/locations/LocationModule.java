package org.gwtgaebook.template.client.locations;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class LocationModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindPresenter(LocationPresenter.class, LocationPresenter.MyView.class,
				LocationView.class, LocationPresenter.MyProxy.class);

	}
}
