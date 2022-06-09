<script lang="ts">
  import { Heading } from "@redinnlabs/system/Elements";

  export let tag = 5;
  export let thin = false;
  export let className = "";
</script>

<Heading {tag} className="mt-4 mb-1 text-center{thin ? ' !font-normal' : ''} {className}"><slot /></Heading>
