<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  export let presetName: string = "Example";

  const hoursTime = timeInputConfig.hoursConfig(1);
  const minutesTime = timeInputConfig.minutesConfig(3);
  const focusTime = timeInputConfig.minutesConfig(5);
  const breakTime = timeInputConfig.minutesConfig(1);
  let h: number;
  let m: number;
  h = hoursTime.current;
  m = minutesTime.current;
  Object.defineProperty(hoursTime, "current", { set: val => (h = val), get: () => h });
  Object.defineProperty(minutesTime, "current", { set: val => (m = val), get: () => m });

  let limit = "Pomodoro";
</script>

<!-- C O N T E N T -->

<!-- svelte-ignore component-name-lowercase -->
<FullHeading backHref="/focus" editHref="">{presetName}</FullHeading>

<H thin>Duration</H>
<RadioInput className="flex-wrap justify-center" bind:chosen={limit} options={["Pomodoro", "Continued", "Stopwatch"]} />
{#if limit == "Pomodoro"}
  <div class="flex justify-center gap-16">
    <span>Focus</span>
    <span>Break</span>
  </div>
  <TimeInput columns={[focusTime, breakTime]} />
{:else if limit == "Continued"}
  <div class="flex justify-center gap-16">
    <span>Hours</span>
    <span>Minutes</span>
  </div>
  <TimeInput columns={[hoursTime, minutesTime]} />
{/if}

<H thin>Options</H>
<CheckMultiple
  className="flex-wrap justify-center"
  options={[
    { label: "Hard Limit", isChecked: true },
    { label: "20:20:20 Rule", isChecked: false },
  ]}
/>

<H thin>Soundtrack</H>
<div class="card-flex-col">
  {#each Array(3) as _}
    <SoundTrack title="Example Soundtrack" info="Chose one" />
  {/each}
</div>

<a sveltekit:prefetch href="/focus/session" class="fixed bottom-10">
  <Button>Start {presetName}</Button>
</a>
