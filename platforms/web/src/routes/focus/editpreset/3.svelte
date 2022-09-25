<!-- displays the summary -->
<script lang="ts">
  import Confirmation from "$lib/Confirmation.svelte";
  import { AppStatus } from "@redinnlabs/system/Units";
  import { Button, H } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";

  import SelectedApps from "$lib/SelectedApps.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";
  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import type { AppIconI, PresetI } from "$schema";
  import SM from "$lib/sessionManager";
  import { t } from "$lib/i18n";

  const presetSM = SM.preset.getProps("id", "name", "icon");
  const dialogsSM = SM.dialogs.getProps("apps", "websites");

  let allowedApps: AppIconI[] = [];

  onMount(async () => {
    allowedApps = await Api.getAppIcons(JSON.parse(dialogsSM.apps));
  });
  /** save preset to a file
   * @returns {void}
   */
  function saveGoal() {
    isComplete = true;
    setTimeout(() => {
      const data: PresetI = {
        id: presetSM.id,
        name: presetSM.name,
        icon: presetSM.icon,
        apps: dialogsSM.apps,
        websites: dialogsSM.apps,
      };
      Api.savePreset(data);
      goTo("/focus");
    }, 1500);
  }
  let isComplete = false;
</script>

<FullHeading backHref="/focus/editpreset/2">{$t("d.summary")}</FullHeading>

<H tag={6} thin>{$t("d.preset.name")}</H>
<H tag={4} className="-mt-2" thin>{presetSM.name}</H>

<H tag={6} thin>{$t("d.preset.icon")}</H>

<div class="shadow-wrapper-sm w-20 rounded-2xl bg-white">
  <AppStatus className="scale-[0.75]" src={presetSM.icon} />
</div>
<H tag={6} thin>{$t("d.dialog.apps")}</H>
<SelectedApps apps={allowedApps} />

<H tag={6} thin>{$t("d.dialog.web")}</H>
<SelectedWebsites websites={JSON.parse(dialogsSM.websites)} />

<div on:click={saveGoal} class="fixed-bottom-button">
  <Button isFullWidth>{$t("d.cta.save")}</Button>
</div>

<Confirmation {isComplete} text={$t("d.preset.saved")} />
