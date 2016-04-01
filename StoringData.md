We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). Having built  [a landing page](BuildingLandingPage.md) which [interacts with the server back-end](ClientServer.md) and [authenticates members](Authentication.md), let's see how to save performances schedule in App Engine datastore.




# Exercise: build Manage Performances client side #
With a [mockup for managing performances UI](VisualizingYourApp.md), build the View & Presenter needed to schedule performances. Ignore update/delete and listing for now.

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/performances.png' border='1' />


## Exercise solution ##
See [performances View/Presenter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/#CultureShows/client/performances). The result:


<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/performancesApp.png' border='1' />

# Coding our data model #
**Pre-requisites**

  * [Quickstart with twig-persist](http://code.google.com/p/twig-persist/wiki/Quickstart), an Object Datastore for Google App Engine
  * [Creating a Data Model](http://code.google.com/p/twig-persist/wiki/Configuration)
  * [twig unit tests](http://code.google.com/p/twig-persist/source/browse/src/test/java/com/google/code/twig/tests/festival/MusicFestivalTestCase.java?r=c82aa7fecaa63d6d90ca6f55d30ecb812ff779e3) to see more usage examples


<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/bale_of_hay.jpg' align='right' border='0' />

Remember [our data modeling](DataModeling.md) ? Let's code it!

[Theater](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/Theater.java)
```
package org.gwtgaebook.CultureShows.shared.model;

public class Theater {
	public String name;
	public String websiteURL;
	public String photoURL;
	public String phone;
	public String locality;
	public String region;
	public String countryName;
	public String language;
}
```

[Show](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/Show.java)
```
package org.gwtgaebook.CultureShows.shared.model;

public class Show {
	public String name;
	public String websiteURL;
	public int minuteDuration;
	...
}
```

Here are [complete data models](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/), explained in more detail below.

Before proceeding with the rest of the code, let's talk about...

# Datastore setup #
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/road_home.jpg' align='right' border='0' />

By default, twig creates indexes for every entity property, which makes it very usable with no configuration. We'll follow the [twig performance advice](http://code.google.com/p/twig-persist/wiki/Performance) and [set properties to be un-indexed by default](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/AnnotationObjectDatastoreProvider.java), specifically annotating each desired property to be indexed. Our updated data model looks like this

[Show](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/Show.java)
```
public class Show {
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	public String websiteURL;
	public int minuteDuration;
	public String posterURL;

	public void setName(String name) {
		this.name = name;
		nameQuery = name.trim().toLowerCase();
	}

	public String getName() {
		return name;

	}
}
```


We'll also [shorten kind names](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/AnnotationObjectDatastoreProvider.java), which means less stored data and increases readability in datastore viewer.

Since we'll use the show name in case-insensitive queries, we're storing a copy of name property which is always trimmed and lowercased.

## One-to-Many relationships ##
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/bouldersbeach.jpg' align='right' border='0' />

(Datastore relationships, that is :)

In a relational database, we would do it like this:
```
CREATE TABLE theater (
	key INTEGER PRIMARY KEY,
	name VARCHAR(128)
);

CREATE TABLE show (
	key INTEGER PRIMARY KEY,
	theaterKey INTEGER REFERENCES theater (key),
	name VARCHAR(128)
);
```

There are several alternatives to implement One-to-Many [relationships in AppEngine](http://code.google.com/appengine/docs/java/datastore/relationships.html) and [twig](http://code.google.com/p/twig-persist/wiki/Configuration#Parent-Child_Relationships).

We'll take advantage of [Keys](http://code.google.com/appengine/docs/java/javadoc/com/google/appengine/api/datastore/Key.html) being able to [contain a reference to parent entity](http://code.google.com/appengine/docs/java/datastore/creatinggettinganddeletingdata.html#Keys) and that [Keys can be queried](http://code.google.com/appengine/docs/java/datastore/queriesandindexes.html#Queries_on_Keys). We can leave our data models as they are, and use them like this
```
//store shows belonging to a theater
showKey = datastore.store().instance(show).parent(theater).now();

//query for shows belonging to a theater instance
List<Show> shows = datastore.find().type(Show.class).ancestor(theater).returnAll().now();
```

Coming back to the relational database analogy, the theaterKey reference is embedded in Show key `parentKey` property. AppEngine keys are always indexed, even if we disable indexing for class properties.


## Many-to-Many relationships ##

In a relational database, we would do it like this:
```
CREATE TABLE member (
	key INTEGER PRIMARY KEY,
	email VARCHAR(128)
);

CREATE TABLE theaterMember (
	theaterKey INTEGER REFERENCES theater (key),
	memberKey INTEGER REFERENCES member (key),
	role INTEGER NOT NULL,
	PRIMARY KEY (theaterKey, memberKey)
);
```


We'll go with the same approach as in relational databases and then optimize it for AppEngine by [denormalizing](http://en.wikipedia.org/wiki/Database_normalization).

Let's create a new model, [TheaterMemberJoin](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/TheaterMemberJoin.java)
```
public class TheaterMemberJoin {
	public enum Role {
		ADMINISTRATOR, ARTIST, ASSISTANT
	};

	@Index
	public String theaterKey;
	@Index
	public String memberKey;
	@Index
	public Role role;
}
```

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/quijote3.jpg' align='right' border='0' />


To add a Member to a Theater, we need just one query on `TheaterMemberJoin`.

This enables us to answer the two basic questions
  * what Members does a Theater have and with what roles?
  * what Theaters is a Member part of?

However, to display a list of Members for a Theater, we need to
  * run a query to get all member keys and roles
  * for each member, run a query to get its details


If we consider that we actually don't need ALL Member information in thist list, and storage is cheap, we can optimize reading by denormalizing our model and storing a part of Member data in the Join model. We need email and name to get a Member listing, so let's add them
```
public class TheaterMemberJoin {
	...
	// denormalized
	public String theaterName;

	public String memberName;
	public String memberEmail;
}
```

Now getting a list of Members for a Theater is one query, adding a Member to a Theater is still one write, but changing a Member email requires two writes. We don't do that often, so we're good.

When thinking how to implement your models, consider if you are doings reads often than writes and make the choices that are best for your project. If you are doing way many more readings than updates, consider storing a list of Members desired properties embedded in the Theater model. You will need two writes for a Member property update in this case.

Hopefully we'll have lots of read requests for Performances, so there too we'll keep copies of Show and Location properties.

**Resources**
  * [article about many to many relationships](http://blog.arbingersys.com/2008/04/google-app-engine-better-many-to-many.html), with [group discussion](http://groups.google.com/group/google-appengine/browse_thread/thread/e9464ceb131c726f)
  * [other approaches](http://stackoverflow.com/questions/1376814/listproperty-of-keys-vs-many-to-many-in-app-engine)




# Basic data storage #

We see a common theme for datastore entities: we need basic [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) functionality available for each of them, with some variations. Let's introduce a [data access object](http://en.wikipedia.org/wiki/Data_access_object) (DAO) for each entity. Since there's a lot of common functionality, we can have entity DAOs inherit from an abstract class:

[DAO](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dao/DAO.java)
```
public abstract class DAO<T> {
	protected Class<T> tClass;
	protected final ObjectDatastore datastore;

	protected static final Logger logger = Logger
			.getLogger(DAO.class.getName());

	protected DAO(Class<T> tClass, final ObjectDatastore datastore) {
		this.tClass = tClass;
		this.datastore = datastore;
	}

	public void validateKeySameAsClass(Key key) {
		// getCanonicalName().replaceAll("\\.", "_")
		String name = tClass.getName();
		if (name.lastIndexOf('.') > 0) {
			name = name.substring(name.lastIndexOf('.') + 1);
		}
		if (!name.equals(key.getKind())) {
			throw new IllegalArgumentException("Key kind " + key.getKind()
					+ " != class " + name);
		}
	}

	public Key getKey(T entity) {
		return datastore.associatedKey(entity);
	}

	public Key create(T entity) {
		// store creates a Key in the datastore and keeps it in the
		// ObjectDatastore associated with this theater instance.
		// Basically, every OD has a Map<Object, Key> which is used to look up
		// the Key for every operation.
		return datastore.store(entity);
	}

	public T read(Key key) {
		validateKeySameAsClass(key);
		return datastore.load(key);
	}

	public T read(String key) {
		return read(Validation.getValidDSKey(key));
	}

	public void update(T entity, Key key) {
		validateKeySameAsClass(key);
		datastore.associate(entity, key);
		datastore.update(entity);
	}

	public void delete(Key key) {
		validateKeySameAsClass(key);
		// workaround for
		// http://code.google.com/p/twig-persist/issues/detail?id=47
		T entity = datastore.load(key);
		datastore.getService().delete(key);
		datastore.disassociate(entity);
	}

}
```

Each entity DAO will inherit this, introducing the necessary variations or extra functionality. [TheaterDAO](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dao/TheaterDAO.java) is as simple as
```
public class TheaterDAO extends DAO<Theater> {

	@Inject
	public TheaterDAO(final ObjectDatastore datastore) {
		super(Theater.class, datastore);
	}

	@Override
	public void delete(Key key) {
		// TODO need to check associated entities, such as shows. Will do later
		// when needed
		super.delete(key);
	}

}
```


<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/rencontres-2.jpg' border='0' />


# Creating members and theaters #
Shows belong to Theaters. In a regular app, we would have asked Violeta to perform the following steps after signing in:
  * create a theater and specify theater details such as name, website URL
  * define shows and locations assigned to the theater defined above
  * and at last, schedule a show performance


We can do better! We'll simply asking Violeta to schedule a performance. We'll take care of the details behind the scenes (pun intended :-), even though that's more work for us,  engineers.


The first detail we need to take care of is creating member entities on signing in, if they don't exist already. We'll do this in [GetUserHandler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/GetUserHandler.java) :
```
	@Override
	public GetUserResult execute(GetUserAction action, ExecutionContext context)
			throws ActionException {

		UserInfo userInfo = userInfoProvider.get();

		UserService userService = UserServiceFactory.getUserService();

		// theaters user has access to; key/name
		List<Theater> theaters = new ArrayList<Theater>();

		if (userInfo.isSignedIn) {
			userInfo.signOutURL = userService.createLogoutURL(action
					.getRequestURI());

			// check if user has a Member record, create one if not
			Member member = null;
			Key memberKey = null;

			member = memberDAO.readByUserId(userInfo.userId);

			if (null == member) {
				// first signin, create new member
				member = new Member();
				member.userId = userInfo.userId;
				member.email = userInfo.email;
				member.signUpDate = new Date();
				memberDAO.create(member);

			}
			memberKey = memberDAO.getKey(member);

			theaters = tmjDAO.readbyMember(KeyFactory.keyToString(memberKey));

			if (0 == theaters.size()) {
				// create a theater and give member access to it
				Theater theater = new Theater();
				theater.name = Constants.defaultTheaterName;
				Key theaterKey = theaterDAO.create(theater);
				theater.theaterKey = KeyFactory.keyToString(theaterKey);
				theaters.add(theater);

				// assign member to theater
				TheaterMemberJoin tmj = new TheaterMemberJoin();
				tmj.theaterKey = KeyFactory.keyToString(theaterKey);
				tmj.memberKey = KeyFactory.keyToString(memberKey);
				tmj.role = Role.ADMINISTRATOR;

				tmj.theaterName = theater.name;
				tmj.memberEmail = member.email;
				tmj.memberName = member.name;

				tmjDAO.create(tmj);
			}

		} else {
			userInfo.signInURLs.put("Google", userService.createLoginURL(
					action.getRequestURI(), null, "google.com/accounts/o8/id",
					null));
			userInfo.signInURLs.put("Yahoo", userService.createLoginURL(
					action.getRequestURI(), null, "yahoo.com", null));
		}

		return new GetUserResult("", userInfo, theaters);
	}
```


After signing in now in your development environment, go to [your local datastore viewer](http://127.0.0.1:8888/_ah/admin) and check that Member and Theater entities have been created. Besides the fields we declared in our data model, you'll see two more:  [Key and ID/Name](http://code.google.com/appengine/docs/python/datastore/keysandentitygroups.html).




Next, let's start a [ManagePerformance server handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/ManagePerformanceHandler.java), which stores performances, if the current member is signed in and has access to the given Theater:

```

	@Override
	public ManagePerformanceResult execute(ManagePerformanceAction action,
			ExecutionContext context) throws ActionException {

		UserInfo userInfo = userInfoProvider.get();

		Member member = null;

		Theater theater = null;
		Key theaterKey = null;
		Show show = null;
		Location location = null;

		Key performanceKey = null;
		Performance performance = null;

		if (!userInfo.isSignedIn) {
			return new ManagePerformanceResult("Not signed in", null);
		}

		member = memberDAO.readByUserId(userInfo.userId);
		theaterKey = Validation.getValidDSKey(action.getTheaterKey());

		if (!tmjDAO.memberHasAccessToTheater(
				KeyFactory.keyToString(memberDAO.getKey(member)),
				KeyFactory.keyToString(theaterKey))) {
			return new ManagePerformanceResult(
					"You don't have access to this theater", null);

		}

		theater = theaterDAO.read(theaterKey);

		switch (action.getActionType()) {
		case CREATE:
			performance = action.getPerformance();
			show = createOrReadShow(theater, action.getPerformance().showName);
			location = createOrReadLocation(theater,
					action.getPerformance().locationName);

			// set/update performance data
			performance.showKey = KeyFactory.keyToString(showDAO.getKey(show));
			performance.locationKey = KeyFactory.keyToString(locationDAO
					.getKey(location));

			performance.showName = show.getName();
			performance.showWebsiteURL = show.websiteURL;
			performance.locationName = location.getName();
			performanceKey = performanceDAO.create(theater, performance);

			break;

		...

		}

		performance.performanceKey = KeyFactory.keyToString(performanceKey);

		return new ManagePerformanceResult("", performance);
	}

	Show createOrReadShow(Theater theater, String showName) {
		// setup show
		// does show already exist?
		Show show = new Show();
		show.setName(showName);

		List<Show> shows = showDAO.readByName(theater, showName);
		if (shows.size() > 0) {
			show = shows.get(0);
		} else {
			showDAO.create(theater, show);
		}
		logger.info("Current show "
				+ KeyFactory.keyToString(showDAO.getKey(show)));

		return show;
	}
```




## Hey client, remember me? ##

We need the GWT client to remember the current theater and send it with each create performance request. The current theater will be chosen by Violeta, if she will have multiple theaters.

There are a few possibilities to store data in GWT client as a session:
  * Use HTML5 web storage
  * Use cookies
  * [Use server sessions](http://stackoverflow.com/questions/1382088/session-id-cookie-in-gwt-rpc), and persist sessions among requests. [full sample](http://groups.google.com/group/google-web-toolkit/browse_thread/thread/268ec2887bec95d2/)
  * Use a static variable. It will be lost when users refresh page.
  * Use an equivalent of a static variable: a [singleton](http://en.wikipedia.org/wiki/Singleton_pattern). It has the advantage of being injected by Gin rather than having a global scope, so it can easily be replaced with another implementation when doing testing if needed.

We'll go with the last solution: a
[ClientState class](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/ClientState.java)
```
public class ClientState {
	public UserInfo userInfo;

	// theaters user has access to; key/name
	public List<Theater> theaters = new ArrayList<Theater>();

	public String currentTheaterKey;
}
```

[binded as a singleton](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/MainModule.java)
```
		bind(ClientState.class).in(Singleton.class);
```




# Additional resources #
  * [App Engine Datastore Java API](http://code.google.com/appengine/docs/java/datastore/)
  * [A comparison of persistence frameworks](http://borglin.net/gwt-project/?page_id=604)  for App Engine, [another comparison](http://code.google.com/p/twig-persist/wiki/Comparison)
  * [Latest source code](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/dispatch/) containing additional enhancements, such as injecting ObjectDatastore


<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/Authentication'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Authentication with OpenID' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ListingSchedule'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Listing saved performances' /></a>
<a href='Hidden comment: NAV_END'></a>