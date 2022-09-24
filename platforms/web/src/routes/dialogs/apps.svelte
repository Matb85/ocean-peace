<!-- allows to select apps -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { AppStatus } from "@redinnlabs/system/Units";

  import FullHeading from "$lib/FullHeading.svelte";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";
  import type { AppIconI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";

  const goBackUrl = SM.dialogs.getProp("backUrl");

  let allApps: AppIconI[] = [];
  let selectedApps: string[] = JSON.parse(SM.dialogs.getProp("apps"));

  onMount(async () => {
    allApps = await Api.getAllAppIcons();
  });

  /** removes or add an app to the selectedApps array
   * @param app the app to toggle
   * @returns {void}
   */
  function toggleApp(app: AppIconI) {
    if (!selectedApps.includes(app.packageName)) {
      selectedApps = [...selectedApps, app.packageName];
    } else {
      selectedApps = selectedApps.filter(x => x != app.packageName);
    }
    SM.dialogs.setProps({ apps: JSON.stringify(selectedApps) });
  }
</script>

<FullHeading>{$t("d.dialog.select_apps")}</FullHeading>

<H thin className="mt-8">{$t("d.dialog.apps")} ({selectedApps.length})</H>

<section class="app-group-con">
  {#each allApps.filter(x => selectedApps.includes(x.packageName)) as app}
    <AppStatus
      on:click={() => toggleApp(app)}
      src={app.iconPath}
      label={app.label}
      isSelected={selectedApps.includes(app.packageName)}
      alt="app icon"
    />
  {/each}
  {#if selectedApps.length <= 0}
    <p>{$t("d.dialog.none")}</p>
  {/if}
</section>

<H thin>{$t("d.dialog.all_apps")} ({allApps.length})</H>

<section class="app-group-con">
  {#each allApps as app}
    <AppStatus
      on:click={() => toggleApp(app)}
      src={app.iconPath}
      label={app.label}
      isSelected={selectedApps.includes(app.packageName)}
      alt="app icon"
    />
  {/each}
</section>

<Link href={goBackUrl} className="fixed bottom-10 z-50 w-10/12">
  <Button isFullWidth>{$t("d.cta.save")}</Button>
</Link>
