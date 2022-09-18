<!-- allows to go back or return to editing -->
<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";

  const data = SM.action.getProps("continueUrl", "backUrl", "type");
</script>

<H tag={3} thin className="absolute top-1/4">Cancel {data.type}ing?</H>
<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <Link href={data.continueUrl}>
    <Button isFullWidth>No, continue</Button>
  </Link>
  <Link href={data.backUrl}>
    <Button secondary isFullWidth>Yes, cancel</Button>
  </Link>
</div>
