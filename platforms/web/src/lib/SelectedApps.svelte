<script lang="ts">
  import { AppIcon } from "@redinnlabs/system/Units";
</script>

<section class="w-11/12 max-w-sm flex flex-wrap justify-center gap-2">
  {#each Array(7) as _}
    <AppIcon src="/instagram.svg" label="Instagram" alt="app icon" />
  {/each}
</section>
