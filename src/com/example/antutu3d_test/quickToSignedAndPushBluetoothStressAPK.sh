#!/bin/sh
java -jar ./signapk.jar ./platform.x509.pem ./platform.pk8 app-debug-androidTest.apk app-debug-androidTest_signed.apk
print 'signed app-debug-androidTest.apk'
java -jar ./signapk.jar ./platform.x509.pem ./platform.pk8 app-debug.apk app-debug_signed.apk
print 'signed app-debug.apk'
adb push app-debug_signed.apk system/app
print 'push app-debug_signed.apk'
adb push app-debug-androidTest_signed.apk system/app
print 'push app-debug-androidTest_signed.apk'
adb shell am instrument -w -r   -e debug false -e EnableIterations 10 -e DiscoverableIterations 10 -e ScanIterations 10 -e class com.xiaomi.wifistress.xiaomibluetoothstresstests.BluetoothTest -w com.xiaomi.wifistress.xiaomibluetoothstresstests.test/android.support.test.runner.AndroidJUnitRunner
print 'run...'
