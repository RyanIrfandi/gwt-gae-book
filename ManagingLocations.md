We discussed about [clarifying your ideas before writing code](SoYouHaveAnIdea.md) and [setting up the development environment](GettingStarted.md). We built [a landing page](BuildingLandingPage.md) which [communicates with App Engine back-end](ClientServer.md) and [authenticates members](Authentication.md), and implemented [managing performances schedule](ManagingPerformances.md), resulting in [a sample application](http://cultureshows.appspot.com/) with [reasonable quality](QualityAssurance.md). We've also added [theater show management](ManagingShows.md) and enabled theater owners to [display performances on their own websites](ExposingData.md) with the help of an YQL mashup.

Next, we'll cover REST web services.




# About REST #

Web services have several [styles of use](http://en.wikipedia.org/wiki/Web_service#Styles_of_use). So far we used [remote procedure calls](http://en.wikipedia.org/wiki/Remote_procedure_call) to manage shows and other client-server interaction.

[REST](http://en.wikipedia.org/wiki/Representational_State_Transfer), with [JSON](http://en.wikipedia.org/wiki/JSON) data format, is now the de facto architecture of web services. We will manage theater locations using a REST architecture.

# Exposing REST web services with App Engine #
[We already saw](ExposingData.md) how to implement reading the performances list with REST. Following the same pattern, locations APIs are built by
  * implementing  [Locations](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/api/LocationsResource.java) and [Location](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/api/LocationResource.java) resources
  * [attaching routes](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/server/APIRouter.java) from URLs to resources


# Consuming REST services with GWT #
Besides [GWTP dispatch](http://code.google.com/p/gwt-platform/), we'll use [RequestBuilder](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/http/client/RequestBuilder.html) and [piriti](http://code.google.com/p/piriti/) to work with REST services from GWT.

After implementing some [generic helper classes](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/#client%2Fdispatch), in order to handle each client-server call we should
  1. define [JSON models](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/#locations%2Fmodel) for sending and receiving data (see [proposed enhancement](http://code.google.com/p/piriti/issues/detail?id=22))
  1. write a [generator for Action and Result data](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/dispatch/ReadLocations.java)
  1. write [a handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/dispatch/) which
    * sets `RequestBuilder` data (url, headers, body)
    * extracts the result by parsing the response body according to given model (this is generic, might be implemented by `AbstractRequestBuilderClientActionHandler`)
  1. [register the handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/RESTHandlerRegistry.java) (see [discussion](http://groups.google.com/group/gwt-platform/browse_thread/thread/df016bb90190f9af#))
  1. [run](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/LocationPresenter.java) gwtp `dispatch.execute()` (easy to re-use, e.g. by [locations auto-suggest](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/performances/PerformancePresenter.java))

This approach needs to be [enhanced](http://groups.google.com/group/gwt-platform/browse_thread/thread/df016bb90190f9af/306d24f851e7c56a#306d24f851e7c56a) with a specific REST dispatcher, e.g. the current one still goes through `com.google.gwt.user.client.rpc.*`

Question: how about using just a `RequestBuilder` wrapper, without gwtp-dispatch?

# RPC vs REST #

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/doi_dintre_noi6.jpg' align='right' border='0' />

When implementing GWT/GAE apps, should one use RPC or REST?

The RPC advantage is 1st class support in GWT/GAE, requiring now less dependencies and code to write than implementing a similar functionality using REST.

REST has many advantages though:

  * When you are done implementing your required app functionality, you also end up with APIs usable by 3rd parties. Others could integrate with your back-end through these APIs. You and others will be able to build more front-ends if desired (e.g. native Android/iPhone apps, Flex etc). Thanks to APIs, [innovation can happen elsewhere](http://wordpress.chanezon.com/?p=158).
  * You can easily implement automated APIs functional and performance testing and monitoring with tools such as JMeter.
  * Easy to debug: with tools like Firebug, you can inspect directly in browser what happened on a call and identify the area with having the issue: client or server.
  * Being independent of specific technologies, you can easily completely replace the back-end or front-end implementation any time, as long as new ones support same APIs. You can also build just the back-end, or just the front-end, for your customers. (e.g. build a [twitpic](http://twitpic.com/) GWT client hosted anywhere, since they [allow access from any origin](http://en.wikipedia.org/wiki/List_of_Web_services_allowing_access_from_any_origin))
  * Finally, as REST with JSON is standard on the web today, you can use the knowledge you gain for various other technologies.


Therefore, when building new GWT/GAE apps, I'd recommend to consider having two completely separate projects for back-end and front-end and use REST.


Let me conclude this section with a wish: Dear GAE and GWT developers, please make REST services simpler to expose from App Engine and consume from GWT. Some specific requests:
  * (GAE) have support for restlet-like functionality out of the box (or something like the internal tool shown at [How Google builds APIs](http://www.youtube.com/watch?v=nyu5ZxGUfgs))
  * (GWT) support piriti-like functionality for working with JSON ([piriti is easier to use than AutoBean](GWTREST.md))
  * (GWT) have a framework built around `RequestBuilder` and JSON for easily communicating with REST services (see the 5 steps above required now)


# Web services best practices #

Good web services are explorable ( maybe through a discovery API), predictable, consistent. Here are some best practices on building web services:

  * [How Google builds APIs](http://www.youtube.com/watch?v=nyu5ZxGUfgs)
  * [The interaction design of APIs](http://www.youtube.com/watch?v=VVovVjT_H8A)
  * [Understanding API Usage](http://www.readwriteweb.com/hack/2010/11/clay-loveless-understanding-ap.php)
  * [Avoid a Failed SOA: Business & Autonomous Components to the Rescue](http://www.infoq.com/presentations/SOA-Business-Autonomous-Components)

Other practices include versioning, paging, filtering, working with API keys, protecting against DOS attacks, supporting partial operations and custom verbs, using standard microformats when possible.


# Additional resources #
  * [SOAP vs. REST](http://stackoverflow.com/questions/76595?tab=votes#tab-top)
  * [Alternatives on consuming REST services with GWT](GWTREST.md)
  * [tire-d8](http://code.google.com/p/tire-d8/), open source GWT/GAE project using a REST architecture
  * [What makes a REST service](http://www.crummy.com/writing/speaking/2008-QCon/act3.html). The implementation above is at level two, missing hyperlinks.
  * [REST APIs must be hypertext-driven](http://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven)



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/ExposingData'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Exposing read-only data to 3rd parties' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/AdditionalResources'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Additional resources' /></a>
<a href='Hidden comment: NAV_END'></a>