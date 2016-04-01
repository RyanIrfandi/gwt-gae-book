# Internationalization #

**Pre-requisites**
  * [Developer's Guide - Internationalization](http://code.google.com/webtoolkit/doc/latest/DevGuideI18n.html)
  * [using the provided MergeLocale script](http://code.google.com/p/gwt-platform/wiki/MergeLocale).

TODO


# Data validation #
TODO Waiting for gwt 2.1.x/2.2 to finish data validation support.


# Remembering last used location #
Since location is usually the same for smaller theaters, let's save the last used location in a cookie and set it to be the default value when creating new performances.
V
```
	public void setDefaultValues() {
		date.setValue(null);
		show.setValue("");
		if (null != Cookies
				.getCookie(Constants.PerformanceLocationNameCookieName)) {
			location.setValue(Cookies
					.getCookie(Constants.PerformanceLocationNameCookieName));
		} else {
			location.setValue("");
		}
	}

	public void resetAndFocus() {
		...
		setDefaultValues();
	}
```

P
```
	public void createPerformance(Date date, String showName,
			String locationName) {
		...
								Cookies.setCookie(
										Constants.PerformanceLocationNameCookieName,
										result.getPerformanceOut().locationName);
								getView().setDefaultValues();
	}
```

# Form fields auto-completion #


# Giving users feedback on action results #
messages fade out after 3s: added, deleted, updated..



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/QualityAssurance'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Quality assurance' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/AdditionalResources'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Additional  resources' /></a>
<a href='Hidden comment: NAV_END'></a>