<!-- allows to set the name and select apps & websites -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import Link from "$lib/Link.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";

  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import SM from "$lib/sessionManager";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    SM.dialogs.backUrl = "/focus/editpreset/1";
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.apps));
  });

  let name = SM.preset.name;
  $: SM.preset.name = name;
</script>

<FullHeading backHref="/dialogs/cancel">
  {SM.action.type} preset
</FullHeading>

<H thin>Name</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" bind:value={name} />
</div>

<H thin>Allowed apps</H>

<SelectedApps apps={selectedApps} />

<Link href="/dialogs/apps">
  <Button secondary size="small">edit</Button>
</Link>

<H thin>Allowed websites</H>

<SelectedWebsites websites={JSON.parse(SM.dialogs.websites)} />

<Link href="/dialogs/websites">
  <Button secondary size="small">edit</Button>
</Link>

<div class="fixed-bottom-button bg-white" style:opacity={selectedApps.length > 0 && name.length > 0 ? "1" : "0.5"}>
  <Link href={selectedApps.length > 0 && name.length > 0 ? "/focus/editpreset/2" : ""}>
    <Button isFullWidth>next</Button>
  </Link>
</div>
