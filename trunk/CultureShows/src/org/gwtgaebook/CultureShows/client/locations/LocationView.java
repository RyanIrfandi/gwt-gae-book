package org.gwtgaebook.CultureShows.client.locations;

import java.util.List;

import org.gwtgaebook.CultureShows.client.locations.model.Location;
import org.gwtgaebook.CultureShows.shared.Constants;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class LocationView extends ViewWithUiHandlers<LocationUiHandlers>
		implements LocationPresenter.MyView {

	interface LocationViewUiBinder extends UiBinder<Widget, LocationView> {
	}

	private static LocationViewUiBinder uiBinder = GWT
			.create(LocationViewUiBinder.class);

	public class LocationCell extends AbstractCell<Location> {

		@Override
		public void render(Context context, Location location,
				SafeHtmlBuilder sb) {

			if (null == location) {
				return;
			}

			sb.append(SafeHtmlUtils
					.fromTrustedString("<div class='locationPosterContainer'>"));
			sb.append(SafeHtmlUtils.fromTrustedString("</div>"));

			sb.append(SafeHtmlUtils
					.fromTrustedString("<div class='locationPosterContainer'>"));
			sb.append(SafeHtmlUtils
					.fromTrustedString("<a class='locationWebsite' target='_blank' href='"));
			if (null == location.websiteURL || location.websiteURL.isEmpty()) {
				sb.append(SafeHtmlUtils
						.fromTrustedString("http://www.google.com/search?q="
								+ location.name));
			} else {
				sb.appendEscaped(location.websiteURL);
			}
			sb.append(SafeHtmlUtils.fromTrustedString("'>"));
			sb.appendEscaped(location.name);
			sb.append(SafeHtmlUtils.fromTrustedString("</a><br/>"));

			sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
		}
	}

	protected class LocationsAsyncAdapter extends AsyncDataProvider<Location> {
		@Override
		protected void onRangeChanged(HasData<Location> display) {
			if (getUiHandlers() != null) {
				Range newRange = display.getVisibleRange();
				getUiHandlers().onRangeOrSizeChanged(newRange.getStart(),
						newRange.getLength());
			}
		}
	}

	interface LocationsResources extends CellList.Resources {
		@Source(value = { CellList.Style.DEFAULT_CSS, "../resources/cell.css" })
		CellList.Style cellListStyle();
	}

	public final Widget widget;
	private final LocationsAsyncAdapter locationsAsyncAdapter;

	@UiField
	TextBox name;
	@UiField
	TextBox websiteURL;

	@UiField
	Button update;
	@UiField
	Button delete;

	@UiField
	CellList<Location> locationsCL;

	public LocationView() {
		widget = uiBinder.createAndBindUi(this);

		// workaround for
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=5295
		name.getElement().setAttribute("required", "");
		name.getElement().setAttribute("placeholder", "Name");
		websiteURL.getElement().setAttribute("placeholder", "Website URL");

		locationsCL.setVisibleRange(Constants.visibleRangeStart,
				Constants.visibleRangeLength);
		locationsAsyncAdapter = new LocationsAsyncAdapter();
		locationsAsyncAdapter.addDataDisplay(locationsCL);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDefaultValues() {
		name.setValue("");
		websiteURL.setValue("");
	}

	public void resetAndFocus() {
		setDefaultValues();
	}

	@UiFactory
	CellList<Location> createLocationCL() {
		LocationCell locationCell = new LocationCell();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		CellList<Location> cl = new CellList<Location>(locationCell,
				GWT.<LocationsResources> create(LocationsResources.class),
				Location.KEY_PROVIDER);
		cl.setEmptyListMessage(sb
				.appendHtmlConstant("No locations created yet").toSafeHtml());
		setSelectionModel(cl);

		return cl;
	}

	@Override
	public void loadLocationData(Integer start, Integer length,
			List<Location> locations) {
		setIsLocationSelected(false);
		locationsAsyncAdapter.updateRowData(start, locations);
		locationsAsyncAdapter.updateRowCount(length, false);
		locationsCL.setVisibleRange(start, Constants.visibleRangeLength);
		locationsCL.redraw();
	}

	@Override
	public void refreshLocations() {
		setIsLocationSelected(false);
		getUiHandlers().onRangeOrSizeChanged(
				locationsCL.getVisibleRange().getStart(),
				locationsCL.getVisibleRange().getLength());
	}

	public void setIsLocationSelected(Boolean selected) {
		update.setVisible(selected);
		delete.setVisible(selected);
		if (!selected) {
			setDefaultValues();
		}
	}

	private void setSelectionModel(CellList<Location> cl) {
		final SingleSelectionModel<Location> selectionModel = new SingleSelectionModel<Location>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Location l = selectionModel.getSelectedObject();
						setIsLocationSelected(null != l);
						if (null != l) {
							name.setValue(l.name);
							websiteURL.setValue(l.websiteURL);
						}
						getUiHandlers().onLocationSelected(l);
					}
				});

		cl.setSelectionModel(selectionModel);
	}

	@UiHandler("create")
	void onCreateLocationClicked(ClickEvent event) {
		getUiHandlers().create(name.getValue(), websiteURL.getValue());
	}

	@UiHandler("update")
	void onUpdateLocationClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Location> selectionModel = (SingleSelectionModel<Location>) locationsCL
				.getSelectionModel();
		// TODO int validation
		getUiHandlers().update(selectionModel.getSelectedObject().locationKey,
				name.getValue(), websiteURL.getValue());
	}

	@UiHandler("delete")
	void onDeleteLocationClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Location> selectionModel = (SingleSelectionModel<Location>) locationsCL
				.getSelectionModel();
		getUiHandlers().delete(selectionModel.getSelectedObject().locationKey);
	}
}
