<script lang="ts">
  import { Heading } from "@redinnlabs/system/Elements";

  export let tag = 5;
  export let thin = false;
  export let className = "";
  export let noMargins = false;
</script>

<Heading {tag} className="{noMargins ? '' : 'mt-2 -mb-2 '}text-center{thin ? ' !font-normal' : ''} {className}">
  <slot />
</Heading>
