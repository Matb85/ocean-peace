<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button } from "@redinnlabs/system/Elements";
  import { ToDo } from "@redinnlabs/system/Units";
  import { CircleChart } from "@redinnlabs/system/Charts";
  import H from "$lib/H.svelte";

  export let presetName: string = "Example";
  export let appsCount: number = 16;
</script>

<H tag={3} className="mt-7">{presetName} Session</H>

<div class="w-3/4 max-w-md">
  <CircleChart className="wh-full" />
  <H tag={6}>
    {appsCount}
    {appsCount > 1 ? "apps" : "app"} available
    <br />
    You will get 25 points for this session
  </H>
</div>

<a sveltekit:prefetch href="/focus/summary" class="mt-4">
  <Button secondary size="small" isWarning>Cancel Session</Button>
</a>

<H thin>Things to do later</H>
<div class="card-flex-col">
  {#each Array(5) as _}
    <ToDo title="Example" info="Example info" />
  {/each}
  <div>
    <Button>Add a todo</Button>
  </div>
</div>
