package org.gwtgaebook.CultureShows.client;

import java.util.Iterator;

import com.allen_sauer.gwt.log.client.*;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.*;
import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;
import com.gwtplatform.dispatch.client.*;

import org.gwtgaebook.CultureShows.client.*;
import org.gwtgaebook.CultureShows.client.event.*;
import org.gwtgaebook.CultureShows.client.event.SignInEvent.SignInHandler;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent.UserInfoAvailableHandler;
import org.gwtgaebook.CultureShows.client.util.*;
import org.gwtgaebook.CultureShows.client.widget.*;
import org.gwtgaebook.CultureShows.shared.*;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> implements
		MainUiHandlers {

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<MainUiHandlers> {
	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	public static final Object TYPE_RevealHeaderContent = new Object();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_RevealMainContent = new Type<RevealContentHandler<?>>();

	private final HeaderPresenter headerPresenter;

	private final SignInPresenter signInPresenter;

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager,
			final DispatchAsync dispatcher,
			final HeaderPresenter headerPresenter,
			final SignInPresenter signInPresenter, final ClientState clientState) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.headerPresenter = headerPresenter;
		this.signInPresenter = signInPresenter;
		this.clientState = clientState;

		getView().setUiHandlers(this);

	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	public void onReveal() {
		super.onReveal();

		setInSlot(TYPE_RevealHeaderContent, headerPresenter);
	}

	@Override
	protected void onBind() {
		super.onBind();

		dispatcher.execute(new GetUserAction(Window.Location.getHref()),
				new DispatchCallback<GetUserResult>() {
					@Override
					public void onSuccess(GetUserResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						clientState.userInfo = result.getUserInfo();
						clientState.theatersMap = result.getTheatersMap();

						if (clientState.theatersMap.size() > 0) {
							Iterator<String> it = clientState.theatersMap
									.keySet().iterator();
							if (it.hasNext()) {
								clientState.currentTheaterKey = it.next();
							}
						}
						// TODO testability broken if relying to global static
						// UserInfo
						onGetUserSuccess();
					}
				});

		addRegisteredHandler(SignInEvent.getType(), new SignInHandler() {
			@Override
			public void onHasSignIn(SignInEvent event) {
				showSignInDialog();

			}
		});

	}

	public void onGetUserSuccess() {
		// this.userInfo = userInfo;
		Log.info("User isSignedIn "
				+ clientState.userInfo.isSignedIn.toString() + " with email "
				+ clientState.userInfo.email + " username "
				+ clientState.userInfo.userId);
		Log.info("Sign In URLs " + clientState.userInfo.signInURLs.toString()
				+ " Sign Out URL " + clientState.userInfo.signOutURL);
		// , UserInfouserInfo
		UserInfoAvailableEvent.fire(this, clientState.userInfo);
	}

	public void showSignInDialog() {
		signInPresenter.setUserInfo(clientState.userInfo);
		RevealRootPopupContentEvent.fire(this, signInPresenter);
	}

}
