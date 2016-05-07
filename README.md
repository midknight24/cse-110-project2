#CSE110 Project
###Xiangqun's API List
###Domain: http://coupletone.zxq.io/
###Api version: v1
###So all of the api webpages are in http://coupletone.zxq.io/apiv1/ dir.
###For example, if you want to request the login api, then you need to have a HTTP POST request to http://coupletone.zxq.io/apiv1/login.php
###with the arguments that listed in the argument part of Login section. After you do this, you'll receive a JSON from my server, which should
###contain the info that I listed in Results part in the Login section.
###If you still have any problem, feel free to contact me via Slack or QQ.


> ###Login
> ####login.php
> Arguments:

> token: user’s token that you get from Google.


> Results:

> bool userexists: if user exists, true for user exists, false otherwise.


> ###Register
> ####register.php
> Arguments:

> token: user’s token that you get from Google.

> email: user's email.

> username: user's preferred name.

> avatarurl: the url of user's google account avatar.


> Results:

> bool success: true if success, false otherwise. (REMINDER: if the email already exists)


> ###Logout
> ####logout.php
> Arguments:

> token: user’s token that you get from Google.


> Results:

> bool success: true if success, false otherwise.

> ###Search user by email
> ####searchuser.php
> Arguments:

> token:  user’s token that you get from Google.

> email: the email that being searched.


>Results:

>bool userexists: true if user exists, false otherwise.

>username: username of the user that being searched, if user exists.

>avatarurl: the url of user's google account avatar, if user exists.

###MORE ON THE WAY......
