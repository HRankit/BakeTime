

# Welcome to BakeTime!

Hi! This project is Baking App Project meant for Android Developer Nanodegree provided by  **UDACITY**. 


# Requirements

 - [x] Display recipes
 - [x] App Navigation
 - [x] Utilization of RecyclerView
 - [x] Master Detail Flow and Fragments
 - [x] Exoplayer(MediaPlayer) to display videos
 - [x] Proper utilization of video assets
 - [x] Proper network asset utilization
 - [x] Espresso UI Testing
 - [x] Third-party libraries
 - [x] Application has a companion homescreen widget.
 - [x] Widget displays ingredient list for desired recipe.

# Features

With the app, you can:

 - Json is parsed and all recipe names are shown to the user.
 - The user selects the recipe and steps are shown.
 - Master Detail flow is implemented.
 - Tablets will show everything on one screen whereas mobile phones will start necessary activities.
 - Widget has been added, if the user clicks on the recipe name in the widget, the appropriate steps are shown.
 - User can also see the ingredients required for the recipe by clicking the ingredients button in the widget.

 

# Libraries

* [Retrofit](https://github.com/square/retrofit)
* [Picasso](https://github.com/square/picasso)
* [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
* [Exoplayer](https://github.com/google/ExoPlayer)
* [Timber ](https://github.com/JakeWharton/timber)
* Espresso UI Testing





# Animations

Simple slide in-out animations have been used. Also minor animations to move views along X axis have been implemented in Master Detail Flow. Check the animation below.
![Look and Feel of App](https://thumbs.gfycat.com/NegativeNippyKarakul-size_restricted.gif)
X axis movement of the Steps.

# Android Architecture Components

View Model provides an efficient way for managing the Orientation Changes without fetching data again from Database or Network. The use of **Retrofit alongside ViewModel** helps us to overcome what was previously a nightmare for developers. Gone are the days of query getting recalled on orientation changes.

![Small Screen Look](https://image.ibb.co/b49Y8z/device_2018_08_05_111726.png)
Small Screen Look
***
# Video
Get an idea of the app.


***
![Mobile Phones General App Usage](https://thumbs.gfycat.com/SarcasticUnkemptBaiji-size_restricted.gif)
Mobile Phones App Flow.
***


![Tablet View](https://thumbs.gfycat.com/UncommonPoorBlackpanther-size_restricted.gif)
Tablet App Flow.
*****
![Widget](https://thumbs.gfycat.com/LiquidDampGardensnake-size_restricted.gif)
Widget Showing Ingredients of desired recipe.

*****

# License
This project was submitted by HRankit  as part of the Nanodegree At Udacity.

Image Assets used in this app are Copyright (c) of Dr. Nida. Under **CC BY-NC-ND 4.0** you are required to **Attribute Dr. Nida**, Not use those for commercial products, Not share, derive any images from original image assets.
```
Licensed under CC BY-NC-ND 4.0;
You may not use this file/files except in compliance with the License.
You may obtain a copy of the License at

   https://creativecommons.org/licenses/by-nc-nd/4.0/

Every use of Image Asset must have attribution of Dr. Nida in the Project.
```


Code Copyright (c) 2018 HRankit.

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
`````
