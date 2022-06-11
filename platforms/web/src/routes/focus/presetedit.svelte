<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  export let presetName: string = "Example";
</script>

<!-- C O N T E N T -->

<!-- svelte-ignore component-name-lowercase -->
<FullHeading backHref="/focus" editHref="">{presetName}</FullHeading>

<H thin>Duration</H>
<RadioInput
  className="flex-wrap justify-center"
  options={[{ value: "Pomodoro" }, { value: "Continues" }, { value: "Stopwatch" }]}
/>
<TimeInput />

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
