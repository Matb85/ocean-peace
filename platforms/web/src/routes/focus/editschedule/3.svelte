<!-- displays the summary -->
<script lang="ts">
  import Confirmation from "$lib/Confirmation.svelte";
  import { Preset } from "@redinnlabs/system/Units";
  import { Button, H } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";

  import { t } from "$lib/i18n";
  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import type { PresetI, ScheduleI } from "$schema";
  import SM from "$lib/sessionManager";

  let preset: PresetI = {} as any;
  const scheduleSM = SM.schedule.getProps("id", "name", "preset", "activeDays", "startTime", "stopTime");
  onMount(async () => {
    preset = (await Api.getPreset(scheduleSM.preset)) as any as PresetI;
  });
  /** save schedule to a file
   * @returns void
   */
  function saveSchedule() {
    isComplete = true;
    setTimeout(async () => {
      const data: ScheduleI = {
        id: scheduleSM.id,
        name: scheduleSM.name,
        preset: scheduleSM.preset,
        activeDays: scheduleSM.activeDays,
        startTime: scheduleSM.startTime,
        stopTime: scheduleSM.stopTime,
      };
      await Api.saveSchedule(data);
      goTo("/focus");
    }, 1500);
  }
  let isComplete = false;
</script>

<FullHeading backHref="/focus/editschedule/2">{$t("d.summary")}</FullHeading>

<H tag={6} thin>{$t("d.schedule.name")}</H>
<H tag={4} className="-mt-2" thin>{scheduleSM.name}</H>

<H tag={6} thin>{$t("d.schedule.chosen_preset")}</H>
<Preset src={preset.icon} label={preset.name} />

<H tag={6} thin>{$t("d.schedule.a_days")}</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(scheduleSM.activeDays) as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>{$t("d.schedule.a_hours")}</H>
<H tag={4} className="-mt-2" thin>
  {Math.floor(parseInt(scheduleSM.startTime) / 60)}:{Math.floor(parseInt(scheduleSM.startTime) % 60)}
  -
  {Math.floor(parseInt(scheduleSM.stopTime) / 60)}:{Math.floor(parseInt(scheduleSM.stopTime) % 60)}
</H>

<div on:click={saveSchedule} class="fixed-bottom-button">
  <Button isFullWidth>{$t("d.cta.save")}</Button>
</div>

<Confirmation {isComplete} text={$t("d.schedule.saved")} />
