<script lang="ts">
  import { BackButton, EditButton, Heading } from "@redinnlabs/system/Elements";
  import Link from "./Link.svelte";
  export let backHref = null;
  export let editHref = null;
</script>

<Heading className="mt-5 mx-auto" tag={3}><slot /></Heading>

{#if backHref}
  <Link href={backHref} className="fixed z-50 top-3 left-3">
    <BackButton />
  </Link>
{/if}

{#if editHref}
  <Link href={editHref} className="absolute top-3 right-3">
    <EditButton />
  </Link>
{/if}
