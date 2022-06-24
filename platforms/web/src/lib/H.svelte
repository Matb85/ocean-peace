<script lang="ts">
  import { Heading } from "@redinnlabs/system/Elements";

  export let tag: 1 | 2 | 3 | 4 | 5 | 6 = 5;
  export let thin = false;
  export let className = "";
  export let noMargins = false;
</script>

<Heading {tag} className="{noMargins ? '' : 'mt-4 -mb-1 '}text-center{thin ? ' !font-normal' : ''} {className}">
  <slot />
</Heading>
