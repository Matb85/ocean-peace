<!-- displays the ongoing session -->
<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, H } from "@redinnlabs/system/Elements";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import { CircleChart } from "@redinnlabs/system/Charts";

  import { onMount } from "svelte";
  import type { AppIconI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";

  let allowedApps: AppIconI[] = [];

  onMount(async () => {
    Api.getAppIcons(JSON.parse(SM.dialogs.getProp("apps"))).then(data => (allowedApps = data));
    const t: boolean = (await Api.startPomodoro(1000 * 10, 1000 * 1, false, true)).started;
    console.log(t);
  });
</script>

<H tag={3} className="mt-7">{SM.preset.getProp("name")} {$t("d.focus.session")}</H>

<div class="w-3/4 max-w-md">
  <CircleChart className="wh-full" />
  <H tag={6}>
    {$t("d.focus.get", { default: 25 })}
  </H>
</div>

<Link href="/focus/session/2" className="mt-4">
  <Button secondary size="small" isWarning>{$t("d.focus.cancel")}</Button>
</Link>

<H thin>{$t("d.dialog.allowed_apps")}</H>
<SelectedApps apps={allowedApps} />

<H thin>{$t("d.dialog.allowed_web")}</H>
