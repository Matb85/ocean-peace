<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  const hours = timeInputConfig.hoursConfig();
  const minutes = timeInputConfig.minutesConfig();
  let h: number;
  let m: number;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", { set: val => (h = val), get: () => h });
  Object.defineProperty(minutes, "current", { set: val => (m = val), get: () => m });

  let type: { value: string };
  let limit: { value: string };
</script>

<FullHeading backHref="/goal">Goal - Edit</FullHeading>

<H thin>Select Days</H>

<CheckMultiple
  className="flex-wrap justify-center max-w-sm"
  options={[
    { label: "Mon", isChecked: true },
    { label: "Tue", isChecked: false },
    { label: "Wed", isChecked: false },
    { label: "Thu", isChecked: false },
    { label: "Fri", isChecked: false },
    { label: "Sat", isChecked: false },
    { label: "Sun", isChecked: false },
  ]}
/>

<H thin>Select Limit</H>
<RadioInput bind:chosen={limit} options={[{ value: "Time period" }, { value: "Times opened" }]} />

<TimeInput columns={[hours, minutes]} />

<H thin>Limit Type</H>
<RadioInput bind:chosen={type} options={[{ value: "Soft" }, { value: "Hard" }]} />

<a sveltekit:prefetch href="/" class="fixed bottom-10 w-11/12"><Button isFullWidth>save</Button> </a>
