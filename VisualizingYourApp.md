We discussed about [putting an application ideas in writing](SoYouHaveAnIdea.md), personae and user stories. Next, we'll see what other steps we can do to further clarify stories we plan to implement.



You now have a clear product backlog. Coding time, right? Let's do an imagination exercise first. Pretend all those user stories are already implemented. How would your application functionality look? Will it actually satisfy your personae's needs?

# Mockups #
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/images/grasping_the_sun.jpg' align='right' border='0' />
Let's try to illustrate how our UI would look like using [Evolus Pencil](http://pencil.evolus.vn/en-US/Home.aspx), the best completely free mockup building tool I've found.

Looking at the top user stories, could we cover these in the managing performances page?
  * As Violeta, I want to schedule show performances so that the world is aware about them
  * As Violeta, I want to schedule show performances in various locations so that I use the best location for a show or do tours
  * As Violeta, I want to edit existing scheduled performances so that I can correct any mistakes I might have done
  * As Violeta, I want to delete existing scheduled performances so that I can handle cancellations


## Exercise ##
Make a few drafts of the UI by yourself before going further. Consider
  * [user stories](SoYouHaveAnIdea.md)
  * user acquisition
  * [usability](http://en.wikipedia.org/wiki/Usability)

After a first draft of mockup you're happy with, brainstorm about it. For example:
  * Should fields in the grid be editable, so we avoid edit buttons?
    * unintuitive to use and harder to implement
    * how about clicking on a row will fill the fields at top and the "Schedule" button is replaced with "Update" and "Delete from schedule" buttons?
  * How to delete entries?
  * Show and location: text entry with auto-completion or combo boxes?
  * Need to edit shows to specify more details (summary, link to show homepage, actors). Could do via a "Manage shows" page
  * Is this UI fully usable on touch-based devices?


Here's a proposed UI:


## Landing page ##


<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/landing.png' border='1' />


## Managing performances ##

<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/performances.png' border='1' />

[Mockups source](http://gwt-gae-book.googlecode.com/svn/wiki/assets/CultureShowsMocks.ep)


# Get feedback #
You can usually find target personae among your friends, if you don't have access to possible customers yet. Get them to provide feedback about your ideas and do [usability testing](http://en.wikipedia.org/wiki/Usability_testing) using mockups you developed. Changes are much cheaper to do now than once the code is written, tested and released.

<a href='Hidden comment: NAV_START'></a>
<a href='http://code.google.com/p/gwt-gae-book/issues/entry'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/envelope.png' border='0' title='Send feedback' /></a>
<img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/spacer.png' border='0' />
<a href='http://code.google.com/p/gwt-gae-book/wiki/SoYouHaveAnIdea'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/prev.png' border='0' title='Previous chapter: Clarifying your ideas before writing code' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TableOfContents'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/contents.png' border='0' title='Table Of Contents' /></a>
<a href='http://code.google.com/p/gwt-gae-book/wiki/TheRightToolForTheJob'><img src='http://gwt-gae-book.googlecode.com/svn/wiki/assets/next.png' border='0' title='Next chapter: Are you using the right tool for the job?' /></a>
<a href='Hidden comment: NAV_END'></a>