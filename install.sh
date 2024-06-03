./gradlew :app:assembleDebug

SCRIPT_DIR=$(dirname "$0")
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

adb install -t "$SCRIPT_DIR/$APK_PATH"
adb shell am start -n "com.chx.androidcloud/com.proxy.androidcloud.LoginActivity"