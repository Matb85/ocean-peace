<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import type { AppIconI } from "$lib/../../api/index";

  export let apps: AppIconI[] = [];
</script>

{apps.length}
<section class="w-full max-w-sm flex flex-wrap justify-center gap-x-2 gap-y-2 mt-4">
  {#each apps as app}
    <div class="mt-2 self-start">
      <AppStatus src={app.src} alt="app icon" />
      <p class="text-center w-20 text-xs text-wrap">{app.name}</p>
    </div>
  {/each}
  {#if apps.length <= 0}
    <p>No apps selected</p>
  {/if}
</section>
