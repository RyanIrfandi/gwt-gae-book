# Goals #
  * support JSON-only APIs
  * support custom headers for each call (use cases: authentication token, pagination, XSRF...), media type
  * support binary fields / multipart requests (e.g. [uploading a file](http://twitpic.com/api.do))
  * get request status, cancel request (use case: loading indicator)

## Nice to have ##
  * XSRF protection. Reason for being optional: for services requiring auth, use a HTTP header. Store the auth token value in GWT client, never in a cookie, so it can't be used by other sites.
  * caching, batching, centralized failure handling, undo/redo,  similar to [gwtp dispatch](http://code.google.com/p/gwt-platform/wiki/GettingStartedDispatch)
  * be able to demo the app offline, with easy to run mocked calls. One easy solution: have a local HTTP server with static JSON files in pre-determined places.
  * offline support - be able to run the app offline, and sync when going online (might be a generic problem not related to REST)


# Good alternative: GWTP dispatch, `RequestBuilder` and piriti #

[Generic helpers](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/#client%2Fdispatch)



For each client-server call, client should
  1. define [JSON models](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/#locations%2Fmodel) for sending and receiving data (see [proposed enhancement](http://code.google.com/p/piriti/issues/detail?id=22))
  1. write a [generator for Action and Result data](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/dispatch/ReadLocations.java)
  1. write [a handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/dispatch/ReadLocationsHandler.java) which
    * sets `RequestBuilder` data (url, headers, body)
    * extracts the result by parsing the response body according to given model (this is generic, might be implemented by `AbstractRequestBuilderClientActionHandler`)
  1. [register the handler](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/RESTHandlerRegistry.java) - could this be done automatically?
  1. [run](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/LocationPresenter.java) regular gwtp dispatch.execute()


# Good alternative: GWTP dispatch, GWT Restlet and piriti #
Comparison with `RequestBuilder`alternative:

PROs
  * Support for content negotiation
  * Better error / exception handling
  * Better support for HTTP methods
  * Piriti supports Restlet
  * no need to declare `public static final LocationReader JSON` in model, piriti-restlet handles this
  * easier to work with model from dispatch.execute(): `result.getLocations().locations` vs. `result.getLocations()`

CONs
  * yet another library to depend on (Restlet, with piriti-restlet)




# Good alternative: `RequestBuilder` and piriti #


For each client-server call, client should
  1. define JSON models for sending and receiving data
  1. set `RequestBuilder` data
  1. call a wrapper for executing `RequestBuilder`
  1. extract the result

TBD

# Poor alternative: `AutoBean` #

[Sample](http://code.google.com/p/gwt-gae-book/source/detail?r=829)

  * Model requires lots of boilerplate code (compare [AutoBean](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/LocationModel.java?spec=svn829&r=829) with [Piriti](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/locations/model/Location.java) version])
  * `AutoBean` entities aren't easy to use in the rest of the app, besides java/json conversion.


# TODO #
  * by default extract the JSON result in the generic handler by passing the model class when extending generic handler? Need to be able to overwrite this and access additional data from the response, such as HTTP headers

  * Automatically handle registering action handlers, somehow?
    * remove the need for LocationsHandlerRegistry

  * include final versions of [AbstractRequestBuilderClientActionHandler, DispatchRequestRequestBuilderImpl, RequestRuntimeException in gwtp](http://code.google.com/p/gwt-gae-book/source/browse/trunk/CultureShows/src/org/gwtgaebook/CultureShows/client/#client%2Fdispatch)

  * include [UrlBuilder](http://code.google.com/p/piriti/source/browse/trunk/restlet/src/main/java/name/pehl/piriti/restlet/client/UrlBuilder.java) in guava ?

  * use [RequestBuilder](http://code.google.com/p/gwt-platform/issues/detail?id=179) or [Restlet](http://code.google.com/p/tire-d8/source/browse/trunk/app/src/main/java/name/pehl/tire/client/dispatch/AbstractRestletClientActionHandler.java) implementation? `Restlet` requires yet another library and doesn't bring any major advantages compared to a `RequestBuilder` wrapper.

  * full sample including login using openid


# References #


http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/http/client/RequestBuilder.html

http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/http/client/Response.html

http://code.google.com/p/piriti/ (gson would have been great, but GWT doesn't support java reflection)

http://code.google.com/p/gwt-platform/issues/detail?id=179