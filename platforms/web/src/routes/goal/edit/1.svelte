<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { SearchBar } from "@redinnlabs/system/Units";
  import { TextInput } from "@redinnlabs/system/Form";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
</script>

<FullHeading backHref="/goal">Goal - Edit</FullHeading>

<div class="flex flex-col gap-4 w-10/12">
  <H thin>Name</H>
  <TextInput placeholder="Name of your goal" />

  <H thin>Selected apps</H>
  <SearchBar placeholder="Search..." />
</div>

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 w-11/12"><Button isFullWidth="true">next</Button> </a>

<SelectedApps />
