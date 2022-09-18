<!-- allows to go back or return to editing -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
</script>

<H tag={3} thin className="absolute top-1/4">Cancel {SM.action.type}ing?</H>
<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <Link href={SM.action.continueUrl}>
    <Button isFullWidth>No, continue</Button>
  </Link>
  <Link href={SM.action.backUrl}>
    <Button secondary isFullWidth>Yes, cancel</Button>
  </Link>
</div>
