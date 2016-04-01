# 2012 UPDATE #
Instead of GWT/GAE, I highly recommend developing with backbone.js, node.js and mongodb. These fully take advantage of web standards and latest browser advancements, while bringing a higher productivity.

Try out [ownDJ](http://owndj.com/), an app for playing and mixing music online, which is more complex than the sample app presented in this book, but took me less time to develop. Feel free to look at the client-server calls and see beautiful REST, JSON-formatted communication.

# Table of contents #


### [Introduction](Introduction.md) ###
  * [Why a free, online book?](Introduction#Why_a_free,_online_book?.md) <br />
  * [How to read this book](Introduction#How_to_read_this_book.md) <br />
  * [Acknowledgements](Introduction#Acknowledgements.md) <br />
  * [About the author](Introduction#About_the_author.md) <br />

### [Clarifying your ideas before writing code](SoYouHaveAnIdea.md) ###
  * [Putting ideas in writing](SoYouHaveAnIdea#Putting_ideas_in_writing.md) <br />
  * [Personae](SoYouHaveAnIdea#Personae.md) <br />
    * [Violeta](SoYouHaveAnIdea#Violeta.md) <br />
    * [Stefan](SoYouHaveAnIdea#Stefan.md) <br />
    * [Claudia](SoYouHaveAnIdea#Claudia.md) <br />
  * [Expressing personae needs](SoYouHaveAnIdea#Expressing_personae_needs.md) <br />
    * [User stories](SoYouHaveAnIdea#User_stories.md) <br />
  * [Backlog grooming](SoYouHaveAnIdea#Backlog_grooming.md) <br />
  * [Exercise](SoYouHaveAnIdea#Exercise.md) <br />
  * [Culture Shows product backlog](SoYouHaveAnIdea#Culture_Shows_product_backlog.md) <br />
  * [Culture Shows product backlog (for real)](SoYouHaveAnIdea#Culture_Shows_product_backlog_(for_real).md) <br />
  * [Additional resources](SoYouHaveAnIdea#Additional_resources.md) <br />

### [Visualizing your application in action](VisualizingYourApp.md) ###
  * [Mockups](VisualizingYourApp#Mockups.md) <br />
    * [Exercise](VisualizingYourApp#Exercise.md) <br />
    * [Landing page](VisualizingYourApp#Landing_page.md) <br />
    * [Managing performances](VisualizingYourApp#Managing_performances.md) <br />
  * [Get feedback](VisualizingYourApp#Get_feedback.md) <br />

### [Are you using the right tool for the job?](TheRightToolForTheJob.md) ###
  * [Google Web Toolkit (GWT)](TheRightToolForTheJob#Google_Web_Toolkit_(GWT).md) <br />
  * [Google App Engine (GAE)](TheRightToolForTheJob#Google_App_Engine_(GAE).md) <br />
  * [Evaluating alternatives](TheRightToolForTheJob#Evaluating_alternatives.md) <br />
  * [GWT apps at Google](TheRightToolForTheJob#GWT_apps_at_Google.md) <br />
  * [Other GWT apps](TheRightToolForTheJob#Other_GWT_apps.md) <br />

### [Getting started](GettingStarted.md) ###
  * [Environment Setup](GettingStarted#Environment_Setup.md) <br />
    * [Useful Eclipse plugins](GettingStarted#Useful_Eclipse_plugins.md) <br />
    * [Useful Eclipse preferences](GettingStarted#Useful_Eclipse_preferences.md) <br />
  * [Getting Started](GettingStarted#Getting_Started.md) <br />
  * [Application source code](GettingStarted#Application_source_code.md) <br />
  * [Useful libraries](GettingStarted#Useful_libraries.md) <br />
  * [Useful tools](GettingStarted#Useful_tools.md) <br />

### [Data modeling](DataModeling.md) ###
  * [Data modeling](DataModeling#Data_modeling.md) <br />
    * [Exercise](DataModeling#Exercise.md) <br />
    * [Exercise solution: Entity relationship model](DataModeling#Exercise_solution:_Entity_relationship_model.md) <br />
    * [Notes](DataModeling#Notes.md) <br />

### [Building the landing page](BuildingLandingPage.md) ###
  * [Mockup](BuildingLandingPage#Mockup.md) <br />
  * [Page Layout](BuildingLandingPage#Page_Layout.md) <br />
  * [Exercise: build the mocked up UI](BuildingLandingPage#Exercise:_build_the_mocked_up_UI.md) <br />
  * [Implementation](BuildingLandingPage#Implementation.md) <br />
  * [Using native JavaScript](BuildingLandingPage#Using_native_JavaScript.md) <br />
  * [Mobile, tablets, TV and other devices](BuildingLandingPage#Mobile,_tablets,_TV_and_other_devices.md) <br />
  * [Model-View-Presenter (MVP)](BuildingLandingPage#Model-View-Presenter_(MVP).md) <br />
    * [Exercise](BuildingLandingPage#Exercise.md) <br />
    * [Exercise solution](BuildingLandingPage#Exercise_solution.md) <br />
  * [Additional resources](BuildingLandingPage#Additional_resources.md) <br />

### [Client-server communication](ClientServer.md) ###
  * [Pre-requisites](ClientServer#Pre-requisites.md) <br />
  * [Exercise](ClientServer#Exercise.md) <br />
  * [Exercise solution](ClientServer#Exercise_solution.md) <br />
  * [Auto-generated Action and Result](ClientServer#Auto-generated_Action_and_Result.md) <br />
  * [More code cleanup](ClientServer#More_code_cleanup.md) <br />
  * [Summary](ClientServer#Summary.md) <br />

### [Authentication with OpenID](Authentication.md) ###
  * [Signing in using OpenID](Authentication#Signing_in_using_OpenID.md) <br />
  * [Getting user info in server handlers](Authentication#Getting_user_info_in_server_handlers.md) <br />
  * [Custom widgets & events](Authentication#Custom_widgets_&_events.md) <br />

### [Storing entities in App Engine datastore](StoringData.md) ###
  * [Exercise: build Manage Performances client side](StoringData#Exercise:_build_Manage_Performances_client_side.md) <br />
    * [Exercise solution](StoringData#Exercise_solution.md) <br />
  * [Coding our data model](StoringData#Coding_our_data_model.md) <br />
  * [Datastore setup](StoringData#Datastore_setup.md) <br />
    * [One-to-Many relationships](StoringData#One-to-Many_relationships.md) <br />
    * [Many-to-Many relationships](StoringData#Many-to-Many_relationships.md) <br />
  * [Basic data storage](StoringData#Basic_data_storage.md) <br />
  * [Creating members and theaters](StoringData#Creating_members_and_theaters.md) <br />
    * [Hey client, remember me?](StoringData#Hey_client,_remember_me?.md) <br />
  * [Additional resources](StoringData#Additional_resources.md) <br />

### [Listing saved performances](ListingSchedule.md) ###
  * [A quick, basic solution](ListingSchedule#A_quick,_basic_solution.md) <br />
    * [Exercise](ListingSchedule#Exercise.md) <br />
    * [Exercise solution](ListingSchedule#Exercise_solution.md) <br />
  * [Advanced use cases](ListingSchedule#Advanced_use_cases.md) <br />
  * [Exercise](ListingSchedule#Exercise.md) <br />
  * [Enhancing the basic solution](ListingSchedule#Enhancing_the_basic_solution.md) <br />
  * [Handling item selection](ListingSchedule#Handling_item_selection.md) <br />
  * [Styling data presentation widgets](ListingSchedule#Styling_data_presentation_widgets.md) <br />
  * [Additional resources](ListingSchedule#Additional_resources.md) <br />

### [Updating and deleting performances](ManagingPerformances.md) ###
  * [Server handler](ManagingPerformances#Server_handler.md) <br />
  * [Client view](ManagingPerformances#Client_view.md) <br />
  * [Client presenter](ManagingPerformances#Client_presenter.md) <br />

### [Quality assurance](QualityAssurance.md) ###
  * [Pre-requisites](QualityAssurance#Pre-requisites.md) <br />
  * [When should QA start?](QualityAssurance#When_should_QA_start?.md) <br />
  * [Personae](QualityAssurance#Personae.md) <br />
  * [User stories](QualityAssurance#User_stories.md) <br />
  * [Metrics and KPIs](QualityAssurance#Metrics_and_KPIs.md) <br />
  * [User interface](QualityAssurance#User_interface.md) <br />
  * [Functional specifications](QualityAssurance#Functional_specifications.md) <br />
  * [Design/technical specifications](QualityAssurance#Design/technical_specifications.md) <br />
  * [Code](QualityAssurance#Code.md) <br />
    * [Static code analysis tools](QualityAssurance#Static_code_analysis_tools.md) <br />
    * [Code reviews](QualityAssurance#Code_reviews.md) <br />
  * [Testing](QualityAssurance#Testing.md) <br />
    * [Unit testing](QualityAssurance#Unit_testing.md) <br />
      * [Exercise](QualityAssurance#Exercise.md) <br />
      * [Exercise solution](QualityAssurance#Exercise_solution.md) <br />
    * [Integration testing](QualityAssurance#Integration_testing.md) <br />
      * [Exercise](QualityAssurance#Exercise.md) <br />
      * [Exercise solution](QualityAssurance#Exercise_solution.md) <br />
    * [Web services testing](QualityAssurance#Web_services_testing.md) <br />
    * [UI testing](QualityAssurance#UI_testing.md) <br />
    * [Performance testing](QualityAssurance#Performance_testing.md) <br />
    * [Manual test cases](QualityAssurance#Manual_test_cases.md) <br />
    * [Scenario testing](QualityAssurance#Scenario_testing.md) <br />
    * [Additional resources](QualityAssurance#Additional_resources.md) <br />
  * [Other areas](QualityAssurance#Other_areas.md) <br />

### [Managing shows](ManagingShows.md) ###
  * [Mockup](ManagingShows#Mockup.md) <br />
  * [Client side functionality](ManagingShows#Client_side_functionality.md) <br />
  * [Server side functionality](ManagingShows#Server_side_functionality.md) <br />

### [Exposing read-only data to 3rd parties](ExposingData.md) ###
  * [Exposing performances data as JSON](ExposingData#Exposing_performances_data_as_JSON.md) <br />
  * [Consuming data on 3rd party websites](ExposingData#Consuming_data_on_3rd_party_websites.md) <br />
    * [Setting up YQL](ExposingData#Setting_up_YQL.md) <br />
    * [Displaying data on a test website](ExposingData#Displaying_data_on_a_test_website.md) <br />
    * [Displaying data on a real website](ExposingData#Displaying_data_on_a_real_website.md) <br />

### [Managing locations with REST APIs](ManagingLocations.md) ###
  * [About REST](ManagingLocations#About_REST.md) <br />
  * [Exposing REST web services with App Engine](ManagingLocations#Exposing_REST_web_services_with_App_Engine.md) <br />
  * [Consuming REST services with GWT](ManagingLocations#Consuming_REST_services_with_GWT.md) <br />
  * [RPC vs REST](ManagingLocations#RPC_vs_REST.md) <br />
  * [Web services best practices](ManagingLocations#Web_services_best_practices.md) <br />
  * [Additional resources](ManagingLocations#Additional_resources.md) <br />

### [Additional resources](AdditionalResources.md) ###
  * [Discussion groups](AdditionalResources#Discussion_groups.md) <br />
  * [Blogs](AdditionalResources#Blogs.md) <br />
  * [Open source applications using GWT/GAE](AdditionalResources#Open_source_applications_using_GWT/GAE.md) <br />
  * [Miscellaneous](AdditionalResources#Miscellaneous.md) <br />

### [Possible upcoming chapters](TODO.md) ###