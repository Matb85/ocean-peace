<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
</script>

<section class="w-11/12 max-w-sm flex flex-wrap justify-center gap-x-2 gap-y-2 mt-4">
  {#each Array(7) as _}
    <AppStatus src="/instagram.svg" label="Instagram" alt="app icon" />
  {/each}
</section>
