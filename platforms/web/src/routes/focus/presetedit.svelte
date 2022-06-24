<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  export let presetName: string = "Example";

  const hours = timeInputConfig.hoursConfig();
  const minutes = timeInputConfig.minutesConfig();
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", { set: val => (h = val), get: () => h });
  Object.defineProperty(minutes, "current", { set: val => (m = val), get: () => m });

  let limit: { value: string };
</script>

<!-- C O N T E N T -->

<!-- svelte-ignore component-name-lowercase -->
<FullHeading backHref="/focus" editHref="">{presetName}</FullHeading>

<H thin>Duration</H>
<RadioInput
  className="flex-wrap justify-center"
  bind:chosen={limit}
  options={[{ value: "Pomodoro" }, { value: "Continues" }, { value: "Stopwatch" }]}
/>
<TimeInput columns={[hours, minutes]} />

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
  <Button
  >Start {presetName}</Button>
</a>
