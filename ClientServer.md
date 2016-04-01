We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md),  [setting up the development environment](GettingStarted.md), and [just built a static landing page](BuildingLandingPage.md). Let's see how the landing page will interact with the server back-end.



When users initially access our application, we'd like the GWT client to check with App Engine back-end if they are signed in or not.

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/chair_theater.jpg' align='right' border='0' />

# Pre-requisites #

  * [Google App Engine introduction](http://code.google.com/appengine/docs/whatisgoogleappengine.html)
  * [Client-server communication](http://code.google.com/webtoolkit/doc/latest/DevGuideServerCommunication.html)
  * [Client-server communication with gwt-platform](http://code.google.com/p/gwt-platform/wiki/GettingStartedDispatch)

# Exercise #
Using what you learned above, start implementing checking if the current user is signed in or not. Pass the current application URI from client to server and write a `GetUserSampleHandler` which just logs a message for now and returns a String.

# Exercise solution #

Action/Result pair

[GetUserSampleAction](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetUserSampleAction.java)
```
public class GetUserSampleAction extends
		UnsecuredActionImpl<GetUserSampleResult> {

	private String requestURI;

	public GetUserSampleAction(final String requestURI) {
		this.requestURI = requestURI;
	}

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private GetUserSampleAction() {
	}

	public String getRequestURI() {
		return requestURI;
	}

}
```

[GetUserSampleResult](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetUserSampleResult.java)
```
public class GetUserSampleResult implements Result {

	private String response;

	public GetUserSampleResult(final String response) {
		this.response = response;
	}

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private GetUserSampleResult() {
	}

	public String getResponse() {
		return response;
	}

}
```


The server handler, which receives an `Action` and sends back a `Result`

[GetUserSampleHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/GetUserSampleHandler.java)
```
public class GetUserSampleHandler implements
		ActionHandler<GetUserSampleAction, GetUserSampleResult> {

	@Override
	public GetUserSampleResult execute(GetUserSampleAction action,
			ExecutionContext context) throws ActionException {

		System.out.println("Client URI: " + action.getRequestURI());

		return new GetUserSampleResult("not signed in");
	}

	@Override
	public Class<GetUserSampleAction> getActionType() {
		return GetUserSampleAction.class;
	}

	@Override
	public void undo(GetUserSampleAction action, GetUserSampleResult result,
			ExecutionContext context) throws ActionException {
		// Not undoable
	}

}
```

Bindings in
[MainHandlerModule](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/MainHandlerModule.java)
```
...
		// bind Actions to ActionHandlers and ActionValidators
		bindHandler(GetUserSampleAction.class, GetUserSampleHandler.class);
```

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/made1.jpg' align='middle' border='0' />

And the client call in [LandingPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java)
```
...
       private final DispatchAsync dispatcher;

	@Inject
	public LandingPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			final PlaceManager placeManager, DispatchAsync dispatcher,
			final ClientState clientState) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);

		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.clientState = clientState;
	}

	@Override
	protected void onBind() {
		super.onBind();

		dispatcher.execute(new GetUserSampleAction(Window.Location.getHref()),
				new AsyncCallback<GetUserSampleResult>() {

					@Override
					public void onFailure(Throwable caught) {
						Main.logger.severe("GetUserSample call failed "
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(GetUserSampleResult result) {
						Main.logger.info("GetUserSample result: "
								+ result.getResponse());
					}
				});

	}
```

When running this, the server will log
```
INFO: GetUserSample result: 
Client URI: http://127.0.0.1:8888/main.html?gwt.codesvr=127.0.0.1:9997#!landing
```

and the client will log in browser console
```
INFO: GetUserSample result: not signed in
```

It works, but it's a lot of code to write just to get a string from client to server, isn't it?

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/elements_2.jpg' align='right' border='0' />

# Auto-generated Action and Result #
**Pre-requisites**
  * [Configure your build environment](http://code.google.com/p/gwt-platform/wiki/BoilerplateGeneration#Configuring_your_build_environment) for annotation processing.
  * [Generating Action and Result](http://code.google.com/p/gwt-platform/wiki/BoilerplateGeneration#Generate_Action_and_Result)

Instead of `Action` and `Result`, we can have only a [GetUser](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetUser.java) generator
```
@GenDispatch(isSecure = false)
public class GetUser {
	@In(1)
	String requestURI;

	@Out(1)
	String response;
}
```

# More code cleanup #
To further reduce the code written for each specific request, we can have an `AsyncCallback` wrapper in the form of [DispatchCallback](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/DispatchCallback.java). For now, it can just provide error handling, so we don't have to do that for every call.

Using it, the updated [LandingPresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java) looks like this:
```
		dispatcher.execute(new GetUserSampleAction(Window.Location.getHref()),
				new DispatchCallback<GetUserSampleResult>() {

					@Override
					public void onSuccess(GetUserSampleResult result) {
						Main.logger.info("GetUserSample result: "
								+ result.getResponse());
					}
				});
```


We can also have a [wrapper for ActionHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/DispatchActionHandler.java). Extending it, our server handler might become
```
public class GetUserSampleHandler extends
		DispatchActionHandler<GetUserSampleAction, GetUserSampleResult> {

	@Override
	public GetUserSampleResult execute(GetUserSampleAction action,
			ExecutionContext context) throws ActionException {

		System.out.println("Client URI: " + action.getRequestURI());

		return new GetUserSampleResult("not signed in");
	}

}
```


# Summary #
To make a call from client to server, you need these parts

  * [generator for Action and Result data](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetUser.java)
  * [server handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/GetUserSampleHandler.java)
  * [binding of Action to ActionHandler and optional ActionValidator](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/MainHandlerModule.java)
  * [the call itself](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingPresenter.java), using `dispatcher.execute()`



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/BuildingLandingPage'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Building the landing page' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/Authentication'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Authentication with OpenID' /></a>
<a href='Hidden comment: NAV_END'></a>