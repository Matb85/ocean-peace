import { svelte } from "@sveltejs/vite-plugin-svelte";

import path from "path";

const alias = {
  $schema: path.resolve(process.cwd(), "./api"),
  $lib: path.resolve(process.cwd(), "./src/lib"),
};

/** @type {import('vite').UserConfig} */
const config = {
  plugins: [svelte()],
  resolve: { alias },
  build: {
    outDir: "./build",
  },
};

export default config;
