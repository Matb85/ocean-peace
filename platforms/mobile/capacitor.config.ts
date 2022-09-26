import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "ocean.peace.mobile",
  appName: "Ocean Peace",
  bundledWebRuntime: false,
  webDir: "../web/build",
  // plugins: {
  //   SplashScreen: {
  //     launchShowDuration: 10000,
  //     launchAutoHide: true,
  //     backgroundColor: "ffffffff",
  //     androidScaleType: "CENTER_CROP",
  //     showSpinner: false,
  //     splashFullScreen: true,
  //     splashImmersive: true,
  //   },
  // },
  // comment out this line for production
  // adjust for development whenever necessary
  // server: { url: "http://10.0.2.2:3000", cleartext: true }, // for the emulator
  //server: { url: "http://192.168.1.6:3000", cleartext: true }, // for real devices
};

export default config;
