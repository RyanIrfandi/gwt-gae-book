We discussed about [putting an application ideas in writing](SoYouHaveAnIdea.md),  [setting up the development environment](GettingStarted.md), and [data modeling](DataModeling.md). Coding time!



# Mockup #

Here's how the landing page might look

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/landing.png' border='1' />

There is some reading you have to do in advance for each section below, but it's worth it.

# Page Layout #
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/grunge.jpg' align='right' border='0' />
There are two approaches to setup a page layout in GWT:
  * [using just HTML and CSS](http://www.noobcube.com/tutorials/html-css/fixed-header-footer-layout-a-beginners-guide-/), with [RootPanel](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#BasicPanels).
  * [using GWT LayoutPanels](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#LayoutPanels), with RootLayoutPanel. This approach introduces additional div tags, which contribute to tag soup and [sometimes](http://www.devcomments.com/RootPanel-vs-RootLayoutPanel-at197454.htm) prevent [proper firing of events](http://groups.google.com/group/google-web-toolkit/browse_thread/thread/c8cfc4d2dba16862#) on some elements. See also [using GWT for layout](http://www.zackgrossbart.com/hackito/antiptrn-gwt2/) (controversial).

We'll go with the first, standards-based approach. You will use your existing CSS knowledge, or learn it now if needed. CSS will benefit you when developing any web application, not just GWT-based ones.



**Pre-requisites**
  * [Build User Interfaces](http://code.google.com/webtoolkit/doc/latest/DevGuideUi.html), including [Declarative Layout with UiBinder](http://code.google.com/webtoolkit/doc/latest/DevGuideUiBinder.html)
  * [Widget List](http://code.google.com/webtoolkit/doc/latest/RefWidgetGallery.html)
  * [GWT showcase](http://gwt.google.com/samples/Showcase/Showcase.html) to see some widgets in action
  * [GWT API Reference](http://code.google.com/webtoolkit/doc/latest/RefGWTClassAPI.html) - bookmark this and keep it handy
  * [CSS Tutorial](http://www.w3schools.com/css/default.asp)
  * [CSS Tips & Tricks](http://csstipsandtricks.com)

First, let's add [an image](http://code.google.com/p/gwt-gae-book/source/browse/#svn/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/resources) in [Resources](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/resources/Resources.java)
```
	@Source("Theatre_Masks.png")
	ImageResource logo();
```


# Exercise: build the mocked up UI #
Create an [UiBinder](http://code.google.com/webtoolkit/doc/latest/DevGuideUiBinder.html) .ui.xml file and try to implement the mocked up UI. No need to implement any Java logic yet, just focus on widgets and styling. Continue only when you have a draft version done by yourself.

# Implementation #


[LandingView.ui.xml](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingView.ui.xml)

```
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	xmlns:g="urn:import:com.google.gwt.user.client.ui"
	xmlns:gcell="urn:import:com.google.gwt.user.cellview.client"
	xmlns:gdp="urn:import:com.google.gwt.user.datepicker.client"
	ui:generateFormat='com.google.gwt.i18n.rebind.format.PropertiesFormat'
	ui:generateKeys="com.google.gwt.i18n.rebind.keygen.MD5KeyGenerator"
	ui:generateLocales="default"
	>

	<ui:with type="org.gwtgaebook.CultureShows.client.NameTokens" field="nameTokens" />
	<ui:with field='res' type='org.gwtgaebook.CultureShows.client.resources.Resources' />


	<ui:image field="google" src="../resources/google.png" />
	<ui:image field="yahoo" src="../resources/yahoo.png" />


	<ui:style>

	.sideNavContent {
		float: right;
		width: 25%;
		margin-top: 1em;
		padding: 1em 1em;
		word-wrap: break-word;

		background-color: #e8f0de;
		-webkit-border-radius: .5em;
		-moz-border-radius: .5em;
		border-radius: .5em;
		-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
		-moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
		box-shadow: 0 1px 2px rgba(0,0,0,.2);
	}


	.specificContent {
		float: left;
		width: 65%;
		padding: 3em 0 0 1em;
	}



	.specificContent::after {
		clear: both;
		display: block;
	}

	.center {
	    margin: 1em auto;
	    text-align: center;

	}

	.provider {
		cursor: pointer;
	    margin: 1em auto;
		display: block;
		-webkit-border-radius: .5em;
		-moz-border-radius: .5em;
		border-radius: .5em;
		-webkit-box-shadow: 0 1px 2px rgba(0,0,0,.2);
		-moz-box-shadow: 0 1px 2px rgba(0,0,0,.2);
		box-shadow: 0 1px 2px rgba(0,0,0,.2);
		border: solid 1px #538312;
		/* TODO padding doesn't work, image is always top-left */
	}

	</ui:style>

    <g:HTMLPanel ui:field="container">

	    <g:HTMLPanel addStyleNames="{style.specificContent}">
		Schedule theater performances and other events, and easily embed them on your website.

		<ul>
			<li>real time updates: your website reflects any changes automatically</li>
			<li>be independent from other persons: no need to ask somebody else to publish your updates</li>
		</ul>
	    </g:HTMLPanel>

	    <g:HTMLPanel ui:field="signInContainer" addStyleNames="{style.sideNavContent}" visible="false">
			<g:HTMLPanel addStyleNames="{style.center}">
			<b>Sign in</b><br/>with your existing account
				<g:Image ui:field="googleSignIn" altText="Google" addStyleNames="{style.provider}" resource="{google}" />
				<g:Image ui:field="yahooSignIn" altText="Yahoo" addStyleNames="{style.provider}" resource="{yahoo}" />
			</g:HTMLPanel>

			<g:HTMLPanel addStyleNames="{style.center}">
				<a href="https://www.google.com/accounts/NewAccount" target="_blank">Create an account</a>
			</g:HTMLPanel>

		</g:HTMLPanel>

    </g:HTMLPanel>

</ui:UiBinder>
```


We also added various styles in  [main.css](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/resources/main.css) , to be used in all application, and listed style names in  [Resources.java](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/resources/Resources.java)

Here's the result :)

![http://gwt-gae-book.googlecode.com/svn/wiki/assets/landingApp.png](http://gwt-gae-book.googlecode.com/svn/wiki/assets/landingApp.png)

As with any other section, make sure you understand what's happening before going further. Use the provided links to docs and Google to search for something else.


# Using native JavaScript #

There are cases when you might want to use native JavaScript rather than pure GWT, such as the one above. See [JavaScript Native Interface](http://code.google.com/webtoolkit/doc/latest/DevGuideCodingBasicsJSNI.html) and an example of  [building custom GWT widgets which include native JavaScript](http://www.zackgrossbart.com/hackito/antiptrn-gwt2/). JSNI is a good choice when you want to reuse existing larger JavaScript functionality (e.g. jQuery plugins) with easy upgrades to new versions, or sharing your code with other pure JavaScript users. Note that using native JavaScript will prevent you from using pure JUnit test cases and you will need to use GWTTestCase, which is slower. Usage of native JavaScript should be minimized whenever possible.

If you are looking for jQuery-like functionality in GWT, there's [gwtQuery](http://code.google.com/p/gwtquery/) ([video introduction](http://www.youtube.com/watch?v=sl5em1UPuoI)). gwtQuery is a better choice when you're writing functionality from scratch as it gives you all GWT advantages over native JavaScript.

Again, use the best tool for the job. Therefore an important criteria selection for your tool set should be allowing easy inter-operating with other tools.

An early version of the Landing page used a !jQuery HTML5 placeholder plugin for form fields. [main.html](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/war/main.html?r=372) contained the jQuery plugin, and [the view](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/landing/LandingView.java?r=184) added HTML5 placeholder attributes and activates jQuery plugin via [JSNI](http://code.google.com/webtoolkit/doc/latest/DevGuideCodingBasicsJSNI.html). This functionality was later removed.


# Mobile, tablets, TV and other devices #

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/quijote1.jpg' align='right' border='0' />

Sometimes, you might want to have different views for different screen sizes. The styling above is liquid (resize browser window to try it out) and it works for now. We could have used [CSS3 media queries](http://www.webdesignerwall.com/tutorials/css3-media-queries/) to hide completely some information on small screens. Media queries support [is still missing](http://groups.google.com/group/google-web-toolkit/browse_thread/thread/86047ca5c364679f/2871b1e5e35be0d1) from GWT.


# Model-View-Presenter (MVP) #

Let's learn about

  * [MVP architecture](http://code.google.com/webtoolkit/articles/mvp-architecture.html)
  * [Getting started with gwt-platform](http://code.google.com/p/gwt-platform/wiki/GettingStarted)
  * [GWT MVP Framework](http://code.google.com/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html)
  * [Comparison of gwt-platform with GWT MVP](http://code.google.com/p/gwt-platform/wiki/ComparisonWithGwtMVP)
  * [GUI Architectures](http://martinfowler.com/eaaDev/uiArchs.html)
  * [Guice dependency injection framework](http://code.google.com/p/google-guice/)
  * [Gin dependency injection for GWT](http://code.google.com/p/google-gin/wiki/GinTutorial)
  * [Reversing the MVP pattern and using @UiHandler](http://arcbees.wordpress.com/2010/09/03/reversing-the-mvp-pattern-and-using-uihandler/)
  * [UiHandlers and supervising controller](http://arcbees.wordpress.com/2010/09/18/uihandlers-and-supervising-controlers/)

Think of UiHandlers as a contract signed by Presenter: this is what Presenter has to offer, usable by others (usually Views). The Presenter also requires Views to offer something:
```
	public interface MyView extends View, HasUiHandlers<LandingUiHandlers> {
		public void setUserInfo(UserInfo userInfo);
	}
```
You can switch different Presenter and View implementations without any worries as long as they respect these contracts.


Note we are using [nested presenters](http://code.google.com/p/gwt-platform/wiki/GettingStarted?tm=6#Nested_presenters) :
  * [Main](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/) Presenter/View contains a `pageContent` placeholder, besides the header
  * [Page](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/client/page) Presenter/View provides the logic/content for the `pageContent` placeholder, while leaving a `specificContent` widget to be filled in by other presenters
  * [Landing](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/client/landing) Presenter/View is an example of `specificContent`


## Exercise ##
Using what you learned above, create a new "performances" package, with [a View and a Presenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/client/performances), and start implementing saving a performance when "Schedule performance" button is clicked. For now, it should just log a message in Presenter with the date, location and show name.

## Exercise solution ##

In [PerformanceUiHandlers](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceUiHandlers.java), the Presenter commits to offer some functionality:
```
public interface PerformanceUiHandlers extends UiHandlers {
	public void createPerformance(Date date, String showName,
			String locationName);
}
```

After you save this, notice there is an error in PerformancePresenter:
```
The type PerformancePresenter must implement the inherited abstract method
PerformanceUiHandlers.createPerformance(Date, String, String)
```

Let's actually keep our commitment and implement this:
```
	@Override
	public void createPerformance(Date date, String showName,
			String locationName) {
		Main.logger.info("Requested performance scheduling on "
				+ date.toString() + ": show " + showName + " at location "
				+ locationName);
	}
```

Here's how it's used by the View
```
	@UiField
	TextBox show;
	@UiField
	TextBox location;
	@UiField
	DateBox date;

	@UiHandler("createPerformance")
	void onCreatePerformanceClicked(ClickEvent event) {
		getUiHandlers().createPerformance(date.getValue(), show.getValue(),
				location.getValue());
	}
```

Run and test this functionality. In Development Mode Eclipse tab you'll see
```
... [INFO ] Requested performance scheduling on Fri Oct 22 12:00:00 EEST 2010: show Dream at location Rapsodia
```

Note there are no widgets or other UI elements from View exposed in Presenter. The Presenter should not care about UI elements, only about data the View exposes. You can have different Views for different devices, or mocked Views for unit tests.

[Browse the full source code](http://code.google.com/p/gwt-gae-book/source/browse/#svn/trunk/CultureShows)

# Additional resources #
  * [Open Clip Art Library](http://www.openclipart.org)

  * [Stock.XCHNG free stock photos](http://www.sxc.hu/)

  * [Smashing Magazine](http://www.smashingmagazine.com)

  * [Fluid grids](http://www.alistapart.com/articles/fluidgrids/)

  * [Gradient buttons](http://www.webdesignerwall.com/tutorials/css3-gradient-buttons/)

  * [CSS Zen Garden resources](http://www.mezzoblue.com/zengarden/resources/)

  * [Color combos](http://www.colorcombos.com/combolibrary.html)

  * [How To Use CSS3 Media Queries To Create a Mobile Version of Your Website](http://www.smashingmagazine.com/2010/07/19/how-to-use-css3-media-queries-to-create-a-mobile-version-of-your-website/)

<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/DataModeling'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Data modeling' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ClientServer'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Client-server communication' /></a>
<a href='Hidden comment: NAV_END'></a>