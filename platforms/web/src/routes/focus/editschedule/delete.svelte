<script lang="ts">
  import H from "$lib/H.svelte";
  import { Button } from "@redinnlabs/system/Elements";
  import Link from "$lib/Link.svelte";

  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import SM from "$lib/sessionManager";

  /** delete a given schedule
   * @returns {void}
   */
  async function deleteSchedule() {
    await Api.deleteSchedule(SM.schedule.id);
    goTo("/focus");
  }
</script>

<div class="absolute top-1/4 text-center px-4">
  <H tag={3}>Are you sure?</H>
  <H thin className="mt-4">You're about to delete schedule<br />{SM.schedule.name}</H>
</div>

<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <Link href="/focus/schedule?id={SM.schedule.id}">
    <Button isFullWidth>No, don't do this</Button>
  </Link>
  <Button on:click={deleteSchedule} isFullWidth isWarning>Yes, delete it</Button>
</div>
