<!-- displays the summary -->
<script lang="ts">
  import { Icon, H } from "@redinnlabs/system/Elements";
  import { mdiCheck } from "@mdi/js";

  import { fly } from "svelte/transition";
  export let text;
  export let isComplete = false;
</script>

{#if isComplete}
  <section in:fly={{ y: 200, duration: 300 }} class="fixed top-0 left-0 z-50 bg-white w-full h-screen">
    <H tag={3} thin className="mt-32">{text}</H>
    <section
      in:fly={{ x: -400, duration: 1200 }}
      class="mt-16 mx-auto bg-green-light w-1/2 aspect-square rounded-3xl flex justify-center items-center"
    >
      <Icon d={mdiCheck} className="fill-green-dark w-3/4" />
    </section>
  </section>
{/if}
