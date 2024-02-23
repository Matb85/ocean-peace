<script lang="ts">
  import { Icon, Aquarium, H } from "@redinnlabs/system/Elements";
  import { mdiCheck, mdiClose } from "@mdi/js";
  import { PieChart, LineChart, ChartKey } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import { t } from "$lib/i18n";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { formatMinutes } from "$lib/utils";
  import type { GoalHistoryI, HourlyUsageI, SingleAppUsageI } from "$schema/usage";
  import { onMount } from "svelte";

  let appsUsed: SingleAppUsageI[] = [];
  let screenTimeHistory: GoalHistoryI[] = [];
  let hourlyUsageToday: HourlyUsageI[] = [];
  let totalTime = 0;
  let maxScreenTime: number = 0;
  let appsUsedTimeNormaliser = 0;

  onMount(() => {
    Api.getPreferences().then(d => (maxScreenTime = parseInt(d.screentime)));
    Api.getTotalTime().then(d => (totalTime = d));
    Api.getAppsUsedToday().then(d => {
      let timeSpent = 0;
      for (const app of d) timeSpent += app.minutes;
      appsUsed = d;
      appsUsedTimeNormaliser = maxScreenTime / timeSpent;
    });
    Api.getScreenTimeHistory().then(d => (screenTimeHistory = d));
    Api.getUsageIntensityToday().then(d => (hourlyUsageToday = d));
  });
</script>

<FullHeading tag={3} backHref="/">{$t("d.screentime.screentime")}</FullHeading>

<div class="w-full h-80 absolute -z-50 top-0">
  <div class="wh-full block absolute z-10 bg-gradient-to-t from-white" />
  <Aquarium percent={maxScreenTime - totalTime < 0 ? 0 : ((maxScreenTime - totalTime) / maxScreenTime) * 100} />
  <Cutout className="w-full bottom-0 absolute" />
</div>

<H tag={5} thin className="w-9/12 text-center"
  >{$t("d.settings.screentime") + ": "}<br />
  {formatMinutes(maxScreenTime)}
</H>

<section class="w-11/12">
  <PieChart
    className="w-3/4 mx-auto"
    maxValue={maxScreenTime}
    data={appsUsed.map(a => ({ color: a.color, value: a.minutes * appsUsedTimeNormaliser }))}
  >
    <div class="wh-full flex flex-col items-center justify-center gap-2">
      <H tag={2}>
        {formatMinutes(totalTime)}
      </H>
    </div>
  </PieChart>
  <ChartKey
    className="w-full mt-2 items-center flex-col flex-wrap flex-none"
    data={appsUsed.map(a => ({ color: a.color, text: a.icon.label, bold: formatMinutes(a.minutes) }))}
  />
</section>

<H thin>Hourly Usage Today</H>
<LineChart
  axisX={hourlyUsageToday.map(h => h.hour)}
  data={hourlyUsageToday.map(h => ({ key: h.key, value: h.value }))}
/>

<H thin>{$t("d.l7d")}</H>
<section class="flex flex-wrap items-center justify-center max-w-xs">
  {#each screenTimeHistory as session}
    <div class="text-center">
      <PieChart className="w-20 h-20" maxValue={maxScreenTime} data={[{ color: "#3772FF", value: session.minutes }]}>
        <div class="wh-full bg-green-light text-green-dark flex items-center justify-center">
          <Icon d={session.status ? mdiCheck : mdiClose} className="fill-current w-32" />
        </div>
      </PieChart>
      <p>{session.day}</p>
    </div>
  {/each}
</section>
