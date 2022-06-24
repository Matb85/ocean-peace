<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import W from "@redinnlabs/system/assets/icon-working.svg";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
</script>

<FullHeading backHref="/">Focus</FullHeading>

<H thin>Presets</H>

<div class="grid grid-cols-2 gap-4">
  {#each Array(3) as _, i}
    <a sveltekit:prefetch href="/focus/presetedit">
      <Preset src={W} label="Bottom text {i}" />
    </a>
  {/each}
  <a sveltekit:prefetch href="/focus/presetedit">
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
