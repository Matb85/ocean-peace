<!-- set the timer before starting -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import DangerZone from "$lib/DangerZone.svelte";
  import { querystring } from "svelte-spa-router";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { PresetI } from "$schema";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";

  const presetId = new URLSearchParams($querystring).get("id");

  let presetData: PresetI;
  onMount(async () => {
    presetData = await Api.getPreset(presetId);

    SM.preset.id = presetData.id;
    SM.preset.name = presetData.name;
    SM.preset.icon = presetData.icon;

    SM.dialogs.apps = presetData.apps;
    SM.dialogs.websites = presetData.websites;
    SM.action.type = "Edit";
    SM.action.backUrl = "/goal/preset?" + $querystring;
    SM.action.continueUrl = "/focus/editpreset/1";
  });

  const hoursTime = timeInputConfig.hoursConfig(1);
  const minutesTime = timeInputConfig.minutesConfig(3);
  const focusTime = timeInputConfig.minutesConfig(5);
  const breakTime = timeInputConfig.minutesConfig(1);
  let h: number;
  let m: number;
  h = hoursTime.current;
  m = minutesTime.current;
  Object.defineProperty(hoursTime, "current", { set: val => (h = val), get: () => h });
  Object.defineProperty(minutesTime, "current", { set: val => (m = val), get: () => m });

  let limit = "Pomodoro";
</script>

<FullHeading backHref="/focus" editHref="/focus/editpreset/1">{presetData?.name || ""}</FullHeading>

<H thin>Duration</H>
<RadioInput className="flex-wrap justify-center" bind:chosen={limit} options={["Pomodoro", "Continued", "Stopwatch"]} />
{#if limit == "Pomodoro"}
  <div class="flex justify-center gap-16">
    <span>Focus</span>
    <span>Break</span>
  </div>
  <TimeInput columns={[focusTime, breakTime]} />
{:else if limit == "Continued"}
  <div class="flex justify-center gap-16">
    <span>Hours</span>
    <span>Minutes</span>
  </div>
  <TimeInput columns={[hoursTime, minutesTime]} />
{/if}

<H thin>Options</H>
<CheckMultiple className="flex-wrap justify-center" options={["Hard Limit", "20:20:20 Rule"]} />

<H thin>Soundtrack</H>
<div class="card-flex-col">
  {#each Array(3) as _}
    <SoundTrack title="Example Soundtrack" info="Chose one" />
  {/each}
</div>

<Link href="/focus/session/1" className="fixed bottom-10">
  <Button>Start {presetData?.name || ""}</Button>
</Link>

<DangerZone deleteUrl="/focus/editpreset/delete" label="Delete Preset" />
