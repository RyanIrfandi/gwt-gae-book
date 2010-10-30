package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.*;
import com.google.gwt.query.client.*;
import static com.google.gwt.query.client.GQuery.*;
import static com.google.gwt.query.client.css.CSS.*;

import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;

import org.gwtgaebook.CultureShows.client.*;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.*;

public class LandingView extends ViewWithUiHandlers<LandingUiHandlers>
		implements LandingPresenter.MyView {

	interface LandingViewUiBinder extends UiBinder<Widget, LandingView> {
	}

	private static LandingViewUiBinder uiBinder = GWT
			.create(LandingViewUiBinder.class);

	public final Widget widget;

	// @UiField
	// HTMLPanel container;

	@UiField
	TextBox show;
	@UiField
	TextBox location;
	@UiField
	DateBox date;

	@UiField
	Button scheduleShow;

	@UiField
	HTML performancesContainer;

	@UiField
	HTMLPanel sidebar;

	public LandingView() {
		widget = uiBinder.createAndBindUi(this);

		date.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat(Constants.defaultDateFormat)));
		// workaround for
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=5295
		date.getElement().setAttribute("required", "");
		date.getElement().setAttribute("placeholder", "Date and time");
		show.getElement().setAttribute("placeholder", "Show name");
		show.getElement().setAttribute("required", "");
		location.getElement().setAttribute("placeholder", "Location");
		show.getElement().setAttribute("required", "");

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	private static native void placeholder() /*-{
		$wnd.$("input:text").placeholder();
	}-*/;

	public void resetAndFocus() {
		// jQuery fallback for browsers which don't support placeholder
		// attribute
		placeholder();
	}

	public void addPerformances(Map<String, Performance> performanceMap) {
		Iterator<String> i = performanceMap.keySet().iterator();
		String key;
		while (i.hasNext()) {
			key = i.next();
			performancesContainer.setHTML(performancesContainer.getHTML()
					+ "<br/>" + performanceMap.get(key).showName + " | "
					+ performanceMap.get(key).locationName + " | "
					+ performanceMap.get(key).date.toString());

		}

	}

	public void setPerformances(Map<String, Performance> performanceMap) {
		performancesContainer.setHTML("Show | Location | Date");

		addPerformances(performanceMap);

	}

	@UiHandler("scheduleShow")
	void onScheduleShowClicked(ClickEvent event) {
		getUiHandlers().scheduleShow(date.getValue(), show.getValue(),
				location.getValue());
	}

	public void setSignInOut(UserInfo userInfo) {
		// ideally, sign in markup shouldn't even be loaded or present in dom if
		// user is signed in, but it's not that big to justify creating a
		// separate widget or create it here dynamically instead of UiBinder
		if (!userInfo.isSignedIn) {
			sidebar.setVisible(true);
		}

	}

	@UiHandler("signIn")
	void onSignInClicked(ClickEvent event) {
		getUiHandlers().requestSignIn();
	}

}
