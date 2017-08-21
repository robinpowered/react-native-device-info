/**
 * @providesModule react-native-device-info
 */

var RNDeviceInfo = require('react-native').NativeModules.RNDeviceInfo;
var DeviceInfo = RNDeviceInfo.DeviceInfo;
module.exports = {
  getUniqueID: function () {
    return DeviceInfo.uniqueId;
  },
  getInstanceID: function() {
    return DeviceInfo.instanceId;
  },
  getDeviceId: function () {
    return DeviceInfo.deviceId;
  },
  getManufacturer: function () {
    return DeviceInfo.systemManufacturer;
  },
  getModel: function () {
    return DeviceInfo.model;
  },
  getBrand: function () {
    return DeviceInfo.brand;
  },
  getSystemName: function () {
    return DeviceInfo.systemName;
  },
  getSystemVersion: function () {
    return DeviceInfo.systemVersion;
  },
  getBundleId: function() {
    return DeviceInfo.bundleId;
  },
  getBuildNumber: function() {
    return DeviceInfo.buildNumber;
  },
  getVersion: function() {
    return DeviceInfo.appVersion;
  },
  getReadableVersion: function() {
    return DeviceInfo.appVersion + "." + DeviceInfo.buildNumber;
  },
  getDeviceName: function() {
    return DeviceInfo.deviceName;
  },
  getUserAgent: function() {
    return DeviceInfo.userAgent;
  },
  getDeviceLocale: function() {
    return DeviceInfo.deviceLocale;
  },
  getDeviceCountry: function() {
    return DeviceInfo.deviceCountry;
  },
  getTimezone: function() {
    return DeviceInfo.timezone;
  },
  isEmulator: function() {
    return DeviceInfo.isEmulator;
  },
  isTablet: function() {
    return DeviceInfo.isTablet;
  },
};

module.exports.DeviceInfo = DeviceInfo;
