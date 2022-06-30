<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";

  import { onMount } from "svelte";
  import Api from "$api";
  import type { AppIconI } from "$schema";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(sessionStorage.getItem("edit_goal_apps")));
  });

  let name = sessionStorage.getItem("edit_goal_name");
  $: name, sessionStorage.setItem("edit_goal_name", name);
</script>

<FullHeading backHref="./0">
  {sessionStorage.getItem("edit_goal_action_type")} goal
</FullHeading>

<H thin>Name</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" bind:value={name} />
</div>

<H thin>Selected apps</H>

<SelectedApps apps={selectedApps} />

<a sveltekit:prefetch href="./select">
  <Button secondary size="small">edit</Button>
</a>

<H thin>Selected Websites</H>

<SelectedApps />

<a sveltekit:prefetch href="./select">
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
