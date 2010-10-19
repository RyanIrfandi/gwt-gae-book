package org.gwtgaebook.CultureShows.client;

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
import org.gwtgaebook.CultureShows.client.util.*;
import org.gwtgaebook.CultureShows.client.widget.HeaderPresenter;
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
	private UserInfo userInfo;

	public static final Object TYPE_RevealHeaderContent = new Object();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_RevealMainContent = new Type<RevealContentHandler<?>>();

	private final HeaderPresenter headerPresenter;

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager,
			final DispatchAsync dispatcher, UserInfo userInfo,
			final HeaderPresenter headerPresenter) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.userInfo = userInfo;
		this.headerPresenter = headerPresenter;
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
						setUserInfo(result.getUserInfo());
					}
				});

		// set a random user token to emulate signed in functionality
		if (null == Cookies.getCookie(Constants.userTokenCookieName)
				|| Cookies.getCookie(Constants.userTokenCookieName).isEmpty()) {
			Cookies.setCookie(Constants.userTokenCookieName, MyUUID.uuid());
		}

	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		Log.info("User isSignedIn " + userInfo.isSignedIn.toString()
				+ " with email " + userInfo.email + " username "
				+ userInfo.userId);
		Log.info("Sign In URLs " + userInfo.signInURLs.toString()
				+ " Sign Out URL " + userInfo.signOutURL);
		UserInfoAvailableEvent.fire(this, userInfo);
	}
}
