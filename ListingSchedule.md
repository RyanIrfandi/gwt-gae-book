We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md). Having implemented [storing performances schedule](StoringData.md), we'll cover next how to list the saved performances.




Until now, we used the local datastore viewer to see what data is saved. Well, it's time to let Violeta see it too.

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/rencontres_1.jpg' align='right' border='0' />

# A quick, basic solution #
Our basic need is to have a client doing [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations on Performance entity items. We just covered Create, moving on to Read now.

In order to quickly get the latest performances and display them on client, we can
  1. Get all items when page loads
  1. When adding/updating/deleting items, read again all performances from server and refresh the display

## Exercise ##
Implement listing added performances. Hints:
  * Remember [client-server communication](ClientServer#Summary.md)
  * No need to check access, performances are public
  * Use a HTMLPanel to display data. We'll enhance this later

## Exercise solution ##
Add [GetPerformances](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/dispatch/GetPerformances.java) Action/Result generator.
```
public class GetPerformances {
	@In(1)
	String theaterKey;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	List<Performance> performances = new ArrayList<Performance>();
}
```

Add [GetPerformancesHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/GetPerformancesHandler.java)
```
public class GetPerformancesHandler extends
		DispatchActionHandler<GetPerformancesAction, GetPerformancesResult> {

	@Inject
	PerformanceDAO performanceDAO;

	@Inject
	public GetPerformancesHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetPerformancesResult execute(GetPerformancesAction action,
			ExecutionContext context) throws ActionException {

		// no need to check access, performances are public

		List<Performance> performances = performanceDAO.readByTheater(action
				.getTheaterKey());

		return new GetPerformancesResult("", performances);
	}
}
```


Enhance [PerformanceView](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.java) to display performances...
```
	@UiField
	HTML performancesContainer;

	public void addPerformance(Performance p) {
		performancesContainer.setHTML(performancesContainer.getHTML() + "<br/>"
				+ p.showName + " | " + p.locationName + " | "
				+ p.date.toString());

	}

	public void setPerformances(List<Performance> performances) {
		performancesContainer.setHTML("Show | Location | Date");

		for (Performance p : performances) {
			addPerformance(p);
		}

	}
```

...as instructed by [PerformancePresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformancePresenter.java)
```
	public void requestPerformances() {
		// Strings.isNullOrEmpty(clientState.currentTheaterKey)
		if (!(null == clientState.currentTheaterKey || clientState.currentTheaterKey
				.isEmpty())) {
			dispatcher.execute(new GetPerformancesAction(
					clientState.currentTheaterKey),
					new DispatchCallback<GetPerformancesResult>() {
						@Override
						public void onSuccess(GetPerformancesResult result) {
							getView().setPerformances(
									result.getPerformances());
						}
					});
		}

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, PagePresenter.TYPE_RevealSpecificContent,
				this);
		requestPerformances();
	}

```

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/made_in_romania7.jpg' align='right' border='0' />

# Advanced use cases #

(see photo on the right for another advanced use case :-)

For the sake of example, let's consider some use cases which aren't really needed by Culture Shows, but might make sense in a larger app:
  * Working with millions of entity items
  * Pagination (either in the form of prev/next pages or [continuous](http://ui-patterns.com/patterns/ContinuousScrolling) / [infinite](http://www.infinite-scroll.com/) scrolling)
  * Sorting
  * Filtering
  * Collaboration (changes made by others should appear real-time to current user)
  * Offline usage

# Exercise #

Think what are the issues of the basic solution and how could it be enhanced to support the use cases above.

# Enhancing the basic solution #

Luckily, GWT team already thought about these use cases and introduced [Data Presentation Widgets](http://code.google.com/webtoolkit/doc/latest/DevGuideUiCellWidgets.html). Go ahead and learn about them.

Ready? As a very first step, let's add a [Key Provider](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/view/client/ProvidesKey.html) to [Performance](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/Performance.java) data model:
```
public class Performance implements Serializable {
	public static final ProvidesKey<Performance> KEY_PROVIDER = new ProvidesKey<Performance>() {
		public Object getKey(Performance p) {
			return (null == p) ? null : p.performanceKey;
		}
	};

	@Store(false)
	public String performanceKey;

	...
```

Note that `performanceKey` won't be stored, we'll just use it to send keys to client so we can uniquely identify entities later. The [PerformanceDAO](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dao/PerformanceDAO.java) sets the key property:

```
	public List<Performance> readByTheater(String theaterKey) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		Theater theater = theaterDAO.read(theaterKey);

		// by default, get only future performances
		Date date = new java.util.Date();
		List<Performance> performances = datastore.find()
				.type(Performance.class).ancestor(theater)
				.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, date)
				.addSort("date").returnAll().now();

		// add key to model, so it can be sent to client
		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.performanceKey = KeyFactory.keyToString(getKey(p));
			performances.set(i, p);
		}

		return performances;
	}
```


Next, add a [CellList](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/user/cellview/client/CellList.html) widget to [PerformanceView.ui.xml](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.ui.xml)
```
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	xmlns:gcell="urn:import:com.google.gwt.user.cellview.client"
	...
	>

		...
		<g:HTMLPanel addStyleNames="{res.style.listing}">
			<h4>Scheduled performances</h4>
	    	<gcell:CellList ui:field="performancesCL" />
		</g:HTMLPanel>

```

and bring it to life with [PerformanceView](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.java)...
```
	...
	public class PerformanceCell extends AbstractCell<Performance> {

		@Override
		public void render(Context context, Performance performance,
				SafeHtmlBuilder sb) {

			if (null == performance) {
				return;
			}

			DateTimeFormat dateFormat = DateTimeFormat
					.getFormat(Constants.defaultDateFormat);

			sb.appendHtmlConstant("<span class='performanceDate'>");
			sb.appendEscaped(dateFormat.format(performance.date));
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='showName'>");
			sb.appendEscaped(performance.showName);
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("<span class='locationName'>");
			sb.appendEscaped(performance.locationName);
			sb.appendHtmlConstant("</span>");
		}
	}

	protected class PerformancesAsyncAdapter extends
			AsyncDataProvider<Performance> {
		@Override
		protected void onRangeChanged(HasData<Performance> display) {
			if (getUiHandlers() != null) {
				Range newRange = display.getVisibleRange();
				getUiHandlers().onRangeOrSizeChanged(newRange.getStart(),
						newRange.getLength());
			}
		}
	}

	interface PerformancesResources extends CellList.Resources {
		@Source(value = { CellList.Style.DEFAULT_CSS, "../resources/cell.css" })
		CellList.Style cellListStyle();
	}

	private final PerformancesAsyncAdapter performancesAsyncAdapter;

	@UiField
	CellList<Performance> performancesCL;


	public PerformanceView() {
		...
		performancesCL.setVisibleRange(Constants.visibleRangeStart,
				Constants.visibleRangeLength);
		performancesAsyncAdapter = new PerformancesAsyncAdapter();
		performancesAsyncAdapter.addDataDisplay(performancesCL);

	}

	...

	@UiFactory
	CellList<Performance> createPerformanceCL() {
		PerformanceCell performanceCell = new PerformanceCell();
		CellList<Performance> cl = new CellList<Performance>(performanceCell,
				Performance.KEY_PROVIDER);

		return cl;
	}

	@Override
	public void loadPerformanceData(Integer start, Integer length,
			List<Performance> performances) {
		performancesAsyncAdapter.updateRowData(start, performances);
		performancesAsyncAdapter.updateRowCount(length, false);
		performancesCL.setVisibleRange(start, Constants.visibleRangeLength);
		performancesCL.redraw();
	}

	@Override
	public void refreshPerformances() {
		getUiHandlers().onRangeOrSizeChanged(
				performancesCL.getVisibleRange().getStart(),
				performancesCL.getVisibleRange().getLength());
	}

```

...and [PerformancePresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformancePresenter.java)
```

	public void requestPerformances() {
	...
							getView().loadPerformanceData(
									Constants.visibleRangeStart,
									result.getPerformances().size(),
									result.getPerformances());
	...
	}

	public void createPerformance(Date date, String showName,
			String locationName) {
		...
					public void onSuccess(ManagePerformanceResult result) {
						...
						getView().refreshPerformances();
					}
		...
	}


	@Override
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength) {
		// usually, this should have requested a new set of data from server for
		// visible range. Not needed on upcoming performances, just fetch them
		// all
		requestPerformances();
	}

```

Try it out! Go through [examples](http://code.google.com/webtoolkit/doc/latest/DevGuideUiCellWidgets.html) and  [GWT API docs](http://code.google.com/webtoolkit/doc/latest/RefGWTClassAPI.html) to make sure you understand everything we just did.

# Handling item selection #

Later, we'll also need to select a performance from the list, in order to update or delete it. We might as well implement selection now, so we can also test the performance key availability.

In [PerformanceView](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.java) add a [SingleSelectionModel](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/view/client/SingleSelectionModel.html)
```

	@UiFactory
	CellList<Performance> createPerformanceCL() {
		...
		setSelectionModel(cl);

		return cl;
	}

	private void setSelectionModel(CellList<Performance> cl) {
		final SingleSelectionModel<Performance> selectionModel = 
			new SingleSelectionModel<Performance>();

		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						Performance p = selectionModel.getSelectedObject();

						getUiHandlers().onPerformanceSelected(p);
					}
				});

		cl.setSelectionModel(selectionModel);
	}

```

and in [PerformancePresenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformancePresenter.java) just log this for now
```
	public void onPerformanceSelected(Performance p) {
		Main.logger.info("Selected performance " + p.performanceKey
				+ " with show " + p.showName);
	}
```

We'll leave pagination, sorting, filtering, collaboration, offline usage to be implemented in later chapters. We have a good setup now, ready to be built upon.

# Styling data presentation widgets #

Let's overwrite the default styling by extending [CellList.Resources](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/user/cellview/client/CellList.Resources.html) with a modified copy of [CellList.css](http://code.google.com/p/google-web-toolkit/source/browse/releases/2.1/user/src/com/google/gwt/user/cellview/client/CellList.css), then pass it to `CellList` constructor in [PerformanceView](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformanceView.java):
```
	interface PerformancesResources extends CellList.Resources {
		@Source(value = { CellList.Style.DEFAULT_CSS, "../resources/cell.css" })
		CellList.Style cellListStyle();
	}

	CellList<Performance> createPerformanceCL() {
		...
		CellList<Performance> cl = new CellList<Performance>(
				performanceCell,
				GWT.<PerformancesResources> create(PerformancesResources.class),
				Performance.KEY_PROVIDER);
		...

	}
```

And here's the result

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/performancesAppListing.png' />

TODO: Would anybody please contribute an implementation which avoids re-reading all performances on each create/update/delete? Consider
  * filtering/sorting/paginating - the newly added entity might not be in the current view
  * other users might simultaneously add data - how to reflect this? reread only current page view or server push (channel API, comet...) ?




# Additional resources #

  * Learn more about [Cell styling](http://groups.google.com/group/google-web-toolkit/browse_thread/thread/6c17bb1825685564)
  * [Sample app with cell styling](http://code.google.com/p/google-web-toolkit/source/browse/trunk/samples/expenses/#expenses/src/main/java/com/google/gwt/sample/expenses/client%3Fstate%3Dclosed)


<a href='Hidden comment: 

How Google Docs solved collaboration: [http://googledocs.blogspot.com/2010/09/whats-different-about-new-google-docs.html challenges], [http://googledocs.blogspot.com/2010/09/whats-different-about-new-google-docs_22.html operational transformation], [http://googledocs.blogspot.com/2010/09/whats-different-about-new-google-docs_23.html collaboration protocol]. The difference to GWT is that the client has only a fragment of all the dataset, while a Google Docs client always has the full dataset (the document).
'></a>



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/StoringData'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Storing entities in App Engine datastore' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ManagingPerformances'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Updating and deleting performances' /></a>
<a href='Hidden comment: NAV_END'></a>