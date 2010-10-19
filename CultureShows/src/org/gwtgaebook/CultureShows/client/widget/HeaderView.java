package org.gwtgaebook.CultureShows.client.widget;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.*;

public class HeaderView extends ViewWithUiHandlers<HeaderUiHandlers> implements
		HeaderPresenter.MyView {

	interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
	}

	private static HeaderViewUiBinder uiBinder = GWT
			.create(HeaderViewUiBinder.class);
	private final Widget widget;

	@UiField
	HTML signInOut;

	@Inject
	public HeaderView() {
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setSignInOut(UserInfo userInfo) {
		String html;
		if (userInfo.isSignedIn) {
			html = userInfo.email + " | " + "<a href='" + userInfo.signOutURL
					+ "'>Sign Out</a>";
		} else {
			html = "<a href='" + userInfo.signInURLs.get("Google")
					+ "'>Sign In with Google</a>" + " | " + "<a href='"
					+ userInfo.signInURLs.get("Yahoo")
					+ "'>Sign In with Yahoo</a>";

		}
		signInOut.setHTML(html);

	}

}