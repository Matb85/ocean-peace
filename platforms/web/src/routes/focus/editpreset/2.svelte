<!-- allows to select the icon -->
<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";

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

<FullHeading backHref="/focus/editpreset/1">{SM.action.getProp("type")} preset</FullHeading>

<H thin>Choose Icon</H>

<section class="app-group-con">
  {#each icons as icon}
    <div class="w-20">
      <AppStatus className="scale-[0.75]" src={icon} isSelected={chosen == icon} on:click={() => (chosen = icon)} />
    </div>
  {/each}
</section>

<div class="fixed-bottom-button bg-white rounded-2xl" style:opacity={chosen.length > 0 ? "1" : "0.5"}>
  <Link href={chosen.length > 0 ? "/focus/editpreset/3" : ""}>
    <Button isFullWidth>next</Button>
  </Link>
</div>
