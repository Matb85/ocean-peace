<script lang="ts">
  import { Icon, Aquarium, H } from "@redinnlabs/system/Elements";
  import { mdiCheck, mdiClose } from "@mdi/js";
  import { PieChart, ChartKey } from "@redinnlabs/system/Charts";
  import Cutout from "$lib/Cutout.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import DangerZone from "$lib/DangerZone.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";
  import { querystring } from "svelte-spa-router";
  import { t } from "$lib/i18n";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import { formatMinutes } from "$lib/utils";

  const goalData = SM.goal.getProps(
    "id",
    "name",
    "limit",
    "activeDays",
    "limitActionType",
    "sessionTime",
    "sessionHistory"
  );
  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.getProp("apps")));
    SM.action.setProps({ type: "edit", backUrl: "/goal?" + $querystring, continueUrl: "/goal/edit/1" });
  });
</script>

<FullHeading tag={3} backHref="/" editHref="/goal/edit/1">{$t("d.goal.goal")}</FullHeading>

<section class="w-full h-80 absolute -z-50 top-0">
  <div class="wh-full block absolute z-10 bg-gradient-to-t from-white" />
  <Aquarium percent={80} />
  <Cutout className="w-full bottom-0 absolute" />
</section>

<H tag={5} thin className="w-9/12 text-center">{goalData?.name || ""}</H>
<section class="w-11/12">
  <PieChart
    className="w-3/4 mx-auto"
    maxValue={parseInt(goalData.limit) * 60 * 1000}
    data={[{ color: "#3772FF", value: parseInt(goalData.sessionTime) }]}
  >
    <div class="wh-full flex flex-col items-center justify-center gap-2">
      <H tag={2}>{formatMinutes((parseInt(goalData.limit) - parseInt(goalData.sessionTime)) / (1000 * 60))} min</H>
      <H tag={3} className="!font-normal">{$t("d.left")}</H>
    </div>
  </PieChart>
  <ChartKey
    className="w-full mt-2 items-center flex-col flex-wrap flex-none"
    data={[
      {
        color: "#3772FF",
        text: "Various",
        bold: formatMinutes(parseInt(goalData.sessionTime) / (1000 * 60)),
      },
      {
        color: "#F8F5FA",
        text: "Time left",
        bold: formatMinutes((parseInt(goalData.limit) - parseInt(goalData.sessionTime)) / (1000 * 60)),
      },
    ]}
  />
</section>

<H thin>{$t("d.l7d")}</H>
<section class="flex flex-wrap items-center justify-center max-w-xs">
  {#each JSON.parse(goalData.sessionHistory) as session}
    <div class="text-center">
      <PieChart
        className="w-20 h-20"
        maxValue={parseInt(goalData.limit) * 60 * 1000}
        data={[{ color: "#3772FF", value: parseInt(session.time) }]}
      >
        <div class="wh-full bg-green-light text-green-dark flex items-center justify-center">
          <Icon d={session.status ? mdiCheck : mdiClose} className="fill-current w-32" />
        </div>
      </PieChart>
      <p>{session.day}</p>
    </div>
  {/each}
</section>

<H thin>{$t("d.dialog.apps")}</H>
<SelectedApps apps={selectedApps} />

<H thin>{$t("d.dialog.web")}</H>
<SelectedWebsites websites={JSON.parse(SM.dialogs.getProp("websites") || "[]")} />

<DangerZone deleteUrl="/goal/delete" label={$t("d.cta.delete") + " " + $t("d.goal.goal").toLowerCase()} />
