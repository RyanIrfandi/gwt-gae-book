package org.gwtgaebook.CultureShows.client.performances;

import java.util.List;

import org.gwtgaebook.CultureShows.client.locations.model.Location;
import org.gwtgaebook.CultureShows.client.shows.model.Show;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Performance;

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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PerformanceView extends ViewWithUiHandlers<PerformanceUiHandlers>
		implements PerformancePresenter.MyView {

	interface PerformanceViewUiBinder extends UiBinder<Widget, PerformanceView> {
	}

	private static PerformanceViewUiBinder uiBinder = GWT
			.create(PerformanceViewUiBinder.class);

	public class PerformanceCell extends AbstractCell<Performance> {

		@Override
		public void render(Context context, Performance performance,
				SafeHtmlBuilder sb) {

			if (null == performance) {
				return;
			}

			// DateTimeFormat dateFormat = DateTimeFormat
			// .getFormat(Constants.defaultDateFormat);

			sb.appendHtmlConstant("<span class='performanceDate'>");
			// sb.appendEscaped(dateFormat.format(performance.date));
			sb.appendEscaped(performance.date);
			sb.appendHtmlConstant(" ");
			sb.appendEscaped(performance.timeHourMinute);
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
	TextBox timeHourMinute;

	// @UiField
	// TextBox show;
	@UiField(provided = true)
	SuggestBox show;

	// @UiField
	// TextBox location;
	@UiField(provided = true)
	SuggestBox location;

	@UiField
	Button updatePerformance;
	@UiField
	Button deletePerformance;

	@UiField
	CellList<Performance> performancesCL;

	MultiWordSuggestOracle showSO, locationSO;

	public PerformanceView() {
		showSO = new MultiWordSuggestOracle();
		show = new SuggestBox(showSO);
		locationSO = new MultiWordSuggestOracle();
		location = new SuggestBox(locationSO);

		widget = uiBinder.createAndBindUi(this);

		// fallback for browsers which don't support placeholder attribute
		// null == date.getElement().getAttribute("placeholder")
		// if (placeholderSupport().equals("no")) {
		// dateLbl.setVisible(true);
		// showLbl.setVisible(true);
		// locationLbl.setVisible(true);
		// }

		date.setFormat(new DateBox.DefaultFormat(DateTimeFormat
				.getFormat(Constants.defaultDateFormat)));
		// workaround for
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=5295
		// date.getElement().setAttribute("placeholder", "Date and time");
		// show.getElement().setAttribute("placeholder", "Show name");
		// location.getElement().setAttribute("placeholder", "Location");
		date.getElement().setAttribute("required", "");
		show.getElement().setAttribute("required", "");
		show.getElement().setAttribute("required", "");

		performancesCL.setVisibleRange(Constants.visibleRangeStart,
				Constants.visibleRangeLength);
		performancesAsyncAdapter = new PerformancesAsyncAdapter();
		performancesAsyncAdapter.addDataDisplay(performancesCL);

	}

	// workaround for
	// http://code.google.com/p/google-web-toolkit/issues/detail?id=5541
	// private static native String placeholderSupport() /*-{
	// var i = document.createElement('input');
	// if ('placeholder' in i) {
	// return "yes";
	// } else {
	// return "no";
	// }
	// }-*/;

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDefaultValues() {
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(Constants.defaultDateFormat);
		if (null != Cookies.getCookie(Constants.PerformanceDateCookieName)) {
			try {
				date.setValue(dateFormat.parse(Cookies
						.getCookie(Constants.PerformanceDateCookieName)));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			date.setValue(null);
		}

		if (null != Cookies.getCookie(Constants.PerformanceTimeCookieName)) {
			timeHourMinute.setValue(Cookies
					.getCookie(Constants.PerformanceTimeCookieName));
		} else {
			timeHourMinute.setValue("");
		}

		show.setValue("");

		if (null != Cookies
				.getCookie(Constants.PerformanceLocationNameCookieName)) {
			location.setValue(Cookies
					.getCookie(Constants.PerformanceLocationNameCookieName));
		} else {
			location.setValue("");
		}
	}

	public void resetAndFocus() {
		setDefaultValues();
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
							DateTimeFormat dateFormat = DateTimeFormat
									.getFormat(Constants.defaultDateFormat);
							try {
								date.setValue(dateFormat.parse(p.date));
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							timeHourMinute.setValue(p.timeHourMinute);
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
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(Constants.defaultDateFormat);

		getUiHandlers()
				.createPerformance(dateFormat.format(date.getValue()),
						timeHourMinute.getValue(), show.getValue(),
						location.getValue());
	}

	@UiHandler("updatePerformance")
	void onUpdatePerformanceClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		DateTimeFormat dateFormat = DateTimeFormat
				.getFormat(Constants.defaultDateFormat);

		final SingleSelectionModel<Performance> selectionModel = (SingleSelectionModel<Performance>) performancesCL
				.getSelectionModel();
		getUiHandlers().updatePerformance(
				selectionModel.getSelectedObject().performanceKey,
				dateFormat.format(date.getValue()), timeHourMinute.getValue(),
				show.getValue(), location.getValue());
	}

	@UiHandler("deletePerformance")
	void onDeletePerformanceClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Performance> selectionModel = (SingleSelectionModel<Performance>) performancesCL
				.getSelectionModel();
		getUiHandlers().deletePerformance(
				selectionModel.getSelectedObject().performanceKey);
	}

	@Override
	public void setLocationData(List<Location> locations) {
		locationSO.clear();
		for (Location l : locations) {
			locationSO.add(l.name);
		}
	}

	@Override
	public void setShowData(List<Show> shows) {
		showSO.clear();
		for (Show s : shows) {
			showSO.add(s.name);
		}
	}

}
