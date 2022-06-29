<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { AppStatus } from "@redinnlabs/system/Units";
  import H from "$lib/H.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import { onMount } from "svelte";

  import type { AppIconI } from "$lib/../../api/index";
  import Api from "$api";

  let allApps: AppIconI[] = [];
  let selectedApps: string[] = JSON.parse(sessionStorage.getItem("edit_goal_apps") || "[]");

  onMount(async () => {
    allApps = await Api.getAllAppIcons();
  });

  /** removes or add an app to the selectedApps array
   * @param app the app to toggle
   * @returns {void}
   */
  function toggleApp(app: AppIconI) {
    if (!selectedApps.includes(app.packageName)) {
      selectedApps = [...selectedApps, app.packageName];
    } else {
      selectedApps = selectedApps.filter(x => x != app.packageName);
    }
    sessionStorage.setItem("edit_goal_apps", JSON.stringify(selectedApps));
  }
</script>

<FullHeading>Select apps</FullHeading>

<H thin className="mt-8">Selected Apps ({selectedApps.length})</H>

<section class="w-full flex flex-wrap justify-center gap-x-2 gap-y-4">
  {#each allApps.filter(x => selectedApps.includes(x.packageName)) as app}
    <AppStatus
      on:click={() => toggleApp(app)}
      src={app.iconPath}
      label={app.label}
      isSelected={selectedApps.includes(app.packageName)}
      alt="app icon"
    />
  {/each}
  {#if selectedApps.length <= 0}
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
      isSelected={selectedApps.includes(app.packageName)}
      alt="app icon"
    />
  {/each}
</section>

<a href="/goal/edit/1" sveltekit:prefetch class="fixed bottom-10 z-50 w-10/12">
  <Button isFullWidth>Save</Button>
</a>
