We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md), and implemented [managing performances schedule](ManagingPerformances.md), resulting in [a sample application](http://cultureshows.appspot.com/) with [reasonable quality](QualityAssurance.md). We've also just added [theater show management](ManagingShows.md).

The primary purpose of starting this application was to be able to display this data on various theater websites, and this what we'll do next.



# Exposing performances data as JSON #

First, [checkout the latest version of the CultureShows code](http://code.google.com/p/gwt-gae-book/source/checkout) and go through the reading list below.

**Pre-requisites**
  * [JSON](http://en.wikipedia.org/wiki/JSON)
  * [Converting Java Objects into JSON representation](http://code.google.com/p/google-gson/)
  * [Restlet, a RESTful web framework for Java](http://www.restlet.org/)
  * [Creating a simple web service with Restlet](http://www.2048bits.com/2008/06/creating-simple-web-service-with.html)
  * [Taoki, small library to use Restlet with Guice](http://code.google.com/p/taoki) and [user guide](http://code.google.com/p/taoki/wiki/Configuration)

Ideally, we would want to have an URL like
```
http://cultureshows.appspot.com/api/v1/theaters/{theaterKey}/performances
```
which would list all the upcoming performances for the theater key given in the path. We will include `Show` and `Location` entities in the `Performance` data too, the reason being shown later in this chapter.


Let's setup the infrastructure to use restlet with taoki, as described in the [taoki user guide](http://code.google.com/p/taoki/wiki/Configuration):
  * add a [new servlet](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/APIServlet.java)
```
public class APIServlet extends RestletServlet {
	@Override
	protected GuiceRouter createRouter(Injector injector, Context context) {
		return new APIRouter(injector, context);
	}
}
```

  * update [DispatchServletModule](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/DispatchServletModule.java) to handle the API path too
```
	public void configureServlets() {
		serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
				DispatchServiceImpl.class);
		serve("/api/v1/*").with(APIServlet.class);
	}

```

  * handle specific resource serving in [APIRouter](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/APIRouter.java)
```
public class APIRouter extends GuiceRouter {
	public APIRouter(Injector injector, Context context) {
		super(injector, context);
	}

	@Override
	protected void attachRoutes() {
		attach("/theaters/{id}/performances", PerformancesResource.class);
	}
}
```

Next, we can define how performances should be exposed:

[PerformancesResource](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/api/PerformancesResource.java)
```
public class PerformancesResource extends ServerResource {

	@Inject
	PerformanceDAO performanceDAO;
	@Inject
	ShowDAO showDAO;
	@Inject
	LocationDAO locationDAO;
	@Inject
	Gson gson;

	public class PerformancesGET {
		@Expose
		List<Performance> performances;
	}

	@Get("json")
	public Representation get() {
		List<Performance> performances = performanceDAO
				.readByTheater((String) getRequestAttributes().get("id"));

		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.show = showDAO.read(p.showKey);
			p.location = locationDAO.read(p.locationKey);
			performances.set(i, p);
		}

		PerformancesGET get = new PerformancesGET();
		get.performances = performances;
		JsonRepresentation representation = new JsonRepresentation(
				gson.toJson(get));

		return representation;
	}
}
```

Note that we updated the [Performance model](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/shared/model/Performance.java) with new fields and annotations:
```
public class Performance implements Serializable {
	...
	@Index
	@Expose
	public Date date;

	...
	@Store(false)
	@Expose
	public transient Show show;

	@Store(false)
	@Expose
	public transient Location location;
}
```

The [Java transient keyword](http://en.wikibooks.org/wiki/Java_Programming/Keywords/transient) was used in order to prevent `Show` and `Location` to be serialized when transferring data between GWT client to App Engine server.


[The result](http://cultureshows.appspot.com/api/v1/theaters/agxjdWx0dXJlc2hvd3NyDwsSB1RoZWF0ZXIY8asBDA/performances) looks like this
```
{
   "performances":[
      {
         "performanceKey":"agxjdWx0dXJlc2hvd3NyIQsSB1RoZWF0ZXIY8asBDAsSC1BlcmZvcm1hbmNlGIknDA",
         "date":"2010-12-10 10:00",
         "show":{
            "name":"Vis",
            "websiteURL":"",
            "minuteDuration":75,
            "posterURL":"http://www.passe-partoutdp.ro/images/vis.jpg"
         },
         "location":{
            "name":"Rapsodia"
         }
      },
      {
         "performanceKey":"agxjdWx0dXJlc2hvd3NyIQsSB1RoZWF0ZXIY8asBDAsSC1BlcmZvcm1hbmNlGNEPDA",
         "date":"2010-12-25 10:00",
         "show":{
            "name":"Made in Romania",
            "websiteURL":"",
            "minuteDuration":120,
            "posterURL":"http://www.passe-partoutdp.ro/images/made_thumb.jpg"
         },
         "location":{
            "name":"Rapsodia"
         }
      }
   ]
}
```



<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/romania_inventar.jpg' border='0' />


# Consuming data on 3rd party websites #

How do we get performances to be displayed on a theater website? We can parse the JSON data exposed earlier and just generate some HTML using jQuery.

We need to consider making the data available in a [JSON with padding format](http://en.wikipedia.org/wiki/JSON#JSONP), [performance](http://blog.listry.com/2010/10/app-engine-warm-up-requests-death-of.html) and [cost](http://code.google.com/appengine/docs/billing.html), since each 3rd party website request translates to CPU and bandwidth usage on App Engine.
To reduce the impact of all these, let's use [Yahoo! Query Language (YQL)](http://developer.yahoo.com/yql/) to fetch and cache the data for us. See [YQL  Guide](http://developer.yahoo.com/yql/guide) to understand how it works.

**UPDATE** : YQL is way too unreliable. We stopped using it on the real site and implemented another caching mechanism instead. Please use the code below for educational purposes only.

## Setting up YQL ##

Ideally, we would setup three open data tables for Shows, Locations and Performances, and then perform a join on all these. As [YQL doesn't support inner joins](http://developer.yahoo.net/forum/?showtopic=2506&hl=join&endsession=1) easily, we'll go with embedding needed data into Performance entity, as we already did.

The [performances table](http://cultureshows.appspot.com/yql/performances.xml) is defined as follows
```
<?xml version="1.0" encoding="UTF-8"?>
<table xmlns="http://query.yahooapis.com/v1/schema/table.xsd">
  <meta>
    <sampleQuery>select * from {table} where theaterKey='abc';</sampleQuery>
  </meta>
  <bindings>
    <select itemPath="json.performances" produces="JSON">
      <urls>
        <url>http://cultureshows.appspot.com/api/v1/theaters/{theaterKey}/performances</url>
      </urls>
      <inputs>
        <key id='theaterKey' type='xs:string' paramType='path' required='true' />
      </inputs>
    </select>
  </bindings>
</table>
```

and we can use it from [YQL console](http://developer.yahoo.com/yql/console/) like this
```
USE "http://cultureshows.appspot.com/yql/performances.xml";
SELECT * FROM performances WHERE theaterKey="agxjdWx0dXJlc2hvd3NyDwsSB1RoZWF0ZXIY8asBDA";
```

Additional resources
  * [Proxy caching on Google App Engine](http://www.kyle-jensen.com/proxy-caching-on-google-appengine)

## Displaying data on a test website ##
On a [test website](http://cultureshows.appspot.com/yql/performances.html), we can now easily display performances data using [jquery.tmpl](http://api.jquery.com/jquery.tmpl/)
```
<!doctype html>
<html>
<head>
	<title>Performances listing</title>

	<script type="text/javascript" language="javascript" 
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
	<script type="text/javascript" language="javascript" 
		src="http://ajax.microsoft.com/ajax/jquery.templates/beta1/jquery.tmpl.min.js"></script>

	<script type='text/javascript'>
	var theaterKey = 'agxjdWx0dXJlc2hvd3NyDwsSB1RoZWF0ZXIYqcMBDA';

	var yqlUrl = 'http://query.yahooapis.com/v1/public/yql?q=...&format=json&_maxage=86400';
	$.getJSON(yqlUrl, function(data) {
		$("#performanceTemplate").tmpl(data.query.results.performances).appendTo( "#performances" );
	});

	</script>

	<script id="performanceTemplate" type="text/html">
	<li>
		<div class='showPosterContainer'>
		<img class='showPoster' src='${show.posterURL}'/>
		</div>

		<div class='showPosterContainer'>
		<a class='showWebsite' href='${show.websiteURL}'>${show.name}</a><br/>
		${date}<br/>
		${location.name}<br/>
		Duration: ${show.duration}
		</div>
	</li>
	</script>


</head>
<body>
	<ul id='performances'></ul>
</body>
</html>
```

Note the `_maxage=86400` query parameter, which tells YQL to fetch this data from its cache for the next day, rather than reading it from App Engine.

Additional resources
  * [Building mashups with JSONP, jQuery, and Yahoo! Query Language](http://www.ibm.com/developerworks/web/library/wa-aj-jsonp2/index.html)


## Displaying data on a real website ##

Starting from the HTML page above, we can add [i18n support](http://jquery-howto.blogspot.com/2010/10/jquery-globalization-plugin.html) and customize the template as desired ([sample](http://cultureshows.appspot.com/yql/performancesGlob.html)). Here's how performances listing look on a real website:

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/passepartout.png' border='0' />







<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/ManagingShows'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Managing shows' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/ManagingLocations'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Managing locations with REST APIs' /></a>
<a href='Hidden comment: NAV_END'></a>