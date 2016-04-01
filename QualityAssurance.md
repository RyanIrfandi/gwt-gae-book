We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md), and implemented [managing performances schedule](ManagingPerformances.md).

Before we add more features, we need to take a closer look at quality.




# Pre-requisites #
  * Make sure you have [the latest source code](http://code.google.com/p/gwt-gae-book/downloads/detail?name=CultureShows-0.3-before-QA.tgz) for this chapter
  * [General quality assurance](http://en.wikipedia.org/wiki/Quality_assurance)
  * [Software quality](http://en.wikipedia.org/wiki/Software_quality)

# When should QA start? #
If you answered at unit tests, or right at the beginning of development with [Test-driven development](http://en.wikipedia.org/wiki/Test-driven_development), think again. What if you deliver a bug-free, but useless project to your customer?

QA should start when we [start defining requirements](SoYouHaveAnIdea.md). The later we fix defects, the more costly they are. A bad user story is much easier to fix before we actually implement it.

Let's see how we can increase quality for the main phases in a software project. Note that this is an iterative process which repeats continuously throughout the project lifetime, rather than being done in a [waterfall](http://en.wikipedia.org/wiki/Waterfall_model) way.

# Personae #
When defining [user personae](http://en.wikipedia.org/wiki/Persona_(marketing)), consider your intended audience. Then search, meet and discuss with a few representatives for each of the draft personae you described so far. Understand their behavior patterns, environment, skills and needs, and include these in persona description.

**Additional resources**
  * [The Persona Process](http://www.designstamp.com/downloads/DesignStamp_PersonaProcess.pdf)

# User stories #
Having a good idea of personae life goals, experience goals and end goals, we can start [defining user stories](SoYouHaveAnIdea.md) that contribute to accomplishing those goals.

One of the best ways to validate stories and prioritization is to share them with personae representatives. List all the stories (removing "As Persona") and ask them to rate their importance. Discuss why they rated it that way. The lowest rated stories should probably be completely removed from your backlog.
Ask what's missing to help them accomplish their goals, and rate the importance of those stories too.

Rather than having a huge list of features demanded by all potential users you talk to, try to determine the minimum feature set required to get early customers. [Fail fast, fail cheap](http://www.businessweek.com/magazine/content/07_26/b4040436.htm).

**Additional resources**
  * [INVEST in user stories](http://www.slideshare.net/craigwbrown/invest-in-good-user-stories-presentation)
  * <a href='http://www.amazon.com/gp/product/0976470705?ie=UTF8&tag=gwtgaebook-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0976470705'>The Four Steps to the Epiphany</a><img src='http://www.assoc-amazon.com/e/ir?t=gwtgaebook-20&l=as2&o=1&a=0976470705' alt='' border='0' width='1' height='1' /> : customer discovery, customer validation, customer creation, company building
  * [Startup lessons learned blog](http://www.startuplessonslearned.com/)

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/rencontres-3.jpg' align='right' border='0' />

# Metrics and KPIs #
They say "You can’t improve what you can’t measure". If we're asked "How is the project doing?" and reply "Good", a follow-up question might be "What do you understand by good?". Common project metrics include
  * Schedule - estimated/targeted delivery date and slippage in days
  * Cost - actual budget versus original budget
  * Resources - what resources and much of them are we using (people, time, equipment...)

Using Scrum allows quick (bi-weekly to monthly) inspection of these, and adapting.

[Key Performance Indicators](http://en.wikipedia.org/wiki/Performance_indicator) give us more specific indicators on how we stand on areas important to our project. Some of the Culture Shows KPIs are:
  * visitors of the landing page
  * users signing in
  * users scheduling performances
  * 3rd party sites displaying performances
  * active theaters (having regular performances)

[Net Promoter Score](http://en.wikipedia.org/wiki/Net_Promoter) is a relatively recent and simple metric for [customer satisfaction](http://en.wikipedia.org/wiki/Customer_satisfaction).

**Additional resources**
  * [Measuring Net Promoter Score](http://www.theultimatequestion.com/theultimatequestion/measuring_netpromoter.asp?groupcode=2)

# User interface #
We've already covered [building mockups](VisualizingYourApp.md) and getting feedback on them.

A fast and cheap technique for [usability testing](http://en.wikipedia.org/wiki/Usability_testing) is to print all the mocked screens of the app, then going with prints to potential users. After giving a basic context, present the landing page print, asking questions such as "what do you think this is", "what would you do next", "how would you do that". Be careful to avoid direct instructions such as "Now please schedule a performance" (the button is obvious).

According to their actions, present the corresponding screen print. If they want to fill out form fields, ask them to do that with a pen, and fill it yourself too on the next screen where that data appears. Investing a few days in preparing and performing this research will save weeks later.

**Additional resources**
  * <a href='http://www.amazon.com/gp/product/0321344758?ie=UTF8&tag=gwtgaebook-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=0321344758'>Don't Make Me Think: A Common Sense Approach to Web Usability</a><img src='http://www.assoc-amazon.com/e/ir?t=gwtgaebook-20&l=as2&o=1&a=0321344758' alt='' border='0' width='1' height='1' /> (great book)

# Functional specifications #
[Functional specifications](http://en.wikipedia.org/wiki/Functional_specification) detail the application from users point of view. It helps making sure all stakeholders (from customers to engineers) have the same understanding of the expected behavior.

A functional specification might include details about user interface (UI), behaviors, expected performance, internationalization and privacy requirements, constraints and limitations, interaction with other systems.

[Peer reviews](http://en.wikipedia.org/wiki/Software_peer_review), with varying degrees of formality, help in discovering defects.

**Additional resources**
  * [Painless Functional Specifications](http://www.joelonsoftware.com/articles/fog0000000036.html)
  * [Example Requirements Review Checklist](http://vast.uccs.edu/~tboult/CS330/DOCS/Requirements%20Review%20Checklist.doc)

# Design/technical specifications #

[Design specifications](http://en.wikipedia.org/wiki/Software_design) explain how the application works from engineering point of view. What should go into it? Topics vary from project to project and might include architecture, data models, logic, internationalization, testability, testing recommendations (just what's not obvious from functional specification), performance, security. As a guideline, I like to think about
  * what helps to clarify and estimate how one would implement this feature, before writing code
  * what would I find helpful if I come back to this project/functionality after six months
  * if I was a new team member, what would help me getting ready for changing existing features or implementing new ones

I might be old school, but I like doing [pseudocode](http://en.wikipedia.org/wiki/Pseudocode) first for complex functionality. Pseudocode gives a higher-level overview on how various components works. Keep it updated as the project develops, to be used later as a reference by you and others.

[Peer reviews](http://en.wikipedia.org/wiki/Software_peer_review) with fellow engineers  are extremely useful for early defect discovery. Keeping a history of found defects and spent time helps balancing the allocated review time for best return of investment. If reviews find almost no defects, the author is either a great engineer, or the reviews aren't effective.


# Code #


## Static code analysis tools ##
[Static code analysis](http://en.wikipedia.org/wiki/Static_code_analysis) tools scan code and report various possible issues, from code formatting style to bugs. Go through [the list of Java tools](http://en.wikipedia.org/wiki/List_of_tools_for_static_code_analysis#Java) and see what would benefit you most. Setup the tools to run as part of the build process, after every commit (you are using [continuous integration](http://en.wikipedia.org/wiki/Continuous_integration), right?).

Running PMD on Culture Shows code found nothing. FindBugs though discovered these small issues:
  * Constants.manageActionType should start with an uppercase letter
  * Main.logger should be final
  * Dead store to local variable in GetUserInfo, the value is not read or used in any subsequent instruction:
```
memberKey = datastore.store(member);
```
and several false positives (the tool gives more details, but copy-pasting exact issue report doesn't work (!)).


## Code reviews ##
Effective [code reviews](http://en.wikipedia.org/wiki/Code_review) are one of the most handy methods to increase code quality. [Code review tools](http://www.google.com/search?sourceid=chrome&ie=UTF-8&q=code+review+code.google) ease the interaction between engineers and offer integrated comments and defects tracking.

**Additional resources**
  * <a href='http://www.amazon.com/gp/product/020161622X?ie=UTF8&tag=gwtgaebook-20&linkCode=as2&camp=1789&creative=9325&creativeASIN=020161622X'>The Pragmatic Programmer: From Journeyman to Master</a><img src='http://www.assoc-amazon.com/e/ir?t=gwtgaebook-20&l=as2&o=1&a=020161622X' alt='' border='0' width='1' height='1' />
  * [Code reviewers guide](http://misko.hevery.com/code-reviewers-guide/) (focused on Java)
  * [Strategies for effective code reviews](http://www.basilv.com/psd/blog/2007/strategies-for-effective-code-reviews)


# Testing #

Here are more details about what [testing](http://en.wikipedia.org/wiki/Software_testing) can be performed. We'll focus only on a few of these.

[Peer reviews](http://en.wikipedia.org/wiki/Software_peer_review) should again be used in the areas below to discover where testing could be improved.

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/morning_tea_4.jpg' align='left' border='0' />


## Unit testing ##

As we'll keep adding and changing features, how can we know if we break existing functionality? This is where automated tests, such as [unit testing](http://en.wikipedia.org/wiki/Unit_testing), come in.

Another benefit is that we'll accomplish a better design of our code just by making it unit testable. For example, [ManagePerformanceHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java?r=584) initially had one big method doing everything, which was harder to follow and debug.

Unit testing focuses only on the smallest, independent units of our code. [Protons](http://en.wikipedia.org/wiki/Proton) and [neutrons](http://en.wikipedia.org/wiki/Neutron) you say? Well, not _that_ small. We need to look at parts which have logic which could be tested. If they interact with other components, such as storage, we need to replace those parts with [mock objects](http://en.wikipedia.org/wiki/Mock_object). Methods suitable for unit testing should take an input, do something with it, and provide an output. We'll test them by providing known inputs and checking if outputs match what we expected.

### Exercise ###
Review [an older version of ManagePerformanceHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java?r=584) and identify parts which should be unit tested. Remember we are testing the logic of these parts. A simple block of code which just stores an object in the datastore and returns it doesn't have anything to be meaningfully unit tested.

### Exercise solution ###
If we're trying to update or delete a performance, we expect to be given valid theater and performance keys:
```
		case DELETE:
			...
			if (!Strings.isNullOrEmpty(action.getPerformance().performanceKey)) {
				try {
					performanceKey = KeyFactory.stringToKey(action
							.getPerformance().performanceKey);
				} catch (Exception e) {
					// invalid key, ignore it
				}
			}

			if (null == performanceKey) {
				return new ManagePerformanceResult("Invalid performance key",
						null);
			}

			performance = datastore.load(performanceKey);
```

This is something we could isolate in a separate method
```
	public static Key getValidDSKey(String key)
```
which would throw an exception when the key is null, empty or invalid. Why not [return an error](http://tutorials.jenkov.com/java-exception-handling/validation-throw-exception-or-return-false.html) or [use asserts](http://download.oracle.com/javase/6/docs/technotes/guides/language/assert.html)? As [preconditions checking](http://download.oracle.com/javase/6/docs/technotes/guides/language/assert.html) advise, the keys are passed to the `public execute()` as part of the input, so we should enforce the argument checks in a way understandable by the caller of this method.

Here are the [validation method](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/util/Validation.java)
```
	public static Key getValidDSKey(String key) {
		if (Strings.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Null or empty key");
		}

		Key dsKey;
		try {
			dsKey = KeyFactory.stringToKey(key);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid key " + key);
		}
		
		return dsKey;
	}
```

[the test](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/test/org/gwtgaebook/CultureShows/server/util/ValidationTest.java)
```
	@Test
	public final void getValidDSKeyTest() throws Exception {
		try {
			Validation.getValidDSKey(null);
			fail("Null key was considered valid");
		} catch (IllegalArgumentException expected) {
			// exception thrown, we're good, nothing else to check here
		}

		try {
			Validation.getValidDSKey("");
			fail("Empty key was considered valid");
		} catch (IllegalArgumentException expected) {
			// exception thrown, we're good, nothing else to check here
		}

		final String invalidKey = "invalidKey";
		try {
			Validation.getValidDSKey(invalidKey);
			fail("Invalid key " + invalidKey + " was considered valid");
		} catch (IllegalArgumentException expected) {
			assertTrue("Exception message doesn't mention " + invalidKey,
					expected.getMessage().indexOf(invalidKey) >= 0);
		}

		// any appengine key will work
		final String validKey = "agxjdWx0dXJlc2hvd3NyDgsSB1RoZWF0ZXIY4V0M";
		Key dsKey;
		dsKey = Validation.getValidDSKey(validKey);
		assertTrue("Returned key is null", null != dsKey);
		assertTrue("Returned key " + dsKey.toString() + " is not equal to "
				+ validKey, dsKey.equals(KeyFactory.stringToKey(validKey)));

	}
```


and [the slightly refactored code](http://code.google.com/p/gwt-gae-book/source/diff?spec=svn591&r=591&format=side&path=/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java&old_path=/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java&old=584).

We might come back to PerformancePresenter in a later chapter. As with real projects, we are balancing the investment in testing with the rest of the project needs, like getting customers and becoming profitable. Therefore, we won't set a specific [code coverage](http://en.wikipedia.org/wiki/Code_coverage) target, but consider [what makes sense](http://googletesting.blogspot.com/2010/07/code-coverage-goal-80-and-no-less.html) on a case by case basis.


**Additional resources**
  * [JUnit tutorial](http://www.vogella.de/articles/JUnit/article.html)
  * [GWT testing methodologies](http://code.google.com/webtoolkit/articles/testing_methodologies_using_gwt.html)
  * [GWT testing best practices](http://www.youtube.com/watch?v=T_CLzgEL7FA)
  * [gwt-platform unit testing introduction](http://code.google.com/p/gwt-platform/wiki/UnitTesting)
  * [Coding Java Unit Exception Tests](http://www.c2.com/cgi/wiki?CodingJavaUnitExceptionTests)
  * [Test-driven development](http://en.wikipedia.org/wiki/Test-driven_development)
  * http://misko.hevery.com/presentations/
  * [Mockito mocking framework](http://code.google.com/p/mockito/)
  * [The Way of Testivus](http://bit.ly/thewayoftestivus)
  * be careful on [overengineering](http://en.wikipedia.org/wiki/Overengineering)

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/quijote2.jpg' border='0' />

## Integration testing ##

After we unit tested parts of functionality, [integration testing](http://en.wikipedia.org/wiki/Integration_testing) checks if these parts work together.
We'll proceed with a bottom-up testing approach, starting from App Engine storage and our entities.

### Exercise ###
Learn about [AppEngine unit testing](http://code.google.com/appengine/docs/java/tools/localunittesting.html). Then review [DAOs](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/server/dao) and write the tests for them. Consider you have the real storage available (not mocked).

### Exercise solution ###
[TheaterDAOTest](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/test/org/gwtgaebook/CultureShows/server/dao/TheaterDAOTest.java)
```
public class TheaterDAOTest extends LocalDatastoreTestCase {

	public static class Module extends AutomockingModule {
		@Override
		protected void configureTest() {
			bind(ObjectDatastore.class).toProvider(
					AnnotationObjectDatastoreProvider.class).in(
					TestScope.SINGLETON);
			bind(TheaterDAO.class);
		}
	}

	@Inject
	TheaterDAO theaterDAO;

	Theater t1, t2;
	Key t1Key, t2Key;

	@Before
	public void setUp() {
		super.setUp();
	}

	@After
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public final void testTheaters() {
		t1 = new Theater();
		t1.name = UUID.randomUUID().toString();
		t1Key = theaterDAO.create(t1);
		t2 = theaterDAO.read(t1Key);
		assertEquals("created theater doesn't match", t1, t2);

		t1.name = t1.name + "updated";
		theaterDAO.update(t1, t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("updated theater doesn't match", t1, t2);

		theaterDAO.delete(t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("deleted theater still exists", null, t2);

		t1 = new Theater();
		t1Key = theaterDAO.create(t1);
		theaterDAO.delete(t1Key);
		t2 = theaterDAO.read(t1Key);
		assertEquals("deleted theater still exists", null, t2);

	}
}
```


Here's what tests we could run for [ShowDAO](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/test/org/gwtgaebook/CultureShows/server/dao/ShowDAOTest.java)
```
	@Before
	public void setUp() {
		super.setUp();

		t1 = new Theater();
		t1.name = UUID.randomUUID().toString();
		t1Key = theaterDAO.create(t1);

		l1 = new Location();
		l1.setName(UUID.randomUUID().toString());
		l1Key = locationDAO.create(t1, l1);

	}

	@After
	public void tearDown() {
		theaterDAO.delete(t1Key);

		super.tearDown();
	}

	@Test
	public final void testShows() {

		s1 = new Show();
		s1.setName(UUID.randomUUID().toString());

		try {
			s1Key = showDAO.create(s1);
			fail("show created without an associated theater");
		} catch (IllegalArgumentException expected) {
			assertTrue("Exception message doesn't mention " + s1.getName(),
					expected.getMessage().indexOf(s1.getName()) >= 0);
		}
		s1Key = showDAO.create(t1, s1);

		s2 = showDAO.read(s1Key);
		assertEquals("created show doesn't match", s1, s2);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		Performance p = new Performance();
		p.date = date;
		p.showKey = KeyFactory.keyToString(s1Key);
		p.locationKey = KeyFactory.keyToString(l1Key);
		performanceDAO.create(t1, p);

		s1.setName(s1.getName() + "updated");
		showDAO.update(s1, s1Key);
		s2 = showDAO.read(s1Key);
		assertEquals("updated show doesn't match", s1, s2);

		List<Performance> plist = performanceDAO.readByShow(KeyFactory
				.keyToString(s1Key));
		assertEquals("performance show wasn't updated", s1.getName(),
				plist.get(0).showName);

	}

	@Test
	public final void testReferentialIntegrity() {

		s1 = new Show();
		s1.setName(UUID.randomUUID().toString());
		s1Key = showDAO.create(t1, s1);

		Date date = new Date(new java.util.Date().getTime()
				+ Constants.oneDayMiliseconds);

		p1 = new Performance();
		p1.date = date;
		p1.showKey = KeyFactory.keyToString(s1Key);
		p1.locationKey = KeyFactory.keyToString(l1Key);

		p1Key = performanceDAO.create(t1, p1);

		try {
			showDAO.delete(s1Key);
			fail("show with associated performances deleted");
		} catch (IllegalArgumentException expected) {
		}

		s2 = showDAO.read(s1Key);
		assertEquals(
				"show was deleted although performances with refences to it exist",
				s1, s2);

		performanceDAO.delete(p1Key);

		showDAO.delete(s1Key);
		s2 = showDAO.read(s1Key);
		assertEquals("deleted show still exists", null, s2);

	}
```


See [all integration tests](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/test/org/gwtgaebook/CultureShows/server/dao/).

Since we have several classes to test now, let's create a test suite with all of them. We can simply run this suite when making changes to tested classes to see if we broke something. If you're using a build system, running tests should be part of the build.

```
@RunWith(Suite.class)
@Suite.SuiteClasses({ ValidationTest.class, TheaterDAOTest.class,
		ShowDAOTest.class, LocationDAOTest.class, PerformanceDAOTest.class })
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$

		// $JUnit-END$
		return suite;
	}

}
```



## Web services testing ##

The GWT client is using web services such as `GetPerformances` which we implemented in App Engine. These services could be used by other clients too. We should ensure their functionality, backwards-compatibility and response time with automated web services tests. There are various tools available, one of them being [JMeter](http://jakarta.apache.org/jmeter/). We will come back to automated web service tests after we implement REST web services, which are easier to test than RPC.

**Additional resources**
  * [Web Services Testing lecture](http://www.cs.colorado.edu/~kena/classes/7818/f06/lectures/WebTest.pdf)
  * [Testing Applications and APIs](http://googletesting.blogspot.com/2007/09/testing-applications-and-apis.html)

## UI testing ##
[Selenium](http://seleniumhq.org/) is a suite of tools which helps automating complete application tests, which are usually done manually. We will invest in these tests later.

**Additional resources**
  * [GWT Remote Testing](http://code.google.com/webtoolkit/doc/latest/DevGuideTestingRemoteTesting.html)

## Performance testing ##
With all the tests above we made sure everything functions as expected. We also need to set performance expectations, such as initial loading time, response time duration from user initiating an action until it's completed, and how many concurrent users we can support until performance starts degrading.

[Google Speed Tracer](http://code.google.com/webtoolkit/speedtracer/) is one of the tools which can be used for performance testing.

To see how an application performs on a slow connection, limit the bandwidth using [Firefox Throttle](http://www.uselessapplications.com/en/Application/FirefoxThrottle.aspx).

We can check how fast a website loads from various locations using [webpagetest.org](http://webpagetest.org). Right now, on the first access, Culture Shows landing page takes 1.2s to fully load.


**Additional resources**
  * [Using a Dynamic Host Page for Authentication and Initialization](http://code.google.com/webtoolkit/articles/dynamic_host_page.html)
  * [How I made my GWT/AppEngine application appear to load quicker](http://arcbees.wordpress.com/2010/09/29/how-i-made-my-gwtappengine-application-appear-to-load-quicker/)
  * [HttpFox](https://addons.mozilla.org/en-US/firefox/addon/6647/) monitors and analyzes HTTP traffic
  * [Google Page Speed](http://code.google.com/speed/page-speed/)
  * [YSlow](http://developer.yahoo.com/yslow/)

## Manual test cases ##
Finally, there are situations when automating some tests isn't cost effective. These [test cases](http://en.wikipedia.org/wiki/Test_case) should be documented so one can run them whenever needed.

## Scenario testing ##
If all tests above work, brief [scenario testing](http://en.wikipedia.org/wiki/Scenario_testing) makes sure all the system works. After a deploy, we'll just manually test a few major workflows: scheduling a performance and then deleting it.


## Additional resources ##
  * [Google testing blog](http://googletesting.blogspot.com/)
  * [A/B testing](http://en.wikipedia.org/wiki/A/B_testing)
  * [Google website optimizer](https://www.google.com/analytics/siteopt)



# Other areas #
Quality should be considered in all project areas, not just software development. These include deployment, data centers management (monitoring availability, performance), support, marketing.



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/ManagingPerformances'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Updating and deleting performances' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ManagingShows'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Managing shows' /></a>
<a href='Hidden comment: NAV_END'></a>