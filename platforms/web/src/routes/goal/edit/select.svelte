<script lang="ts">
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
  });
</script>

<FullHeading>Select apps</FullHeading>

<H thin className="mt-8">Selected Apps (0)</H>
<SelectedApps />

<H thin>All Apps ({allApps.length})</H>
<SelectedApps apps={allApps} />

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 z-50 w-10/12"><Button isFullWidth>Save</Button></a>
