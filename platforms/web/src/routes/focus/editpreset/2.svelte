<!-- allows to select the icon -->
<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import { Button, H } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";

  const icons = [
    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",

    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",

    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",

    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-my-time.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
    "/presets/icon-studying.svg",
    "/presets/icon-working.svg",
  ];

  let chosen = SM.preset.getProp("icon");
  $: SM.preset.setProp("icon", chosen);
</script>

<FullHeading backHref="/focus/editpreset/1">{$t("d.preset." + SM.action.getProp("type"))}</FullHeading>

<H thin>{$t("d.preset.choose_icon")}</H>

<section class="app-group-con">
  {#each icons as icon}
    <div class="w-20">
      <AppStatus className="scale-[0.75]" src={icon} isSelected={chosen == icon} on:click={() => (chosen = icon)} />
    </div>
  {/each}
</section>

<div class="fixed-bottom-button bg-white rounded-2xl" style:opacity={chosen.length > 0 ? "1" : "0.5"}>
  <Link href={chosen.length > 0 ? "/focus/editpreset/3" : ""}>
    <Button isFullWidth>{$t("d.cta.con")}</Button>
  </Link>
</div>
