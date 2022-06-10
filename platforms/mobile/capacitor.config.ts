import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "ocean.peace.mobile",
  appName: "ocean-peace-mobile",
  bundledWebRuntime: false,
  webDir: "../web/build",
  plugins: {
    SplashScreen: {
      launchAutoHide: false,
      backgroundColor: "#ffffffff",
      androidScaleType: "CENTER_CROP",
      showSpinner: false,
      splashFullScreen: true,
      splashImmersive: true,
    },
  },
};

export default config;
