<script lang="ts">
  import H from "$lib/H.svelte";
  import { Button } from "@redinnlabs/system/Elements";

  import Api from "@redinn/oceanpeace-mobile/api";
  import { goto } from "$app/navigation";
  import SM from "$lib/sessionManager";

  /** delete a given goal
   * @returns {void}
   */
  async function deleteGoal() {
    await Api.deleteGoal(SM.goal.id);
    goto("/");
  }
</script>

<div class="absolute top-1/4 text-center px-4">
  <H tag={3}>Are you sure?</H>
  <H thin className="mt-4">You're about to delete goal<br />{SM.goal.name}</H>
</div>

<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <a sveltekit:prefetch href="/goal?id={SM.goal.id}">
    <Button isFullWidth>No, don't do this</Button>
  </a>
  <Button on:click={deleteGoal} isFullWidth isWarning>Yes, delete it</Button>
</div>
