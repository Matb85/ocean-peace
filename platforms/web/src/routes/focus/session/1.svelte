<!-- displays the ongoing session -->
<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button } from "@redinnlabs/system/Elements";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import { CircleChart } from "@redinnlabs/system/Charts";
  import H from "$lib/H.svelte";
  import { onMount } from "svelte";
  import type { AppIconI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";
  import SM from "$lib/sessionManager";

  export let appsCount: number = 16;

  let allowedApps: AppIconI[] = [];

  onMount(async () => {
    Api.getAppIcons(JSON.parse(SM.dialogs.apps)).then(data => (allowedApps = data));
    const t: boolean = (await Api.startPomodoro(1000 * 10, 1000 * 1, false, true)).started;
    console.log(t);
  });
</script>

<H tag={3} className="mt-7">{SM.preset.name} Session</H>

<div class="w-3/4 max-w-md">
  <CircleChart className="wh-full" />
  <H tag={6}>
    {appsCount}
    {appsCount > 1 ? "apps" : "app"} available
    <br />
    You'll get 25 points for this session
  </H>
</div>

<a sveltekit:prefetch href="./2" class="mt-4">
  <Button secondary size="small" isWarning>Cancel Session</Button>
</a>

<H thin>Allowed apps</H>
<SelectedApps apps={allowedApps} />

<H thin>Allowed websites</H>
