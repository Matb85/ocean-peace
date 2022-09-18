<script lang="ts">
  export let deleteUrl = "";
  export let label = "";
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
  import Link from "$lib/Link.svelte";
</script>

<H className="!mt-32" thin>Danger zone</H>
<hr class="border-0 border-b-2 border-gray w-9/12" />

<Link href={deleteUrl} className="my-4">
  <Button isWarning>{label}</Button>
</Link>

<hr class="border-0 border-b-2 border-gray w-9/12 mb-16" />
