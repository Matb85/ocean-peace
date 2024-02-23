<!-- allows to select active days & set time limit -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import FullHeading from "$lib/FullHeading.svelte";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";
  import { timeFromNumber } from "$lib/utils";

  const time = timeFromNumber(SM.goal.getProp("limit"));

  const hours: columnI = {
    id: "hours",
    units: "h",
    data: [...Array(6).keys()],
    current: time[0],
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig("minutes", time[1] / 5);
  /** updates the time
   * @param e event
   * @returns void
   */
  function onUpdate(e: CustomEvent<{ id: string; current: number }>) {
    const { id, current } = e.detail;
    if (id == "hours") SM.goal.setProp("limit", current * 60 + minutes.current * minutes.multiplier);
    if (id == "minutes") SM.goal.setProp("limit", hours.current * 60 + current * minutes.multiplier);
  }

  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  let activeDays = JSON.parse(SM.goal.getProp("activeDays"));
  /** filter days when saving to maintain order*/
  $: SM.goal.setProp("activeDays", JSON.stringify(days.filter(x => activeDays.includes(x))));

  const limitTypes = ["Notification", "Close app"];
  let type = SM.goal.getProp("limitActionType");
  $: SM.goal.setProp("limitActionType", type);
</script>

<FullHeading backHref="/goal/edit/1">{$t("d.goal." + SM.action.getProp("type"))}</FullHeading>

<H thin>{$t("d.goal.a_days")}</H>

<CheckMultiple className="flex-wrap justify-center max-w-sm" bind:chosen={activeDays} options={days} />

<H thin>{$t("d.goal.time_limit")}</H>
<!--
<RadioInput bind:chosen={limit} options={["Time period", "Times opened"]} />
-->
<TimeInput columns={[hours, minutes]} on:update={onUpdate} />

<H thin>{$t("d.goal.limit_type")}</H>
<RadioInput bind:chosen={type} options={limitTypes} />

<div class="fixed-bottom-button" class:enabled={activeDays.length > 0}>
  <Link href={activeDays.length > 0 ? "/goal/edit/3" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
