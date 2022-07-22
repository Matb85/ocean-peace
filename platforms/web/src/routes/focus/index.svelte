<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import W from "@redinnlabs/system/assets/icon-working.svg";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  import SM from "$lib/sessionManager";
  import { beforeNavigate } from "$app/navigation";
  beforeNavigate(({ to }) => {
    if (to.pathname == "/focus/editpreset/1") {
      SM.preset.id = "goal" + Date.now();
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
  {#each Array(3) as _, i}
    <a sveltekit:prefetch href="/focus/preset">
      <Preset src={W} label="Bottom text {i}" />
    </a>
  {/each}
  <a sveltekit:prefetch href="/focus/editpreset/1">
    <Preset src={A} noShadowWrapper />
  </a>
</div>

<H thin>Schedule</H>

<div class="card-flex-col">
  {#each Array(3) as _}
    <Schedule src={W} title="Event example" />
  {/each}
  <Button secondary>Add a Rule</Button>
</div>
