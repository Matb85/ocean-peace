<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Heading } from "@redinnlabs/system/Elements";
  import { ToDo } from "@redinnlabs/system/Units";
  import { CircleChart } from "@redinnlabs/system/Charts";

  export let presetName: string = "Example";
  export let appsCount: number = 16;
</script>

<!-- C O N T E N T -->
<Heading tag={3} className="mt-7">{presetName} Session</Heading>

<div class="w-3/4">
  <CircleChart className="w-full h-full" />
  <Heading tag={6} className="text-center">{appsCount} {appsCount > 1 ? "apps" : "app"} available</Heading>
  <Heading tag={6} className="text-center !font-normal">You will get 25 points for this session</Heading>
</div>

<div class="cancel">
  <a href="/summary">
    <Button secondary="true" isWarning="true">Cancel Session</Button>
  </a>
</div>

<div class="grid grid-cols-1 items-center place-items-center select-none w-11/12 gap-4">
  <Heading tag={5} className="text-center">Things to do later</Heading>
  {#each Array(5) as _, i}
    <ToDo title="Example" info="Example info" />
  {/each}
  <Button>Add a todo</Button>
</div>
