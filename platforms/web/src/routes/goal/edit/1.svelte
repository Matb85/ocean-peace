<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";

  import { onMount } from "svelte";
  import Api from "$api";
  import type { AppIconI } from "$lib/../../api/index";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(sessionStorage.getItem("edit_goal_apps") || "[]"));
  });

  let name = sessionStorage.getItem("edit_goal_name") || "";
  $: name, sessionStorage.setItem("edit_goal_name", name);
</script>

<FullHeading backHref="/goal">Goal - Edit</FullHeading>

<H thin>Name</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" bind:value={name} />
</div>

<H thin>Blocked apps</H>

<SelectedApps apps={selectedApps} />

<a sveltekit:prefetch href="/goal/edit/select">
  <Button secondary size="small">edit</Button>
</a>

<H thin>Blocked Websites</H>

<SelectedApps />

<a sveltekit:prefetch href="/goal/edit/select">
  <Button secondary size="small">edit</Button>
</a>

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 z-50 w-11/12"><Button isFullWidth>next</Button> </a>
