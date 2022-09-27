<script lang="ts">
  export let className = "";
  import { locale } from "./i18n";

  /** changes the locale
   * @param e the input event
   * @returns void
   */
  function onChange(e: Event) {
    $locale = (e.target as HTMLInputElement).value;
    localStorage.setItem("oceanpeace_lang", $locale);
  }
</script>

<select
  class="lang-selector text-white {className}"
  on:input={onChange}
  value={locale.get() || localStorage.getItem("oceanpeace_lang") || "en"}
>
  <option value="en"> 🇺🇸 English</option>
  <option value="pl"> 🇵🇱 Polski</option>
</select>

<style lang="postcss">
  .lang-selector {
    appearance: none;
    @apply bg-transparent px-4 py-1 border-none outline-primary1 rounded-xl;
  }
</style>
