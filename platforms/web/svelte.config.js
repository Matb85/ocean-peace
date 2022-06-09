import sveltePreprocess from "svelte-preprocess";
import adapter from "@sveltejs/adapter-static";

export default {
  // Consult https://github.com/sveltejs/svelte-preprocess
  // for more information about preprocessors
  preprocess: sveltePreprocess({ postcss: true }),
  kit: {
    floc: true,
    adapter: adapter({ fallback: "index.html" }),
    prerender: { enabled: false },
  },
};
