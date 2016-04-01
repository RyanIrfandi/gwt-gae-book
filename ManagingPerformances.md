We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md). We also covered [storing](StoringData.md) and [listing](ListingSchedule.md) performances schedule. In this chapter, we'll look at updating and deleting performances, in order to have a complete [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete).




# Server handler #
Let's update [ManagePerformanceHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java) to include update and create functionality:
```
	public ManagePerformanceResult execute(ManagePerformanceAction action,
			ExecutionContext context) throws ActionException {
		...
		switch (action.getActionType()) {
		case UPDATE:
			performanceKey = Validation
					.getValidDSKey(action.getPerformance().performanceKey);

			// check performance belongs to given theaterKey
			if (!KeyFactory.keyToString(performanceKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManagePerformanceResult(
						"Performance doesn't belong to given theater", null);
			}

			performance = performanceDAO.read(performanceKey);

			show = createOrReadShow(theater, action.getPerformance().showName);
			location = createOrReadLocation(theater,
					action.getPerformance().locationName);

			// set/update performance data
			performance.date = action.getPerformance().date;
			performance.showKey = KeyFactory.keyToString(showDAO.getKey(show));
			performance.locationKey = KeyFactory.keyToString(locationDAO
					.getKey(location));

			performance.showName = show.getName();
			performance.showWebsiteURL = show.websiteURL;
			performance.locationName = location.getName();

			performanceDAO.update(performance, performanceKey);

			break;

		case DELETE:
			performanceKey = Validation
					.getValidDSKey(action.getPerformance().performanceKey);

			// check performance belongs to given theaterKey
			if (!KeyFactory.keyToString(performanceKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManagePerformanceResult(
						"Performance doesn't belong to given theater", null);
			}
			performanceDAO.delete(performanceKey);
			return new ManagePerformanceResult("", null);
		...
		}

		performance.performanceKey = KeyFactory.keyToString(performanceKey);

		return new ManagePerformanceResult("", performance);
	}
```



# Client view #
The Performances page has two buttons, initially hidden

[PerformanceView.ui.xml](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.ui.xml)
```
<g:Button ui:field="updatePerformance" visible="false" 
    addStyleNames="{res.style.button} {res.style.blue} {res.style.small}"
    >Update</g:Button>
<g:Button ui:field="deletePerformance" visible="false" 
    addStyleNames="{res.style.button} {res.style.blue} {res.style.small}"
    >Delete</g:Button>
```

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/theatre_and_performance.jpg' align='right' border='0' />

The view shows/hides these when a performance is selected, and calls the appropriate presenter methods

[PerformanceView](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.java)
```
	@UiField
	Button updatePerformance;
	@UiField
	Button deletePerformance;
	...
	@Override
	public void setDefaultValues() {
		date.setValue(null);
		show.setValue("");
		location.setValue("");
	}

	public void resetAndFocus() {
		setDefaultValues();
	}
	...

	public void setIsPerformanceSelected(Boolean selected) {
		updatePerformance.setVisible(selected);
		deletePerformance.setVisible(selected);
		if (!selected) {
			setDefaultValues();
		}
	}

	@Override
	public void loadPerformanceData(Integer start, Integer length,
			List<Performance> performances) {
		setIsPerformanceSelected(false);
		...
	}

	@Override
	public void refreshPerformances() {
		setIsPerformanceSelected(false);
		...
	}

	private void setSelectionModel(CellList<Performance> cl) {
		final SingleSelectionModel<Performance> selectionModel = 
			new SingleSelectionModel<Performance>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Performance p = selectionModel.getSelectedObject();
						setIsPerformanceSelected(null != p);
						if (null != p) {
							date.setValue(p.date);
							show.setValue(p.showName);
							location.setValue(p.locationName);
						}
						getUiHandlers().onPerformanceSelected(p);
					}
				});

		cl.setSelectionModel(selectionModel);
	}
	...
	@UiHandler("updatePerformance")
	void onUpdatePerformanceClicked(ClickEvent event) {
		final SingleSelectionModel<Performance> selectionModel =
			(SingleSelectionModel<Performance>) performancesCL.getSelectionModel();
		getUiHandlers().updatePerformance(
				selectionModel.getSelectedObject().performanceKey,
				date.getValue(), show.getValue(), location.getValue());
	}

	@UiHandler("deletePerformance")
	void onDeletePerformanceClicked(ClickEvent event) {
		final SingleSelectionModel<Performance> selectionModel =
			 (SingleSelectionModel<Performance>) performancesCL.getSelectionModel();
		getUiHandlers().deletePerformance(
				selectionModel.getSelectedObject().performanceKey);
	}

```

# Client presenter #

The presenter will handle the create, update and delete actions.

[PerformancePresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformancePresenter.java)
```
	@Override
	public void updatePerformance(String performanceKey, Date date,
			String showName, String locationName) {
		Main.logger.info("Requested performance update for " + performanceKey
				+ " with date " + date.toString() + " the show " + showName
				+ " at location " + locationName + " for theater "
				+ clientState.currentTheaterKey);

		Performance p = new Performance();
		p.performanceKey = performanceKey;
		p.date = date;
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
```

Next, we'll look at how can we increase the quality of what we've got so far.


<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/ListingSchedule'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Listing saved performances' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/QualityAssurance'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Quality assurance' /></a>
<a href='Hidden comment: NAV_END'></a>