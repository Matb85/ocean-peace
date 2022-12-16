/// <reference types="@capacitor/splash-screen" />
import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "com.redinn.oceanpeace",
  appName: "Ocean Peace",
  bundledWebRuntime: false,
  webDir: "../web/build",
  plugins: {
    SplashScreen: {
      launchAutoHide: false,
      backgroundColor: "ffffffff",
      androidScaleType: "CENTER_CROP",
    },
  },
  // comment out this line for production
  // adjust for development whenever necessary
  // server: { url: "http://10.0.2.2:3000", cleartext: true }, // for the emulator
  //server: { url: "http://192.168.1.6:3000", cleartext: true }, // for real devices
};

export default config;
