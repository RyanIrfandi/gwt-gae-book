package org.gwtgaebook.CultureShows.client.performances;

import java.util.Date;
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
import org.gwtgaebook.CultureShows.client.shows.dispatch.ReadShowsAction;
import org.gwtgaebook.CultureShows.client.shows.dispatch.ReadShowsResult;
import org.gwtgaebook.CultureShows.client.shows.model.Show;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesResult;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceResult;
import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class PerformancePresenter extends
		Presenter<PerformancePresenter.MyView, PerformancePresenter.MyProxy>
		implements PerformanceUiHandlers {

	@ProxyStandard
	@NameToken(NameTokens.performances)
	@UseGatekeeper(SignedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<PerformancePresenter> {
	}

	public interface MyView extends View, HasUiHandlers<PerformanceUiHandlers> {
		public void resetAndFocus();

		public void setDefaultValues();

		public void loadPerformanceData(Integer start, Integer length,
				List<Performance> performances);

		public void refreshPerformances();

		public void setLocationData(List<Location> locations);

		public void setShowData(List<Show> shows);

	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	@Inject
	public PerformancePresenter(EventBus eventBus, MyView view, MyProxy proxy,
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
		requestPerformances();

		dispatcher.execute(new ReadShowsAction(clientState.currentTheaterKey),
				new DispatchCallback<ReadShowsResult>() {
					@Override
					public void onSuccess(ReadShowsResult result) {
						Main.logger.info(result.toString());
						// TODO have just getLocations() instead of
						// getLocations().locations, by using piriti-restlet
						getView().setShowData(result.getShows().shows);

					}
				});

		dispatcher.execute(new ReadLocationsAction(
				clientState.currentTheaterKey),
				new DispatchCallback<ReadLocationsResult>() {
					@Override
					public void onSuccess(ReadLocationsResult result) {
						Main.logger.info(result.toString());
						// TODO have just getLocations() instead of
						// getLocations().locations, by using piriti-restlet
						getView().setLocationData(
								result.getLocations().locations);

					}
				});

	}

	public void requestPerformances() {
		// Strings.isNullOrEmpty(clientState.currentTheaterKey)
		if (!(null == clientState.currentTheaterKey || clientState.currentTheaterKey
				.isEmpty())) {
			dispatcher.execute(new GetPerformancesAction(
					clientState.currentTheaterKey),
					new DispatchCallback<GetPerformancesResult>() {
						@Override
						public void onSuccess(GetPerformancesResult result) {
							if (!result.getErrorText().isEmpty()) {
								// TODO have a general handler for this
								Window.alert(result.getErrorText());
								return;
							}
							// getView().setPerformances(result.getPerformances());
							// TODO result.getPageStart()
							getView().loadPerformanceData(
									Constants.visibleRangeStart,
									result.getPerformances().size(),
									result.getPerformances());
						}
					});
		}

	}

	@Override
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength) {
		// usually, this should have requested a new set of data from server for
		// visible range. Not needed on upcoming performances, just fetch them
		// all
		requestPerformances();
	}

	public void onPerformanceSelected(Performance p) {
		Main.logger.info("Selected performance " + p.performanceKey
				+ " with show " + p.showName);
	}

	@Override
	public void createPerformance(String date, String timeHourMinute,
			String showName, String locationName) {
		Main.logger.info("Requested performance scheduling on " + date + " "
				+ timeHourMinute + " : show " + showName + " at location "
				+ locationName + " for theater "
				+ clientState.currentTheaterKey);
		Performance p = new Performance();
		p.date = date;
		p.timeHourMinute = timeHourMinute;
		p.showName = showName;
		p.locationName = locationName;

		dispatcher.execute(new ManagePerformanceAction(
				clientState.currentTheaterKey,
				Constants.ManageActionType.CREATE, p),
				new DispatchCallback<ManagePerformanceResult>() {
					@Override
					public void onSuccess(ManagePerformanceResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						// remember last used values
						// DateTimeFormat dateFormat = DateTimeFormat
						// .getFormat(Constants.defaultDateFormat);
						// dateFormat.format(result.getPerformanceOut().date)
						Cookies.setCookie(Constants.PerformanceDateCookieName,
								result.getPerformanceOut().date);
						Cookies.setCookie(Constants.PerformanceTimeCookieName,
								result.getPerformanceOut().timeHourMinute);
						Cookies.setCookie(
								Constants.PerformanceLocationNameCookieName,
								result.getPerformanceOut().locationName);

						getView().setDefaultValues();
						getView().refreshPerformances();
					}
				});

	}

	@Override
	public void updatePerformance(String performanceKey, String date,
			String timeHourMinute, String showName, String locationName) {
		Main.logger.info("Requested performance update for " + performanceKey
				+ " with date " + date.toString() + " " + timeHourMinute
				+ " the show " + showName + " at location " + locationName
				+ " for theater " + clientState.currentTheaterKey);

		Performance p = new Performance();
		p.performanceKey = performanceKey;
		p.date = date;
		p.timeHourMinute = timeHourMinute;
		p.showName = showName;
		p.locationName = locationName;

		dispatcher.execute(new ManagePerformanceAction(
				clientState.currentTheaterKey,
				Constants.ManageActionType.UPDATE, p),
				new DispatchCallback<ManagePerformanceResult>() {
					@Override
					public void onSuccess(ManagePerformanceResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshPerformances();
					}
				});

	}

	@Override
	public void deletePerformance(String performanceKey) {
		Main.logger.info("Requested performance update for " + performanceKey);

		Performance p = new Performance();
		p.performanceKey = performanceKey;

		dispatcher.execute(new ManagePerformanceAction(
				clientState.currentTheaterKey,
				Constants.ManageActionType.DELETE, p),
				new DispatchCallback<ManagePerformanceResult>() {
					@Override
					public void onSuccess(ManagePerformanceResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshPerformances();
					}
				});

	}

}
