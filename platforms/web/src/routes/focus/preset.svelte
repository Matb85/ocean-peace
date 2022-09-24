<!-- set the timer before starting -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import DangerZone from "$lib/DangerZone.svelte";
  import { querystring } from "svelte-spa-router";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";
  const presetData = SM.preset.getProps("id", "name", "icon");
  onMount(async () => {
    SM.action.setProps({
      type: "edit",
      backUrl: "/focus/preset?" + $querystring,
      continueUrl: "/focus/editpreset/1",
    });
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

<H thin>{$t("d.focus.duration")}</H>
<RadioInput className="flex-wrap justify-center" bind:chosen={limit} options={["Pomodoro", "Continued", "Stopwatch"]} />
{#if limit == "Pomodoro"}
  <div class="flex justify-center gap-10 text-center">
    <span>{$t("d.focus.focust")}</span>
    <span>{$t("d.focus.breakt")}</span>
  </div>
  <TimeInput columns={[focusTime, breakTime]} />
{:else if limit == "Continued"}
  <div class="flex justify-center gap-10">
    <span>{$t("d.focus.hours")}</span>
    <span>{$t("d.focus.minutes")}</span>
  </div>
  <TimeInput columns={[hoursTime, minutesTime]} />
{/if}

<H thin>{$t("d.focus.options")}</H>
<CheckMultiple className="flex-wrap justify-center" options={["Hard Limit", "20:20:20 Rule"]} />

<H thin>Soundtrack</H>
<div class="card-flex-col">
  {#each Array(3) as _}
    <SoundTrack title="Example Soundtrack" info="Chose one" />
  {/each}
</div>

<Link href="/focus/session/1" className="fixed bottom-10">
  <Button>{$t("d.cta.start")} {presetData?.name || ""}</Button>
</Link>

<DangerZone
  deleteUrl="/focus/editpreset/delete"
  label={$t("d.cta.delete") + " " + $t("d.preset.preset").toLowerCase()}
/>
