<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button } from "@redinnlabs/system/Elements";
  import { ToDo } from "@redinnlabs/system/Units";
  import { CircleChart } from "@redinnlabs/system/Charts";
  import H from "$lib/H.svelte";

  export let presetName: string = "Example";
  export let appsCount: number = 16;
</script>

<!-- C O N T E N T -->
<H tag={3} className="mt-7">{presetName} Session</H>

<div class="w-3/4 max-w-md">
  <CircleChart className="w-full h-full" />
  <H tag={6}>{appsCount} {appsCount > 1 ? "apps" : "app"} available</H>
  <H tag={6}>You will get 25 points for this session</H>
</div>

<a sveltekit:prefetch href="/focus/summary">
  <Button secondary="true" size="small" isWarning="true">Cancel Session</Button>
</a>

<div class="flex flex-col items-center w-11/12 gap-4">
  <H thin>Things to do later</H>
  {#each Array(5) as _, i}
    <ToDo title="Example" info="Example info" />
  {/each}
  <div>
    <Button>Add a todo</Button>
  </div>
</div>
