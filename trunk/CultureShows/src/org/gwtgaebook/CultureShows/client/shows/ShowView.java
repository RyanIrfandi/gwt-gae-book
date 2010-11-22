package org.gwtgaebook.CultureShows.client.shows;

import java.util.List;

import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.Misc;
import org.gwtgaebook.CultureShows.shared.model.Show;

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

public class ShowView extends ViewWithUiHandlers<ShowUiHandlers> implements
		ShowPresenter.MyView {

	interface ShowViewUiBinder extends UiBinder<Widget, ShowView> {
	}

	private static ShowViewUiBinder uiBinder = GWT
			.create(ShowViewUiBinder.class);

	public class ShowCell extends AbstractCell<Show> {

		@Override
		public void render(Show show, Object key, SafeHtmlBuilder sb) {

			if (null == show) {
				return;
			}

			sb.append(SafeHtmlUtils
					.fromTrustedString("<div class='showPosterContainer'>"));
			if (null != show.posterURL) {
				sb.append(SafeHtmlUtils
						.fromTrustedString("<img class='showPoster' src='"));
				sb.appendEscaped(show.posterURL);
				sb.append(SafeHtmlUtils.fromTrustedString("' />"));
			}
			sb.append(SafeHtmlUtils.fromTrustedString("</div>"));

			sb.append(SafeHtmlUtils
					.fromTrustedString("<div class='showPosterContainer'>"));
			sb.append(SafeHtmlUtils
					.fromTrustedString("<a class='showWebsite' target='_blank' href='"));
			if (null == show.websiteURL || show.websiteURL.isEmpty()) {
				sb.append(SafeHtmlUtils
						.fromTrustedString("http://www.google.com/search?q="
								+ show.getName()));
			} else {
				sb.appendEscaped(show.websiteURL);
			}
			sb.append(SafeHtmlUtils.fromTrustedString("'>"));
			sb.appendEscaped(show.getName());
			sb.append(SafeHtmlUtils.fromTrustedString("</a><br/>"));

			sb.appendEscaped(Misc.minutesToHHMM(show.minuteDuration));
			sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
		}
	}

	protected class ShowsAsyncAdapter extends AsyncDataProvider<Show> {
		@Override
		protected void onRangeChanged(HasData<Show> display) {
			if (getUiHandlers() != null) {
				Range newRange = display.getVisibleRange();
				getUiHandlers().onRangeOrSizeChanged(newRange.getStart(),
						newRange.getLength());
			}
		}
	}

	interface ShowsResources extends CellList.Resources {
		@Source(value = { CellList.Style.DEFAULT_CSS, "../resources/cell.css" })
		CellList.Style cellListStyle();
	}

	public final Widget widget;
	private final ShowsAsyncAdapter showsAsyncAdapter;

	@UiField
	TextBox name;
	@UiField
	TextBox websiteURL;
	@UiField
	TextBox minuteDuration;
	@UiField
	TextBox posterURL;

	@UiField
	Button update;
	@UiField
	Button delete;

	@UiField
	CellList<Show> showsCL;

	public ShowView() {
		widget = uiBinder.createAndBindUi(this);

		// workaround for
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=5295
		name.getElement().setAttribute("required", "");
		name.getElement().setAttribute("placeholder", "Name");
		websiteURL.getElement().setAttribute("placeholder", "Website URL");
		minuteDuration.getElement().setAttribute("placeholder",
				"Duration (minutes)");
		posterURL.getElement().setAttribute("placeholder", "Poster URL");

		showsCL.setVisibleRange(Constants.visibleRangeStart,
				Constants.visibleRangeLength);
		showsAsyncAdapter = new ShowsAsyncAdapter();
		showsAsyncAdapter.addDataDisplay(showsCL);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setDefaultValues() {
		name.setValue("");
		websiteURL.setValue("");
		minuteDuration.setValue("");
		posterURL.setValue("");
	}

	public void resetAndFocus() {
		setDefaultValues();
	}

	@UiFactory
	CellList<Show> createShowCL() {
		ShowCell showCell = new ShowCell();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		CellList<Show> cl = new CellList<Show>(showCell,
				GWT.<ShowsResources> create(ShowsResources.class),
				Show.KEY_PROVIDER);
		cl.setEmptyListMessage(sb.appendHtmlConstant("No shows created yet")
				.toSafeHtml());
		setSelectionModel(cl);

		return cl;
	}

	@Override
	public void loadShowData(Integer start, Integer length, List<Show> shows) {
		setIsShowSelected(false);
		showsAsyncAdapter.updateRowData(start, shows);
		showsAsyncAdapter.updateRowCount(length, false);
		showsCL.setVisibleRange(start, Constants.visibleRangeLength);
		showsCL.redraw();
	}

	@Override
	public void refreshShows() {
		setIsShowSelected(false);
		getUiHandlers().onRangeOrSizeChanged(
				showsCL.getVisibleRange().getStart(),
				showsCL.getVisibleRange().getLength());
	}

	public void setIsShowSelected(Boolean selected) {
		update.setVisible(selected);
		delete.setVisible(selected);
		if (!selected) {
			setDefaultValues();
		}
	}

	private void setSelectionModel(CellList<Show> cl) {
		final SingleSelectionModel<Show> selectionModel = new SingleSelectionModel<Show>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Show s = selectionModel.getSelectedObject();
						setIsShowSelected(null != s);
						if (null != s) {
							name.setValue(s.getName());
							websiteURL.setValue(s.websiteURL);
							minuteDuration.setValue(Integer
									.toString(s.minuteDuration));
							posterURL.setValue(s.posterURL);
						}
						getUiHandlers().onShowSelected(s);
					}
				});

		cl.setSelectionModel(selectionModel);
	}

	@UiHandler("create")
	void onCreateShowClicked(ClickEvent event) {
		int mD;
		try {
			mD = Integer.parseInt(minuteDuration.getValue());
		} catch (NumberFormatException nfe) {
			mD = 0;
		}
		getUiHandlers().create(name.getValue(), websiteURL.getValue(), mD,
				posterURL.getValue());
	}

	@UiHandler("update")
	void onUpdateShowClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Show> selectionModel = (SingleSelectionModel<Show>) showsCL
				.getSelectionModel();
		// TODO int validation
		getUiHandlers().update(selectionModel.getSelectedObject().showKey,
				name.getValue(), websiteURL.getValue(),
				Integer.parseInt(minuteDuration.getValue()),
				posterURL.getValue());
	}

	@UiHandler("delete")
	void onDeleteShowClicked(ClickEvent event) {
		// TODO how to solve Unchecked cast?
		final SingleSelectionModel<Show> selectionModel = (SingleSelectionModel<Show>) showsCL
				.getSelectionModel();
		getUiHandlers().delete(selectionModel.getSelectedObject().showKey);
	}
}
