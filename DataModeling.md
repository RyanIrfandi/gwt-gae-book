We discussed about [putting an application ideas in writing](SoYouHaveAnIdea.md) and [how to further clarify stories](VisualizingYourApp.md) we plan to implement. [With the development environment setup](GettingStarted.md), we'll think about how to model the data entities used by our application.





<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/statue_series_1.jpg' align='left' border='0' />


By now we have a pretty good idea about what our application should do and how it would look. We can start thinking about how to implement it.

# Data modeling #

Almost all applications are usually working with data, even if they don't actually store it and just pass it around as a [mashup](http://en.wikipedia.org/wiki/Mashup_(web_application_hybrid)). Thinking about [data modeling](http://en.wikipedia.org/wiki/Data_modeling) gets us started on [software design](http://en.wikipedia.org/wiki/Software_design).

To keep it light, doing an
[entity relationship model](http://en.wikipedia.org/wiki/Entity_relationship_model) will be enough for a small application. After all, we'd like to get to launch it sometime soon instead of writing docs all week, right?

## Exercise ##
On a piece of paper, draft the Culture Shows entity relationship model before going further.

## Exercise solution: Entity relationship model ##
![http://gwt-gae-book.googlecode.com/svn/wiki/assets/CultureShowsERM.png](http://gwt-gae-book.googlecode.com/svn/wiki/assets/CultureShowsERM.png)

Google Docs (Drawing) was used to build this.
## Notes ##
A member is part of a theater with a role (administrator, artist, assistant). Same member can belong to multiple theaters.

A theater has shows and locations. A performance consists of a show held in a location at a date and time.

For simplicity, we won't handle the case when a location is shared by more theaters (say a bar or concert hall, or theater which goes on tour). For these cases, the theater owner will have to add locations to her theater, even though these locations might be already defined somewhere. (one idea could be to use Google Places instead of locations, but Places API doesn't have search by address / name yet, just by geo-location).

A theater has contact info. Locations also may have contact info (if they are not owned by theater but rather rented, or a theater might have two locations in different city areas, each with it's own phone to make ticket reservations).

Try to avoid generic entities such as "User". Besides Member, here we might also have Spectator (Claudia).

<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/GettingStarted'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Getting started' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/BuildingLandingPage'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Building the landing page' /></a>
<a href='Hidden comment: NAV_END'></a>