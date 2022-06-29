<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import type { AppIconI } from "$lib/../../api/index";

  interface SelctableAppIconI extends AppIconI {
    isSelected?: boolean;
  }
  export let apps: SelctableAppIconI[] = [];
</script>

<section class="w-full flex flex-wrap justify-center gap-x-2 gap-y-4">
  {#each apps as app}
    <AppStatus
      on:click={() => {
        if (!app.isSelected) app.isSelected = true;
        else app.isSelected = false;
      }}
      src={app.iconPath}
      label={app.label}
      isSelected={app.isSelected}
      alt="app icon"
    />
  {/each}
  {#if apps.length <= 0}
    <p>0 selected</p>
  {/if}
</section>
