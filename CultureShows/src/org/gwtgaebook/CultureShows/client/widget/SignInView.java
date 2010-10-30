package org.gwtgaebook.CultureShows.client.widget;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.*;

public class SignInView extends PopupViewImpl implements SignInPresenter.MyView {

	interface HeaderViewUiBinder extends UiBinder<Widget, SignInView> {
	}

	private static HeaderViewUiBinder uiBinder = GWT
			.create(HeaderViewUiBinder.class);
	private final Widget widget;

	private UserInfo userInfo;

	@Inject
	public SignInView(EventBus eventBus) {
		super(eventBus);
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@UiHandler("googleSignIn")
	void onGoogleSignInClicked(ClickEvent event) {
		Main.logger.info("redirecting to " + userInfo.signInURLs.get("Google"));
		Window.Location.replace(userInfo.signInURLs.get("Google"));
	}

	@UiHandler("yahooSignIn")
	void onYahooSignInClicked(ClickEvent event) {
		Window.Location.replace(userInfo.signInURLs.get("Yahoo"));
	}

}