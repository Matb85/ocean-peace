<!-- allows to go back or return to editing -->
<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  import { t } from "$lib/i18n";
  const data = SM.action.getProps("continueUrl", "backUrl", "type");
</script>

<H tag={3} thin className="absolute top-1/4">{$t("d.danger.cancel")} {$t("d.danger." + data.type + "ing")}?</H>
<div class="w-3/4 absolute top-2/3 flex flex-col gap-4">
  <Link href={data.continueUrl}>
    <Button isFullWidth>{$t("d.danger.no_con")}</Button>
  </Link>
  <Link href={data.backUrl}>
    <Button secondary isFullWidth>{$t("d.danger.yes_cancel")}</Button>
  </Link>
</div>
