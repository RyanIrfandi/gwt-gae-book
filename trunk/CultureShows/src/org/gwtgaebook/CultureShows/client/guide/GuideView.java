package org.gwtgaebook.CultureShows.client.guide;

import org.gwtgaebook.CultureShows.client.ClientState;
import org.restlet.Client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class GuideView extends ViewWithUiHandlers<GuideUiHandlers> implements
		GuidePresenter.MyView {

	interface GuideViewUiBinder extends UiBinder<Widget, GuideView> {
	}

	private static GuideViewUiBinder uiBinder = GWT
			.create(GuideViewUiBinder.class);

	public final Widget widget;
	// private ClientState clientState;

	@UiField
	Label theaterKey;

	@Inject
	public GuideView(final ClientState clientState) {
		widget = uiBinder.createAndBindUi(this);
		theaterKey.setText(clientState.currentTheaterKey);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

}
