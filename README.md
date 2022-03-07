# react-native-guest-survey-sdk

React Native module for Geo4Cast with survey engine support.

## Installation

```sh
npm install react-native-guest-survey-sdk
```

yarn

```sh
yarn add react-native-guest-survey-sdk
```

If you have yarn scripts enabled, don't forget to run <mark style="background-color: lightgrey">yarn</mark> to install the dependencies automatically.

As for iOS installation, disable flipper and add <mark style="background-color: lightgrey">use_frameworks!</mark> in the target of the podfile. Before reading running the module, make sure to read [iOS](#iOSHeader) and [Android](#andHeader) sections below for in depth configuration before you can run the module

## Usage

To use the module, you have to first import it where needed at the top of the page.

```js
import GuestSurveySdk from 'react-native-guest-survey-sdk';
```

Make sure to call this function as soon as your app launches and before using the SDK

```
GuestSurveySdk.configure(secretKey:string, programKey:string, language:string, enableDebugging:bool, (done: any) => {})
```

**Start Collection**

Initializes data collection with setting user_id

```
GuestSurveySdk.startCollection(<userId>)
```

**StopCollection**

Stop all data collection by the SDK and deletes _should only be triggered after user logout_

```
GuestSurveySdk.stopcollection()
```

**GetUserSurveys**

Get User Survey List in JSON format. Use any item in this list to later present a survey using _triggerSurvey function_

```
GuestSurveySdk.getUserSurveys((values: [any]) => {})
```

**TriggerSurvey**

_Trigger ConfirmitMobile SDK to present the survey according to the programKey, eventName and any other parameters(such as language, attractionId, etc..) that is intended to be received and viewed from the control center. This method should present a native Webview controller inApp with designed survey components according the Confirmit Survey Design_

```
GuestSurveySdk.triggerSurvey(<programKey:str>, <eventName:str>, <parameters:any>)
```

**AddPushNotifcationToken**

_**OPTIONAL**
Add Remote notifications token to the Geo4Cast payload. To be set after the response of **didRegisterForRemoteNotificationsWithDeviceToken**. This function should load the device token in the SDK, if needed._

```
GuestSurveySdk.addPushNotificationToken(token:<**notifications_token**>**>)
```

**SendLog**

_Opens default mail app on device to send the app generated logs from Geo4CastSDK debugging.Do not use after use logout as the logs are deleted on user session logout._

```
GuestSurveySdk.sendLog()
```

**_IMPORTANT: Naming of the functions and the their final functionalities, callbacks, etc.. are tentative and might change._**

### Additional Resources

[DigitalFeedbackMobileSDK Documentation](https://github.com/Confirmit/DigitalFeedbackMobileSDK)

#### Native Geo4CastSDK Mobile Documentation (iOS/Android)

<a name='iosHeader'></a>

# iOS Developer Setup for Geo4CastSDK

## Podfile

Add framework support in the target part of the Podfile and comment out flipper for the module wrapper to work.

```
use_frameworks!
#use_flipper!()
```

If bitcode is enabled in your project, disable it for the Geo4CaseSDK manually, or add the below in your post_install script in the podfile

```ruby
      installer.pods_project.targets.each do |target|
       target.build_configurations.each do |config|
        config.build_settings['ENABLE_BITCODE'] = 'NO'
       end
    end
```

then, run pod update in the terminal

```
pod update
```

The value of this key should be received with your onboarding kit from ALEIA

## Permissions

In the _info.plist_ file, don't forget to add the relevant permissions for our SDK to work.
_Omit the unneeded keys and personalize the values with your own message_.

```
<key>NSBluetoothAlwaysUsageDescription</key>
<string>Grant the bluetooth</string>
<key>NSBluetoothPeripheralUsageDescription</key>
<string>$(PRODUCT_NAME) needs your permission to access bluetooth in order to scan nearby bluetooth devices</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>Please grant location access, if not, $(PRODUCT_NAME) will not be able to get your location.</string>
<key>NSLocationAlwaysUsageDescription</key>
<string>Please grant location access, if not, $(PRODUCT_NAME) will not be able to get your location.</string>
<key>NSLocationWhenInUseUsageDescription</key>
<string>Please grant location access, if not, $(PRODUCT_NAME) will not be able to get your location.</string>
<key>NSMotionUsageDescription</key>
<string>Please grant motion access, if not, $(PRODUCT_NAME) will not be able to find your mode of transport.</string>

```

## Background Modes

Our SDK runs silently in the background, please enable the following **Background modes**.
In the info.plist file.

```

<key>UIBackgroundModes</key>
<array>
<string>bluetooth-central</string>
<string>bluetooth-peripheral</string>
<string>fetch</string>
<string>location</string>
<string>processing</string>
<string>remote-notification</string>
</array>

```

<a name='andHeader'></a>

# Android Developer Setup for Geo4CastSDK

## Secret Key

Before using our SDK, you could add a new key in your project's String file.

```

<string name="Geo4CastSDKSecretKey">_key_supplied_by_vendor_</string>

```

The value of this key should be received with your onboarding kit from ALEIA

## Permissions

In the AndroidManifest file, don't forget to add the relevant permissions for our SDK to work.

```
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_NOTIFICATION_POLICY" />
    <uses-permission android:name="android.permission.WRITE_APN_SETTINGS" />
    <uses-permission android:name="android.permission.ACTIVITY_RECOGNITION"/>
    <uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION"/>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature
        android:name="android.hardware.bluetooth_le"
        android:required="true" />

```

In the strings file, Omit the unneeded keys and personalize the values with your own message.

```

<string name="location_access">This app needs location access</string>
<string name="location_access_msg">Please grant location access so this app can detect peripherals.</string>
<string name="functionality_limited">Functionality limited</string>
<string name="functionality_limited_msg">Since location access has not been granted, this app will not be able to discover beacons when in the background.</string>

<string name="location_service">This app needs active Location Service</string>
<string name="location_service_msg">Location Service is disabled, please active Location Service in Settings to send you the survey corresponding.</string>
<string name="bluetooth_service_msg">Bluetooth is disabled, please active Bluetooth service to send you the survey corresponding.</string>
<string name="active_permissions_msg">please active permissions to send you the survey corresponding.</string>
<string name="active_service_msg">The application service will be degraded because some fonctions are not enabled and/or allowed.</string>
<string name="active_location_service_always_msg">Thanks for choosing the mode "'"all the time"'" in the next pop up</string>
<string name="purpose_location_service_always_msg">For an optimal app result, please chosse the mode "'"always"'" for geolocation access</string>

```

## Create a file local.properties

1. Go to your Project -> Android
2. Create a file local.properties
3. Open the file
4. Paste your Android SDK path depending on the operating system:

   4.a Windows
   sdk.dir=C:\\Users\\UserName\\AppData\\Local\\Android\\sdk

   4.b Linux or MacOS
   sdk.dir = /home/USERNAME/Android/sdk

Replace USERNAME with your user name

# Additional

If there are two apps in the simulator or on your real device and both of them are working fine after you run yarn android, first go to this Dir: android/app/src/main/AndroidManifest.xml, second, find

```
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>

```

Then, change it to

```
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.DEFAULT" />
        </intent-filter>

```

## Distribution Channels

The react-native model will be hosted on NPM. Link to be shared later

### iOS Dependencies

[Geo4CastSDK](https://cocoapods.org/pods/Geo4CastSDK) is available on Cocoapods for iOS

[DigitalFeedbackMobileSDK](https://cocoapods.org/pods/ConfirmitMobileSDK) is available on Cocoapods iOS

### Android Dependencies

[Geo4CastSDK](https://search.maven.org/artifact/fr.it4pme/geo4cast-sdk/1.0.2/aar) is available on Maven for Android

```

implementation 'fr.it4pme:geo4cast-sdk:1.0.2'

```
