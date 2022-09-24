<script lang="ts">
  import { Icon, Aquarium } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";
  import { PieChart, ChartKey } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import H from "$lib/H.svelte";
  import DangerZone from "$lib/DangerZone.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";
  import { querystring } from "svelte-spa-router";
  import { t } from "$lib/i18n";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  const goalData = SM.goal.getProps("id", "name", "timeMinutes", "timeHours", "activeDays", "limitActionType");
  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.getProp("apps")));
    SM.action.setProps({ type: "edit", backUrl: "/goal?" + $querystring, continueUrl: "/goal/edit/1" });
  });
</script>

<FullHeading backHref="/" editHref="/goal/edit/1">{$t("d.goal.goal")}</FullHeading>

<section class="w-full h-80 absolute -z-50 top-0">
  <div class="wh-full block absolute z-10 bg-gradient-to-t from-white" />
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
</section>

<H tag={5} thin className="w-9/12">{goalData?.name || ""}</H>
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

<H thin>{$t("d.l7d")}</H>
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

<H thin>{$t("d.dialog.apps")}</H>
<SelectedApps apps={selectedApps} />

<H thin>{$t("d.dialog.web")}</H>
<SelectedWebsites websites={JSON.parse(SM.dialogs.getProp("websites") || "[]")} />

<DangerZone deleteUrl="/goal/delete" label={$t("d.cta.delete") + " " + $t("d.goal.goal").toLowerCase()} />
