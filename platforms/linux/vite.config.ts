import { defineConfig } from "vite";
import { svelte } from "@sveltejs/vite-plugin-svelte";
import routify from "@roxi/routify/vite-plugin";

const production = process.env.NODE_ENV === "production";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    routify(),
    svelte({
      emitCss: false,
      compilerOptions: { dev: !production },
    }),
  ],
  build: {
    outDir: "build/",
  },
});
