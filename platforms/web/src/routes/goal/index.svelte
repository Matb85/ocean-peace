<script lang="ts">
  import { Icon, Aquarium } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import { PieChart } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import H from "$lib/H.svelte";
</script>

<FullHeading backHref="/" editHref="/goal/edit/1">Goal</FullHeading>

<section class="w-full h-80 absolute -z-50 top-0">
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
</section>

<section class="grid grid-cols-1 items-center place-items-center select-none w-11/12">
  <H tag={5} thin className="w-9/12">Limit Social Media to 90min a day</H>
  <div class="graph">
    <PieChart
      className="w-64 h-64"
      maxValue={200}
      data={[
        { color: "#3772FF", value: 110 },
        { color: "#FCBA04", value: 40 },
      ]}
    >
      <div class="w-full h-full flex flex-col items-center justify-center gap-2">
        <H tag={2}>24 min</H>
        <H tag={2} thin>left</H>
      </div>
    </PieChart>
  </div>
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
        <div class="w-full h-full bg-[#CFEED1] text-[#03A108] flex items-center justify-center">
          <Icon d={mdiCheck} className="fill-current w-32" />
        </div>
      </PieChart>
      <p>{day}</p>
    </div>
  {/each}
</section>

<H thin>Selected apps</H>

<SelectedApps />
