<script lang="ts">
  import { Aquarium } from "@redinnlabs/system/Elements";
  import { PieChart, LineChart, ChartKey } from "@redinnlabs/system/Charts";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { onMount } from "svelte";
  import type { ChartColumnI } from "@redinnlabs/system/utils";

  import Api from "$api";

  let usageStats: Array<ChartColumnI> = [];

  onMount(async () => {
    const t = (await Api.getAppsUsage()).stats;
    let counter: number = 0;
    for (const app in t) {
      if (Object.prototype.hasOwnProperty.call(t, app)) {
        const a: ChartColumnI = { value: 0, color: "#3772FF" };
        a.value = t[app].timeSpent as number;
        usageStats = [...usageStats, a];
        counter = counter + 1;
      }
    }
  });
</script>

<FullHeading backHref="/">Screen Time</FullHeading>

<div class="w-full h-80 absolute -z-50 top-0">
  <div class="wh-full block absolute z-10 bg-gradient-to-t from-white" />
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
</div>

<section class="w-11/12 grid grid-cols-6 gap-2 mt-12">
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
      <H tag={3} className="!font-normal">left</H>
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

<H thin>Usage Intensity</H>

<LineChart
  axisX={["8am", "10am", "12am", "2pm", "4pm", "6pm"]}
  data={[
    { key: 0, value: 0 },
    { key: 10, value: 30 },
    { key: 20, value: 45 },
    { key: 30, value: 40 },
    { key: 40, value: 80 },
    { key: 50, value: 50 },
    { key: 60, value: 40 },
    { key: 70, value: 90 },
    { key: 80, value: 60 },
    { key: 90, value: 80 },
    { key: 100, value: 100 },
  ]}
/>

<H thin>Apps used today</H>

<PieChart
  className="w-64 h-64"
  maxValue={600}
  data={usageStats}
>
  <div class="wh-full flex flex-col items-center justify-center gap-2">
    <H tag={2}>17 apps</H>
    <H tag={3} thin>opened</H>
  </div>
</PieChart>

<section class="w-11/12 flex flex-col gap-4">
  {#each Array(4) as _}
    <SoundTrack src="/instagram.svg" alt="app icon" title="Instagram" info="38min today" />
  {/each}
</section>
