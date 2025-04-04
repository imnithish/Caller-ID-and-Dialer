# Who Calling

Sample app demonstrating basic Caller ID/Spam identification and Dialer features.

### Topics covered

* Fetch spam list from a dummy remote source ✅
    * Retrofit ✅
* Caller Identification Popup ✅
    * Caller Name ✅
    * Phone Number ✅
    * Location ✅
    * Spam Identification ✅
    * Popup dismissible or interactable ✅
* Permission ✅
    * READ_CALL_LOG, READ_CONTACTS, SYSTEM_ALERT_WINDOW, CALL_PHONE, etc ✅
* Dialer UI ✅
    * Dial Call ✅
    * Match Contact ✅
* Call Log ✅
    * Spam Identification wrt remote source ✅
    * Dial Call ✅
* Search Contact ✅
* Default Dialer Setting ❌
* Arch ✅
    * MVVM ✅
    * Dependency Injection ✅

### How to setup dummy api which have the spam numbers list?

* Goto https://designer.mocky.io/design
* Enter the below HTTP Response Body with your modifications
    ```
  {
  "numbers": [
    {
      "number": "+918907471155",
      "name": "Nitheesh",
      "isSpam": false,
      "location": "Trivandrum"
    },
    {
      "number": "+919876543210",
      "name": "John Doe",
      "isSpam": false,
      "location": "Mumbai, India"
    },
    {
      "number": "+441234567890",
      "name": "Spam Caller",
      "isSpam": true,
      "location": "London, UK"
    },
    {
      "number": "+15551234567",
      "name": "Alice Smith",
      "isSpam": false,
      "location": "New York, USA"
    }
  ] }
* Then you'll get something like 
  ```https://run.mocky.io/v3/e450275c-e62c-4771-9c50-b5ff97ba8875``` in that ```e450275c-e62c-4771-9c50-b5ff97ba8875``` is the ```MOCKY_NAME```
* Update it in https://github.com/imnithish/Caller-ID-and-Dialer/blob/819087d435e136cd1b3bb2445961649001f64b4d/app/src/main/java/com/imn/whocalling/util/Constants.kt

### Build

https://github.com/imnithish/Caller-ID-and-Dialer/blob/master/app/release/app-release.apk