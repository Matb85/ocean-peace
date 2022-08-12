<!-- allows to select active days & set time limit -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SM from "$lib/sessionManager";

  // start time
  const startHours: columnI = {
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.startTime) / 60),
    multiplier: 1,
  };
  const startMinutes = timeInputConfig.minutesConfig((parseInt(SM.schedule.startTime) % 60) / 5);
  startMinutes.units = "";
  let startH: number;
  let startM: number;
  startH = startHours.current;
  startM = startMinutes.current;
  Object.defineProperty(startHours, "current", {
    set: val => {
      startH = val;
      SM.schedule.startTime = (startH * 60 + startM * startMinutes.multiplier).toString();
    },
    get: () => startH,
  });
  Object.defineProperty(startMinutes, "current", {
    set: val => {
      startM = val;
      SM.schedule.startTime = (startH * 60 + startM * startMinutes.multiplier).toString();
    },
    get: () => startM,
  });
  // stop time
  const stopHours: columnI = {
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.stopTime) / 60),
    multiplier: 1,
  };
  const stopMinutes = timeInputConfig.minutesConfig((parseInt(SM.schedule.stopTime) % 60) / 5);
  stopMinutes.units = "";
  let stopH: number;
  let stopM: number;
  stopH = stopHours.current;
  stopM = stopMinutes.current;
  Object.defineProperty(stopHours, "current", {
    set: val => {
      stopH = val;
      SM.schedule.stopTime = (stopH * 60 + stopM * stopMinutes.multiplier).toString();
    },
    get: () => stopH,
  });
  Object.defineProperty(stopMinutes, "current", {
    set: val => {
      stopM = val;
      SM.schedule.stopTime = (stopH * 60 + stopM * stopMinutes.multiplier).toString();
    },
    get: () => stopM,
  });

  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  let activeDays = JSON.parse(SM.schedule.activeDays);
  /** filter days when saving to maintain order*/
  $: SM.schedule.activeDays = JSON.stringify(days.filter(x => activeDays.includes(x)));
</script>

<FullHeading backHref="./1">
  {SM.action.type} schedule
</FullHeading>

<H thin>Active days</H>

<CheckMultiple className="flex-wrap justify-center max-w-sm" bind:chosen={activeDays} options={days} />

<div class="flex justify-center gap-8">
  <div class="flex flex-col items-center gap-2 relative">
    <H thin>Start Time</H>

    <TimeInput columns={[startHours, startMinutes]} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>

  <div class="flex flex-col items-center gap-2 relative">
    <H thin>Stop Time</H>
    <TimeInput columns={[stopHours, stopMinutes]} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>
</div>

<div class="fixed-bottom-button bg-white">
  <a sveltekit:prefetch href={activeDays.length > 0 ? "./3" : ""} style:opacity={activeDays.length > 0 ? "1" : "0.5"}>
    <Button isFullWidth>next</Button>
  </a>
</div>
