<script lang="ts">
  import { BackButton, EditButton, H } from "@redinnlabs/system/Elements";
  import Link from "./Link.svelte";
  import SM from "$lib/sessionManager";

  export let backHref = null;
  export let editHref = null;
  export let tag: 1 | 2 | 3 | 4 | 5 | 6 = 4;

  SM.action.setProp("nativeBackUrl", backHref);
</script>

<H noMargins className="mt-5 mx-auto" {tag}><slot /></H>

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
