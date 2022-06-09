const sveltePreprocess = require("./platforms/web/node_modules/svelte-preprocess/dist/index.js");

module.exports = {
  preprocess: sveltePreprocess({ postcss: true, tsconfigFile: "./platforms/web/tsconfig.json" }),
};
