<script lang="ts">
  import { Icon, Aquarium } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import { PieChart, ChartKey } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import H from "$lib/H.svelte";
  import { beforeNavigate } from "$app/navigation";

  beforeNavigate(({ to }) => {
    if (to.pathname == "/goal/edit/1") {
      sessionStorage.setItem("edit_goal_apps", "[]");
      sessionStorage.setItem("edit_goal_name", "");
      sessionStorage.setItem("edit_goal_time_minutes", "3");
      sessionStorage.setItem("edit_goal_time_hours", "1");
      sessionStorage.setItem("edit_goal_active_days", "[]");
      sessionStorage.setItem("edit_goal_limit_action_type", "Notification");
      sessionStorage.setItem("edit_goal_action_type", "Edit");
      sessionStorage.setItem("edit_goal_action_back", "/goal");
    }
  });
</script>

<FullHeading backHref="/" editHref="/goal/edit/1">Goal</FullHeading>

<section class="w-full h-80 absolute -z-50 top-0">
  <div class="wh-full block absolute z-10 bg-gradient-to-t from-white" />
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
</section>

<H tag={5} thin className="w-9/12">Limit Social Media to 90min a day</H>
<section class="w-11/12 grid grid-cols-6 gap-2">
  <PieChart
    className="w-full col-span-4"
    maxValue={200}
    data={[
      { color: "#3772FF", value: 110 },
      { color: "#FCBA04", value: 40 },
    ]}
  >
    <div class="wh-full flex flex-col items-center justify-center gap-2">
      <H tag={2}>24 min</H>
      <H tag={2} thin>left</H>
    </div>
  </PieChart>
  <ChartKey
    isVertical
    className="col-span-2 self-center flex-col flex-none"
    data={[
      { color: "#3772FF", text: "Samsung", bold: "1h 27min" },
      { color: "#FCBA04", text: "Macbook", bold: "0h 16min" },
      { color: "#F8F5FA", text: "Time left", bold: "3h 37min" },
    ]}
  />
</section>

<H thin>Last 7 days</H>
<section class="flex flex-wrap items-center justify-center max-w-xs">
  {#each ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"] as day}
    <div class="text-center">
      <PieChart
        className="w-20 h-20"
        maxValue={200}
        data={[
          { color: "#3772FF", value: 100 },
          { color: "#FCBA04", value: 30 },
        ]}
      >
        <div class="wh-full bg-green-light text-green-dark flex items-center justify-center">
          <Icon d={mdiCheck} className="fill-current w-32" />
        </div>
      </PieChart>
      <p>{day}</p>
    </div>
  {/each}
</section>

<H thin>Selected apps</H>

<SelectedApps />
