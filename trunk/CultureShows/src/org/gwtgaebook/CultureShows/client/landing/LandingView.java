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

	interface PerformancesResources extends CellList.Resources {
		@Source(value = { CellList.Style.DEFAULT_CSS, "../resources/cell.css" })
		CellList.Style cellListStyle();
	}

	public final Widget widget;
	private final PerformancesAsyncAdapter performancesAsyncAdapter;

	@UiField
	DateBox date;
	@UiField
	TextBox show;
	@UiField
	TextBox location;

	@UiField
	Label dateLbl;
	@UiField
	Label showLbl;
	@UiField
	Label locationLbl;

	@UiField
	Button updatePerformance;
	@UiField
	Button deletePerformance;

	@UiField
	CellList<Performance> performancesCL;

	@UiField
	HTMLPanel sidebar;

	public LandingView() {
		widget = uiBinder.createAndBindUi(this);

		// fallback for browsers which don't support placeholder attribute
		// null == date.getElement().getAttribute("placeholder")
		if (placeholderSupport().equals("no")) {
			dateLbl.setVisible(true);
			showLbl.setVisible(true);
			locationLbl.setVisible(true);
		}

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

	// workaround for
	// http://code.google.com/p/google-web-toolkit/issues/detail?id=5541
	private static native String placeholderSupport() /*-{
		var i = document.createElement('input');
		if ('placeholder' in i) {
		return "yes";
		} else {
		return "no";
		}
	}-*/;

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDefaultValues() {
		date.setValue(null);
		show.setValue("");
		if (null != Cookies
				.getCookie(Constants.performanceLocationNameCookieName)) {
			location.setValue(Cookies
					.getCookie(Constants.performanceLocationNameCookieName));
		} else {
			location.setValue("");
		}
	}

	public void resetAndFocus() {
		setDefaultValues();
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
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		CellList<Performance> cl = new CellList<Performance>(
				performanceCell,
				GWT.<PerformancesResources> create(PerformancesResources.class),
				Performance.KEY_PROVIDER);
		cl.setEmptyListMessage(sb.appendHtmlConstant(
				"No performances created yet").toSafeHtml());
		setSelectionModel(cl);

		return cl;
	}

	@Override
	public void loadPerformanceData(Integer start, Integer length,
			List<Performance> performances) {
		setIsPerformanceSelected(false);
		performancesAsyncAdapter.updateRowData(start, performances);
		performancesAsyncAdapter.updateRowCount(length, false);
		performancesCL.setVisibleRange(start, Constants.visibleRangeLength);
		performancesCL.redraw();
	}

	@Override
	public void refreshPerformances() {
		setIsPerformanceSelected(false);
		getUiHandlers().onRangeOrSizeChanged(
				performancesCL.getVisibleRange().getStart(),
				performancesCL.getVisibleRange().getLength());
	}

	public void setIsPerformanceSelected(Boolean selected) {
		updatePerformance.setVisible(selected);
		deletePerformance.setVisible(selected);
		if (!selected) {
			setDefaultValues();
		}
	}

	private void setSelectionModel(CellList<Performance> cl) {
		final SingleSelectionModel<Performance> selectionModel = new SingleSelectionModel<Performance>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Performance p = selectionModel.getSelectedObject();
						setIsPerformanceSelected(null != p);
						if (null != p) {
							date.setValue(p.date);
							show.setValue(p.showName);
							location.setValue(p.locationName);
						}
						getUiHandlers().onPerformanceSelected(p);
					}
				});

		cl.setSelectionModel(selectionModel);
	}

	@UiHandler("createPerformance")
	void onCreatePerformanceClicked(ClickEvent event) {
		getUiHandlers().createPerformance(date.getValue(), show.getValue(),
				location.getValue());
	}

	@UiHandler("updatePerformance")
	void onUpdatePerformanceClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Performance> selectionModel = (SingleSelectionModel<Performance>) performancesCL
				.getSelectionModel();
		getUiHandlers().updatePerformance(
				selectionModel.getSelectedObject().performanceKey,
				date.getValue(), show.getValue(), location.getValue());
	}

	@UiHandler("deletePerformance")
	void onDeletePerformanceClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Performance> selectionModel = (SingleSelectionModel<Performance>) performancesCL
				.getSelectionModel();
		getUiHandlers().deletePerformance(
				selectionModel.getSelectedObject().performanceKey);
	}
}
