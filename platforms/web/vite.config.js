import { sveltekit } from "@sveltejs/kit/vite";
import path from "path";

const alias = {
  $schema: path.resolve(process.cwd(), "./api"),
};

/** @type {import('vite').UserConfig} */
const config = {
  plugins: [sveltekit()],
  resolve: { alias },
};

export default config;
