<script lang="ts">
  import H from "$lib/H.svelte";
  import { Button } from "@redinnlabs/system/Elements";
  import Link from "$lib/Link.svelte";
  export let callback = () => {};
  export let cancelHref: string;
  export let name: string;
  export let type: string;
</script>

<div class="absolute top-1/4 text-center px-4">
  <H tag={3}>Are you sure?</H>
  <H thin className="mt-4">You're about to delete<br />{type} {name}</H>
</div>

<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <Link href={cancelHref}>
    <Button isFullWidth>No, don't do this</Button>
  </Link>

  <Button on:click={callback} isFullWidth isWarning>Yes, delete it</Button>
</div>
