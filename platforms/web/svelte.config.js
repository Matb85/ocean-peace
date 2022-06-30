import sveltePreprocess from "svelte-preprocess";
import adapter from "@sveltejs/adapter-static";
import path from "path";

const alias = {
  $schema: path.resolve(process.cwd(), "./api"),
};

export default {
  // Consult https://github.com/sveltejs/svelte-preprocess
  // for more information about preprocessors
  preprocess: sveltePreprocess({ postcss: true }),
  kit: {
    floc: true,
    adapter: adapter({ fallback: "index.html" }),
    vite: {
      resolve: { alias },
    },
  },
};
