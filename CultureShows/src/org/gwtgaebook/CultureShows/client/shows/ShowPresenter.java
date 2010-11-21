package org.gwtgaebook.CultureShows.client.shows;

import java.util.List;

import org.gwtgaebook.CultureShows.client.ClientState;
import org.gwtgaebook.CultureShows.client.DispatchCallback;
import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.MainPresenter;
import org.gwtgaebook.CultureShows.client.NameTokens;
import org.gwtgaebook.CultureShows.client.SignedInGatekeeper;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.dispatch.GetShowsAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetShowsResult;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceResult;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowResult;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class ShowPresenter extends
		Presenter<ShowPresenter.MyView, ShowPresenter.MyProxy> implements
		ShowUiHandlers {

	@ProxyCodeSplit
	@NameToken(NameTokens.shows)
	@UseGatekeeper(SignedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<ShowPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<ShowUiHandlers> {
		public void resetAndFocus();

		public void setDefaultValues();

		public void loadShowData(Integer start, Integer length, List<Show> shows);

		public void refreshShows();

	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	@Inject
	public ShowPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager, DispatchAsync dispatcher,
			final ClientState clientState) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.clientState = clientState;

		getView().setUiHandlers(this);

	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().resetAndFocus();

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_RevealMainContent,
				this);
		requestShows();
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void requestShows() {
		// Strings.isNullOrEmpty(clientState.currentTheaterKey)
		// TODO move this check to gatekeeper
		if (!(null == clientState.currentTheaterKey || clientState.currentTheaterKey
				.isEmpty())) {
			dispatcher.execute(
					new GetShowsAction(clientState.currentTheaterKey),
					new DispatchCallback<GetShowsResult>() {
						@Override
						public void onSuccess(GetShowsResult result) {
							if (!result.getErrorText().isEmpty()) {
								// TODO have a general handler for this
								Window.alert(result.getErrorText());
								return;
							}
							getView()
									.loadShowData(Constants.visibleRangeStart,
											result.getShows().size(),
											result.getShows());
						}
					});
		}

	}

	@Override
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength) {
		requestShows();
	}

	public void onShowSelected(Show s) {
		Main.logger.info("Selected show " + s.showKey + " with show "
				+ s.getName());
	}

	@Override
	public void create(String name, String websiteURL, int minuteDuration,
			String posterURL) {
		Show s = new Show();
		s.setName(name);
		s.websiteURL = websiteURL;
		s.minuteDuration = minuteDuration;
		s.posterURL = posterURL;

		dispatcher.execute(new ManageShowAction(clientState.currentTheaterKey,
				Constants.ManageActionType.CREATE, s),
				new DispatchCallback<ManageShowResult>() {
					@Override
					public void onSuccess(ManageShowResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshShows();
					}
				});

	}

	@Override
	public void update(String showKey, String name, String websiteURL,
			int minuteDuration, String posterURL) {

		Show s = new Show();
		s.showKey = showKey;
		s.setName(name);
		s.websiteURL = websiteURL;
		s.minuteDuration = minuteDuration;
		s.posterURL = posterURL;

		dispatcher.execute(new ManageShowAction(clientState.currentTheaterKey,
				Constants.ManageActionType.UPDATE, s),
				new DispatchCallback<ManageShowResult>() {
					@Override
					public void onSuccess(ManageShowResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshShows();
					}
				});

	}

	@Override
	public void delete(String showKey) {
		Show s = new Show();
		s.showKey = showKey;

		dispatcher.execute(new ManageShowAction(clientState.currentTheaterKey,
				Constants.ManageActionType.DELETE, s),
				new DispatchCallback<ManageShowResult>() {
					@Override
					public void onSuccess(ManageShowResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshShows();
					}
				});

	}

}