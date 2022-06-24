<script lang="ts">
  import { BackButton, EditButton, Heading } from "@redinnlabs/system/Elements";
  export let backHref = "/";
  export let editHref = null;
</script>

<Heading className="mt-5 mx-auto" tag={3}><slot /></Heading>

<a sveltekit:prefetch href={backHref} class="fixed z-50 top-3 left-3">
  <BackButton />
</a>

{#if editHref}
  <a href={editHref} class="absolute top-3 right-3">
    <EditButton />
  </a>
{/if}
