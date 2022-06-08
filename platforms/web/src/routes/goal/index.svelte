<script lang="ts">
  import { BackButton, Icon, Heading, Aquarium, EditButton } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import { PieChart } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
</script>

<!-- C O N T E N T -->
<div class="w-full h-80 absolute -z-50 top-0">
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
  <svg width="full" height="full">
    <rect
      width="full"
      height="full"
      style="fill: linear-gradient(180deg, rgba(255, 255, 255, 0) 22.4%, #FFFFFF 100%)"
    />
  </svg>
</div>

<Heading tag={3} className="mt-7">Goal</Heading>

<div class="grid grid-cols-1 items-center place-items-center select-none w-11/12">
  <Heading tag={5} className="text-center !font-normal w-9/12">Limit Social Media to 90min a day</Heading>
  <div class="graph">
    <PieChart
      className="w-64 h-64"
      maxValue="200"
      data={[
        { color: "#3772FF", value: 110 },
        { color: "#FCBA04", value: 40 },
      ]}
    >
      <div class="w-full h-full flex flex-col items-center justify-center gap-2">
        <Heading tag={3}>24 min</Heading>
        <Heading tag={3} className="!font-normal">left</Heading>
      </div>
    </PieChart>
  </div>
</div>

<Heading tag={5} className="text-center !font-normal">Last 7 days</Heading>
<div class="flex flex-wrap items-center justify-center">
  {#each Array(7) as _, i}
    <PieChart
      className="w-20 h-20"
      maxValue="200"
      data={[
        { color: "#3772FF", value: 100 },
        { color: "#FCBA04", value: 30 },
      ]}
    >
      <div class="w-full h-full bg-[#CFEED1] text-[#03A108] flex items-center justify-center">
        <Icon d={mdiCheck} className="fill-current w-32" />
      </div>
    </PieChart>
  {/each}
</div>

<Heading tag={5} className="text-center !font-normal">Selected apps</Heading>

<a href="/" class="absolute top-5 left-3"><BackButton /></a>
<a href="/goal/edit/1" class="absolute top-5 right-3"><EditButton /> </a>
