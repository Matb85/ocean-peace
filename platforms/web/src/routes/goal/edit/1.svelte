<!-- allows to set the name and select apps & websites -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";

  import SelectedApps from "$lib/SelectedApps.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";
  import Link from "$lib/Link.svelte";
  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI } from "$schema";
  import SM from "$lib/sessionManager";
  import { t } from "$lib/i18n";

  let selectedApps: AppIconI[] = [];
  onMount(async () => {
    SM.dialogs.setProp("backUrl", "/goal/edit/1");
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.getProp("apps")));
  });

  let name = SM.goal.getProp("name");
  $: SM.goal.setProp("name", name);
</script>

<FullHeading backHref="/dialogs/cancel">
  {$t("d.goal." + SM.action.getProp("type"))}
</FullHeading>

<H thin>{$t("d.goal.name")}</H>
<div class="w-11/12">
  <TextInput placeholder={$t("d.goal.name_p")} bind:value={name} />
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
  <Link href={selectedApps.length > 0 && name.length > 0 ? "/goal/edit/2" : ""}>
    <Button isFullWidth>next</Button>
  </Link>
</div>
