<script lang="ts">
  export let deleteUrl = "";
  export let label = "";
  import { Button } from "@redinnlabs/system/Elements";
  import H from "$lib/H.svelte";
</script>

<H className="!mt-32" thin>Danger zone</H>
<hr class="border-0 border-b-2 border-gray w-9/12" />

<a sveltekit:prefetch href={deleteUrl} class="my-4">
  <Button isWarning>{label}</Button>
</a>

<hr class="border-0 border-b-2 border-gray w-9/12 mb-16" />
