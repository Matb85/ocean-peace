<!-- allows to select apps -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { AppStatus } from "@redinnlabs/system/Units";
  import H from "$lib/H.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";

  const goBackUrl = SM.selectors.backUrl;

  import type { AppIconI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";

  let allApps: AppIconI[] = [];
  let selectedApps: string[] = JSON.parse(SM.selectors.apps);

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
    SM.selectors.apps = JSON.stringify(selectedApps);
  }
</script>

<FullHeading>Select apps</FullHeading>

<H thin className="mt-8">Selected Apps ({selectedApps.length})</H>
{goBackUrl}
<section class="app-group-con">
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

<section class="app-group-con">
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

<a href={goBackUrl} sveltekit:prefetch class="fixed bottom-10 z-50 w-10/12">
  <Button isFullWidth>Save</Button>
</a>
