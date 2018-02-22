# MC Night Mode
[![](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#JELLY_BEAN"><img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat" alt="API" /></a>

Simple Android Library to enable/disable night mode (like NightShift for iOS). This library provides you a background Service that overlap a filter on your screen device; this will reduce the amount of blue on your display.

Screenshot
:-------------------------
![](https://i.imgur.com/DNnnX2Q.gif)

## Install
Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
Add this to your module build.gradle

```gradle
   dependencies {
        compile 'com.github.PuffoCyano:mcnightmode:v1.1'
    }

```
### Usage
Keep in mind to allow the permission "Settings.ACTION_MANAGE_OVERLAY_PERMISSION" to allow Service to draw on other apps:
```java
if (Build.VERSION.SDK_INT >= 23)
{
 if (!Settings.canDrawOverlays(this))
 {
    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
    startActivity(myIntent);
 }
}
```
In your activity/fragment call service "NightService":
```java
    startService(new Intent(context, NightService.class));
```
If you want you can pass a parameter to service to set filter's intensity (default value is 5 if you don't pass anything):
```java
    startService(new Intent(context, NightService.class).putExtra("amount",value)); // value = 0 - 10
```
However if you run Android 8 it is important start service with this code:
```java
if (Build.VERSION.SDK_INT >= 26)
{
   startForegroundService(new Intent(context, NightService.class).putExtra("amount",value));
}
else
{
   startService(new Intent(context, NightService.class).putExtra("amount",value));
}
```

## License
```
Copyright 2018 Alessandro Marino

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
