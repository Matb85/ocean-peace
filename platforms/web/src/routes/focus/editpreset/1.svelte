<!-- allows to set the name and select apps & websites -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import Link from "$lib/Link.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";
  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import SM from "$lib/sessionManager";
  import { t } from "$lib/i18n";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    SM.dialogs.setProp("backUrl", "/focus/editpreset/1");
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.getProp("apps")));
  });

  let name = SM.preset.getProp("name");
  $: SM.preset.setProp("name", name);
</script>

<FullHeading backHref="/dialogs/cancel">
  {$t("d.preset." + SM.action.getProp("type"))}
</FullHeading>

<H thin>{$t("d.preset.name")}</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" bind:value={name} />
</div>

<H thin>{$t("d.dialog.apps")}</H>

<SelectedApps apps={selectedApps} />

<Link href="/dialogs/apps">
  <Button secondary size="small">{$t("d.cta.edit")}</Button>
</Link>

<H thin>{$t("d.dialog.web")}</H>

<SelectedWebsites websites={JSON.parse(SM.dialogs.getProp("websites"))} />

<Link href="/dialogs/websites">
  <Button secondary size="small">{$t("d.cta.edit")}</Button>
</Link>

<div class="fixed-bottom-button bg-white" style:opacity={selectedApps.length > 0 && name.length > 0 ? "1" : "0.5"}>
  <Link href={selectedApps.length > 0 && name.length > 0 ? "/focus/editpreset/2" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
