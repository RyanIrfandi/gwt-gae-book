package org.gwtgaebook.CultureShows.client.landing;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
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

	public class PerformanceCell extends AbstractCell<Performance> {

		@Override
		public void render(Performance performance, Object key,
				SafeHtmlBuilder sb) {

			if (null == performance) {
				return;
			}

			DateTimeFormat dateFormat = DateTimeFormat
					.getFormat(Constants.defaultDateFormat);

			sb.appendHtmlConstant("<span class='performanceDate'>");
			sb.appendEscaped(dateFormat.format(performance.date));
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='showName'>");
			sb.appendEscaped(performance.showName);
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='locationName'>");
			sb.appendEscaped(performance.locationName);
			sb.appendHtmlConstant("</span>");
		}
	}

	protected class PerformancesAsyncAdapter extends
			AsyncDataProvider<Performance> {
		@Override
		protected void onRangeChanged(HasData<Performance> display) {
			if (getUiHandlers() != null) {
				Range newRange = display.getVisibleRange();
				getUiHandlers().onRangeOrSizeChanged(newRange.getStart(),
						newRange.getLength());
			}
		}
	}

	public final Widget widget;
	private final PerformancesAsyncAdapter performancesAsyncAdapter;

	//
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

	// @UiField
	// HTML performancesContainer;

	@UiField
	CellList<Performance> performancesCL;

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

		performancesCL.setVisibleRange(Constants.visibleRangeStart,
				Constants.visibleRangeLength);
		performancesAsyncAdapter = new PerformancesAsyncAdapter();
		performancesAsyncAdapter.addDataDisplay(performancesCL);

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

	// public void addPerformance(Performance p) {
	// performancesContainer.setHTML(performancesContainer.getHTML() + "<br/>"
	// + p.showName + " | " + p.locationName + " | "
	// + p.date.toString());
	//
	// }
	//
	// public void setPerformances(List<Performance> performances) {
	// performancesContainer.setHTML("Show | Location | Date");
	//
	// for (Performance p : performances) {
	// addPerformance(p);
	// }
	//
	// }

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

	@UiFactory
	CellList<Performance> createPerformanceCL() {
		PerformanceCell performanceCell = new PerformanceCell();
		CellList<Performance> cl = new CellList<Performance>(performanceCell,
				Performance.KEY_PROVIDER);
		setSelectionModel(cl);

		return cl;
	}

	@Override
	public void loadPerformanceData(Integer start, Integer length,
			List<Performance> performances) {
		performancesAsyncAdapter.updateRowData(start, performances);
		performancesAsyncAdapter.updateRowCount(length, false);
		performancesCL.setVisibleRange(start, Constants.visibleRangeLength);
		performancesCL.redraw();
	}

	@Override
	public void refreshPerformances() {
		getUiHandlers().onRangeOrSizeChanged(
				performancesCL.getVisibleRange().getStart(),
				performancesCL.getVisibleRange().getLength());
	}

	private void setSelectionModel(CellList<Performance> cl) {
		final SingleSelectionModel<Performance> selectionModel = new SingleSelectionModel<Performance>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Performance p = selectionModel.getSelectedObject();

						getUiHandlers().onPerformanceSelected(p);
					}
				});

		cl.setSelectionModel(selectionModel);
	}

}
