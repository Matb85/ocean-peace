<script lang="ts">
  import H from "$lib/H.svelte";
  import { Button } from "@redinnlabs/system/Elements";

  import Api from "@redinn/oceanpeace-mobile/api";
  import { goto } from "$app/navigation";
  import SM from "$lib/sessionManager";

  /** delete a given preset
   * @returns {void}
   */
  async function deletePreset() {
    await Api.deletePreset(SM.preset.id);
    goto("/focus");
  }
</script>

<div class="absolute top-1/4 text-center px-4">
  <H tag={3}>Are you sure?</H>
  <H thin className="mt-4">You're about to delete preset<br />{SM.preset.name}</H>
</div>

<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <a sveltekit:prefetch href="/focus/preset?id={SM.preset.id}">
    <Button isFullWidth>No, don't do this</Button>
  </a>
  <Button on:click={deletePreset} isFullWidth isWarning>Yes, delete it</Button>
</div>
