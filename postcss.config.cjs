const tailwindcss = require("./platforms/web/node_modules/tailwindcss/lib/index.js");

module.exports = {
  plugins: [tailwindcss({ config: "./platforms/web/tailwind.config.cjs" })],
};
