<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Heading } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import W from "@redinnlabs/system/assets/icon-working.svg";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import Navbar from "../lib/Navbar.svelte";
  import FullHeading from "$lib/FullHeading.svelte";
</script>

<FullHeading href="/">Focus</FullHeading>

<div class="grid grid-cols-2 gap-4">
  <Heading tag={5} className="!font-normal">Presets</Heading>
</div>
<div class="grid grid-cols-2 gap-4">
  {#each Array(3) as _, i}
    <a href="/presetedit">
      <Preset src={W} label="Bottom text {i}" />
    </a>
  {/each}
  <a href="/presetedit">
    <Preset src={A} noShadowWrapper="true" />
  </a>
</div>

<Heading tag={5} className="!font-normal">Schedule</Heading>

<div class="w-11/12 mx-auto grid grid-cols-1 gap-4 place-items-center">
  {#each Array(3) as _, i}
    <Schedule src={W} title="Event example" />
  {/each}
  <Button secondary="true">Add a Rule</Button>
</div>

<Navbar />
