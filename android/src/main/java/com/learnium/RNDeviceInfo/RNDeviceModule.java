package com.learnium.RNDeviceInfo;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nullable;

public class RNDeviceModule extends ReactContextBaseJavaModule {

  private static final String KEY_DEVICE_INFO = "DeviceInfo";

  ReactApplicationContext reactContext;

  public RNDeviceModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNDeviceInfo";
  }

  private String getCurrentLanguage() {
      Locale current = getReactApplicationContext().getResources().getConfiguration().locale;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          return current.toLanguageTag();
      } else {
          StringBuilder builder = new StringBuilder();
          builder.append(current.getLanguage());
          if (current.getCountry() != null) {
              builder.append("-");
              builder.append(current.getCountry());
          }
          return builder.toString();
      }
  }

  private String getCurrentCountry() {
    Locale current = getReactApplicationContext().getResources().getConfiguration().locale;
    return current.getCountry();
  }

  private Boolean isEmulator() {
    return Build.FINGERPRINT.startsWith("generic")
      || Build.FINGERPRINT.startsWith("unknown")
      || Build.MODEL.contains("google_sdk")
      || Build.MODEL.contains("Emulator")
      || Build.MODEL.contains("Android SDK built for x86")
      || Build.MANUFACTURER.contains("Genymotion")
      || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
      || "google_sdk".equals(Build.PRODUCT);
  }

  private Boolean isTablet() {
    int layout = getReactApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    return layout == Configuration.SCREENLAYOUT_SIZE_LARGE || layout == Configuration.SCREENLAYOUT_SIZE_XLARGE;
  }

  @ReactMethod
  public void getDeviceInfo(@NonNull final Promise promise) {
    promise.resolve(this.getWritableConstantsMap());
  }

  public @Nullable WritableNativeMap getWritableConstantsMap() {
    WritableNativeMap constants = new WritableNativeMap();

    PackageManager packageManager = this.reactContext.getPackageManager();
    String packageName = this.reactContext.getPackageName();

    constants.putString("appVersion", "not available");
    constants.putString("buildVersion", "not available");
    constants.putInt("buildNumber", 0);

    try {
      PackageInfo info = packageManager.getPackageInfo(packageName, 0);
      constants.putString("appVersion", info.versionName);
      constants.putInt("buildNumber", info.versionCode);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    String deviceName = "Unknown";

    try {
      BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
      deviceName = myDevice.getName();
    } catch(Exception e) {
      e.printStackTrace();
    }

    constants.putString("deviceName", deviceName);
    constants.putString("systemName", "Android");
    constants.putString("systemVersion", Build.VERSION.RELEASE);
    constants.putString("model", Build.MODEL);
    constants.putString("brand", Build.BRAND);
    constants.putString("deviceId", Build.BOARD);
    constants.putString("deviceLocale", this.getCurrentLanguage());
    constants.putString("deviceCountry", this.getCurrentCountry());
    constants.putString("uniqueId", Secure.getString(this.reactContext.getContentResolver(), Secure.ANDROID_ID));
    constants.putString("systemManufacturer", Build.MANUFACTURER);
    constants.putString("bundleId", packageName);
    constants.putString("userAgent", System.getProperty("http.agent"));
    constants.putString("timezone", TimeZone.getDefault().getID());
    constants.putBoolean("isEmulator", this.isEmulator());
    constants.putBoolean("isTablet", this.isTablet());
    constants.putBoolean("is24HourTime", DateFormat.is24HourFormat(this.reactContext));
    return constants;
  }

  @Override
  public @Nullable Map<String, Object> getConstants() {
    HashMap<String, Object> constantsWrapper = new HashMap<>();
    constantsWrapper.put(KEY_DEVICE_INFO, this.getWritableConstantsMap().toHashMap());
    return constantsWrapper;
  }
}
