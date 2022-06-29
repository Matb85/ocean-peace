<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { AppStatus } from "@redinnlabs/system/Units";
  import H from "$lib/H.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import { onMount } from "svelte";

  import type { AppIconI } from "$lib/../../api/index";
  import Api from "$api";

  let allApps: AppIconI[] = [];
  let selectedApps: string[] = [];
  onMount(async () => {
    allApps = await Api.getAllAppIcons();
  });

  /** removes or add an app to the selectedApps array
   * @param app the app to toggle
   * @returns {void}
   */
  function toggleApp(app: AppIconI) {
    if (!selectedApps.includes(app.label)) {
      selectedApps = [...selectedApps, app.label];
    } else {
      selectedApps = selectedApps.filter(x => x != app.label);
    }
  }
</script>

<FullHeading>Select apps</FullHeading>

<H thin className="mt-8">Selected Apps ({selectedApps.length})</H>

<section class="w-full flex flex-wrap justify-center gap-x-2 gap-y-4">
  {#each allApps.filter(x => selectedApps.includes(x.label)) as app}
    <AppStatus
      on:click={() => toggleApp(app)}
      src={app.iconPath}
      label={app.label}
      isSelected={selectedApps.includes(app.label)}
      alt="app icon"
    />
  {/each}
  {#if allApps.length <= 0}
    <p>0 selected</p>
  {/if}
</section>

<H thin>All Apps ({allApps.length})</H>

<section class="w-full flex flex-wrap justify-center gap-x-2 gap-y-4">
  {#each allApps as app}
    <AppStatus
      on:click={() => toggleApp(app)}
      src={app.iconPath}
      label={app.label}
      isSelected={selectedApps.includes(app.label)}
      alt="app icon"
    />
  {/each}
  {#if allApps.length <= 0}
    <p>0 selected</p>
  {/if}
</section>

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 z-50 w-10/12"><Button isFullWidth>Save</Button></a>
