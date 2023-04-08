## QR Scanner

A simple QR scanner that uses the Google Vision API.

## Installation

Add it in your root build.gradle at the end of repositories:
```bash
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency:
```bash
dependencies {
  implementation 'com.github.maxwell-xin:qr-scanner:Tag'
}
```

Simple Usage
------------

1.) Add to your AndroidManifest.xml file:

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

2.) Add to your activity_main.xml file:

```xml
<com.maxwellxin.qr_scanner.QrScanner
    android:id="@+id/qrScanner"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

3.) Add to your activity:

```kotlin
qrScanner.setupControls(object : ScanListener {
    override fun result(result: String?) {
        println("Result: $result")
    }
})        
```
