

# Protecting against cross-site request forgeries (XSRF) #

**Pre-requisites**
[Cross-site request forgery](http://en.wikipedia.org/wiki/Cross-site_request_forgery)

What's the problem we're trying to solve? If Violeta is signed in, and she navigates to badsite.com, then badsite.com can make some actions in our application on behalf of Violeta. Although [same origin policy](http://en.wikipedia.org/wiki/Same_origin_policy) prevents badsite.com making JavaScript calls, badsite.com can still load URLs such as
```
<img src="http://cultureshows.appspot.com/logout/" />
```
and log out Violeta for example. Other sites might even perform other actions on GET, such as `delete_contact.php?id=14`.

One way to protect agains XSRF is to allow only requests which have your site as referral. A more effective way is to issue a token on user login, which can be stored by our application client in a cookie and cannot be accessed by badsite.com, then pass this token on each request and have the server check the token validity.

TODO
# Action validators #
TODO

# Giving feedback to users on communication status #
loading indicator

TODO


# Generic error handling #
TODO

# Client caching #
TODO

# Server caching #
TODO