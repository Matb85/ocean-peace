<script lang="ts">
  import { SearchBar } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import { onMount } from "svelte";

  import type { AppIconI } from "$lib/../../api/index";
  import Api from "$api";

  let allApps: AppIconI[] = [];
  onMount(async () => {
    allApps = await Api.getAllAppIcons();
    console.log("logging the result");
    console.log("result" + Object.keys(allApps));
  });
</script>

<FullHeading>Select apps</FullHeading>

<div class="w-11/12">
  <SearchBar placeholder="Search for an app or website..." />
</div>

<H thin>Selected Apps</H>
<SelectedApps />

<H thin>All Apps</H>
<SelectedApps apps={allApps} />

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 z-50 w-10/12"><Button isFullWidth>Save</Button></a>
