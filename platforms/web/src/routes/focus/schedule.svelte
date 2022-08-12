<script lang="ts">
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { Preset } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
</script>

<FullHeading backHref="/focus" editHref="/focus/editschedule">Schedule</FullHeading>

<H tag={6} thin>Rule name</H>
<H tag={4} className="-mt-2" thin>LOredim</H>

<H tag={6} thin>Preset for this rule</H>

<Preset src={"/presets/icon-working.svg"} label={"LOredim"} />

<H tag={6} thin>Days active</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse('["days"]') as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Hours active</H>
<H tag={4} className="-mt-2" thin>17:30 - 19:30</H>

<H tag={6} thin>Status</H>
<div class="grayscale">
  <Button isWarning>Not running</Button>
</div>
