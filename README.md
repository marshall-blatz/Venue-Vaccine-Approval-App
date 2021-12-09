# Venue-Vaccine-Approval-App
# COVID-19 Registration App

### Problem Statement
The goal is to develop a GUI-based platform/app for a uniform vaccine registration system for both individuals and organizations. The developed platform will ideally utilize a persistent storage of individuals and businesses with network based communications to communicate between the clients and server. Individual users should be able to register their vaccination, with extra validation to combat false registration. Organizations should be able to register and list their Covid-19 rules and regulations. After registration, they should be able to confirm the claimed vaccination status of individuals who want to get into their venue, while maintaining their privacy. 

### Developer Documentation
The designed COVID-19 registration app is largely GUI based, utilizing a network connected SQL database to provide simple server storage that can be accessed by a client. The majority of the source code is making the GUI and the logic to make it functional. Each page of our GUI functions using its own controller and model class that implement all the code needed. The controller classes house the methods and variables that are called by directly interacting with our app interface while the model classes house all the logic needed to verify everything and any access to the server database that was needed. Specific switch scene methods were implemented throughout the various interface packages to allow the GUI to actually switch pages to the new interface needed, for example when a user logs in and needs to be taken to the main app screen a switch scene method would be called to initialize the app with their info then do the switch. Other methods used extensively by the various GUI screens were the refresh methods that would pull any newly updated database info and then update the GUI display with it, simple update methods that just updated database info with info entered into the GUI fields and the methods used by venues to approve, deny, and request access from users. These methods required a lot of communication between the GUI and the network database to verify the info selected on the GUI, change whether or not a user was approved after they had requested access and then update both the venue and user database to have the user sorted into the right list. This utilized pulling info from each database, verifying everything, and then using lists filled with the info of who was requesting access and who was approved to properly sort users and then correctly update the database so their new status could be shown by their profile.

UML Diagram:

![COVID-19 Registration App Project](https://class-git.engineering.uiowa.edu/swd2021fall/team4_swd/-/raw/main/Project/UML.png)

### User Documentation
The apps user experience will differ depending on what kind of user is accessing the registration service. Initially starting up the app you are met with the entry screen where you can choose to either enter as an individual user or an organization. If entering as am individual is chosen you are taken to a screen where you are presented with login or sign up information. If you have previously signed up to the registration service before simply login by entering your username and password then pressing the button to take you to the user's main app interface. If you haven't registered with the service before simply enter the required info into the fields and press the sign up button, after which your info will be securely stored in the database and you will be taken to the main user interface. Once you have logged into the app you are presented with the main interface consisting of three tabs, each with different info, a refresh button to refresh the page and a logout button. The first tab simply displays the logged in user's information pulled from the database with an option to upload an image of your vaccine card and to change your password. The next tab displays a list of all the registered venues that the user can request access to. To do so, simply select the venue which will display all the venue's information and then press the request access button. The third tab simply displays the list of venues the user has already been approved to access, again selecting the venue will display their information. Accessing the registration service as an organization is slightly different but not by a lot. The login process is exactly the same but with the required sign up information being business specific. Once the organization reaches their main interface they have different option than the individual users. The first of which is the main page where they are presented with simple choices of what COVID guidelines they have and a field for any additional info they want to enter and button used to update their stored database info. The next organization tab is simply used to approve or deny individuals requesting access to their venue. The are presented with a list of usernames that once selected will display only the information relevant to make the decision of whether they should be approved or not. The final tab is a page used to update simple information about the organization like their location, name and password.

### Source Code
[Click here to view source code](https://class-git.engineering.uiowa.edu/swd2021fall/team4_swd/-/tree/main/Project/src)
