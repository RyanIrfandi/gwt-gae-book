package org.gwtgaebook.CultureShows.client.widget;

import org.gwtgaebook.CultureShows.client.event.SignInEvent;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineLabel;
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
	TokenSeparatedList nav;

	@UiField
	Anchor signIn;
	@UiField
	Anchor signOut;
	@UiField
	InlineLabel username;

	private UserInfo userInfo;

	public HeaderView() {
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		if (userInfo.isSignedIn) {
			username.setText(userInfo.email);
			nav.setWidgetVisible(username, true);
			nav.setWidgetVisible(signOut, true);
		} else {
			nav.setWidgetVisible(signIn, true);
		}

	}

	@UiHandler("signIn")
	void onSignInClicked(ClickEvent event) {
		getUiHandlers().requestSignIn();
	}

	@UiHandler("signOut")
	void onSignOutClicked(ClickEvent event) {
		Window.Location.replace(userInfo.signOutURL);
	}
}