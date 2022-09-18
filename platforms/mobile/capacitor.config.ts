import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "ocean.peace.mobile",
  appName: "ocean-peace-mobile",
  bundledWebRuntime: false,
  webDir: "../web/build",
  plugins: {
    SplashScreen: {
      launchShowDuration: 10000,
      launchAutoHide: true,
      backgroundColor: "ffffffff",
      androidScaleType: "CENTER_CROP",
      showSpinner: false,
      splashFullScreen: true,
      splashImmersive: true,
    },
  },
  // comment out this line for production
  // adjust for development whenever necessary
  //server: { url: "http://192.168.1.6:3000", cleartext: true },
};

export default config;
