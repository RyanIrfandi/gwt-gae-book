We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). Having built  [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md), we'll work next on authentication for our users.



<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/vintage_keys.jpg' align='right' border='0' />

Although it's possible to develop App Engine applications with custom authentication (e.g. using your own username/password lookup, or by connecting to custom 3rd party web services), we'll focus on OpenID.

# Signing in using OpenID #
**Pre-requisites**

  * [OpenID introduction](http://en.wikipedia.org/wiki/OpenID)
  * [AppEngine Users Java API overview](http://code.google.com/appengine/docs/java/users/overview.html)
  * [Using federated authentication via OpenID](http://code.google.com/appengine/articles/openid.html)

Here are the steps to add authentication support
  1. Define [UserInfo](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/UserInfo.java) model, used to send user information from server to client
  1. Define [GetUser action/result generator](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetUser.java) and [handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/GetUserHandler.java), [bind them](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/MainHandlerModule.java). The handler contains
```
	public GetUserResult execute(GetUserAction action, ExecutionContext context)
			throws ActionException {

		UserInfo userInfo = new UserInfo();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			userInfo.isSignedIn = true;
			userInfo.signOutURL = userService.createLogoutURL(action
					.getRequestURI());
			userInfo.email = user.getEmail();
			userInfo.userId = user.getUserId();
		} else {
			userInfo.isSignedIn = false;
			userInfo.signInURLs.put("Google", userService.createLoginURL(action
					.getRequestURI(), null, "google.com/accounts/o8/id", null));
			userInfo.signInURLs.put("Yahoo", userService.createLoginURL(action
					.getRequestURI(), null, "yahoo.com", null));
		}

		return new GetUserResult("", userInfo);
}
```
  1. In [LandingPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java), get user info from server
```
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
						onGetUserSuccess();
					}
				});
	...

	public void onGetUserSuccess() {
		Main.logger.info("onGetUserSuccess: User isSignedIn "
				+ clientState.userInfo.isSignedIn.toString() + " with email "
				+ clientState.userInfo.email + " username "
				+ clientState.userInfo.userId);

		if (clientState.userInfo.isSignedIn) {
			...
		} else {
			getView().setUserInfo(clientState.userInfo);
		}
	}
```
  1. In [LandingPage](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingView.java)  have an UI for signing in
```
	private UserInfo userInfo;

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		if (!userInfo.isSignedIn) {
			signInContainer.setVisible(true);
		}
	}

	@UiHandler("googleSignIn")
	void onGoogleSignInClicked(ClickEvent event) {
		Main.logger.info("redirecting to " + userInfo.signInURLs.get("Google"));
		Window.Location.replace(userInfo.signInURLs.get("Google"));
	}

	@UiHandler("yahooSignIn")
	void onYahooSignInClicked(ClickEvent event) {
		Window.Location.replace(userInfo.signInURLs.get("Yahoo"));
	}
```

It's really that easy. The result?

_Signed out_ <br />
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/landingApp.png' border='1' />


---


_Signing in with Google_ <br />
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/signingInG.png' border='1' />


---


_Signing in with Yahoo_ <br />
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/signingInY.png' border='1' />


---



App Engine supports more OpenID providers. To keep the UI usable, we'll limit the options to the most popular two.


<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/architecture.jpg' align='right' border='0' />

# Getting user info in server handlers #
Since we'll start needing user info in multiple server handlers, we'll introduce a [Guice Provider](http://google-guice.googlecode.com/svn/trunk/javadoc/com/google/inject/Provider.html) for it: [UserInfoProvider](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/UserInfoProvider.java)
```
public class UserInfoProvider implements Provider<UserInfo> {

	public UserInfo get() {
		UserInfo userInfo = new UserInfo();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			userInfo.isSignedIn = true;
			userInfo.email = user.getEmail();
			userInfo.userId = user.getUserId();
		} else {
			userInfo.isSignedIn = false;
		}

		return userInfo;
	}
}

```

The Handler constructors will be called only once per instance, not once per request. As we need fresh user info on each request to see if the user is actually signed in or not, we'll inject the provider and get user info manually in execute() :
```
	@Inject
	public GetUserHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetUserResult execute(GetUserAction action, ExecutionContext context)
			throws ActionException {

		UserInfo userInfo = userInfoProvider.get();
		...
	}
```



<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/vis_3.jpg' align='right' border='0' />


# Custom widgets & events #
**Pre-requisites**
  * [Presenter widgets](http://code.google.com/p/gwt-platform/wiki/GettingStarted?tm=6#Presenter_widgets)
  * [Custom events with gwt-platform](http://arcbees.wordpress.com/2010/08/24/gwt-platform-event-best-practice/)

We are getting user info in [LandingPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java), but once the user info is available we need to notify other places, such as [HeaderPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/widget/HeaderPresenter.java). Our header is empty by default, but when it receives a notification with user info, it will set the appropriate content, such as username.

Therefore, let's define a [UserInfoAvailableEvent](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/event/UserInfoAvailableEvent.java) :

```
public class UserInfoAvailableEvent extends
		GwtEvent<UserInfoAvailableEvent.UserInfoAvailableHandler> {

	public interface HasUserInfoAvailableHandlers extends HasHandlers {
		HandlerRegistration addUserInfoAvailableHandler(
				UserInfoAvailableHandler handler);
	}

	public interface UserInfoAvailableHandler extends EventHandler {
		void onHasUserInfoAvailable(UserInfoAvailableEvent event);
	}

	private static Type<UserInfoAvailableHandler> TYPE = new Type<UserInfoAvailableHandler>();

	public static Type<UserInfoAvailableHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<UserInfoAvailableHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserInfoAvailableHandler handler) {
		handler.onHasUserInfoAvailable(this);
	}

	public static void fire(HasHandlers source, ClientState clientState) {
		if (TYPE != null) {
			source.fireEvent(new UserInfoAvailableEvent(clientState));
		}
	}

	private ClientState clientState;

	public UserInfoAvailableEvent(ClientState clientState) {
		this.clientState = clientState;
	}

	public ClientState getClientState() {
		return this.clientState;
	}

}
```


which will be fired by [LandingPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java) when user info is received:

```
	public void onGetUserSuccess() {
		...
		UserInfoAvailableEvent.fire(this, clientState);
		...
	}
```

Interested parties can listen to this event and handle it as desired. For example, [HeaderPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/widget/HeaderPresenter.java) widget will do
```
	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(UserInfoAvailableEvent.getType(),
				new UserInfoAvailableHandler() {
					@Override
					public void onHasUserInfoAvailable(
							UserInfoAvailableEvent event) {
						onUserInfoAvailable(event.getClientState());

					}
				});
	}

	public void onUserInfoAvailable(ClientState clientState) {
		this.clientState = clientState;
		getView().setClientState(clientState);
	}

```





<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/ClientServer'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Client-server communication' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/StoringData'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Storing entities in App Engine datastore' /></a>
<a href='Hidden comment: NAV_END'></a>