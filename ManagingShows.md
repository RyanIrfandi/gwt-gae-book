We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md), and implemented [managing performances schedule](ManagingPerformances.md), resulting in [a sample application](http://cultureshows.appspot.com/) with [reasonable quality](QualityAssurance.md).

In this chapter we'll implement managing theater shows.



# Mockup #

Here's how managing shows UI could look

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/shows.png' border='2' />


# Client side functionality #


**Pre-requisites**
  * [GWTP gatekeepers](http://code.google.com/p/gwt-platform/wiki/GettingStarted?tm=6#Blocking_some_presenters)


Since accessing the Shows view requires to be signed in, let's add a [gatekeeper](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/SignedInGatekeeper.java):

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/venetian_mask_female.jpg' align='right' border='0' />

```
public class SignedInGatekeeper implements Gatekeeper {

	@Inject
	private ClientState clientState;

	@Override
	public boolean canReveal() {

		if (null != clientState.userInfo) {
			if (clientState.userInfo.isSignedIn) {
				return true;
			}
		}

		return false;
	}
}
```


We'll basically duplicate [the performances package](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/) structure. Here are the steps:
  * create a `shows` package, with the same files as in `performances`, and change the class names
  * add [ShowModule](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/shows/ShowModule.java) to the [Gin Injector](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/MainGinjector.java)
  * add `SignedInGatekeeper` and `ShowPresenter` to [MainGinjector](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/MainGinjector.java)
  * add a new "shows" [name token](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/NameTokens.java)


As shows view isn't needed on the very first access of the landing page, we'll also [split its code](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html) into separate files, to be loaded by GWT on demand, with `@ProxyCodeSplit` annotation:
```
public class ShowPresenter extends
		Presenter<ShowPresenter.MyView, ShowPresenter.MyProxy> implements
		ShowUiHandlers {

	@ProxyCodeSplit
	@NameToken(NameTokens.shows)
	@UseGatekeeper(SignedInGatekeeper.class)
	public interface MyProxy extends ProxyPlace<ShowPresenter> {
	}

	...
}
```

This requires using an [AsyncProvider](http://code.google.com/p/google-gin/source/browse/trunk/src/com/google/gwt/inject/client/AsyncProvider.java) for `ShowPresenter` in [MainGinjector](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/MainGinjector.java)
```
	AsyncProvider<ShowPresenter> getShowPresenter();
```



# Server side functionality #

We'll also use the `GetPerformances` and `ManagePerformance` as a reference on server-side. To implement the `Show` equivalent of these:
  * create [Action/Result generators](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/shared/dispatch)
  * create [Action handlers](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/server/dispatch)
  * bind handlers to actions in [MainHandlerModule](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/MainHandlerModule.java)


And here's the result

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/showsApp.png' border='0' />

We have some code duplicated for Shows and Performances both for client and server. In a later chapter, we'll work on minimizing this.

**Additional resources**
  * [Elegant code splitting](http://jcheng.wordpress.com/2010/02/16/elegant-code-splitting-with-gwt/)


<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/QualityAssurance'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Quality assurance' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ExposingData'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Exposing read-only data to 3rd parties' /></a>
<a href='Hidden comment: NAV_END'></a>