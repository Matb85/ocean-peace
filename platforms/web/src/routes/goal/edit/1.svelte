<!-- allows to set the name and select apps & websites -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";

  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import SM from "$lib/sessionManager";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    SM.selectors.backUrl = "/goal/edit/1";
    selectedApps = await Api.getAppIcons(JSON.parse(SM.selectors.apps));
  });

  let name = SM.goal.name;
  $: SM.goal.name = name;
</script>

<FullHeading backHref="./0">
  {SM.action.type} goal
</FullHeading>

<H thin>Name</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" bind:value={name} />
</div>

<H thin>Selected apps</H>

<SelectedApps apps={selectedApps} />

<a sveltekit:prefetch href="/selectors/apps">
  <Button secondary size="small">edit</Button>
</a>

<H thin>Selected Websites</H>

<SelectedApps />

<a sveltekit:prefetch href="/selectors/apps">
  <Button secondary size="small">edit</Button>
</a>

<div class="fixed-bottom-button bg-white">
  <a
    sveltekit:prefetch
    href={selectedApps.length > 0 && name.length > 0 ? "./2" : ""}
    style:opacity={selectedApps.length > 0 && name.length > 0 ? "1" : "0.5"}
  >
    <Button isFullWidth>next</Button>
  </a>
</div>
