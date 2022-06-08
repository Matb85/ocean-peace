<script lang="ts">
  import { Button, Heading, EditButton } from "@redinnlabs/system/Elements";
  import { TimeInput, RadioInput, CheckMultiple } from "@redinnlabs/system/Form";
  import { SoundTrack } from "@redinnlabs/system/Units";
  import FullHeading from "$lib/FullHeading.svelte";

  export let presetName: string = "Example";
</script>

<!-- C O N T E N T -->

<!-- svelte-ignore component-name-lowercase -->
<FullHeading href="/focus">{presetName}</FullHeading>

<div class="grid gap-3 place-items-center w-full">
  <Heading tag={5} className="!font-normal">Duration</Heading>
  <RadioInput
    className="flex-wrap justify-center"
    options={[{ value: "Pomodoro" }, { value: "Continues" }, { value: "Stopwatch" }]}
  />
  <TimeInput />
</div>

<div class="custom_grid">
  <Heading tag={5} className="!font-normal">Options</Heading>
  <CheckMultiple
    className="flex-wrap justify-center"
    options={[
      { label: "Hard Limit", isChecked: true },
      { label: "20:20:20 Rule", isChecked: false },
    ]}
  />
</div>

<div class="custom_grid">
  <Heading tag={5} className="!font-normal mt-3">Soundtrack</Heading>
  {#each Array(3) as _, i}
    <SoundTrack title="Example Soundtrack" info="Chose one" />
  {/each}
</div>

<a sveltekit:prefetch href="/focus/session" class="fixed bottom-10 min-h-fit min-w-fit">
  <Button>Start {presetName}</Button>
</a>

<div class="absolute top-5 right-3 min-h-fit min-w-fit">
  <EditButton />
</div>

<style lang="postcss">
  .custom_grid {
    @apply flex flex-col items-center gap-3
            place-items-center
            w-11/12 mx-auto;
  }
</style>
