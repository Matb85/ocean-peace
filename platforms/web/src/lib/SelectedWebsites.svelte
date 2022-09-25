<script lang="ts">
  import { CheckBoxStatic } from "@redinnlabs/system/Elements";
  import { WebsiteTypes } from "$lib/utils/index";
  import { Schedule } from "@redinnlabs/system/Units";
  import { t } from "./i18n";

  export let websites = [];
</script>

<div class="card-flex-col">
  {#each websites as web}
    <Schedule src={web.favicon} title={web.label} info={WebsiteTypes[web.type]} alt="">
      <CheckBoxStatic state={true} />
    </Schedule>
  {/each}
  {#if websites.length <= 0}
    <p>{$t("d.dialog.none")}</p>
  {/if}
</div>
