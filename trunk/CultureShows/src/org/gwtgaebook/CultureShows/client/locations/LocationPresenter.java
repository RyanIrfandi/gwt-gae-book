package org.gwtgaebook.CultureShows.client.locations;

import java.util.List;

import org.gwtgaebook.CultureShows.client.ClientState;
import org.gwtgaebook.CultureShows.client.DispatchCallback;
import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.NameTokens;
import org.gwtgaebook.CultureShows.client.SignedInGatekeeper;
import org.gwtgaebook.CultureShows.client.locations.dispatch.ReadLocationsAction;
import org.gwtgaebook.CultureShows.client.locations.dispatch.ReadLocationsResult;
import org.gwtgaebook.CultureShows.client.locations.model.Location;
import org.gwtgaebook.CultureShows.client.page.PagePresenter;
import org.gwtgaebook.CultureShows.shared.Constants;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class LocationPresenter extends
		Presenter<LocationPresenter.MyView, LocationPresenter.MyProxy>
		implements LocationUiHandlers {

	@ProxyCodeSplit
	@NameToken(NameTokens.locations)
	@UseGatekeeper(SignedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<LocationPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<LocationUiHandlers> {
		public void resetAndFocus();

		public void setDefaultValues();

		public void loadLocationData(Integer start, Integer length,
				List<Location> locations);

		public void refreshLocations();

	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	@Inject
	public LocationPresenter(EventBus eventBus, MyView view, MyProxy proxy,
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
		RevealContentEvent.fire(this, PagePresenter.TYPE_RevealSpecificContent,
				this);
		requestLocations();
	}

	public void requestLocations() {
		// Strings.isNullOrEmpty(clientState.currentTheaterKey)
		// TODO move this check to gatekeeper
		if (!(null == clientState.currentTheaterKey || clientState.currentTheaterKey
				.isEmpty())) {

			dispatcher.execute(new ReadLocationsAction(
					clientState.currentTheaterKey),
					new DispatchCallback<ReadLocationsResult>() {
						@Override
						public void onSuccess(ReadLocationsResult result) {
							Main.logger.info(result.toString());
							// TODO have just getLocations() instead of
							// getLocations().locations, by using piriti-restlet
							getView().loadLocationData(
									Constants.visibleRangeStart,
									result.getLocations().locations.size(),
									result.getLocations().locations);

						}
					});
		}

	}

	@Override
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength) {
		requestLocations();
	}

	public void onLocationSelected(Location l) {
		Main.logger.info("Selected location " + l.locationKey + " with name "
				+ l.name);
	}

	@Override
	public void create(String name, String websiteURL) {
		// Show s = new Show();
		// s.setName(name);
		// s.websiteURL = websiteURL;
		// s.minuteDuration = minuteDuration;
		// s.posterURL = posterURL;
		//
		// dispatcher.execute(new
		// ManageShowAction(clientState.currentTheaterKey,
		// Constants.ManageActionType.CREATE, s),
		// new DispatchCallback<ManageShowResult>() {
		// @Override
		// public void onSuccess(ManageShowResult result) {
		// if (!result.getErrorText().isEmpty()) {
		// // TODO have a general handler for this
		// Window.alert(result.getErrorText());
		// return;
		// }
		// getView().setDefaultValues();
		// getView().refreshLocations();
		// }
		// });

	}

	@Override
	public void update(String locationKey, String name, String websiteURL) {

		// Show s = new Show();
		// s.showKey = showKey;
		// s.setName(name);
		// s.websiteURL = websiteURL;
		// s.minuteDuration = minuteDuration;
		// s.posterURL = posterURL;
		//
		// dispatcher.execute(new
		// ManageShowAction(clientState.currentTheaterKey,
		// Constants.ManageActionType.UPDATE, s),
		// new DispatchCallback<ManageShowResult>() {
		// @Override
		// public void onSuccess(ManageShowResult result) {
		// if (!result.getErrorText().isEmpty()) {
		// // TODO have a general handler for this
		// Window.alert(result.getErrorText());
		// return;
		// }
		// getView().setDefaultValues();
		// getView().refreshLocations();
		// }
		// });

	}

	@Override
	public void delete(String locationKey) {
		// Show s = new Show();
		// s.showKey = showKey;
		//
		// dispatcher.execute(new
		// ManageShowAction(clientState.currentTheaterKey,
		// Constants.ManageActionType.DELETE, s),
		// new DispatchCallback<ManageShowResult>() {
		// @Override
		// public void onSuccess(ManageShowResult result) {
		// if (!result.getErrorText().isEmpty()) {
		// // TODO have a general handler for this
		// Window.alert(result.getErrorText());
		// return;
		// }
		// getView().setDefaultValues();
		// getView().refreshLocations();
		// }
		// });

	}

}