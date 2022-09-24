<script lang="ts">
  import { stringTimeFromNumber } from "$lib/utils";
  import FullHeading from "$lib/FullHeading.svelte";
  import DangerZone from "$lib/DangerZone.svelte";

  import { Preset } from "@redinnlabs/system/Units";
  import { Button, H } from "@redinnlabs/system/Elements";
  import type { PresetI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import { querystring } from "svelte-spa-router";
  import { t } from "$lib/i18n";

  const scheduleData = SM.schedule.getProps("id", "name", "preset", "activeDays", "startTime", "stopTime");
  let presetData: Partial<PresetI> = {};
  onMount(async () => {
    presetData = await Api.getPreset(scheduleData.preset);

    SM.action.setProps({
      type: "edit",
      backUrl: "/focus/schedule?" + $querystring,
      continueUrl: "/focus/editschedule/1",
    });
  });
</script>

<FullHeading tag={3} backHref="/focus" editHref="/focus/editschedule/1">{$t("d.schedule.schedule")}</FullHeading>

<H tag={6} thin>{$t("d.schedule.name")}</H>
<H tag={4} className="-mt-2" thin>{scheduleData.name}</H>

<H tag={6} thin>{$t("d.schedule.chosen_preset")}</H>

<Preset src={presetData.icon} label={presetData.name} />

<H tag={6} thin>{$t("d.schedule.a_days")}</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(scheduleData.activeDays || "[]") as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>{$t("d.schedule.a_hours")}</H>
<H tag={4} className="-mt-2" thin
  >{stringTimeFromNumber(scheduleData.startTime)} - {stringTimeFromNumber(scheduleData.stopTime)}</H
>

<H tag={6} thin>{$t("d.schedule.status")}</H>
<div class="grayscale">
  <Button isWarning>{$t("d.schedule.not_running")}</Button>
</div>

<DangerZone
  deleteUrl="/focus/editschedule/delete"
  label={$t("d.cta.delete") + " " + $t("d.schedule.schedule").toLowerCase()}
/>
