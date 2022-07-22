<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import W from "@redinnlabs/system/assets/icon-working.svg";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  import { onMount } from "svelte";
  import type { PresetI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";

  let allPresets: PresetI[] = [];
  onMount(async () => {
    allPresets = await Api.getAllPresets();
  });

  import SM from "$lib/sessionManager";
  import { beforeNavigate } from "$app/navigation";
  beforeNavigate(({ to }) => {
    if (to.pathname == "/focus/editpreset/1") {
      SM.preset.id = "" + (allPresets.length + 1);
      SM.preset.name = "";
      SM.preset.icon = "";

      SM.action.type = "Add";
      SM.action.backUrl = "/focus";

      SM.selectors.apps = "[]";
    }
  });
</script>

<FullHeading backHref="/">Focus</FullHeading>

<H thin>Presets</H>

<div class="grid grid-cols-2 gap-4">
  {#each allPresets as preset}
    <a sveltekit:prefetch href="/focus/preset?id={preset.id}">
      <Preset src={preset.icon} label={preset.name} />
    </a>
  {/each}
  {#each new Array(Math.abs(4 - allPresets.length)) as _}
    <a sveltekit:prefetch href="/focus/editpreset/1">
      <Preset src={A} noShadowWrapper />
    </a>
  {/each}
</div>

<H thin>Schedule</H>

<div class="card-flex-col">
  {#each Array(3) as _}
    <Schedule src={W} title="Event example" />
  {/each}
  <Button secondary>Add a Rule</Button>
</div>
