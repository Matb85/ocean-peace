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
  const startH: columnI = {
    id: "startH",
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.getProp("startTime")) / 60),
    multiplier: 1,
  };
  const startM = timeInputConfig.minutesConfig("startM", (parseInt(SM.schedule.getProp("startTime")) % 60) / 5);
  startM.units = "";

  // stop time
  const stopH: columnI = {
    id: "stopH",
    units: "",
    data: [...Array(24).keys()],
    current: Math.floor(parseInt(SM.schedule.getProp("stopTime")) / 60),
    multiplier: 1,
  };
  const stopM = timeInputConfig.minutesConfig("stopM", (parseInt(SM.schedule.getProp("stopTime")) % 60) / 5);
  stopM.units = "";
  /** updates the time
   * @param e event
   * @returns {void}
   */
  function onUpdate(e: CustomEvent<{ id: string; current: number }>) {
    const { id, current } = e.detail;
    if (id == "startH") SM.schedule.setProp("startTime", current * 60 + startM.current * startM.multiplier);
    if (id == "startM") SM.schedule.setProp("startTime", startH.current * 60 + current * startM.multiplier);
    if (id == "stopH") SM.schedule.setProp("stopTime", current * 60 + stopM.current * startM.multiplier);
    if (id == "stopM") SM.schedule.setProp("stopTime", stopH.current * 60 + current * startM.multiplier);
    validateTime();
  }
  let isTimeCorrect = false;
  /** checks if the time is set correctly
   * @returns {void}
   */
  function validateTime() {
    if (
      startH.current * 60 + startM.current * startM.multiplier <
      stopH.current * 60 + stopM.current * stopM.multiplier
    )
      isTimeCorrect = true;
    else isTimeCorrect = false;
  }
  validateTime();
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

    <TimeInput columns={[startH, startM]} on:update={onUpdate} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>

  <div class="flex flex-col items-center gap-2 relative">
    <H thin>{$t("d.schedule.endt")}</H>
    <TimeInput columns={[stopH, stopM]} on:update={onUpdate} />
    <div class="w-full h-full absolute -z-10 flex items-center justify-center"><span class="mt-14">:</span></div>
  </div>
</div>

<div class="fixed-bottom-button bg-white" style:opacity={activeDays.length > 0 && isTimeCorrect ? "1" : "0.5"}>
  <Link href={activeDays.length > 0 && isTimeCorrect ? "/focus/editschedule/3" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
