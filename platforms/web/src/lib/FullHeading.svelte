<script lang="ts">
  import { BackButton, Heading } from "@redinnlabs/system/Elements";
  export let href = "/";
</script>

<Heading className="mt-7 mx-auto" tag={3}><slot /></Heading>

<a {href} class="fixed top-5 left-3 min-h-fit min-w-fit">
  <BackButton />
</a>
