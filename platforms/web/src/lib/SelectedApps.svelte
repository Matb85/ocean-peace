<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import type { AppIconI } from "$lib/../../api/index";

  interface SelctableAppIconI extends AppIconI {
    isSelected?: boolean;
  }
  export let apps: SelctableAppIconI[] = [];
</script>

<section class="w-full max-w-sm flex flex-wrap justify-center gap-x-2 gap-y-4">
  {#each apps as app}
    <div
      on:click={() => {
        if (!app.isSelected) app.isSelected = true;
        else app.isSelected = false;
      }}
      class="app_status_con self-start"
    >
      <AppStatus src={app.src} label={app.name} isSelected={app.isSelected} alt="app icon" />
    </div>
  {/each}
  {#if apps.length <= 0}
    <p>0 selected</p>
  {/if}
</section>

<style>
  .app_status_con :global(*) {
    box-shadow: none !important;
  }
</style>
