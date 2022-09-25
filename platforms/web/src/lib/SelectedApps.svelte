<script lang="ts">
  import { AppStatus } from "@redinnlabs/system/Units";
  import type { AppIconI } from "$schema";
  import { t } from "./i18n";
  export let apps: AppIconI[] = [];
</script>

<section class="app-group-con">
  {#each apps as app}
    <AppStatus src={app.iconPath} label={app.label} alt={app.label} />
  {/each}
  {#if apps.length <= 0}
    <p>{$t("d.dialog.none")}</p>
  {/if}
</section>
