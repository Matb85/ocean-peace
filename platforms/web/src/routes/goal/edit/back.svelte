<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
</script>

<H tag={3} thin className="absolute top-1/4">Cancel {sessionStorage.getItem("edit_goal_action_type")}ing?</H>
<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <a sveltekit:prefetch href="./1">
    <Button isFullWidth>Stay</Button>
  </a>
  <a sveltekit:prefetch href={sessionStorage.getItem("edit_goal_action_back")}>
    <Button secondary isFullWidth>Go back</Button>
  </a>
</div>
