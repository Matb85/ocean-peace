<!-- allows to select active days & set time limit -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import FullHeading from "$lib/FullHeading.svelte";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";

  const hours: columnI = {
    units: "h",
    data: [...Array(6).keys()],
    current: parseInt(SM.goal.getProp("timeHours")),
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig(parseInt(SM.goal.getProp("timeMinutes")) / 5);
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", {
    set: val => {
      h = val;
      SM.goal.setProp("timeHours", h);
    },
    get: () => h,
  });
  Object.defineProperty(minutes, "current", {
    set: val => {
      m = val;
      SM.goal.setProp("timeMinutes", m * minutes.multiplier);
    },
    get: () => m,
  });

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
<TimeInput columns={[hours, minutes]} />

<H thin>{$t("d.goal.limit_type")}</H>
<RadioInput bind:chosen={type} options={limitTypes} />

<div class="fixed-bottom-button bg-white" style:opacity={activeDays.length > 0 ? "1" : "0.5"}>
  <Link href={activeDays.length > 0 ? "/goal/edit/3" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
