<!-- allows to select active days & set time limit -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import FullHeading from "$lib/FullHeading.svelte";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";

  // start time
  const startHours: columnI = {
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.getProp("startTime")) / 60),
    multiplier: 1,
  };
  const startMinutes = timeInputConfig.minutesConfig((parseInt(SM.schedule.getProp("startTime")) % 60) / 5);
  startMinutes.units = "";
  let startH: number;
  let startM: number;
  startH = startHours.current;
  startM = startMinutes.current;
  Object.defineProperty(startHours, "current", {
    set: val => {
      startH = val;
      SM.schedule.setProp("startTime", startH * 60 + startM * startMinutes.multiplier);
    },
    get: () => startH,
  });
  Object.defineProperty(startMinutes, "current", {
    set: val => {
      startM = val;
      SM.schedule.setProp("startTime", startH * 60 + startM * startMinutes.multiplier);
    },
    get: () => startM,
  });
  // stop time
  const stopHours: columnI = {
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.getProp("stopTime")) / 60),
    multiplier: 1,
  };
  const stopMinutes = timeInputConfig.minutesConfig((parseInt(SM.schedule.getProp("stopTime")) % 60) / 5);
  stopMinutes.units = "";
  let stopH: number;
  let stopM: number;
  stopH = stopHours.current;
  stopM = stopMinutes.current;
  Object.defineProperty(stopHours, "current", {
    set: val => {
      stopH = val;
      SM.schedule.setProp("stopTime", stopH * 60 + stopM * stopMinutes.multiplier);
    },
    get: () => stopH,
  });
  Object.defineProperty(stopMinutes, "current", {
    set: val => {
      stopM = val;
      SM.schedule.setProp("stopTime", stopH * 60 + stopM * stopMinutes.multiplier);
    },
    get: () => stopM,
  });

  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  let activeDays = JSON.parse(SM.schedule.getProp("activeDays"));
  /** filter days when saving to maintain order*/
  $: SM.schedule.setProp("activeDays", JSON.stringify(days.filter(x => activeDays.includes(x))));
</script>

<FullHeading backHref="/focus/editschedule/1">
  {$t("d.schedule." + SM.action.getProp("type"))}
</FullHeading>

<H thin>{$t("d.schedule.a_days")}</H>

<CheckMultiple className="flex-wrap justify-center max-w-sm" bind:chosen={activeDays} options={days} />

<div class="flex justify-center gap-8">
  <div class="flex flex-col items-center gap-2 relative">
    <H thin>{$t("d.schedule.startt")}</H>

    <TimeInput columns={[startHours, startMinutes]} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>

  <div class="flex flex-col items-center gap-2 relative">
    <H thin>{$t("d.schedule.endt")}</H>
    <TimeInput columns={[stopHours, stopMinutes]} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>
</div>

<div class="fixed-bottom-button bg-white" style:opacity={activeDays.length > 0 ? "1" : "0.5"}>
  <Link href={activeDays.length > 0 ? "/focus/editschedule/3" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
