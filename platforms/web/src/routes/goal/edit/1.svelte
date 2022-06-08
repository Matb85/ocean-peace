<script lang="ts">
  import { BackButton, Button, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { SearchBar } from "@redinnlabs/system/Units";
  import { TextInput } from "@redinnlabs/system/Form";
</script>

<!-- C O N T E N T -->
<Aquarium percent={80} />

<Heading tag={3} className="mt-7">Goal - Edit</Heading>

<div class="flex flex-col gap-4 w-10/12">
  <Heading tag={5} className="text-center !font-normal">Name</Heading>
  <TextInput placeholder="Name of your goal" />

  <Heading tag={5} className="text-center !font-normal">Selected apps</Heading>
  <SearchBar placeholder="Search..." />
</div>

<a sveltekit:prefetch href="/goal/edit/2" class="fixed bottom-10 w-11/12"><Button isFullWidth="true">next</Button> </a>
<a sveltekit:prefetch href="/goal" class="absolute top-5 left-3"><BackButton /></a>
