import i18n from "sveltekit-i18n";
import type { Config } from "sveltekit-i18n";

const config: Config = {
  loaders: [
    {
      locale: "en",
      key: "d",
      loader: async () => (await import("./en.json")).default,
    },
    {
      locale: "pl",
      key: "d",
      loader: async () => (await import("./pl.json")).default,
    },
  ],
};

export const { t, locale, locales, loading, loadTranslations } = new i18n(config); // eslint-disable-line
