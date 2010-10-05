package org.gwtgaebook.CultureShows.client.landing;

import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.*;
import com.google.gwt.query.client.*;
import static com.google.gwt.query.client.GQuery.*;
import static com.google.gwt.query.client.css.CSS.*;

import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.allen_sauer.gwt.log.client.*;

import org.gwtgaebook.CultureShows.client.*;

public class LandingView extends ViewWithUiHandlers<LandingUiHandlers>
		implements LandingPresenter.MyView {

	interface LandingViewUiBinder extends UiBinder<Widget, LandingView> {
	}

	private static LandingViewUiBinder uiBinder = GWT
			.create(LandingViewUiBinder.class);

	public final Widget widget;

	@UiField
	HTMLPanel container;

	@UiField
	TextBox show;
	@UiField
	TextBox location;
	@UiField
	DateBox dateTime;

	@UiField
	Button scheduleShow;

	public LandingView() {
		widget = uiBinder.createAndBindUi(this);

		// workaround for
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=5295
		dateTime.getElement().setAttribute("required", "");
		dateTime.getElement().setAttribute("placeholder", "Date and time");
		show.getElement().setAttribute("placeholder", "Show name");
		show.getElement().setAttribute("required", "");
		location.getElement().setAttribute("placeholder", "Location");
		show.getElement().setAttribute("required", "");

	}

	@Override
	public Widget asWidget() {
		return container;
	}

	private static native void placeholder() /*-{
		$wnd.$("input:text").placeholder();
	}-*/;

	public void resetAndFocus() {
		// jQuery fallback for browsers which don't support placeholder
		// attribute
		placeholder();
	}

	@UiHandler("scheduleShow")
	void onScheduleShowClicked(ClickEvent event) {
		getUiHandlers().scheduleShow(dateTime.getValue(), show.getValue(),
				location.getValue());
	}

}
