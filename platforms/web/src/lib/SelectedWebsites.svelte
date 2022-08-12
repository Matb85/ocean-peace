<script lang="ts">
  import { CheckBoxStatic } from "@redinnlabs/system/Elements";
  import { WebsiteTypes } from "$lib/utils/index";
  import { Schedule } from "@redinnlabs/system/Units";

  export let websites = [];
</script>

<div class="card-flex-col">
  {#each websites as web}
    <Schedule src={web.favicon} title={web.label} info={WebsiteTypes[web.type]} alt="">
      <CheckBoxStatic state={true} />
    </Schedule>
  {/each}
  {#if websites.length <= 0}
    <p>0 selected</p>
  {/if}
</div>
