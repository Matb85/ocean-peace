<!-- allows to select active days & set time limit -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import type { columnI } from "@redinnlabs/system/Form/TimeInput/TimeInput.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  const hours: columnI = {
    units: "h",
    data: [...Array(6).keys()],
    current: parseInt(sessionStorage.getItem("edit_goal_time_hours")) || 1,
    multiplier: 1,
  };
  const minutes = timeInputConfig.minutesConfig(parseInt(sessionStorage.getItem("edit_goal_time_minutes")) / 5);
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", {
    set: val => {
      h = val;
      sessionStorage.setItem("edit_goal_time_hours", h.toString());
    },
    get: () => h,
  });
  Object.defineProperty(minutes, "current", {
    set: val => {
      m = val;
      sessionStorage.setItem("edit_goal_time_minutes", (m * minutes.multiplier).toString());
    },
    get: () => m,
  });

  const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  let activeDays = JSON.parse(sessionStorage.getItem("edit_goal_active_days"));
  $: activeDays /** filter days when saving to maintain order*/,
    sessionStorage.setItem("edit_goal_active_days", JSON.stringify(days.filter(x => activeDays.includes(x))));

  const limitTypes = ["Notification", "Close app"];
  let type = sessionStorage.getItem("edit_goal_limit_action_type");
  $: type, sessionStorage.setItem("edit_goal_limit_action_type", type);
</script>

<FullHeading backHref="./1">{sessionStorage.getItem("edit_goal_action_type")} goal</FullHeading>

<H thin>Active days</H>

<CheckMultiple className="flex-wrap justify-center max-w-sm" bind:chosen={activeDays} options={days} />

<H thin>Time Limit</H>
<!--
<RadioInput bind:chosen={limit} options={["Time period", "Times opened"]} />
-->
<TimeInput columns={[hours, minutes]} />

<H thin>Limit Type</H>
<RadioInput bind:chosen={type} options={limitTypes} />

<div class="fixed-bottom-button bg-white">
  <a sveltekit:prefetch href={activeDays.length > 0 ? "./3" : ""} style:opacity={activeDays.length > 0 ? "1" : "0.5"}>
    <Button isFullWidth>next</Button>
  </a>
</div>
