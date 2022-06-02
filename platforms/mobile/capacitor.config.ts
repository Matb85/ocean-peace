import { CapacitorConfig } from "@capacitor/cli";

const config: CapacitorConfig = {
  appId: "ocean.peace.mobile",
  appName: "ocean-peace-mobile",
  bundledWebRuntime: false,
  webDir: "../web/build",
  plugins: {
    SplashScreen: {
      launchShowDuration: 0,
    },
  },
};

export default config;
