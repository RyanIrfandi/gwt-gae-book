package org.gwtgaebook.CultureShows.client;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import com.gwtplatform.mvp.client.*;

public class MainView extends ViewWithUiHandlers<MainUiHandlers> implements
		MainPresenter.MyView {

	interface LandingViewUiBinder extends UiBinder<Widget, MainView> {
	}

	private static LandingViewUiBinder uiBinder = GWT
			.create(LandingViewUiBinder.class);

	public final Widget widget;

	@UiField
	SimplePanel headerNav;

	@UiField
	SimplePanel main;

	public MainView() {
		widget = uiBinder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPresenter.TYPE_RevealHeaderContent) {
			headerNav.clear();
			headerNav.add(content);
		} else if (slot == MainPresenter.TYPE_RevealMainContent) {
			main.clear();
			main.add(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

}
