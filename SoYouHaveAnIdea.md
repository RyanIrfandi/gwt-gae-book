

So you have a great idea about a new web application? Cool! Sometimes it might be best to simply jump and code it. Some other times you might want to flesh it out before you start implementing it.

# Putting ideas in writing #
Let's consider this idea: there is a small theater company in your city which has great shows. They also have a website which presents to the public the group and upcoming show performances. You'd like to enable somebody in the theater company to easily update the performances schedule on the website. Once you implement this functionality, you'd like to make it available to other theater companies across the world. Let's call this application Culture Shows.

You've just identified somebody's need that you could solve. "Somebody" is usually a human, which in software design is referred to as [persona](http://en.wikipedia.org/wiki/Persona_(marketing)).

# Personae #
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/actor_red.jpg' align='left' border='0' />
Who are the Culture Shows personae?

## Violeta ##
Violeta is the theater company manager. She works with the group to schedule shows, according to the availability of actors and theater hall. Violeta would like the world to know about upcoming shows, so they can come to them. Violeta couldn't care less about technology, but she is already using email and surfs the web to keep up to date with what happens around.

## Stefan ##
Stefan is an actor in the theater company. He also has a day job and knows how to use computers not only for email, but also to send newsletters to publications, to publish show photos on Facebook and to discuss with theater website designers for making updates such as shows schedule, description and photos.

## Claudia ##
Claudia loves going to theater shows. She usually looks at what shows are scheduled in her city for the next two weeks, so she can make plans with her friends.

# Expressing personae needs #
With this application, we want to satisfy the needs of Violeta, Stefan and Claudia. It helps if we  clearly identify their needs: we can then estimate and prioritize their order of implementation.

One form of expressing personae needs is  [user stories](http://en.wikipedia.org/wiki/User_story). See [how to write effective user stories](http://www.examiner.com/business-technology-in-kansas-city/how-to-write-effective-user-stories-a-agile-scrum-environment) and [how do they compare with use cases](http://www.stellman-greene.com/2009/05/03/requirements-101-user-stories-vs-use-cases/).

So what would be some of our user stories?

## User stories ##

As Violeta or Stefan, I want to easily update the shows schedule on our website myself, so that I don't waste time asking somebody else to do it for me.

As Claudia, I want to learn about upcoming shows so that I can make plans with my friends.


# Backlog grooming #
In [Scrum methodology](http://en.wikipedia.org/wiki/Scrum_(development)), the above user stories constitute a product backlog. We can estimate each story in [Story points](http://en.wikipedia.org/wiki/Planning_poker) and prioritize it. The first story is kinda large to estimate, so let's break it into smaller ones.

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/actor_white.jpg' align='right' border='0' />

# Exercise #

After reading all about scrum, user stories and story points, take a stab at developing the Culture Shows product backlog by yourself and estimating each story before going further.

Consider the story "As Violeta, I want to delete existing scheduled shows so that it reflects cancellations" to be 1 story point, and estimate the rest relatively to it.

# Culture Shows product backlog #

You did actually try to do this by yourself first, right? Go on, this chapter will just wait for you here until you have your own draft. You should decompose the two stories above into at least ten stories, each sized at most 13 story points. Then you should also sort them by priority.

# Culture Shows product backlog (for real) #
| **User story** | **Estimate** (story points) |
|:---------------|:----------------------------|
| As Violeta, I want to schedule show performances so that the world is aware about them  | 13                          |
| As Violeta, I want to create/update/delete shows so I can specify show details | 5                           |
| As Violeta, I want to schedule show performances in various locations so that I use the best location for a show or do tours | 5                           |
| As Violeta, I want to display a show schedule on the theater website as soon as I define it so that I don't waste time asking somebody else to edit it| 13                          |
| As Violeta, I want to edit existing scheduled performances so that I can correct any mistakes I might have done | 2                           |
| As Violeta, I want to delete existing scheduled performances so that I can handle cancellations | 1                           |
| As Stefan, I want to enable various websites to display my company's shows schedule so that more people find out about it| 13                          |
| As Violeta, I want to restrict access to scheduling shows so that only authorized persons manage it| 13                          |
| As Violeta, I want to use Culture Shows in my own language so that I don't have to learn English | 5                           |
| As Stefan, I want our Facebook group to display each show information two weeks in advance so that more people find out about it| 13                          |
| As Stefan, I want our Twitter account to display each show information two weeks in advance so that more people find out about it| 13                          |
| As Claudia, I want to make a reservation so that I know for sure I'll have a place to see the show when I go to theater | 40                          |
| As Violeta, I want to notify existing reservation owners about changes in schedule so that they find out about it and adjust their plans | 8                           |
| As Violeta, I want to remind existing reservation owners about shows two days in advance so that I'm sure they didn't forget and minimize the chances of them being late | 8                           |
| As Violeta, I want to display all shows on theater website with links to details so that I can have a portfolio | 5                           |

# Additional resources #

  * [Painless Functional Specifications](http://www.joelonsoftware.com/articles/fog0000000036.html)



<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/Introduction'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Introduction' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/VisualizingYourApp'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Visualizing your application in action' /></a>
<a href='Hidden comment: NAV_END'></a>