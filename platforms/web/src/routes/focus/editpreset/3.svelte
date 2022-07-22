<!-- displays the summary -->
<script lang="ts">
  import Confirmation from "$lib/Confirmation.svelte";
  import { AppStatus } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";

  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goto } from "$app/navigation";
  import type { AppIconI, PresetI } from "$schema";
  import SM from "$lib/sessionManager";

  let selectedApps: AppIconI[] = [];

  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(SM.selectors.apps));
  });
  /** save goal to a file
   * @returns {void}
   */
  function saveGoal() {
    isComplete = true;
    setTimeout(() => {
      const data: PresetI = {
        id: SM.preset.id,
        name: SM.preset.name,
        icon: SM.preset.icon,
        apps: SM.selectors.apps,
      };
      Api.savePreset(data);
      goto("/focus");
    }, 1500);
  }
  let isComplete = false;
</script>

<FullHeading backHref="./2">Summary</FullHeading>

<H tag={6} thin>Goal name</H>
<H tag={4} className="-mt-2" thin>{SM.preset.name}</H>

<H tag={6} thin>Icon</H>

<div class="shadow-wrapper-sm w-20 rounded-2xl bg-white">
  <AppStatus className="scale-[0.75]" src={SM.preset.icon} />
</div>
<H tag={6} thin>Allowed apps</H>
<SelectedApps apps={selectedApps} />

<H tag={6} thin>Allowed Websites</H>

<div on:click={saveGoal} class="fixed-bottom-button">
  <Button isFullWidth>save</Button>
</div>

<Confirmation {isComplete} />
