const config = require("@redinnlabs/system/utils/tailwind.config.cjs");
config.content = ["./src/**/*", "./node_modules/@redinnlabs/system/**/*"];
config.theme.colors.transparent = "transparent";
config.theme.colors.current = "currentColor";
module.exports = config;
