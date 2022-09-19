<script lang="ts">
  let currentLanguage = "en";
  export let className = "";
</script>

<select class="lang-selector text-white {className}" bind:value={currentLanguage}>
  <option value="en"> 🇺🇸 English</option>
  <option value="pl"> 🇵🇱 Polski</option>
</select>

<style>
  .lang-selector {
    appearance: none;
    @apply bg-transparent px-4 py-1 border-none outline-primary1 rounded-xl;
  }
</style>
