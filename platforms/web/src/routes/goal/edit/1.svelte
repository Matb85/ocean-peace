<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
</script>

<FullHeading backHref="/goal">Goal - Edit</FullHeading>

<H thin>Name</H>
<div class="w-11/12">
  <TextInput placeholder="Name of your goal" />
</div>

<H thin>Selected apps</H>

<SelectedApps />

<a sveltekit:prefetch href="/goal/edit/select">
  <Button>Add or edit</Button>
</a>

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 z-50 w-11/12"><Button isFullWidth>next</Button> </a>
