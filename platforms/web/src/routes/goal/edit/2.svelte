<script lang="ts">
  import { Button, Heading } from "@redinnlabs/system/Elements";
  import { RadioInput, TimeInput, CheckMultiple, timeInputConfig } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";

  const hours = timeInputConfig.hoursConfig();
  const minutes = timeInputConfig.minutesConfig();
  let h;
  let m;
  h = hours.current;
  m = minutes.current;
  Object.defineProperty(hours, "current", { set: val => (h = val), get: () => h });
  Object.defineProperty(minutes, "current", { set: val => (m = val), get: () => m });

  let type;
  let limit;
</script>

<FullHeading backHref="/goal">Goal - Edit</FullHeading>

<Heading tag={5} className="text-center !font-normal">Select Days</Heading>

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

<Heading tag={5} className="text-center !font-normal">Select Limit</Heading>
<RadioInput bind:chosen={limit} options={[{ value: "Time period" }, { value: "Times opened" }]} />

<TimeInput columns={[hours, minutes]} />

<Heading tag={5} className="text-center !font-normal">Limit Type</Heading>
<RadioInput bind:chosen={type} options={[{ value: "Soft" }, { value: "Hard" }]} />

<a sveltekit:prefetch href="/" class="fixed bottom-10 w-11/12"><Button isFullWidth="true">save</Button> </a>
