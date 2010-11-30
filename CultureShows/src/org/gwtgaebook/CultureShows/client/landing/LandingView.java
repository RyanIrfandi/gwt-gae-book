package org.gwtgaebook.CultureShows.client.landing;

import java.util.List;

import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class LandingView extends ViewWithUiHandlers<LandingUiHandlers>
		implements LandingPresenter.MyView {

	interface LandingViewUiBinder extends UiBinder<Widget, LandingView> {
	}

	private static LandingViewUiBinder uiBinder = GWT
			.create(LandingViewUiBinder.class);

	public final Widget widget;

	private UserInfo userInfo;

	@UiField
	HTMLPanel signInContainer;

	public LandingView() {
		widget = uiBinder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		if (!userInfo.isSignedIn) {
			signInContainer.setVisible(true);
		}
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
